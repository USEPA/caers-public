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
import gov.epa.cef.web.domain.FacilityNAICSXref;
import gov.epa.cef.web.domain.FacilitySiteContact;

public interface FacilityNAICSXrefRepository extends CrudRepository<FacilityNAICSXref, Long>, ProgramIdRetriever, ReportIdRetriever {

    /**
     * Retrieve Facility NAICS for a facility site
     * @param facilitySiteId
     * @return
     */
    List<FacilityNAICSXref> findByFacilitySiteId(Long facilitySiteId);

    @Transactional
    @Modifying
    @Query("delete from FacilityNAICSXref where id in (select fx.id from FacilityNAICSXref fx join fx.facilitySite fs where fs.id = :id)")
    void deleteByFacilitySiteId(@Param("id") Long id);

    @Cacheable(value = CacheName.FacilityNAICSMasterIds)
    @Query("select mfr.id from FacilityNAICSXref fn join fn.facilitySite fs join fs.emissionsReport r join r.masterFacilityRecord mfr where fn.id = :id and r.isDeleted = false")
    Optional<Long> retrieveMasterFacilityRecordIdById(@Param("id") Long id);

    /**
     * Retrieve Emissions Report id for a Facility NAICS Xref
     * @param id
     * @return Emissions Report id
     */
    @Cacheable(value = CacheName.FacilityNAICSEmissionsReportIds)
    @Query("select er.id from FacilityNAICSXref fn join fn.facilitySite fs join fs.emissionsReport er where fn.id = :id and er.isDeleted = false")
    Optional<Long> retrieveEmissionsReportById(@Param("id") Long id);

    /**
     * Retrieve a list of all facility NAICS codes for a specific program system code and emissions reporting year
     * @param psc Program System Code
     * @param emissionsReportYear
     * @return
     */
    @Query("select fnx from FacilityNAICSXref fnx join fnx.facilitySite fs join fs.emissionsReport er where er.programSystemCode.code = :psc and er.year = :emissionsReportYear and er.isDeleted = false")
    List<FacilityNAICSXref> findByPscAndEmissionsReportYear(String psc, Short emissionsReportYear);

}
