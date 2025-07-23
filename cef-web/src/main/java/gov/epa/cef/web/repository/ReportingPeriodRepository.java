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
import gov.epa.cef.web.domain.ReportingPeriod;
import gov.epa.cef.web.domain.common.IReportComponent;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ReportingPeriodRepository extends CrudRepository<ReportingPeriod, Long>, ProgramIdRetriever, ReportIdRetriever, ReportComponentRetriever {

    /**
     * Retrieve Reporting Periods for an emissions process
     * @param processId
     * @return
     */
    List<ReportingPeriod> findByEmissionsProcessId(Long processId);

    /**
     * Return all Reporting Periods for a facility site
     * @param facilitySiteId
     * @return
     */
    @Query("select rp from ReportingPeriod rp join rp.emissionsProcess p join p.emissionsUnit eu join eu.facilitySite fs where fs.id = :facilitySiteId")
    List<ReportingPeriod> findByFacilitySiteId(Long facilitySiteId);

    List<ReportingPeriod> findByEmissionsProcessIdAndReportingPeriodTypeCodeCode(Long facilitySiteId, String period);

    /**
     * Return all Reporting Periods for a facility site and specific reporting period based on monthly reporting status
     * @param facilitySiteId
     * @param period
     * @param monthlyReporting
     * @return
     */
    @Query("select rp from ReportingPeriod rp join rp.emissionsProcess p join p.emissionsUnit eu join eu.facilitySite fs join PointSourceSccCode pscc on p.sccCode = pscc.code "
        + "where fs.id = :facilitySiteId and rp.reportingPeriodTypeCode.shortName = :period and pscc.monthlyReporting = :monthlyReporting")
    List<ReportingPeriod> findByFacilitySiteIdAndPeriod(Long facilitySiteId, String period, boolean monthlyReporting);

    /**
     * Return all Operating Reporting Periods for a facility site and specific reporting period based on monthly reporting status
     * @param facilitySiteId
     * @param period
     * @param monthlyReporting
     * @return
     */
    @Query("select rp from ReportingPeriod rp join rp.emissionsProcess p join p.emissionsUnit eu join eu.facilitySite fs "
        + "join PointSourceSccCode pscc on p.sccCode = pscc.code "
        + "where fs.id = :facilitySiteId and rp.reportingPeriodTypeCode.shortName = :period and pscc.monthlyReporting = :monthlyReporting "
        + "and ((p.operatingStatusCode.code = 'OP' and eu.operatingStatusCode.code = 'OP') or fs.facilitySourceTypeCode.code = '104') "
        + "order by eu.unitIdentifier, p.emissionsProcessIdentifier")
    List<ReportingPeriod> findOperatingByFacilitySiteIdAndPeriod(Long facilitySiteId, String period, boolean monthlyReporting);

    /**
     * Return all Operating Reporting Periods for a facility site based on monthly reporting status
     * @param facilitySiteId
     * @param monthlyReporting
     * @return
     */
    @Query("select rp from ReportingPeriod rp join rp.emissionsProcess p join p.emissionsUnit eu join eu.facilitySite fs "
        + "join PointSourceSccCode pscc on p.sccCode = pscc.code "
        + "where fs.id = :facilitySiteId and pscc.monthlyReporting = :monthlyReporting "
        + "and ((p.operatingStatusCode.code = 'OP' and eu.operatingStatusCode.code = 'OP') or fs.facilitySourceTypeCode.code = '104') "
        + "order by eu.unitIdentifier, p.emissionsProcessIdentifier")
    List<ReportingPeriod> findMonthlyOperatingByFacilitySiteId(Long facilitySiteId, boolean monthlyReporting);

    List<ReportingPeriod> findByAnnualReportingPeriodId(Long annualReportingPeriodId);

    /**
     * Find Reporting Period with the specified type, process identifier, unit identifier, master facility record id, and year.
     * This combination should be unique and can be used to find a specific Reporting Period for a specific year
     * @param typeCode
     * @param processIdentifier
     * @param unitIdentifier
     * @param mfrId
     * @param year
     * @return
     */
    @Query("select rp from ReportingPeriod rp join rp.emissionsProcess ep join ep.emissionsUnit eu join eu.facilitySite fs join fs.emissionsReport r join r.masterFacilityRecord mfr "
            + "where rp.reportingPeriodTypeCode.code = :typeCode and ep.emissionsProcessIdentifier = :processIdentifier and eu.unitIdentifier = :unitIdentifier "
            + "and mfr.id = :mfrId and r.year = :year and r.isDeleted = false")
    List<ReportingPeriod> retrieveByTypeIdentifierParentFacilityYear(@Param("typeCode") String typeCode,
            @Param("processIdentifier") String processIdentifier, @Param("unitIdentifier") String unitIdentifier,
            @Param("mfrId") Long mfrId, @Param("year") Short year);

    @Cacheable(value = CacheName.ReportingPeriodMasterIds)
    @Query("select mfr.id from ReportingPeriod rp join rp.emissionsProcess p join p.emissionsUnit eu join eu.facilitySite fs join fs.emissionsReport r join r.masterFacilityRecord mfr where rp.id = :id and r.isDeleted = false")
    Optional<Long> retrieveMasterFacilityRecordIdById(@Param("id") Long id);

    /**
     * Retrieve Emissions Report id for a Reporting Period
     * @param id
     * @return Emissions Report id
     */
    @Cacheable(value = CacheName.ReportingPeriodEmissionsReportIds)
    @Query("select r.id from ReportingPeriod rp join rp.emissionsProcess p join p.emissionsUnit eu join eu.facilitySite fs join fs.emissionsReport r where rp.id = :id and r.isDeleted = false")
    Optional<Long> retrieveEmissionsReportById(@Param("id") Long id);


    /**
     * Retrieve a list of all reporting periods for a specific program system code and emissions reporting year where the associated facility is operating
     * @param psc Program System Code
     * @param emissionsReportYear
     * @return
     */
    @Query("select rp from ReportingPeriod rp join rp.emissionsProcess ep join ep.emissionsUnit eu join eu.facilitySite fs join fs.emissionsReport er where er.programSystemCode.code = :psc and er.year = :emissionsReportYear "
        + "and (er.thresholdStatus in (gov.epa.cef.web.domain.ThresholdStatus.OPERATING_ABOVE_THRESHOLD) or er.thresholdStatus is null) "
        + "and fs.operatingStatusCode.code = 'OP' and ep.operatingStatusCode.code = 'OP' and er.isDeleted = false")
    List<ReportingPeriod> findByPscAndEmissionsReportYear(String psc, Short emissionsReportYear);

    /**
     * Retrieve a list of all reporting periods from 2020 or later to insert calculated standard non-point fuel use
     * @return
     */
    @Query("select rp from ReportingPeriod rp join rp.emissionsProcess ep join ep.emissionsUnit eu join eu.facilitySite fs join fs.emissionsReport er where er.year >= 2020")
    List<ReportingPeriod> findAllForNonPointStandardizedUpdate();

    @Query("select rc from #{#entityName} rc where rc.id = ?1")
    Optional<IReportComponent> retrieveReportComponentById(Long id);

    /**
     * Delete by facility site ID
     */
    @Transactional
    @Modifying
    @Query("delete from ReportingPeriod where id in (select rp.id from ReportingPeriod rp join rp.emissionsProcess ep " +
                                        "join ep.emissionsUnit eu join eu.facilitySite fs where fs.id = :facilitySiteId)")
    void deleteByFacilitySite(Long facilitySiteId);
}
