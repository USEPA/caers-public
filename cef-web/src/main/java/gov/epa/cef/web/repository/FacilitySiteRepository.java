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
import gov.epa.cef.web.domain.FacilitySite;
import gov.epa.cef.web.domain.common.IReportComponent;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FacilitySiteRepository extends CrudRepository<FacilitySite, Long>, ProgramIdRetriever, ReportIdRetriever, ReportComponentRetriever {

    /**
     * Retrieve facilities by emissions report
     * @param emissionsReportId
     * @return
     */
    @Query("select fs from FacilitySite fs join fs.emissionsReport er where er.id = :emissionsReportId and er.isDeleted = false")
    List<FacilitySite> findByEmissionsReportId(Long emissionsReportId);

    /***
     * Retrieve the common form facilities based on a given state code
     * @param stateCode : 2 character state code
     * @return
     */
    List<FacilitySite> findByStateCode(String stateCode);

    @Cacheable(value = CacheName.FacilityMasterIds)
    @Query("select mfr.id from FacilitySite fs join fs.emissionsReport r join r.masterFacilityRecord mfr where fs.id = :id and r.isDeleted = false")
    Optional<Long> retrieveMasterFacilityRecordIdById(@Param("id") Long id);

    /**
     * Retrieve Emissions Report id for a Facility Site
     * @param id
     * @return Emissions Report id
     */
    @Cacheable(value = CacheName.FacilityEmissionsReportIds)
    @Query("select r.id from FacilitySite fs join fs.emissionsReport r where fs.id = :id and r.isDeleted = false")
    Optional<Long> retrieveEmissionsReportById(@Param("id") Long id);

    /**
     * Retrieve a list of all facilities for a specific program system code and emissions reporting year
     * @param psc Program System Code
     * @param emissionsReportYear
     * @return
     */
    @Query("select fs from FacilitySite fs join fs.emissionsReport er where er.programSystemCode.code = :psc and er.year = :emissionsReportYear and er.isDeleted = false")
    List<FacilitySite> findByPscAndEmissionsReportYear(String psc, Short emissionsReportYear);

    /**
     * Retrieve a list of all facility site ids for a specific program system code and emissions reporting year
     * @param psc Program System Code
     * @param emissionsReportYear
     * @return
     */
    @Query("select fs.id from FacilitySite fs join fs.emissionsReport er where er.programSystemCode.code = :psc and er.year = :emissionsReportYear and er.isDeleted = false")
    List<Long> findFacilityIds(String psc, Short emissionsReportYear);

    /**
     * Retrieve the facility for the specified emissions reporting year
     * @param agencyFacilityIdentifier
     * @param psc
     * @param emissionsReportYear
     * @return
     */
    @Query("select fs from FacilitySite fs join fs.emissionsReport er where fs.agencyFacilityIdentifier = :agencyFacilityIdentifier "
    		+ "and er.programSystemCode.code = :psc and er.year = :emissionsReportYear and er.isDeleted = false")
    FacilitySite findByAgencyFacilityIdentifierAndEmissionsReportYear(String agencyFacilityIdentifier, String psc, Short emissionsReportYear);

    @Query("select rc from #{#entityName} rc where rc.id = ?1")
    Optional<IReportComponent> retrieveReportComponentById(Long id);
}
