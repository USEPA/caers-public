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
