/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.api.rest;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;

import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.service.GeneratedReportsService;
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
import gov.epa.cef.web.util.CsvBuilder;

@RestController
@RequestMapping("/api/generatedReports")
public class GeneratedReportsApi {

	private final GeneratedReportsService generatedReportsService;

    @Autowired
    private SecurityService securityService;

	@Autowired
	GeneratedReportsApi(GeneratedReportsService generatedReportsService) {
		this.generatedReportsService = generatedReportsService;
	}

    /**
     * Generate Non-Point Fuel Subtraction Report for the given program system code and inventory year (CSV)
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/nonPointFuelSubtraction/{year}/{programSystemCode}")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    @ResponseBody
    @Operation(summary = "Get non point fuel subtraction report",
        description = "Get non point fuel subtraction report",
        tags = "Generated Reports")
    public ResponseEntity<List<NonPointFuelSubtractionReportDto>> generateNonPointFuelSubtractionReport(@NotNull @PathVariable int year, @NotNull @PathVariable String programSystemCode) {
    	List<NonPointFuelSubtractionReportDto> result = this.generatedReportsService.generateNonPointFuelSubtractionReport(year, programSystemCode);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Generate Average Number of Max QAs Report for the given program system code (CSV)
     * @return
     */
    @GetMapping(value = "/averageNumberQAsReport")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    @ResponseBody
    @Operation(summary = "Get average number QA report",
        description = "Get average number QA report",
        tags = "Generated Reports")
    public ResponseEntity<List<AverageNumberQAsReportDto>> generateAverageNumberQAsReport() {
        String programSystemCode = this.securityService.getCurrentProgramSystemCode();
        List<AverageNumberQAsReportDto> result = this.generatedReportsService.generateAverageNumberQAsReport(programSystemCode);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Generate Average Number of Max QAs Report for the given program system code, or all SLTs (CSV)
     * @param programSystemCode
     * @return
     */
    @GetMapping(value = "/averageNumberQAsReportAdmin/{programSystemCode}")
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN})
    @ResponseBody
    @Operation(summary = "Get average number QA report admin",
        description = "Get average number QA report admin",
        tags = "Generated Reports")
    public ResponseEntity<List<AverageNumberQAsReportDto>> generateAverageNumberQAsReportAdmin(@NotNull @PathVariable String programSystemCode) {
        List<AverageNumberQAsReportDto> result = this.generatedReportsService.generateAverageNumberQAsReport(programSystemCode);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Generate Submission Status Audit Report for the given program system code (CSV)
     * @param programSystemCode
     * @return
     */
    @GetMapping(value = "/submissionStatusAudit/{programSystemCode}")
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN})
    @ResponseBody
    @Operation(summary = "Get submission status audit report",
        description = "Get submission status audit report",
        tags = "Generated Reports")
    public ResponseEntity<List<SubmissionsStatusAuditReportDto>> generateSubmissionStatusAuditReport(@NotNull @PathVariable String programSystemCode) {
        List<SubmissionsStatusAuditReportDto> result = this.generatedReportsService.generateSubmissionStatusAuditReport(programSystemCode);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /***
     * Generate zip file containing csv reports showing each component based on the given program system code and inventory year
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/annualEmissionsReportData/all/{year}/{programSystemCode}")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    @Operation(summary = "Get all SLT generated reports",
        description = "Get all SLT generated reports",
        tags = "Generated Reports")
    public ResponseEntity<StreamingResponseBody> getSltAll(@PathVariable String programSystemCode, @PathVariable Short year) {

    	return ResponseEntity.ok()
    			.contentType(MediaType.parseMediaType("application/zip"))
    			.header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + year + " Annual Emission Reporting Data" + "\"")
    			.body(outputStream -> {
    				this.generatedReportsService.generateEmissionsReportComponentCsv(programSystemCode, year, outputStream);
    			});
    }

    /***
     * Generate report showing all of the controls based on the given program system code and inventory year (CSV)
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/annualEmissionsReportData/controls/{year}/{programSystemCode}")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    @Operation(summary = "Get SLT generated report controls",
        description = "Get SLT generated report controls",
        tags = "Generated Reports")
    public ResponseEntity<StreamingResponseBody> getSltControls(@PathVariable String programSystemCode, @PathVariable Short year) {

    	CsvBuilder<ControlBulkUploadDto> csvBuilder = this.generatedReportsService.generateEmissionsReportControlsCsv(programSystemCode, year);

    	return ResponseEntity.ok()
    			.contentType(MediaType.parseMediaType("application/csv"))
    			.header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;  filename=\"%s\"", csvBuilder.fileName()))
    			.body(outputStream -> {
    				this.generatedReportsService.writeCsv(csvBuilder, outputStream);
    			});
    }

    /***
     * Generate report showing all of the control assignments based on the given program system code and inventory year (CSV)
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/annualEmissionsReportData/controlAssignments/{year}/{programSystemCode}")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    @Operation(summary = "Get SLT generated report control assignments",
        description = "Get SLT generated report control assignments",
        tags = "Generated Reports")
    public ResponseEntity<StreamingResponseBody> getSltControlAssignments(@PathVariable String programSystemCode, @PathVariable Short year) {

    	CsvBuilder<ControlAssignmentBulkUploadDto> csvBuilder = this.generatedReportsService.generateEmissionsReportControlAssignmentsCsv(programSystemCode, year);

    	return ResponseEntity.ok()
    			.contentType(MediaType.parseMediaType("application/csv"))
    			.header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;  filename=\"%s\"", csvBuilder.fileName()))
    			.body(outputStream -> {
    				this.generatedReportsService.writeCsv(csvBuilder, outputStream);
    			});
    }

    /***
     * Generate report showing all of the control pollutants based on the given program system code and inventory year (CSV)
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/annualEmissionsReportData/controlPollutants/{year}/{programSystemCode}")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    @Operation(summary = "Get SLT generated report control pollutants",
        description = "Get SLT generated report control pollutants",
        tags = "Generated Reports")
    public ResponseEntity<StreamingResponseBody> getSltControlPollutants(@PathVariable String programSystemCode, @PathVariable Short year) {

    	CsvBuilder<ControlPollutantBulkUploadDto> csvBuilder = this.generatedReportsService.generateEmissionsReportControlPollutantsCsv(programSystemCode, year);

    	return ResponseEntity.ok()
    			.contentType(MediaType.parseMediaType("application/csv"))
    			.header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;  filename=\"%s\"", csvBuilder.fileName()))
    			.body(outputStream -> {
    				this.generatedReportsService.writeCsv(csvBuilder, outputStream);
    			});
    }

    /***
     *
     *  Generate report showing all of the control paths based on the given program system code and inventory year (CSV)
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/annualEmissionsReportData/controlPaths/{year}/{programSystemCode}")
    @Operation(summary = "Get SLT generated report control paths",
        description = "Get SLT generated report control paths",
        tags = "Generated Reports")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    public ResponseEntity<StreamingResponseBody> getSltControlPaths(@PathVariable String programSystemCode, @PathVariable Short year) {

    	CsvBuilder<ControlPathBulkUploadDto> csvBuilder = this.generatedReportsService.generateEmissionsReportControlPathsCsv(programSystemCode, year);

    	return ResponseEntity.ok()
    			.contentType(MediaType.parseMediaType("application/csv"))
    			.header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;  filename=\"%s\"", csvBuilder.fileName()))
    			.body(outputStream -> {
    				this.generatedReportsService.writeCsv(csvBuilder, outputStream);
    			});
    }

    /***
     * Generate report showing all of the control path pollutants based on the given program system code and inventory year (CSV)
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/annualEmissionsReportData/controlPathPollutants/{year}/{programSystemCode}")
    @Operation(summary = "Get SLT generated report control path pollutants",
        description = "Get SLT generated report control path pollutants",
        tags = "Generated Reports")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    public ResponseEntity<StreamingResponseBody> getSltControlPathPollutants(@PathVariable String programSystemCode, @PathVariable Short year) {

    	CsvBuilder<ControlPathPollutantBulkUploadDto> csvBuilder = this.generatedReportsService.generateEmissionsReportControlPathPollutantsCsv(programSystemCode, year);

    	return ResponseEntity.ok()
    			.contentType(MediaType.parseMediaType("application/csv"))
    			.header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;  filename=\"%s\"", csvBuilder.fileName()))
    			.body(outputStream -> {
    				this.generatedReportsService.writeCsv(csvBuilder, outputStream);
    			});
    }

    /***
     * Generate report showing all of the emissions based on the given program system code and inventory year (CSV)
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/annualEmissionsReportData/emissions/{year}/{programSystemCode}")
    @Operation(summary = "Get SLT generated report emissions",
        description = "Get SLT generated report emissions",
        tags = "Generated Reports")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    public ResponseEntity<StreamingResponseBody> getSltEmissions(@PathVariable String programSystemCode, @PathVariable Short year) {

    	CsvBuilder<EmissionBulkUploadDto> csvBuilder = this.generatedReportsService.generateEmissionsReportEmissionsCsv(programSystemCode, year);

    	return ResponseEntity.ok()
    			.contentType(MediaType.parseMediaType("application/csv"))
    			.header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;  filename=\"%s\"", csvBuilder.fileName()))
    			.body(outputStream -> {
    				this.generatedReportsService.writeCsv(csvBuilder, outputStream);
    			});
    }

    /***
     * Generate report showing all of the emission formula variables based on the given program system code and inventory year (CSV)
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/annualEmissionsReportData/eFFormulaVariables/{year}/{programSystemCode}")
    @Operation(summary = "Get SLT generated report emissions formula variables",
        description = "Get SLT generated report emissions formula variables",
        tags = "Generated Reports")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    public ResponseEntity<StreamingResponseBody> getSltEmissionFormulaVariables(@PathVariable String programSystemCode, @PathVariable Short year) {

    	CsvBuilder<EmissionFormulaVariableBulkUploadDto> csvBuilder = this.generatedReportsService.generateEmissionsReportEmissionFormulaVariablesCsv(programSystemCode, year);

    	return ResponseEntity.ok()
    			.contentType(MediaType.parseMediaType("application/csv"))
    			.header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;  filename=\"%s\"", csvBuilder.fileName()))
    			.body(outputStream -> {
    				this.generatedReportsService.writeCsv(csvBuilder, outputStream);
    			});
    }

    /***
     * Generate report showing all of the emissions processes based on the given program system code and inventory year (CSV)
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/annualEmissionsReportData/emissionsProcesses/{year}/{programSystemCode}")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    @Operation(summary = "Get SLT generated report emissions processes",
        description = "Get SLT generated report emissions processes",
        tags = "Generated Reports")
    public ResponseEntity<StreamingResponseBody> getSltEmissionsProcesses(@PathVariable String programSystemCode, @PathVariable Short year) {

    	CsvBuilder<EmissionsProcessBulkUploadDto> csvBuilder = this.generatedReportsService.generateEmissionsReportEmissionsProcessesCsv(programSystemCode, year);

    	return ResponseEntity.ok()
    			.contentType(MediaType.parseMediaType("application/csv"))
    			.header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;  filename=\"%s\"", csvBuilder.fileName()))
    			.body(outputStream -> {
    				this.generatedReportsService.writeCsv(csvBuilder, outputStream);
    			});
    }

    /***
     * Generate report showing all of the emissions units based on the given program system code and inventory year (CSV)
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/annualEmissionsReportData/emissionsUnits/{year}/{programSystemCode}")
    @Operation(summary = "Get SLT generated report emissions units",
        description = "Get SLT generated report emissions units",
        tags = "Generated Reports")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    public ResponseEntity<StreamingResponseBody> getSltEmissionsUnits(@PathVariable String programSystemCode, @PathVariable Short year) {

    	CsvBuilder<EmissionsUnitBulkUploadDto> csvBuilder = this.generatedReportsService.generateEmissionsReportEmissionsUnitsCsv(programSystemCode, year);

    	return ResponseEntity.ok()
    			.contentType(MediaType.parseMediaType("application/csv"))
    			.header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;  filename=\"%s\"", csvBuilder.fileName()))
    			.body(outputStream -> {
    				this.generatedReportsService.writeCsv(csvBuilder, outputStream);
    			});
    }

    /***
     * Generate report showing all of the facilities based on the given program system code and inventory year (CSV)
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/annualEmissionsReportData/facilitySites/{year}/{programSystemCode}")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    @Operation(summary = "Get SLT generated report facilities",
        description = "Get SLT generated report facilities",
        tags = "Generated Reports")
    public ResponseEntity<StreamingResponseBody> getSltFacilities(@PathVariable String programSystemCode, @PathVariable Short year) {

    	CsvBuilder<FacilitySiteBulkUploadDto> csvBuilder = this.generatedReportsService.generateEmissionsReportFacilitiesCsv(programSystemCode, year);

    	return ResponseEntity.ok()
    			.contentType(MediaType.parseMediaType("application/csv"))
    			.header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;  filename=\"%s\"", csvBuilder.fileName()))
    			.body(outputStream -> {
    				this.generatedReportsService.writeCsv(csvBuilder, outputStream);
    			});
    }

    /***
     * Generate report showing all of the facility site NAICS codes based on the given program system code and inventory year (CSV)
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/annualEmissionsReportData/facilityNAICS/{year}/{programSystemCode}")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    @Operation(summary = "Get SLT generated report facility NAICS codes",
        description = "Get SLT generated report facility NAICS codes",
        tags = "Generated Reports")
    public ResponseEntity<StreamingResponseBody> getSltFacilityNaicsCodes(@PathVariable String programSystemCode, @PathVariable Short year) {

    	CsvBuilder<FacilityNAICSBulkUploadDto> csvBuilder = this.generatedReportsService.generateEmissionsReportFacilityNAICSCsv(programSystemCode, year);

    	return ResponseEntity.ok()
    			.contentType(MediaType.parseMediaType("application/csv"))
    			.header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;  filename=\"%s\"", csvBuilder.fileName()))
    			.body(outputStream -> {
    				this.generatedReportsService.writeCsv(csvBuilder, outputStream);
    			});
    }

    /***
     * Generate report showing all of the facility contacts based on the given program system code and inventory year (CSV)
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/annualEmissionsReportData/facilitySiteContacts/{year}/{programSystemCode}")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    @Operation(summary = "Get SLT generated report facility contacts",
        description = "Get SLT generated report facility contacts",
        tags = "Generated Reports")
    public ResponseEntity<StreamingResponseBody> getSltFacilityContacts(@PathVariable String programSystemCode, @PathVariable Short year) {

    	CsvBuilder<FacilitySiteContactBulkUploadDto> csvBuilder = this.generatedReportsService.generateEmissionsReportFacilitySiteContactsCsv(programSystemCode, year);

    	return ResponseEntity.ok()
    			.contentType(MediaType.parseMediaType("application/csv"))
    			.header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;  filename=\"%s\"", csvBuilder.fileName()))
    			.body(outputStream -> {
    				this.generatedReportsService.writeCsv(csvBuilder, outputStream);
    			});
    }

    /***
     * Generate report showing all of the operating details based on the given program system code and inventory year (CSV)
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/annualEmissionsReportData/operatingDetails/{year}/{programSystemCode}")
    @Operation(summary = "Get SLT generated report operating details",
        description = "Get SLT generated report operating details",
        tags = "Generated Reports")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    public ResponseEntity<StreamingResponseBody> getSltOperatingDetails(@PathVariable String programSystemCode, @PathVariable Short year) {

    	CsvBuilder<OperatingDetailBulkUploadDto> csvBuilder = this.generatedReportsService.generateEmissionsReportOperatingDetailsCsv(programSystemCode, year);

    	return ResponseEntity.ok()
    			.contentType(MediaType.parseMediaType("application/csv"))
    			.header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;  filename=\"%s\"", csvBuilder.fileName()))
    			.body(outputStream -> {
    				this.generatedReportsService.writeCsv(csvBuilder, outputStream);
    			});
    }

    /***
     * Generate report showing all of the release points based on the given program system code and inventory year (CSV)
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/annualEmissionsReportData/releasePoints/{year}/{programSystemCode}")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    @Operation(summary = "Get SLT generated report release points",
        description = "Get SLT generated report release points",
        tags = "Generated Reports")
    public ResponseEntity<StreamingResponseBody> getSltReleasePoints(@PathVariable String programSystemCode, @PathVariable Short year) {

    	CsvBuilder<ReleasePointBulkUploadDto> csvBuilder = this.generatedReportsService.generateEmissionsReportReleasePointsCsv(programSystemCode, year);

    	return ResponseEntity.ok()
    			.contentType(MediaType.parseMediaType("application/csv"))
    			.header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;  filename=\"%s\"", csvBuilder.fileName()))
    			.body(outputStream -> {
    				this.generatedReportsService.writeCsv(csvBuilder, outputStream);
    			});
    }

    /***
     * Generate report showing all of the release point apportionments based on the given program system code and inventory year (CSV)
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/annualEmissionsReportData/releasePointApportionments/{year}/{programSystemCode}")
    @Operation(summary = "Get SLT generated report release point apportionments",
        description = "Get SLT generated report release point apportionments",
        tags = "Generated Reports")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    public ResponseEntity<StreamingResponseBody> getSltReleasePointApportionments(@PathVariable String programSystemCode, @PathVariable Short year) {

    	CsvBuilder<ReleasePointApptBulkUploadDto> csvBuilder = this.generatedReportsService.generateEmissionsReportReleasePointApptsCsv(programSystemCode, year);

    	return ResponseEntity.ok()
    			.contentType(MediaType.parseMediaType("application/csv"))
    			.header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;  filename=\"%s\"", csvBuilder.fileName()))
    			.body(outputStream -> {
    				this.generatedReportsService.writeCsv(csvBuilder, outputStream);
    			});
    }

    /***
     * Generate report showing all of the reporting periods based on the given program system code and inventory year (CSV)
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/annualEmissionsReportData/reportingPeriods/{year}/{programSystemCode}")
    @Operation(summary = "Get SLT generated report periods",
        description = "Get SLT generated report periods",
        tags = "Generated Reports")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    public ResponseEntity<StreamingResponseBody> getSltReportingPeriods(@PathVariable String programSystemCode, @PathVariable Short year) {

    	CsvBuilder<ReportingPeriodBulkUploadDto> csvBuilder = this.generatedReportsService.generateEmissionsReportReportingPeriodsCsv(programSystemCode, year);

    	return ResponseEntity.ok()
    			.contentType(MediaType.parseMediaType("application/csv"))
    			.header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;  filename=\"%s\"", csvBuilder.fileName()))
    			.body(outputStream -> {
    				this.generatedReportsService.writeCsv(csvBuilder, outputStream);
    			});
    }

    /***
     * Generate report showing all relevant emissions processes data based on the given program system code and inventory year (CSV)
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/standaloneEmissionsProcesses/{year}/{programSystemCode}")
    @Operation(summary = "Get standalone generated report emissions processes",
        description = "Get standalone generated report emissions processes",
        tags = "Generated Reports")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    public ResponseEntity<StreamingResponseBody> getStandaloneEmissionsProcesses(@PathVariable String programSystemCode, @PathVariable Short year) {

        CsvBuilder<StandaloneEmissionsProcessReportDto> csvBuilder = this.generatedReportsService.generateStandaloneEmissionsProcessesCsv(programSystemCode, year);

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/csv"))
            .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;  filename=\"%s\"", csvBuilder.fileName()))
            .body(outputStream -> {
                this.generatedReportsService.writeCsv(csvBuilder, outputStream);
            });
    }

    /***
     * Generate report showing facility reporting statuses based on the given program system code and inventory year (CSV)
     * @param programSystemCode
     * @param year
     * @return
     */
    @GetMapping(value = "/facilityReportingStatus/{year}/{programSystemCode}")
    @Operation(summary = "Get generated report facility reporting statuses",
        description = "Get generated report facility reporting statuses",
        tags = "Generated Reports")
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    public ResponseEntity<StreamingResponseBody> getFacilityReportingStatuses(@PathVariable String programSystemCode, @PathVariable Short year) {

        CsvBuilder<FacilityReportingStatusReportDto> csvBuilder = this.generatedReportsService.generateFacilityReportingStatusesCsv(programSystemCode, year);

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/csv"))
            .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;  filename=\"%s\"", csvBuilder.fileName()))
            .body(outputStream -> {
                this.generatedReportsService.writeCsv(csvBuilder, outputStream);
            });
    }
}
