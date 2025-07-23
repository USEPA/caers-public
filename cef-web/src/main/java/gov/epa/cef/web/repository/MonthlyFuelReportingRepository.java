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

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import gov.epa.cef.web.domain.MonthlyFuelReporting;

public interface MonthlyFuelReportingRepository extends CrudRepository<MonthlyFuelReporting, Long> {

    /**
     * Return all monthly Reporting Periods for a facility site
     * @param facilitySiteId
     * @return
     */
    @Query("select m from MonthlyFuelReporting m where m.reportingPeriod in ("
    		+ "select distinct mr.reportingPeriod from MonthlyFuelReporting mr join mr.reportingPeriod rp join rp.emissionsProcess p join p.emissionsUnit eu join eu.facilitySite fs "
    		+ "where fs.id = :facilitySiteId)")
    List<MonthlyFuelReporting> findByFacilitySiteId(Long facilitySiteId);
   
    MonthlyFuelReporting findByReportingPeriodId(Long reportingPeriodId);
    
    @Transactional
    @Modifying
    @Query("delete from MonthlyFuelReporting where id in (select mr.id from MonthlyFuelReporting mr join mr.reportingPeriod rp join rp.emissionsProcess p where p.id = :processId)")
    void deleteByProcess(Long processId);
    
    @Transactional
    @Modifying
    @Query("delete from MonthlyFuelReporting where id in (select mr.id from MonthlyFuelReporting mr join mr.reportingPeriod rp join rp.emissionsProcess p join p.emissionsUnit u where u.id = :unitId)")
    void deleteByUnit(Long unitId);
    
}
