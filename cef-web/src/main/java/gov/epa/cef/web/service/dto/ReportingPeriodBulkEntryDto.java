/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ReportingPeriodBulkEntryDto implements Serializable {

    private static final long serialVersionUID = 1L;

    // unit
    private Long emissionsUnitId;
    private String unitIdentifier;
    private String unitDescription;
    private CodeLookupDto unitStatus;

    // process
    private Long emissionsProcessId;
    private String emissionsProcessIdentifier;
    private String emissionsProcessDescription;
    private CodeLookupDto operatingStatusCode;
    private String emissionsProcessSccCode;
    private Boolean fuelUseRequired;

    // reporting period
    private Long reportingPeriodId;
    private CodeLookupDto reportingPeriodTypeCode;
    private String calculationParameterValue;
    private UnitMeasureCodeDto calculationParameterUom;
    private CodeLookupDto calculationMaterialCode;
    private String fuelUseValue;
    private UnitMeasureCodeDto fuelUseUom;
    private CodeLookupDto fuelUseMaterialCode;
    private String previousCalculationParameterValue;
    private String previousCalculationParameterUomCode;

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

    public String getUnitDescription() {
        return unitDescription;
    }

    public void setUnitDescription(String unitDescription) {
        this.unitDescription = unitDescription;
    }

    public CodeLookupDto getUnitStatus() {
        return unitStatus;
    }

    public void setUnitStatus(CodeLookupDto unitStatus) {
        this.unitStatus = unitStatus;
    }

    public Long getEmissionsProcessId() {
        return emissionsProcessId;
    }

    public void setEmissionsProcessId(Long emissionsProcessid) {
        this.emissionsProcessId = emissionsProcessid;
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

    public void setEmissionsProcessDescription(String processDescription) {
        this.emissionsProcessDescription = processDescription;
    }

    public CodeLookupDto getOperatingStatusCode() {
        return operatingStatusCode;
    }

    public void setOperatingStatusCode(CodeLookupDto operatingStatusCode) {
        this.operatingStatusCode = operatingStatusCode;
    }

    public Boolean getFuelUseRequired() {
        return fuelUseRequired;
    }

    public void setFuelUseRequired(Boolean fuelUseRequired) {
        this.fuelUseRequired = fuelUseRequired;
    }

    public Long getReportingPeriodId() {
        return reportingPeriodId;
    }

    public void setReportingPeriodId(Long reportingPeriodId) {
        this.reportingPeriodId = reportingPeriodId;
    }

    public CodeLookupDto getReportingPeriodTypeCode() {
        return reportingPeriodTypeCode;
    }

    public void setReportingPeriodTypeCode(CodeLookupDto reportingPeriodTypeCode) {
        this.reportingPeriodTypeCode = reportingPeriodTypeCode;
    }

    public String getCalculationParameterValue() {
        return calculationParameterValue;
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

    public CodeLookupDto getCalculationMaterialCode() {
        return calculationMaterialCode;
    }

    public void setCalculationMaterialCode(CodeLookupDto calculationMaterialCode) {
        this.calculationMaterialCode = calculationMaterialCode;
    }

    public String getEmissionsProcessSccCode() {
        return emissionsProcessSccCode;
    }

    public void setEmissionsProcessSccCode(String emissionsProcessSccCode) {
        this.emissionsProcessSccCode = emissionsProcessSccCode;
    }

    public String getFuelUseValue() {
        return fuelUseValue;
    }

    public void setFuelUseValue(String fuelUseValue) {
        this.fuelUseValue = fuelUseValue;
    }

    public UnitMeasureCodeDto getFuelUseUom() {
        return fuelUseUom;
    }

    public void setFuelUseUom(UnitMeasureCodeDto fuelUseUom) {
        this.fuelUseUom = fuelUseUom;
    }

    public CodeLookupDto getFuelUseMaterialCode() {
        return fuelUseMaterialCode;
    }

    public void setFuelUseMaterialCode(CodeLookupDto fuelUseMaterialCode) {
        this.fuelUseMaterialCode = fuelUseMaterialCode;
    }

    public String getPreviousCalculationParameterValue() {
        return previousCalculationParameterValue;
    }

    public void setPreviousCalculationParameterValue(String previousCalculationParameterValue) {
        this.previousCalculationParameterValue = previousCalculationParameterValue;
    }

    public String getPreviousCalculationParameterUomCode() {
        return previousCalculationParameterUomCode;
    }

    public void setPreviousCalculationParameterUomCode(String previousCalculationParameterUom) {
        this.previousCalculationParameterUomCode = previousCalculationParameterUom;
    }

}
