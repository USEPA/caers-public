/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import gov.epa.cef.web.domain.common.BaseAuditEntity;
import gov.epa.cef.web.domain.common.IReportComponent;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;

/**
 * OperatingDetail entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "operating_detail")

public class OperatingDetail extends BaseAuditEntity implements IReportComponent {

    private static final long serialVersionUID = 1L;

    // Fields

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporting_period_id", nullable = false)
    private ReportingPeriod reportingPeriod;

    @Column(name = "actual_hours_per_period", precision = 9, scale = 5)
    private BigDecimal actualHoursPerPeriod;

    @Column(name = "avg_hours_per_day", precision = 7, scale = 5)
    private BigDecimal avgHoursPerDay;

    @Column(name = "avg_days_per_week", precision = 6, scale = 5)
    private BigDecimal avgDaysPerWeek;

    @Column(name = "avg_weeks_per_period", precision = 7, scale = 5)
    private BigDecimal avgWeeksPerPeriod;

    @Column(name = "percent_winter", precision = 4, scale = 1)
    private BigDecimal percentWinter;

    @Column(name = "percent_spring", precision = 4, scale = 1)
    private BigDecimal percentSpring;

    @Column(name = "percent_summer", precision = 4, scale = 1)
    private BigDecimal percentSummer;

    @Column(name = "percent_fall", precision = 4, scale = 1)
    private BigDecimal percentFall;


    /***
     * Default constructor
     */
    public OperatingDetail() {}


    /***
     * Copy constructor
     * @param reportingPeriod The reporting period that this new operating detail should be associated with
     * @param originalOperatingDetail The operating detail object that is being copied
     */
    public OperatingDetail(ReportingPeriod reportingPeriod, OperatingDetail originalOperatingDetail) {
    	this.id = originalOperatingDetail.getId();
        this.reportingPeriod = reportingPeriod;
        this.actualHoursPerPeriod = originalOperatingDetail.getActualHoursPerPeriod();
        this.avgHoursPerDay = originalOperatingDetail.getAvgHoursPerDay();
        this.avgDaysPerWeek = originalOperatingDetail.getAvgDaysPerWeek();
        this.avgWeeksPerPeriod = originalOperatingDetail.getAvgWeeksPerPeriod();
        this.percentWinter = originalOperatingDetail.getPercentWinter();
        this.percentSpring = originalOperatingDetail.getPercentSpring();
        this.percentSummer = originalOperatingDetail.getPercentSummer();
        this.percentFall = originalOperatingDetail.getPercentFall();
    }

    public ReportingPeriod getReportingPeriod() {
        return this.reportingPeriod;
    }

    public void setReportingPeriod(ReportingPeriod reportingPeriod) {
        this.reportingPeriod = reportingPeriod;
    }

    public BigDecimal getActualHoursPerPeriod() {
        return this.actualHoursPerPeriod;
    }

    public void setActualHoursPerPeriod(BigDecimal actualHoursPerPeriod) {
        this.actualHoursPerPeriod = actualHoursPerPeriod;
    }

    public BigDecimal getAvgHoursPerDay() {
        return this.avgHoursPerDay;
    }

    public void setAvgHoursPerDay(BigDecimal avgHoursPerDay) {
        this.avgHoursPerDay = avgHoursPerDay;
    }

    public BigDecimal getAvgDaysPerWeek() {
        return this.avgDaysPerWeek;
    }

    public void setAvgDaysPerWeek(BigDecimal avgDaysPerWeek) {
        this.avgDaysPerWeek = avgDaysPerWeek;
    }

    public BigDecimal getAvgWeeksPerPeriod() {
        return this.avgWeeksPerPeriod;
    }

    public void setAvgWeeksPerPeriod(BigDecimal avgWeeksPerPeriod) {
        this.avgWeeksPerPeriod = avgWeeksPerPeriod;
    }

    public BigDecimal getPercentWinter() {
        return this.percentWinter;
    }

    public void setPercentWinter(BigDecimal percentWinter) {
        this.percentWinter = percentWinter;
    }

    public BigDecimal getPercentSpring() {
        return this.percentSpring;
    }

    public void setPercentSpring(BigDecimal percentSpring) {
        this.percentSpring = percentSpring;
    }

    public BigDecimal getPercentSummer() {
        return this.percentSummer;
    }

    public void setPercentSummer(BigDecimal percentSummer) {
        this.percentSummer = percentSummer;
    }

    public BigDecimal getPercentFall() {
        return this.percentFall;
    }

    public void setPercentFall(BigDecimal percentFall) {
        this.percentFall = percentFall;
    }


    /***
     * Set the id property to null for this object and the id for it's direct children.  This method is useful to INSERT the updated object instead of UPDATE.
     */
    public void clearId() {
    	this.id = null;
    }


    @Override
    public ValidationDetailDto getComponentDetails() {

        ValidationDetailDto parent = getReportingPeriod() != null ? getReportingPeriod().getComponentDetails() : null;

        String description = parent != null ? parent.getDescription() : "";

        ValidationDetailDto dto = new ValidationDetailDto(getId(), null, EntityType.OPERATING_DETAIL, description).withParent(parent);

        return dto;
    }

}
