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

import java.util.List;

import gov.epa.cef.web.service.dto.FacilitySiteContactDto;
import gov.epa.cef.web.service.dto.bulkUpload.FacilitySiteContactBulkUploadDto;

public interface FacilitySiteContactService {

	/**
     * Create a new Facility Site Contact
     * @param dto
     * @return
     */
	FacilitySiteContactDto create(FacilitySiteContactDto dto);
	
	/**
     * Retrieve Facility Site Contact by id
     * @param id 
     * @return
     */
    FacilitySiteContactDto retrieveById(Long id);

    /**
     * Retrieves Facility Site Contacts for a facility site
     * @param facilitySiteId
     * @return
     */
    List<FacilitySiteContactDto> retrieveForFacilitySite(Long facilitySiteId);
    
    /**
     * Update an existing Facility Site Contact by id
     * @param dto
     * @return
     */
    FacilitySiteContactDto update(FacilitySiteContactDto dto);
    
    /**
     * Delete Facility Site Contact by id
     * @param id
     */
    void delete(Long id);

	List<FacilitySiteContactDto> retrieveInventoryContactsForFacility(Long facilitySiteId);

    /**
     * Retrieve a list of facility contacts for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */

	List<FacilitySiteContactBulkUploadDto> retrieveFacilitySiteContacts(String programSystemCode, Short emissionsReportYear);
}