/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.domain;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import gov.epa.cef.web.domain.common.BaseEmissionFactor;

import java.math.BigDecimal;
import java.util.Date;

@MappedSuperclass
public abstract class EmissionFactor extends BaseEmissionFactor {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "control_measure_code_2")
    private ControlMeasureCode controlMeasureCode2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "control_measure_code_3")
    private ControlMeasureCode controlMeasureCode3;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "control_measure_code_4")
    private ControlMeasureCode controlMeasureCode4;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "control_measure_code_5")
    private ControlMeasureCode controlMeasureCode5;

    @Column(name = "revoked_date")
    private Date revokedDate;

    @Column(name = "quality", length = 10)
    private String quality;

    @Column(name = "min_value")
    private BigDecimal minValue;

    @Column(name = "max_value")
    private BigDecimal maxValue;

    @Column(name = "applicability", length = 400)
    private String applicability;

    @Column(name = "derivation", length = 400)
    private String derivation;

    @Column(name = "date_updated")
    private Date dateUpdated;

    @Column(name = "condition", length = 20)
    private String condition;

    public ControlMeasureCode getControlMeasureCode2() {
        return controlMeasureCode2;
    }

    public void setControlMeasureCode2(ControlMeasureCode controlMeasureCode2) {
        this.controlMeasureCode2 = controlMeasureCode2;
    }

    public ControlMeasureCode getControlMeasureCode3() {
        return controlMeasureCode3;
    }

    public void setControlMeasureCode3(ControlMeasureCode controlMeasureCode3) {
        this.controlMeasureCode3 = controlMeasureCode3;
    }

    public ControlMeasureCode getControlMeasureCode4() {
        return controlMeasureCode4;
    }

    public void setControlMeasureCode4(ControlMeasureCode controlMeasureCode4) {
        this.controlMeasureCode4 = controlMeasureCode4;
    }

    public ControlMeasureCode getControlMeasureCode5() {
        return controlMeasureCode5;
    }

    public void setControlMeasureCode5(ControlMeasureCode controlMeasureCode5) {
        this.controlMeasureCode5 = controlMeasureCode5;
    }

    public Date getRevokedDate() {
        return revokedDate;
    }

    public void setRevokedDate(Date revokedDate) {
        this.revokedDate = revokedDate;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public void setMinValue(BigDecimal minValue) {
        this.minValue = minValue;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(BigDecimal maxValue) {
        this.maxValue = maxValue;
    }

    public String getApplicability() {
        return applicability;
    }

    public void setApplicability(String applicability) {
        this.applicability = applicability;
    }

    public String getDerivation() {
        return derivation;
    }

    public void setDerivation(String derivation) {
        this.derivation = derivation;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
