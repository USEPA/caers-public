package gov.epa.cef.web.service.dto.json;

import gov.epa.cef.web.domain.ReportStatus;

import java.util.Date;
import java.util.List;

public class ReportExportQueryDto {

    String programSystemCode;
    Integer reportYear;
    Date modifiedStartDate;
    Date modifiedEndDate;
    Date certifiedStartDate;
    Date certifiedEndDate;
    List<String> agencyFacilityId;
    List<String> reportId;
    List<ReportStatus> reportStatus;
    Integer page;
    Integer pageSize;

    public Integer getReportYear() {
        return reportYear;
    }

    public void setReportYear(Integer reportYear) {
        this.reportYear = reportYear;
    }

    public Date getModifiedStartDate() {
        return modifiedStartDate;
    }

    public void setModifiedStartDate(Date modifiedStartDate) {
        this.modifiedStartDate = modifiedStartDate;
    }

    public Date getModifiedEndDate() {
        return modifiedEndDate;
    }

    public void setModifiedEndDate(Date modifiedEndDate) {
        this.modifiedEndDate = modifiedEndDate;
    }

    public Date getCertifiedStartDate() {
        return certifiedStartDate;
    }

    public void setCertifiedStartDate(Date certifiedStartDate) {
        this.certifiedStartDate = certifiedStartDate;
    }

    public Date getCertifiedEndDate() {
        return certifiedEndDate;
    }

    public void setCertifiedEndDate(Date certifiedEndDate) {
        this.certifiedEndDate = certifiedEndDate;
    }

    public List<String> getAgencyFacilityId() {
        return agencyFacilityId;
    }

    public void setAgencyFacilityId(List<String> agencyFacilityId) {
        this.agencyFacilityId = agencyFacilityId;
    }

    public List<String> getReportId() {
        return reportId;
    }

    public void setReportId(List<String> reportId) {
        this.reportId = reportId;
    }

    public List<ReportStatus> getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(List<ReportStatus> reportStatus) {
        this.reportStatus = reportStatus;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getProgramSystemCode() {
        return programSystemCode;
    }

    public void setProgramSystemCode(String programSystemCode) {
        this.programSystemCode = programSystemCode;
    }

    public ReportExportQueryDto(String programSystemCode, Integer reportYear, Date modifiedStartDate, Date modifiedEndDate, Date certifiedStartDate, Date certifiedEndDate, List<String> agencyFacilityId, List<String> reportId, List<ReportStatus> reportStatus, Integer page, Integer pageSize) {
        this.programSystemCode = programSystemCode;
        this.reportYear = reportYear;
        this.modifiedStartDate = modifiedStartDate;
        this.modifiedEndDate = modifiedEndDate;
        this.certifiedStartDate = certifiedStartDate;
        this.certifiedEndDate = certifiedEndDate;
        this.agencyFacilityId = agencyFacilityId;
        this.reportId = reportId;
        this.reportStatus = reportStatus;
        this.page = page;
        this.pageSize = pageSize;
    }
}
