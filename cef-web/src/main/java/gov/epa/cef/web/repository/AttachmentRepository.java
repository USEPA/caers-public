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
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import gov.epa.cef.web.config.CacheName;
import gov.epa.cef.web.domain.Attachment;

public interface AttachmentRepository extends CrudRepository<Attachment, Long>, ProgramIdRetriever, ReportIdRetriever {

	/**
   * Find all attachments with the specified report id
   * @param reportId
   * @return
   */
	@Query("select ra from Attachment ra join ra.emissionsReport r where r.id = :reportId and r.isDeleted = false")
	List<Attachment> findAllByReportId(@Param("reportId") Long reportId);

   @Query("select mfr.id from Attachment ra join ra.emissionsReport r join r.masterFacilityRecord mfr where ra.id = :id and r.isDeleted = false")
   Optional<Long> retrieveMasterFacilityRecordIdById(@Param("id") Long id);

   @Query("select a from Attachment a join a.communication c where c.id = :communicationId")
   Optional<Attachment> findByCommunication(@Param("communicationId") Long communicationId);

   @Transactional
   @Modifying
   @Query("delete from Attachment where id in (select a.id from Attachment a join a.communication c where c.emailStatus = null)")
   void deleteAllCommunicationAttachmentsEmailStatusNotSent();

   /**
   * Retrieve Emissions Report id for a report attachment
   * @param id
   * @return Emissions Report id
   */
  @Cacheable(value = CacheName.ReportAttachmentReportIds)
  @Query("select r.id from Attachment ra join ra.emissionsReport r where ra.id = :id and r.isDeleted = false")
  Optional<Long> retrieveEmissionsReportById(@Param("id") Long id);

}
