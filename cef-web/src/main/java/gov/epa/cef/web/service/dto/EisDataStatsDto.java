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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class EisDataStatsDto {

    private final Set<Integer> availableYears = new LinkedHashSet<>();

    private final List<EisDataStatusStat> statuses = new ArrayList<>();

    public Collection<Integer> getAvailableYears() {

        return availableYears;
    }

    public void setAvailableYears(Collection<Integer> availableYears) {

        this.availableYears.clear();
        if (availableYears != null) {

            this.availableYears.addAll(availableYears);
        }
    }

    public Collection<EisDataStatusStat> getStatuses() {

        return statuses;
    }

    public void setStatuses(Collection<EisDataStatusStat> statuses) {

        this.statuses.clear();
        if (statuses != null) {
            this.statuses.addAll(statuses);
        }
    }

    public EisDataStatsDto withAvailableYears(final Collection<Integer> availableYears) {

        setAvailableYears(availableYears);
        return this;
    }

    public EisDataStatsDto withStatuses(final Collection<EisDataStatusStat> statuses) {

        setStatuses(statuses);
        return this;
    }

    public interface EisDataStatusStat {

        int getCount();

        EisSubmissionStatus getStatus();
    }
}
