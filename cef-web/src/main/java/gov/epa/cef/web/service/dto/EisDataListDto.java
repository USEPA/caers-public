/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class EisDataListDto implements Consumer<EisDataReportDto> {

    private final EisDataCriteria criteria;

    private final List<EisDataReportDto> reports;

    public EisDataListDto() {

        this(null);
    }

    public EisDataListDto(EisDataCriteria criteria) {

        this.criteria = criteria;
        this.reports = new ArrayList<>();
    }

    @Override
    public void accept(EisDataReportDto eisDataReportDto) {

        if (eisDataReportDto != null) {

            this.reports.add(eisDataReportDto);
        }
    }

    public EisDataCriteria getCriteria() {

        return criteria;
    }

    public Collection<EisDataReportDto> getReports() {

        return reports;
    }

    public void setReports(Collection<EisDataReportDto> reports) {

        this.reports.clear();
        if (reports != null) {

            this.reports.addAll(reports);
        }
    }

    public EisDataListDto withReports(Collection<EisDataReportDto> reports) {

        setReports(reports);
        return this;
    }
}

