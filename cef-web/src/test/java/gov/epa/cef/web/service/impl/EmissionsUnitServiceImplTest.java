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

import gov.epa.cef.web.domain.EmissionsUnit;
import gov.epa.cef.web.repository.EmissionsUnitRepository;
import gov.epa.cef.web.service.dto.EmissionsUnitDto;
import gov.epa.cef.web.service.mapper.EmissionsUnitMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmissionsUnitServiceImplTest extends BaseServiceTest {

    private EmissionsUnit emissionUnit;

    @Mock
    private EmissionsUnitRepository unitRepo;

    @Mock
    private EmissionsUnitMapper emissionsUnitMapper;

	@InjectMocks
	private EmissionsUnitServiceImpl emissionsUnitServiceImpl;

	@Before
	public void init(){
	    emissionUnit=new EmissionsUnit();
        when(emissionsUnitMapper.emissionsUnitToDto(null)).thenReturn(null);
        when(emissionsUnitMapper.emissionsUnitToDto(emissionUnit)).thenReturn(new EmissionsUnitDto());

	    when(unitRepo.findById(1L)).thenReturn(Optional.of(emissionUnit));
	    when(unitRepo.findById(2L)).thenReturn(Optional.empty());

	    List<EmissionsUnit> emissionsUnitsList=new ArrayList<>();
	    List<EmissionsUnitDto> emissionsUnitDtosList=new ArrayList<>();
        when(emissionsUnitMapper.emissionsUnitsToEmissionUnitsDtos(emissionsUnitsList))
                .thenReturn(emissionsUnitDtosList);
        when(emissionsUnitMapper.emissionsUnitsToEmissionUnitsDtos(null)).thenReturn(null);

        when(unitRepo.findByFacilitySiteIdOrderByUnitIdentifier(1L)).thenReturn(emissionsUnitsList);
	    when(unitRepo.findByFacilitySiteIdOrderByUnitIdentifier(2L)).thenReturn(null);

	}

	@Test
	public void retrieveById_Should_ReturnEmissionsUnittoObject_When_EmissionsUnitExists(){
	    EmissionsUnitDto emissionsUnit=emissionsUnitServiceImpl.retrieveUnitById(1L);
	    assertNotEquals(null, emissionsUnit);
	}

	@Test
    public void retrieveById_Should_ReturnNull_When_EmissionsUnitNotExists(){
       EmissionsUnitDto emissionsUnit=emissionsUnitServiceImpl.retrieveUnitById(2L);
       assertEquals(null, emissionsUnit);
    }

    @Test
    public void retrieveById_Should_ReturnNull_When_IdIsNull() {
        EmissionsUnitDto emissionsUnit = emissionsUnitServiceImpl.retrieveUnitById(null);
        assertEquals(null, emissionsUnit);
    }

    @Test
    public void retrieveForReleasePoint_Should_ReturnEmissionsUnitDtosList_When_ValidFacilitytIdPassed(){
        List<EmissionsUnitDto> emissionsUnitDtosList=emissionsUnitServiceImpl.retrieveEmissionUnitsForFacility(1L);
        assertNotEquals(null, emissionsUnitDtosList);
    }

    @Test
    public void retrieveForReleasePoint_Should_ReturnNull_When_InvalidFacilityIdPassed(){
        List<EmissionsUnitDto> emissionsUnitDtosList=emissionsUnitServiceImpl.retrieveEmissionUnitsForFacility(2L);
        assertEquals(null, emissionsUnitDtosList);
    }

}
