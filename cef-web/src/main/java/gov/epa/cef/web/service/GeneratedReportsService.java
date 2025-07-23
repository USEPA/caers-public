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
