/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
