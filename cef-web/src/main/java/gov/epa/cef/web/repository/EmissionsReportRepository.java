/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.repository;

import gov.epa.cef.web.config.CacheName;
import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.domain.common.BaseLookupEntity;
import gov.epa.cef.web.service.dto.EisDataCriteria;
import gov.epa.cef.web.service.dto.EisDataStatsDto;
import net.exchangenetwork.wsdl.register.program_facility._1.ProgramFacility;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EmissionsReportRepository extends CrudRepository<EmissionsReport, Long>, JpaSpecificationExecutor<EmissionsReport>, ProgramIdRetriever {

    /**
     * Find reports for a given eisProgramId
     * @param eisProgramId {@link ProgramFacility}'s programId
     * @return
     */
    @Query("select r from EmissionsReport r where r.eisProgramId = :eisProgramId and r.isDeleted = false")
    List<EmissionsReport> findByEisProgramId(String eisProgramId);

    /**
     * Find reports for a given masterFacilityRecordId
     * @param id
     * @return
     */
    @Query("select r from EmissionsReport r where r.masterFacilityRecord.id = :id and r.isDeleted = false")
    List<EmissionsReport> findByMasterFacilityRecordId(Long id);

    /**
     * Find reports for a given eisProgramId  with the specified order
     * @param eisProgramId {@link ProgramFacility}'s programId
     * @return
     */
    @Query("select r from EmissionsReport r where r.eisProgramId = :eisProgramId and r.isDeleted = false")
    List<EmissionsReport> findByEisProgramId(String eisProgramId, Sort sort);

    /**
     * Find report for a given masterFacilityRecordId with the specified order
     * @param id
     * @param sort
     * @return
     */
    @Query("select r from EmissionsReport r where r.masterFacilityRecord.id = :id and r.isDeleted = false")
    List<EmissionsReport> findByMasterFacilityRecordId(Long id, Sort sort);

    @Query("select r from FacilitySite fs join fs.emissionsReport r where fs.id = :facilitySiteId and r.isDeleted = false")
    Optional<EmissionsReport> findByFacilitySiteId(Long facilitySiteId);



    /***
     * Retrieve facilities based on master facility record id and status
     * @param masterFacilityRecordId
     * @param status (APPROVED, IN_PROGRESS, etc)
     * @return
     */
    @Query("select r from EmissionsReport r where r.masterFacilityRecord.id = :masterFacilityId "
        + "and r.status in (gov.epa.cef.web.domain.ReportStatus.IN_PROGRESS, gov.epa.cef.web.domain.ReportStatus.RETURNED) "
        + "and r.isDeleted = false")
    List<EmissionsReport> findInProgressOrReturnedByMasterFacilityId(@Param ("masterFacilityId") @NotNull Long masterFacilityId);

    /**
     * Find the report for a specified master facility record id for the specified year
     * @param masterFacilityRecordId
     * @param year
     * @return
     */
    @Query("select r from EmissionsReport r where r.masterFacilityRecord.id = :masterFacilityRecordId and r.year = :year and r.isDeleted = false")
    Optional<EmissionsReport> findByMasterFacilityRecordIdAndYear(@NotNull Long masterFacilityRecordId, @NotNull Short year);

    /**
     * Find the most recent report for the specified master facility record id before the specified year
     * @param masterFacilityRecordId
     * @param year
     * @return
     */
    Optional<EmissionsReport> findFirstByMasterFacilityRecordIdAndIsDeletedIsFalseAndYearLessThanOrderByYearDesc(@NotNull Long masterFacilityRecordId, @NotNull Short year);


    @Query("select r from EmissionsReport r where r.programSystemCode.code = :#{#crit.programSystemCode} and r.year = :#{#crit.reportingYear} and r.status = gov.epa.cef.web.domain.ReportStatus.APPROVED and r.isDeleted = false "
    		+ "and (r.thresholdStatus in (gov.epa.cef.web.domain.ThresholdStatus.OPERATING_ABOVE_THRESHOLD, gov.epa.cef.web.domain.ThresholdStatus.PERM_SHUTDOWN, gov.epa.cef.web.domain.ThresholdStatus.TEMP_SHUTDOWN) or r.thresholdStatus is null)")
    Collection<EmissionsReport> findEisDataByYearAndNotComplete(@Param("crit") EisDataCriteria criteria);

    @Query("select r from EmissionsReport r where r.programSystemCode.code = :#{#crit.programSystemCode} and r.year = :#{#crit.reportingYear} and r.eisLastSubmissionStatus = :#{#crit.submissionStatus} and r.status = gov.epa.cef.web.domain.ReportStatus.APPROVED and r.isDeleted = false "
    		+ "and (r.thresholdStatus in (gov.epa.cef.web.domain.ThresholdStatus.OPERATING_ABOVE_THRESHOLD, gov.epa.cef.web.domain.ThresholdStatus.PERM_SHUTDOWN, gov.epa.cef.web.domain.ThresholdStatus.TEMP_SHUTDOWN) or r.thresholdStatus is null)")
    Collection<EmissionsReport> findEisDataByYearAndStatus(@Param("crit") EisDataCriteria criteria);

    @Query("select r.eisLastSubmissionStatus as status, count(r.id) as count from EmissionsReport r where r.year = :year and r.programSystemCode.code = :programSystemCode and r.status = gov.epa.cef.web.domain.ReportStatus.APPROVED and r.isDeleted = false "
    		+ "and (r.thresholdStatus in (gov.epa.cef.web.domain.ThresholdStatus.OPERATING_ABOVE_THRESHOLD, gov.epa.cef.web.domain.ThresholdStatus.PERM_SHUTDOWN, gov.epa.cef.web.domain.ThresholdStatus.TEMP_SHUTDOWN) or r.thresholdStatus is null) "
    		+ "group by r.eisLastSubmissionStatus")
    Collection<EisDataStatsDto.EisDataStatusStat> findEisDataStatusesByYear(@Param("programSystemCode") String programSystemCode, @Param("year") Short year);

    @Query("select distinct r.year from EmissionsReport r where r.programSystemCode.code = :programSystemCode and r.status = gov.epa.cef.web.domain.ReportStatus.APPROVED and r.isDeleted = false "
    		+ "and (r.thresholdStatus in (gov.epa.cef.web.domain.ThresholdStatus.OPERATING_ABOVE_THRESHOLD, gov.epa.cef.web.domain.ThresholdStatus.PERM_SHUTDOWN, gov.epa.cef.web.domain.ThresholdStatus.TEMP_SHUTDOWN) "
    		+ "or r.thresholdStatus is null) order by r.year desc")
    Collection<Integer> findEisDataYears(@Param("programSystemCode") String programSystemCode);

    @Query("select distinct r.programSystemCode FROM EmissionsReport r where r.isDeleted = false")
    List<BaseLookupEntity> findDistinctProgramSystems();

    @Query("select distinct r.year from EmissionsReport r where r.programSystemCode.code = :programSystemCode and r.isDeleted = false order by r.year desc")
    List<Integer> findDistinctReportingYears(@Param("programSystemCode") String programSystemCode);

    @Query("select distinct r.year from EmissionsReport r where r.programSystemCode.code = :programSystemCode and r.midYearSubmissionStatus <> null and r.isDeleted = false order by r.year desc")
    List<Integer> findDistinctReportingYearsWithMonthlyReportingEnabled(@Param("programSystemCode") String programSystemCode);

    @Cacheable(value = CacheName.ReportMasterIds)
    @Query("select mfr.id from EmissionsReport r join r.masterFacilityRecord mfr where r.id = :id")
    Optional<Long> retrieveMasterFacilityRecordIdById(@Param("id") Long id);

    /**
     * Get reports to be deleted
     * @return
     */
    @Query("select r from EmissionsReport r where r.isDeleted = true")
    List<EmissionsReport> findDeletedReports();
}
