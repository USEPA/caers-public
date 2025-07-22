/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service;

import gov.epa.cef.web.service.dto.*;
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

import java.io.OutputStream;
import java.util.List;

public interface GeneratedReportsService {

    /**
     * Generate Non-Point Fuel Subtraction Report
     * @return
     */
    List<NonPointFuelSubtractionReportDto> generateNonPointFuelSubtractionReport(int year, String slt);

    /**
     * Generate Average Number of QAs Report
     * @return
     */
    List<AverageNumberQAsReportDto> generateAverageNumberQAsReport(String slt);

    /**
     * Generate Submission Status Audit Report
     * @return
     */
    List<SubmissionsStatusAuditReportDto> generateSubmissionStatusAuditReport(String slt);

	void generateEmissionsReportComponentCsv(String programSystemCode, Short year, OutputStream outputStream);

	CsvBuilder<ControlBulkUploadDto> generateEmissionsReportControlsCsv(String programSystemCode, Short year);

	CsvBuilder<ControlAssignmentBulkUploadDto> generateEmissionsReportControlAssignmentsCsv(String programSystemCode, Short year);

	CsvBuilder<ControlPollutantBulkUploadDto> generateEmissionsReportControlPollutantsCsv(String programSystemCode, Short year);

	CsvBuilder<ControlPathBulkUploadDto> generateEmissionsReportControlPathsCsv(String programSystemCode, Short year);

	CsvBuilder<ControlPathPollutantBulkUploadDto> generateEmissionsReportControlPathPollutantsCsv(String programSystemCode, Short year);

	CsvBuilder<EmissionBulkUploadDto> generateEmissionsReportEmissionsCsv(String programSystemCode, Short year);

	CsvBuilder<EmissionFormulaVariableBulkUploadDto> generateEmissionsReportEmissionFormulaVariablesCsv(String programSystemCode, Short year);

	CsvBuilder<EmissionsProcessBulkUploadDto> generateEmissionsReportEmissionsProcessesCsv(String programSystemCode, Short year);

	CsvBuilder<EmissionsUnitBulkUploadDto> generateEmissionsReportEmissionsUnitsCsv(String programSystemCode, Short year);

	CsvBuilder<FacilitySiteBulkUploadDto> generateEmissionsReportFacilitiesCsv(String programSystemCode, Short year);

	CsvBuilder<FacilityNAICSBulkUploadDto> generateEmissionsReportFacilityNAICSCsv(String programSystemCode, Short year);

	CsvBuilder<FacilitySiteContactBulkUploadDto> generateEmissionsReportFacilitySiteContactsCsv(String programSystemCode, Short year);

	CsvBuilder<OperatingDetailBulkUploadDto> generateEmissionsReportOperatingDetailsCsv(String programSystemCode, Short year);

	CsvBuilder<ReleasePointBulkUploadDto> generateEmissionsReportReleasePointsCsv(String programSystemCode, Short year);

	CsvBuilder<ReleasePointApptBulkUploadDto> generateEmissionsReportReleasePointApptsCsv(String programSystemCode, Short year);

	CsvBuilder<ReportingPeriodBulkUploadDto> generateEmissionsReportReportingPeriodsCsv(String programSystemCode, Short year);

    CsvBuilder<StandaloneEmissionsProcessReportDto> generateStandaloneEmissionsProcessesCsv(String programSystemCode, Short year);

    CsvBuilder<FacilityReportingStatusReportDto> generateFacilityReportingStatusesCsv(String programSystemCode, Short year);

    void writeCsv(CsvBuilder<?> csvBuilder, OutputStream outputStream);
}
