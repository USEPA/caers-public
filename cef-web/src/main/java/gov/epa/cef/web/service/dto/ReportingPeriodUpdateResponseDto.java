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
import java.util.ArrayList;
import java.util.List;

public class ReportingPeriodUpdateResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private ReportingPeriodDto reportingPeriod;
    private List<String> updatedEmissions = new ArrayList<>();
    private List<String> notUpdatedEmissions = new ArrayList<>();
    private List<String> failedEmissions = new ArrayList<>();

    public ReportingPeriodDto getReportingPeriod() {
        return reportingPeriod;
    }

    public void setReportingPeriod(ReportingPeriodDto reportingPeriod) {
        this.reportingPeriod = reportingPeriod;
    }

    public List<String> getUpdatedEmissions() {
        return updatedEmissions;
    }

    public void setUpdatedEmissions(List<String> updatedEmissions) {
        this.updatedEmissions = updatedEmissions;
    }

    public List<String> getNotUpdatedEmissions() {
        return notUpdatedEmissions;
    }

    public void setNotUpdatedEmissions(List<String> notUpdatedEmissions) {
        this.notUpdatedEmissions = notUpdatedEmissions;
    }

    public List<String> getFailedEmissions() {
        return failedEmissions;
    }

    public void setFailedEmissions(List<String> failedEmissions) {
        this.failedEmissions = failedEmissions;
    }
}
