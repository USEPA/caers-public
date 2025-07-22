/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
