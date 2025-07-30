/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service;

import gov.epa.cef.web.service.dto.EmissionsProcessDto;
import gov.epa.cef.web.service.dto.EmissionsProcessSaveDto;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionsProcessBulkUploadDto;

import java.util.List;

public interface EmissionsProcessService {

    /**
     * Create a new Emissions Process
     * @param dto
     * @return
     */
    EmissionsProcessDto create(EmissionsProcessSaveDto dto);

    /**
     * Update an Emissions Process
     * @param dto
     * @return
     */
    EmissionsProcessDto update(EmissionsProcessSaveDto dto);


    /**
     * Update only the SCC for an Emissions Process
     * @param dto
     * @return
     */
    EmissionsProcessDto updateScc(EmissionsProcessSaveDto dto);

    /**
     * Retrieve Emissions Process by its id
     * @param id
     * @return
     */
    EmissionsProcessDto retrieveById(Long id);

    /**
     * Retrieve versions of this process from the last year reported
     * @param id
     * @return
     */
    EmissionsProcessDto retrievePreviousById(Long id);

    /**
     * Retrieve Emissions Processes for a release point
     * @param pointId
     * @return
     */
    List<EmissionsProcessDto> retrieveForReleasePoint(Long pointId);

    /**
     * Retrieve Emissions Processes for an Emissions Unit
     * @param emissionsUnitId
     * @return
     */
    List<EmissionsProcessDto> retrieveForEmissionsUnit(Long emissionsUnitId);

    /**
     * Delete an Emissions Process for a given id
     * @param id
     */
    void delete(Long id);

    /**
     * Retrieve a list of emissions processes for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    List<EmissionsProcessBulkUploadDto> retrieveEmissionsProcesses(String programSystemCode, Short emissionsReportYear);

}
