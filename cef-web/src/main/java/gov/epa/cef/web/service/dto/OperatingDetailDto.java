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
package gov.epa.cef.web.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class OperatingDetailDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long reportingPeriodId;
    private BigDecimal actualHoursPerPeriod;
    private BigDecimal avgHoursPerDay;
    private BigDecimal avgDaysPerWeek;
    private BigDecimal avgWeeksPerPeriod;
    private BigDecimal percentWinter;
    private BigDecimal percentSpring;
    private BigDecimal percentSummer;
    private BigDecimal percentFall;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportingPeriodId() {
        return reportingPeriodId;
    }

    public void setReportingPeriodId(Long reportingPeriodId) {
        this.reportingPeriodId = reportingPeriodId;
    }

    public BigDecimal getActualHoursPerPeriod() {
        return actualHoursPerPeriod;
    }

    public void setActualHoursPerPeriod(BigDecimal actualHoursPerPeriod) {
        this.actualHoursPerPeriod = actualHoursPerPeriod;
    }

    public BigDecimal getAvgHoursPerDay() {
        return avgHoursPerDay;
    }

    public void setAvgHoursPerDay(BigDecimal avgHoursPerDay) {
        this.avgHoursPerDay = avgHoursPerDay;
    }

    public BigDecimal getAvgDaysPerWeek() {
        return avgDaysPerWeek;
    }

    public void setAvgDaysPerWeek(BigDecimal avgDaysPerWeek) {
        this.avgDaysPerWeek = avgDaysPerWeek;
    }

    public BigDecimal getAvgWeeksPerPeriod() {
        return avgWeeksPerPeriod;
    }

    public void setAvgWeeksPerPeriod(BigDecimal avgWeeksPerPeriod) {
        this.avgWeeksPerPeriod = avgWeeksPerPeriod;
    }

    public BigDecimal getPercentWinter() {
        return percentWinter;
    }

    public void setPercentWinter(BigDecimal percentWinter) {
        this.percentWinter = percentWinter;
    }

    public BigDecimal getPercentSpring() {
        return percentSpring;
    }

    public void setPercentSpring(BigDecimal percentSpring) {
        this.percentSpring = percentSpring;
    }

    public BigDecimal getPercentSummer() {
        return percentSummer;
    }

    public void setPercentSummer(BigDecimal percentSummer) {
        this.percentSummer = percentSummer;
    }

    public BigDecimal getPercentFall() {
        return percentFall;
    }

    public void setPercentFall(BigDecimal percentFall) {
        this.percentFall = percentFall;
    }

    public OperatingDetailDto withId(Long id) {

        setId(id);
        return this;
    }
}
