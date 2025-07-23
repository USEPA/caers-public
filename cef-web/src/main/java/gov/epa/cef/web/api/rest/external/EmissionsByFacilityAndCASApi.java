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
package gov.epa.cef.web.api.rest.external;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.epa.cef.web.service.EmissionService;
import gov.epa.cef.web.service.dto.EmissionsByFacilityAndCASDto;

@RestController
@RequestMapping("/api/public/emissionsByFacilityAndCAS")
public class EmissionsByFacilityAndCASApi {

    @Autowired
    private EmissionService emissionService;

    /**
     * Retrieve the total point and non-point emissions for a given TRI Facility ID and pollutant
     * @param trifid
     * @param casNumber
     * @return
     */
    @GetMapping(value = "/v2/{trifid}/{casNumber}")
    @Operation(summary = "Retrieve Emissions by Trifid",
        description = "Retrieve Emissions by Trifid.",
        tags = "Emissions by Facility")
    @ResponseBody
    public ResponseEntity<EmissionsByFacilityAndCASDto> retrieveEmissionsByTrifid(@PathVariable String trifid, @PathVariable String casNumber) {
        EmissionsByFacilityAndCASDto emissions = emissionService.findEmissionsByTrifidAndCAS(trifid, casNumber);
        return new ResponseEntity<EmissionsByFacilityAndCASDto>(emissions, HttpStatus.OK);
    }

}
