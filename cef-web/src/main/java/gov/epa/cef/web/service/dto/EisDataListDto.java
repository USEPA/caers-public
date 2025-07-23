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

