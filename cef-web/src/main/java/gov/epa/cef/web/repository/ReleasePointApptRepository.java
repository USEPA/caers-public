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
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import gov.epa.cef.web.config.CacheName;
import gov.epa.cef.web.domain.ReleasePointAppt;
import gov.epa.cef.web.domain.ReportingPeriod;

public interface ReleasePointApptRepository extends CrudRepository<ReleasePointAppt, Long>, ProgramIdRetriever, ReportIdRetriever {

    /**
     * Retrieve Release Point Apportionments for an EmissionsProcess
     * @param emissionsProcessId
     * @return
     */
    List<ReleasePointAppt> findByEmissionsProcessId(Long emissionsProcessId);

    @Cacheable(value = CacheName.ReleasePointApptMasterIds)
    @Query("select mfr.id from ReleasePointAppt rpa join rpa.releasePoint rp join rp.facilitySite fs join fs.emissionsReport r join r.masterFacilityRecord mfr where rpa.id = :id and r.isDeleted = false")
    Optional<Long> retrieveMasterFacilityRecordIdById(@Param("id") Long id);

    /**
     * Retrieve Emissions Report id for a Release Point Appt
     * @param id
     * @return Emissions Report id
     */
    @Cacheable(value = CacheName.ReleasePointApptEmissionsReportIds)
    @Query("select r.id from ReleasePointAppt rpa join rpa.releasePoint rp join rp.facilitySite fs join fs.emissionsReport r where rpa.id = :id and r.isDeleted = false")
    Optional<Long> retrieveEmissionsReportById(@Param("id") Long id);

    /**
     * Retrieve a list of all release point apportionment records for a specific program system code and emissions reporting year where the associated facility is operating
     * @param psc
     * @param emissionsReportYear
     * @return
     */
    @Query("select rpa from ReleasePointAppt rpa join rpa.releasePoint rp join rp.facilitySite fs join fs.emissionsReport er where er.programSystemCode.code = :psc and er.year = :emissionsReportYear "
        + "and (er.thresholdStatus in (gov.epa.cef.web.domain.ThresholdStatus.OPERATING_ABOVE_THRESHOLD) or er.thresholdStatus is null) and fs.operatingStatusCode.code = 'OP' and er.isDeleted = false")
    List<ReleasePointAppt> findByPscAndEmissionsReportYear(String psc, Short emissionsReportYear);

}
