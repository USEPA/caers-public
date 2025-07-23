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
package gov.epa.cef.web.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import gov.epa.cef.web.domain.EmissionFormulaVariableCode;
import gov.epa.cef.web.domain.GHGEmissionFactor;
import gov.epa.cef.web.domain.WebfireEmissionFactor;
import gov.epa.cef.web.repository.WebfireEmissionFactorRepository;
import gov.epa.cef.web.repository.EmissionFormulaVariableCodeRepository;
import gov.epa.cef.web.repository.SLTEmissionFactorRepository;
import gov.epa.cef.web.repository.GHGEmissionFactorRepository;
import gov.epa.cef.web.service.EmissionFactorService;
import gov.epa.cef.web.service.dto.EmissionFactorDto;
import gov.epa.cef.web.service.dto.EmissionFormulaVariableCodeDto;
import gov.epa.cef.web.service.mapper.EmissionFactorMapper;
import gov.epa.cef.web.service.mapper.LookupEntityMapper;

@Service
public class EmissionFactorServiceImpl implements EmissionFactorService {

    @Autowired
    private WebfireEmissionFactorRepository webfireEfRepo;

    @Autowired
    private GHGEmissionFactorRepository ghgEfRepo;

    @Autowired
    private SLTEmissionFactorRepository sltEfRepo;

    @Autowired
    private EmissionFormulaVariableCodeRepository efVariableRepo;

    @Autowired
    private EmissionFactorMapper mapper;

    @Autowired
    private LookupEntityMapper lookupMapper;

    /* (non-Javadoc)
     * @see gov.epa.cef.web.service.impl.EmissionFactorService#retrieveByExample(gov.epa.cef.web.service.dto.EmissionFactorDto)
     */
    @Override
    public List<EmissionFactorDto> retrieveByExample(EmissionFactorDto dto) {

        EmissionFactorDto webfireDto = new EmissionFactorDto();
        webfireDto.setSccCode(dto.getSccCode());
        webfireDto.setPollutantCode(dto.getPollutantCode());
        webfireDto.setControlIndicator(dto.getControlIndicator());

        EmissionFactorDto ghgDto = new EmissionFactorDto();
        ghgDto.setPollutantCode(dto.getPollutantCode());
        ghgDto.setControlIndicator(dto.getControlIndicator());
        ghgDto.setCalculationMaterialCode(dto.getCalculationMaterialCode());
        ghgDto.setCalculationParameterTypeCode(dto.getCalculationParameterTypeCode());

        List<EmissionFactorDto> resultList = mapper.toWebfireEfDtoList(
            new ArrayList<>(webfireEfRepo.findAll(Example.of(mapper.webfireEfFromDto(webfireDto)))));
        List<EmissionFactorDto> ghgResultList = mapper.toGHGEfDtoList(
            new ArrayList<>(ghgEfRepo.findAll(Example.of(mapper.ghgEfFromDto(ghgDto)))));
        resultList.addAll(ghgResultList);

        resultList.forEach(ef -> {
            if (Boolean.TRUE.equals(ef.getFormulaIndicator())) {
                ef.setVariables(parseFormulaVariables(ef.getEmissionFactorFormula()));
            }
        });

        return resultList;
    }

    @Override
    public List<EmissionFactorDto> retrieveSLTEfByExample(EmissionFactorDto dto) {

        EmissionFactorDto sltDto = new EmissionFactorDto();
        sltDto.setSccCode(dto.getSccCode());
        sltDto.setPollutantCode(dto.getPollutantCode());
        sltDto.setControlIndicator(dto.getControlIndicator());

        List<EmissionFactorDto> result = mapper.toSltEfDtoList(sltEfRepo.findAll(Example.of(mapper.sltEfFromDto(sltDto))).stream().collect(Collectors.toList()));

        result.forEach(ef -> {
            if (Boolean.TRUE.equals(ef.getFormulaIndicator())) {
                ef.setVariables(parseFormulaVariables(ef.getEmissionFactorFormula()));
            }
        });

        return result;
    }

    @Override
    public List<EmissionFormulaVariableCodeDto> parseFormulaVariables(String formula) {
        // Sorting by code length in descending order will ensure that variables are identified correctly.
        // Should also be able to add new variables without code changes.
        List<EmissionFormulaVariableCode> variables = efVariableRepo.findAll(JpaSort.unsafe(Sort.Direction.DESC, "LENGTH(code)"));
        List<EmissionFormulaVariableCodeDto> result = new ArrayList<EmissionFormulaVariableCodeDto>();

        for (EmissionFormulaVariableCode variable : variables) {
            if (formula.contains(variable.getCode())) {
                formula = formula.replaceAll(variable.getCode(), "");
                result.add(lookupMapper.emissionFactorVariableCodeToDto(variable));
            }
        }

        return result;
    }

    @Override
    public EmissionFactorDto retrieveByWebfireId(Long webfireId) {
        WebfireEmissionFactor ef = webfireEfRepo.findByWebfireId(webfireId);
        EmissionFactorDto dto = mapper.toWebfireEfDto(ef);
        return dto;//mapper.toDto(efRepo.findByWebfireId(webfireId));
    }

    /**
     * Retrieve the Emission Factor with the associated GHG ID
     * @param ghgId
     * @return
     */
    @Override
    public EmissionFactorDto retrieveByGhgId(Long ghgId) {
        GHGEmissionFactor ef = ghgEfRepo.findById(ghgId).orElse(null);
        EmissionFactorDto dto = mapper.toGHGEfDto(ef);
        return dto;
    }
}
