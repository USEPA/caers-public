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

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.service.UserFeedbackService;
import gov.epa.cef.web.service.dto.UserFeedbackDto;
import gov.epa.cef.web.service.dto.IUserFeedbackStatsDto;


@RestController
@RequestMapping("/api/userFeedback")
public class UserFeedbackApi {


    @Autowired
    private UserFeedbackService userFeedbackService;

    /**
     * Create a feedback submission
     * @param dto
     * @return
     */
    @PostMapping
    @Operation(summary = "Get user feedback",
        description = "Get user feedback",
        tags = "User Feedback")
    public ResponseEntity<UserFeedbackDto> createUserFeedbackSubmission(@NotNull @RequestBody UserFeedbackDto dto) {

    	UserFeedbackDto result = userFeedbackService.create(dto);

    	return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve all feedback for selected year and program system code
     * @return
     */
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN})
    @GetMapping(value = "/byYearAndProgramSystemCode")
    @ResponseBody
    @Operation(summary = "Get user feedback by year and program system",
        description = "Get user feedback by year and program system",
        tags = "User Feedback")
    public ResponseEntity<List<UserFeedbackDto>> retrieveAllByYearAndProgramSystem(@NotNull @RequestParam(value = "year") Short year,
            @NotNull @RequestParam(value = "programSystemCode") String programSystemCode) {

    	List<UserFeedbackDto> result = userFeedbackService.retrieveAllByYearAndProgramSystem(year, programSystemCode);
        return new ResponseEntity<List<UserFeedbackDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve stats for selected year and program system code
     * @return
     */
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN})
    @GetMapping(value = "/stats/byYearAndProgramSystemCode")
    @ResponseBody
    @Operation(summary = "Get user feedback stats by year and program system",
        description = "Get user feedback stats by year and program system",
        tags = "User Feedback")
    public ResponseEntity<IUserFeedbackStatsDto> retrieveStatsByYearAndProgramSystem(@NotNull @RequestParam(value = "year") Short year,
            @NotNull @RequestParam(value = "programSystemCode") String programSystemCode) {

    	IUserFeedbackStatsDto result = userFeedbackService.retrieveStatsByYearAndProgramSystem(year, programSystemCode);
        return new ResponseEntity<IUserFeedbackStatsDto>(result, HttpStatus.OK);
    }

    /**
     * Retrieve available year and program system codes
     * @return
     */
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN})
    @GetMapping(value = "/years")
    @ResponseBody
    @Operation(summary = "Get user feedback available years",
        description = "Get user feedback available years",
        tags = "User Feedback")
    public ResponseEntity<List<Short>> retrieveAvailableYears() {

    	List<Short> result =userFeedbackService.retrieveAvailableYears();
        return new ResponseEntity<List<Short>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve available year and program system codes
     * @return
     */
    @RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN})
    @GetMapping(value = "/programSystemCodes")
    @ResponseBody
    @Operation(summary = "Get user feedback available program system",
        description = "Get user feedback available program system",
        tags = "User Feedback")
    public ResponseEntity<List<String>> retrieveAvailableProgramSystems() {

    	List<String> result =userFeedbackService.retrieveAvailableProgramSystems();
        return new ResponseEntity<List<String>>(result, HttpStatus.OK);
    }

}
