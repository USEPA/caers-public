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

import gov.epa.cef.web.service.dto.EmissionsUnitDto;
import gov.epa.cef.web.service.dto.SideNavItemDto;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionsUnitBulkUploadDto;

import java.util.List;

public interface EmissionsUnitService {

    /**
     * Retrieve Emissions Unit by its id
     * @param unitId
     * @return
     */
    EmissionsUnitDto retrieveUnitById(Long unitId);

    /**
     * Retrieves Emissions Units for a facility
     * @param facilitySiteId
     * @return
     */
    List<EmissionsUnitDto> retrieveEmissionUnitsForFacility(Long facilitySiteId);
    
    /**
     * Retrieve emissions unit and processes for nav tree for a facility
     * @param facilitySiteId
     * @return 
     */
    List<SideNavItemDto> retrieveEmissionUnitNavForFacility(Long facilitySiteId);
    /**
     * Retrieve versions of this unit from the last year reported
     * @param unitId
     * @return
     */
    EmissionsUnitDto retrievePreviousById(Long unitId);

    /**
     * Delete an Emissions Unit for a given id
     * @param unitId
     */
    void delete(Long unitId);

    /**
     * Create a new Emissions Unit
     * @param dto
     * @return
     */
	EmissionsUnitDto create(EmissionsUnitDto dto);
	
    /**
     * Update an Emissions Unit
     * @param dto
     * @return
     */
    EmissionsUnitDto update(EmissionsUnitDto dto);

    /**
     * Retrieve a list of emissions units for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    List<EmissionsUnitBulkUploadDto> retrieveEmissionsUnits(String programSystemCode, Short emissionsReportYear);
}
