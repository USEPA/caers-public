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
package gov.epa.cef.web.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import gov.epa.cef.web.domain.ReportingPeriodCode;

import javax.persistence.QueryHint;
import java.util.List;

public interface ReportingPeriodCodeRepository extends CrudRepository<ReportingPeriodCode, String> {

    @QueryHints({
        @QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Iterable<ReportingPeriodCode> findAll(Sort sort);

    /**
     * Retrieve reporting period code based on shortName
     */
    @Query("select rpc from ReportingPeriodCode rpc where rpc.shortName = :shortName")
    ReportingPeriodCode findRPCByShortName(String shortName);

    /**
     * Retrieve reporting period code for every month and semiannual
     */
    @Query("select rpc from ReportingPeriodCode rpc where rpc.reportingPeriodCodeType in (gov.epa.cef.web.domain.ReportingPeriodCodeType.MONTHLY, gov.epa.cef.web.domain.ReportingPeriodCodeType.SEMIANNUAL)")
    List<ReportingPeriodCode> findAllMonthsAndSemiAnnual();
}
