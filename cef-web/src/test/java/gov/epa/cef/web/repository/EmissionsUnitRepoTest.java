/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.repository;

import gov.epa.cef.web.config.CommonInitializers;
import gov.epa.cef.web.domain.EmissionsProcess;
import gov.epa.cef.web.domain.EmissionsUnit;
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
public class EmissionsUnitRepoTest extends BaseRepositoryTest {

    @Autowired
    EmissionsUnitRepository unitRepo;

    @Autowired
    EmissionsProcessRepository processRepo;

    /**
     * Verify that deleting an emission unit works and that any child emission processes
     * are also deleted
     * @throws Exception
     */
    @Test
    public void deletingEmissionsUnit_should_DeleteChildEmissionsProcesses() throws Exception {

        //verify the unit and process exist
        Optional<EmissionsUnit> unit = unitRepo.findById(9999991L);
        assertEquals(true, unit.isPresent());

        Optional<EmissionsProcess> process = processRepo.findById(9999991L);
        assertEquals(true, process.isPresent());

        //delete the unit and verify that the processes are gone as well
        unitRepo.deleteById(9999991L);

        //verify the unit and process exist
        unit = unitRepo.findById(9999991L);
        assertEquals(false, unit.isPresent());

        process = processRepo.findById(9999991L);
        assertEquals(false, process.isPresent());

    }

}
