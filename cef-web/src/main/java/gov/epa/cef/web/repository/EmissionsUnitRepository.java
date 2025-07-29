/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.repository;

import gov.epa.cef.web.config.CacheName;
import gov.epa.cef.web.domain.EmissionsUnit;
import gov.epa.cef.web.domain.common.IReportComponent;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmissionsUnitRepository extends CrudRepository<EmissionsUnit, Long>, ProgramIdRetriever, ReportIdRetriever, ReportComponentRetriever {

    /**
     * Retrieve Emissions Units for a facility
     * @param facilitySiteId
     * @return
     */
    List<EmissionsUnit> findByFacilitySiteIdOrderByUnitIdentifier(Long facilitySiteId);

    /**
     * Find Emissions Units with the specified identifier, master facility record id, and year
     * @param identifier
     * @param mfrId
     * @param year
     * @return
     */
    @Query("select eu from EmissionsUnit eu join eu.facilitySite fs join fs.emissionsReport r join r.masterFacilityRecord mfr where eu.unitIdentifier = :identifier and mfr.id = :mfrId and r.year = :year and r.isDeleted = false")
    List<EmissionsUnit> retrieveByIdentifierFacilityYear(@Param("identifier") String identifier, @Param("mfrId") Long mfrId, @Param("year") Short year);

    /**
     * Find Emissions Units with the specified identifier, master facility record id, and year
     * @param mfrId
     * @param year
     * @return
     */
    @Query("select eu from EmissionsUnit eu join eu.facilitySite fs join fs.emissionsReport r join r.masterFacilityRecord mfr where mfr.id = :mfrId and r.year = :year and r.isDeleted = false")
    List<EmissionsUnit> retrieveByFacilityYear(@Param("mfrId") Long mfrId, @Param("year") Short year);

    @Cacheable(value = CacheName.UnitMasterIds)
    @Query("select mfr.id from EmissionsUnit eu join eu.facilitySite fs join fs.emissionsReport r join r.masterFacilityRecord mfr where eu.id = :id and r.isDeleted = false")
    Optional<Long> retrieveMasterFacilityRecordIdById(@Param("id") Long id);

    /**
     * Retrieve Emissions Report id for an Emissions Unit
     * @param id
     * @return Emissions Report id
     */
    @Cacheable(value = CacheName.UnitEmissionsReportIds)
    @Query("select r.id from EmissionsUnit eu join eu.facilitySite fs join fs.emissionsReport r where eu.id = :id and r.isDeleted = false")
    Optional<Long> retrieveEmissionsReportById(@Param("id") Long id);

    /**
     * Retrieve a list of all emissions units for a specific program system code and emissions reporting year where the associated facility is operating
     * @param psc Program System Code
     * @param emissionsReportYear
     * @return
     */
    @Query("select eu from EmissionsUnit eu join eu.facilitySite fs join fs.emissionsReport er where er.programSystemCode.code = :psc and er.year = :emissionsReportYear "
        + "and (er.thresholdStatus in (gov.epa.cef.web.domain.ThresholdStatus.OPERATING_ABOVE_THRESHOLD) or er.thresholdStatus is null) and fs.operatingStatusCode.code = 'OP' and er.isDeleted = false")
    List<EmissionsUnit> findByPscAndEmissionsReportYear(String psc, Short emissionsReportYear);

    @Query("select rc from #{#entityName} rc where rc.id = ?1")
    Optional<IReportComponent> retrieveReportComponentById(Long id);
}
