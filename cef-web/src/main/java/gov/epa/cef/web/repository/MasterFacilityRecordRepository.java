/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
