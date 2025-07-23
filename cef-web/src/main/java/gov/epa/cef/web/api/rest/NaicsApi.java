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

import gov.epa.cef.web.domain.NaicsCodeIndustry;
import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.service.NaicsCodeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;


@RestController
@RequestMapping("/api/naics")
public class NaicsApi {

    @Autowired
    private NaicsCodeService naicsCodeService;

    /**
     * Retrieve unique NAICS code industries
     * @return
     */
    @RolesAllowed(value = {AppRole.ROLE_REVIEWER})
    @GetMapping(value = "/industries")
    @ResponseBody
    @Operation(summary = "Get distinct NAICS industries",
        description = "Get distinct NAICS industries",
        tags = "NAICS")
    public ResponseEntity<List<NaicsCodeIndustry>> findDistinctNaicsIndustries() {

    	List<NaicsCodeIndustry> result = naicsCodeService.getNaicsIndustries();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
