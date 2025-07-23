/*
 * © Copyright 2019 EPA CAERS Project Team
 *
 * This file is part of the Common Air Emissions Reporting System (CAERS).
 *
 * CAERS is free software: you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License as published by the Free Software Foundation, 
 * either version 3 of the License, or (at your option) any later version.
 *
 * CAERS is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with CAERS.  If 
 * not, see <https://www.gnu.org/licenses/>.
*/
package gov.epa.cef.web.service.impl;

import com.google.common.collect.Streams;

import gov.epa.cef.web.client.soap.NodeClient;
import gov.epa.cef.web.client.soap.NodeTransaction;
import gov.epa.cef.web.config.NetworkNodeName;
import gov.epa.cef.web.domain.EisTransactionHistory;
import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.domain.ProgramSystemCode;
import gov.epa.cef.web.exception.NotExistException;
import gov.epa.cef.web.repository.EisTransactionAttachmentRepository;
import gov.epa.cef.web.repository.EisTransactionHistoryRepository;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.ProgramSystemCodeRepository;
import gov.epa.cef.web.service.dto.EisDataCriteria;
import gov.epa.cef.web.service.dto.EisDataListDto;
import gov.epa.cef.web.service.dto.EisDataReportDto;
import gov.epa.cef.web.service.dto.EisDataStatsDto;
import gov.epa.cef.web.service.dto.EisHeaderDto;
import gov.epa.cef.web.service.dto.EisSubmissionStatus;
import gov.epa.cef.web.service.dto.EisTransactionHistoryDto;
import gov.epa.cef.web.service.mapper.EisTransactionMapper;
import gov.epa.cef.web.util.TempFile;
import net.exchangenetwork.schema.header._2.ExchangeNetworkDocumentType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class EisTransmissionServiceImpl {

    private final EmissionsReportRepository reportRepository;

    private final EisTransactionHistoryRepository transactionHistoryRepo;

    private final EisTransactionAttachmentRepository attachmentRepo;

    private final ProgramSystemCodeRepository pscRepo;

    private final EisXmlServiceImpl xmlService;

    private final EisTransactionMapper mapper;

    private final NodeClient nodeClient;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    EisTransmissionServiceImpl(EisXmlServiceImpl xmlService,
                               EmissionsReportRepository reportRepository,
                               EisTransactionHistoryRepository transactionHistoryRepo,
                               EisTransactionAttachmentRepository attachmentRepo,
                               ProgramSystemCodeRepository pscRepo,
                               EisTransactionMapper mapper,
                               NodeClient nodeClient) {

        this.xmlService = xmlService;
        this.reportRepository = reportRepository;
        this.transactionHistoryRepo = transactionHistoryRepo;
        this.attachmentRepo = attachmentRepo;
        this.pscRepo = pscRepo;
        this.mapper = mapper;
        this.nodeClient = nodeClient;
    }

    public EisDataListDto retrieveDataList(Set<Long> emissionReports) {

        EisDataListDto result = new EisDataListDto();

        Streams.stream(this.reportRepository.findAllById(emissionReports))
            .map(new EisDataReportDto.FromEntity())
            .forEach(result);

        return result;
    }

	public EisDataStatsDto retrieveStatInfoByYear(String programSystemCode, Short year) {

        Collection<EisDataStatsDto.EisDataStatusStat> stats = this.reportRepository.findEisDataStatusesByYear(programSystemCode, year);

        Collection<Integer> years = this.reportRepository.findEisDataYears(programSystemCode);

        return new EisDataStatsDto()
            .withStatuses(stats)
            .withAvailableYears(years);
    }

    public EisDataListDto retrieveSubmittableData(EisDataCriteria criteria) {

        Collection<EmissionsReport> reports = criteria.getSubmissionStatus() == null
            ? this.reportRepository.findEisDataByYearAndNotComplete(criteria)
            : this.reportRepository.findEisDataByYearAndStatus(criteria);

        return new EisDataListDto(criteria)
            .withReports(reports.stream()
                .map(new EisDataReportDto.FromEntity())
                .map(report -> {

                    if (report.getLastTransactionId() != null) {
                        report.setAttachment(this.attachmentRepo.findByTransactionHistoryTransactionId(report.getLastTransactionId())
                            .map(attachment -> mapper.attachmentToDto(attachment))
                            .orElse(null));
                    }

                    return report;
                })
                .collect(Collectors.toList()));
    }

    public List<EisTransactionHistoryDto> retrieveTransactionHistory(String programSystemCode) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -7);

        // pull attachments and less than 7 days old separately because query wouldn't work combined
        List<EisTransactionHistory> entities = this.transactionHistoryRepo.findByProgramSystemCodeBeforeDate(programSystemCode, calendar.getTime());
        entities.addAll(this.transactionHistoryRepo.findByProgramSystemCodeWithAttachment(programSystemCode));

        return mapper.historyToDtoList(entities.stream().distinct().collect(Collectors.toList()));

    }

    public void deleteFromTransactionHistory(List<Long> ids) {

        Iterable<EisTransactionHistory> entities = this.transactionHistoryRepo.findAllById(ids);

        this.transactionHistoryRepo.deleteAll(entities);
    }

    public EisDataListDto submitReports(EisHeaderDto eisHeader) {

        EisDataListDto result = new EisDataListDto();

        String transactionId = transferXml(eisHeader);

        Streams.stream(this.reportRepository.findAllById(eisHeader.getEmissionsReports()))
            .peek(report -> {

                report.setEisLastTransactionId(transactionId);
                report.setEisLastSubmissionStatus(eisHeader.getSubmissionStatus());
                report.setEisPassed(false);

                this.reportRepository.save(report);
            })
            .map(new EisDataReportDto.FromEntity())
            .forEach(result);

        return result;
    }

    public EisDataReportDto updateReportComment(long reportId, String comment) {

        EmissionsReport report = this.reportRepository.findById(reportId)
            .orElseThrow(() -> new NotExistException("Emissions Report", reportId));

        report.setEisComments(comment);

        return new EisDataReportDto.FromEntity().apply(this.reportRepository.save(report));
    }
    
    public EisDataReportDto updateReportEisPassedStatus(long reportId, String passed) {

        EmissionsReport report = this.reportRepository.findById(reportId)
            .orElseThrow(() -> new NotExistException("Emissions Report", reportId));

        report.setEisPassed(Boolean.valueOf(passed));
        
        if (report.getEisLastSubmissionStatus() != null && report.getEisLastSubmissionStatus().equals(EisSubmissionStatus.ProdEmissions) && Boolean.valueOf(passed) == true) {
        	report.setEisLastSubmissionStatus(EisSubmissionStatus.Complete);
        } else if (report.getEisLastSubmissionStatus() != null && report.getEisLastSubmissionStatus().equals(EisSubmissionStatus.Complete) && Boolean.valueOf(passed) == false) {
        	report.setEisLastSubmissionStatus(EisSubmissionStatus.ProdEmissions);
        }

        return new EisDataReportDto.FromEntity().apply(this.reportRepository.save(report));
    }

    private String transferXml(EisHeaderDto eisHeader) {

        NodeTransaction transaction;

        ProgramSystemCode psc = this.pscRepo.findById(eisHeader.getProgramSystemCode()).orElseThrow(() -> {

            return new NotExistException("ProgramSystemCode", eisHeader.getProgramSystemCode());
        });

        ExchangeNetworkDocumentType xml = this.xmlService.generateEisDocument(eisHeader);

        try (TempFile tmpFile = TempFile.create("submission" + xml.getId(), ".zip")) {

            try (FileOutputStream fos = new FileOutputStream(tmpFile.getFile());
                 ZipOutputStream zos = new ZipOutputStream(fos)) {

                ZipEntry zipEntry = new ZipEntry("report.xml");
                zos.putNextEntry(zipEntry);

                this.xmlService.writeEisXmlTo(xml, zos);

                zos.closeEntry();
                zos.finish();

            } catch (FileNotFoundException e) {

                throw new IllegalStateException(e);
            } catch (IOException e) {

                throw new IllegalStateException(e);
            }

            transaction = this.nodeClient.submit(NetworkNodeName.eis, "submission" + xml.getId() + ".zip", tmpFile.getFile());

            logger.info(transaction.toString());

            EisTransactionHistory history = new EisTransactionHistory();
            history.setEisSubmissionStatus(eisHeader.getSubmissionStatus());
            history.setSubmitterName(eisHeader.getAuthorName());
            history.setTransactionId(transaction.getTransactionId());
            history.setProgramSystemCode(psc);

            this.transactionHistoryRepo.save(history);

        }

        return transaction.getTransactionId();
    }
}
