/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
