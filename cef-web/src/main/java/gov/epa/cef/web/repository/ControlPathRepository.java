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
import gov.epa.cef.web.domain.ControlPath;
import gov.epa.cef.web.domain.ReportingPeriod;

import gov.epa.cef.web.domain.common.IReportComponent;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ControlPathRepository extends CrudRepository<ControlPath, Long>, ProgramIdRetriever, ReportIdRetriever, ReportComponentRetriever {

    /**
     * Retrieve Control Paths for an emissions process
     * @param processId
     * @return
     */
	@Query("SELECT DISTINCT cp FROM ControlPath cp INNER JOIN cp.releasePointAppts rpa INNER JOIN rpa.emissionsProcess ep WHERE ep.id = :processId")
    List<ControlPath> findByEmissionsProcessId(@Param("processId") Long processId);

    /**
     * Retrieve Control Paths for an emissions unit
     * @param unitId
     * @return
     */
	@Query("SELECT DISTINCT cp FROM ControlPath cp INNER JOIN cp.releasePointAppts rpa INNER JOIN rpa.emissionsProcess ep INNER JOIN ep.emissionsUnit eu WHERE eu.id = :unitId")
    List<ControlPath> findByEmissionsUnitId(@Param("unitId") Long unitId);

    /**
     * Retrieve Control Paths for an control device
     * @param deviceId
     * @return
     */
	@Query("SELECT DISTINCT cp FROM ControlPath cp INNER JOIN cp.assignments ca INNER JOIN ca.control c WHERE c.id = :deviceId")
    List<ControlPath> findByControlId(@Param("deviceId") Long deviceId);

    /**
     * Retrieve Control Paths for a release point
     * @param pointId
     * @return
     */
	@Query("SELECT DISTINCT cp FROM ControlPath cp INNER JOIN cp.releasePointAppts rpa INNER JOIN rpa.releasePoint rp WHERE rp.id = :pointId")
    List<ControlPath> findByReleasePointId(@Param("pointId") Long pointId);

	/**
	 * Find Control Paths with the specified identifier, master facility record id, and year
	 * @param identifier
	 * @param mfrId
	 * @param year
	 * @return
	 */
    @Query("select cp from ControlPath cp join cp.facilitySite fs join fs.emissionsReport r join r.masterFacilityRecord mfr where cp.pathId = :identifier and mfr.id = :mfrId and r.year = :year and r.isDeleted = false")
    List<ControlPath> retrieveByIdentifierFacilityYear(@Param("identifier") String identifier, @Param("mfrId") Long mfrId, @Param("year") Short year);

    @Cacheable(value = CacheName.ControlPathMasterIds)
    @Query("select mfr.id from ControlPath cp join cp.facilitySite fs join fs.emissionsReport r join r.masterFacilityRecord mfr where cp.id = :id and r.isDeleted = false")
    Optional<Long> retrieveMasterFacilityRecordIdById(@Param("id") Long id);

    /**
     * Retrieve Emissions Report id for a Control Path
     * @param id
     * @return Emissions Report id
     */
    @Cacheable(value = CacheName.ControlPathEmissionsReportIds)
    @Query("select r.id from ControlPath cp join cp.facilitySite fs join fs.emissionsReport r where cp.id = :id and r.isDeleted = false")
    Optional<Long> retrieveEmissionsReportById(@Param("id") Long id);

    /**
     * Retrieve Control Paths for a facility site
     * @param facilitySiteId
     * @return
     */
    List<ControlPath> findByFacilitySiteIdOrderByPathId(Long facilitySiteId);

    /**
     * Retrieve a list of all control paths for a specific program system code and emissions reporting year where the associated facility is operating
     * @param psc Program System Code
     * @param emissionsReportYear
     * @return
     */
    @Query("select cp from ControlPath cp join cp.facilitySite fs join fs.emissionsReport er where er.programSystemCode.code = :psc and er.year = :emissionsReportYear "
        + "and (er.thresholdStatus in (gov.epa.cef.web.domain.ThresholdStatus.OPERATING_ABOVE_THRESHOLD) or er.thresholdStatus is null) and fs.operatingStatusCode.code = 'OP' and er.isDeleted = false")
    List<ControlPath> findByPscAndEmissionsReportYear(String psc, Short emissionsReportYear);

    @Query("select rc from #{#entityName} rc where rc.id = ?1")
    Optional<IReportComponent> retrieveReportComponentById(Long id);
}
