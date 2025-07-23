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
