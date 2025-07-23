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

import javax.validation.constraints.NotNull;

import gov.epa.cef.web.service.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.epa.cef.web.service.LookupService;

@RestController
@RequestMapping("/api/lookup")
public class LookupApi {

    @Autowired
    private LookupService lookupService;

    /**
     * Retrieve Calculation Material codes
     * @return
     */
    @GetMapping(value = "/calculation/material")
    @ResponseBody
    @Operation(summary = "Get calc material codes",
        description = "Get calc material codes",
        tags = "Lookup")
    public ResponseEntity<List<CodeLookupDto>> retrieveCalcMaterialCodes() {

        List<CodeLookupDto> result = lookupService.retrieveCalcMaterialCodes();
        return new ResponseEntity<List<CodeLookupDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Calculation Material codes for fuel use and asphalt
     * @return
     */
    @GetMapping(value = "/fuelUse/material")
    @ResponseBody
    @Operation(summary = "Get fuel use material codes",
        description = "Get fuel use material codes",
        tags = "Lookup")
    public ResponseEntity<List<CalculationMaterialCodeDto>> retrieveFuelUseMaterialCodes() {

        List<CalculationMaterialCodeDto> result = lookupService.retrieveFuelUseMaterialCodes();
        return new ResponseEntity<List<CalculationMaterialCodeDto>>(result, HttpStatus.OK);
    }


    /**
     * Retrieve Calculation Method codes
     * @return
     */
    @GetMapping(value = "/calculation/method")
    @ResponseBody
    @Operation(summary = "Get calc method codes",
        description = "Get calc method codes",
        tags = "Lookup")
    public ResponseEntity<List<CalculationMethodCodeDto>> retrieveCalcMethodCodes() {

        List<CalculationMethodCodeDto> result = lookupService.retrieveCalcMethodCodes();
        return new ResponseEntity<List<CalculationMethodCodeDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Calculation Parameter Type codes
     * @return
     */
    @GetMapping(value = "/calculation/parameter")
    @ResponseBody
    @Operation(summary = "Get calc param type codes",
        description = "Get calc param type codes",
        tags = "Lookup")
    public ResponseEntity<List<CodeLookupDto>> retrieveCalcParamTypeCodes() {

        List<CodeLookupDto> result = lookupService.retrieveCalcParamTypeCodes();
        return new ResponseEntity<List<CodeLookupDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Operating Status codes for sub-facility components
     * @return
     */
    @GetMapping(value = "/subFacilityOperatingStatus")
    @ResponseBody
    @Operation(summary = "Get sub facility operating status codes",
        description = "Get sub facility operating status codes",
        tags = "Lookup")
    public ResponseEntity<List<CodeLookupDto>> retrieveSubFacilityOperatingStatusCodes() {

        List<CodeLookupDto> result = lookupService.retrieveSubFacilityOperatingStatusCodes();
        return new ResponseEntity<List<CodeLookupDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Operating Status codes for facilities
     * @return
     */
    @GetMapping(value = "/facilityOperatingStatus")
    @ResponseBody
    @Operation(summary = "Get facility operating status codes",
        description = "Get facility operating status codes",
        tags = "Lookup")
    public ResponseEntity<List<CodeLookupDto>> retrieveFacilityOperatingStatusCodes() {

        List<CodeLookupDto> result = lookupService.retrieveFacilityOperatingStatusCodes();
        return new ResponseEntity<List<CodeLookupDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Emissions Operating Type Codes
     * @return
     */
    @GetMapping(value = "/emissionsOperatingType")
    @ResponseBody
    @Operation(summary = "Get emission operating type codes",
        description = "Get emission operating type codes",
        tags = "Lookup")
    public ResponseEntity<List<CodeLookupDto>> retrieveEmissionOperatingTypeCodes() {

        List<CodeLookupDto> result = lookupService.retrieveEmissionOperatingTypeCodes();
        return new ResponseEntity<List<CodeLookupDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Pollutants
     * @return
     */
    @GetMapping(value = "/pollutant")
    @ResponseBody
    @Operation(summary = "Get pollutants",
        description = "Get pollutants",
        tags = "Lookup")
    public ResponseEntity<List<PollutantDto>> retrievePollutants() {

        List<PollutantDto> result = lookupService.retrievePollutants();
        return new ResponseEntity<List<PollutantDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Pollutants for a specific year
     * @return
     */
    @GetMapping(value = "/pollutant/{year}")
    @ResponseBody
    @Operation(summary = "Get current pollutants",
        description = "Get current pollutants",
        tags = "Lookup")
    public ResponseEntity<List<PollutantDto>> retrieveCurrentPollutants(@NotNull @PathVariable Integer year) {

        List<PollutantDto> result = lookupService.retrieveCurrentPollutants(year);
        return new ResponseEntity<List<PollutantDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Reporting Period codes
     * @return
     */
    @GetMapping(value = "/reportingPeriod")
    @ResponseBody
    @Operation(summary = "Get reporting period codes",
        description = "Get reporting period codes",
        tags = "Lookup")
    public ResponseEntity<List<CodeLookupDto>> retrieveReportingPeriodCodes() {

        List<CodeLookupDto> result = lookupService.retrieveReportingPeriodCodes();
        return new ResponseEntity<List<CodeLookupDto>>(result, HttpStatus.OK);
    }


    /**
     * Retrieve Contact Types codes
     * @return
     */
    @GetMapping(value = "/unitType")
    @ResponseBody
    @Operation(summary = "Get unit type codes",
        description = "Get unit type codes",
        tags = "Lookup")
    public ResponseEntity<List<CodeLookupDto>> retrieveUnitTypeCodes() {

        List<CodeLookupDto> result = lookupService.retrieveUnitTypeCodes();
        return new ResponseEntity<List<CodeLookupDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Fuel Use UoM codes
     * @return
     */
    @GetMapping(value = "/fuelUse/uom")
    @ResponseBody
    @Operation(summary = "Get fuel use unit measure codes",
        description = "Get fuel use unit codes",
        tags = "Lookup")
    public ResponseEntity<List<UnitMeasureCodeDto>> retrieveFuelUseUnitMeasureCodes() {

        List<UnitMeasureCodeDto> result = lookupService.retrieveFuelUseUnitMeasureCodes();
        return new ResponseEntity<List<UnitMeasureCodeDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve UoM codes
     * @return
     */
    @GetMapping(value = "/uom/{year}")
    @ResponseBody
    @Operation(summary = "Get unit measure codes",
        description = "Get unit measure codes",
        tags = "Lookup")
    public ResponseEntity<List<UnitMeasureCodeDto>> retrieveUnitMeasureCodes(@NotNull @PathVariable Integer year) {

        List<UnitMeasureCodeDto> result = lookupService.retrieveCurrentUnitMeasureCodes(year);
        return new ResponseEntity<List<UnitMeasureCodeDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Contact Types codes
     * @return
     */
    @GetMapping(value = "/contactType")
    @ResponseBody
    @Operation(summary = "Get contact type codes",
        description = "Get contact type codes",
        tags = "Lookup")
    public ResponseEntity<List<CodeLookupDto>> retrieveContactTypeCodes() {

        List<CodeLookupDto> result = lookupService.retrieveContactTypeCodes();
        return new ResponseEntity<List<CodeLookupDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Fips Counties
     * @return
     */
    @GetMapping(value = "/county")
    @ResponseBody
    @Operation(summary = "Get counties",
        description = "Get counties",
        tags = "Lookup")
    public ResponseEntity<List<FipsCountyDto>> retrieveCounties() {

        List<FipsCountyDto> result = lookupService.retrieveCountyCodes();
        return new ResponseEntity<List<FipsCountyDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Fips Counties valid for a specific year
     * @param year
     * @return
     */
    @GetMapping(value = "/county/{year}")
    @ResponseBody
    @Operation(summary = "Get current counties",
        description = "Get current counties",
        tags = "Lookup")
    public ResponseEntity<List<FipsCountyDto>> retrieveCurrentCounties(@NotNull @PathVariable Integer year) {

        List<FipsCountyDto> result = lookupService.retrieveCurrentCounties(year);
        return new ResponseEntity<List<FipsCountyDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Fips Counties by state
     * @param stateCode
     * @return
     */
    @GetMapping(value = "/county/state/{stateCode}")
    @ResponseBody
    @Operation(summary = "Get counties by state",
        description = "Get counties by state",
        tags = "Lookup")
    public ResponseEntity<List<FipsCountyDto>> retrieveCountiesForState(@PathVariable String stateCode) {

        List<FipsCountyDto> result = lookupService.retrieveCountyCodesByState(stateCode);
        return new ResponseEntity<List<FipsCountyDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Fips Counties valid for a specific year by state
     * @param stateCode
     * @return
     */
    @GetMapping(value = "/county/state/{stateCode}/{year}")
    @ResponseBody
    @Operation(summary = "Get current counties by state",
        description = "Get current counties by state",
        tags = "Lookup")
    public ResponseEntity<List<FipsCountyDto>> retrieveCurrentCountiesForState(@PathVariable String stateCode, @NotNull @PathVariable Integer year) {

        List<FipsCountyDto> result = lookupService.retrieveCurrentCountyCodesByState(stateCode, year);
        return new ResponseEntity<List<FipsCountyDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Fips State codes
     * @return
     */
    @GetMapping(value = "/stateCode")
    @ResponseBody
    @Operation(summary = "Get state codes",
        description = "Get state codes",
        tags = "Lookup")
    public ResponseEntity<List<FipsStateCodeDto>> retrieveStateCodes() {

        List<FipsStateCodeDto> result = lookupService.retrieveStateCodes();
        return new ResponseEntity<List<FipsStateCodeDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Release Point Type codes
     * @return
     */
    @GetMapping(value = "/releaseType")
    @ResponseBody
    @Operation(summary = "Get release point type codes",
        description = "Get release point type codes",
        tags = "Lookup")
    public ResponseEntity<List<CodeLookupDto>> retrieveReleasePointTypeCodes() {

        List<CodeLookupDto> result = lookupService.retrieveReleasePointTypeCodes();
        return new ResponseEntity<List<CodeLookupDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Program System Type codes
     * @return
     */
    @GetMapping(value = "/programSystemType")
    @ResponseBody
    @Operation(summary = "Get program system type codes",
        description = "Get program system type codes",
        tags = "Lookup")
    public ResponseEntity<List<CodeLookupDto>> retrieveProgramSystemTypeCodes() {

        List<CodeLookupDto> result = lookupService.retrieveProgramSystemTypeCodes();
        return new ResponseEntity<List<CodeLookupDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Program System code by description
     * @return
     */
    @GetMapping(value = "/programSystem/description/{description}")
    @ResponseBody
    @Operation(summary = "Get program system codes by description",
        description = "Get program system codes by description",
        tags = "Lookup")
    public ResponseEntity<CodeLookupDto> retrieveProgramSystemCodeByDescription(@NotNull @PathVariable String description) {

        CodeLookupDto result = lookupService.retrieveProgramSystemCodeByDescription(description);
        return new ResponseEntity<CodeLookupDto>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Control Measure codes
     * @return
     */
    @GetMapping(value = "/controlMeasure")
    @ResponseBody
    @Operation(summary = "Get control measure codes",
        description = "Get control measure codes",
        tags = "Lookup")
    public ResponseEntity<List<CodeLookupDto>> retrieveControlMeasureCodes() {

        List<CodeLookupDto> result = lookupService.retrieveControlMeasureCodes();
        return new ResponseEntity<List<CodeLookupDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Control Measure codes valid for a specific year
     * @param year
     * @return
     */
    @GetMapping(value = "/controlMeasure/{year}")
    @ResponseBody
    @Operation(summary = "Get current control measure codes",
        description = "Get current control measure codes",
        tags = "Lookup")
    public ResponseEntity<List<CodeLookupDto>> retrieveCurrentControlMeasureCodes(@NotNull @PathVariable Integer year) {

        List<CodeLookupDto> result = lookupService.retrieveCurrentControlMeasureCodes(year);
        return new ResponseEntity<List<CodeLookupDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Tribal Codes
     * @return
     */
    @GetMapping(value = "/tribalCode")
    @ResponseBody
    @Operation(summary = "Get tribal codes",
        description = "Get tribal codes",
        tags = "Lookup")
    public ResponseEntity<List<CodeLookupDto>> retrieveTribalCodes() {

        List<CodeLookupDto> result = lookupService.retrieveTribalCodes();
        return new ResponseEntity<List<CodeLookupDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Facility Permit Types
     * @return
     */
    @GetMapping(value = "/permitType")
    @ResponseBody
    @Operation(summary = "Get permit type codes",
        description = "Get permit type codes",
        tags = "Lookup")
    public ResponseEntity<List<FacilityPermitTypeDto>> retrievePermitTypes() {

        List<FacilityPermitTypeDto> result = lookupService.retrieveFacilityPermitTypes();
        return new ResponseEntity<List<FacilityPermitTypeDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Facility NAICS Codes
     * @return
     */
    @GetMapping(value = "/naicsCode")
    @ResponseBody
    @Operation(summary = "Get naics codes",
        description = "Get naics codes",
        tags = "Lookup")
    public ResponseEntity<List<CodeLookupDto>> retrieveNaicsCodes() {

        List<CodeLookupDto> result = lookupService.retrieveNaicsCodes();
        return new ResponseEntity<List<CodeLookupDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Facility NAICS codes valid for a specific year
     * @param year
     * @return
     */
    @GetMapping(value = "/naicsCode/{year}")
    @ResponseBody
    @Operation(summary = "Get naics codes by year",
        description = "Get naics code by year",
        tags = "Lookup")
    public ResponseEntity<List<CodeLookupDto>> retrieveNaicsCodesByYear(@NotNull @PathVariable Integer year) {

        List<CodeLookupDto> result = lookupService.retrieveNaicsCodesByYear(
            year.shortValue(), true, "code");
        return new ResponseEntity<List<CodeLookupDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Aircraft Engine Type codes
     * @return
     */
    @GetMapping(value = "/aircraftEngineCode/{scc}")
    @ResponseBody
    @Operation(summary = "Get aircraft engine codes",
        description = "Get aircraft engine codes",
        tags = "Lookup")
    public ResponseEntity<List<AircraftEngineTypeCodeDto>> retrieveAircraftEngineCodes(@NotNull @PathVariable String scc) {

        List<AircraftEngineTypeCodeDto> result = lookupService.retrieveAircraftEngineCodes(scc);
        return new ResponseEntity<List<AircraftEngineTypeCodeDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Aircraft Engine Type codes valid for a specific year
     * @param scc
     * @param year
     * @return
     */
    @GetMapping(value = "/aircraftEngineCode/{scc}/{year}")
    @ResponseBody
    @Operation(summary = "Get aircraft engine codes by year",
        description = "Get aircraft engine codes by year",
        tags = "Lookup")
    public ResponseEntity<List<AircraftEngineTypeCodeDto>> retrieveAircraftEngineCodes(@NotNull @PathVariable String scc, @NotNull @PathVariable Integer year) {

        List<AircraftEngineTypeCodeDto> result = lookupService.retrieveCurrentAircraftEngineCodes(scc, year);
        return new ResponseEntity<List<AircraftEngineTypeCodeDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Point Source SCC code
     * @param code
     * @return
     */
    @GetMapping(value = "/pointSourceSccCode/{code}")
    @ResponseBody
    @Operation(summary = "Get point source scc code",
        description = "Get point source scc code",
        tags = "Lookup")
    public ResponseEntity<PointSourceSccCodeDto> retrievePointSourceSccCode(@NotNull @PathVariable String code) {

    	PointSourceSccCodeDto result = lookupService.retrievePointSourceSccCode(code);
    	return new ResponseEntity<PointSourceSccCodeDto>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Facility Category codes
     * @return
     */
    @GetMapping(value = "/facility/category")
    @ResponseBody
    @Operation(summary = "Get facility category codes",
        description = "Get facility category codes",
        tags = "Lookup")
    public ResponseEntity<List<FacilityCategoryCodeDto>> retrieveFacilityCategoryCodes() {

        List<FacilityCategoryCodeDto> result = lookupService.retrieveFacilityCategoryCodes();
        return new ResponseEntity<List<FacilityCategoryCodeDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Facility Source Type codes
     * @return
     */
    @GetMapping(value = "/facility/sourceType")
    @ResponseBody
    @Operation(summary = "Get facility source type codes",
        description = "Get facility source type codes",
        tags = "Lookup")
    public ResponseEntity<List<CodeLookupDto>> retrieveFacilitySourceTypeCodes() {

        List<CodeLookupDto> result = lookupService.retrieveFacilitySourceTypeCodes();
        return new ResponseEntity<List<CodeLookupDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Facility Source Type codes
     * @return
     */
    @GetMapping(value = "/facility/sourceType/{year}")
    @ResponseBody
    @Operation(summary = "Get current facility source type codes",
        description = "Get current facility source type codes",
        tags = "Lookup")
    public ResponseEntity<List<CodeLookupDto>> retrieveCurrentFacilitySourceTypeCodes(@NotNull @PathVariable Integer year) {

        List<CodeLookupDto> result = lookupService.retrieveCurrentFacilitySourceTypeCodes(year);
        return new ResponseEntity<List<CodeLookupDto>>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Release Point Type codes valid for a specific year
     * @param year
     * @return
     */
    @GetMapping(value = "/releasePointType/{year}")
    @ResponseBody
    @Operation(summary = "Get current release point type codes",
        description = "Get current release point codes",
        tags = "Lookup")
    public ResponseEntity<List<CodeLookupDto>> retrieveCurrentReleasePointTypeCodes(@NotNull @PathVariable Integer year) {

        List<CodeLookupDto> result = lookupService.retrieveCurrentReleasePointTypeCodes(year);
        return new ResponseEntity<List<CodeLookupDto>>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/scc/{slt}/{searchTerm}")
    @ResponseBody
    @Operation(summary = "Get scc codes",
        description = "Get scc codes",
        tags = "Lookup")
    public ResponseEntity<List<PointSourceSccCodeDto>> retrieveSearchSccCodes(@NotNull @PathVariable String slt,
                                                                              @NotNull @PathVariable String searchTerm) {

    	List<PointSourceSccCodeDto> result = lookupService.searchSccCodes(searchTerm, slt);
        return new ResponseEntity<List<PointSourceSccCodeDto>>(result, HttpStatus.OK);
    }
}
