/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
