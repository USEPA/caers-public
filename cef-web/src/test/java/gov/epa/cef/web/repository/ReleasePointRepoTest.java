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
import gov.epa.cef.web.domain.EmissionsProcess;
import gov.epa.cef.web.domain.FacilitySite;
import gov.epa.cef.web.domain.OperatingStatusCode;
import gov.epa.cef.web.domain.ReleasePoint;
import gov.epa.cef.web.domain.ReleasePointAppt;
import gov.epa.cef.web.domain.ReleasePointTypeCode;
import gov.epa.cef.web.domain.UnitMeasureCode;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@SqlGroup(value = {@Sql("classpath:db/test/baseTestData.sql")})
@ContextConfiguration(initializers = {
    CommonInitializers.NoCacheInitializer.class
})
public class ReleasePointRepoTest extends BaseRepositoryTest {

	@Autowired
    DataSource dataSource;

    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    ReleasePointRepository rpRepo;

    @Autowired
    ReleasePointApptRepository rpApptRepo;

    @Autowired
    EmissionsProcessRepository processRepo;

    @Before
    public void _onJunitBeginTest() {

        this.jdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
    }

    @Test
    public void createReleasePointTest() throws Exception {

    	//delete the all release points from facility 9999992
    	rpRepo.deleteAll(rpRepo.findByFacilitySiteIdOrderByReleasePointIdentifier(9999992L));

    	List<ReleasePoint> releasePtList = rpRepo.findByFacilitySiteIdOrderByReleasePointIdentifier(9999992L);
    	assertEquals(0, releasePtList.size());

    	//create release point
    	ReleasePoint releasePt = newReleasePt();

        this.rpRepo.save(releasePt);

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("id", releasePt.getId());

        List<Map<String, Object>> releasePts = this.jdbcTemplate.queryForList(
            "select * from release_point where id = :id", params);

        assertEquals(1, releasePts.size());
    }

    /**
     * Verify that update of a release point works
     * @throws Exception
     */
    @Test
    public void updateReleasePointTest() throws Exception {
    	ReleasePoint releasePoint = this.rpRepo.findById(9999991L)
    		.orElseThrow(() -> new IllegalStateException("Release point 9999991L does not exist."));

    	assertEquals("A big smokestack", releasePoint.getDescription());

    	releasePoint.setDescription("A big vent");

    	rpRepo.save(releasePoint);

    	//verify information was updated
		SqlParameterSource params = new MapSqlParameterSource().addValue("id", releasePoint.getId());

		List<Map<String, Object>> releasePt = this.jdbcTemplate
				.queryForList("select * from release_point where id = :id", params);

		assertEquals(1, releasePt.size());
		assertEquals("A big vent", releasePt.get(0).get("description"));
    }

    /**
     * Verify that deleting a release point works and any child apportioned processes
     * are also deleted
     * @throws Exception
     */
    @Test
    public void deletingReleasePoint_should_DeleteChildApportionedProcesses() throws Exception {

        //verify the release point and process exist
        Optional<ReleasePoint> releasePoint = rpRepo.findById(9999991L);
        assertEquals(true, releasePoint.isPresent());

        List<EmissionsProcess> process = processRepo.findByReleasePointApptsReleasePointIdOrderByEmissionsProcessIdentifier(9999991L);
        assertEquals(true, process.size()>0);

        //delete the release point and verify that the processes are gone as well
        rpRepo.deleteById(9999991L);

        //verify the release point and process exist
        releasePoint = rpRepo.findById(9999991L);
        assertEquals(false, releasePoint.isPresent());

        process = processRepo.findByReleasePointApptsReleasePointIdOrderByEmissionsProcessIdentifier(9999991L);
        assertEquals(0, process.size());
    }

    /**
     * Verify that deleting a release point apportionment works
     * @throws Exception
     */
    @Test
    public void deletingReleasePointAppt() throws Exception {

    	//verify the release point apportionment, release point, and process exist
    	Optional<ReleasePointAppt> releasePtAppt = rpApptRepo.findById(9999991L);
    	assertEquals(true, releasePtAppt.isPresent());

    	Optional<ReleasePoint> releasePoint = rpRepo.findById(9999991L);
    	assertEquals(true, releasePoint.isPresent());

    	Optional<EmissionsProcess> process = processRepo.findById(9999991L);
    	assertEquals(true, process.isPresent());

        //delete the release point apportionment and verify only release point apportionments is deleted.
        rpApptRepo.deleteById(9999991L);

        //verify the release point and process exist
        releasePtAppt = rpApptRepo.findById(9999991L);
        assertEquals(false, releasePtAppt.isPresent());

        releasePoint = rpRepo.findById(9999991L);
        assertEquals(true, releasePoint.isPresent());

        process = processRepo.findById(9999991L);
        assertEquals(true, process.isPresent());
    }

    /**
     * Verify that creating a release point apportionment works
     * @throws Exception
     */
    @Test
    public void createReleasePointApptTest() throws Exception {

    	//delete the all release point apportionments from Emissions Process 9999992
    	rpApptRepo.deleteAll(rpApptRepo.findByEmissionsProcessId(9999992L));

    	List<ReleasePointAppt> releasePtApptList = rpApptRepo.findByEmissionsProcessId(9999992L);
    	assertEquals(0, releasePtApptList.size());

    	//create release point apportionment
    	ReleasePointAppt releasePtAppt = newReleasePtAppt();

        this.rpApptRepo.save(releasePtAppt);

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("id", releasePtAppt.getId());

        List<Map<String, Object>> releasePtAppts = this.jdbcTemplate.queryForList(
            "select * from release_point_appt where id = :id", params);

        assertEquals(1, releasePtAppts.size());
    }

    /**
     * Verify that update of a release point apportionment works
     * @throws Exception
     */
    @Test
    public void updateReleasePointApptTest() throws Exception {
    	ReleasePointAppt releasePointAppt = this.rpApptRepo.findById(9999991L)
    		.orElseThrow(() -> new IllegalStateException("Release point apportionment 9999991L does not exist."));

    	assertEquals(BigDecimal.valueOf(33.0).setScale(2), releasePointAppt.getPercent());

    	releasePointAppt.setPercent(BigDecimal.valueOf(44.0));

    	rpApptRepo.save(releasePointAppt);

    	//verify information was updated
		SqlParameterSource params = new MapSqlParameterSource().addValue("id", releasePointAppt.getId());

		List<Map<String, Object>> releasePtAppt = this.jdbcTemplate
				.queryForList("select * from release_point_appt where id = :id", params);
		
		assertEquals(1, releasePtAppt.size());
		assertEquals(BigDecimal.valueOf(44.0).setScale(2), releasePtAppt.get(0).get("percent"));
    }

private ReleasePointAppt newReleasePtAppt() {
	EmissionsProcess emissionsProcess = new EmissionsProcess();
    emissionsProcess.setId(9999992L);

    ReleasePoint releasePoint = new ReleasePoint();
    releasePoint.setId(9999992L);

    ReleasePointAppt releasePtAppt = new ReleasePointAppt();

    releasePtAppt.setEmissionsProcess(emissionsProcess);
    releasePtAppt.setPercent(BigDecimal.valueOf(11.00));
    releasePtAppt.setReleasePoint(releasePoint);

    return releasePtAppt;
}

private ReleasePoint newReleasePt() {

    	FacilitySite facilitySite = new FacilitySite();
        facilitySite.setId(9999992L);

        OperatingStatusCode operatingStatusCode = new OperatingStatusCode();
        operatingStatusCode.setCode("I");
        operatingStatusCode.setDescription("Fugitive");

        UnitMeasureCode distanceUom = new UnitMeasureCode();
        distanceUom.setCode("FT");

        UnitMeasureCode velocityUom = new UnitMeasureCode();
        velocityUom.setCode("FT/HR");

        UnitMeasureCode flowUom = new UnitMeasureCode();
        flowUom.setCode("FT3/HR");

        ReleasePointTypeCode releasePointTypeCode = new ReleasePointTypeCode();
        releasePointTypeCode.setCode("1");

        ReleasePoint releasePt = new ReleasePoint();

        releasePt.setFacilitySite(facilitySite);
        releasePt.setOperatingStatusCode(operatingStatusCode);
        releasePt.setTypeCode(releasePointTypeCode);
        releasePt.setReleasePointIdentifier("TestRP");
        releasePt.setDescription("Test Description");
        releasePt.setStackHeightUomCode(distanceUom);
        releasePt.setStackDiameterUomCode(distanceUom);
        releasePt.setExitGasVelocityUomCode(velocityUom);
        releasePt.setExitGasFlowUomCode(flowUom);
        releasePt.setLatitude(BigDecimal.valueOf(1111.000000));
        releasePt.setLongitude(BigDecimal.valueOf(1111.000000));

        return releasePt;
    }

}
