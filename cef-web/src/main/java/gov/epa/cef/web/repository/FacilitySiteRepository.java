/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
