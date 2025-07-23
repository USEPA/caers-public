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

import gov.epa.cef.web.domain.Emission;
import gov.epa.cef.web.domain.EmissionsProcess;
import gov.epa.cef.web.domain.ReportingPeriod;
import gov.epa.cef.web.service.dto.*;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionFormulaVariableBulkUploadDto;
import gov.epa.cef.web.util.ReportCreationContext;

public interface EmissionService {

    /**
     * Create a new Emission
     * @param dto
     * @return
     */
    public EmissionDto create(EmissionDto dto);

    /**
     * Retrieve Emission by id
     * @param id
     * @return
     */
    public EmissionDto retrieveById(Long id);

    /**
     * Retrieve Emission by id and generate missing variables
     * @param id
     * @return
     */
    public EmissionDto retrieveWithVariablesById(Long id);

    /**
     * Update an existing Emission
     * @param dto
     * @return
     */
    public EmissionDto update(EmissionDto dto);

    /**
     * Delete an Emission for a given id
     * @param id
     */
    public void delete(Long id);

    /**
     * Retrieve Emissions grouped by Reporting Period for Bulk Entry
     * @param facilitySiteId
     * @return
     */
    public List<EmissionBulkEntryHolderDto> retrieveBulkEntryEmissionsForFacilitySite(Long facilitySiteId);

    /**
     * Update the total emissions for the provided emissions and recalculate all emissions for the facility
     * @param facilitySiteId
     * @param dtos
     * @return
     */
    public List<EmissionBulkEntryHolderDto> bulkUpdate(Long facilitySiteId, List<EmissionDto> dtos);

    /**
     * Recalculate the total emissions in tons for all emissions in a report without changing any other values
     * @param reportId
     * @return
     */
    public List<EmissionDto> recalculateEmissionTons(Long reportId);

    /**
     * Calculate total emissions for an emission. Also calculates emission factor if it uses a formula
     * This method should be used when the Reporting Period in the database should be used for calculations
     * and you have an EmissionDto, probably with values that differ from the ones in the database.
     * @param dto
     * @return
     */
    public EmissionDto calculateTotalEmissions(EmissionDto dto);

    /**
     * Calculate total emissions for an emission and reporting period. Also calculates emission factor if it uses a formula
     * This method should be used when you need to specify a Reporting Period with a different throughput or UoM than the
     * one in the database.
     * @param emission
     * @param rp
     * @return
     */
    public Emission calculateTotalEmissions(Emission emission, ReportingPeriod rp);

    /**
     * Calculate total emissions for an emission and reporting period. Also calculates emission factor if it uses a formula
     * This method should be used when you need to specify a Reporting Period with a different throughput or UoM than the
     * one in the database, and the report is not yet fully assembled (bulk uploads).
     * @param emission
     * @param rp
     * @param emissionsReportYear
     * @return
     */
    public Emission calculateTotalEmissions(Emission emission, ReportingPeriod rp, Short emissionsReportYear);

    /**
     * Calculate emission factor for an emission if a formula is present.
     * This method should be used when you need to calculate the emission factor without calculating the total emissions.
     * @param dto
     * @return
     */
    public EmissionDto calculateEmissionFactorDto(EmissionDto dto);

    /**
     * Find Emission by TRI Facility ID and CAS Number.
     * This method is the second version of the interface to TRIMEweb so that TRI users can
     * see what emissions have been reported to the Common Emissions Form for the current
     * facility and chemical that they are working on. This version takes a TRIFID and looks
     * up the EIS ID in CAERS and then finds any existing emissions.
     *
     * @param trifid
     * @param pollutantCasId
     * @return
     */
    public EmissionsByFacilityAndCASDto findEmissionsByTrifidAndCAS(String trifid, String pollutantCasId);

    /**
     * Delete Emissions by facility site id
     * @param facilitySiteId
     */
    void deleteByFacilitySite(Long facilitySiteId);

    /**
     * Retrieve Emissions grouped by Reporting Period for Monthly Reporting
     * @param facilitySiteId
     * @param period
     * @return
     */
    List<MonthlyReportingEmissionHolderDto> retrieveMonthlyEmissionsForFacilitySitePeriod(Long facilitySiteId, String period);

  /**
   * Retrieve all semiannual or annual monthly emissions for report download
   * @param reportId
   * @param isSemiAnnual
   * @return
   */
    List<MonthlyReportingDownloadDto> retrieveMonthlyDownLoadDtoListForReport(Long reportId, boolean isSemiAnnual);

    /**
     * Retrieve all emissions for semiannual for Monthly Reporting
     * @param facilitySiteId
     * @return
     */
    List<MonthlyReportingEmissionHolderDto> retrieveAllSemiAnnualMonthlyEmissionsForFacilitySite(Long facilitySiteId);

    /**
     * Retrieve all emissions for annual for Monthly Reporting
     * @param facilitySiteId
     * @return
     */
    List<MonthlyReportingEmissionHolderDto> retrieveAllAnnualMonthlyEmissionsForFacilitySite(Long facilitySiteId);

    /**
     * Update the monthly rate and total emissions for the provided emissions
     * @param facilitySiteId
     * @param period
     * @param dtos
     * @return
     */
    List<MonthlyReportingEmissionHolderDto> monthlyUpdate(Long facilitySiteId, String period, List<EmissionDto> dtos);

    /**
     * Retrieve a list of emissions for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    List<EmissionBulkUploadDto> retrieveEmissions(String programSystemCode, Short emissionsReportYear);

    /**
     * Retrieve a list of emission formula variables for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    List<EmissionFormulaVariableBulkUploadDto> retrieveEmissionFormulaVariables(String programSystemCode, Short emissionsReportYear);

    /**
     * Update emission factor description where possible
     * @param emission
     * @return
     */
    Emission updateEmissionFactorDetails(Emission emission, EmissionsProcess process, Short emissionsReportYear, ReportCreationContext context);

    /**
     * Recalculate total emissions when creating new or upload report
     * @param emission
     * @return
     */
    Emission recalculateTotalEmissions(Emission emission, Short reportYear, ReportCreationContext context);

    /**
     * Recalculate annual and semiannual emissions
     * @param emission
     * @return
     */
    List<Emission> updateAnnualAndSemiAnnualEmissions(Emission emission);

    /**
     * Get list of pollutants created after semiannual report has been submitted
     * @param processId
     * @param reportId
     * @return
     */
    List<String> getEmissionsCreatedAfterSemiannualSubmission(Long processId, Long reportId);

    /**
     * Calculate the total emissions in tons for the given emission
     * @param emission
     * @return
     */
    BigDecimal calculateEmissionTons(Emission emission);
}
