/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
