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
