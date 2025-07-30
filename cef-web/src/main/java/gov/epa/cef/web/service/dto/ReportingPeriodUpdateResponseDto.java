/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
