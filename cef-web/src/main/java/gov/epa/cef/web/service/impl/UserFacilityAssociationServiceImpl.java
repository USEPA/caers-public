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

import java.util.*;
import java.util.stream.Collectors;

import gov.epa.cef.web.security.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Functions;
import com.google.common.base.Strings;

import gov.epa.cdx.shared.security.ApplicationUser;
import gov.epa.cef.web.client.soap.RegisterFacilityClient;
import gov.epa.cef.web.client.soap.RegisterServiceClient;
import gov.epa.cef.web.client.soap.StreamlinedRegistrationServiceClient;
import gov.epa.cef.web.config.CefConfig;
import gov.epa.cef.web.config.SLTBaseConfig;
import gov.epa.cef.web.domain.CdxInboxMessageType;
import gov.epa.cef.web.domain.MasterFacilityRecord;
import gov.epa.cef.web.domain.UserFacilityAssociation;
import gov.epa.cef.web.exception.NotExistException;
import gov.epa.cef.web.repository.MasterFacilityRecordRepository;
import gov.epa.cef.web.repository.UserFacilityAssociationRepository;
import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.service.NotificationService;
import gov.epa.cef.web.service.dto.MasterFacilityRecordDto;
import gov.epa.cef.web.service.dto.UserFacilityAssociationDto;
import gov.epa.cef.web.service.mapper.MasterFacilityRecordMapper;
import gov.epa.cef.web.service.mapper.UserFacilityAssociationMapper;
import gov.epa.cef.web.util.SLTConfigHelper;
import net.exchangenetwork.wsdl.register._1.RegistrationUser;
import net.exchangenetwork.wsdl.register.program_facility._1.ProgramFacility;
import net.exchangenetwork.wsdl.register.streamlined._1.RegistrationNewUserProfile;
import net.exchangenetwork.wsdl.register.streamlined._1.RegistrationRoleType;
import net.exchangenetwork.wsdl.register.streamlined._1.RegistrationUserSearchCriteria;

@Service
public class UserFacilityAssociationServiceImpl {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserFacilityAssociationRepository ufaRepo;

    @Autowired
    private MasterFacilityRecordRepository mfrRepo;

    @Autowired
    private RegisterFacilityClient registerFacilityClient;

    @Autowired
    private RegisterServiceClient registerServiceClient;

    @Autowired
    private StreamlinedRegistrationServiceClient streamlinedRegClient;

    @Autowired
    private MasterFacilityRecordMapper mfrMapper;

    @Autowired
    private UserFacilityAssociationMapper ufaMapper;

    @Autowired
    private CefConfig cefConfig;

    @Autowired
    private SLTConfigHelper sltConfigHelper;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SecurityService securityService;

    public UserFacilityAssociationDto findById(Long id) {

        return ufaRepo.findById(id)
                .map(this.ufaMapper::toDto)
                .orElse(null);
    }

    public void removeSelfAssociationByCdxUserId(ApplicationUser user, Long masterFacilityId) {
        UserFacilityAssociation userFacility = ufaRepo.findByUserRoleIdMasterFacilityId(user.getUserRoleId(), masterFacilityId);
        UserFacilityAssociationDto userFacilityDetails = mapAssociation(userFacility);
        SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(userFacilityDetails.getMasterFacilityRecord().getProgramSystemCode().getCode());

        userFacility.setActive(false);
        userFacility.setDeauthorizedBy(user.getUserId());
        userFacility.setDeauthorizationDate(new Date());

        ufaRepo.save(userFacility);

        //send an email notification to the user being removed and SLT's predefined address that a user access is removed
        notificationService.sendUserAccessRemovedNotification(
            userFacilityDetails.getEmail(),
            sltConfig.getSltEmail(),
            cefConfig.getDefaultEmailAddress(),
            userFacilityDetails.getMasterFacilityRecord().getName(),
            userFacilityDetails.getRoleDescription(),
            sltConfig.getSltEisProgramCode(),
            sltConfig.getSltEmail());

        //add vars to map
        HashMap<String, Object> varMap = new HashMap<String, Object>();
        varMap.put("role", userFacilityDetails.getRoleDescription());
        varMap.put("facilityName", userFacilityDetails.getMasterFacilityRecord().getName());
        varMap.put("sltEmail", sltConfig.getSltEmail());
        varMap.put("slt", sltConfig.getSltEisProgramCode());

        notificationService.sendCdxInboxMessage(CdxInboxMessageType.FACILITY_ASSOCIATION_REMOVED, userFacilityDetails.getCdxUserId(), cefConfig.getDefaultEmailAddress(), varMap);
    }

    public void deleteById(Long id, ApplicationUser user) {
        this.deleteById(id, user.getUserId());
    }

    public void deleteById(Long id, String user) {
        UserFacilityAssociation userFacility = ufaRepo.findById(id).orElse(null);
        UserFacilityAssociationDto userFacilityDetails = mapAssociation(userFacility);
        SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(userFacilityDetails.getMasterFacilityRecord().getProgramSystemCode().getCode());

        userFacility.setActive(false);
        userFacility.setDeauthorizedBy(user);
        userFacility.setDeauthorizationDate(new Date());

        ufaRepo.save(userFacility);

        //send an email notification to the user and SLT's predefined address that a user access is removed
        if (Strings.emptyToNull(userFacilityDetails.getEmail()) != null) {
            notificationService.sendUserAccessRemovedNotification(
                    userFacilityDetails.getEmail(),
                    sltConfig.getSltEmail(),
                    cefConfig.getDefaultEmailAddress(),
                    userFacilityDetails.getMasterFacilityRecord().getName(),
                    userFacilityDetails.getRoleDescription(),
                    sltConfig.getSltEisProgramCode(),
                    sltConfig.getSltEmail());
        }

        //add vars to map
        HashMap<String, Object> varMap = new HashMap<String, Object>();
        varMap.put("role", userFacilityDetails.getRoleDescription());
        varMap.put("facilityName", userFacilityDetails.getMasterFacilityRecord().getName());
        varMap.put("sltEmail", sltConfig.getSltEmail());
        varMap.put("slt", sltConfig.getSltEisProgramCode());

        notificationService.sendCdxInboxMessage(CdxInboxMessageType.FACILITY_ASSOCIATION_REMOVED, userFacilityDetails.getCdxUserId(), cefConfig.getDefaultEmailAddress(), varMap);
    }

    public UserFacilityAssociationDto requestFacilityAssociation(MasterFacilityRecordDto facilityDto, ApplicationUser user) {

        MasterFacilityRecord facility = this.mfrMapper.fromDto(facilityDto);

        SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(facility.getProgramSystemCode().getCode());

        // throw an exception if the SLT is not yet supported
        if (Strings.emptyToNull(sltConfig.getSltEmail()) == null) {
            throw new NotExistException("SltConfigProperty", facility.getProgramSystemCode().getCode());
        }

        UserFacilityAssociation result = this.createFacilityAssociation(facility, user.getUserId(), user.getUserRoleId(), false);

        //send an email notification to the SLT's predefined address that a user has requested access
        notificationService.sendUserAccessRequestNotification(sltConfig.getSltEmail(),
                cefConfig.getDefaultEmailAddress(),
                facility.getName(),
                facility.getAgencyFacilityIdentifier(),
                user.getFirstName() + " " + user.getLastName(),
                user.getEmail(),
                user.getIdTypeText());

        return this.ufaMapper.toDto(result);
    }

    public UserFacilityAssociation createFacilityAssociation(MasterFacilityRecord facility, String cdxUserId, Long userRoleId, Boolean active) {

        UserFacilityAssociation association = new UserFacilityAssociation();
        association.setMasterFacilityRecord(facility);
        association.setCdxUserId(cdxUserId);
        association.setUserRoleId(userRoleId);
        association.setActive(active);
        association.setLastModifiedDate(new Date());
        return this.ufaRepo.save(association);
    }

    public List<UserFacilityAssociationDto> approveAssociations(List<UserFacilityAssociationDto> associations, ApplicationUser user) {

    	SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(associations.get(0).getMasterFacilityRecord().getProgramSystemCode().getCode());

        List<Long> ids = associations.stream().map(UserFacilityAssociationDto::getId).collect(Collectors.toList());

        Iterable<UserFacilityAssociation> entities = this.ufaRepo.findAllById(ids);

        entities.forEach(a -> {
        	a.setActive(true);
        	a.setAuthorizedBy(user.getUserId());
        	a.setAuthorizationDate(new Date());
        });

        entities = this.ufaRepo.saveAll(entities);

        for (UserFacilityAssociation ufa : entities) {
            securityService.evictUserCachedItems(ufa.getUserRoleId());
        }

        associations.forEach(a -> {
            notificationService.sendUserAssociationAcceptedNotification(
            		a.getEmail(),
            		sltConfig.getSltEmail(),
                    cefConfig.getDefaultEmailAddress(),
                    a.getMasterFacilityRecord().getName(),
                    a.getRoleDescription());

            //add vars to map
            HashMap<String, Object> varMap = new HashMap<String, Object>();
            varMap.put("role", a.getRoleDescription());
            varMap.put("facilityName", a.getMasterFacilityRecord().getName());

            notificationService.sendCdxInboxMessage(CdxInboxMessageType.FACILITY_ASSOCIATION_APPROVED, a.getCdxUserId(), cefConfig.getDefaultEmailAddress(), varMap);
        });

        return this.ufaMapper.toDtoList(entities);
    }

    public List<UserFacilityAssociationDto> rejectAssociations(List<UserFacilityAssociationDto> associations, String comments, ApplicationUser user) {

    	SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(associations.get(0).getMasterFacilityRecord().getProgramSystemCode().getCode());

        List<Long> ids = associations.stream().map(UserFacilityAssociationDto::getId).collect(Collectors.toList());

        Iterable<UserFacilityAssociation> entities = this.ufaRepo.findAllById(ids);

        entities.forEach(a -> {
            a.setActive(false);
            a.setDeauthorizedBy(user.getUserId());
            a.setDeauthorizationDate(new Date());
        });

        entities = this.ufaRepo.saveAll(entities);

        associations.forEach(a -> {
            notificationService.sendUserAssociationRejectedNotification(
            		a.getEmail(),
            		sltConfig.getSltEmail(),
                    cefConfig.getDefaultEmailAddress(),
                    a.getMasterFacilityRecord().getName(),
                    a.getRoleDescription(),
                    comments);

            //add vars to map
            HashMap<String, Object> varMap = new HashMap<String, Object>();
            varMap.put("role", a.getRoleDescription());
            varMap.put("facilityName", a.getMasterFacilityRecord().getName());
            varMap.put("comments", comments);

            notificationService.sendCdxInboxMessage(CdxInboxMessageType.FACILITY_ASSOCIATION_REJECTED, a.getCdxUserId(), cefConfig.getDefaultEmailAddress(), varMap);
        });

        return this.ufaMapper.toDtoList(entities);
    }

    public List<UserFacilityAssociationDto> findByUserRoleId(Long userRoleId) {

        List<UserFacilityAssociation> entities = this.ufaRepo.findByUserRoleId(userRoleId);

        return this.ufaMapper.toDtoList(entities);
    }

    public List<String> findProgramSystemCodesByUserRoleId(Long userRoleId) {

        List<String> entities = this.ufaRepo.retrieveMasterFacilityRecordProgramSystemCodes(userRoleId);

        return entities;
    }

    public List<UserFacilityAssociationDto> findDetailsByMasterFacilityRecordIdAndActive(Long masterFacilityRecordId, boolean active) {

        List<UserFacilityAssociation> entities = this.ufaRepo.findByMasterFacilityRecordIdAndActive(masterFacilityRecordId, active);

        return this.mapAssociations(entities);
    }

    public List<UserFacilityAssociationDto> findDetailsByProgramSystemCodeAndActive(String programSystemCode, boolean active) {

        List<UserFacilityAssociation> entities = this.ufaRepo.findByProgramSystemCodeAndActive(programSystemCode, active);

        return this.mapAssociations(entities);
    }

    private UserFacilityAssociationDto mapAssociation(UserFacilityAssociation association) {

        RegistrationUser user = this.registerServiceClient.retrieveUserByUserRoleId(association.getUserRoleId());
        RegistrationUserSearchCriteria criteria = new RegistrationUserSearchCriteria();
        criteria.setUserId(user.getUserId());
        criteria.setDataflow("CAER");
        RegistrationNewUserProfile profile = this.streamlinedRegClient.retrieveUsersByCriteria(criteria)
                .stream()
                .filter(r ->association.getUserRoleId().equals(r.getRole().getUserRoleId()))
                .findFirst()
                .orElse(null);
        if (profile != null ) {
            profile.getRole().getType().setDescription(AppRole.RoleType.fromId(profile.getRole().getType().getCode()).roleName());
        }
        return this.ufaMapper.toDto(association, profile);
    }

    public List<UserFacilityAssociationDto> mapAssociations(List<UserFacilityAssociation> associations) {

        RegistrationUserSearchCriteria criteria = new RegistrationUserSearchCriteria();
        criteria.setDataflow("CAER");
        criteria.getUserIds().addAll(associations.stream().map(UserFacilityAssociation::getCdxUserId).distinct().collect(Collectors.toSet()));
        Map<Long, RegistrationNewUserProfile> profileMap = this.streamlinedRegClient.retrieveUsersByCriteria(criteria)
                .stream()
                .peek(p -> p.getRole().getType().setDescription(AppRole.RoleType.fromId(p.getRole().getType().getCode()).roleName()))
                .collect(Collectors.toMap(p -> p.getRole().getUserRoleId(), Functions.identity()));
        return associations.stream().map(a -> this.ufaMapper.toDto(a, profileMap.get(a.getUserRoleId()))).collect(Collectors.toList());
    }

    public List<UserFacilityAssociationDto> migrateAssociations() {

        logger.info("User Association migration start");
        RegistrationUserSearchCriteria criteria = new RegistrationUserSearchCriteria();
        criteria.setDataflow("CAER");
        RegistrationRoleType preparerType = new RegistrationRoleType();
        preparerType.setCode(AppRole.RoleType.PREPARER.getId());
        RegistrationRoleType certifierType = new RegistrationRoleType();
        certifierType.setCode(AppRole.RoleType.NEI_CERTIFIER.getId());
        criteria.getRoleTypes().add(preparerType);
        criteria.getRoleTypes().add(certifierType);
        List<RegistrationNewUserProfile> profiles = this.streamlinedRegClient.retrieveUsersByCriteria(criteria);
        logger.info("{} profiles received", profiles.size());
        List<UserFacilityAssociation> associations = profiles.stream().map(p -> {
            List<ProgramFacility> programFacilities = this.registerFacilityClient.getFacilitiesByUserRoleId(p.getRole().getUserRoleId());
            List<MasterFacilityRecord> mfrs = programFacilities.stream().map(pf -> {
                return this.mfrRepo.findByEisProgramId(pf.getProgramId()).orElse(null);
            }).filter(mfr -> mfr != null).distinct().collect(Collectors.toList());

            List<UserFacilityAssociation> newAssociations = mfrs.stream().map(mfr -> {
                UserFacilityAssociation ufa = new UserFacilityAssociation();
                ufa.setMasterFacilityRecord(mfr);
                ufa.setCdxUserId(p.getUser().getUserId());
                ufa.setUserRoleId(p.getRole().getUserRoleId());
                ufa.setActive(true);
                return ufa;
            }).collect(Collectors.toList());
            logger.info("{} associations generated for {}", newAssociations.size(), p.getUser().getUserId());
            return newAssociations;
        }).flatMap(List::stream).collect(Collectors.toList());

        Iterable<UserFacilityAssociation> result = this.ufaRepo.saveAll(associations);
        logger.info("{} associations saved", associations.size());
        logger.info("User Association migration end");

        return this.ufaMapper.toDtoList(result);
    }

    public List<UserFacilityAssociationDto> getAllActiveAssociations() {

        List<UserFacilityAssociation> result = new ArrayList<>();
        Iterable<UserFacilityAssociation> entities = this.ufaRepo.findAllActiveAssociations();

        entities.forEach(entity -> {
            result.add(entity);
        });

        return this.mapAssociations(result);
    }

    public RegistrationNewUserProfile getProfile(UserFacilityAssociationDto association) {

        RegistrationUser user = this.registerServiceClient.retrieveUserByUserRoleId(association.getUserRoleId());
        RegistrationUserSearchCriteria criteria = new RegistrationUserSearchCriteria();
        criteria.setUserId(user.getUserId());
        criteria.setDataflow("CAER");
        RegistrationNewUserProfile profile = this.streamlinedRegClient.retrieveUsersByCriteria(criteria)
            .stream()
            .filter(r ->association.getUserRoleId().equals(r.getRole().getUserRoleId()))
            .findFirst()
            .orElse(null);

        return profile;
    }
}
