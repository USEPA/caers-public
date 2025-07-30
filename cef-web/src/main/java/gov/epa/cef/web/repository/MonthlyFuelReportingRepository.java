/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
