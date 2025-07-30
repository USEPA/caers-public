/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
