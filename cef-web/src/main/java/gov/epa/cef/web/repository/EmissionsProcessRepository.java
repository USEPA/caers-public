/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.repository;

import gov.epa.cef.web.config.CacheName;
import gov.epa.cef.web.domain.EmissionsProcess;
import gov.epa.cef.web.domain.EmissionsUnit;

import gov.epa.cef.web.domain.common.IReportComponent;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmissionsProcessRepository extends CrudRepository<EmissionsProcess, Long>, ProgramIdRetriever, ReportIdRetriever, ReportComponentRetriever {

    @Query("select ep from EmissionsProcess ep join fetch ep.emissionsUnit eu join ep.releasePointAppts rpa where rpa.releasePoint.id = :releasePointId order by ep.emissionsProcessIdentifier")
    List<EmissionsProcess> findByReleasePointApptsReleasePointIdOrderByEmissionsProcessIdentifier(@Param("releasePointId") Long releasePointId);

    List<EmissionsProcess> findByEmissionsUnitIdOrderByEmissionsProcessIdentifier(Long emissionsUnitId);

    /**
     * Find Emissions Process with the specified identifier, parent identifier, master facility record id, and year
     * @param identifier
     * @param parentIdentifier
     * @param mfrId
     * @param year
     * @return
     */
    @Query("select ep from EmissionsProcess ep join ep.emissionsUnit eu join eu.facilitySite fs join fs.emissionsReport r join r.masterFacilityRecord mfr "
            + "where ep.emissionsProcessIdentifier = :identifier and eu.unitIdentifier = :parentIdentifier and mfr.id = :mfrId and r.year = :year and r.isDeleted = false")
    List<EmissionsProcess> retrieveByIdentifierParentFacilityYear(@Param("identifier") String identifier, @Param("parentIdentifier") String parentIdentifier,
                                                                      @Param("mfrId") Long mfrId, @Param("year") Short year);

    /**
     * Find Emissions Process with the specified identifier, parent identifier, master facility record id, and year
     * @param parentIdentifier
     * @param mfrId
     * @param year
     * @return
     */
    @Query("select ep from EmissionsProcess ep join ep.emissionsUnit eu join eu.facilitySite fs join fs.emissionsReport r join r.masterFacilityRecord mfr "
            + "where eu.unitIdentifier = :parentIdentifier and mfr.id = :mfrId and r.year = :year and r.isDeleted = false")
    List<EmissionsProcess> retrieveByParentFacilityYear(@Param("parentIdentifier") String parentIdentifier,
    															  @Param("mfrId") Long mfrId, @Param("year") Short year);

    @Cacheable(value = CacheName.ProcessMasterIds)
    @Query("select mfr.id from EmissionsProcess p join p.emissionsUnit eu join eu.facilitySite fs join fs.emissionsReport r join r.masterFacilityRecord mfr where p.id = :id and r.isDeleted = false")
    Optional<Long> retrieveMasterFacilityRecordIdById(@Param("id") Long id);

    /**
     * Retrieve Emissions Report id for an Emissions Process
     * @param id
     * @return Emissions Report id
     */
    @Cacheable(value = CacheName.ProcessEmissionsReportIds)
    @Query("select r.id from EmissionsProcess p join p.emissionsUnit eu join eu.facilitySite fs join fs.emissionsReport r where p.id = :id and r.isDeleted = false")
    Optional<Long> retrieveEmissionsReportById(@Param("id") Long id);

    /**
     * Retrieve a list of all emissions processes for a specific program system code and emissions reporting year where the associated facility is operating
     * @param psc Program System Code
     * @param emissionsReportYear
     * @return
     */
    @Query("select ep from EmissionsProcess ep join ep.emissionsUnit eu join eu.facilitySite fs join fs.emissionsReport er where er.programSystemCode.code = :psc and er.year = :emissionsReportYear "
        + "and (er.thresholdStatus in (gov.epa.cef.web.domain.ThresholdStatus.OPERATING_ABOVE_THRESHOLD) or er.thresholdStatus is null) and fs.operatingStatusCode.code = 'OP' and er.isDeleted = false")
    List<EmissionsProcess> findByPscAndEmissionsReportYear(String psc, Short emissionsReportYear);

    @Query("select rc from #{#entityName} rc where rc.id = ?1")
    Optional<IReportComponent> retrieveReportComponentById(Long id);
}
