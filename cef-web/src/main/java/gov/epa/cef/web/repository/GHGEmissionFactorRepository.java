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

import gov.epa.cef.web.domain.GHGEmissionFactor;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import javax.persistence.QueryHint;

public interface GHGEmissionFactorRepository extends JpaRepository<GHGEmissionFactor, Long> {

    @Query("select ef from GHGEmissionFactor ef where ef.pollutantCode = :pollutantCode" +
    " and ef.controlIndicator = :controlIndicator and ef.calculationMaterialCode.code = :calculationMaterialCode" +
    " and ef.calculationParameterTypeCode.code = :calculationParameterTypeCode")
    List<GHGEmissionFactor> findByPollutantThroughputMaterialThroughputTypeControlIndicator(
        @Param("pollutantCode") String pollutantCode, @Param("controlIndicator") boolean controlIndicator,
        @Param("calculationMaterialCode") String calculationMaterialCode,
        @Param("calculationParameterTypeCode") String calculationParameterTypeCode);

    @Query("select distinct ef.emissionFactorFormula from GHGEmissionFactor ef"
            + " where ef.emissionFactorFormula is not null and ef.revoked = false")
    @QueryHints({
        @QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<String> findDistinctGHGEFFormulasNotRevoked(Sort sort);
}
