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
import gov.epa.cef.web.domain.MasterFacilityRecord;
import gov.epa.cef.web.domain.common.BaseLookupEntity;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MasterFacilityRecordRepository extends JpaRepository<MasterFacilityRecord, Long>, ProgramIdRetriever {

    /**
     * Retrieve facility by eis program
     * @param eisProgramId
     * @return
     */
    Optional<MasterFacilityRecord> findByEisProgramId(String eisProgramId);
    
    /**
     * Retrieve facility by Program System Code and Agency Facility Id
     * @param programSystemCode
     * @param agencyFacilityIdentifier
     * @return
     */
    Optional<MasterFacilityRecord> findByProgramSystemCodeCodeAndAgencyFacilityIdentifier(String programSystemCode, String agencyFacilityIdentifier);

    /***
     * Retrieve the common form facilities based on a given state code
     * @param stateCode : 2 character state code
     * @return
     */
    List<MasterFacilityRecord> findByStateCode(String stateCode);

    List<MasterFacilityRecord> findByProgramSystemCodeCode(String programSystemCode);

    List<MasterFacilityRecord> findByProgramSystemCodeCodeAndAgencyFacilityIdentifierIgnoreCase(String programSystemCode, String agencyFacilityIdentifier);

    /**
     * Retrieve program system codes currently in use; sort does not work on this query
     */
    @Query("select distinct programSystemCode FROM MasterFacilityRecord")
    List<BaseLookupEntity> findDistinctProgramSystems();

    @Cacheable(value = CacheName.MasterFacilityMasterIds)
    @Query("select mfr.id from MasterFacilityRecord mfr where mfr.id = :id")
    Optional<Long> retrieveMasterFacilityRecordIdById(@Param("id") Long id);
    
    /**
     * Retrieve lat/long tolerance by eis program
     * @param eisProgramId
     * @return
     */
    @Query("select coordinateTolerance FROM MasterFacilityRecord mfr where mfr.eisProgramId = :eisProgramId")
    BigDecimal getLatLongTolerance(String eisProgramId);

}
