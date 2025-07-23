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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import gov.epa.cef.web.domain.OperatingDetail;

import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OperatingDetailRepository extends CrudRepository<OperatingDetail, Long>, ProgramIdRetriever, ReportIdRetriever, ReportComponentRetriever {

    @Cacheable(value = CacheName.OperatingDetailMasterIds)
    @Query("select mfr.id "
            + "from OperatingDetail od join od.reportingPeriod rp join rp.emissionsProcess p join p.emissionsUnit eu join eu.facilitySite fs "
            + "join fs.emissionsReport r join r.masterFacilityRecord mfr where od.id = :id and r.isDeleted = false")
    Optional<Long> retrieveMasterFacilityRecordIdById(@Param("id") Long id);

    /**
     * Retrieve Emissions Report id for an Operating Detail
     * @param id
     * @return Emissions Report id
     */
    @Cacheable(value = CacheName.OperatingDetailEmissionsReportIds)
    @Query("select r.id from OperatingDetail od join od.reportingPeriod rp join rp.emissionsProcess p join p.emissionsUnit eu join eu.facilitySite fs join fs.emissionsReport r where od.id = :id and r.isDeleted = false")
    Optional<Long> retrieveEmissionsReportById(@Param("id") Long id);

    /**
     * Retrieve a list of all operating details for a specific program system code and emissions reporting year where the associated facility is operating
     * @param psc
     * @param emissionsReportYear
     * @return
     */
    @Query("select od from OperatingDetail od join od.reportingPeriod rp join rp.emissionsProcess ep join ep.emissionsUnit eu join eu.facilitySite fs join fs.emissionsReport er where er.programSystemCode.code = :psc and er.year = :emissionsReportYear "
        + "and (er.thresholdStatus in (gov.epa.cef.web.domain.ThresholdStatus.OPERATING_ABOVE_THRESHOLD) or er.thresholdStatus is null) "
        + "and fs.operatingStatusCode.code = 'OP' and ep.operatingStatusCode.code = 'OP' and er.isDeleted = false")
    List<OperatingDetail> findByPscAndEmissionsReportYear(String psc, Short emissionsReportYear);

    /**
     * Retrieve a list of all operating details for a specific program system code and emissions
     * reporting year where the associated facility is operating and associated reporting period is an annual period
     * @param psc
     * @param emissionsReportYear
     * @return
     */
    @Query("select od from OperatingDetail od join od.reportingPeriod rp join rp.emissionsProcess ep join ep.emissionsUnit eu join eu.facilitySite fs join fs.emissionsReport er where er.programSystemCode.code = :psc and er.year = :emissionsReportYear "
        + "and fs.operatingStatusCode.code = 'OP' and rp.reportingPeriodTypeCode.code = 'A' and er.isDeleted = false")
    List<OperatingDetail> findAnnualByPscAndEmissionsReportYear(String psc, Short emissionsReportYear);

    /**
     * Delete by facility site ID
     */
    @Transactional
    @Modifying
    @Query("delete from OperatingDetail where id in (select od.id from OperatingDetail od join od.reportingPeriod rp join rp.emissionsProcess ep " +
                                                            "join ep.emissionsUnit eu join eu.facilitySite fs where fs.id = :facilitySiteId)")
    void deleteByFacilitySite(Long facilitySiteId);
}
