/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import gov.epa.cef.web.domain.FipsCounty;

public interface FipsCountyRepository extends CrudRepository<FipsCounty, String> {

    @QueryHints({
        @QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<FipsCounty> findAll(Sort sort);
    
    @QueryHints({
        @QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("select fc from FipsCounty fc where fc.lastInventoryYear = null or fc.lastInventoryYear >= :year")
    List<FipsCounty> findAllCurrent(@Param("year") int year, Sort sort);
    
    @QueryHints({
        @QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("select fc from FipsCounty fc where fc.fipsStateCode.code = :stateCode and (fc.lastInventoryYear = null or fc.lastInventoryYear >= :year)")
    List<FipsCounty> findCurrentByFipsStateCodeCode(@Param("stateCode") String stateCode, @Param("year") int year, Sort sort);

    List<FipsCounty> findByFipsStateCodeCode(String stateCode, Sort sort);

    Optional<FipsCounty> findByFipsStateCodeCodeAndName(String stateCode, String name);

    @QueryHints({
        @QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Optional<FipsCounty> findFirstByCountyCode(String countyCode);

    Optional<FipsCounty> findByFipsStateCodeCodeAndCountyCode(String stateCode, String countyCode);

}
