/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import gov.epa.cef.web.config.SLTBaseConfig;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.*;
import gov.epa.cef.web.service.dto.*;
import gov.epa.cef.web.util.SLTConfigHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import gov.epa.cef.web.service.LookupService;
import gov.epa.cef.web.service.mapper.LookupEntityMapper;

@Service
public class LookupServiceImpl implements LookupService {

    private static final String DESCRIPTION = "description";

    @Autowired
    private CalculationMaterialCodeRepository materialCodeRepo;

    @Autowired
    private CalculationMethodCodeRepository methodCodeRepo;

    @Autowired
    private CalculationParameterTypeCodeRepository paramTypeCodeRepo;

    @Autowired
    private OperatingStatusCodeRepository operatingStatusRepo;

    @Autowired
    private EmissionsOperatingTypeCodeRepository emissionsOperatingTypeCodeRepo;

    @Autowired
    private PollutantRepository pollutantRepo;

    @Autowired
    private UnitTypeCodeRepository unitTypeCodeRepo;

    @Autowired
    private ReportingPeriodCodeRepository periodCodeRepo;

    @Autowired
    private UnitMeasureCodeRepository uomRepo;

    @Autowired
    private ContactTypeCodeRepository contactTypeRepo;

    @Autowired
    private FipsCountyRepository countyRepo;

    @Autowired
    private FipsStateCodeRepository stateCodeRepo;

    @Autowired
    private ReleasePointTypeCodeRepository releasePtTypeRepository;

    @Autowired
    private ProgramSystemCodeRepository programSystemCodeRepo;

    @Autowired
    private ControlMeasureCodeRepository controlMeasureCodeRepo;

    @Autowired
    private TribalCodeRepository tribalCodeRepo;

    @Autowired
    private NaicsCodeRepository naicsCodeRepo;

    @Autowired
    private AircraftEngineTypeCodeRepository aircraftEngCodeRepo;

    @Autowired
    private PointSourceSccCodeRepository pointSourceSccCodeRepo;

    @Autowired
    private FacilityCategoryCodeRepository facilityCategoryCodeRepo;

    @Autowired
    private FacilitySourceTypeCodeRepository facilitySourceTypeCodeRepo;

    @Autowired
    private NaicsDescriptionHistoryRepository naicsDescriptionRepo;

    @Autowired
    private FacilityPermitTypeRepository permitTypeRepository;


    // TODO: switch to using LookupRepositories, not currently done due to tests

    @Autowired
    private LookupEntityMapper lookupMapper;

    @Autowired
    private EmissionFormulaVariableCodeRepository formulaVariableCodeRepository;

    @Autowired
    private SLTConfigHelper sltConfigHelper;


    /* (non-Javadoc)
     * @see gov.epa.cef.web.service.impl.LookupService#retrieveCalcMaterialCodes()
     */
    @Override
    public List<CodeLookupDto> retrieveCalcMaterialCodes() {

        List<CodeLookupDto> result = new ArrayList<CodeLookupDto>();
        Iterable<CalculationMaterialCode> entities = materialCodeRepo.findAll(Sort.by(Direction.ASC, DESCRIPTION));

        entities.forEach(entity -> {
            result.add(lookupMapper.toDto(entity));
        });
        return result;
    }

    /* (non-Javadoc)
     * @see gov.epa.cef.web.service.impl.LookupService#retrieveFuelUseMaterialCodes()
     */
    public List<CalculationMaterialCodeDto> retrieveFuelUseMaterialCodes() {

        List<CalculationMaterialCode> entities = materialCodeRepo.findAllFuelUseMaterial(Sort.by(Direction.ASC, DESCRIPTION));

        List<CalculationMaterialCodeDto> result = lookupMapper.fuelUseCalculationMaterialToDtoList(entities);

        return result;
    }

    /**
     * Retrieves Fuel Use Material Code entity by code
     * @param code
     * @return CalculationMaterialCode
     * */
    @Override
    public CalculationMaterialCode retrieveFuelUseMaterialCodeEntityByCode(String code) {
        CalculationMaterialCode result= materialCodeRepo
            .findById(code)
            .orElse(null);
        return result;
    }

    public CalculationMaterialCode retrieveCalcMaterialCodeEntityByCode(String code) {
        CalculationMaterialCode result= materialCodeRepo
            .findById(code)
            .orElse(null);
        return result;
    }

    public List<CalculationMethodCodeDto> retrieveCalcMethodCodes() {

        List<CalculationMethodCodeDto> result = new ArrayList<CalculationMethodCodeDto>();
        Iterable<CalculationMethodCode> entities = methodCodeRepo.findAll(Sort.by(Direction.ASC, DESCRIPTION));

        entities.forEach(entity -> {
            result.add(lookupMapper.calculationMethodCodeToDto(entity));
        });
        return result;
    }

    /**
     * Retrieve Calculation Method Code entity by code
     * @param code
     * @return CalculationMethodCode
     */
    @Override
    public CalculationMethodCode retrieveCalcMethodCodeEntityByCode(String code) {
        CalculationMethodCode result = methodCodeRepo
            .findById(code)
            .orElse(null);
        return result;
    }

    /* (non-Javadoc)
     * @see gov.epa.cef.web.service.impl.LookupService#retrieveCalcParamTypeCodes()
     */
    @Override
    public List<CodeLookupDto> retrieveCalcParamTypeCodes() {

        List<CodeLookupDto> result = new ArrayList<CodeLookupDto>();
        Iterable<CalculationParameterTypeCode> entities = paramTypeCodeRepo.findAll(Sort.by(Direction.ASC, DESCRIPTION));

        entities.forEach(entity -> {
            result.add(lookupMapper.toDto(entity));
        });
        return result;
    }

    public CalculationParameterTypeCode retrieveCalcParamTypeCodeEntityByCode(String code) {
        CalculationParameterTypeCode result= paramTypeCodeRepo
            .findById(code)
            .orElse(null);
        return result;
    }

    /* (non-Javadoc)
     * @see gov.epa.cef.web.service.impl.LookupService#retrieveSubFacilityOperatingStatusCodes()
     */
    public List<CodeLookupDto> retrieveSubFacilityOperatingStatusCodes() {

        List<CodeLookupDto> result = new ArrayList<CodeLookupDto>();
        Iterable<OperatingStatusCode> entities = operatingStatusRepo.findAllSubFacilityStatuses(Sort.by(Direction.ASC, DESCRIPTION));

        entities.forEach(entity -> {
            result.add(lookupMapper.toDto(entity));
        });
        return result;
    }

    /**
     * Retrieve Operating Status Code for sub-facility components by code
     * @param code;
     * @return OperatingStatusCode
     */
    @Override
    public OperatingStatusCode retrieveSubFacilityOperatingStatusCodeEntityByCode(String code) {
        OperatingStatusCode result = operatingStatusRepo
            .findSubFacilityStatusEntityByCode(code)
            .orElse(null);
        return result;
    }

    /* (non-Javadoc)
     * @see gov.epa.cef.web.service.impl.LookupService#retrieveFacilityOperatingStatusCodes()
     */
    public List<CodeLookupDto> retrieveFacilityOperatingStatusCodes() {

        List<CodeLookupDto> result = new ArrayList<CodeLookupDto>();
        Iterable<OperatingStatusCode> entities = operatingStatusRepo.findAllFacilityStatuses(Sort.by(Direction.ASC, DESCRIPTION));

        entities.forEach(entity -> {
            result.add(lookupMapper.toDto(entity));
        });
        return result;
    }

    public OperatingStatusCode retrieveOperatingStatusCodeEntityByCode(String code) {
        OperatingStatusCode result= operatingStatusRepo
            .findById(code)
            .orElse(null);
        return result;
    }

    @Override
    public List<CodeLookupDto> retrieveEmissionOperatingTypeCodes() {

        List<CodeLookupDto> result = new ArrayList<CodeLookupDto>();
        Iterable<EmissionsOperatingTypeCode> entities = emissionsOperatingTypeCodeRepo.findAll(Sort.by(Direction.ASC, "shortName"));

        entities.forEach(entity -> {
            result.add(lookupMapper.emissionsOperatingTypeCodeToDto(entity));
        });
        return result;
    }

    public EmissionsOperatingTypeCode retrieveEmissionsOperatingTypeCodeEntityByCode(String code) {

        EmissionsOperatingTypeCode result= emissionsOperatingTypeCodeRepo
                .findById(code)
                .orElse(null);
        return result;
    }

    @Override
    public List<PollutantDto> retrievePollutants() {

        List<PollutantDto> result = new ArrayList<PollutantDto>();
        Iterable<Pollutant> entities = pollutantRepo.findAll();

        entities.forEach(entity -> {
            result.add(lookupMapper.pollutantToDto(entity));
        });
        return result;
    }

    /**
     * Retrieve Pollutant by Pollutant Code
     * @param pollutantCode
     * @return Pollutant
     */
    @Override
    public Pollutant retrievePollutantByPollutantCode(String pollutantCode) {
        Pollutant result = pollutantRepo
            .findById(pollutantCode)
            .orElse(null);
        return result;
    }

    /*
     * Retrieve non-legacy Pollutants
     * @return
     */
    @Override
    public List<PollutantDto> retrieveCurrentPollutants(Integer year) {

        List<Pollutant> entities = pollutantRepo.findAllCurrent(year, Sort.by(Direction.ASC, "pollutantName"));

        List<PollutantDto> result = lookupMapper.pollutantToDtoList(entities);
        return result;
    }

    /* (non-Javadoc)
     * @see gov.epa.cef.web.service.impl.LookupService#retrieveReportingPeriodCodes()
     */
    @Override
    public List<CodeLookupDto> retrieveReportingPeriodCodes() {

        List<CodeLookupDto> result = new ArrayList<CodeLookupDto>();
        Iterable<ReportingPeriodCode> entities = periodCodeRepo.findAll(Sort.by(Direction.ASC, "shortName"));

        entities.forEach(entity -> {
            result.add(lookupMapper.reportingPeriodCodeToDto(entity));
        });
        return result;
    }

    public ReportingPeriodCode retrieveReportingPeriodCodeEntityByCode(String code) {
        ReportingPeriodCode result= periodCodeRepo
            .findById(code)
            .orElse(null);
        return result;
    }

    /* (non-Javadoc)
     * @see gov.epa.cef.web.service.impl.LookupService#retrieveUnitMeasureCodes()
     */
    @Override
    public List<UnitMeasureCodeDto> retrieveUnitMeasureCodes() {

        List<UnitMeasureCodeDto> result = new ArrayList<UnitMeasureCodeDto>();
        Iterable<UnitMeasureCode> entities = uomRepo.findAll(Sort.by(Direction.ASC, "code"));

        entities.forEach(entity -> {
            result.add(lookupMapper.unitMeasureCodeToDto(entity));
        });
        return result;
    }

    /*
     * Retrieve non-legacy UoM codes
     * @return
     */
    @Override
    public List<UnitMeasureCodeDto> retrieveCurrentUnitMeasureCodes(Integer year) {

        List<UnitMeasureCode> entities = uomRepo.findAllCurrent(Sort.by(Direction.ASC, "code"), year);

        List<UnitMeasureCodeDto> result = lookupMapper.unitMeasureCodeToDtoList(entities);
        return result;
    }

    /*
     * Retrieve Fuel Use UoM codes
     * @return
     */
    @Override
    public List<UnitMeasureCodeDto> retrieveFuelUseUnitMeasureCodes() {

        List<UnitMeasureCode> entities = uomRepo.findAllFuelUseUom(Sort.by(Direction.ASC, "code"));

        List<UnitMeasureCodeDto> result = lookupMapper.unitMeasureCodeToDtoList(entities);
        return result;
    }

    public UnitMeasureCode retrieveUnitMeasureCodeEntityByCode(String code) {
        UnitMeasureCode result= uomRepo
            .findById(code)
            .orElse(null);
        return result;
    }

    public List<CodeLookupDto> retrieveContactTypeCodes() {

        List<CodeLookupDto> result = new ArrayList<CodeLookupDto>();
        Iterable<ContactTypeCode> entities = contactTypeRepo.findAll(Sort.by(Direction.ASC, "code"));

        entities.forEach(entity -> {
            result.add(lookupMapper.toDto(entity));
        });
        return result;
    }

    public List<CodeLookupDto> retrieveUnitTypeCodes() {

        List<CodeLookupDto> result = new ArrayList<CodeLookupDto>();
        Iterable<UnitTypeCode> entities = unitTypeCodeRepo.findAll(Sort.by(Direction.ASC, DESCRIPTION));

        entities.forEach(entity -> {
            result.add(lookupMapper.toDto(entity));
        });
        return result;
    }

    /**
     * Retrieve UnitTypeCode entity by typeCode
     * @param typeCode
     * @return UnitTypeCode
     * */
    @Override
    public UnitTypeCode retrieveUnitTypeCodeEntityByCode(String typeCode) {
        UnitTypeCode result = unitTypeCodeRepo
            .findById(typeCode)
            .orElse(null);
        return result;
    }

    public ContactTypeCode retrieveContactTypeEntityByCode(String code) {
    	ContactTypeCode result= contactTypeRepo
            .findById(code)
            .orElse(null);
        return result;
    }

    public List<FipsCountyDto> retrieveCountyCodes() {

        List<FipsCounty> entities = countyRepo.findAll(Sort.by(Direction.ASC, "code"));

        return lookupMapper.fipsCountyToDtoList(entities);
    }

    /*
     * Retrieve non-legacy county codes
     * @return
     */
    public List<FipsCountyDto> retrieveCurrentCounties(Integer year) {

        List<FipsCounty> entities = countyRepo.findAllCurrent(year, Sort.by(Direction.ASC, "code"));

        return lookupMapper.fipsCountyToDtoList(entities);
    }

    public List<FipsCountyDto> retrieveCountyCodesByState(String stateCode) {

        List<FipsCounty> entities = countyRepo.findByFipsStateCodeCode(stateCode, Sort.by(Direction.ASC, "code"));

        return lookupMapper.fipsCountyToDtoList(entities);
    }

    /**
     * Retrieve non-legacy county codes by State
     * @param stateCode
     * @param year
     * @return
     */
    public List<FipsCountyDto> retrieveCurrentCountyCodesByState(String stateCode, Integer year) {

        List<FipsCounty> entities = countyRepo.findCurrentByFipsStateCodeCode(stateCode, year, Sort.by(Direction.ASC, "code"));

        return lookupMapper.fipsCountyToDtoList(entities);
    }

    /**
     * Retrieve FipsCounty entity by code
     * @param stateCode
     * @param countyCode
     * @return
     */
    @Override
    public FipsCounty retrieveCountyEntityByStateAndCountyCode(String stateCode, String countyCode) {
        FipsCounty result= countyRepo
            .findByFipsStateCodeCodeAndCountyCode(stateCode, countyCode)
            .orElse(null);
        return result;
    }

    /**
     * Retrieve County code database object by code
     * @param countyCode
     * @return
     */
    @Override
    public FipsCounty retrieveCountyEntityByCountyCode(String countyCode) {
        FipsCounty result = countyRepo
            .findFirstByCountyCode(countyCode)
            .orElse(null);
        return result;
    }

    @Override
    public List<FipsStateCodeDto> retrieveStateCodes() {

        List<FipsStateCodeDto> result = new ArrayList<FipsStateCodeDto>();
        Iterable<FipsStateCode> entities = stateCodeRepo.findAll(Sort.by(Direction.ASC, "code"));

        entities.forEach(entity -> {
            result.add(lookupMapper.fipsStateCodeToDto(entity));
        });
        return result;
    }

    /**
     * Retrieve FipsSateCode entity by code
     * @param code
     * @return
     */
    @Override
    public FipsStateCode retrieveStateCodeEntityByCode(String code) {
    	FipsStateCode result= stateCodeRepo
            .findById(code)
            .orElse(null);
        return result;
    }

    /**
     * Retrieve FipsSateCode entity by UspsCode
     * @param uspsCode
     * @return
     */
    @Override
    public FipsStateCode retrieveStateCodeEntityByUspsCode(String uspsCode) {
        FipsStateCode result= stateCodeRepo
            .findByUspsCode(uspsCode)
            .orElse(null);
        return result;
    }

    @Override
    public List<CodeLookupDto> retrieveReleasePointTypeCodes() {

        List<CodeLookupDto> result = new ArrayList<CodeLookupDto>();
        Iterable<ReleasePointTypeCode> entities = releasePtTypeRepository.findAll(Sort.by(Direction.ASC, DESCRIPTION));

        entities.forEach(entity -> {
            result.add(lookupMapper.toDto(entity));
        });
        return result;
    }

    public ReleasePointTypeCode retrieveReleasePointTypeCodeEntityByCode(String code) {
    	ReleasePointTypeCode result= releasePtTypeRepository
            .findById(code)
            .orElse(null);
        return result;
    }

    /**
     * Retrieve non-legacy Release Point Type codes
     * @param year
     * @return
     */
    @Override
    public List<CodeLookupDto> retrieveCurrentReleasePointTypeCodes(Integer year) {

        List<ReleasePointTypeCode> entities = releasePtTypeRepository.findAllCurrent(year, Sort.by(Direction.ASC, DESCRIPTION));

        List<CodeLookupDto> result = lookupMapper.releasePointTypCodeToDtoList(entities);
        return result;
    }

    @Override
    public List<CodeLookupDto> retrieveProgramSystemTypeCodes() {

        List<CodeLookupDto> result = new ArrayList<CodeLookupDto>();
        Iterable<ProgramSystemCode> entities = programSystemCodeRepo.findAll(Sort.by(Direction.ASC, DESCRIPTION));

        entities.forEach(entity -> {
            result.add(lookupMapper.toDto(entity));
        });
        return result;
    }

    @Override
    public CodeLookupDto retrieveProgramSystemCodeByDescription(String description) {

        ProgramSystemCode entity = programSystemCodeRepo.findByDescriptionIgnoreCase(description).orElse(null);
        return lookupMapper.toDto(entity);
    }

    public ProgramSystemCode retrieveProgramSystemTypeCodeEntityByCode(String code) {
    	ProgramSystemCode result= programSystemCodeRepo
            .findById(code)
            .orElse(null);
        return result;
    }

    @Override
    public List<CodeLookupDto> retrieveControlMeasureCodes() {

        List<CodeLookupDto> result = new ArrayList<CodeLookupDto>();
        Iterable<ControlMeasureCode> entities = controlMeasureCodeRepo.findAll(Sort.by(Direction.ASC, DESCRIPTION));

        entities.forEach(entity -> {
            result.add(lookupMapper.controlMeasureCodeToDto(entity));
        });
        return result;
    }

    /**
     * Retrieve non-legacy Control Measure codes
     * @param year
     * @return
     */
    @Override
    public List<CodeLookupDto> retrieveCurrentControlMeasureCodes(Integer year) {

        List<ControlMeasureCode> entities = controlMeasureCodeRepo.findAllCurrent(year, Sort.by(Direction.ASC, DESCRIPTION));

        List<CodeLookupDto> result = lookupMapper.controlMeasureCodeToDtoList(entities);
        return result;
    }

    public ControlMeasureCode retrieveControlMeasureCodeEntityByCode(String code) {
    	ControlMeasureCode result = controlMeasureCodeRepo
            .findById(code)
            .orElse(null);
        return result;
    }

    @Override
    public List<CodeLookupDto> retrieveTribalCodes() {

        List<CodeLookupDto> result = new ArrayList<CodeLookupDto>();
        Iterable<TribalCode> entities = tribalCodeRepo.findAll(Sort.by(Direction.ASC, DESCRIPTION));

        entities.forEach(entity -> {
            result.add(lookupMapper.toDto(entity));
        });
        return result;
    }

    public TribalCode retrieveTribalCodeEntityByCode(String code) {
    	TribalCode result = tribalCodeRepo
            .findById(code)
            .orElse(null);
        return result;
    }

    /**
     * Retrieve NAICS Codes
     * @return List of CodeLookupDtos
     */
    @Override
    public List<CodeLookupDto> retrieveNaicsCodes() {

        List<CodeLookupDto> result = new ArrayList<>();
        Iterable<NaicsCode> entities = naicsCodeRepo.findAll(Sort.by(Direction.ASC, "code"));
        List<NaicsCode> sixDigitCodes = StreamSupport.stream(entities.spliterator(), false)
            .filter(entity ->  entity.getCode().toString().length() == 6)
            .collect(Collectors.toList());

        sixDigitCodes.forEach(entity -> result.add(lookupMapper.naicsCodeToDto(entity)));

        return result;
    }

    /**
    * Retrieve NaicsCode entity by NAICS code
    * @param code
    * @return
    */
    @Override
    public NaicsCode retrieveNAICSCodeEntityByCode(Integer code) {
        NaicsCode result = naicsCodeRepo.findById(code).orElse(null);
        return result;
    }

    /**
     * Retrieve NAICS codes by year and boolean to limit results to six-digit codes, sort results by sortingField
     * @param year
     * @param onlySixDigits
     * @param sortingField
     * @return
     */
    @Override
    public List<CodeLookupDto> retrieveNaicsCodesByYear(Short year, boolean onlySixDigits, String sortingField) {

        List<NaicsCode> entities = naicsCodeRepo.findAllCurrent(year, Sort.by(Direction.ASC, sortingField)).stream()
                .collect(Collectors.toList());

        if (onlySixDigits) {
            entities = entities.stream()
                .filter(entity ->  entity.getCode().toString().length() == 6).collect(Collectors.toList());
        }

        for (NaicsCode nc : entities) {
            String description = findNaicsCodeDescriptionByYear(nc.getCode(), year);
            if (description != null) { // Alternate description found for year
                nc.setDescription(description);
            }
        }

        List<CodeLookupDto> result = lookupMapper.naicsCodeToDtoList(entities);
        return result;
    }

    @Override
    public List<FacilityPermitTypeDto> retrieveFacilityPermitTypes() {

        List<FacilityPermitType> result = permitTypeRepository.findAll(Sort.by(Direction.ASC, "code"));

       return lookupMapper.permitTypeToDtoList(result);
    }

    @Override
    public List<AircraftEngineTypeCodeDto> retrieveAircraftEngineCodes(String scc) {

        List<AircraftEngineTypeCodeDto> result = new ArrayList<AircraftEngineTypeCodeDto>();
        Iterable<AircraftEngineTypeCode> entities = aircraftEngCodeRepo.findByScc(scc, Sort.by(Direction.ASC, "faaAircraftType"));

        entities.forEach(entity -> {
            result.add(lookupMapper.aircraftEngCodeToDto(entity));
        });
        return result;
    }

    /**
     * Retrieve AircraftEngineTypeCode entity by code
     * @param code
     * @return AircraftEngineTypeCode
     * */
    @Override
    public AircraftEngineTypeCode retrieveAircraftEngineCodeEntityByCode(String code) {
        AircraftEngineTypeCode result = aircraftEngCodeRepo
            .findById(code)
            .orElse(null);
        return result;
    }

    @Override
    public List<AircraftEngineTypeCodeDto> retrieveCurrentAircraftEngineCodes(String scc, Integer year) {

        List<AircraftEngineTypeCode> entities = aircraftEngCodeRepo.findCurrentByScc(year, scc, Sort.by(Direction.ASC, "faaAircraftType"));

        List<AircraftEngineTypeCodeDto> result = lookupMapper.aircraftEngCodeToDtoList(entities);
        return result;
    }

    public PointSourceSccCodeDto retrievePointSourceSccCode(String code) {

    	PointSourceSccCode entity = pointSourceSccCodeRepo.findById(code).orElse(null);
    	return lookupMapper.pointSourceSccCodeToDto(entity);
    }

    @Override
    public List<FacilityCategoryCodeDto> retrieveFacilityCategoryCodes() {

        List<FacilityCategoryCodeDto> result = new ArrayList<FacilityCategoryCodeDto>();
        Iterable<FacilityCategoryCode> entities = facilityCategoryCodeRepo.findAll(Sort.by(Direction.ASC, DESCRIPTION));

        entities.forEach(entity -> {
            result.add(lookupMapper.facilityCategoryCodeToDto(entity));
        });
        return result;
    }

    /**
     * Retrieve Facility Category Code entity by code
     * @param code
     * @return FacilityCategoryCode
     */
    @Override
    public FacilityCategoryCode retrieveFacilityCategoryCodeEntityByCode(String code) {
        FacilityCategoryCode result = facilityCategoryCodeRepo
            .findById(code)
            .orElse(null);
        return result;
    }

    /**
     * Retrieve non-legacy Facility Source Type codes
     * @param year
     * @return
     */
    @Override
    public List<CodeLookupDto> retrieveCurrentFacilitySourceTypeCodes(Integer year) {

        List<FacilitySourceTypeCode> entities = facilitySourceTypeCodeRepo.findAllCurrent(year, Sort.by(Direction.ASC, DESCRIPTION));

        List<CodeLookupDto> result = lookupMapper.facilitySourceTypeCodeToDtoList(entities);
        return result;
    }

    @Override
    public List<CodeLookupDto> retrieveFacilitySourceTypeCodes() {

        List<CodeLookupDto> result = new ArrayList<CodeLookupDto>();
        Iterable<FacilitySourceTypeCode> entities = facilitySourceTypeCodeRepo.findAll(Sort.by(Direction.ASC, DESCRIPTION));

        entities.forEach(entity -> {
            result.add(lookupMapper.toDto(entity));
        });
        return result;
    }

    /**
     * Retrieve NAICS code description per given report year
     * @param code
     * @param year
     * @return
     */
    @Override
    public String findNaicsCodeDescriptionByYear(Integer code, Short year) {
        String description = null;
        List<NaicsDescriptionHistory> naicsDescriptionList = naicsDescriptionRepo.findAllByCode(
            code, Sort.by(Sort.Direction.DESC, "descriptionYear"));
        if (naicsDescriptionList != null && naicsDescriptionList.size() > 1) {
            for (NaicsDescriptionHistory ndh : naicsDescriptionList) {
                if (year >= ndh.getDescriptionYear()) {
                    description = ndh.getDescription();
                    break;
                }
            }
        }
        return description;
    }

    public List<PointSourceSccCodeDto> searchSccCodes(String searchTerm, String slt) {

        // TODO: error handling
        SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(slt);
        List<PointSourceSccCodeDto> result;

        // search all SCCs if non-points are enabled
        if (sltConfig.getNonPointSccEnabled()) {
            List<PointSourceSccCode> entities = pointSourceSccCodeRepo.findAnyBySearchTerm(searchTerm.toLowerCase(), Sort.by(Direction.ASC, "code"));
            result = lookupMapper.pointSourceSccCodeToDtoList(entities);
        } else {
            // search just point normally
            List<PointSourceSccCode> entities = pointSourceSccCodeRepo.findPointBySearchTerm(searchTerm.toLowerCase(), Sort.by(Direction.ASC, "code"));
            result = lookupMapper.pointSourceSccCodeToDtoList(entities);
        }

        return result;
    }

    public List<ReportingPeriodCode> findReportingPeriodMonthAndSemiAnnualCodes() {
        return periodCodeRepo.findAllMonthsAndSemiAnnual();
    }

    /**
     * Retrieve Emission Formula Variable Code entity by code
     * @param code
     * @return EmissionFormulaVariableCode
     */
    @Override
    public EmissionFormulaVariableCode retrieveFormulaVariableCodeEntityByCode(String code) {
        EmissionFormulaVariableCode result = formulaVariableCodeRepository
            .findById(code)
            .orElse(null);
        return result;
    }

    @Override
    public boolean wasPollutantReportedLastYear(String pollutantCode, Long masterFacilityId) {
         return pollutantRepo.wasReportedLastYear(pollutantCode, masterFacilityId);
    }
}
