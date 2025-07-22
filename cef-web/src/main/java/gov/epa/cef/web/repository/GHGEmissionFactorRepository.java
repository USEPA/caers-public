/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
