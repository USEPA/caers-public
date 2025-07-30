/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
