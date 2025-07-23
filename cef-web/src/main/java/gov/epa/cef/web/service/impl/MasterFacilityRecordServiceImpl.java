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

import java.math.BigDecimal;
import java.util.List;

import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.*;
import gov.epa.cef.web.service.dto.*;
import gov.epa.cef.web.service.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import gov.epa.cef.web.config.SLTBaseConfig;
import gov.epa.cef.web.domain.common.BaseLookupEntity;
import gov.epa.cef.web.util.SLTConfigHelper;
import gov.epa.cef.web.service.MasterFacilityRecordService;

@Service
public class MasterFacilityRecordServiceImpl implements MasterFacilityRecordService {

    @Autowired
    private FacilitySiteRepository facilitySiteRepo;

    @Autowired
    private EmissionsReportRepository emissionsReportRepo;

    @Autowired
    private MasterFacilityRecordRepository mfrRepo;

    @Autowired
    private MasterFacilityRecordMapper mapper;

    @Autowired
    private LookupEntityMapper lookupMapper;
    
    @Autowired
    private NaicsCodeRepository naicsCodeRepo;
    
    @Autowired
    private MasterFacilityNAICSMapper mfNaicsMapper;

    @Autowired
    private FacilityNAICSMapper facilityNaicsMapper;

    @Autowired
    private FacilityPermitMapper permitMapper;
    
    @Autowired
    private MasterFacilityNAICSXrefRepository mfNaicsXrefRepo;
    
    @Autowired
    private FacilityNAICSXrefRepository facilityNaicsXrefRepo;

    @Autowired
    private FacilityPermitRepository permitRepo;

    @Autowired
    private FacilityPermitTypeRepository permitTypeRepo;
    
    @Autowired
    private SLTConfigHelper sltConfigHelper;


    public MasterFacilityRecordDto findById(Long id) {

        return mfrRepo.findById(id)
                .map(this.mapper::toDto)
                .orElse(null);
    }

    public MasterFacilityRecordDto findByEisProgramId(String eisProgramId) {

        return mfrRepo.findByEisProgramId(eisProgramId)
                .map(this.mapper::toDto)
                .orElse(null);
    }

    public List<MasterFacilityRecordDto> findByProgramSystemCode(String programSystemCode) {

        List<MasterFacilityRecord> entities = this.mfrRepo.findByProgramSystemCodeCode(programSystemCode);
        return this.mapper.toDtoList(entities);
    }

    public List<MasterFacilityRecordDto> findByExample(MasterFacilityRecordDto criteria) {

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING).withIgnoreCase();
        return this.mapper.toDtoList(this.mfrRepo.findAll(Example.of(this.mapper.fromDto(criteria), matcher), Sort.by(Direction.ASC, "name")));
    }

    public List<CodeLookupDto> findDistinctProgramSystems() {

        List<BaseLookupEntity> entities = this.mfrRepo.findDistinctProgramSystems();
        return this.lookupMapper.toDtoList(entities);
    }

    public MasterFacilityRecord transformFacilitySite(FacilitySiteDto fs) {

        MasterFacilityRecord mfr = new MasterFacilityRecord();
        this.mapper.updateFromDto(this.mapper.facilitySiteDtoToDto(fs), mfr);
        return mfr;
    }


    public MasterFacilityRecordDto update(MasterFacilityRecordDto dto) {

    	MasterFacilityRecord masterFacilityRecord = mfrRepo.findById(dto.getId()).orElse(null);
        mapper.updateFromDto(dto, masterFacilityRecord);
        MasterFacilityRecord updatedMasterFacilityRecord = mfrRepo.save(masterFacilityRecord);

        //Cascade changes to the master facility record to the facility site for each of the "In Progress" emissions reports
        emissionsReportRepo.findInProgressOrReturnedByMasterFacilityId(updatedMasterFacilityRecord.getId())
            .forEach(report -> report.getFacilitySites()
                .forEach(fs -> {
                	fs.setAgencyFacilityIdentifier(updatedMasterFacilityRecord.getAgencyFacilityIdentifier());
                	fs.setLongitude(updatedMasterFacilityRecord.getLongitude());
                	fs.setLatitude(updatedMasterFacilityRecord.getLatitude());
                    fs.setCity(updatedMasterFacilityRecord.getCity());
                    fs.setCountryCode(updatedMasterFacilityRecord.getCountryCode());
                    fs.setCountyCode(updatedMasterFacilityRecord.getCountyCode());
                    fs.setDescription(updatedMasterFacilityRecord.getDescription());
                    fs.setFacilityCategoryCode(updatedMasterFacilityRecord.getFacilityCategoryCode());
                    fs.setMailingCity(updatedMasterFacilityRecord.getMailingCity());
                    fs.setMailingPostalCode(updatedMasterFacilityRecord.getMailingPostalCode());
                    fs.setMailingStateCode(updatedMasterFacilityRecord.getMailingStateCode());
                    fs.setMailingStreetAddress(updatedMasterFacilityRecord.getMailingStreetAddress());
                    fs.setName(updatedMasterFacilityRecord.getName());
                    fs.setOperatingStatusCode(updatedMasterFacilityRecord.getOperatingStatusCode());
                    fs.setPostalCode(updatedMasterFacilityRecord.getPostalCode());
                    fs.setStateCode(updatedMasterFacilityRecord.getStateCode());
                    fs.setStatusYear(updatedMasterFacilityRecord.getStatusYear());
                    fs.setStreetAddress(updatedMasterFacilityRecord.getStreetAddress());
                    fs.setTribalCode(updatedMasterFacilityRecord.getTribalCode());
                    facilitySiteRepo.save(fs);
                }));

    	MasterFacilityRecordDto result = mapper.toDto(updatedMasterFacilityRecord);

    	return result;
    }

    public MasterFacilityRecordDto create(MasterFacilityRecordDto dto) {
    	MasterFacilityRecord masterFacilityRecord = mapper.fromDto(dto);
        
        MasterFacilityRecord updatedMasterFacilityRecord = mfrRepo.save(masterFacilityRecord);

    	MasterFacilityRecordDto result = mapper.toDto(updatedMasterFacilityRecord);

    	return result;
    }


    public Boolean isDuplicateAgencyId(String agencyFacilityIdentifier, String programSystemCode) {
        List<MasterFacilityRecord> mfr = mfrRepo.findByProgramSystemCodeCodeAndAgencyFacilityIdentifierIgnoreCase(programSystemCode.trim(), agencyFacilityIdentifier.trim());
        return Boolean.valueOf(mfr.size() > 0);
    }
    
    /**
     * Create Master Facility Record NAICS
     * @param dto
     */
    public MasterFacilityNAICSDto createMasterFacilityNaics(MasterFacilityNAICSDto dto) {
    	MasterFacilityNAICSXref mfNaics = mfNaicsMapper.fromDto(dto);
    	
    	MasterFacilityNAICSDto mfNaicsDto = mfNaicsMapper.mfrNAICSXrefToMfrNAICSDto(mfNaicsXrefRepo.save(mfNaics));
    	masterFacilityNaicsToFacilityNaics(mfNaics.getMasterFacilityRecord().getId());
    	return mfNaicsDto;
    }
    
    /**
     * Update Master Facility Record NAICS
     */
    public MasterFacilityNAICSDto updateMasterFacilityNaics(MasterFacilityNAICSDto dto) {
    	MasterFacilityNAICSXref mfNaics = mfNaicsXrefRepo.findById(dto.getId()).orElse(null);
    	mfNaicsMapper.updateFromDto(dto, mfNaics);
    	
    	NaicsCode naicsCode = naicsCodeRepo.findById(Integer.parseInt(dto.getCode())).orElse(null);
    	mfNaics.setNaicsCode(naicsCode);
    	
    	MasterFacilityNAICSDto mfNaicsDto = mfNaicsMapper.mfrNAICSXrefToMfrNAICSDto(mfNaicsXrefRepo.save(mfNaics));
    	masterFacilityNaicsToFacilityNaics(mfNaics.getMasterFacilityRecord().getId());
    	return mfNaicsDto;
    }
    
    /**
     * Delete Master Facility Record NAICS by id
     * @param mfNaicsId
     */
    public void deleteMasterFacilityNaics(Long mfNaicsId) {
    	MasterFacilityNAICSXref mfNaics = mfNaicsXrefRepo.findById(mfNaicsId).orElse(null);
    	mfNaicsXrefRepo.deleteById(mfNaicsId);
    	masterFacilityNaicsToFacilityNaics(mfNaics.getMasterFacilityRecord().getId());
    }
    
    private void masterFacilityNaicsToFacilityNaics(Long mfrId) {
    	
    	List<EmissionsReport> erList = emissionsReportRepo.findInProgressOrReturnedByMasterFacilityId(mfrId);
    	
    	if (!erList.isEmpty()) {
    		erList.forEach(report -> {
	        	SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(report.getProgramSystemCode().getCode());
	        	report.getFacilitySites().forEach(fs -> {
	            	
			        if (Boolean.TRUE.equals(sltConfig.getFacilityNaicsEnabled())) {
			        	facilityNaicsXrefRepo.deleteByFacilitySiteId(fs.getId());
			        	
			        	report.getMasterFacilityRecord().getMasterFacilityNAICS().forEach(masterFacilityNAICS -> {
			        		FacilityNAICSXref facilityNAICS;
			        		facilityNAICS = facilityNaicsMapper.toFacilityNaicsXref(masterFacilityNAICS);
			                facilityNAICS.setFacilitySite(fs);
			                fs.getFacilityNAICS().add(facilityNAICS);
			            });
			        }
			        facilitySiteRepo.save(fs);
	            });
	        });
    	}
    }

    /**
     * Create Master Facility Record Permit
     * @param dto
     */
    public FacilityPermitDto createMasterFacilityPermit(FacilityPermitDto dto) {
        FacilityPermit permit = permitMapper.fromDto(dto);

        FacilityPermitDto permitDto = permitMapper.toDto(permitRepo.save(permit));
        return permitDto;
    }

    /**
     * Update Master Facility Record Permit
     */
    public FacilityPermitDto updateMasterFacilityPermit(FacilityPermitDto dto) {
        FacilityPermit permit = permitRepo.findById(dto.getId()).orElse(null);
        permitMapper.updateFromDto(dto, permit);

        FacilityPermitType permitType = permitTypeRepo.findById(dto.getPermitType().getCode()).orElse(null);
        permit.setPermitType(permitType);


        FacilityPermitDto permitDto = permitMapper.toDto(permitRepo.save(permit));
        return permitDto;
    }

    /**
     * Delete Master Facility Record Permit by id
     * @param permitId
     */
    public void deleteMasterFacilityPermit(Long permitId) {
        permitRepo.deleteById(permitId);
    }
    
    /**
     * Update master facility record with changes made in emission report
     * @param mfr
     * @param fs
     */
    public void updateMasterFacilityRecord(MasterFacilityRecord mfr, FacilitySite fs) {
    	mfr.setFacilityCategoryCode(fs.getFacilityCategoryCode());
    	mfr.setOperatingStatusCode(fs.getOperatingStatusCode());
    	mfr.setStatusYear(fs.getStatusYear());
    	mfr.setTribalCode(fs.getTribalCode());
    	mfr.setMailingStreetAddress(fs.getMailingStreetAddress());
    	mfr.setMailingCity(fs.getMailingCity());
    	mfr.setMailingStateCode(fs.getMailingStateCode());
    	mfr.setMailingPostalCode(fs.getMailingPostalCode());
    	mfr.setDescription(fs.getDescription());
    	mfrRepo.save(mfr);
    }
    
    /**
     * Get lat/long tolerance for EIS ID
     * @param eisId
     * @return
     */
    public BigDecimal getLatLongTolerance(String eisId) {
        return mfrRepo.getLatLongTolerance(eisId);
    }
}
