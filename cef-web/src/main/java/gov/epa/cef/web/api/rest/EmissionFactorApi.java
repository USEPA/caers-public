/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.api.rest;

import gov.epa.cef.web.service.EmissionFactorService;
import gov.epa.cef.web.service.dto.EmissionFactorDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/emissionFactor")
public class EmissionFactorApi {

    @Autowired
    private EmissionFactorService efService;

    /**
     * Search for Emission Factors matching the provided criteria
     * @param dto
     * @return
     */
    @PostMapping
    @Operation(summary = "Get emission factors",
        description = "Get emission factors",
        tags = "Emission Factor")
    public ResponseEntity<List<EmissionFactorDto>> search(@NotNull @RequestBody EmissionFactorDto dto) {

        List<EmissionFactorDto> result = efService.retrieveByExample(dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Search for Emission Factors matching the provided criteria
     * @param dto
     * @return
     */
    @PostMapping(value = "/slt")
    @Operation(summary = "Get SLT emission factor",
        description = "Get SLT emission factor",
        tags = "Emission Factor")
    public ResponseEntity<List<EmissionFactorDto>> sltEfSearch(@NotNull @RequestBody EmissionFactorDto dto) {

        List<EmissionFactorDto> result = efService.retrieveSLTEfByExample(dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve the Emission Factor with the associated WebFIRE ID
     * @param webfireId
     * @return
     */
    @GetMapping(value = "/webfireId/{webfireId}")
    @Operation(summary = "Get emission factors by webfire id",
        description = "Get emission factors by webfire id",
        tags = "Emission Factor")
    public ResponseEntity<EmissionFactorDto> retrieveByWebfireId(@NotNull @PathVariable Long webfireId) {

        EmissionFactorDto result = efService.retrieveByWebfireId(webfireId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve the Emission Factor with the associated GHG ID
     * @param ghgId
     * @return
     */
    @GetMapping(value = "/ghgId/{ghgId}")
    @Operation(summary = "Get emission factors by GHG id",
        description = "Get emission factors by GHG id",
        tags = "Emission Factor")
    public ResponseEntity<EmissionFactorDto> retrieveByGhgId(@NotNull @PathVariable Long ghgId) {

        EmissionFactorDto result = efService.retrieveByGhgId(ghgId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
