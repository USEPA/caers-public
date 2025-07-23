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
package gov.epa.cef.web.api.rest;

import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.CersXmlService;
import gov.epa.cef.web.service.dto.EisSubmissionStatus;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/cers/")
public class CersApi {

    private final CersXmlService cersXmlService;

    private final SecurityService securityService;

    public static final String FACILITY_XML_INDICATOR = "facility";
    public static final String EMISSIONS_XML_INDICATOR = "emissions";

    @Autowired
    CersApi(SecurityService securityService,
            CersXmlService cersXmlService) {

        this.securityService = securityService;
        this.cersXmlService = cersXmlService;
    }

    /**
     * Retrieve XML report for an Emissions Report
     *
     * @param reportId
     * @return
     */
    @GetMapping(value = "/emissionsReport/{reportId}/xml/v2/{submissionType}", produces = MediaType.APPLICATION_XML_VALUE)
    @Operation(summary = "Retrieve Report V2 Xml",
        description = "Get report V2 XML.",
        tags = "CERS")
    public ResponseEntity<StreamingResponseBody> retrieveReportV2Xml(
            @NotNull @PathVariable("reportId") Long reportId,
            @NotNull @PathVariable("submissionType") String submissionType) {

        this.securityService.facilityEnforcer().enforceEntity(reportId, EmissionsReportRepository.class);

        EisSubmissionStatus eisSubmissionStatus;
        if (StringUtils.equals(EMISSIONS_XML_INDICATOR, submissionType)){
            eisSubmissionStatus = EisSubmissionStatus.QaEmissions;
        } else {
            eisSubmissionStatus = EisSubmissionStatus.QaFacility;
        }

        return new ResponseEntity<>(outputStream -> {
                this.cersXmlService.writeCersV2XmlTo(reportId, outputStream, eisSubmissionStatus);

        }, HttpStatus.OK);
    }
}
