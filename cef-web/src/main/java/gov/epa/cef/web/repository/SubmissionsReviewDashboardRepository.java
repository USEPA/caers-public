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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gov.epa.cef.web.domain.ReportStatus;
import gov.epa.cef.web.domain.SubmissionsReviewDashboardView;

public interface SubmissionsReviewDashboardRepository extends JpaRepository<SubmissionsReviewDashboardView, Long> {

    List<SubmissionsReviewDashboardView> findByReportStatusAndProgramSystemCode(ReportStatus reportStatus, String programSystemCode);
    
	List<SubmissionsReviewDashboardView> findByYearAndReportStatusAndProgramSystemCode(Short year, ReportStatus reportStatus, String programSystemCode);
    
    List<SubmissionsReviewDashboardView> findByYearAndProgramSystemCode(Short year, String programSystemCode); 
    
    List<SubmissionsReviewDashboardView> findByProgramSystemCode(String programSystemCode);
    
    List<SubmissionsReviewDashboardView> findByYearAndMidYearSubmissionStatusAndProgramSystemCode(Short year, ReportStatus midYearSubmissionStatus, String programSystemCode);
    
    List<SubmissionsReviewDashboardView> findByMidYearSubmissionStatusAndProgramSystemCode(ReportStatus midYearSubmissionStatus, String programSystemCode);
    
    @Query("select sr from SubmissionsReviewDashboardView sr where sr.year = :year and sr.reportStatus = :reportStatus and sr.programSystemCode = :programSystemCode and sr.thresholdStatus = 'OPERATING_BELOW_THRESHOLD'")
    List<SubmissionsReviewDashboardView> findByYearAndReportStatusAndProgramSystemCodeAndIsOptOut(@Param("year") Short year, @Param("reportStatus") ReportStatus reportStatus, @Param("programSystemCode") String programSystemCode);
    
    @Query("select sr from SubmissionsReviewDashboardView sr where sr.year = :year and sr.reportStatus = :reportStatus and sr.programSystemCode = :programSystemCode "
	 		+ "and (sr.thresholdStatus in ('OPERATING_ABOVE_THRESHOLD', 'PERM_SHUTDOWN', 'TEMP_SHUTDOWN') or sr.thresholdStatus is null)")
    List<SubmissionsReviewDashboardView> findByYearAndReportStatusAndProgramSystemCodeAndNotOptOut(@Param("year") Short year, @Param("reportStatus") ReportStatus reportStatus, @Param("programSystemCode") String programSystemCode);
    
    @Query("select sr from SubmissionsReviewDashboardView sr where sr.reportStatus = :reportStatus and sr.programSystemCode = :programSystemCode and sr.thresholdStatus = 'OPERATING_BELOW_THRESHOLD'")
    List<SubmissionsReviewDashboardView> findByReportStatusAndProgramSystemCodeAndIsOptOut(@Param("reportStatus") ReportStatus reportStatus, @Param("programSystemCode") String programSystemCode);
    
    @Query("select sr from SubmissionsReviewDashboardView sr where sr.reportStatus = :reportStatus and sr.programSystemCode = :programSystemCode "
			+ "and (sr.thresholdStatus in ('OPERATING_ABOVE_THRESHOLD', 'PERM_SHUTDOWN', 'TEMP_SHUTDOWN') or sr.thresholdStatus is null)")
    List<SubmissionsReviewDashboardView> findByReportStatusAndProgramSystemCodeAndNotOptOut(@Param("reportStatus") ReportStatus reportStatus, @Param("programSystemCode") String programSystemCode);
    
}
