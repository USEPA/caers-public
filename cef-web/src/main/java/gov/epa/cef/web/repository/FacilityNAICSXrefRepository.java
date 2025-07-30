/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
