/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
