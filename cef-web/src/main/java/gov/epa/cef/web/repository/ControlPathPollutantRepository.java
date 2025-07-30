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

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import gov.epa.cef.web.config.CacheName;
import gov.epa.cef.web.domain.ControlAssignment;
import gov.epa.cef.web.domain.ControlPathPollutant;

public interface ControlPathPollutantRepository extends CrudRepository<ControlPathPollutant, Long>, ProgramIdRetriever, ReportIdRetriever {

	/**
     * Retrieve Control Path Pollutants for a control
     * @param controlPathId
     * @return
     */
    List<ControlPathPollutant> findByControlPathId(Long controlPathId);

   @Cacheable(value = CacheName.ControlPathPollutantMasterIds)
   @Query("select mfr.id from ControlPathPollutant cpp join cpp.controlPath cp join cp.facilitySite fs join fs.emissionsReport r join r.masterFacilityRecord mfr where cpp.id = :id and r.isDeleted = false")
   Optional<Long> retrieveMasterFacilityRecordIdById(@Param("id") Long id);

   /**
    * Retrieve Emissions Report id for a Control Path Pollutant
    * @param id
    * @return Emissions Report id
    */
   @Cacheable(value = CacheName.ControlPathPollutantEmissionsReportIds)
   @Query("select r.id from ControlPathPollutant cpp join cpp.controlPath cp join cp.facilitySite fs join fs.emissionsReport r where cpp.id = :id")
   Optional<Long> retrieveEmissionsReportById(@Param("id") Long id);

   /**
    * Retrieve a list of all control path pollutants for a specific program system code and emissions reporting year where the associated facility is operating
    * @param psc Program System Code
    * @param emissionsReportYear
    * @return
    */
   @Query("select cpp from ControlPathPollutant cpp join cpp.controlPath cp join cp.facilitySite fs join fs.emissionsReport er where er.programSystemCode.code = :psc and er.year = :emissionsReportYear "
       + "and (er.thresholdStatus in (gov.epa.cef.web.domain.ThresholdStatus.OPERATING_ABOVE_THRESHOLD) or er.thresholdStatus is null) and fs.operatingStatusCode.code = 'OP'")
   List<ControlPathPollutant> findByPscAndEmissionsReportYear(String psc, Short emissionsReportYear);
}
