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
package gov.epa.cef.web.util;

import gov.epa.cef.web.config.CommonInitializers;
import gov.epa.cef.web.config.TestCategories;
import gov.epa.cef.web.domain.UnitMeasureCode;
import gov.epa.cef.web.repository.UnitMeasureCodeRepository;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SqlGroup(value = {@Sql("classpath:db/test/emptyTestData.sql")})
@ContextConfiguration(initializers = {
    CommonInitializers.NoCacheInitializer.class
})
@Category(TestCategories.EmbeddedDatabaseTest.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase(provider = DatabaseProvider.ZONKY)
@WithUserDetails(userDetailsServiceBeanName = "junitUserDetailsServiceImpl")
public class CalculationUtilsConversionTest {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    DataSource dataSource;

    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    UnitMeasureCodeRepository uomRepo;

    @Before
    public void _onJunitBeginTest() {

        this.jdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
    }

    @Test
    public void convertMassUnits_Should_Return_ConvertedUnits_When_ValidValuesPassed() {

        Map<String, List<UnitMeasureCode>> units = StreamSupport.stream(this.uomRepo.findAll().spliterator(), false)
                .filter(code -> !"UNSUPPORTED".equals(code.getUnitType()))
                .collect(Collectors.groupingBy(UnitMeasureCode::getUnitType));

        units.values().forEach(codeList -> {
            codeList.forEach(code -> {
                codeList.forEach(targetCode -> {
                    if (!code.getCode().equals(targetCode.getCode())) {
//                        logger.info("Converting {} to {}", code.getDescription(), targetCode.getDescription());
                        BigDecimal result = CalculationUtils.convertUnits(code.getCalculationVariable(), targetCode.getCalculationVariable());
//                        logger.info("Converted {} to {} result {}", code.getDescription(), targetCode.getDescription(), result);
                        assertNotNull(result); 
                    }
                });
            });
        });

    }

}
