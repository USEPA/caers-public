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
package gov.epa.cef.web.domain;

import gov.epa.cef.web.domain.common.BaseAuditEntity;
import gov.epa.cef.web.service.validation.ValidationResult;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "quality_check_results", uniqueConstraints = {
    @UniqueConstraint(name = "UniqueReportIdAndIsAnnual", columnNames = {"semiannual", "report_id"})
})
public class QualityCheckResults extends BaseAuditEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "semiannual", nullable = false)
    private boolean semiannual;

    @Column(name = "federal_errors_count", nullable = false)
    private int federalErrorsCount;

    @Column(name = "federal_warnings_count", nullable = false)
    private int federalWarningsCount;

    @Column(name = "state_errors_count", nullable = false)
    private int stateErrorsCount;

    @Column(name = "state_warnings_count", nullable = false)
    private int stateWarningsCount;

    @Column(name = "facility_inventory_changes_count", nullable = false)
    private int facilityInventoryChangesCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private EmissionsReport emissionsReport;

    @Enumerated(EnumType.STRING)
    @Column(name = "qa_results_status", nullable = false)
    private QAResultsStatus qaResultsStatus;

    @Type(type = "json")
    @Column(name = "current_results", columnDefinition = "jsonb")
    private ValidationResult currentResults;

    public boolean isSemiannual() {
        return semiannual;
    }

    public void setIsSemiannual(boolean semiannual) {
        this.semiannual = semiannual;
    }

    public int getFederalErrorsCount() {
        return federalErrorsCount;
    }

    public void setFederalErrorsCount(int federalErrorsCount) {
        this.federalErrorsCount = federalErrorsCount;
    }

    public int getFederalWarningsCount() {
        return federalWarningsCount;
    }

    public void setFederalWarningsCount(int federalWarningsCount) {
        this.federalWarningsCount = federalWarningsCount;
    }

    public int getStateErrorsCount() {
        return stateErrorsCount;
    }

    public void setStateErrorsCount(int stateErrorsCount) {
        this.stateErrorsCount = stateErrorsCount;
    }

    public int getStateWarningsCount() {
        return stateWarningsCount;
    }

    public void setStateWarningsCount(int stateWarningsCount) {
        this.stateWarningsCount = stateWarningsCount;
    }

    public int getFacilityInventoryChangesCount() {
        return facilityInventoryChangesCount;
    }

    public void setFacilityInventoryChangesCount(int facilityInventoryChangesCount) {
        this.facilityInventoryChangesCount = facilityInventoryChangesCount;
    }

    public EmissionsReport getEmissionsReport() {
        return emissionsReport;
    }

    public void setEmissionsReport(EmissionsReport emissionsReport) {
        this.emissionsReport = emissionsReport;
    }

    public QAResultsStatus getQaResultsStatus() {
        return qaResultsStatus;
    }

    public void setQaResultsStatus(QAResultsStatus qaResultsStatus) {
        this.qaResultsStatus = qaResultsStatus;
    }

    public ValidationResult getCurrentResults() {
        return currentResults;
    }

    public void setCurrentResults(ValidationResult currentResults) {
        this.currentResults = currentResults;
    }

    public void clearResults() {
        this.federalErrorsCount = 0;
        this.federalWarningsCount = 0;
        this.stateErrorsCount = 0;
        this.stateWarningsCount = 0;
        this.facilityInventoryChangesCount = 0;
        this.currentResults = null;
    }

}
