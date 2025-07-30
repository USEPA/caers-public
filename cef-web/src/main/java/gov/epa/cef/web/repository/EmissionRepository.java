/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.repository;

import gov.epa.cef.web.config.CacheName;
import gov.epa.cef.web.domain.common.IReportComponent;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import gov.epa.cef.web.domain.Emission;

import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface EmissionRepository extends CrudRepository<Emission, Long>, ProgramIdRetriever, ReportIdRetriever, ReportComponentRetriever {

	/**
   * Find all Emissions with the specified report id
   * @param reportId
   * @return
   */
	@Query("select e from Emission e join e.reportingPeriod rp join rp.emissionsProcess ep join ep.emissionsUnit eu join eu.facilitySite fs join fs.emissionsReport r "
			+ "where r.id = :reportId and ep.operatingStatusCode.code = 'OP' and r.isDeleted = false")
	List<Emission> findAllByReportId(@Param("reportId") Long reportId);

  /**
   * Find all Emissions, including Operating Details, with the specified report id when components are operating
   * or the facility is a landfill
   * @param reportId
   * @return
   */
  @Query("select e from Emission e join e.reportingPeriod rp join rp.operatingDetails o "
      + "join rp.emissionsProcess ep join ep.emissionsUnit eu join eu.facilitySite fs join fs.emissionsReport r "
      + "where r.id = :reportId and r.isDeleted = false "
      + "and (fs.facilitySourceTypeCode.code = '104' "
          + "or (ep.operatingStatusCode.code = 'OP' "
              + "and eu.operatingStatusCode.code = 'OP' "
              + "and fs.operatingStatusCode.code = 'OP'))")
  List<Emission> findAllByReportIdWithOperatingDetails(@Param("reportId") Long reportId);

	/**
   * Find all Emissions with the specified process id
   * @param processId
   * @param reportId
   * @return
   */
	@Query("select e from Emission e join e.reportingPeriod rp join rp.emissionsProcess ep join ep.emissionsUnit eu join eu.facilitySite fs join fs.emissionsReport r where ep.id = :processId and r.id = :reportId and r.isDeleted = false")
	List<Emission> findAllByProcessIdReportId(@Param("processId") Long processId, @Param("reportId") Long reportId);

    /**
     * Find all Annual Emissions with the specified process id
     * @param processId
     * @param reportId
     * @return
     */
    @Query("select e from Emission e join e.reportingPeriod rp join rp.reportingPeriodTypeCode rpc join rp.emissionsProcess ep join ep.emissionsUnit eu join eu.facilitySite fs join fs.emissionsReport r "
            + "where ep.id = :processId and r.id = :reportId and rpc.code = 'A' and r.isDeleted = false")
    List<Emission> findAllAnnualByProcessIdReportId(@Param("processId") Long processId, @Param("reportId") Long reportId);

    /**
     * Retrieve a specific Emission for a specific year
     * @param pollutantCode
     * @param rpTypeCode
     * @param processIdentifier
     * @param unitIdentifier
     * @param eisProgramId
     * @param year
     * @return
     */
    @Query("select e from Emission e join e.reportingPeriod rp join rp.emissionsProcess ep join ep.emissionsUnit eu join eu.facilitySite fs join fs.emissionsReport r "
            + "where  e.pollutant.pollutantCode = :pollutantCode and rp.reportingPeriodTypeCode.code = :rpTypeCode "
            + "and ep.emissionsProcessIdentifier = :processIdentifier and eu.unitIdentifier = :unitIdentifier "
            + "and r.eisProgramId = :eisProgramId and r.year = :year and r.isDeleted = false")
    List<Emission> retrieveMatchingForYear(@Param("pollutantCode") String pollutantCode, @Param("rpTypeCode") String rpTypeCode,
            @Param("processIdentifier") String processIdentifier, @Param("unitIdentifier") String unitIdentifier,
            @Param("eisProgramId") String eisProgramId, @Param("year") Short year);

    @Cacheable(value = CacheName.EmissionMasterIds)
    @Query("select mfr.id "
            + "from Emission e join e.reportingPeriod rp join rp.emissionsProcess p join p.emissionsUnit eu join eu.facilitySite fs "
            + "join fs.emissionsReport r join r.masterFacilityRecord mfr where e.id = :id and r.isDeleted = false")
    Optional<Long> retrieveMasterFacilityRecordIdById(@Param("id") Long id);

    /**
    * Retrieve Emissions Report id for an Emission
    * @param id
    * @return Emissions Report id
    */
   @Cacheable(value = CacheName.EmissionEmissionsReportIds)
   @Query("select r.id from Emission e join e.reportingPeriod rp join rp.emissionsProcess p join p.emissionsUnit eu join eu.facilitySite fs join fs.emissionsReport r where e.id = :id and r.isDeleted = false")
   Optional<Long> retrieveEmissionsReportById(@Param("id") Long id);

   /**
    * Retrieve a list of all emissions for a specific program system code and emissions reporting year where the associated facility is operating
    * @param psc Program System Code
    * @param emissionsReportYear
    * @return
    */
   @Query("select e from Emission e join e.reportingPeriod rp join rp.emissionsProcess ep join ep.emissionsUnit eu join eu.facilitySite fs join fs.emissionsReport er where er.programSystemCode.code = :psc and er.year = :emissionsReportYear "
       + "and (er.thresholdStatus in (gov.epa.cef.web.domain.ThresholdStatus.OPERATING_ABOVE_THRESHOLD) or er.thresholdStatus is null) "
       + "and fs.operatingStatusCode.code = 'OP' and ep.operatingStatusCode.code = 'OP' and er.isDeleted = false")
   List<Emission> findByPscAndEmissionsReportYear(String psc, Short emissionsReportYear);

    @Query("select rc from #{#entityName} rc where rc.id = ?1")
    Optional<IReportComponent> retrieveReportComponentById(Long id);

    List<Emission> findByAnnualEmissionId(Long annualEmissionId);

    /**
     * Delete by facility site ID
     */
    @Transactional
    @Modifying
    @Query("delete from Emission where id in (select e.id from Emission e join e.reportingPeriod rp join rp.emissionsProcess ep " +
                                                "join ep.emissionsUnit eu join eu.facilitySite fs where fs.id = :facilitySiteId)")
    void deleteByFacilitySite(Long facilitySiteId);
}
