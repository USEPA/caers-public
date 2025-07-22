/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.repository;

import gov.epa.cef.web.config.CommonInitializers;
import gov.epa.cef.web.domain.FacilityNAICSXref;
import gov.epa.cef.web.domain.FacilitySite;
import gov.epa.cef.web.domain.NaicsCode;
import gov.epa.cef.web.domain.NaicsCodeType;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@SqlGroup(value = {@Sql("classpath:db/test/baseTestData.sql")})
@ContextConfiguration(initializers = {
    CommonInitializers.NoCacheInitializer.class
})
public class FacilitySiteRepoTest extends BaseRepositoryTest {

	@Autowired
    DataSource dataSource;

    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    FacilitySiteRepository facilitySiteRepo;

    @Autowired
    FacilityNAICSXrefRepository naicsXrefRepo;

    @Before
    public void _onJunitBeginTest() {

        this.jdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
    }

    /**
     * Verify update of a facility
     * @throws Exception
     */
    @Test
    public void updateFacilityInformationTest() throws Exception {

	    //update facility information
			FacilitySite updatefacilityInfo = facilitySiteRepo.findById(9999991L)
					.orElseThrow(() -> new IllegalStateException("Facility Site 9999991L does not exist."));

			assertEquals("173 Peachtree Rd", updatefacilityInfo.getStreetAddress());

			updatefacilityInfo.setStreetAddress("123 Test St");

			facilitySiteRepo.save(updatefacilityInfo);

			//verify information was updated
			SqlParameterSource params = new MapSqlParameterSource().addValue("id", updatefacilityInfo.getId());

			List<Map<String, Object>> facility = this.jdbcTemplate
					.queryForList("select * from facility_site where id = :id", params);

			assertEquals(1, facility.size());
			assertEquals("123 Test St", facility.get(0).get("street_address"));
    }

    /**
     * Verify creating a facility NAICS
     * @throws Exception
     */
    @Test
    public void createFacilityNaicsTest() throws Exception {

      FacilitySite facilitySite = facilitySiteRepo.findById(9999991L)
				.orElseThrow(() -> new IllegalStateException("Facility Site 9999991L does not exist."));
      
	  //delete the all facility NAICS codes from facility 9999991
	  naicsXrefRepo.deleteAll(naicsXrefRepo.findByFacilitySiteId(9999991L));

      List<FacilityNAICSXref> facilityNaicsList = naicsXrefRepo.findByFacilitySiteId(9999991L);
      assertEquals(0, facilityNaicsList.size());
      
      NaicsCode naics = new NaicsCode();
  	  naics.setCode(311222);

      //create new facility NAICS code
      FacilityNAICSXref facilityNaics = new FacilityNAICSXref();
      facilityNaics.setFacilitySite(facilitySite);
  	  facilityNaics.setNaicsCode(naics);
  	  facilityNaics.setNaicsCodeType(NaicsCodeType.SECONDARY);

      naicsXrefRepo.save(facilityNaics);

      //verify the facility NAICS code created
      facilityNaicsList = naicsXrefRepo.findByFacilitySiteId(9999991L);

      assertEquals(1, facilityNaicsList.size());
      assertEquals(NaicsCodeType.SECONDARY, facilityNaicsList.get(0).getNaicsCodeType());
      assertEquals("311222", facilityNaicsList.get(0).getNaicsCode().getCode().toString());
    }

    /**
     * Verify update of a facility NAICS
     * @throws Exception
     */
    @Test
    public void updateFacilityNaicTest() throws Exception {
    	//update facility NAICS information
    	FacilityNAICSXref updatefacilityNaic = naicsXrefRepo.findById(9999991L)
					.orElseThrow(() -> new IllegalStateException("Facility NAICS 9999991L does not exist."));

			assertEquals(NaicsCodeType.PRIMARY, updatefacilityNaic.getNaicsCodeType());

			updatefacilityNaic.setNaicsCodeType(NaicsCodeType.SECONDARY);

			naicsXrefRepo.save(updatefacilityNaic);

			//verify information was updated
			SqlParameterSource params = new MapSqlParameterSource().addValue("id", updatefacilityNaic.getId());

			List<Map<String, Object>> facilityNaic = this.jdbcTemplate
					.queryForList("select * from facility_naics_xref where id = :id", params);

			assertEquals(1, facilityNaic.size());
			assertEquals("SECONDARY", facilityNaic.get(0).get("naics_code_type"));
    }

    /**
     * Verify deleting an facility NAICS
     * @throws Exception
     */
    @Test
    public void deletingFacilityNaic_return_false_WhenFacilityNaicDoesNotExist() throws Exception {

      //verify the facility NAICS code exist
      Optional<FacilityNAICSXref> contact = naicsXrefRepo.findById(9999991L);
      assertEquals(true, contact.isPresent());

      //delete the facility NAICS code
      naicsXrefRepo.deleteById(9999991L);

      //verify the facility NAICS code does not exist
      contact = naicsXrefRepo.findById(9999991L);
      assertEquals(false, contact.isPresent());
    }


    private FacilityNAICSXref newFacilityNAICSXref() {

    	FacilityNAICSXref facilityNaics = new FacilityNAICSXref();

    	FacilitySite facilitySite = new FacilitySite();
      facilitySite.setId(9999991L);

      NaicsCode naics = new NaicsCode();
      naics.setCode(332116);
      naics.setDescription("Metal Stamping");

    	facilityNaics.setFacilitySite(facilitySite);
    	facilityNaics.setNaicsCode(naics);
    	facilityNaics.setNaicsCodeType(NaicsCodeType.PRIMARY);

    	return facilityNaics;
    }
}
