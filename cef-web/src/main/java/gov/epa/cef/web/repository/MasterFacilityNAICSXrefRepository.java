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

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import gov.epa.cef.web.config.CacheName;
import gov.epa.cef.web.domain.MasterFacilityNAICSXref;

public interface MasterFacilityNAICSXrefRepository extends CrudRepository<MasterFacilityNAICSXref, Long>, ProgramIdRetriever {
	
	/**
     * Retrieve NAICS for a Master Facility Record
     * 
     * @param masterFacilityId
     * @return
     */
    List<MasterFacilityNAICSXref> findByMasterFacilityRecordId(Long masterFacilityRecordId);

    @Transactional
    @Modifying
    @Query("delete from MasterFacilityNAICSXref where id in (select mfx.id from MasterFacilityNAICSXref mfx join mfx.masterFacilityRecord mfr where mfr.id = :id)")
    void deleteByMasterFacilityId(@Param("id") Long id);
    
    @Cacheable(value = CacheName.MasterFacilityNAICSMasterIds)
    @Query("select mfr.id from MasterFacilityNAICSXref mfn join mfn.masterFacilityRecord mfr where mfn.id = :masterFacilityId")
    Optional<Long> retrieveMasterFacilityRecordIdById(@Param("masterFacilityId") Long masterFacilityId);
}
