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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.AverageNumberQAsReportRepository;
import gov.epa.cef.web.repository.SubmissionStatusAuditReportRepository;
import gov.epa.cef.web.service.*;
import gov.epa.cef.web.service.dto.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.epa.cef.web.repository.NonPointFuelSubtractionReportRepository;
import gov.epa.cef.web.service.dto.bulkUpload.ControlAssignmentBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ControlBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ControlPathBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ControlPathPollutantBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ControlPollutantBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionFormulaVariableBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionsProcessBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionsUnitBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.FacilityNAICSBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.FacilitySiteBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.FacilitySiteContactBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.OperatingDetailBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ReleasePointApptBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ReleasePointBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ReportingPeriodBulkUploadDto;
import gov.epa.cef.web.service.mapper.GeneratedReportsMapper;
import gov.epa.cef.web.util.CsvBuilder;

@Service
public class GeneratedReportsServiceImpl implements GeneratedReportsService {

    private final NonPointFuelSubtractionReportRepository nonPointFuelSubtractionReportRepo;

    private final AverageNumberQAsReportRepository averageNumberQAsReportRepo;

    private final SubmissionStatusAuditReportRepository submissionStatusAuditReportRepo;

    private final GeneratedReportsMapper generatedReportsMapper;

    private final ControlService controlService;

	private final ControlPathService controlPathService;

	private final EmissionService emissionService;

	private final EmissionsProcessService processService;

	private final EmissionsUnitService emissionsUnitService;

	private final FacilitySiteService facilityService;

	private final FacilitySiteContactService facilitySiteContactService;

	private final OperatingDetailService operatingDetailService;

	private final ReportingPeriodService reportingPeriodService;

	private final ReleasePointService releasePointService;

    private final SubmissionsReviewDasboardService submissionsReviewDashboardService;

	@Autowired
	GeneratedReportsServiceImpl(NonPointFuelSubtractionReportRepository nonPointFuelSubtractionReportRepo,
                                AverageNumberQAsReportRepository averageNumberQAsReportRepo,
                                SubmissionStatusAuditReportRepository submissionStatusAuditReportRepo,
                                GeneratedReportsMapper generatedReportsMapper,
                                ControlService controlService,
                                ControlPathService controlPathService,
                                EmissionService emissionService,
                                EmissionsProcessService processService,
                                EmissionsUnitService emissionsUnitService,
                                FacilitySiteService facilityService,
                                FacilitySiteContactService facilitySiteContactService,
                                OperatingDetailService operatingDetailService,
                                ReportingPeriodService reportingPeriodService,
                                ReleasePointService releasePointService,
                                SubmissionsReviewDasboardService submissionsReviewDashboardService) {
		this.nonPointFuelSubtractionReportRepo = nonPointFuelSubtractionReportRepo;
		this.generatedReportsMapper = generatedReportsMapper;
        this.submissionStatusAuditReportRepo = submissionStatusAuditReportRepo;
		this.controlService = controlService;
		this.controlPathService = controlPathService;
		this.emissionService = emissionService;
		this.processService = processService;
		this.emissionsUnitService = emissionsUnitService;
		this.facilityService = facilityService;
		this.facilitySiteContactService = facilitySiteContactService;
		this.operatingDetailService = operatingDetailService;
		this.reportingPeriodService = reportingPeriodService;
		this.releasePointService = releasePointService;
        this.averageNumberQAsReportRepo = averageNumberQAsReportRepo;
        this.submissionsReviewDashboardService = submissionsReviewDashboardService;
    }

    protected static final long  GIGABYTE = 1024L * 1024L *1024L;

    public List<NonPointFuelSubtractionReportDto> generateNonPointFuelSubtractionReport(int year, String slt) {

        List<NonPointFuelSubtractionReport> result = new ArrayList<NonPointFuelSubtractionReport>();

        result = nonPointFuelSubtractionReportRepo.generateNonPointFuelSubtractionReport(year, slt);

        return generatedReportsMapper.nonPointFuelSubtractionReportToDtos(result);
    }

    public List<AverageNumberQAsReportDto> generateAverageNumberQAsReport(String slt) {

        List<AverageNumberQAsReport> result = new ArrayList<>();

        result = averageNumberQAsReportRepo.generateAverageNumberQAsReport(slt);

        return generatedReportsMapper.averageNumberQAsReportToDtos(result);
    }

    public List<SubmissionsStatusAuditReportDto> generateSubmissionStatusAuditReport(String slt) {

        List<SubmissionStatusAuditReport> result;

        result = submissionStatusAuditReportRepo.generateSubmissionStatusAuditReport(slt);

        return generatedReportsMapper.submissionStatusAuditReportToDtos(result);
    }

    public void generateEmissionsReportComponentCsv(String programSystemCode, Short year, OutputStream outputStream) {

    	List<ControlBulkUploadDto> controlCsvRows = controlService.retrieveControls(programSystemCode, year);
    	List<ControlAssignmentBulkUploadDto> controlAssignCsvRows = this.controlService.retrieveControlAssignments(programSystemCode, year);
    	List<ControlPollutantBulkUploadDto> controlPollutantCsvRows = this.controlService.retrieveControlPollutants(programSystemCode, year);
    	List<ControlPathBulkUploadDto> controlPathCsvRows = this.controlPathService.retrieveControlPaths(programSystemCode, year);
    	List<ControlPathPollutantBulkUploadDto> controlPathPollutantCsvRows = this.controlPathService.retrieveControlPathPollutants(programSystemCode, year);
    	List<EmissionBulkUploadDto> emissionCsvRows = this.emissionService.retrieveEmissions(programSystemCode, year);
    	List<EmissionFormulaVariableBulkUploadDto> emissionFormulaVarCsvRows = this.emissionService.retrieveEmissionFormulaVariables(programSystemCode, year);
    	List<EmissionsProcessBulkUploadDto> emissionsProcessCsvRows = this.processService.retrieveEmissionsProcesses(programSystemCode, year);
    	List<EmissionsUnitBulkUploadDto> emissionsUnitCsvRows = this.emissionsUnitService.retrieveEmissionsUnits(programSystemCode, year);
    	List<FacilitySiteBulkUploadDto> facilitySiteCsvRows = this.facilityService.retrieveFacilities(programSystemCode, year);
    	List<FacilityNAICSBulkUploadDto> facilityNaicsCsvRows = this.facilityService.retrieveFacilityNaics(programSystemCode, year);
    	List<FacilitySiteContactBulkUploadDto> FacilitySiteContactCsvRows = this.facilitySiteContactService.retrieveFacilitySiteContacts(programSystemCode, year);
    	List<OperatingDetailBulkUploadDto> opDetailsCsvRows = this.operatingDetailService.retrieveOperatingDetails(programSystemCode, year);
    	List<ReportingPeriodBulkUploadDto> reportingPeriodCsvRows = this.reportingPeriodService.retrieveReportingPeriods(programSystemCode, year);
    	List<ReleasePointBulkUploadDto> releasePointCsvRows = this.releasePointService.retrieveReleasePoints(programSystemCode, year);
    	List<ReleasePointApptBulkUploadDto> releasePointApptCsvRows = this.releasePointService.retrieveReleasePointAppts(programSystemCode, year);

        List<CsvBuilder<?>> csvList = new ArrayList<> ();
    	csvList.add(new CsvBuilder<ControlBulkUploadDto>(ControlBulkUploadDto.class, controlCsvRows));
    	csvList.add(new CsvBuilder<ControlAssignmentBulkUploadDto>(ControlAssignmentBulkUploadDto.class, controlAssignCsvRows));
    	csvList.add(new CsvBuilder<ControlPollutantBulkUploadDto>(ControlPollutantBulkUploadDto.class, controlPollutantCsvRows));
    	csvList.add(new CsvBuilder<ControlPathBulkUploadDto>(ControlPathBulkUploadDto.class, controlPathCsvRows));
    	csvList.add(new CsvBuilder<ControlPathPollutantBulkUploadDto>(ControlPathPollutantBulkUploadDto.class, controlPathPollutantCsvRows));
    	csvList.add(new CsvBuilder<EmissionBulkUploadDto>(EmissionBulkUploadDto.class, emissionCsvRows));
    	csvList.add(new CsvBuilder<EmissionFormulaVariableBulkUploadDto>(EmissionFormulaVariableBulkUploadDto.class, emissionFormulaVarCsvRows));
    	csvList.add(new CsvBuilder<EmissionsProcessBulkUploadDto>(EmissionsProcessBulkUploadDto.class, emissionsProcessCsvRows));
    	csvList.add(new CsvBuilder<EmissionsUnitBulkUploadDto>(EmissionsUnitBulkUploadDto.class, emissionsUnitCsvRows));
    	csvList.add(new CsvBuilder<FacilitySiteBulkUploadDto>(FacilitySiteBulkUploadDto.class, facilitySiteCsvRows));
    	csvList.add(new CsvBuilder<FacilityNAICSBulkUploadDto>(FacilityNAICSBulkUploadDto.class, facilityNaicsCsvRows));
    	csvList.add(new CsvBuilder<FacilitySiteContactBulkUploadDto>(FacilitySiteContactBulkUploadDto.class, FacilitySiteContactCsvRows));
    	csvList.add(new CsvBuilder<OperatingDetailBulkUploadDto>(OperatingDetailBulkUploadDto.class, opDetailsCsvRows));
    	csvList.add(new CsvBuilder<ReportingPeriodBulkUploadDto>(ReportingPeriodBulkUploadDto.class, reportingPeriodCsvRows));
    	csvList.add(new CsvBuilder<ReleasePointBulkUploadDto>(ReleasePointBulkUploadDto.class, releasePointCsvRows));
    	csvList.add(new CsvBuilder<ReleasePointApptBulkUploadDto>(ReleasePointApptBulkUploadDto.class, releasePointApptCsvRows));

        try (ZipOutputStream zos = new ZipOutputStream(outputStream)) {

        	csvList.forEach(csv -> {
        		try {
        			ZipEntry zipEntry = new ZipEntry(year+"_"+programSystemCode+"_"+csv.fileName());
					zos.putNextEntry(zipEntry);
					writeCsv(csv, zos);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	});

        	zos.closeEntry();
            zos.finish();

        } catch (IOException e) {
        	throw new IllegalStateException(e);
		}
    }

    public CsvBuilder<ControlBulkUploadDto> generateEmissionsReportControlsCsv(String programSystemCode, Short year) {
    	List<ControlBulkUploadDto> csvRows = this.controlService.retrieveControls(programSystemCode, year);
    	CsvBuilder<ControlBulkUploadDto> csvBuilder = new CsvBuilder<ControlBulkUploadDto>(ControlBulkUploadDto.class, csvRows);
    	return csvBuilder;
    }

    public CsvBuilder<ControlAssignmentBulkUploadDto> generateEmissionsReportControlAssignmentsCsv(String programSystemCode, Short year) {
    	List<ControlAssignmentBulkUploadDto> csvRows = this.controlService.retrieveControlAssignments(programSystemCode, year);
    	CsvBuilder<ControlAssignmentBulkUploadDto> csvBuilder = new CsvBuilder<ControlAssignmentBulkUploadDto>(ControlAssignmentBulkUploadDto.class, csvRows);
    	return csvBuilder;
    }

    public CsvBuilder<ControlPollutantBulkUploadDto> generateEmissionsReportControlPollutantsCsv(String programSystemCode, Short year) {
    	List<ControlPollutantBulkUploadDto> csvRows = this.controlService.retrieveControlPollutants(programSystemCode, year);
    	CsvBuilder<ControlPollutantBulkUploadDto> csvBuilder = new CsvBuilder<ControlPollutantBulkUploadDto>(ControlPollutantBulkUploadDto.class, csvRows);
    	return csvBuilder;
    }

    public CsvBuilder<ControlPathBulkUploadDto> generateEmissionsReportControlPathsCsv(String programSystemCode, Short year) {
    	List<ControlPathBulkUploadDto> csvRows = this.controlPathService.retrieveControlPaths(programSystemCode, year);
    	CsvBuilder<ControlPathBulkUploadDto> csvBuilder = new CsvBuilder<ControlPathBulkUploadDto>(ControlPathBulkUploadDto.class, csvRows);
    	return csvBuilder;
    }

    public CsvBuilder<ControlPathPollutantBulkUploadDto> generateEmissionsReportControlPathPollutantsCsv(String programSystemCode, Short year) {
    	List<ControlPathPollutantBulkUploadDto> csvRows = this.controlPathService.retrieveControlPathPollutants(programSystemCode, year);
    	CsvBuilder<ControlPathPollutantBulkUploadDto> csvBuilder = new CsvBuilder<ControlPathPollutantBulkUploadDto>(ControlPathPollutantBulkUploadDto.class, csvRows);
    	return csvBuilder;
    }

    public CsvBuilder<EmissionBulkUploadDto> generateEmissionsReportEmissionsCsv(String programSystemCode, Short year) {
    	List<EmissionBulkUploadDto> csvRows = this.emissionService.retrieveEmissions(programSystemCode, year);
    	CsvBuilder<EmissionBulkUploadDto> csvBuilder = new CsvBuilder<EmissionBulkUploadDto>(EmissionBulkUploadDto.class, csvRows);
    	return csvBuilder;
    }

    public CsvBuilder<EmissionFormulaVariableBulkUploadDto> generateEmissionsReportEmissionFormulaVariablesCsv(String programSystemCode, Short year) {
    	List<EmissionFormulaVariableBulkUploadDto> csvRows = this.emissionService.retrieveEmissionFormulaVariables(programSystemCode, year);
    	CsvBuilder<EmissionFormulaVariableBulkUploadDto> csvBuilder = new CsvBuilder<EmissionFormulaVariableBulkUploadDto>(EmissionFormulaVariableBulkUploadDto.class, csvRows);
    	return csvBuilder;
    }

    public CsvBuilder<EmissionsProcessBulkUploadDto> generateEmissionsReportEmissionsProcessesCsv(String programSystemCode, Short year) {
    	List<EmissionsProcessBulkUploadDto> csvRows = this.processService.retrieveEmissionsProcesses(programSystemCode, year);
    	CsvBuilder<EmissionsProcessBulkUploadDto> csvBuilder = new CsvBuilder<EmissionsProcessBulkUploadDto>(EmissionsProcessBulkUploadDto.class, csvRows);
    	return csvBuilder;
    }

    public CsvBuilder<EmissionsUnitBulkUploadDto> generateEmissionsReportEmissionsUnitsCsv(String programSystemCode, Short year) {
    	List<EmissionsUnitBulkUploadDto> csvRows = this.emissionsUnitService.retrieveEmissionsUnits(programSystemCode, year);
    	CsvBuilder<EmissionsUnitBulkUploadDto> csvBuilder = new CsvBuilder<EmissionsUnitBulkUploadDto>(EmissionsUnitBulkUploadDto.class, csvRows);
    	return csvBuilder;
    }

    public CsvBuilder<FacilitySiteBulkUploadDto> generateEmissionsReportFacilitiesCsv(String programSystemCode, Short year) {
    	List<FacilitySiteBulkUploadDto> csvRows = this.facilityService.retrieveFacilities(programSystemCode, year);
    	CsvBuilder<FacilitySiteBulkUploadDto> csvBuilder = new CsvBuilder<FacilitySiteBulkUploadDto>(FacilitySiteBulkUploadDto.class, csvRows);
    	return csvBuilder;
    }

    public CsvBuilder<FacilityNAICSBulkUploadDto> generateEmissionsReportFacilityNAICSCsv(String programSystemCode, Short year) {
    	List<FacilityNAICSBulkUploadDto> csvRows = this.facilityService.retrieveFacilityNaics(programSystemCode, year);
    	CsvBuilder<FacilityNAICSBulkUploadDto> csvBuilder = new CsvBuilder<FacilityNAICSBulkUploadDto>(FacilityNAICSBulkUploadDto.class, csvRows);
    	return csvBuilder;
    }

    public CsvBuilder<FacilitySiteContactBulkUploadDto> generateEmissionsReportFacilitySiteContactsCsv(String programSystemCode, Short year) {
    	List<FacilitySiteContactBulkUploadDto> csvRows = this.facilitySiteContactService.retrieveFacilitySiteContacts(programSystemCode, year);
    	CsvBuilder<FacilitySiteContactBulkUploadDto> csvBuilder = new CsvBuilder<FacilitySiteContactBulkUploadDto>(FacilitySiteContactBulkUploadDto.class, csvRows);
    	return csvBuilder;
    }

	public CsvBuilder<OperatingDetailBulkUploadDto> generateEmissionsReportOperatingDetailsCsv(String programSystemCode, Short year) {
		List<OperatingDetailBulkUploadDto> csvRows = this.operatingDetailService.retrieveOperatingDetails(programSystemCode, year);
        CsvBuilder<OperatingDetailBulkUploadDto> csvBuilder = new CsvBuilder<OperatingDetailBulkUploadDto>(OperatingDetailBulkUploadDto.class, csvRows);
    	return csvBuilder;
	}

	public CsvBuilder<ReleasePointBulkUploadDto> generateEmissionsReportReleasePointsCsv(String programSystemCode, Short year) {
		List<ReleasePointBulkUploadDto> csvRows = this.releasePointService.retrieveReleasePoints(programSystemCode, year);
    	CsvBuilder<ReleasePointBulkUploadDto> csvBuilder = new CsvBuilder<ReleasePointBulkUploadDto>(ReleasePointBulkUploadDto.class, csvRows);
    	return csvBuilder;
	}

	public CsvBuilder<ReleasePointApptBulkUploadDto> generateEmissionsReportReleasePointApptsCsv(String programSystemCode, Short year) {
		List<ReleasePointApptBulkUploadDto> csvRows = this.releasePointService.retrieveReleasePointAppts(programSystemCode, year);
    	CsvBuilder<ReleasePointApptBulkUploadDto> csvBuilder = new CsvBuilder<ReleasePointApptBulkUploadDto>(ReleasePointApptBulkUploadDto.class, csvRows);
    	return csvBuilder;
	}

	public CsvBuilder<ReportingPeriodBulkUploadDto> generateEmissionsReportReportingPeriodsCsv(String programSystemCode, Short year) {
		List<ReportingPeriodBulkUploadDto> csvRows = this.reportingPeriodService.retrieveReportingPeriods(programSystemCode, year);
    	CsvBuilder<ReportingPeriodBulkUploadDto> csvBuilder = new CsvBuilder<ReportingPeriodBulkUploadDto>(ReportingPeriodBulkUploadDto.class, csvRows);
    	return csvBuilder;
	}

    public CsvBuilder<StandaloneEmissionsProcessReportDto> generateStandaloneEmissionsProcessesCsv(String programSystemCode, Short year) {
        // use operating details because it is the lowest level of details that are needed for the full report
        List<OperatingDetail> operatingDetails = this.operatingDetailService.retrieveAnnualOperatingDetails(programSystemCode, year);
        List<StandaloneEmissionsProcessReportDto> csvRows = this.generatedReportsMapper.processToStandaloneProcessReportDtos(operatingDetails);
        CsvBuilder<StandaloneEmissionsProcessReportDto> csvBuilder = new CsvBuilder<StandaloneEmissionsProcessReportDto>(StandaloneEmissionsProcessReportDto.class, csvRows);
        return csvBuilder;
    }

    public CsvBuilder<FacilityReportingStatusReportDto> generateFacilityReportingStatusesCsv(String programSystemCode, Short year) {
        List<SubmissionsReviewNotStartedDashboardView> reportsList = this.submissionsReviewDashboardService.retrieveFacilityReportingStatusReport(year, programSystemCode);
        List<FacilityReportingStatusReportDto> csvRows = this.generatedReportsMapper.submissionReviewToFacilityReportingStatusReportDtos(reportsList);
        CsvBuilder<FacilityReportingStatusReportDto> csvBuilder = new CsvBuilder<>(FacilityReportingStatusReportDto.class, csvRows);
        return csvBuilder;
    }

    public void writeCsv(CsvBuilder<?> csvBuilder, OutputStream outputStream) {
    	try (InputStream inputStream = new ByteArrayInputStream(csvBuilder.build().toString().getBytes())) {
	    	if(csvBuilder.build().toString().length() > GIGABYTE*2) {
				IOUtils.copyLarge(inputStream, outputStream);
			}else {
				IOUtils.copy(inputStream, outputStream);
			}
	    } catch (Exception ex) {
			throw new RuntimeException("There is an error writing the CSV:\n\n", ex);
		}
    }
}
