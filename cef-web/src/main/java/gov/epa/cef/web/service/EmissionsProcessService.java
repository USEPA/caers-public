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
