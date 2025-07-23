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

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Transient;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import gov.epa.cef.web.domain.common.BaseAuditEntity;
import gov.epa.cef.web.domain.common.IReportComponent;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.util.ReportCreationContext;

/**
 * Emission entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "emission")
public class Emission extends BaseAuditEntity implements IReportComponent {

    private static final long serialVersionUID = 1L;

    // Fields

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporting_period_id", nullable = false)
    private ReportingPeriod reportingPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pollutant_code", nullable = false)
    private Pollutant pollutant;

    @Column(name = "total_manual_entry", nullable = false)
    private Boolean totalManualEntry;

    @Column(name = "overall_control_percent")
    private BigDecimal overallControlPercent;

    @Column(name = "total_emissions")
    private BigDecimal totalEmissions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emissions_uom_code", nullable = false)
    private UnitMeasureCode emissionsUomCode;

    @Column(name = "formula_indicator")
    private Boolean formulaIndicator;

    @Column(name = "emissions_factor")
    private BigDecimal emissionsFactor;

    @Column(name = "emissions_factor_formula", length = 100)
    private String emissionsFactorFormula;

    @Column(name = "emissions_factor_text", length = 100)
    private String emissionsFactorText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emissions_calc_method_code", nullable = false)
    private CalculationMethodCode emissionsCalcMethodCode;

    @Column(name = "comments", length = 400)
    private String comments;

    @Column(name = "calculation_comment", length = 4000)
    private String calculationComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emissions_numerator_uom")
    private UnitMeasureCode emissionsNumeratorUom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emissions_denominator_uom")
    private UnitMeasureCode emissionsDenominatorUom;

    @Column(name = "calculated_emissions_tons")
    private BigDecimal calculatedEmissionsTons;

    @Column(name = "emissions_factor_source", length = 200)
    private String emissionsFactorSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ef_webfire_id", referencedColumnName ="webfire_id")
    private WebfireEmissionFactor webfireEf;

    @Column(name = "ef_ghg_id")
    private Long ghgEfId;

    @Column(name = "monthly_rate")
    private BigDecimal monthlyRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annual_emission_id")
    private Emission annualEmission;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "emission")
    private List<EmissionFormulaVariable> variables = new ArrayList<>();


    @Column(name = "not_reporting")
    private Boolean notReporting;

    @Transient
    private String emissionsFactorDescription;

    /***
     * Default constructor
     */
    public Emission() {}


    /***
     * Copy constructor
     * @param reportingPeriod The reporting period that the copied Emission object should be associated with
     * @param originalEmission The emission object that is being copied
     */
    public Emission(ReportingPeriod reportingPeriod, Emission originalEmission) {
        this(reportingPeriod, originalEmission, null);
    }

    /***
     * Copy constructor
     * @param reportingPeriod The reporting period that the copied Emission object should be associated with
     * @param originalEmission The emission object that is being copied
     * @param context context for tracking report creation changes
     */
    public Emission(ReportingPeriod reportingPeriod, Emission originalEmission, ReportCreationContext context) {
		this.id = originalEmission.getId();
        this.reportingPeriod = reportingPeriod;
        this.pollutant = originalEmission.getPollutant();
        this.totalManualEntry = originalEmission.getTotalManualEntry();
        this.overallControlPercent = originalEmission.getOverallControlPercent();
        this.totalEmissions = originalEmission.getTotalEmissions();
        this.emissionsUomCode = originalEmission.getEmissionsUomCode();
        this.emissionsFactor = originalEmission.getEmissionsFactor();
        this.emissionsFactorText = originalEmission.getEmissionsFactorText();
        this.emissionsCalcMethodCode = originalEmission.getEmissionsCalcMethodCode();
        this.comments = originalEmission.getComments();
        this.calculationComment = originalEmission.getCalculationComment();
        this.emissionsNumeratorUom = originalEmission.getEmissionsNumeratorUom();
        this.emissionsDenominatorUom = originalEmission.getEmissionsDenominatorUom();
        this.calculatedEmissionsTons = originalEmission.getCalculatedEmissionsTons();
        this.formulaIndicator = originalEmission.getFormulaIndicator();
        this.emissionsFactorFormula = originalEmission.getEmissionsFactorFormula();
        this.emissionsFactorSource = originalEmission.getEmissionsFactorSource();
        this.webfireEf = originalEmission.getWebfireEf();
        this.ghgEfId = originalEmission.getGhgEfId();
        this.notReporting = originalEmission.getNotReporting();

        for (EmissionFormulaVariable variable : originalEmission.getVariables()) {
            this.variables.add(new EmissionFormulaVariable(this, variable));
        }
    }

    public ReportingPeriod getReportingPeriod() {
        return this.reportingPeriod;
    }

    public void setReportingPeriod(ReportingPeriod reportingPeriod) {
        this.reportingPeriod = reportingPeriod;
    }

    public Pollutant getPollutant() {
        return pollutant;
    }

    public void setPollutant(Pollutant pollutant) {
        this.pollutant = pollutant;
    }

    public Boolean getTotalManualEntry() {
        return totalManualEntry;
    }

    public void setTotalManualEntry(Boolean totalManualEntry) {
        this.totalManualEntry = totalManualEntry;
    }

    public BigDecimal getOverallControlPercent() {
        return overallControlPercent;
    }

    public void setOverallControlPercent(BigDecimal overallControlPercent) {
        this.overallControlPercent = overallControlPercent;
    }

    public BigDecimal getTotalEmissions() {
        return this.totalEmissions;
    }

    public void setTotalEmissions(BigDecimal totalEmissions) {
        this.totalEmissions = totalEmissions;
    }

    public UnitMeasureCode getEmissionsUomCode() {
        return this.emissionsUomCode;
    }

    public void setEmissionsUomCode(UnitMeasureCode emissionsUomCode) {
        this.emissionsUomCode = emissionsUomCode;
    }

    public Boolean getFormulaIndicator() {
        return formulaIndicator;
    }

    public void setFormulaIndicator(Boolean formulaIndicator) {
        this.formulaIndicator = formulaIndicator;
    }

    public BigDecimal getEmissionsFactor() {
        return this.emissionsFactor;
    }

    public void setEmissionsFactor(BigDecimal emissionsFactor) {
        this.emissionsFactor = emissionsFactor;
    }

    public String getEmissionsFactorFormula() {
        return emissionsFactorFormula;
    }

    public void setEmissionsFactorFormula(String emissionsFactorFormula) {
        this.emissionsFactorFormula = emissionsFactorFormula;
    }

    public String getEmissionsFactorText() {
        return this.emissionsFactorText;
    }

    public void setEmissionsFactorText(String emissionsFactorText) {
        this.emissionsFactorText = emissionsFactorText;
    }

    public CalculationMethodCode getEmissionsCalcMethodCode() {
        return this.emissionsCalcMethodCode;
    }

    public void setEmissionsCalcMethodCode(CalculationMethodCode emissionsCalcMethodCode) {
        this.emissionsCalcMethodCode = emissionsCalcMethodCode;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCalculationComment() {
        return calculationComment;
    }

    public void setCalculationComment(String calculationComment) {
        this.calculationComment = calculationComment;
    }

    public BigDecimal getCalculatedEmissionsTons() {
        return calculatedEmissionsTons;
    }

    public void setCalculatedEmissionsTons(BigDecimal calculatedEmissionsTons) {
        this.calculatedEmissionsTons = calculatedEmissionsTons;
    }

    public UnitMeasureCode getEmissionsNumeratorUom() {
        return emissionsNumeratorUom;
    }

    public void setEmissionsNumeratorUom(UnitMeasureCode emissionsNumeratorUom) {
        this.emissionsNumeratorUom = emissionsNumeratorUom;
    }

    public UnitMeasureCode getEmissionsDenominatorUom() {
        return emissionsDenominatorUom;
    }

    public void setEmissionsDenominatorUom(UnitMeasureCode emissionsDenominatorUom) {
        this.emissionsDenominatorUom = emissionsDenominatorUom;
    }

    public WebfireEmissionFactor getWebfireEf() {
        return webfireEf;
    }

    public void setWebfireEf(WebfireEmissionFactor webfireEf) {
        this.webfireEf = webfireEf;
    }

    public Long getGhgEfId() { return ghgEfId; }

    public void setGhgEfId(Long ghgEfId) { this.ghgEfId = ghgEfId; }

    public BigDecimal getMonthlyRate() {
        return monthlyRate;
    }

    public void setMonthlyRate(BigDecimal monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public List<EmissionFormulaVariable> getVariables() {
        return variables;
    }

    public void setVariables(List<EmissionFormulaVariable> variables) {

        this.variables.clear();
        if (variables != null) {
            this.variables.addAll(variables);
        }
    }

	public String getEmissionsFactorSource() {
		return emissionsFactorSource;
	}

	public void setEmissionsFactorSource(String emissionsFactorSource) {
		this.emissionsFactorSource = emissionsFactorSource;
	}

    public Emission getAnnualEmission() {
        return annualEmission;
    }

    public void setAnnualEmission(Emission annualEmission) {
        this.annualEmission = annualEmission;
    }

    public Boolean getNotReporting() {
        return notReporting;
    }

    public void setNotReporting(Boolean notReporting) {
        this.notReporting = notReporting;
    }

    public String getEmissionsFactorDescription() {
        return emissionsFactorDescription;
    }

    public void setEmissionsFactorDescription(String emissionsFactorDescription) {
        this.emissionsFactorDescription = emissionsFactorDescription;
    }


    /***
     * Set the id property to null for this object and the id for it's direct children.  This method is useful to INSERT the updated object instead of UPDATE.
     */
    public void clearId() {
    	this.id = null;

    	for (EmissionFormulaVariable variable : this.variables) {
    		variable.clearId();
    	}
    }


    @Override
    public ValidationDetailDto getComponentDetails() {

        ValidationDetailDto parent =  getReportingPeriod() != null ? getReportingPeriod().getComponentDetails() : null;

        String identifier = getPollutant() != null ? getPollutant().getPollutantName() : null;

        String description = MessageFormat.format("{0}, Pollutant: {1}",
                parent != null ? parent.getDescription() : "",
                identifier);

        ValidationDetailDto dto = new ValidationDetailDto(getId(), identifier, EntityType.EMISSION, description).withParent(parent);

        return dto;
    }
}
