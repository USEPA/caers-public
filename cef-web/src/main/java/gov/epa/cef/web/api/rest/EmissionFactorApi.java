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
