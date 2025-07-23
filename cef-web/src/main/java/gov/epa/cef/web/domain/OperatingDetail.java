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
