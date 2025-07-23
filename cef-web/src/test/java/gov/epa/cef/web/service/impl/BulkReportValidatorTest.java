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

import gov.epa.cef.web.config.TestCategories;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionsReportBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.FacilitySiteBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.WorksheetError;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Category(TestCategories.FastTest.class)
public class BulkReportValidatorTest {

    @Test
    public void testFacilityIdCheck() {

        String eis1 = "EISProgramId001";
        String eis2 = "EISProgramId002";
        String alt1 = "AltSiteId001";
        String alt2 = "AltSiteId002";
        String psc1 = "GADNR";
        String psc2 = "DOEE";

        EmissionsReportBulkUploadDto report = new EmissionsReportBulkUploadDto();
        report.setEisProgramId(eis1);
        report.setAgencyFacilityIdentifier(alt1);
        report.setProgramSystemCode(psc1);

        FacilitySiteBulkUploadDto facilitySite = new FacilitySiteBulkUploadDto();
        facilitySite.setEisProgramId(eis2);
        facilitySite.setAgencyFacilityIdentifier(alt2);
        facilitySite.setProgramSystemCode(psc2);

        List<WorksheetError> violations = new ArrayList<>();

        BulkReportValidator.FacilityIdValidator facilityIdValidator =
            new BulkReportValidator.FacilityIdValidator(report, violations);

        // all bad
        violations.clear();
        facilityIdValidator.accept(facilitySite);
        assertEquals(3, violations.size());

        // psc bad
        violations.clear();
        facilitySite.setEisProgramId(eis1);
        facilitySite.setAgencyFacilityIdentifier(alt1);
        facilityIdValidator.accept(facilitySite);
        assertEquals(1, violations.size());

        // eis bad
        violations.clear();
        facilitySite.setEisProgramId(eis2);
        facilitySite.setAgencyFacilityIdentifier(alt1);
        facilitySite.setProgramSystemCode(psc1);
        facilityIdValidator.accept(facilitySite);
        assertEquals(1, violations.size());

        // alt id bad
        violations.clear();
        facilitySite.setEisProgramId(eis1);
        facilitySite.setAgencyFacilityIdentifier(alt2);
        facilitySite.setProgramSystemCode(psc1);
        facilityIdValidator.accept(facilitySite);
        assertEquals(1, violations.size());

        // none bad
        violations.clear();
        facilitySite.setEisProgramId(eis1);
        facilitySite.setAgencyFacilityIdentifier(alt1);
        facilitySite.setProgramSystemCode(psc1);
        facilityIdValidator.accept(facilitySite);
        assertTrue(violations.isEmpty());
    }
}
