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
package gov.epa.cef.web.service;

import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.service.dto.*;

import java.util.List;

public interface LookupService {

    /**
     * Retrieve Calculation Material codes
     * @return
     */
    List<CodeLookupDto> retrieveCalcMaterialCodes();

    /**
     * Retrieve Fuel Use and Asphalt Calculation Material codes
     * @return
     */
    List<CalculationMaterialCodeDto> retrieveFuelUseMaterialCodes();

    /**
     * Retrieves Fuel Use Material Code entity by code
     * @param code
     * @return CalculationMaterialCode
     * */
    CalculationMaterialCode retrieveFuelUseMaterialCodeEntityByCode(String code);

    /**
     * Retrieve Calculation Material code database object by code
     * @param code
     * @return
     */
    CalculationMaterialCode retrieveCalcMaterialCodeEntityByCode(String code);

    /**
     * Retrieve Calculation Method codes
     * @return
     */
    List<CalculationMethodCodeDto> retrieveCalcMethodCodes();

    /**
     * Retrieve Calculation Method Code entity by code
     * @param code
     * @return CalculationMethodCode
     */
    CalculationMethodCode retrieveCalcMethodCodeEntityByCode(String code);

    /**
     * Retrieve Calculation Parameter Type codes
     * @return
     */
    List<CodeLookupDto> retrieveCalcParamTypeCodes();

    /**
     * Retrieve Calculation Parameter Type code database object by code
     * @param code
     * @return
     */
    CalculationParameterTypeCode retrieveCalcParamTypeCodeEntityByCode(String code);

    /**
     * Retrieve Operating Status codes for sub-facility components
     * @return
     */
    List<CodeLookupDto> retrieveSubFacilityOperatingStatusCodes();

    /**
     * Retrieve OperatingStatusCode entities for sub-facility components by code
     * @param code;
     * @return OperatingStatusCode
     */
    OperatingStatusCode retrieveSubFacilityOperatingStatusCodeEntityByCode(String code);

    /**
     * Retrieve Operating Status codes for facilities
     * @return
     */
    List<CodeLookupDto> retrieveFacilityOperatingStatusCodes();

    /**
     * Retrieve Pollutants
     * @return
     */
    List<PollutantDto> retrievePollutants();

    /**
     * Retrieve non-legacy Pollutants
     * @param year
     * @return
     */
    List<PollutantDto> retrieveCurrentPollutants(Integer year);

    /**
     * Retrieve Pollutant by Pollutant Code
     * @param pollutantCode
     * @return Pollutant
     */
    Pollutant retrievePollutantByPollutantCode(String pollutantCode);

    /**
     * Retrieve Operating Status code database object by code
     * @param code
     * @return
     */
    OperatingStatusCode retrieveOperatingStatusCodeEntityByCode(String code);

    /**
     * Retrieve Reporting Period codes
     * @return
     */
    List<CodeLookupDto> retrieveReportingPeriodCodes();

    /**
     * Retrieve Reporting Period code database object by code
     * @param code
     * @return
     */
    ReportingPeriodCode retrieveReportingPeriodCodeEntityByCode(String code);

    /**
     * Retrieve UoM codes
     * @return
     */
    List<UnitMeasureCodeDto> retrieveUnitMeasureCodes();

    /**
     * Retrieve non-legacy UoM codes
     * @return
     */
    List<UnitMeasureCodeDto> retrieveCurrentUnitMeasureCodes(Integer year);

    /**
     * Retrieve Fuel Use UoM codes
     * @return
     */
    List<UnitMeasureCodeDto> retrieveFuelUseUnitMeasureCodes();

    /**
     * Retrieve UoM code database object by code
     * @param code
     * @return
     */
    UnitMeasureCode retrieveUnitMeasureCodeEntityByCode(String code);

    /**
     * Retrieve the list of Emission Operating Type Codes
     * @return
     */
    List<CodeLookupDto> retrieveEmissionOperatingTypeCodes();

    /**
     * Retrieve the Emissions Operating Type Code entity by code
     *
     * @param code
     * @return
     */
    EmissionsOperatingTypeCode retrieveEmissionsOperatingTypeCodeEntityByCode(String code);

    /**
     * Retrieve Contact Type codes
     * @return
     */
    List<CodeLookupDto> retrieveContactTypeCodes();

    /**
     * Retrieve Contact Type code database object by code
     * @param code
     * @return
     */
    ContactTypeCode retrieveContactTypeEntityByCode(String code);
    /**
     * Retrieve Unit Type codes
     * @return
     */
    List<CodeLookupDto> retrieveUnitTypeCodes();

    /**
     * Retrieve UnitTypeCode entity by typeCode
     * @param typeCode
     * @return UnitTypeCode
     * */
    UnitTypeCode retrieveUnitTypeCodeEntityByCode(String typeCode);

    /**
     * Retrieve County codes
     * @return
     */
    List<FipsCountyDto> retrieveCountyCodes();

    /**
     * Retrieve non-legacy County codes
     * @param year
     * @return
     */
    List<FipsCountyDto> retrieveCurrentCounties(Integer year);

    /**
     * Retrieve County codes for a state
     * @param stateCode
     * @return
     */
    List<FipsCountyDto> retrieveCountyCodesByState(String stateCode);

    /**
     * Retrieve non-legacy County codes for a state
     * @param stateCode
     * @param year
     * @return
     */
    List<FipsCountyDto> retrieveCurrentCountyCodesByState(String stateCode, Integer year);


    /**
     * Retrieve FipsCounty entity by code
     * @param stateCode
     * @param countyCode
     * @return
     */
    FipsCounty retrieveCountyEntityByStateAndCountyCode(String stateCode, String countyCode);

    /**
     * Retrieve FipsCounty entity by countyCode
     * @param countyCode
     * @return
     */
    FipsCounty retrieveCountyEntityByCountyCode(String countyCode);

    /**
     * Retrieve Fips State codes
     * @return
     */
    List<FipsStateCodeDto> retrieveStateCodes();

    /**
     * Retrieve Fips State code database object by code
     * @param code
     * @return
     */
    FipsStateCode retrieveStateCodeEntityByCode(String code);

    /**
     * Retrieve Fips State code database object by uspsCode
     * @param uspsCode
     * @return
     */
    FipsStateCode retrieveStateCodeEntityByUspsCode(String uspsCode);

    /**
     * Retrieve the list of Release Point Type Codes
     * @return
     */
    List<CodeLookupDto> retrieveReleasePointTypeCodes();

    /**
     * Retrieve Release Point Type code database object by code
     * @param code
     * @return
     */
    ReleasePointTypeCode retrieveReleasePointTypeCodeEntityByCode(String code);

    /**
     * Retrieve non-legacy Release Point Type codes
     * @param year
     * @return
     */
    List<CodeLookupDto> retrieveCurrentReleasePointTypeCodes(Integer year);

    /**
     * Retrieve the list of Program System Type Codes
     * @return
     */
    List<CodeLookupDto> retrieveProgramSystemTypeCodes();

    /**
     * Retrieve Program System code by description
     * @param description
     * @return
     */
    CodeLookupDto retrieveProgramSystemCodeByDescription(String description);

    /**
     * Retrieve Program System Type code database object by code
     * @param code
     * @return
     */
    ProgramSystemCode retrieveProgramSystemTypeCodeEntityByCode(String code);

    /**
     * Retrieve Control Measure codes
     * @return
     */
    List<CodeLookupDto> retrieveControlMeasureCodes();

    /**
     * Retrieve non-legacy Control Measure codes
     * @param year
     * @return
     */
    List<CodeLookupDto> retrieveCurrentControlMeasureCodes(Integer year);

    /**
    * Retrieve Control Measure code database object by code
    * @param code
    * @return
    */
    ControlMeasureCode retrieveControlMeasureCodeEntityByCode(String code);

    /**
     * Retrieve Tribal Codes
     * @return
     */
    List<CodeLookupDto> retrieveTribalCodes();

    /**
     * Retrieve Tribal code database object by code
     * @param code
     * @return
     */
    TribalCode retrieveTribalCodeEntityByCode(String code);

    /**
     * Retrieve NAICS Codes
     * @return List of CodeLookupDtos
     */
    List<CodeLookupDto> retrieveNaicsCodes();

    /**
     * Retrieve NAICS code entity by code
     * @param code
     * @return
     */
    NaicsCode retrieveNAICSCodeEntityByCode(Integer code);

    /**
     * Retrieve NAICS codes by year and boolean to limit results to six-digit codes, sort results by sortingField
     * @param year
     * @param onlySixDigits
     * @param sortingField
     * @return
     */
    List<CodeLookupDto> retrieveNaicsCodesByYear(Short year, boolean onlySixDigits, String sortingField);

    List<FacilityPermitTypeDto> retrieveFacilityPermitTypes();

    /**
     * Retrieve Aircraft Engine Type Codes
     * @return
     */
    List<AircraftEngineTypeCodeDto> retrieveAircraftEngineCodes(String scc);

    /**
     * Retrieve AircraftEngineTypeCode entity by code
     * @param code
     * @return AircraftEngineTypeCode
     * */
    AircraftEngineTypeCode retrieveAircraftEngineCodeEntityByCode(String code);

    /**
     * Retrieve non-legacy Aircraft Engine Type Codes
     * @param scc
     * @param year
     * @return
     */
    List<AircraftEngineTypeCodeDto> retrieveCurrentAircraftEngineCodes(String scc, Integer year);

    /**
    * Retrieve Point Source SCC code database object by code
    * @param code
    * @return
    */
    PointSourceSccCodeDto retrievePointSourceSccCode(String code);

    /**
     * Retrieve Facility Category codes
     * @return
     */
    List<FacilityCategoryCodeDto> retrieveFacilityCategoryCodes();

    /**
     * Retrieve Facility Category Code entity by code
     * @param code
     * @return FacilityCategoryCode
     */
    FacilityCategoryCode retrieveFacilityCategoryCodeEntityByCode(String code);

    /**
     * Retrieve Facility Source Type codes
     * @return
     */
    List<CodeLookupDto> retrieveFacilitySourceTypeCodes();

    /**
     * Retrieve non-legacy Facility Source Type codes
     * @param year
     * @return
     */
    List<CodeLookupDto> retrieveCurrentFacilitySourceTypeCodes(Integer year);


    /**
     * Retrieve scc codes by search term. SLT (or program system code) is provided
     * for checking what SCCs are allowed.
     * @param searchTerm
     * @param slt
     * @return
     */
    List<PointSourceSccCodeDto> searchSccCodes(String searchTerm, String slt);

    /**
     * Retrieve reporting period codes for all 12 months
     * @return
     */
    List<ReportingPeriodCode> findReportingPeriodMonthAndSemiAnnualCodes();

    /**
     * Retrieve NAICS code description per given report year
     * @param code
     * @param year
     * @return
     */
    String findNaicsCodeDescriptionByYear(Integer code, Short year);

    /**
     * Retrieve Emission Formula Variable Code entity by code
     * @param code
     * @return EmissionFormulaVariableCode
     */
    EmissionFormulaVariableCode retrieveFormulaVariableCodeEntityByCode(String code);

    /**
     * Check if a pollutant was reported in the previous year's report
     * @param pollutantCode
     * @param masterFacilityId
     * @return boolean
     */
    boolean wasPollutantReportedLastYear(String pollutantCode, Long masterFacilityId);

}
