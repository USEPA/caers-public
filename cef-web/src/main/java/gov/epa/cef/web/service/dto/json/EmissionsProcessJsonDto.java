package gov.epa.cef.web.service.dto.json;

import gov.epa.cef.web.service.dto.bulkUpload.ReleasePointApptBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ReportingPeriodBulkUploadDto;
import gov.epa.cef.web.service.dto.json.shared.MeasureJsonDto;
import io.swagger.v3.oas.annotations.media.Schema;
import net.exchangenetwork.schema.cer._2._0.ControlApproachDataType;
import net.exchangenetwork.schema.cer._2._0.DeleteReportingPeriodDataType;
import net.exchangenetwork.schema.cer._2._0.IdentificationDataType;
import net.exchangenetwork.schema.cer._2._0.RegulationDataType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "EmissionsProcess")
public class EmissionsProcessJsonDto extends BaseFacilityComponentJsonDto {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 20)
    private String sourceClassificationCode;

    @Size(max = 100)
    private String sccShortName;

    @Size(max = 500)
    private String sccDescription;

    private AircraftEngineJsonDto aircraftEngineTypeCode;

    private List<ReportingPeriodJsonDto> reportingPeriods = new ArrayList<>();

    private List<ReleasePointApptJsonDto> releasePointApportionment = new ArrayList<>();


    protected String emissionsTypeCode;
    protected String processIsReadOnly;
    protected List<RegulationDataType> processRegulation;
    protected ControlApproachDataType processControlApproach;
    protected List<DeleteReportingPeriodDataType> deleteReportingPeriod;


    protected LocalDate permitStatusBeginYear;
    protected LocalDate permitStatusEndYear;
    private MeasureJsonDto maximumTheoreticalEmissions;
    private MeasureJsonDto potentialToEmit;
    private String isBillable;
    private String insignificantSource;

    public String getSourceClassificationCode() {
        return sourceClassificationCode;
    }

    public void setSourceClassificationCode(String sourceClassificationCode) {
        this.sourceClassificationCode = sourceClassificationCode;
    }

    public String getSccShortName() {
        return sccShortName;
    }

    public void setSccShortName(String sccShortName) {
        this.sccShortName = sccShortName;
    }

    public String getSccDescription() {
        return sccDescription;
    }

    public void setSccDescription(String sccDescription) {
        this.sccDescription = sccDescription;
    }

    public AircraftEngineJsonDto getAircraftEngineTypeCode() {
        return aircraftEngineTypeCode;
    }

    public void setAircraftEngineTypeCode(AircraftEngineJsonDto aircraftEngineTypeCode) {
        this.aircraftEngineTypeCode = aircraftEngineTypeCode;
    }

    public List<ReportingPeriodJsonDto> getReportingPeriods() {
        return reportingPeriods;
    }

    public void setReportingPeriods(List<ReportingPeriodJsonDto> reportingPeriods) {
        this.reportingPeriods = reportingPeriods;
    }

    public List<ReleasePointApptJsonDto> getReleasePointApportionment() {
        return releasePointApportionment;
    }

    public void setReleasePointApportionment(List<ReleasePointApptJsonDto> releasePointApportionment) {
        this.releasePointApportionment = releasePointApportionment;
    }

    public String getEmissionsTypeCode() {
        return emissionsTypeCode;
    }

    public void setEmissionsTypeCode(String emissionsTypeCode) {
        this.emissionsTypeCode = emissionsTypeCode;
    }

    public String getProcessIsReadOnly() {
        return processIsReadOnly;
    }

    public void setProcessIsReadOnly(String processIsReadOnly) {
        this.processIsReadOnly = processIsReadOnly;
    }

    public List<RegulationDataType> getProcessRegulation() {
        return processRegulation;
    }

    public void setProcessRegulation(List<RegulationDataType> processRegulation) {
        this.processRegulation = processRegulation;
    }

    public ControlApproachDataType getProcessControlApproach() {
        return processControlApproach;
    }

    public void setProcessControlApproach(ControlApproachDataType processControlApproach) {
        this.processControlApproach = processControlApproach;
    }

    public List<DeleteReportingPeriodDataType> getDeleteReportingPeriod() {
        return deleteReportingPeriod;
    }

    public void setDeleteReportingPeriod(List<DeleteReportingPeriodDataType> deleteReportingPeriod) {
        this.deleteReportingPeriod = deleteReportingPeriod;
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

    public String getIsBillable() {
        return isBillable;
    }

    public void setIsBillable(String isBillable) {
        this.isBillable = isBillable;
    }

    public String getInsignificantSource() {
        return insignificantSource;
    }

    public void setInsignificantSource(String insignificantSource) {
        this.insignificantSource = insignificantSource;
    }
}
