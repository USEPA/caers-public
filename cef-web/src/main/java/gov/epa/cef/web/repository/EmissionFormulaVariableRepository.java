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
