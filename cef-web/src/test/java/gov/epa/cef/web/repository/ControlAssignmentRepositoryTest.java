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

import gov.epa.cef.web.config.CommonInitializers;
import gov.epa.cef.web.domain.Control;
import gov.epa.cef.web.domain.ControlAssignment;
import gov.epa.cef.web.domain.ControlPath;
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

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@SqlGroup(value = {@Sql("classpath:db/test/baseTestData.sql")})
@ContextConfiguration(initializers = {
    CommonInitializers.NoCacheInitializer.class
})
public class ControlAssignmentRepositoryTest extends BaseRepositoryTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    DataSource dataSource;

    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    ControlAssignmentRepository repository;

    @Before
    public void _onJunitBeginTest() {

        this.jdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
    }

    @Test
    public void createControlAssignmentTest() throws Exception {

        ControlAssignment controlPathAssignment = newControlAssignmentInstance();

        this.repository.save(controlPathAssignment);

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("id", controlPathAssignment.getId());

        List<Map<String, Object>> controls = this.jdbcTemplate.queryForList(
            "select * from control_assignment where id = :id", params);

        assertEquals(1, controls.size());
    }

    @Test
    public void deleteControlAssignmentTest() throws Exception {

        ControlAssignment controlPathAssigment = this.repository.findById(9999991L)
            .orElseThrow(() -> new IllegalStateException("Control 9999991L does not exist."));

        this.repository.delete(controlPathAssigment);

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("id", controlPathAssigment.getId());

        List<Map<String, Object>> controlPathAssignments = this.jdbcTemplate.queryForList(
            "select * from control_assignment where id = :id", params);

        assertTrue(controlPathAssignments.isEmpty());

    }

    @Test
    public void updateControlPathAssignmentTest() throws Exception {

        ControlAssignment controlPathAssignment = this.repository.findById(9999991L)
            .orElseThrow(() -> new IllegalStateException("Control 9999991L does not exist."));

        BigDecimal percentApportionment = BigDecimal.valueOf(100);

        controlPathAssignment.setPercentApportionment(percentApportionment);
        this.repository.save(controlPathAssignment);

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("id", controlPathAssignment.getId());

        List<Map<String, Object>> controls = this.jdbcTemplate.queryForList(
            "select * from control_assignment where id = :id", params);

        assertEquals(1, controls.size());
        assertEquals(BigDecimal.valueOf(100.0).setScale(2), controls.get(0).get("percent_apportionment"));
    }


    private ControlAssignment newControlAssignmentInstance() {

        ControlPath controlPath = new ControlPath();
        controlPath.setId(9999991L);

        Control control = new Control();
        control.setId(9999991L);

        ControlPath controlPathChild = new ControlPath();
        controlPathChild.setId(9999991L);



        ControlAssignment controlPathAssignment = new ControlAssignment();
        controlPathAssignment.setControl(control);
        controlPathAssignment.setControlPath(controlPath);
        controlPathAssignment.setControlPathChild(controlPathChild);
        controlPathAssignment.setSequenceNumber(1);
        controlPathAssignment.setPercentApportionment(BigDecimal.ONE);
        controlPathAssignment.setCreatedBy("JUNIT-TEST");
        controlPathAssignment.setCreatedDate(new Date());
        controlPathAssignment.setLastModifiedBy(controlPathAssignment.getCreatedBy());
        controlPathAssignment.setLastModifiedDate(controlPathAssignment.getCreatedDate());


        return controlPathAssignment;
    }
}
