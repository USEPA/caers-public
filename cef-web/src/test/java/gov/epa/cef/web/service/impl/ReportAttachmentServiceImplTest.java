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
package gov.epa.cef.web.service.impl;

import gov.epa.cef.web.config.CommonInitializers;
import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.domain.MasterFacilityRecord;
import gov.epa.cef.web.domain.ProgramSystemCode;
import gov.epa.cef.web.domain.ReportStatus;
import gov.epa.cef.web.domain.ValidationStatus;
import gov.epa.cef.web.exception.FacilityAccessException;
import gov.epa.cef.web.exception.ReportAttachmentValidationException;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.service.dto.AttachmentDto;
import gov.epa.cef.web.util.TempFile;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;

import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SqlGroup(value = {@Sql("classpath:db/test/baseTestData.sql")})
@ContextConfiguration(initializers = {
    CommonInitializers.NoCacheInitializer.class
})
public class ReportAttachmentServiceImplTest extends BaseServiceDatabaseTest {


    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
	EmissionsReportRepository reportRepo;
    
    @Autowired
    private AttachmentServiceImpl rptAttachmentSvc;
    
    @Autowired
    WebApplicationContext webContext;

    @Autowired
    DataSource dataSource;

    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    BulkUploadServiceImpl uploadService;
    

    @Before
    public void _onJunitBeginTest() {

        this.jdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
        
    }
        
    
    @Test
    public void saveReportAttachmentTest() throws Exception {
    	
    	EmissionsReport report = createHydratedEmissionsReport();
    	report = reportRepo.save(report);
    	
    	String text ="Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", text.getBytes());
        
        TempFile temp = TempFile.from(file.getInputStream(), file.getOriginalFilename());
        
        AttachmentDto dto = new AttachmentDto();
        dto.setId(1L);
        dto.setReportId(report.getId());
        dto.setFileName(file.getOriginalFilename());
        dto.setFileType(file.getContentType());
        
        String comments = "Test...";
    	
        assertNotNull(dto);
        
        this.rptAttachmentSvc.saveAttachment(temp, dto);

        List<Map<String, Object>> reportAttachments = this.jdbcTemplate.queryForList(
                "select * from attachment", new MapSqlParameterSource());

        assertEquals(1, reportAttachments.size());

    }
    
    @Test(expected = ReportAttachmentValidationException.class)
    public void saveReportAttachmentInvalidTypeTest() throws Exception {
    	
    	EmissionsReport report = createHydratedEmissionsReport();
    	report = reportRepo.save(report);
    	
    	String text ="Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", text.getBytes());
        
        TempFile temp = TempFile.from(file.getInputStream(), file.getOriginalFilename());
        
        AttachmentDto dto = new AttachmentDto();
        dto.setId(1L);
        dto.setReportId(report.getId());
        dto.setFileName(file.getOriginalFilename());
        dto.setFileType(file.getContentType());
        
        String comments = "Test...";
    	
        assertNotNull(dto);
        
        this.rptAttachmentSvc.saveAttachment(temp, dto);
    }

    private EmissionsReport createHydratedEmissionsReport() {
        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(9999991L);

        ProgramSystemCode psc = new ProgramSystemCode();
        psc.setCode("GADNR");

    	EmissionsReport er = new EmissionsReport();
		er.setProgramSystemCode(psc);
		er.setMasterFacilityRecord(mfr);
		er.setEisProgramId("ABC");
		er.setId(1L);
		er.setStatus(ReportStatus.APPROVED);
		er.setValidationStatus(ValidationStatus.PASSED);
		er.setReturnedReport(false);
		er.setYear((short) 2019);
		
    	return er;
    }
}
