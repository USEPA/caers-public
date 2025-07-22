/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
