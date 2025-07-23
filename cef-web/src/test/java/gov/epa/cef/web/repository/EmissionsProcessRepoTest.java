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
import gov.epa.cef.web.domain.Emission;
import gov.epa.cef.web.domain.EmissionsProcess;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@SqlGroup(value = {@Sql("classpath:db/test/baseTestData.sql")})
@ContextConfiguration(initializers = {
    CommonInitializers.NoCacheInitializer.class
})
public class EmissionsProcessRepoTest extends BaseRepositoryTest {

    @Autowired
    EmissionsProcessRepository processRepo;

    @Autowired
    EmissionRepository emissionRepo;

    /**
     * Verify that deleting an emission process works and that any child emissions
     * are also deleted
     * @throws Exception
     */
    @Test
    public void deletingProcess_should_DeleteChildEmissions() throws Exception {

        //verify the process and emissions exist
        Optional<EmissionsProcess> process = processRepo.findById(9999991L);
        assertEquals(true, process.isPresent());

        Optional<Emission> emission = emissionRepo.findById(9999991L);
        assertEquals(true, emission.isPresent());

        //delete the process and verify that the emissions are gone as well
        processRepo.deleteById(9999991L);

        //verify the process and emissions exist
        process = processRepo.findById(9999991L);
        assertEquals(false, process.isPresent());

        emission = emissionRepo.findById(9999991L);
        assertEquals(false, emission.isPresent());

    }

}
