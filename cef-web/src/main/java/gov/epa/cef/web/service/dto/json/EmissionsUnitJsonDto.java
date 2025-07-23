package gov.epa.cef.web.service.dto.json;

import gov.epa.cef.web.service.dto.bulkUpload.EmissionsProcessBulkUploadDto;
import gov.epa.cef.web.service.dto.json.shared.MeasureJsonDto;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "EmissionsUnit")
public class EmissionsUnitJsonDto extends BaseFacilityComponentJsonDto {

    private static final long serialVersionUID = 1L;


    private MeasureJsonDto designCapacity;

    @NotBlank
    private LookupJsonDto unitTypeCode;

    private List<EmissionsProcessJsonDto> emissionsProcesses = new ArrayList<>();


    protected LocalDate unitOperationDate;
    protected String permitStatusCode;
    protected LocalDate permitStatusBeginYear;
    protected LocalDate permitStatusEndYear;
    protected String unitIsReadOnly;
    protected String designCapacityIsReadOnly;

    private MeasureJsonDto maximumTheoreticalEmissions;
    private MeasureJsonDto potentialToEmit;
    private String unitNaics;
    private String permitByRule;
    private String engineUseType;
    private String engineUseTypeDescription;
    private String federalEnforcableLimitText;
    private String permitLimitText;
    private LocalDate unitModificationDate;
    private String unitModificationDescription;
    private String reportingRequirement;

    public MeasureJsonDto getDesignCapacity() {
        return designCapacity;
    }

    public void setDesignCapacity(MeasureJsonDto designCapacity) {
        this.designCapacity = designCapacity;
    }

    public LookupJsonDto getUnitTypeCode() {
        return unitTypeCode;
    }

    public void setUnitTypeCode(LookupJsonDto unitTypeCode) {
        this.unitTypeCode = unitTypeCode;
    }

    public List<EmissionsProcessJsonDto> getEmissionsProcesses() {
        return emissionsProcesses;
    }

    public void setEmissionsProcesses(List<EmissionsProcessJsonDto> emissionsProcesses) {
        this.emissionsProcesses = emissionsProcesses;
    }

    public LocalDate getUnitOperationDate() {
        return unitOperationDate;
    }

    public void setUnitOperationDate(LocalDate unitOperationDate) {
        this.unitOperationDate = unitOperationDate;
    }

    public String getPermitStatusCode() {
        return permitStatusCode;
    }

    public void setPermitStatusCode(String permitStatusCode) {
        this.permitStatusCode = permitStatusCode;
    }

    public LocalDate getPermitStatusBeginYear() {
        return permitStatusBeginYear;
    }

    public void setPermitStatusBeginYear(LocalDate permitStatusBeginYear) {
        this.permitStatusBeginYear = permitStatusBeginYear;
    }

    public LocalDate getPermitStatusEndYear() {
        return permitStatusEndYear;
    }

    public void setPermitStatusEndYear(LocalDate permitStatusEndYear) {
        this.permitStatusEndYear = permitStatusEndYear;
    }

    public String getUnitIsReadOnly() {
        return unitIsReadOnly;
    }

    public void setUnitIsReadOnly(String unitIsReadOnly) {
        this.unitIsReadOnly = unitIsReadOnly;
    }

    public String getDesignCapacityIsReadOnly() {
        return designCapacityIsReadOnly;
    }

    public void setDesignCapacityIsReadOnly(String designCapacityIsReadOnly) {
        this.designCapacityIsReadOnly = designCapacityIsReadOnly;
    }

    public MeasureJsonDto getMaximumTheoreticalEmissions() {
        return maximumTheoreticalEmissions;
    }

    public void setMaximumTheoreticalEmissions(MeasureJsonDto maximumTheoreticalEmissions) {
        this.maximumTheoreticalEmissions = maximumTheoreticalEmissions;
    }

    public MeasureJsonDto getPotentialToEmit() {
        return potentialToEmit;
    }

    public void setPotentialToEmit(MeasureJsonDto potentialToEmit) {
        this.potentialToEmit = potentialToEmit;
    }

    public String getUnitNaics() {
        return unitNaics;
    }

    public void setUnitNaics(String unitNaics) {
        this.unitNaics = unitNaics;
    }

    public String getPermitByRule() {
        return permitByRule;
    }

    public void setPermitByRule(String permitByRule) {
        this.permitByRule = permitByRule;
    }

    public String getEngineUseType() {
        return engineUseType;
    }

    public void setEngineUseType(String engineUseType) {
        this.engineUseType = engineUseType;
    }

    public String getEngineUseTypeDescription() {
        return engineUseTypeDescription;
    }

    public void setEngineUseTypeDescription(String engineUseTypeDescription) {
        this.engineUseTypeDescription = engineUseTypeDescription;
    }

    public String getFederalEnforcableLimitText() {
        return federalEnforcableLimitText;
    }

    public void setFederalEnforcableLimitText(String federalEnforcableLimitText) {
        this.federalEnforcableLimitText = federalEnforcableLimitText;
    }

    public String getPermitLimitText() {
        return permitLimitText;
    }

    public void setPermitLimitText(String permitLimitText) {
        this.permitLimitText = permitLimitText;
    }

    public LocalDate getUnitModificationDate() {
        return unitModificationDate;
    }

    public void setUnitModificationDate(LocalDate unitModificationDate) {
        this.unitModificationDate = unitModificationDate;
    }

    public String getUnitModificationDescription() {
        return unitModificationDescription;
    }

    public void setUnitModificationDescription(String unitModificationDescription) {
        this.unitModificationDescription = unitModificationDescription;
    }

    public String getReportingRequirement() {
        return reportingRequirement;
    }

    public void setReportingRequirement(String reportingRequirement) {
        this.reportingRequirement = reportingRequirement;
    }
}
