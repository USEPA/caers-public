/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.impl;

import gov.epa.cef.web.domain.CalculationMaterialCode;
import gov.epa.cef.web.domain.Emission;
import gov.epa.cef.web.domain.EmissionFormulaVariable;
import gov.epa.cef.web.domain.EmissionFormulaVariableCode;
import gov.epa.cef.web.domain.EmissionsByFacilityAndCAS;
import gov.epa.cef.web.domain.EmissionsProcess;
import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.domain.EmissionsUnit;
import gov.epa.cef.web.domain.FacilitySite;
import gov.epa.cef.web.domain.PointSourceSccCode;
import gov.epa.cef.web.domain.ReportingPeriod;
import gov.epa.cef.web.domain.UnitMeasureCode;
import gov.epa.cef.web.repository.EmissionsByFacilityAndCASRepository;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.PointSourceSccCodeRepository;
import gov.epa.cef.web.repository.ReportHistoryRepository;
import gov.epa.cef.web.repository.ReportingPeriodRepository;
import gov.epa.cef.web.repository.UnitMeasureCodeRepository;
import gov.epa.cef.web.service.dto.EmissionDto;
import gov.epa.cef.web.service.dto.EmissionFormulaVariableCodeDto;
import gov.epa.cef.web.service.dto.EmissionFormulaVariableDto;
import gov.epa.cef.web.service.dto.EmissionsByFacilityAndCASDto;
import gov.epa.cef.web.service.dto.UnitMeasureCodeDto;
import gov.epa.cef.web.service.mapper.EmissionMapper;
import gov.epa.cef.web.service.mapper.EmissionsByFacilityAndCASMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.Silent.class)
public class EmissionServiceImplTest extends BaseServiceTest {

    @Mock
    private EmissionsByFacilityAndCASRepository emissionsByFacilityAndCASRepo;

    @Mock
    private EmissionsReportRepository emissionsReportRepo;

    @Mock
    private ReportingPeriodRepository periodRepo;

    @Mock
    private ReportHistoryRepository historyRepo;

    @Mock
    private UnitMeasureCodeRepository uomRepo;
    
    @Mock
    private PointSourceSccCodeRepository pointSourceSccCodeRepo;
    
    @Mock
    EmissionsByFacilityAndCASMapper emissionsByFacilityAndCASMapper;

    @Mock
    private EmissionMapper emissionMapper;

    @InjectMocks
    EmissionServiceImpl emissionServiceImpl;

    private List<EmissionFormulaVariableDto> variableDtoList;
    private List<EmissionFormulaVariable> variableList;

    @Before
    public void init(){
        List<EmissionsReport> emptyEmissionsReportList = new ArrayList<EmissionsReport>();
        EmissionsReport emissionsReport = new EmissionsReport();
        emissionsReport.setYear(new Short("2019"));
        List<EmissionsReport> emissionsReportList2019 = new ArrayList<EmissionsReport>();
        emissionsReportList2019.add(emissionsReport);
        List<EmissionsByFacilityAndCAS> emptyEmissions = new ArrayList<EmissionsByFacilityAndCAS>();
        List<EmissionsByFacilityAndCAS> emissionsList = new ArrayList<EmissionsByFacilityAndCAS>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date testMaxDate = new Date();
        try {
        	testMaxDate = sdf.parse("2019-05-26 19:20:51");
        } 
        catch (ParseException pe) {
        	testMaxDate = null;
        }
        Optional<Date> returnDate = Optional.of(testMaxDate);
        
        CalculationMaterialCode cmc = new CalculationMaterialCode();
        cmc.setCode("698");

        EmissionsByFacilityAndCAS emission1 = new EmissionsByFacilityAndCAS();
        emission1.setId(1L);
        emission1.setFacilityName("Test Facility 1");
        emission1.setTrifid("30906GSTNW2434H");
        emission1.setPollutantCasId("71-43-2");
        emission1.setPollutantName("Benzene");
        emission1.setYear(new Short("2019"));
        emission1.setStatus("ACCEPTED");
        emission1.setReleasePointIdentifier("RP-1234");
        emission1.setReleasePointType("stack");
        emission1.setApportionedEmissions(new BigDecimal("51.75"));
        emission1.setEmissionsUomCode("TON");
        emission1.setReportId(37L);

        EmissionsByFacilityAndCAS emission2 = new EmissionsByFacilityAndCAS();
        emission2.setId(2L);
        emission2.setFacilityName("Test Facility 1");
        emission2.setTrifid("30906GSTNW2434H");
        emission2.setPollutantCasId("71-43-2");
        emission2.setPollutantName("Benzene");
        emission2.setYear(new Short("2019"));
        emission2.setStatus("ACCEPTED");
        emission2.setReleasePointIdentifier("RP-3456");
        emission2.setReleasePointType("stack");
        emission2.setApportionedEmissions(new BigDecimal("765.15"));
        emission2.setEmissionsUomCode("TON");
        emission2.setReportId(37L);

        EmissionsByFacilityAndCAS emission3 = new EmissionsByFacilityAndCAS();
        emission3.setId(3L);
        emission3.setFacilityName("Test Facility 1");
        emission3.setTrifid("30906GSTNW2434H");
        emission3.setPollutantCasId("71-43-2");
        emission3.setPollutantName("Benzene");
        emission3.setYear(new Short("2019"));
        emission3.setStatus("ACCEPTED");
        emission3.setReleasePointIdentifier("RP-2345");
        emission3.setReleasePointType("fugitive");
        emission3.setApportionedEmissions(new BigDecimal("276.25"));
        emission3.setEmissionsUomCode("TON");
        emission3.setReportId(37L);

        EmissionsByFacilityAndCAS emission4 = new EmissionsByFacilityAndCAS();
        emission4.setId(4L);
        emission4.setFacilityName("Test Facility 1");
        emission4.setTrifid("30906GSTNW2434H");
        emission4.setPollutantCasId("71-43-2");
        emission4.setPollutantName("Benzene");
        emission4.setYear(new Short("2019"));
        emission4.setStatus("ACCEPTED");
        emission4.setReleasePointIdentifier("RP-2345");
        emission4.setReleasePointType("fugitive");
        emission4.setApportionedEmissions(new BigDecimal("114.86"));
        emission4.setEmissionsUomCode("TON");
        emission4.setReportId(37L);

        emissionsList.add(emission1);
        emissionsList.add(emission2);
        emissionsList.add(emission3);
        emissionsList.add(emission4);

        EmissionsByFacilityAndCASDto emissionsByFacilityAndCASDto = new EmissionsByFacilityAndCASDto();
        emissionsByFacilityAndCASDto.setTrifid("30906GSTNW2434H");
        emissionsByFacilityAndCASDto.setCasNumber("71-43-2");
        emissionsByFacilityAndCASDto.setChemical("Benzene");
        emissionsByFacilityAndCASDto.setFacilityName("Test Facility 1");
        emissionsByFacilityAndCASDto.setYear(new Short("2019"));
        emissionsByFacilityAndCASDto.setUom("TON");
        emissionsByFacilityAndCASDto.setReportStatus("ACCEPTED");
        emissionsByFacilityAndCASDto.setReportId(37L);

        UnitMeasureCode lbUom = new UnitMeasureCode();
        lbUom.setCode("LB");
        lbUom.setDescription("POUNDS");
        lbUom.setUnitType("MASS");
        lbUom.setCalculationVariable("[lb]");

        UnitMeasureCode tonUom = new UnitMeasureCode();
        tonUom.setCode("TON");
        tonUom.setDescription("TONS");
        tonUom.setUnitType("MASS");
        tonUom.setCalculationVariable("sTon");
        
        PointSourceSccCode scc = new PointSourceSccCode();
        scc.setCode("33000499");
        scc.setFuelUseRequired(false);

        EmissionsProcess p = new EmissionsProcess();
        p.setSccCode("33000499");
        
        ReportingPeriod rp = new ReportingPeriod();
        rp.setCalculationParameterValue(new BigDecimal(10));
        rp.setCalculationParameterUom(lbUom);
        rp.setEmissionsProcess(p);
        rp.getEmissionsProcess().setEmissionsUnit(new EmissionsUnit());
        rp.getEmissionsProcess().getEmissionsUnit().setFacilitySite(new FacilitySite());
        rp.getEmissionsProcess().getEmissionsUnit().getFacilitySite().setEmissionsReport(emissionsReport);
        rp.setCalculationMaterialCode(cmc);

        variableDtoList = new ArrayList<EmissionFormulaVariableDto>();
        variableDtoList.add(createEmissionFormulaVariableDto(new BigDecimal(1), "A"));
        variableDtoList.add(createEmissionFormulaVariableDto(new BigDecimal(2), "S"));
        variableDtoList.add(createEmissionFormulaVariableDto(new BigDecimal(3), "U"));
        variableDtoList.add(createEmissionFormulaVariableDto(new BigDecimal(4), "SU"));
        variableDtoList.add(createEmissionFormulaVariableDto(new BigDecimal(5), "CaSu"));

        variableList = new ArrayList<EmissionFormulaVariable>();
        variableList.add(createEmissionFormulaVariable(new BigDecimal(1), "A"));
        variableList.add(createEmissionFormulaVariable(new BigDecimal(2), "S"));
        variableList.add(createEmissionFormulaVariable(new BigDecimal(3), "U"));
        variableList.add(createEmissionFormulaVariable(new BigDecimal(4), "SU"));
        variableList.add(createEmissionFormulaVariable(new BigDecimal(5), "CaSu"));

        when(emissionsByFacilityAndCASMapper.toDto(emission1)).thenReturn(emissionsByFacilityAndCASDto);
        when(periodRepo.findById(1L)).thenReturn(Optional.of(rp));
        when(historyRepo.retrieveMaxSubmissionDateByReportId(37L)).thenReturn(returnDate);
        when(uomRepo.findById("LB")).thenReturn(Optional.of(lbUom));
        when(uomRepo.findById("TON")).thenReturn(Optional.of(tonUom));
        when(pointSourceSccCodeRepo.findById("33000499")).thenReturn(Optional.of(scc));

        doAnswer(invocation -> {

            EmissionDto dto = invocation.getArgument(0);
            Emission emission = new Emission();

            emission.setFormulaIndicator(dto.getFormulaIndicator());
            emission.setEmissionsFactorFormula(dto.getEmissionsFactorFormula());
            emission.setEmissionsFactor(dto.getEmissionsFactor());
            emission.setOverallControlPercent(dto.getOverallControlPercent());
            if (variableDtoList.equals(dto.getVariables())) {
                emission.setVariables(variableList);
            }

            return emission;
        }).when(emissionMapper).fromDto(any());

        doAnswer(invocation -> {

            Emission emission = invocation.getArgument(0);
            EmissionDto dto = new EmissionDto();

            dto.setEmissionsFactor(emission.getEmissionsFactor());
            dto.setTotalEmissions(emission.getTotalEmissions());

            return dto;
        }).when(emissionMapper).toDto(any());
    }

    @Test
    public void calculateTotalEmissions_should_return_dto_with_ef_when_formulaIndicator_true() {
        EmissionDto emission = new EmissionDto();
        emission.setFormulaIndicator(true);
        emission.setReportingPeriodId(1L);
        emission.setEmissionsFactorFormula("A+(CaSu*U)^(SU/S)");

        emission.setVariables(variableDtoList);

        UnitMeasureCodeDto uomDto = new UnitMeasureCodeDto();
        uomDto.setCode("LB");

        emission.setEmissionsNumeratorUom(uomDto);
        emission.setEmissionsDenominatorUom(uomDto);
        emission.setEmissionsUomCode(uomDto);

        EmissionDto result = emissionServiceImpl.calculateTotalEmissions(emission);

        assertEquals(0, new BigDecimal(226).compareTo(result.getEmissionsFactor()));
        assertEquals(0, new BigDecimal(2260).compareTo(result.getTotalEmissions()));
    }

    @Test
    public void calculateTotalEmissions_should_return_dto_with_ef_when_formulaIndicator_true_unit_conversion() {
        EmissionDto emission = new EmissionDto();
        emission.setFormulaIndicator(true);
        emission.setReportingPeriodId(1L);
        emission.setEmissionsFactorFormula("(A+(CaSu*U)^(SU/S))");

        emission.setVariables(variableDtoList);

        UnitMeasureCodeDto lbUomDto = new UnitMeasureCodeDto();
        lbUomDto.setCode("LB");
        UnitMeasureCodeDto tonUomDto = new UnitMeasureCodeDto();
        tonUomDto.setCode("TON");

        emission.setEmissionsNumeratorUom(tonUomDto);
        emission.setEmissionsDenominatorUom(lbUomDto);
        emission.setEmissionsUomCode(lbUomDto);

        EmissionDto result = emissionServiceImpl.calculateTotalEmissions(emission);

        assertEquals(0, new BigDecimal(226).compareTo(result.getEmissionsFactor()));
        assertEquals(0, new BigDecimal(4520000).compareTo(result.getTotalEmissions()));
    }

    @Test
    public void calculateTotalEmissions_should_return_dto_with_ef_when_formulaIndicator_false() {
        EmissionDto emission = new EmissionDto();
        emission.setFormulaIndicator(false);
        emission.setReportingPeriodId(1L);
        emission.setEmissionsFactor(new BigDecimal(2.5));

        UnitMeasureCodeDto uomDto = new UnitMeasureCodeDto();
        uomDto.setCode("LB");

        emission.setEmissionsNumeratorUom(uomDto);
        emission.setEmissionsDenominatorUom(uomDto);
        emission.setEmissionsUomCode(uomDto);

        EmissionDto result = emissionServiceImpl.calculateTotalEmissions(emission);

        assertEquals(0, new BigDecimal(25).compareTo(result.getTotalEmissions()));
    }

    @Test
    public void calculateTotalEmissions_should_return_dto_with_ef_when_formulaIndicator_false_control_percent() {
        EmissionDto emission = new EmissionDto();
        emission.setFormulaIndicator(false);
        emission.setReportingPeriodId(1L);
        emission.setEmissionsFactor(new BigDecimal(2.5));

        UnitMeasureCodeDto uomDto = new UnitMeasureCodeDto();
        uomDto.setCode("LB");

        emission.setEmissionsNumeratorUom(uomDto);
        emission.setEmissionsDenominatorUom(uomDto);
        emission.setEmissionsUomCode(uomDto);
        emission.setOverallControlPercent(new BigDecimal("20"));

        EmissionDto result = emissionServiceImpl.calculateTotalEmissions(emission);

        assertEquals(0, new BigDecimal("20").compareTo(result.getTotalEmissions()));
    }

//    @Test
//    public void findEmissionsByFacilityAndCAS_should_return_no_emissions_report_when_none_exists() {
//        EmissionsByFacilityAndCASDto emissions = emissionServiceImpl.findEmissionsByFacilityAndCAS("12345", "71-43-2");
//        assertEquals("NO_EMISSIONS_REPORT", emissions.getCode());
//        assertNull(emissions.getCasNumber());
//        assertNull(emissions.getYear());
//        assertNull(emissions.getChemical());
//        assertNull(emissions.getStackEmissions());
//        assertNull(emissions.getFugitiveEmissions());
//        assertNull(emissions.getUom());
//    }
//
//    @Test
//    public void findEmissionsByFacilityAndCAS_should_return_no_emissions_when_pollutant_was_not_reported() {
//        EmissionsByFacilityAndCASDto emissions = emissionServiceImpl.findEmissionsByFacilityAndCAS("110015680799", "71-43-1");
//        assertEquals("NO_EMISSIONS_REPORTED_FOR_CAS", emissions.getCode());
//        assertNull(emissions.getCasNumber());
//        assertNull(emissions.getYear());
//        assertNull(emissions.getChemical());
//        assertNull(emissions.getStackEmissions());
//        assertNull(emissions.getFugitiveEmissions());
//        assertNull(emissions.getUom());
//    }
//
//    @Test
//    public void findEmissionsByFacilityAndCAS_should_return_calculated_emissions_when_pollutant_was_reported() {
//        EmissionsByFacilityAndCASDto emissions = emissionServiceImpl.findEmissionsByFacilityAndCAS("110015680799", "71-43-2");
//        assertEquals("EMISSIONS_FOUND", emissions.getCode());
//        assertEquals("71-43-2", emissions.getCasNumber());
//        assertEquals(new Short("2019"), emissions.getYear());
//        assertEquals("Benzene", emissions.getChemical());
//        assertEquals(new BigDecimal("816.90"), emissions.getStackEmissions());
//        assertEquals(new BigDecimal("391.11"), emissions.getFugitiveEmissions());
//        assertEquals("TON", emissions.getUom());
//        assertEquals("ACCEPTED", emissions.getReportStatus());
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        assertEquals("2019-05-26 19:20:51", sdf.format(emissions.getCertificationDate()));
//        
//    }

    private EmissionFormulaVariableDto createEmissionFormulaVariableDto(BigDecimal value, String name) {
        EmissionFormulaVariableDto variable = new EmissionFormulaVariableDto();
        EmissionFormulaVariableCodeDto code = new EmissionFormulaVariableCodeDto();

        code.setCode(name);
        variable.setValue(value);
        variable.setVariableCode(code);

        return variable;
    }

    private EmissionFormulaVariable createEmissionFormulaVariable(BigDecimal value, String name) {
        EmissionFormulaVariable variable = new EmissionFormulaVariable();
        EmissionFormulaVariableCode code = new EmissionFormulaVariableCode();

        code.setCode(name);
        variable.setValue(value);
        variable.setVariableCode(code);

        return variable;
    }
}
