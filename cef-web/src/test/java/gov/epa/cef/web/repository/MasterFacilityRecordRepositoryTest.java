package gov.epa.cef.web.repository;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import gov.epa.cef.web.config.CommonInitializers;
import gov.epa.cef.web.domain.MasterFacilityNAICSXref;
import gov.epa.cef.web.domain.MasterFacilityRecord;
import gov.epa.cef.web.domain.NaicsCode;
import gov.epa.cef.web.domain.NaicsCodeType;

@SqlGroup(value = {@Sql("classpath:db/test/baseTestData.sql")})
@ContextConfiguration(initializers = {
    CommonInitializers.NoCacheInitializer.class
})
public class MasterFacilityRecordRepositoryTest extends BaseRepositoryTest {
	
	@Autowired
    DataSource dataSource;

    NamedParameterJdbcTemplate jdbcTemplate;
    
    @Autowired
    MasterFacilityRecordRepository mfrRepo;
    
    @Autowired
    MasterFacilityNAICSXrefRepository naicsXrefRepo;
    
    @Autowired
    NaicsCodeRepository naicsRepo;
    
    @Before
    public void _onJunitBeginTest() {

        this.jdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
    }
    
    /**
     * Verify update of a master facility record
     * @throws Exception
     */
    @Test
    public void updateMasterFacilityInformationTest() throws Exception {
    	MasterFacilityRecord mf = mfrRepo.findByEisProgramId("9758611")
    			.orElseThrow(() -> new IllegalStateException("Master Facility Site does not exist."));
    	
    	assertEquals("Pulp and Paper Processing Plant", mf.getDescription());
    	
    	mf.setDescription("Paper Plant");
    	mfrRepo.save(mf);
    	
    	//verify information was updated
		SqlParameterSource params = new MapSqlParameterSource().addValue("id", mf.getId());

		List<Map<String, Object>> masterFacility = this.jdbcTemplate
				.queryForList("select * from master_facility_record where id = :id", params);

		assertEquals(1, masterFacility.size());
		assertEquals("Paper Plant", masterFacility.get(0).get("description"));
    }
    
    /**
     * Verify creating master facility NAICS
     * @throws Exception
     */
    @Test
    public void createMasterFacilityNaicsTest() throws Exception {
    	
    	MasterFacilityRecord mf = mfrRepo.findByEisProgramId("9758611")
    			.orElseThrow(() -> new IllegalStateException("Master Facility Site does not exist."));
    	
    	naicsXrefRepo.deleteAll(naicsXrefRepo.findByMasterFacilityRecordId(mf.getId()));
    	List<MasterFacilityNAICSXref> mfNaicsList = naicsXrefRepo.findByMasterFacilityRecordId(mf.getId());
    	assertEquals(0, mfNaicsList.size());
    	
    	NaicsCode naics = new NaicsCode();
    	naics.setCode(311222);
    	
    	//create new master facility NAICS code
    	MasterFacilityNAICSXref mfNaics = new MasterFacilityNAICSXref();
    	mfNaics.setMasterFacilityRecord(mf);
    	mfNaics.setNaicsCode(naics);
    	mfNaics.setNaicsCodeType(NaicsCodeType.SECONDARY);
    	naicsXrefRepo.save(mfNaics);
    	
    	//verify master facility NAICS code created
		SqlParameterSource params = new MapSqlParameterSource().addValue("id", mfNaics.getId());

		List<Map<String, Object>> masterFacilityNaics = this.jdbcTemplate
				.queryForList("select * from master_facility_naics_xref where id = :id", params);

		assertEquals(1, masterFacilityNaics.size());
		assertEquals(NaicsCodeType.SECONDARY.toString(), masterFacilityNaics.get(0).get("naics_code_type"));
		
		List<Map<String, Object>> mfNaicsCode = this.jdbcTemplate
				.queryForList("select * from naics_code n join master_facility_naics_xref mfn on n.code = mfn.naics_code where mfn.id = :id", params);
		
    	assertEquals(311222, ((BigDecimal) mfNaicsCode.get(0).get("code")).intValue());
    }
    
    /**
     * Verify update of master facility NAICS
     * @throws Exception
     */
    @Test
    public void updateMasterFacilityNaicTest() throws Exception {
    	
    	MasterFacilityNAICSXref mfNaics = naicsXrefRepo.findById(9999991L)
    			.orElseThrow(() -> new IllegalStateException("Master Facility NAICS 9999991L does not exist."));
    	
    	assertEquals(NaicsCodeType.PRIMARY, mfNaics.getNaicsCodeType());
    	assertEquals("4241", mfNaics.getNaicsCode().getCode().toString());
    	
    	//update master facility NAICS information
    	NaicsCode naics = new NaicsCode();
    	naics.setCode(311222);
    	
    	mfNaics.setNaicsCode(naics);
    	mfNaics.setNaicsCodeType(NaicsCodeType.TERTIARY);
    	naicsXrefRepo.save(mfNaics);
    	
    	//verify information was updated
		SqlParameterSource params = new MapSqlParameterSource().addValue("id", mfNaics.getId());

		List<Map<String, Object>> masterFacilityNaics = this.jdbcTemplate
				.queryForList("select * from master_facility_naics_xref where id = :id", params);

		assertEquals(1, masterFacilityNaics.size());
		assertEquals(NaicsCodeType.TERTIARY.toString(), masterFacilityNaics.get(0).get("naics_code_type"));
		
		List<Map<String, Object>> mfNaicsCode = this.jdbcTemplate
				.queryForList("select * from naics_code n join master_facility_naics_xref mfn on n.code = mfn.naics_code where mfn.id = :id", params);
		
    	assertEquals(311222, ((BigDecimal) mfNaicsCode.get(0).get("code")).intValue());
    }
    
    /**
     * Verify deleting a master facility NAICS
     * @throws Exception
     */
    @Test
    public void deletingMasterFacilityNaic_return_false_WhenMasterFacilityNaicDoesNotExist() throws Exception {
    	
    	//verify master facility NAICS code exist
    	Optional<MasterFacilityNAICSXref> mfNaics = naicsXrefRepo.findById(9999991L);
    	assertEquals(true, mfNaics.isPresent());
    	
    	//delete master facility NAICS code
        naicsXrefRepo.deleteById(9999991L);
        
        //verify master facility NAICS code does not exist
        mfNaics = naicsXrefRepo.findById(9999991L);
        assertEquals(false, mfNaics.isPresent());
    }

}
