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

import gov.epa.cef.web.domain.FacilitySourceTypeCode;


public interface FacilitySourceTypeCodeRepository extends CrudRepository<FacilitySourceTypeCode, String> {
	
	@QueryHints({
    @QueryHint(name = "org.hibernate.cacheable", value = "true")})
	Iterable<FacilitySourceTypeCode> findAll(Sort sort);
	
	@QueryHints({
        @QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("select fstc from FacilitySourceTypeCode fstc where fstc.lastInventoryYear = null or fstc.lastInventoryYear >= :year")
    List<FacilitySourceTypeCode> findAllCurrent(@Param("year") int year, Sort sort);
}
