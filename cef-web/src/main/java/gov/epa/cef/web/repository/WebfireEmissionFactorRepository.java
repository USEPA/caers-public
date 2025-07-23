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

import gov.epa.cef.web.domain.WebfireEmissionFactor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import javax.persistence.QueryHint;

public interface WebfireEmissionFactorRepository extends JpaRepository<WebfireEmissionFactor, Long> {

	@Query("select ef from WebfireEmissionFactor ef where ef.sccCode = :sccCode and ef.pollutantCode = :pollutantCode and ef.controlIndicator = :controlIndicator")
	List<WebfireEmissionFactor> findBySccCodePollutantControlIndicator(@Param("sccCode") String sccCode, @Param("pollutantCode") String pollutantCode,
                                                                                      @Param("controlIndicator") boolean controlIndicator);

    @Query("select ef from WebfireEmissionFactor ef where ef.webfireId = :webfireId")
    WebfireEmissionFactor findByWebfireId(@Param("webfireId") Long webfireId);

    @Query("select distinct ef.emissionFactorFormula from WebfireEmissionFactor ef"
            + " where ef.emissionFactorFormula is not null and ef.revoked = false")
    @QueryHints({
        @QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<String> findDistinctWebfireEFFormulasNotRevoked(Sort sort);
}
