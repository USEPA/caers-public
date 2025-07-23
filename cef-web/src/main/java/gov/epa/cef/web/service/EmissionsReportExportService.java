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

import java.io.OutputStream;
import java.util.List;

import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionsReportBulkUploadDto;
import gov.epa.cef.web.service.dto.json.EmissionsReportJsonDto;
import gov.epa.cef.web.service.dto.json.ReportExportQueryDto;
import gov.epa.cef.web.service.dto.json.ReportInfoJsonDto;

public interface EmissionsReportExportService {

    /**
     * Testing method for generating upload JSON for a report
     *
     * @param reportId
     * @return
     */
    EmissionsReportBulkUploadDto generateBulkUploadDto(Long reportId);

    EmissionsReportBulkUploadDto generateBulkUploadDto(EmissionsReport report);
    
    EmissionsReportJsonDto generateJsonDto(Long reportId);

    EmissionsReportJsonDto generateJsonDto(EmissionsReport report);

    List<EmissionsReportJsonDto> generateJsonExportDto(ReportExportQueryDto criteria);

    List<ReportInfoJsonDto> generateJsonExportSummaryDto(ReportExportQueryDto criteria);

    /**
     * Generate an excel spreadsheet export for a report
     * @param reportId
     * @param outputStream
     */
    void generateExcel(Long reportId, OutputStream outputStream);

}
