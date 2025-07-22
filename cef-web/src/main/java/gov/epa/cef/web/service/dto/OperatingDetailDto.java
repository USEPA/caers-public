/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
