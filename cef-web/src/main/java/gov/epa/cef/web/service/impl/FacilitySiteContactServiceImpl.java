/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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