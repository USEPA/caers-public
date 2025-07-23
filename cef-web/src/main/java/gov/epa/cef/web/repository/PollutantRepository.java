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

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import gov.epa.cef.web.domain.Pollutant;
import org.springframework.data.repository.query.Param;

public interface PollutantRepository extends CrudRepository<Pollutant, String> {

    @Override
    @QueryHints({
        @QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Iterable<Pollutant> findAll();

    @QueryHints({
        @QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("select p from Pollutant p where p.lastInventoryYear = null or p.lastInventoryYear >= :year")
    List<Pollutant> findAllCurrent(int year, Sort sort);

@QueryHints({
    @QueryHint(name = "org.hibernate.cacheable", value = "true")
})
@Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
    "FROM Pollutant p " +
    "INNER JOIN Emission e ON e.pollutant.id = p.pollutantCode " +
    "INNER JOIN ReportingPeriod rp ON e.reportingPeriod.id = rp.id " +
    "INNER JOIN EmissionsProcess ep ON rp.emissionsProcess.id = ep.id " +
    "INNER JOIN EmissionsUnit eu ON ep.emissionsUnit.id = eu.id " +
    "INNER JOIN FacilitySite fs ON eu.facilitySite.id = fs.id " +
    "INNER JOIN EmissionsReport er ON fs.emissionsReport.id = er.id " +
    "INNER JOIN MasterFacilityRecord mfr ON er.masterFacilityRecord.id = mfr.id " +
    "WHERE p.pollutantCode = :pollutantCode " +
    "AND er.year = (" +
    "   SELECT MAX(r2.year) " +
    "   FROM EmissionsReport r2 " +
    "   WHERE r2.masterFacilityRecord.id = :masterFacilityId " +
    "   AND r2.year < FUNCTION('YEAR', CURRENT_DATE)" +
    ")")

boolean wasReportedLastYear(@Param("pollutantCode") String pollutantCode,
                            @Param("masterFacilityId") Long masterFacilityId);
}
