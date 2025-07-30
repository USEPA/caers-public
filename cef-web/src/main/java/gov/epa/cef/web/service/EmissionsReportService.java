/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service;

import gov.epa.cef.web.api.rest.EmissionsReportApi.ReviewDTO;
import gov.epa.cef.web.domain.EmissionsReport;

import gov.epa.cef.web.domain.ReportAction;
import gov.epa.cef.web.exception.ApplicationException;
import gov.epa.cef.web.service.dto.EmissionsReportAgencyDataDto;
import gov.epa.cef.web.service.dto.EmissionsReportDto;
import gov.epa.cef.web.service.dto.EmissionsReportStarterDto;
import gov.epa.cef.web.service.dto.ReportChangeDto;
import gov.epa.cef.web.util.ReportCreationContext;
import net.exchangenetwork.wsdl.register.pdf._1.PdfDocumentType;
import net.exchangenetwork.wsdl.register.sign._1.SignatureDocumentType;

import javax.validation.constraints.NotBlank;

import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

public interface EmissionsReportService {

    /**
     * Creates an emissions report from scratch
     * @param reportDto
     * @return
     */
    EmissionsReportDto createEmissionReport(EmissionsReportStarterDto reportDto);

    /**
     * Find reports for a given facility
     * @param masterFacilityRecordId
     * @return
     */
    List<EmissionsReportDto> findByMasterFacilityRecordId(Long masterFacilityRecordId);

    /**
     * Find reports for a given facility and add a new emissions report record in memory for the current year if addReportForCurrentYear is true
     * @param masterFacilityRecordId
     * @param addReportForCurrentReportYear  The current report year will be the current year - 1
     * @param addReportForCurrentCalendarYear  The current calendar year, needed for entering monthly reporting
     * @return
     */
    List<EmissionsReportDto> findByMasterFacilityRecordId(Long masterFacilityRecordId, boolean addReportForCurrentReportYear, boolean addReportForCurrentCalendarYear);

    /**
     * Find report by ID
     * @param id
     * @return
     */
    EmissionsReportDto findById(Long id);

    /**
     * Find report by ID
     * @param id
     * @return
     */
    Optional<EmissionsReport> retrieve(long id);

    /**
     * Find report by facility id and year
     * @return
     */
    Optional<EmissionsReport> retrieveByMasterFacilityRecordIdAndYear(@NotBlank Long masterFacilityRecordId, int year);

    /**
     * Find report dto by facility id and year
     * @return
     */
    EmissionsReportDto retrieveReportByMasterFacilityRecordIdAndYear(Long masterFacilityRecordId, int year);

    /**
     * Find the most recent report for a given facility
     * @param masterFacilityRecordId
     * @return
     */
    EmissionsReportDto findMostRecentByMasterFacilityRecordId(Long masterFacilityRecordId);

    /**
     * Find all agencies with reports and which years they have reports for
     */
    List<EmissionsReportAgencyDataDto> findAgencyReportedYears();

    /**
     * Find all agencies with reports and which years they have monthly reports for
     */
    List<EmissionsReportAgencyDataDto> findAgencyMonthlyReportedYears();

    Boolean readyToCertifyNotification(Long emissionsReportId, boolean isSemiannual);

    String submitToCromerr(Long emissionsReportId, String activityId, boolean isSemiannual) throws ApplicationException;

    /**
     * Create a copy of the emissions report for the current year based on the specified facility and year.  The copy of the report is NOT saved to the database.
     * @param masterFacilityRecordId
     * @param currentReportYear The year of the report that is being created
     * @return
     */
    EmissionsReportDto createEmissionReportCopy(EmissionsReportStarterDto reportDto);

    /**
     * Save the emissions report to the database.
     * @param emissionsReport
     * @return
     */
    EmissionsReportDto saveAndAuditEmissionsReport(EmissionsReport emissionsReport, ReportAction reportAction);

    /**
     * Save the emissions report to the database, update history, and generate report changes
     * @param emissionsReport
     * @param reportAction
     * @param context
     * @return
     */
    EmissionsReportDto saveAndAuditEmissionsReport(EmissionsReport emissionsReport, ReportAction reportAction, ReportCreationContext context);


    List<ReportChangeDto> retrieveChangesForReport(Long reportId);

    /**
     * Delete specified emissions report from database
     * @param id
     */
    void delete(Long id);

    List<EmissionsReportDto> beginAdvancedQAEmissionsReports(List<Long> reportIds);

	List<EmissionsReportDto> acceptEmissionsReports(List<Long> reportIds, String comments);

	List<EmissionsReportDto> rejectEmissionsReports(ReviewDTO reviewDTO);

    /**
     * Update an existing Emissions Report from a DTO
     */
    EmissionsReportDto updateSubmitted(long reportId, boolean submitted);

    List<EmissionsReportDto> acceptSemiAnnualReports(ReviewDTO reviewDTO);

    List<EmissionsReportDto>rejectSemiAnnualReports(ReviewDTO reviewDTO);

    /***
     * Get copy of record document
     * @param activityId
     * @param documentId
     * @return
     */
    SignatureDocumentType getCopyOfRecord(String activityId, String documentId);

    /**
     * Generate CoR PDF
     * @param reportId
     * @param signed
     * @return
     */
    PdfDocumentType generateCopyOfRecord(Long reportId, boolean isSemiannual, boolean signed);

    /***
     * Write file to output stream
     * @param document
     * @return
     */
	void writeFileTo(SignatureDocumentType document, OutputStream outputStream);

    /**
     * Transform XML CoR to HTML and write to output stream
     * @param document
     * @param outputStream
     */
    void transformXmlCor(SignatureDocumentType document, OutputStream outputStream);

    /**
     * Update an existing Emissions Report's max QA counts
     */
    EmissionsReportDto updateMaxNumberOfQAs(long reportId, int numErrors, int numWarnings);

    /**
     * Mark a report as deleted so it is picked up by the report deletion job
     * @return
     */
    void markReportForDeletion(Long id);

    /**
     * Retrieve all reports that need to be deleted
     * @return
     */
    List<EmissionsReport> getReportsToDelete();
}
