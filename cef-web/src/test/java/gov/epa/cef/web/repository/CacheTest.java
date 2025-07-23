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

import gov.epa.cef.web.domain.UnitMeasureCode;
import gov.epa.cef.web.service.LookupService;
import gov.epa.cef.web.service.dto.CodeLookupDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CacheTest extends BaseRepositoryTest {

    private static final String BARRELS = "BARRELS";
    private static final String BBL = "BBL";
    private static final String BABY_BOTTLES = "Baby Bottles";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private Environment environment;

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private LookupService lookupService;

    @Before
    public void _onJunitBeginTest() {

        String configLocation = this.environment.getRequiredProperty("spring.hazelcast.config");
        assertEquals("classpath:cef-hazelcast-cache-test.xml", configLocation);

        logger.info("Active caches {}", String.join(", ", this.cacheManager.getCacheNames()));

        this.jdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);

        resetToBarrelsAndClearCache();
    }

    @After
    public void _onJunitEndTest() {

        resetToBarrelsAndClearCache();
    }

    @Test
    public void testFindAll() throws Exception {

        CodeLookupDto barrel1 = findBarrelFromList();

        assertNotNull(barrel1);
        assertEquals(BARRELS, barrel1.getDescription());

        updateToBabyBottles();

        CodeLookupDto barrel2 = findBarrelFromList();

        assertNotNull(barrel2);
        assertEquals(BARRELS, barrel2.getDescription());

        Thread.sleep(2000);

        barrel2 = findBarrelFromList();

        assertNotNull(barrel2);
        assertEquals(BARRELS, barrel2.getDescription());

        Thread.sleep(18100);

        CodeLookupDto barrel3 = findBarrelFromList();

        assertNotNull(barrel3);
        assertEquals(BABY_BOTTLES, barrel3.getDescription());
    }

    @Test
    public void testFindOne() throws Exception {

        UnitMeasureCode barrel1 = this.lookupService.retrieveUnitMeasureCodeEntityByCode(BBL);

        assertEquals(BBL, barrel1.getCode());
        assertEquals(BARRELS, barrel1.getDescription());

        updateToBabyBottles();

        UnitMeasureCode barrel2 = this.lookupService.retrieveUnitMeasureCodeEntityByCode(BBL);

        assertEquals(BBL, barrel2.getCode());
        assertEquals(BARRELS, barrel2.getDescription());

        Thread.sleep(2000);

        barrel2 = this.lookupService.retrieveUnitMeasureCodeEntityByCode(BBL);
        assertEquals(BBL, barrel2.getCode());
        assertEquals(BARRELS, barrel2.getDescription());

        // wait a few secs for cache to expire
        Thread.sleep(18100);

        UnitMeasureCode barrel3 = this.lookupService.retrieveUnitMeasureCodeEntityByCode(BBL);

        assertEquals(BBL, barrel3.getCode());
        assertEquals(BABY_BOTTLES, barrel3.getDescription());
    }

    private CodeLookupDto findBarrelFromList() {

        CodeLookupDto result = null;

        for (CodeLookupDto dto : this.lookupService.retrieveUnitMeasureCodes()) {

            if (BBL.equals(dto.getCode())) {
                result = dto;
                break;
            }
        }

        return result;
    }

    private void resetToBarrelsAndClearCache() {

        logger.info("Resetting BBL to BARRELS");
        updateToBarrels();

        logger.info("Clearing all caches.");
        this.cacheManager.getCacheNames().forEach(cacheName -> {

            this.cacheManager.getCache(cacheName).clear();
        });
    }

    private void updateToBabyBottles() {

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("description", BABY_BOTTLES)
            .addValue("code", BBL);

        this.jdbcTemplate.update(
            "update unit_measure_code set description = :description where code = :code",
            params);
    }

    private void updateToBarrels() {

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("description", BARRELS)
            .addValue("code", BBL);

        this.jdbcTemplate.update(
            "update unit_measure_code set description = :description where code = :code",
            params);
    }
}
