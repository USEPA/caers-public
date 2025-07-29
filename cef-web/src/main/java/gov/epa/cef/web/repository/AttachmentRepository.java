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
