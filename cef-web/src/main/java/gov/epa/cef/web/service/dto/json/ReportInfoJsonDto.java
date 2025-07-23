package gov.epa.cef.web.service.dto.json;

import gov.epa.cef.web.domain.ReportStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Date;

@Schema(name = "ReportInfo")
public class ReportInfoJsonDto implements Serializable {

    private static final long serialVersionUID = 1L;


    @Schema(description = "The internal CAERS Report ID.")
    private Long reportId;

    @Schema(description = "The Agency Facility Identifier.")
    private String agencyFacilityIdentifier;

    @Schema(description = "The Report's status in CAERS.")
    private ReportStatus status;

    @Schema(description = "The date of the last change made to the Report.")
    private Date modifiedDate;

    @Schema(description = "The date this Report was most recently certified.")
    private Date certifiedDate;

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public String getAgencyFacilityIdentifier() {
        return agencyFacilityIdentifier;
    }

    public void setAgencyFacilityIdentifier(String agencyFacilityIdentifier) {
        this.agencyFacilityIdentifier = agencyFacilityIdentifier;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Date getCertifiedDate() {
        return certifiedDate;
    }

    public void setCertifiedDate(Date certifiedDate) {
        this.certifiedDate = certifiedDate;
    }
}
