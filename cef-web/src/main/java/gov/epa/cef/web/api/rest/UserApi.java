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

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.epa.cef.web.service.UserService;
import gov.epa.cef.web.service.dto.TokenDto;
import gov.epa.cef.web.service.dto.UserDto;

@RestController
@RequestMapping("/api/user")
public class UserApi {


    @Autowired
    private UserService userService;

    /**
     * Retrieve the currently authenticated user
     * @return
     */
    @GetMapping(value = "/me")
    @ResponseBody
    @Operation(summary = "Get current user",
        description = "Get current user",
        tags = "User")
    public ResponseEntity<UserDto> retrieveCurrentUser() {
        UserDto result=userService.getCurrentUser();
        return new ResponseEntity<UserDto>(result, HttpStatus.OK);
    }


    /**
     * Retrieve a new NAAS token for the currently authenticated user
     * @return
     */
    @GetMapping(value = "/token")
    @ResponseBody
    @Operation(summary = "Get user create token",
        description = "Get user create token",
        tags = "User")
    public ResponseEntity<TokenDto> createToken() {
            TokenDto tokenDto=userService.createToken();
            return new ResponseEntity<TokenDto>(tokenDto, HttpStatus.OK);
        }

}
