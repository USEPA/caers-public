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

import java.util.Collection;
import java.util.List;

import gov.epa.cef.web.domain.FacilitySite;
import gov.epa.cef.web.service.dto.FacilityNAICSDto;
import gov.epa.cef.web.service.dto.FacilityPermitDto;
import gov.epa.cef.web.service.dto.FacilitySiteDto;
import gov.epa.cef.web.service.dto.bulkUpload.FacilityNAICSBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.FacilitySiteBulkUploadDto;

public interface FacilitySiteService {

    /**
     * Create a new facilitySite
     * @param facilitySite
     * @return
     */
	FacilitySiteDto create(FacilitySite facilitySite);

    /**
     * Find facility by ID and emissions report year for NAICS code descriptions
     * @param id
     * @param reportYear
     * @return FacilitySiteDto
     */
    FacilitySiteDto findByIdAndReportYear(Long id, Short reportYear);

    /**
     * Retrieve facility by emissions report
     * @param emissionsReportId
     * @return
     */
    FacilitySiteDto findByReportId(Long emissionsReportId);

    /**
     * Update facility information
     * @param dto
     * @return
     */
    FacilitySiteDto update(FacilitySiteDto dto);

    /**
     * Create Facility NAICS
     * @param dto
     */
    FacilityNAICSDto createNaics(FacilityNAICSDto dto);

    /**
     * Update existing facility NAICS
     * @param dto
     * @return
     */
    FacilityNAICSDto updateNaics(FacilityNAICSDto dto);

    /**
     * Delete Facility NAICS by id
     * @param facilityNaicsId
     */
    void deleteFacilityNaics(Long facilityNaicsId);

    Collection<FacilityPermitDto> retrieveFacilityPermits(Long facilitySiteId);


    /**
     * Transform from DTO to new instance FacilitySite
     * @return
     */
    FacilitySite transform(FacilitySiteDto dto);


    /**
     * Retrieve a list of facility site IDs for the given program system code and emissions report year
     * @param programSystemCode
     * @param year
     * @return
     */
    List<Long> getFacilityIds(String programSystemCode, Short year);


    /**
     * Retrieve a list of facilities for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    List<FacilitySiteBulkUploadDto> retrieveFacilities(String programSystemCode, Short emissionsReportYear);


    /**
     * Retrieve a list of facility NAICS codes for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    List<FacilityNAICSBulkUploadDto> retrieveFacilityNaics(String programSystemCode, Short emissionsReportYear);
}
