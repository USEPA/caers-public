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

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import gov.epa.cef.web.domain.SLTConfigProperty;

public interface SLTConfigRepository extends CrudRepository<SLTConfigProperty, Long> {

    List<SLTConfigProperty> findAll();

    List<SLTConfigProperty> findAll(Sort sort);

    /**
     * Find SLT Config Property by specified name and program system code
     * @param name
     * @param programSystemCodecode
     * @return
     */
    @Query("select scp from SLTConfigProperty scp join scp.sltPropertyDetails p where p.name = :name and scp.programSystemCode.code = :programSystemCode")
    Optional<SLTConfigProperty> findByNameAndProgramSystemCodeCode(@Param("name") String name, @Param("programSystemCode") String programSystemCode);

    List<SLTConfigProperty> findByProgramSystemCodeCode(String programSystemCode);

    @Query("select scp from SLTConfigProperty scp join scp.sltPropertyDetails p where scp.programSystemCode.code = :programSystemCode and p.name like '%report-attachment-upload.%' and scp.value = 'true'")
    List<SLTConfigProperty> getPermittedReportUploadTypes(@Param("programSystemCode") String programSystemCode);
    
    @Query("select scp.programSystemCode.code from SLTConfigProperty scp join scp.sltPropertyDetails p where p.name = :name and scp.value = 'true'")
    List<String> findAllSltByEnabledProperty(@Param("name") String name);
}
