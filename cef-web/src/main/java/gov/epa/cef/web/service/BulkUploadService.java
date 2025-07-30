/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import gov.epa.cef.web.service.dto.EmissionsReportDto;
import gov.epa.cef.web.service.dto.EmissionsReportStarterDto;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionsReportBulkUploadDto;
import gov.epa.cef.web.util.TempFile;

import java.util.function.Function;

public interface BulkUploadService {

    /**
     * Converts JSON to EmissionsReportUploadDto.
     * @return
     */
    Function<JsonNode, EmissionsReportBulkUploadDto> parseJsonNode(boolean failUnknownProperties);

    /**
     * Save the emissions report to the database.
     *
     * @param bulkEmissionsReport
     * @return
     */
    EmissionsReportDto saveBulkEmissionsReport(EmissionsReportBulkUploadDto bulkEmissionsReport);

    /**
     * Upload, Parse and Save the emissions report to the database.
     *
     * @param metadata
     * @param workbook
     * @return
     */
    EmissionsReportDto saveBulkWorkbook(EmissionsReportStarterDto metadata, TempFile workbook);

    /**
     * Upload, Parse and Save the emissions report to the database.
     * @param metadata
     * @param jsonNode
     * @return
     */
    EmissionsReportDto saveBulkJson(EmissionsReportStarterDto metadata, JsonNode jsonNode);

    EmissionsReportDto saveCaersJson(EmissionsReportStarterDto metadata, JsonNode jsonNode);
}
