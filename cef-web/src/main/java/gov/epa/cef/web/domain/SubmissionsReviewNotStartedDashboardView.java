/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FieldResult;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import gov.epa.cef.web.service.dto.EisSubmissionStatus;
import org.springframework.data.annotation.Immutable;

import gov.epa.cef.web.domain.common.BaseEntity;

@Entity
@Immutable
@NamedNativeQuery (
		name="notStartedReportsResults",
		query="SELECT row_number() OVER (ORDER BY mfr.id) AS id, "
		+ "sr.emissions_report_id AS emissionsReportId, "
		+ "mfr.id AS masterFacilityId, "
		+ "mfr.eis_program_id AS eisProgramId, "
		+ "mfr.name AS facilityName, "
		+ "sr.facility_site_id AS facilitySiteId, "
		+ "mfr.agency_facility_id AS agencyFacilityIdentifier, "
		+ "os.description AS operatingStatus, "
		+ "CASE WHEN sr.report_status IS NULL THEN 'NOT_STARTED' ELSE sr.report_status END AS reportStatus, "
		+ "(SELECT nci.industry FROM master_facility_naics_xref mfn  "
		+ "LEFT JOIN naics_code_industry nci ON CONCAT(nci.code_prefix,'') = SUBSTRING(CONCAT(mfn.naics_code,''), 1, 2) "
		+ "WHERE mfr.id = mfn.master_facility_id AND (mfn.naics_code_type = 'PRIMARY' OR mfn.naics_code_type IS NULL)) AS industry, "
		+ ":year AS year, "
		+ "mfr.program_system_code AS programSystemCode, "
		+ "sr.mid_year_sub_status AS midYearSubmissionStatus, "
		+ "sr.threshold_status AS thresholdStatus, "
		+ "(SELECT MAX(e.year) AS MAX FROM emissions_report e WHERE e.master_facility_id = mfr.id AND e.year < :year) AS lastSubmittalYear, "
        + "'NotStarted' AS eisLastSubStatus "
		+ "FROM master_facility_record mfr "
		+ "LEFT JOIN vw_submissions_review_dashboard sr ON sr.master_facility_id = mfr.id AND sr.year = :year "
		+ "JOIN operating_status_code os ON os.code = mfr.status_code "
		+ "WHERE mfr.program_system_code = :programSystemCode AND sr.emissions_report_id IS NULL AND os.code <> 'PS' AND os.code <> 'ONRE'"
		+ "AND EXTRACT(YEAR FROM mfr.created_date) <= (:year + 1)",
		resultSetMapping = "reportsResults")
@NamedNativeQuery (
		name="allReportsWithNotStartedResults",
		query="SELECT row_number() OVER (ORDER BY mfr.id) AS id, "
		+ "er.id AS emissionsReportId, "
		+ "mfr.id AS masterFacilityId, "
		+ "mfr.eis_program_id AS eisProgramId, "
		+ "mfr.name AS facilityName, "
		+ "fs.id AS facilitySiteId, "
		+ "mfr.agency_facility_id AS agencyFacilityIdentifier, "
		+ "os.description AS operatingStatus, "
		+ "CASE WHEN er.status IS NULL THEN 'NOT_STARTED' ELSE er.status END AS reportStatus, "
		+ "CASE WHEN er.id IS NULL THEN "
			+ "(SELECT nci.industry FROM master_facility_naics_xref mfn "
			+ "LEFT JOIN naics_code_industry nci ON CONCAT(nci.code_prefix,'') = SUBSTRING(CONCAT(mfn.naics_code,''), 1, 2) "
			+ "WHERE mfr.id = mfn.master_facility_id AND (mfn.naics_code_type = 'PRIMARY' OR mfn.naics_code_type IS NULL)) "
			+ "ELSE (SELECT nci.industry FROM facility_naics_xref fxn "
			+ "LEFT JOIN naics_code_industry nci ON CONCAT(nci.code_prefix,'') = SUBSTRING(CONCAT(fxn.naics_code,''), 1, 2) "
			+ "WHERE fs.id = fxn. facility_site_id AND fxn.naics_code_type = 'PRIMARY') END AS industry, "
		+ "CASE WHEN (er.id IS NULL) THEN :year ELSE er.year END AS year, "
		+ "mfr.program_system_code AS programSystemCode, "
		+ "er.mid_year_sub_status AS midYearSubmissionStatus, "
        + "CASE WHEN (mfr.program_system_code != 'GADNR') THEN 'Not Applicable' ELSE er.threshold_status END AS thresholdStatus, "
		+ "(SELECT MAX(e.year) AS MAX FROM emissions_report e WHERE e.master_facility_id = mfr.id AND e.year < :year) AS lastSubmittalYear, "
        + "CASE WHEN er.eis_last_sub_status IS NULL THEN 'NotStarted' ELSE er.eis_last_sub_status END AS eisLastSubStatus "
		+ "FROM master_facility_record mfr "
		+ "LEFT JOIN emissions_report er ON er.master_facility_id = mfr.id AND er.year = :year "
		+ "LEFT JOIN facility_site fs ON fs.report_id = er.id "
		+ "JOIN operating_status_code os ON CASE WHEN er.id IS NULL THEN os.code = mfr.status_code  ELSE os.code = fs.status_code END "
		+ "WHERE mfr.program_system_code = :programSystemCode "
		+ "AND ((er.id IS NULL AND mfr.status_code <> 'PS' AND mfr.status_code <> 'ONRE') OR (er.id IS NOT NULL)) "
        + "AND (er.id IS NULL OR er.is_deleted = false) "
		+ "AND EXTRACT(YEAR FROM mfr.created_date) <= (:year + 1)",
		resultSetMapping = "reportsResults")
@SqlResultSetMapping(name = "reportsResults", entities = {
		@EntityResult(entityClass = SubmissionsReviewNotStartedDashboardView.class, fields = {
				@FieldResult(name = "id", column = "id"),
				@FieldResult(name = "emissionsReportId", column = "emissionsReportId"),
				@FieldResult(name = "masterFacilityId", column = "masterFacilityId"),
				@FieldResult(name = "eisProgramId", column = "eisProgramId"),
				@FieldResult(name = "facilityName", column = "facilityName"),
				@FieldResult(name = "facilitySiteId", column = "facilitySiteId"),
				@FieldResult(name = "agencyFacilityIdentifier", column = "agencyFacilityIdentifier"),
				@FieldResult(name = "operatingStatus", column = "operatingStatus"),
				@FieldResult(name = "reportStatus", column = "reportStatus"),
				@FieldResult(name = "industry", column = "industry"), @FieldResult(name = "year", column = "year"),
				@FieldResult(name = "programSystemCode", column = "programSystemCode"),
				@FieldResult(name = "midYearSubmissionStatus", column = "midYearSubmissionStatus"),
				@FieldResult(name = "thresholdStatus", column = "thresholdStatus"),
				@FieldResult(name = "lastSubmittalYear", column = "lastSubmittalYear"),
                @FieldResult(name = "eisLastSubStatus", column = "eisLastSubStatus")})})

@Table(name = "vw_submissions_review_not_started_dashboard")
public class SubmissionsReviewNotStartedDashboardView  extends BaseEntity {

    /**
     * default version
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "emissions_report_id", unique = true)
    private Long emissionsReportId;

    @Column(name = "master_facility_id", nullable = false)
    Long masterFacilityId;

    @Column(name = "eis_program_id", nullable = false, length = 22)
    private String eisProgramId;

    @Column(name = "facility_name", nullable = false, length = 80)
    private String facilityName;

    @Column(name = "facility_site_id")
    private Long facilitySiteId;

    @Column(name = "agency_facility_identifier", length = 30)
    private String agencyFacilityIdentifier;

    @Column(name = "operating_status", length = 200)
    private String operatingStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_status")
    private ReportStatus reportStatus;

    @Column(name = "industry", length = 200)
    private String industry;

    @Column(name = "last_submittal_year")
    private Short lastSubmittalYear;

    @Column(name = "year", nullable = false)
    private Short year;

    @Column(name = "program_system_code", nullable = false, length = 20)
    private String programSystemCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "mid_year_sub_status")
    private ReportStatus midYearSubmissionStatus;

    @Column(name = "threshold_status")
    private String thresholdStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "eis_last_sub_status")
    private EisSubmissionStatus eisLastSubStatus;

    public Long getEmissionsReportId() {
        return emissionsReportId;
    }
    public void setEmissionsReportId(Long emissionsReportId) {
        this.emissionsReportId = emissionsReportId;
    }
    public Long getMasterFacilityId() {
		return masterFacilityId;
	}
	public void setMasterFacilityId(Long masterFacilityId) {
		this.masterFacilityId = masterFacilityId;
	}
	public String getEisProgramId() {
        return eisProgramId;
    }
    public void setEisProgramId(String eisProgramId) {
        this.eisProgramId = eisProgramId;
    }
    public String getFacilityName() {
        return facilityName;
    }
    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }
    public Long getFacilitySiteId() {
        return facilitySiteId;
    }
    public void setFacilitySiteId(Long facilitySiteId) {
        this.facilitySiteId = facilitySiteId;
    }
    public String getAgencyFacilityIdentifier() {
        return agencyFacilityIdentifier;
    }
    public void setAgencyFacilityIdentifier(String agencyFacilityIdentifier) {
        this.agencyFacilityIdentifier = agencyFacilityIdentifier;
    }
    public String getOperatingStatus() {
        return operatingStatus;
    }
    public void setOperatingStatus(String operatingStatus) {
        this.operatingStatus = operatingStatus;
    }
    public ReportStatus getReportStatus() {
        return reportStatus;
    }
    public void setReportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }
    public String getIndustry() {
        return industry;
    }
    public void setIndustry(String industry) {
        this.industry = industry;
    }
    public Short getLastSubmittalYear() {
        return lastSubmittalYear;
    }
    public void setLastSubmittalYear(Short lastSubmittalYear) {
        this.lastSubmittalYear = lastSubmittalYear;
    }
    public Short getYear() {
        return year;
    }
    public void setYear(Short year) {
        this.year = year;
    }
    public String getProgramSystemCode() {
        return programSystemCode;
    }
    public void setProgramSystemCode(String programSystemCode) {
        this.programSystemCode = programSystemCode;
    }
	public ReportStatus getMidYearSubmissionStatus() {
		return midYearSubmissionStatus;
	}
	public void setMidYearSubmissionStatus(ReportStatus midYearSubmissionStatus) {
		this.midYearSubmissionStatus = midYearSubmissionStatus;
	}
	public String getThresholdStatus() {
		return thresholdStatus;
	}
	public void setThresholdStatus(String thresholdStatus) {
		this.thresholdStatus = thresholdStatus;
	}
    public EisSubmissionStatus getEisLastSubStatus() {
        return eisLastSubStatus;
    }
    public void setEisLastSubStatus(EisSubmissionStatus eisLastSubStatus) {
        this.eisLastSubStatus = eisLastSubStatus;
    }
}
