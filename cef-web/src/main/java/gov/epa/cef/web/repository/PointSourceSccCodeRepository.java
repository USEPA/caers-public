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
