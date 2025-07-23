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
package gov.epa.cef.web.repository;

import gov.epa.cef.web.config.CacheName;
import gov.epa.cef.web.domain.FacilityPermit;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FacilityPermitRepository extends CrudRepository<FacilityPermit, Long>, ProgramIdRetriever {
	
	/**
     * Retrieve Permits for a Master Facility Record
     * 
     * @param masterFacilityId
     * @return
     */
    List<FacilityPermit> findByMasterFacilityRecordId(Long masterFacilityRecordId);

    @Transactional
    @Modifying
    @Query("delete from FacilityPermit where id in (select fp.id from FacilityPermit fp join fp.masterFacilityRecord mfr where mfr.id = :id)")
    void deleteByMasterFacilityId(@Param("id") Long id);
    
    @Cacheable(value = CacheName.MasterFacilityNAICSMasterIds)
    @Query("select mfr.id from FacilityPermit fp join fp.masterFacilityRecord mfr where fp.id = :masterFacilityId")
    Optional<Long> retrieveMasterFacilityRecordIdById(@Param("masterFacilityId") Long masterFacilityId);
}
