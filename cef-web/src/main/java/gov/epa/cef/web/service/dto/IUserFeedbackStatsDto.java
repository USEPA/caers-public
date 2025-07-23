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
package gov.epa.cef.web.service.dto;

/**
 * Created interface to use Spring Data projections to retrieve results from 
 * SQL queries with aggregate functions.
 * https://www.baeldung.com/jpa-queries-custom-result-with-aggregation-functions
 */

public interface IUserFeedbackStatsDto {

		Long getIntuitiveRateAvg();
	    Long getDataEntryScreensAvg();
	    Long getDataEntryBulkUploadAvg();
	    Long getCalculationScreensAvg();
	    Long getControlsAndControlPathAssignAvg();
	    Long getQualityAssuranceChecksAvg();
	    Long getOverallReportingTimeAvg();
	    
}
