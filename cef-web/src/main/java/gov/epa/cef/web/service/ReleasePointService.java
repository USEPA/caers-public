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

import gov.epa.cef.web.service.dto.ReleasePointApptDto;
import gov.epa.cef.web.service.dto.ReleasePointDto;
import gov.epa.cef.web.service.dto.bulkUpload.ReleasePointApptBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ReleasePointBulkUploadDto;

import java.util.List;

public interface ReleasePointService {
	
	/**
     * Create a new Release Point
     * @param dto
     * @return
     */
	ReleasePointDto create(ReleasePointDto dto);

    /**
     * Retrieve Release Point by id
     * @param releasePointId
     * @return
     */
    ReleasePointDto retrieveById(Long releasePointId);

    /**
     * Retrieve versions of this rp from the last year reported
     * @param releasePointId
     * @return
     */
    ReleasePointDto retrievePreviousById(Long releasePointId);

    /**
     * Retrieve Release Points by facility id
     * @param facilitySiteId
     * @return
     */
    List<ReleasePointDto> retrieveByFacilitySiteId(Long facilitySiteId);
    
    /**
     * Update an existing Release Point by id
     * @param dto
     * @return
     */
    ReleasePointDto update(ReleasePointDto dto);
    
    /**
     * Delete Release Point by id
     * @param releasePointId
     */
    void delete(Long releasePointId);
    
    /**
     * Delete Release Point Apportionment by id
     * @param releasePointApptId
     */
    void deleteAppt(Long releasePointApptId);
    
	/**
     * Create a new Release Point Apportionment
     * @param dto
     * @return
     */
	ReleasePointApptDto createAppt(ReleasePointApptDto dto);
	
    /**
     * Update an existing Release Point Apportionment by id
     * @param dto
     * @return
     */
    ReleasePointApptDto updateAppt(ReleasePointApptDto dto);
    
    /**
     * Retrieve Release Points associated with a control path id
     * @param controlPathId
     * @return
     */
    List<ReleasePointDto> retrieveByControlPathId(Long controlPathId);

    /**
     * Retrieve a list of release points for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    List<ReleasePointBulkUploadDto> retrieveReleasePoints(String programSystemCode, Short emissionsReportYear);

    /**
     * Retrieve a list of release point apportionments for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    List<ReleasePointApptBulkUploadDto> retrieveReleasePointAppts(String programSystemCode, Short emissionsReportYear);
}
