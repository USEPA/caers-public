/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
