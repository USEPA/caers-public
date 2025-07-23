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

import gov.epa.cef.web.domain.AircraftEngineTypeCode;

import java.util.List;

import javax.persistence.QueryHint;

public interface AircraftEngineTypeCodeRepository extends CrudRepository<AircraftEngineTypeCode, String> {

    @QueryHints({
        @QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Iterable<AircraftEngineTypeCode> findAll(Sort sort);

    @QueryHints({
        @QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("select aec from AircraftEngineTypeCode aec where aec.lastInventoryYear = null or aec.lastInventoryYear >= :year")
    List<AircraftEngineTypeCode> findAllCurrent(int year, Sort sort);

    List<AircraftEngineTypeCode> findByScc(String scc, Sort sort);

    @Query("select aec from AircraftEngineTypeCode aec where aec.scc = :scc and (aec.lastInventoryYear = null or aec.lastInventoryYear >= :year)")
    List<AircraftEngineTypeCode> findCurrentByScc(int year, String scc, Sort sort);
}
