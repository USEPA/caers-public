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
package gov.epa.cef.web.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.epa.cef.web.domain.ContactTypeCode;
import gov.epa.cef.web.domain.FacilitySiteContact;
import gov.epa.cef.web.repository.FacilitySiteContactRepository;
import gov.epa.cef.web.service.FacilitySiteContactService;
import gov.epa.cef.web.service.dto.FacilitySiteContactDto;
import gov.epa.cef.web.service.dto.bulkUpload.FacilitySiteContactBulkUploadDto;
import gov.epa.cef.web.service.mapper.BulkUploadMapper;
import gov.epa.cef.web.service.mapper.FacilitySiteContactMapper;

@Service
public class FacilitySiteContactServiceImpl implements FacilitySiteContactService {

    @Autowired
    private FacilitySiteContactRepository contactRepo;

    @Autowired
    private FacilitySiteContactMapper mapper;

    @Autowired
    private EmissionsReportStatusServiceImpl reportStatusService;
    
    @Autowired
    private LookupServiceImpl lookupService;
    
    @Autowired
    private BulkUploadMapper bulkUploadMapper;
    
    private static final String EMISSIONS_INVENTORY_CONTACT_TYPE_CODE = "EI";
    
    /**
     * Create a new Facility Site Contact from a DTO object
     */
    public FacilitySiteContactDto create(FacilitySiteContactDto dto) {
    	
    	FacilitySiteContact facilityContact = mapper.fromDto(dto);
    	
    	FacilitySiteContactDto result = mapper.toDto(contactRepo.save(facilityContact));
    	reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(result.getId()), FacilitySiteContactRepository.class);
    	return result;
    }
    
    @Override
    public FacilitySiteContactDto retrieveById(Long id) {
        FacilitySiteContact result = contactRepo
                .findById(id)
                .orElse(null);
        return mapper.toDto(result);
    }

    @Override
    public List<FacilitySiteContactDto> retrieveForFacilitySite(Long facilitySiteId) {
        List<FacilitySiteContact> result = contactRepo.findByFacilitySiteId(facilitySiteId);
        return mapper.toDtoList(result);
    }
    
    /**
     * Update an existing Facility Site Contact from a DTO
     */
    public FacilitySiteContactDto update(FacilitySiteContactDto dto) {
    	
    	FacilitySiteContact facilityContact = contactRepo.findById(dto.getId()).orElse(null);
    	mapper.updateFromDto(dto, facilityContact);
    	
    	FacilitySiteContactDto result = mapper.toDto(contactRepo.save(facilityContact));
    	reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(result.getId()), FacilitySiteContactRepository.class);

        return result;
    }
    
    public void delete(Long id) {
        reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(id), FacilitySiteContactRepository.class);
    	contactRepo.deleteById(id);
    }
    
    @Override
    public List<FacilitySiteContactDto> retrieveInventoryContactsForFacility(Long facilitySiteId) {
    	ContactTypeCode eiContactType = lookupService.retrieveContactTypeEntityByCode(EMISSIONS_INVENTORY_CONTACT_TYPE_CODE);
        List<FacilitySiteContact> result = contactRepo.findByFacilitySiteIdAndType(facilitySiteId, eiContactType);
        return mapper.toDtoList(result);
    }


    /**
     * Retrieve a list of facility site contacts for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */  
    public List<FacilitySiteContactBulkUploadDto> retrieveFacilitySiteContacts(String programSystemCode, Short emissionsReportYear) {
    	List<FacilitySiteContact> facilitySiteContact = contactRepo.findByPscAndEmissionsReportYear(programSystemCode, emissionsReportYear);
    	return bulkUploadMapper.facilitySiteContactToDtoList(facilitySiteContact);
    }


}