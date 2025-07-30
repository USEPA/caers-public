/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
