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
package gov.epa.cef.web.service;

import java.util.List;

import gov.epa.cef.web.service.dto.MonthlyFuelReportingDownloadDto;
import gov.epa.cef.web.service.dto.MonthlyFuelReportingDto;

public interface MonthlyFuelReportingService {

    /**
     * Retrieve a list of monthly fuel reports for the given period (month/semi-annual/annual) and facility id
     * @param facilitySiteId
     * @param period
     * @return
     */
    public List<MonthlyFuelReportingDto> retrieveForMonthData(Long facilitySiteId, String period);

    /**
     * Retrieve a all reporting periods for monthly reporting that has an SCC that requires fuel use or is asphalt
     * for the given period and facility id
     * @param facilitySiteId
     * @param monthlyFuelData
     * @return
     */
    public List<MonthlyFuelReportingDto> retrieveMonthlyReportingReportingPeriodsForFacilitySite(Long facilitySiteId, List<MonthlyFuelReportingDto> monthlyFuelData);

    /**
     * Saves all monthly fuel data and updates associated fuel value in process reporting period
     * @param facilitySiteId
     * @param dtos
     * @return
     */
    public List<MonthlyFuelReportingDto> saveMonthlyReporting(Long facilitySiteId, List<MonthlyFuelReportingDto> dtos);

    /**
     * delete monthly report by process id
     * @param id
     * @return
     */
    public void deleteByProcess(Long id);

    /**
     * delete monthly report by unit id
     * @param id
     * @return
     */
    public void deleteByUnit(Long id);

    /**
     * Retrieve all reporting info for export
     * @param facilitySiteId
     * @return
     */
    public List<MonthlyFuelReportingDownloadDto> retrieveDownloadDtoByFacilitySiteId(Long facilitySiteId);


}
