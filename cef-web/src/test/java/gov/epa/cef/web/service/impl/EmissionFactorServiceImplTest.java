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

import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.WebfireEmissionFactorRepository;
import gov.epa.cef.web.repository.SLTEmissionFactorRepository;
import gov.epa.cef.web.repository.GHGEmissionFactorRepository;
import gov.epa.cef.web.service.dto.CodeLookupDto;
import gov.epa.cef.web.service.dto.EmissionFactorDto;
import gov.epa.cef.web.service.mapper.EmissionFactorMapper;
import gov.epa.cef.web.service.mapper.EmissionFactorMapperImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Example;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class EmissionFactorServiceImplTest extends BaseServiceTest {

    @Mock
    private WebfireEmissionFactorRepository webfireEmissionFactorRepo;

    @Mock
    private SLTEmissionFactorRepository sltEmissionFactorRepo;

    @Mock
    private GHGEmissionFactorRepository ghgEmissonFactorRepo;

    @Spy
    private EmissionFactorMapper emissionFactorMapper = new EmissionFactorMapperImpl();

    @InjectMocks
    private EmissionFactorServiceImpl emissionFactorServiceImpl;

    private EmissionFactorDto emissionFactorDto;
    private EmissionFactorDto emissionFactorDto2;
    private EmissionFactorDto emissionFactorDto3;
    private EmissionFactorDto emissionFactorDto4;
    private EmissionFactorDto emissionFactorDto5;
    private EmissionFactorDto emissionFactorDto6;

    @Before
    public void init(){
        CalculationMaterialCode cmc = new CalculationMaterialCode();
        cmc.setCode("173");
        CodeLookupDto cmcDto = new CodeLookupDto();
        cmcDto.setCode("173");
        CalculationParameterTypeCode paramTypeCode = new CalculationParameterTypeCode();
        paramTypeCode.setCode("I");
        CodeLookupDto paramTypeCodeDto = new CodeLookupDto();
        paramTypeCodeDto.setCode("I");
        String sccCode10100302 = "10100302";
        String pollutantCodeCO2 = "CO2";

        WebfireEmissionFactor emissionFactor = new WebfireEmissionFactor();
        emissionFactor.setId(1L);
        emissionFactor.setSccCode(sccCode10100302);
        emissionFactor.setPollutantCode(pollutantCodeCO2);
        emissionFactor.setControlIndicator(false);

        WebfireEmissionFactor emissionFactor2 = new WebfireEmissionFactor();
        emissionFactor2.setId(2L);
        SLTEmissionFactor emissionFactor3 = new SLTEmissionFactor();
        emissionFactor3.setId(3L);
        SLTEmissionFactor emissionFactor4 = new SLTEmissionFactor();
        emissionFactor4.setId(4L);

        GHGEmissionFactor emissionFactor5 = new GHGEmissionFactor();
        emissionFactor5.setId(5L);
        emissionFactor5.setPollutantCode(pollutantCodeCO2);
        emissionFactor5.setControlIndicator(false);
        emissionFactor5.setCalculationMaterialCode(cmc);
        emissionFactor5.setCalculationParameterTypeCode(paramTypeCode);

        GHGEmissionFactor emissionFactor6 = new GHGEmissionFactor();
        emissionFactor6.setId(6L);

        List<WebfireEmissionFactor> emissionFactorList = new ArrayList<>();
        List<WebfireEmissionFactor> emptyEmissionFactorList = new ArrayList<>();
        List<SLTEmissionFactor> emissionFactorList2 = new ArrayList<>();
        List<SLTEmissionFactor> emptyEmissionFactorList2 = new ArrayList<>();
        List<GHGEmissionFactor> emissionFactorList3 = new ArrayList<>();
        List<GHGEmissionFactor> emptyEmissionFactorList3 = new ArrayList<>();
        emissionFactorList.add(emissionFactor);
        emissionFactorList2.add(emissionFactor3);
        emissionFactorList3.add(emissionFactor5);

        when(webfireEmissionFactorRepo.findAll(Example.of(emissionFactor))).thenReturn(emissionFactorList);
        when(webfireEmissionFactorRepo.findAll(Example.of(emissionFactor2))).thenReturn(emptyEmissionFactorList);
        when(sltEmissionFactorRepo.findAll(Example.of(emissionFactor3))).thenReturn(emissionFactorList2);
        when(sltEmissionFactorRepo.findAll(Example.of(emissionFactor4))).thenReturn(emptyEmissionFactorList2);
        when(ghgEmissonFactorRepo.findAll(Example.of(emissionFactor5))).thenReturn(emissionFactorList3);
        when(ghgEmissonFactorRepo.findAll(Example.of(emissionFactor6))).thenReturn(emptyEmissionFactorList3);

        emissionFactorDto = new EmissionFactorDto();
        emissionFactorDto.setId(1L);
        emissionFactorDto.setSccCode(sccCode10100302);
        emissionFactorDto.setPollutantCode(pollutantCodeCO2);
        emissionFactorDto.setControlIndicator(false);

        emissionFactorDto2 = new EmissionFactorDto();
        emissionFactorDto2.setId(2L);
        emissionFactorDto3 = new EmissionFactorDto();
        emissionFactorDto3.setId(3L);
        emissionFactorDto4 = new EmissionFactorDto();
        emissionFactorDto4.setId(4L);

        emissionFactorDto5 = new EmissionFactorDto();
        emissionFactorDto5.setId(5L);
        emissionFactorDto5.setPollutantCode(pollutantCodeCO2);
        emissionFactorDto5.setControlIndicator(false);
        emissionFactorDto5.setCalculationMaterialCode(cmcDto);
        emissionFactorDto5.setCalculationParameterTypeCode(paramTypeCodeDto);

        emissionFactorDto6 = new EmissionFactorDto();
        emissionFactorDto6.setId(6L);

        List<EmissionFactorDto> emissionFactorDtoList = new ArrayList<>();
        List<EmissionFactorDto> emptyEmissionFactorDtoList = new ArrayList<>();
        List<EmissionFactorDto> emissionFactorDtoList2 = new ArrayList<>();
        List<EmissionFactorDto> emptyEmissionFactorDtoList2 = new ArrayList<>();
        List<EmissionFactorDto> emissionFactorDtoList3 = new ArrayList<>();
        List<EmissionFactorDto> emptyEmissionFactorDtoList3 = new ArrayList<>();
        emissionFactorDtoList.add(emissionFactorDto);
        emissionFactorDtoList2.add(emissionFactorDto3);
        emissionFactorDtoList3.add(emissionFactorDto5);

        when(emissionFactorMapper.toWebfireEfDto(emissionFactor)).thenReturn(emissionFactorDto);
        when(emissionFactorMapper.toWebfireEfDto(emissionFactor2)).thenReturn(emissionFactorDto2);
        when(emissionFactorMapper.toSltEfDto(emissionFactor3)).thenReturn(emissionFactorDto3);
        when(emissionFactorMapper.toSltEfDto(emissionFactor4)).thenReturn(emissionFactorDto4);
        when(emissionFactorMapper.toGHGEfDto(emissionFactor5)).thenReturn(emissionFactorDto5);
        when(emissionFactorMapper.toGHGEfDto(emissionFactor6)).thenReturn(emissionFactorDto6);
        when(emissionFactorMapper.toWebfireEfDtoList(new ArrayList<>(emissionFactorList)))
            .thenReturn(emissionFactorDtoList);
        when(emissionFactorMapper.toWebfireEfDtoList(new ArrayList<>(emptyEmissionFactorList)))
            .thenReturn(emptyEmissionFactorDtoList);
        when(emissionFactorMapper.toSltEfDtoList(new ArrayList<>(emissionFactorList2)))
            .thenReturn(emissionFactorDtoList2);
        when(emissionFactorMapper.toSltEfDtoList(new ArrayList<>(emptyEmissionFactorList2)))
            .thenReturn(emptyEmissionFactorDtoList2);
        when(emissionFactorMapper.toGHGEfDtoList(new ArrayList<>(emissionFactorList3)))
            .thenReturn(emissionFactorDtoList3);
        when(emissionFactorMapper.toGHGEfDtoList(new ArrayList<>(emptyEmissionFactorList3)))
            .thenReturn(emptyEmissionFactorDtoList3);
        when(emissionFactorMapper.webfireEfFromDto(emissionFactorDto)).thenReturn(emissionFactor);
        when(emissionFactorMapper.webfireEfFromDto(emissionFactorDto2)).thenReturn(emissionFactor2);
        when(emissionFactorMapper.sltEfFromDto(emissionFactorDto3)).thenReturn(emissionFactor3);
        when(emissionFactorMapper.sltEfFromDto(emissionFactorDto4)).thenReturn(emissionFactor4);
        when(emissionFactorMapper.ghgEfFromDto(emissionFactorDto5)).thenReturn((emissionFactor5));
        when(emissionFactorMapper.ghgEfFromDto(emissionFactorDto6)).thenReturn((emissionFactor6));
    }

    @Test
    public void retrieveByExample_Should_Return_EmissionFactorList_When_WebfireEmissionFactorsExists(){
        List<EmissionFactorDto> result = emissionFactorServiceImpl.retrieveByExample(emissionFactorDto);
        assertNotEquals(null, result);
    }

    @Test
    public void retrieveByExample_Should_Return_Empty_When_WebfireEmissionFactorsDoNotExist(){
        List<EmissionFactorDto> result = emissionFactorServiceImpl.retrieveByExample(emissionFactorDto2);
        assertEquals(new ArrayList<EmissionFactorDto>(), result);
    }

    @Test
    public void retrieveByExample_Should_Return_EmissionFactorList_When_SLTEmissionFactorsExists(){
        List<EmissionFactorDto> result = emissionFactorServiceImpl.retrieveSLTEfByExample(emissionFactorDto3);
        assertNotEquals(null, result);
    }

    @Test
    public void retrieveByExample_Should_Return_Empty_When_SLTEmissionFactorsDoNotExist(){
        List<EmissionFactorDto> result = emissionFactorServiceImpl.retrieveSLTEfByExample(emissionFactorDto4);
        assertEquals(new ArrayList<EmissionFactorDto>(), result);
    }

    @Test
    public void retrieveByExample_Should_Return_EmissionFactorList_When_GHGEmissionFactorsExists(){
        List<EmissionFactorDto> result = emissionFactorServiceImpl.retrieveByExample(emissionFactorDto5);
        assertNotEquals(null, result);
    }

    @Test
    public void retrieveByExample_Should_Return_Empty_When_GHGEmissionFactorsDoNotExist(){
        List<EmissionFactorDto> result = emissionFactorServiceImpl.retrieveByExample(emissionFactorDto6);
        assertEquals(new ArrayList<EmissionFactorDto>(), result);
    }

}
