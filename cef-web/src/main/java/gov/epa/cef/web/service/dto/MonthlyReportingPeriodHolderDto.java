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

import com.google.common.base.Strings;

import java.math.BigDecimal;

public class MonthlyReportingPeriodHolderDto {

    private static final long serialVersionUID = 1L;

    // unit
    private Long emissionsUnitId;
    private String unitIdentifier;
    private String unitInitialMonthlyReportingPeriod;

    // process
    private Long emissionsProcessId;
    private String emissionsProcessIdentifier;
    private String emissionsProcessDescription;
    private String emissionsProcessSccCode;
    private String processInitialMonthlyReportingPeriod;

    // reporting period
    private Long reportingPeriodId;
    private CodeLookupDto calculationMaterialCode;
    private String calculationParameterValue;
    private UnitMeasureCodeDto calculationParameterUom;
    private CodeLookupDto fuelUseMaterialCode;
    private String fuelUseValue;
    private UnitMeasureCodeDto fuelUseUom;
    private String period;

    // operating details
    private BigDecimal actualHoursPerPeriod;
    private BigDecimal avgHoursPerDay;
    private BigDecimal avgDaysPerWeek;
    private BigDecimal avgWeeksPerPeriod;

    public Long getEmissionsUnitId() {
        return emissionsUnitId;
    }

    public void setEmissionsUnitId(Long emissionsUnitId) {
        this.emissionsUnitId = emissionsUnitId;
    }

    public String getUnitIdentifier() {
        return unitIdentifier;
    }

    public void setUnitIdentifier(String unitIdentifier) {
        this.unitIdentifier = unitIdentifier;
    }

    public String getUnitInitialMonthlyReportingPeriod() {
        return unitInitialMonthlyReportingPeriod;
    }

    public void setUnitInitialMonthlyReportingPeriod(String unitInitialMonthlyReportingPeriod) {
        this.unitInitialMonthlyReportingPeriod = unitInitialMonthlyReportingPeriod;
    }

    public Long getEmissionsProcessId() {
        return emissionsProcessId;
    }

    public void setEmissionsProcessId(Long emissionsProcessId) {
        this.emissionsProcessId = emissionsProcessId;
    }

    public String getEmissionsProcessIdentifier() {
        return emissionsProcessIdentifier;
    }

    public void setEmissionsProcessIdentifier(String emissionsProcessIdentifier) {
        this.emissionsProcessIdentifier = emissionsProcessIdentifier;
    }

    public String getEmissionsProcessDescription() {
        return emissionsProcessDescription;
    }

    public void setEmissionsProcessDescription(String emissionsProcessDescription) {
        this.emissionsProcessDescription = emissionsProcessDescription;
    }

    public String getEmissionsProcessSccCode() {
        return emissionsProcessSccCode;
    }

    public void setEmissionsProcessSccCode(String emissionsProcessSccCode) {
        this.emissionsProcessSccCode = emissionsProcessSccCode;
    }

    public Long getReportingPeriodId() {
        return reportingPeriodId;
    }

    public void setReportingPeriodId(Long reportingPeriodId) {
        this.reportingPeriodId = reportingPeriodId;
    }

    public CodeLookupDto getCalculationMaterialCode() {
        return calculationMaterialCode;
    }

    public void setCalculationMaterialCode(CodeLookupDto calculationMaterialCode) {
        this.calculationMaterialCode = calculationMaterialCode;
    }

    // this was changed to a string to keep the numbers precise,
    // however mapstruct cannot convert an empty string to a BigDecimal
    // so this will make sure null is returned instead of an empty string
    public String getCalculationParameterValue() {
        return Strings.emptyToNull(calculationParameterValue);
    }

    public void setCalculationParameterValue(String calculationParameterValue) {
        this.calculationParameterValue = calculationParameterValue;
    }

    public UnitMeasureCodeDto getCalculationParameterUom() {
        return calculationParameterUom;
    }

    public void setCalculationParameterUom(UnitMeasureCodeDto calculationParameterUom) {
        this.calculationParameterUom = calculationParameterUom;
    }

    public CodeLookupDto getFuelUseMaterialCode() {
        return fuelUseMaterialCode;
    }

    public void setFuelUseMaterialCode(CodeLookupDto fuelUseMaterialCode) {
        this.fuelUseMaterialCode = fuelUseMaterialCode;
    }

    // this was changed to a string to keep the numbers precise,
    // however mapstruct cannot convert an empty string to a BigDecimal
    // so this will make sure null is returned instead of an empty string
    public String getFuelUseValue() { return Strings.emptyToNull(fuelUseValue); }

    public void setFuelUseValue(String fuelUseValue) {
        this.fuelUseValue = fuelUseValue;
    }

    public UnitMeasureCodeDto getFuelUseUom() {
        return fuelUseUom;
    }

    public void setFuelUseUom(UnitMeasureCodeDto fuelUseUom) {
        this.fuelUseUom = fuelUseUom;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public BigDecimal getActualHoursPerPeriod() {
        return actualHoursPerPeriod;
    }

    public void setActualHoursPerPeriod(BigDecimal actualHoursPerPeriod) {
        this.actualHoursPerPeriod = actualHoursPerPeriod;
    }

    public BigDecimal getAvgHoursPerDay() {
        return avgHoursPerDay;
    }

    public void setAvgHoursPerDay(BigDecimal avgHoursPerDay) {
        this.avgHoursPerDay = avgHoursPerDay;
    }

    public BigDecimal getAvgDaysPerWeek() {
        return avgDaysPerWeek;
    }

    public void setAvgDaysPerWeek(BigDecimal avgDaysPerWeek) {
        this.avgDaysPerWeek = avgDaysPerWeek;
    }

    public BigDecimal getAvgWeeksPerPeriod() {
        return avgWeeksPerPeriod;
    }

    public void setAvgWeeksPerPeriod(BigDecimal avgWeeksPerPeriod) {
        this.avgWeeksPerPeriod = avgWeeksPerPeriod;
    }

    public String getProcessInitialMonthlyReportingPeriod() {
        return processInitialMonthlyReportingPeriod;
    }

    public void setProcessInitialMonthlyReportingPeriod(String processInitialMonthlyReportingPeriod) {
        this.processInitialMonthlyReportingPeriod = processInitialMonthlyReportingPeriod;
    }
}
