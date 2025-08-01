/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.repository;

import gov.epa.cef.web.config.CommonInitializers;
import gov.epa.cef.web.domain.Control;
import gov.epa.cef.web.domain.ControlAssignment;
import gov.epa.cef.web.domain.ControlMeasureCode;
import gov.epa.cef.web.domain.FacilitySite;
import gov.epa.cef.web.domain.OperatingStatusCode;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import javax.sql.DataSource;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@SqlGroup(value = {@Sql("classpath:db/test/baseTestData.sql")})
@ContextConfiguration(initializers = {
    CommonInitializers.NoCacheInitializer.class
})
public class ControlRepositoryTest extends BaseRepositoryTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    DataSource dataSource;

    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    ControlRepository repository;

    @Autowired
    ControlAssignmentRepository controlAssignmentRepo;

    @Before
    public void _onJunitBeginTest() {

        this.jdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
    }

    @Test
    public void createControlTest() throws Exception {

        Control control = newControlInstance();

        this.repository.save(control);

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("id", control.getId());

        List<Map<String, Object>> controls = this.jdbcTemplate.queryForList(
            "select * from control where id = :id", params);

        assertEquals(1, controls.size());
    }

    @Test
    public void deleteControlTest() throws Exception {

        Control control = this.repository.findById(9999992L)
            .orElseThrow(() -> new IllegalStateException("Control 9999992L does not exist."));

        Optional<ControlAssignment> controlAssignment = this.controlAssignmentRepo.findById(9999994L);
            assertEquals(true, controlAssignment.isPresent());

        this.repository.delete(control);

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("id", control.getId());

        List<Map<String, Object>> controls = this.jdbcTemplate.queryForList(
            "select * from control where id = :id", params);

        assertTrue(controls.isEmpty());

        controlAssignment = this.controlAssignmentRepo.findById(9999994L);
        assertEquals(false, controlAssignment.isPresent());

    }

    @Test
    public void updateControlTest() throws Exception {

        Control control = this.repository.findById(9999992L)
            .orElseThrow(() -> new IllegalStateException("Control 9999992L does not exist."));

        String description = String.format("%s new description", UUID.randomUUID().toString());

        control.setDescription(description);
        this.repository.save(control);

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("id", control.getId());

        List<Map<String, Object>> controls = this.jdbcTemplate.queryForList(
            "select * from control where id = :id", params);

        assertEquals(1, controls.size());

        assertEquals(description, controls.get(0).get("description"));
    }


    private Control newControlInstance() {

        FacilitySite facilitySite = new FacilitySite();
        facilitySite.setId(9999991L);

        OperatingStatusCode operatingStatusCode = new OperatingStatusCode();
        operatingStatusCode.setCode("I");

        ControlMeasureCode controlMeasureCode = new ControlMeasureCode();
        controlMeasureCode.setCode("1");
        controlMeasureCode.setDescription("Control measure code description");

        Control control = new Control();
        control.setComments("I got something to say - It's better to burn out than to fade away.");
        control.setDescription("Totally non-descriptive description.");
        control.setFacilitySite(facilitySite);
        control.setOperatingStatusCode(operatingStatusCode);
        control.setIdentifier("Control 001");
        control.setPercentControl(BigDecimal.valueOf(50));
        control.setControlMeasureCode(controlMeasureCode);
        control.setCreatedBy("JUNIT-TEST");
        control.setCreatedDate(new Date());
        control.setLastModifiedBy(control.getCreatedBy());
        control.setLastModifiedDate(control.getCreatedDate());

        return control;
    }
}
