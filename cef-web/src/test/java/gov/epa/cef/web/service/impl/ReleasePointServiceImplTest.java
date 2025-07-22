/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
