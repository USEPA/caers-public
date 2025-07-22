/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.domain.ReportStatus;
import gov.epa.cef.web.domain.ValidationStatus;
import gov.epa.cef.web.exception.NotExistException;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.ReportIdRetriever;
import gov.epa.cef.web.service.EmissionsReportStatusService;
import gov.epa.cef.web.service.dto.EmissionsReportDto;
import gov.epa.cef.web.service.mapper.EmissionsReportMapper;
import gov.epa.cef.web.util.RepoLocator;
import gov.epa.cef.web.service.dto.EisSubmissionStatus;

@Service
public class EmissionsReportStatusServiceImpl implements EmissionsReportStatusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmissionsReportStatusServiceImpl.class);

    @Autowired
    private EmissionsReportRepository erRepo;

    @Autowired
    private EmissionsReportMapper emissionsReportMapper;

    @Autowired
    private RepoLocator repoLocator;


    /**
     * Begin Advanced QA for the specified reports, move from Submitted to Advanced QA
     * @param reportIds
     * @return
     */
    @Override
    public List<EmissionsReportDto> advancedQAEmissionsReports(List<Long> reportIds) {
        return updateEmissionsReportsStatus(reportIds, ReportStatus.ADVANCED_QA);
    }

    /**
     * Approve the specified reports and move to approved
     * @param reportIds
     * @return
     */
    @Override
    public List<EmissionsReportDto> acceptEmissionsReports(List<Long> reportIds) {
        return updateEmissionsReportsStatus(reportIds, ReportStatus.APPROVED);
    }

    /**
     * Reject the specified reports. Sets report status to returned and validation status to unvalidated.
     * @param reportIds
     * @return
     */
    @Override
    public List<EmissionsReportDto> rejectEmissionsReports(List<Long> reportIds) {
        return updateEmissionsReportsStatus(reportIds, ReportStatus.RETURNED, ValidationStatus.UNVALIDATED, EisSubmissionStatus.NotStarted);
    }

    /**
     * Approve the specified semi-annual reports. Sets mid year submission status to approved.
     * @param reportIds
     * @return
     */
    @Override
    public List<EmissionsReportDto> acceptSemiAnnualReports(List<Long> reportIds) {
        return updateMidYearReportsStatus(reportIds, ReportStatus.APPROVED);
    }

    /**
     * Reject the specified semi-annual reports. Sets mid year submission status to returned.
     * @param reportIds
     * @return
     */
    @Override
    public List<EmissionsReportDto> rejectSemiAnnualReports(List<Long>  reportIds) {
        return updateMidYearReportsStatus(reportIds, ReportStatus.RETURNED, ValidationStatus.UNVALIDATED);
    }

    /**
     * Reset report status. Sets report status to in progress and validation status to unvalidated.
     * @param reportIds
     * @return
     */
    @Override
    public List<EmissionsReportDto> resetEmissionsReport(List<Long> reportIds) {
        return updateEmissionsReportsStatus(reportIds, ReportStatus.IN_PROGRESS, ValidationStatus.UNVALIDATED, EisSubmissionStatus.NotStarted);
    }

    /**
     * Find the report for an entity using the provided repository class and reset the report status.
     * Sets report status to in progress and validation status to unvalidated.
     * @param entityIds
     * @param repoClazz
     * @return
     */
    @Override
    public <T extends ReportIdRetriever> List<EmissionsReportDto> resetEmissionsReportForEntity(List<Long> entityIds, Class<T> repoClazz) {

        ReportIdRetriever repo = repoLocator.getReportIdRepository(repoClazz);

        List<Long> reportIds = entityIds.stream()
        .map(id -> {

            return repo.retrieveEmissionsReportById(id)
                .orElseThrow(() -> {

                    String entity = repoClazz.getSimpleName().replace("Repository", "");
                    return new NotExistException(entity, id);
                });
        }).collect(Collectors.toList());

        return updateEmissionsReportsStatus(reportIds, ReportStatus.IN_PROGRESS, ValidationStatus.UNVALIDATED, EisSubmissionStatus.NotStarted);
    }

    /**
     * Update the status of the specified reports
     * @param reportIds
     * @param status
     * @param validationStatus
     * @param eisStatus
     * @return
     */
    private List<EmissionsReportDto> updateEmissionsReportsStatus(List<Long> reportIds, ReportStatus status, ValidationStatus validationStatus, EisSubmissionStatus eisStatus) {

        return StreamSupport.stream(this.erRepo.findAllById(reportIds).spliterator(), false)
            .map(report -> {
                if ((status != null && !status.equals(report.getStatus()))
                        || (validationStatus != null && !validationStatus.equals(report.getValidationStatus()))) {
                    if (status != null) {
                    	if (status.equals(ReportStatus.IN_PROGRESS) && report.isReturnedReport()) {
        					report.setStatus(ReportStatus.RETURNED);
        				} else {
        					report.setStatus(status);
        				}

                    	if (status.equals(ReportStatus.RETURNED)) {
                    		report.setReturnedReport(true);
        				}
                    }
                    if(validationStatus != null) {
                        report.setValidationStatus(validationStatus);
                    }
                    if (eisStatus != null) {
                        report.setEisLastSubmissionStatus(eisStatus);
                    }
                    return this.emissionsReportMapper.toDto(this.erRepo.save(report));
                }
                return this.emissionsReportMapper.toDto(report);
            }).collect(Collectors.toList());

    }

    /**
     * Update the status of the specified reports
     * @param reportIds
     * @param status
     * @return
     */
    private List<EmissionsReportDto> updateEmissionsReportsStatus(List<Long> reportIds, ReportStatus status, ValidationStatus validationStatus) {
        return updateEmissionsReportsStatus(reportIds, status, validationStatus, null);
    }

    /**
     * Update the status of the specified reports
     * @param reportIds
     * @param status
     * @return
     */
    private List<EmissionsReportDto> updateEmissionsReportsStatus(List<Long> reportIds, ReportStatus status) {
        return updateEmissionsReportsStatus(reportIds, status, null, null);
    }

    /**
     * Update the status of the specified reports
     * @param reportIds
     * @param status
     * @return
     */
    private List<EmissionsReportDto> updateMidYearReportsStatus(List<Long> reportIds, ReportStatus status) {
        return updateMidYearReportsStatus(reportIds, status, null);
    }

    /**
     * Update Mid Year Report Submission Status for the specified reports
     * @param reportIds
     * @param midYearSubStatus
     * @param validationStatus
     * @return
     */
    private List<EmissionsReportDto> updateMidYearReportsStatus(List<Long> reportIds, ReportStatus midYearSubStatus, ValidationStatus validationStatus) {

        return StreamSupport.stream(this.erRepo.findAllById(reportIds).spliterator(), false)
            .map(report -> {
                if (midYearSubStatus != null && !midYearSubStatus.equals(report.getMidYearSubmissionStatus())) {
                	if (midYearSubStatus.equals(ReportStatus.IN_PROGRESS) && report.isReturnedReport()) {
    					report.setMidYearSubmissionStatus(ReportStatus.RETURNED);
    				} else {
    					report.setMidYearSubmissionStatus(midYearSubStatus);
    				}
                    if(validationStatus != null && !validationStatus.equals(report.getValidationStatus())) {
                        report.setValidationStatus(validationStatus);
                    }

                    return this.emissionsReportMapper.toDto(this.erRepo.save(report));
                }
                return this.emissionsReportMapper.toDto(report);
            }).collect(Collectors.toList());

    }
}
