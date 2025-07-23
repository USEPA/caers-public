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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import gov.epa.cef.web.config.CommonInitializers;
import gov.epa.cef.web.service.dto.EmissionsReportDto;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionsReportBulkUploadDto;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SqlGroup(value = {@Sql("classpath:db/test/emptyTestData.sql")})
@ContextConfiguration(initializers = {
    CommonInitializers.NoCacheInitializer.class
})
public class BulkUploadServiceImplTest extends BaseServiceDatabaseTest {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper jsonMapper;

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
    public void saveBulkEmissionsReportTest() throws Exception {

        EmissionsReportBulkUploadDto dto =
            hydrateJsonObject("saveBulkEmissionsReport.json", EmissionsReportBulkUploadDto.class)
                .orElse(null);

        assertNotNull(dto);

        EmissionsReportDto report = this.uploadService.saveBulkEmissionsReport(dto);

        List<Map<String, Object>> facilitySites = this.jdbcTemplate.queryForList(
                "select * from facility_site", new MapSqlParameterSource());

        assertEquals(1, facilitySites.size());

        List<Map<String, Object>> emissionsUnits = this.jdbcTemplate.queryForList(
                "select * from emissions_unit", new MapSqlParameterSource());

        assertEquals(3, emissionsUnits.size());

        List<Map<String, Object>> emissionsProcesses = this.jdbcTemplate.queryForList(
                "select * from emissions_process", new MapSqlParameterSource());

        assertEquals(3, emissionsProcesses.size());

        List<Map<String, Object>> releasePoints = this.jdbcTemplate.queryForList(
                "select * from release_point", new MapSqlParameterSource());

        assertEquals(3, releasePoints.size());

        List<Map<String, Object>> releasePointAppts = this.jdbcTemplate.queryForList(
                "select * from release_point_appt", new MapSqlParameterSource());

        assertEquals(8, releasePointAppts.size());

        List<Map<String, Object>> emissions = this.jdbcTemplate.queryForList(
                "select * from emission", new MapSqlParameterSource());

        assertEquals(3, emissions.size());

        List<Map<String, Object>> controls = this.jdbcTemplate.queryForList(
                "select * from control", new MapSqlParameterSource());

        assertEquals(2, controls.size());

        List<Map<String, Object>> controlPaths = this.jdbcTemplate.queryForList(
                "select * from control_path", new MapSqlParameterSource());

        assertEquals(4, controlPaths.size());

        List<Map<String, Object>> controlAssignments = this.jdbcTemplate.queryForList(
                "select * from control_assignment", new MapSqlParameterSource());

        assertEquals(4, controlAssignments.size());

        List<Map<String, Object>> controlPollutants = this.jdbcTemplate.queryForList(
                "select * from control_pollutant", new MapSqlParameterSource());

        assertEquals(3, controlPollutants.size());

        List<Map<String, Object>> facilityContacts = this.jdbcTemplate.queryForList(
        				"select * from facility_site_contact", new MapSqlParameterSource());

        assertEquals(2, facilityContacts.size());

        List<Map<String, Object>> facilityNAICS = this.jdbcTemplate.queryForList(
        				"select * from facility_naics_xref", new MapSqlParameterSource());

        assertEquals(2, facilityNAICS.size());
    }

    private <T> Optional<T> hydrateJsonObject(String resourceName, Class<T> clazz) throws Exception {

        String fullname = String.format("json/bulkUploadServiceImplTest/%s", resourceName);

        return Optional.of(this.jsonMapper.readValue(Resources.getResource(fullname), clazz));
    }
}
