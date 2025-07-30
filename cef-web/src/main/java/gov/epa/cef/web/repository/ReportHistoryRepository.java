/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import gov.epa.cef.web.domain.ReportHistory;

public interface ReportHistoryRepository extends CrudRepository<ReportHistory, Long> {

    /***
     * Return list of report history records
     * @param emissionsReportId
     * @return
     */
    List<ReportHistory> findByEmissionsReportIdOrderByActionDate(Long emissionsReportId);

    /**
     * Retrieve Log for an upload attachment file
     * @param reportAttachmentId
     * @return
     */
    @Query("SELECT rh FROM ReportHistory rh WHERE rh.reportAttachmentId = :reportAttachmentId")
    ReportHistory findByAttachmentId(@Param("reportAttachmentId") Long reportAttachmentId);

    /**
    * Return the latest submission date for a given report
    * @param id Report ID
    * @return
    */
   @Query("select max(actionDate) from ReportHistory rp where rp.emissionsReport.id = :id and rp.reportAction = gov.epa.cef.web.domain.ReportAction.SUBMITTED")
   Optional<Date> retrieveMaxSubmissionDateByReportId(@Param("id") Long id);

    /**
     * Return the latest semiannual submission date for a given report
     * @param id Report ID
     * @return
     */
    @Query("select max(actionDate) from ReportHistory rp where rp.emissionsReport.id = :id and rp.reportAction = gov.epa.cef.web.domain.ReportAction.SEMIANNUAL_SUBMITTED")
    Optional<Date> retrieveMaxSemiannualSubmissionDateByReportId(@Param("id") Long id);
}
