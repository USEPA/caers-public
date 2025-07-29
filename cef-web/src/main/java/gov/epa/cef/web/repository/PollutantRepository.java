/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
