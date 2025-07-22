/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.mapper;

import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.domain.common.BaseLookupEntity;
import gov.epa.cef.web.repository.LookupRepositories;
import gov.epa.cef.web.service.dto.*;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {})
public abstract class LookupEntityMapper {

    @Autowired
    private LookupRepositories repos;

    public abstract CodeLookupDto toDto(BaseLookupEntity source);

    public abstract List<CodeLookupDto> toDtoList(List<BaseLookupEntity> source);

    public abstract CodeLookupDto naicsCodeToDto(NaicsCode code);

    public abstract List<CodeLookupDto> naicsCodeToDtoList(List<NaicsCode> source);

    public abstract CodeLookupDto reportingPeriodCodeToDto(ReportingPeriodCode source);

    public abstract EmissionFormulaVariableCodeDto emissionFactorVariableCodeToDto(EmissionFormulaVariableCode source);

    public abstract CodeLookupDto emissionsOperatingTypeCodeToDto(EmissionsOperatingTypeCode source);

    public abstract CalculationMethodCodeDto calculationMethodCodeToDto(CalculationMethodCode source);

    public abstract CodeLookupDto controlMeasureCodeToDto(ControlMeasureCode source);

    public abstract List<CodeLookupDto> controlMeasureCodeToDtoList(List<ControlMeasureCode> source);

    public abstract List<CalculationMaterialCodeDto> fuelUseCalculationMaterialToDtoList(List<CalculationMaterialCode> source);

    public abstract CodeLookupDto releasePointTypCodeToDto(ReleasePointTypeCode source);

    public abstract List<CodeLookupDto> releasePointTypCodeToDtoList(List<ReleasePointTypeCode> source);

    public abstract FacilityCategoryCodeDto facilityCategoryCodeToDto(FacilityCategoryCode code);

    public abstract UnitMeasureCodeDto unitMeasureCodeToDto(UnitMeasureCode source);

    public abstract List<UnitMeasureCodeDto> unitMeasureCodeToDtoList(List<UnitMeasureCode> source);

    public abstract PollutantDto pollutantToDto(Pollutant source);

    @Mapping(target = "pollutants", qualifiedByName = "mapPollutantsForPollutantGroups")
    public abstract PollutantGroupDto pollutantGroupDto(PollutantGroup source);

    public abstract List<PollutantDto> pollutantToDtoList(List<Pollutant> source);

    public abstract FipsCountyDto fipsCountyToDto(FipsCounty source);

    public abstract List<FipsCountyDto> fipsCountyToDtoList(List<FipsCounty> source);

    public abstract FipsStateCodeDto fipsStateCodeToDto(FipsStateCode source);

    public abstract FacilityPermitTypeDto permitTypeToDto(FacilityPermitType source);

    public abstract List<FacilityPermitTypeDto> permitTypeToDtoList(List<FacilityPermitType> source);

    public abstract AircraftEngineTypeCodeDto aircraftEngCodeToDto(AircraftEngineTypeCode source);

    public abstract List<AircraftEngineTypeCodeDto> aircraftEngCodeToDtoList(List<AircraftEngineTypeCode> source);

    public abstract PointSourceSccCodeDto pointSourceSccCodeToDto(PointSourceSccCode source);

    public abstract List<PointSourceSccCodeDto> pointSourceSccCodeToDtoList(List<PointSourceSccCode> source);

    public abstract CodeLookupDto facilitySourceTypeCodeToDto(FacilitySourceTypeCode source);

    public abstract List<CodeLookupDto> facilitySourceTypeCodeToDtoList(List<FacilitySourceTypeCode> source);

    @Named("CalculationMethodCode")
    public CalculationMethodCode dtoToCalculationMethodCode(CodeLookupDto source) {
        if (source != null) {
            return repos.methodCodeRepo().findById(source.getCode()).orElse(null);
        }
        return null;
    }

    @Named("OperatingStatusCode")
    public OperatingStatusCode dtoToOperatingStatusCode(CodeLookupDto source) {
        if (source != null) {
            return repos.operatingStatusRepo().findById(source.getCode()).orElse(null);
        }
        return null;
    }

    @Named("UnitTypeCode")
    public UnitTypeCode dtoToUnitTypeCode(CodeLookupDto source) {
        if (source != null) {
            return repos.UnitTypeCodeRepo().findById(source.getCode()).orElse(null);
        }
        return null;
    }

    public Pollutant pollutantDtoToPollutant(PollutantDto source) {
        if (source != null) {
            return repos.pollutantRepo().findById(source.getPollutantCode()).orElse(null);
        }
        return null;
    }

    @Named("UnitMeasureCode")
    public UnitMeasureCode dtoToUnitMeasureCode(CodeLookupDto source) {
        if (source != null) {
            return repos.uomRepo().findById(source.getCode()).orElse(null);
        }
        return null;
    }

    @Named("ContactTypeCode")
    public ContactTypeCode dtoToContactTypeCode(CodeLookupDto source) {
        if (source != null) {
            return repos.contactTypeRepo().findById(source.getCode()).orElse(null);
        }
        return null;
    }

    @Named("FipsCounty")
    public FipsCounty dtoToFipsStateCode(FipsCountyDto source) {
        if (source != null) {
            return repos.countyRepo().findById(source.getCode()).orElse(null);
        }
        return null;
    }

    @Named("FipsStateCode")
    public FipsStateCode dtoToFipsStateCode(FipsStateCodeDto source) {
        if (source != null) {

            FipsStateCode result = null;
            if (source.getCode() != null) {
                result = repos.stateCodeRepo().findById(source.getCode()).orElse(null);
            }
            if (result == null && source.getUspsCode() != null) {
                result = repos.stateCodeRepo().findByUspsCode(source.getUspsCode()).orElse(null);
            }

            return result;
        }
        return null;
    }

    @Named("ReleasePointTypeCode")
    public ReleasePointTypeCode dtoToReleasePointTypeCode(CodeLookupDto source) {
        if (source != null) {
            return repos.releasePtCodeRepo().findById(source.getCode()).orElse(null);
        }
        return null;
    }

    @Named("ProgramSystemCode")
    public ProgramSystemCode dtoToProgramSystemCode(CodeLookupDto source) {
        if (source != null) {
            return repos.programSystemCodeRepo().findById(source.getCode()).orElse(null);
        }
        return null;
    }

    @Named("ControlMeasureCode")
    public ControlMeasureCode dtoToControlMeasureCode(CodeLookupDto source) {
        if (source != null) {
            return repos.controlMeasureCodeRepo().findById(source.getCode()).orElse(null);
        }
        return null;
    }

    @Named("TribalCode")
    public TribalCode dtoToTribalCode(CodeLookupDto source) {
        if (source != null) {
            return repos.tribalCodeRepo().findById(source.getCode()).orElse(null);
        }
        return null;
    }

    @Named("AircraftEngineTypeCode")
    public AircraftEngineTypeCode dtoToAircraftEngCode(AircraftEngineTypeCodeDto source) {
        if (source != null) {
            return repos.aircraftEngCodeRepo().findById(source.getCode()).orElse(null);
        }
        return null;
    }

    @Named("FacilitySourceTypeCode")
    public FacilitySourceTypeCode dtoToFacilitySourceTypeCode(CodeLookupDto source) {
        if (source != null) {
            return repos.facilitySourceTypeCodeRepo().findById(source.getCode()).orElse(null);
        }
        return null;
    }

    @Named("FacilityCategoryCode")
    public FacilityCategoryCode dtoToFacilityCategoryCode(CodeLookupDto source) {
        if (source != null) {
            return repos.facilityCategoryCodeRepo().findById(source.getCode()).orElse(null);
        }
        return null;
    }

    @Named("EmissionFactor")
    public WebfireEmissionFactor dtoToWebfireEmissionFactor(EmissionFactorDto source) {
        if (source != null) {
            return repos.webfireEmissionFactorRepo().findByWebfireId(source.getWebfireId());
        }
        return null;
    }

    public Set<PollutantDto> mapPollutantsForPollutantGroups(Set<Pollutant> pollutants) {
        if (pollutants != null) {
            return  pollutants.stream()
                        .map(pollutant -> {
                            PollutantDto dto = new PollutantDto();
                            dto.setPollutantCode(pollutant.getPollutantCode());
                            dto.setPollutantName(pollutant.getPollutantName());
                            dto.setPollutantCasId(pollutant.getPollutantCasId());
                            dto.setPollutantSrsId(pollutant.getPollutantSrsId());
                            dto.setPollutantType(pollutant.getPollutantType());
                            dto.setPollutantStandardUomCode(pollutant.getPollutantStandardUomCode());
                            dto.setLastInventoryYear(pollutant.getLastInventoryYear());

                            // PollutantGroups are not being mapped since there is no reason within PollutantGroups.pollutants to get reference to PollutantGroups.
                            // If in the future we want to get the other PollutantGroups within a pollutant under a PollutantGroup we are going to have to implement this mapping.

                            // ProgramSystemCodes are not being mapped, since there is no need for it within PollutantGroups.pollutants.
                            // If in the future we want this reference we are going to have to implement it
                            return dto;
                        })
                        .collect(Collectors.toSet());
        }
        return null;
    }

}
