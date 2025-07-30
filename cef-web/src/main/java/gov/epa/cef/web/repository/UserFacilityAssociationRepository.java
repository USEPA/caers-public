/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.repository;

import gov.epa.cef.web.config.CacheName;
import gov.epa.cef.web.domain.UserFacilityAssociation;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserFacilityAssociationRepository extends CrudRepository<UserFacilityAssociation, Long> {

    @Query("select ufa from UserFacilityAssociation ufa join ufa.masterFacilityRecord mfr " +
        "where mfr.id = :masterFacilityRecordId and ufa.userRoleId = :userRoleId and ufa.active = true")
    UserFacilityAssociation findByUserRoleIdMasterFacilityId(@Param("userRoleId") Long userRoleId, @Param("masterFacilityRecordId") Long masterFacilityRecordId);

    @Query("select ufa from UserFacilityAssociation ufa join ufa.masterFacilityRecord mfr " +
        "where mfr.id = :masterFacilityRecordId and ufa.active = :active and ufa.deauthorizedBy is null")
    List<UserFacilityAssociation> findByMasterFacilityRecordIdAndActive(@Param("masterFacilityRecordId") Long masterFacilityRecordId, @Param("active") boolean active);

    @Query("select ufa from UserFacilityAssociation ufa where ufa.userRoleId = :userRoleId and ufa.deauthorizedBy is null")
    List<UserFacilityAssociation> findByUserRoleId(@Param("userRoleId") Long userRoleId);

    @Query("select distinct psc.code from UserFacilityAssociation ufa join ufa.masterFacilityRecord mfr join mfr.programSystemCode psc " +
        "where ufa.userRoleId = :userRoleId and ufa.active = true")
    List<String> retrieveMasterFacilityRecordProgramSystemCodes(@Param("userRoleId") Long userRoleId);

    @Query("select ufa from UserFacilityAssociation ufa join ufa.masterFacilityRecord mfr join mfr.programSystemCode psc " +
        "where psc.code = :code and ufa.active = :active and ufa.deauthorizedBy is null")
    List<UserFacilityAssociation> findByProgramSystemCodeAndActive(@Param("code") String code, @Param("active") boolean active);

    @Cacheable(value = CacheName.UserMasterFacilityIds)
    @Query("select mfr.id from UserFacilityAssociation ufa join ufa.masterFacilityRecord mfr where ufa.userRoleId = :userRoleId and ufa.active = true")
    List<Long> retrieveMasterFacilityRecordIds(@Param("userRoleId") Long userRoleId);

    @Query("select ufa from UserFacilityAssociation ufa where ufa.deauthorizedBy is null and ufa.active = true")
    List<UserFacilityAssociation> findAllActiveAssociations();
}
