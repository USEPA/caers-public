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

import gov.epa.cef.web.domain.EmissionFormulaVariable;
import org.springframework.transaction.annotation.Transactional;

public interface EmissionFormulaVariableRepository extends CrudRepository<EmissionFormulaVariable, String> {

	   /**
	    * Retrieve a list of all emission formula variables for a specific program system code and emissions reporting year where the associated facility is operating
	    * @param psc Program System Code
	    * @param emissionsReportYear
	    * @return
	    */
	   @Query("select efv from EmissionFormulaVariable efv join efv.emission e join e.reportingPeriod rp join rp.emissionsProcess ep join ep.emissionsUnit eu join eu.facilitySite fs join fs.emissionsReport er where er.programSystemCode.code = :psc and er.year = :emissionsReportYear "
           + "and (er.thresholdStatus in (gov.epa.cef.web.domain.ThresholdStatus.OPERATING_ABOVE_THRESHOLD) or er.thresholdStatus is null) and fs.operatingStatusCode.code = 'OP' and er.isDeleted = false")
	   List<EmissionFormulaVariable> findByPscAndEmissionsReportYear(String psc, Short emissionsReportYear);

    /**
     * Delete by facility site ID
     */
    @Transactional
    @Modifying
    @Query("delete from EmissionFormulaVariable where id in (select efv.id from EmissionFormulaVariable efv join efv.emission e join e.reportingPeriod rp " +
                                                "join rp.emissionsProcess ep join ep.emissionsUnit eu join eu.facilitySite fs where fs.id = :facilitySiteId)")
    void deleteByFacilitySite(Long facilitySiteId);
}
