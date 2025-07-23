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

import gov.epa.cef.web.domain.ReleasePoint;
import gov.epa.cef.web.repository.ReleasePointRepository;
import gov.epa.cef.web.service.dto.ReleasePointDto;
import gov.epa.cef.web.service.mapper.ReleasePointMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ReleasePointServiceImplTest extends BaseServiceTest {

    @Mock
    private ReleasePointRepository releasePointRepo;

    @Mock
    private ReleasePointMapper releasePointMapper;

    @InjectMocks
    private ReleasePointServiceImpl releasePointServiceImpl;

    private ReleasePointDto releasePointDto;
    private List<ReleasePointDto> releasePointDtoList;

    @Before
    public void init(){
        ReleasePoint releasePoint = new ReleasePoint();
        List<ReleasePoint> releasePointList = new ArrayList<ReleasePoint>();
        List<ReleasePoint> emptyReleasePointList = new ArrayList<ReleasePoint>();
        releasePointList.add(releasePoint);
        when(releasePointRepo.findById(1L)).thenReturn(Optional.of(releasePoint));
        when(releasePointRepo.findById(2L)).thenReturn(Optional.empty());
        when(releasePointRepo.findByFacilitySiteIdOrderByReleasePointIdentifier(1L)).thenReturn(releasePointList);
        when(releasePointRepo.findByFacilitySiteIdOrderByReleasePointIdentifier(2L)).thenReturn(emptyReleasePointList);

        releasePointDto=new ReleasePointDto();
        releasePointDtoList=new ArrayList<>();
        when(releasePointMapper.toDto(releasePoint)).thenReturn(releasePointDto);
        when(releasePointMapper.toDtoList(releasePointList)).thenReturn(releasePointDtoList);
    }

    @Test
    public void retrieveById_Should_Return_ReleasePointObject_When_ReleasePointExists(){
        ReleasePointDto releasePoint = releasePointServiceImpl.retrieveById(1L);
        assertNotEquals(null, releasePoint);
    }

    @Test
    public void retrieveById_Should_Return_Null_When_ReleasePointDoesNotExist(){
        ReleasePointDto releasePoint = releasePointServiceImpl.retrieveById(2L);
        assertEquals(null, releasePoint);
    }

    @Test
    public void retrieveById_Should_Return_Null_When_IDisNull(){
        ReleasePointDto releasePoint = releasePointServiceImpl.retrieveById(null);
        assertEquals(null, releasePoint);
    }

    @Test
    public void retrieveByFacilitySiteId_Should_Return_ReleasePointList_When_ReleasePointsExist() {
        Collection<ReleasePointDto> releasePointList = releasePointServiceImpl.retrieveByFacilitySiteId(1L);
        assertNotEquals(null, releasePointList);
    }

    @Test
    public void retrieveByFacilitySiteId_Should_Return_Empty_When_ReleasePointDoNotExist() {
        Collection<ReleasePointDto> releasePointList = releasePointServiceImpl.retrieveByFacilitySiteId(2L);
        assertEquals(new ArrayList<ReleasePoint>(), releasePointList);
    }

}
