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
import org.springframework.data.repository.query.Param;

import gov.epa.cef.web.domain.PointSourceSccCode;

public interface PointSourceSccCodeRepository extends CrudRepository<PointSourceSccCode, String> {

	@QueryHints({
		@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	Iterable<PointSourceSccCode> findAll(Sort sort);

    @Query("select scc from PointSourceSccCode scc where scc.lastInventoryYear is null")
    @QueryHints({
        @QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<PointSourceSccCode> findAllNotRetired(Sort sort);

    @Query("select scc from PointSourceSccCode scc where scc.lastInventoryYear is null" +
        " and scc.category = gov.epa.cef.web.domain.SccCategory.Point")
    @QueryHints({
        @QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<PointSourceSccCode> findAllPointNotRetired(Sort sort);

	@Query("select pssc from PointSourceSccCode pssc where pssc.category = gov.epa.cef.web.domain.SccCategory.Point and (lower(pssc.code) like %:searchTerm% or lower(pssc.shortName) like %:searchTerm% "
			+ "or lower(pssc.sector) like %:searchTerm% or lower(pssc.sccLevelOne) like %:searchTerm% or lower(pssc.sccLevelTwo) like %:searchTerm% "
			+ "or lower(pssc.sccLevelThree) like %:searchTerm% or lower(pssc.sccLevelFour) like %:searchTerm%)")
	List<PointSourceSccCode> findPointBySearchTerm(@Param("searchTerm") String searchTerm, Sort sort);

    @Query("select pssc from PointSourceSccCode pssc where lower(pssc.code) like %:searchTerm% or lower(pssc.shortName) like %:searchTerm% "
        + "or lower(pssc.sector) like %:searchTerm% or lower(pssc.sccLevelOne) like %:searchTerm% or lower(pssc.sccLevelTwo) like %:searchTerm% "
        + "or lower(pssc.sccLevelThree) like %:searchTerm% or lower(pssc.sccLevelFour) like %:searchTerm%")
    List<PointSourceSccCode> findAnyBySearchTerm(@Param("searchTerm") String searchTerm, Sort sort);

    List<PointSourceSccCode> findByMonthlyReporting(Boolean bool);

    PointSourceSccCode findByCode(String code);
}
