/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
