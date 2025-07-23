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

import gov.epa.cef.web.domain.MasterFacilityRecord;
import gov.epa.cef.web.repository.MasterFacilityRecordRepository;
import gov.epa.cef.web.service.dto.MasterFacilityRecordDto;
import gov.epa.cef.web.service.mapper.MasterFacilityRecordMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class MasterFacilityRecordServiceImplTest extends BaseServiceTest {

    @Mock
    private MasterFacilityRecordRepository mfrRepo;

    @Mock
    private MasterFacilityRecordMapper mfrMapper;

    @InjectMocks
    private MasterFacilityRecordServiceImpl masterFacilityRecordServiceImpl;
    

    @Before
    public void _init() throws Exception {

        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setId(1L);
        MasterFacilityRecord mfr2 = new MasterFacilityRecord();
        mfr2.setId(2L);
        when(mfrRepo.findById(1L)).thenReturn(Optional.of(mfr));
        when(mfrRepo.findById(2L)).thenReturn(Optional.empty());

        when(mfrRepo.save(any())).then(AdditionalAnswers.returnsFirstArg());

        MasterFacilityRecordDto mfrDto = new MasterFacilityRecordDto();
        when(mfrMapper.toDto(mfr)).thenReturn(mfrDto);

        List<MasterFacilityRecord> testSiteList = new ArrayList<MasterFacilityRecord>();
        testSiteList.add(mfr);
        List<MasterFacilityRecord> nullSiteList = null;
        when(mfrRepo.findById(1L)).thenReturn(Optional.of(mfr));
        when(mfrRepo.findByProgramSystemCodeCode("GOODPSC")).thenReturn(testSiteList);
        when(mfrRepo.findByProgramSystemCodeCode("BADPSC")).thenReturn(nullSiteList);
        when(mfrMapper.toDtoList(nullSiteList)).thenReturn(null);
    }


    @Test
    public void retrieveById_Should_Return_MasterFacilityRecordObject_When_MasterFacilityRecordExists() {

        MasterFacilityRecordDto mfr = masterFacilityRecordServiceImpl.findById(1L);
        assertNotEquals(null, mfr);
    }

    @Test
    public void retrieveById_Should_Return_Null_When_MasterFacilityRecordDoesNotExist() {

        MasterFacilityRecordDto mfr = masterFacilityRecordServiceImpl.findById(2L);
        assertEquals(null, mfr);
    }

    @Test
    public void retrieveByPsc_Should_Return_MasterFacilityRecordList_When_PscExists() {

        List<MasterFacilityRecordDto> mfrList = masterFacilityRecordServiceImpl.findByProgramSystemCode("GOODPSC");
        assertNotEquals(1, mfrList.size());
    }

    @Test
    public void retrieveByPsc_Should_Return_Null_When_PscDoesNotExists() {

        List<MasterFacilityRecordDto> mfrList = masterFacilityRecordServiceImpl.findByProgramSystemCode("BADPSC");
        assertEquals(null, mfrList);
    }
}
