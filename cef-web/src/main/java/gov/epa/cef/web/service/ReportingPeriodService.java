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
