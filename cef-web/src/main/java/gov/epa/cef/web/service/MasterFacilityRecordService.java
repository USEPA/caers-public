/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service;

import gov.epa.cef.web.domain.FacilitySite;
import gov.epa.cef.web.domain.MasterFacilityRecord;
import gov.epa.cef.web.service.dto.*;

import java.math.BigDecimal;
import java.util.List;

public interface MasterFacilityRecordService {

    /**
     * Find a master facility record by id
     * @param ID for a master facility record
     * @return
     */
    MasterFacilityRecordDto findById(Long id);


    /**
     * Find a master facility record by eisProgramId
     * @param eisProgramId
     * @return
     */
    MasterFacilityRecordDto findByEisProgramId(String eisProgramId);


    /**
     * Retrieve a list of master facility records based on PSC
     * @param programSystemCode
     * @return
     */
    List<MasterFacilityRecordDto> findByProgramSystemCode(String programSystemCode) ;


    /**
     * Convert a facility site record to a master facility record
     * @param facilitySiteDto
     * @return
     */
    MasterFacilityRecord transformFacilitySite(FacilitySiteDto fs) ;


    /**
     * Update an existing masterFacilityRecord
     * @param masterFacilityRecordDto
     * @return
     */
    MasterFacilityRecordDto update(MasterFacilityRecordDto dto);


    /**
     * Create a new masterFacilityRecord
     * @param masterFacilityRecordDto
     * @return
     */
    MasterFacilityRecordDto create(MasterFacilityRecordDto dto);

    
    /**
     * 
     * @param 
     * @return
     */
    List<MasterFacilityRecordDto> findByExample(MasterFacilityRecordDto criteria);


    /**
     * 
     * @param 
     * @return
     */
    List<CodeLookupDto> findDistinctProgramSystems();


    /**
     * 
     * @param agencyFacilityIdentifier
     * @param programSystemCode
     * @return
     */
    Boolean isDuplicateAgencyId(String agencyFacilityIdentifier, String programSystemCode);
    
    /**
     * Create Master Facility Record NAICS
     * @param dto
     */
    MasterFacilityNAICSDto createMasterFacilityNaics(MasterFacilityNAICSDto dto);
    
    /**
     * Update Master Facility Record NAICS
     * @param dto
     */
    MasterFacilityNAICSDto updateMasterFacilityNaics(MasterFacilityNAICSDto dto);
    
    /**
     * Delete Master Facility Record NAICS by id
     * @param mfNaicsId
     */
    void deleteMasterFacilityNaics(Long mfNaicsId);

    /**
     * Create Master Facility Record Permit
     * @param dto
     */
    FacilityPermitDto createMasterFacilityPermit(FacilityPermitDto dto);

    /**
     * Update Master Facility Record Permit
     * @param dto
     */
    FacilityPermitDto updateMasterFacilityPermit(FacilityPermitDto dto);

    /**
     * Delete Master Facility Record Permit by id
     * @param permitId
     */
    void deleteMasterFacilityPermit(Long permitId);
    
    /**
     * Update master facility record with changes made in emission report
     * @param mfr
     * @param fs
     */
    void updateMasterFacilityRecord(MasterFacilityRecord mfr, FacilitySite fs);
    
    /**
     * Get lat/long tolerance for EIS ID
     * @param eisId
     * @return
     */
    BigDecimal getLatLongTolerance(String eisId);

}
