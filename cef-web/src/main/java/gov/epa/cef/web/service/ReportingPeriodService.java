/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service;

import java.math.BigDecimal;
import java.util.List;

import gov.epa.cef.web.domain.MonthlyFuelReporting;
import gov.epa.cef.web.domain.ReportingPeriod;
import gov.epa.cef.web.service.dto.*;
import gov.epa.cef.web.service.dto.bulkUpload.ReportingPeriodBulkUploadDto;

public interface ReportingPeriodService {

    /**
     * Create a new Reporting Period
     * @param dto
     * @return
     */
    public ReportingPeriodDto create(ReportingPeriodDto dto);

    /**
     * Update a Reporting Period
     * @param dto
     * @return
     */
    public ReportingPeriodUpdateResponseDto update(ReportingPeriodDto dto);

    /**
     * Retrieve Reporting Period by id
     * @param id
     * @return
     */
    ReportingPeriodDto retrieveById(Long id);

    /**
     * Retrieve Reporting Periods for an emissions process
     * @param processId
     * @return
     */
    List<ReportingPeriodDto> retrieveForEmissionsProcess(Long processId);

    /**
     * Retrieve annual Reporting Period for an emissions process
     * @param processId
     * @return
     */
    List<ReportingPeriodDto> retrieveAnnualForEmissionsProcess(Long processId);

    /**
     * Retrieve Reporting Periods for Bulk Entry for a specific facility site
     * @param facilitySiteId
     * @return
     */
    public List<ReportingPeriodBulkEntryDto> retrieveBulkEntryReportingPeriodsForFacilitySite(Long facilitySiteId);

    /**
     * Update the throughput for multiple Reporting Periods at once
     * @param facilitySiteId
     * @param dtos
     * @return
     */
    public List<EmissionBulkEntryHolderDto> bulkUpdate(Long facilitySiteId, List<ReportingPeriodBulkEntryDto> dtos);

    /**
     * Retrieve a list of reporting periods for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    List<ReportingPeriodBulkUploadDto>retrieveReportingPeriods(String programSystemCode, Short emissionsReportYear);

    /**
     * Update the fuel use value for multiple Reporting Periods at once from monthly reporting fuel use values
     * @param monthlyReports
     * @return
     */
    public List<ReportingPeriodDto> monthlyReportingUpdateReportingPeriod(List<MonthlyFuelReporting> monthlyReports);

    /**
     * Calculate non-point fuel use for all reporting periods without changing any other values
     * @return
     */
    public List<ReportingPeriodDto> calculateNonPointStandardizedFuelUse();

    /**
     * Calculate non-point fuel use for a specifiec reporting period without changing any other values
     * @return
     */
    public BigDecimal calculateFuelUseNonPointStandardized(ReportingPeriod period);

    /**
     * Zero out necessary fields when copying reporting periods for monthly reporting
     * @return
     */
    ReportingPeriod nullMonthlyReportingFields(ReportingPeriod period);

    /**
     * Retrieve Monthly Reporting Periods for a specific facility site and month
     * @param facilitySiteId
     * @param period
     * @return
     */
    List<MonthlyReportingPeriodHolderDto> retrieveMonthlyReportingPeriodsForFacilitySite(Long facilitySiteId, String period);

    /**
     * Update monthly reporting info for multiple Reporting Periods at once
     * @param facilitySiteId
     * @param period
     * @param dtos
     * @return
     */
    List<MonthlyReportingEmissionHolderDto> monthlyUpdate(Long facilitySiteId, String period, List<MonthlyReportingPeriodHolderDto> dtos);

    /**
     * Get all semi-annual monthly reporting periods associated with a facility site
     * @param facilitySiteId
     * @return
     */
    List<MonthlyReportingPeriodHolderDto> retrieveAllSemiAnnualMonthlyReportingPeriodsForFacilitySite(Long facilitySiteId);

    /**
     * Get all annual monthly reporting periods associated with a facility site
     * @param facilitySiteId
     * @return
     */
    List<MonthlyReportingPeriodHolderDto> retrieveAllAnnualMonthlyReportingPeriodsForFacilitySite(Long facilitySiteId);

    /**
     * Delete Reporting Periods by facility site id
     * @param facilitySiteId
     */
    void deleteByFacilitySite(Long facilitySiteId);
}
