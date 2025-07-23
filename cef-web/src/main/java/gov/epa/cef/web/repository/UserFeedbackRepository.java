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

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository; 
import org.springframework.data.repository.query.Param;
import gov.epa.cef.web.domain.UserFeedback;
import gov.epa.cef.web.service.dto.IUserFeedbackStatsDto;

import java.util.List;


public interface UserFeedbackRepository extends CrudRepository<UserFeedback, Long> {

    /**
     * Retrieve User Feedback for a Report Id
     * @param id
     * @return userFeedback
     */
    UserFeedback findByEmissionsReportId(@Param("id") Long id);
    
    /**
     * Retrieve a List of User Feedback for a Report Id
     * @param id
     * @return userFeedback
     */
    List<UserFeedback> findAllByEmissionsReportId(@Param("id") Long id);
	
    /**
     * Retrieve a List of User Feedback for a year and program system code
     * @param year
     * @param programSystemCode
     * @return userFeedback
     */
    List<UserFeedback> findByYearAndProgramSystemCodeCode(Short year, String programSystemCode, Sort sort);
    
    /**
     * Retrieve a List of User Feedback for a year and all agencies
     * @param year
     * @return userFeedback
     */
    List<UserFeedback> findAllByYear(Short year, Sort sort);
    
    @Query("select distinct programSystemCode.code FROM UserFeedback")
    List<String> findDistinctProgramSystems(Sort sort);
    
    @Query("select distinct year FROM UserFeedback")
    List<Short> findDistinctYears(Sort sort);
    
    @Query("select ROUND(AVG(intuitiveRating),0) as intuitiveRateAvg, ROUND(AVG(dataEntryScreens),0) as dataEntryScreensAvg, "
    		+ "ROUND(AVG(dataEntryBulkUpload),0) as dataEntryBulkUploadAvg, ROUND(AVG(calculationScreens),0) as calculationScreensAvg, "
    		+ "ROUND(AVG(controlsAndControlPathAssignments),0) as controlsAndControlPathAssignAvg, "
    		+ "ROUND(AVG(qualityAssuranceChecks),0) as qualityAssuranceChecksAvg, ROUND(AVG(overallReportingTime),0) as overallReportingTimeAvg "
    		+ "FROM UserFeedback WHERE year = :year AND programSystemCode.code = :programSystemCode")
    IUserFeedbackStatsDto findAvgByYearAndProgramSystem(@Param("year") Short year, @Param("programSystemCode") String programSystemCode);
    
    @Query("select ROUND(AVG(intuitiveRating),0) as intuitiveRateAvg, ROUND(AVG(dataEntryScreens),0) as dataEntryScreensAvg, "
    		+ "ROUND(AVG(dataEntryBulkUpload),0) as dataEntryBulkUploadAvg, ROUND(AVG(calculationScreens),0) as calculationScreensAvg, "
    		+ "ROUND(AVG(controlsAndControlPathAssignments),0) as controlsAndControlPathAssignAvg, "
    		+ "ROUND(AVG(qualityAssuranceChecks),0) as qualityAssuranceChecksAvg, ROUND(AVG(overallReportingTime),0) as overallReportingTimeAvg "
    		+ "FROM UserFeedback WHERE year = :year")
    IUserFeedbackStatsDto findAvgByYear(@Param("year") Short year);
    
}
