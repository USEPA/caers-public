/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.validation.validator.federal;

import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.google.common.base.Strings;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.exception.CalculationException;
import gov.epa.cef.web.repository.WebfireEmissionFactorRepository;
import gov.epa.cef.web.repository.GHGEmissionFactorRepository;
import gov.epa.cef.web.repository.PointSourceSccCodeRepository;
import gov.epa.cef.web.service.dto.EmissionFactorDto;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.service.impl.EmissionServiceImpl;
import gov.epa.cef.web.service.mapper.EmissionFactorMapper;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.BaseValidator;
import gov.epa.cef.web.util.CalculationUtils;
import gov.epa.cef.web.util.ConstantUtils;
import gov.epa.cef.web.util.ValidatorUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SharedEmissionValidator extends BaseValidator<Emission> {

    @Autowired
	private WebfireEmissionFactorRepository webfireEfRepo;

    @Autowired
    private GHGEmissionFactorRepository ghgEfRepo;

    @Autowired
    private PointSourceSccCodeRepository pointSourceSccCodeRepo;

    @Autowired
    private EmissionServiceImpl emissionService;

    @Autowired
    private EmissionFactorMapper emissionFactorMapper;

    private static final String ASH_EMISSION_FORMULA_CODE = "A";
    private static final String SULFUR_EMISSION_FORMULA_CODE = "SU";
    private static final String RADIONUCLIDES_ID = "605";

    @Override
    public boolean validate(ValidatorContext validatorContext, Emission emission) {

        boolean valid = true;

        CefValidatorContext context = getCefValidatorContext(validatorContext);

        //if the emission is not reported then do not to the checks and message errors
        ValidatorUtil validatorUtil = new ValidatorUtil();
        if(emission.getNotReporting() != null && emission.getNotReporting().booleanValue() == Boolean.FALSE) {
            if (!emission.getReportingPeriod().getEmissionsProcess().getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN)
                    && !emission.getReportingPeriod().getEmissionsProcess().getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN)) {

                // check if last inventory year of a reported pollutant < the year of the report
                if (emission.getPollutant() != null && emission.getPollutant().getLastInventoryYear() != null
                    && emission.getPollutant().getLastInventoryYear() < getReportYear(emission)) {

                    valid = false;
                    context.addFederalError(
                        ValidationField.EMISSION_POLLUTANT.value(),
                        "pollutant.legacy",
                        createValidationDetails(emission));
                }

                if (emission.getEmissionsCalcMethodCode() == null) {

                    // prevented by db constraints
                    valid = false;
                    context.addFederalError(
                        ValidationField.EMISSION_CALC_METHOD.value(),
                        "emission.emissionsCalcMethodCode.required",
                        createValidationDetails(emission));

                } else if (Boolean.FALSE.equals(emission.getEmissionsCalcMethodCode().getTotalDirectEntry())
                    && !Boolean.TRUE.equals(emission.getFormulaIndicator()) && !Boolean.TRUE.equals(emission.getTotalManualEntry())) {

                    // ef is required if totalDirectEntery is false, formula indicator is false, and totalManualEntry (I prefer to calculate) is false
                    if (emission.getEmissionsFactor() == null) {

                        valid = false;
                        context.addFederalError(
                            ValidationField.EMISSION_EF.value(),
                            "emission.emissionsFactor.required.method",
                            createValidationDetails(emission));
                    }
                }

                // Check for valid EPA EF/EF formula
                boolean efFound = false;
                if (emission.getEmissionsNumeratorUom() != null && emission.getEmissionsDenominatorUom() != null
                    && emission.getEmissionsCalcMethodCode().getEpaEmissionFactor() && !Boolean.TRUE.equals(emission.getTotalManualEntry())) {

                    // Get list of all EPA EFs matching SCC/Pollutant/Control Indicator
                    List<EmissionFactorDto> efList =
                        emissionFactorMapper.toWebfireEfDtoList(
                            webfireEfRepo.findBySccCodePollutantControlIndicator(
                                emission.getReportingPeriod().getEmissionsProcess().getSccCode(),
                                emission.getPollutant().getPollutantCode(),
                                emission.getEmissionsCalcMethodCode().getControlIndicator()));
                    // Add all EPA EFs matching Pollutant/Control Indicator/Material/Input Parameter to list
                    if(emission.getReportingPeriod().getCalculationMaterialCode() != null) {
                        efList.addAll(emissionFactorMapper.toGHGEfDtoList(
                            ghgEfRepo.findByPollutantThroughputMaterialThroughputTypeControlIndicator(
                                emission.getPollutant().getPollutantCode(),
                                emission.getEmissionsCalcMethodCode().getControlIndicator(),
                                emission.getReportingPeriod().getCalculationMaterialCode().getCode(),
                                ConstantUtils.INPUT_CALCULATION_PARAMETER_TYPE_CODE)));
                    }
                    // if no results, SCC/Pollutant/control indicator combo is invalid
                    if (efList.isEmpty()) {

                        valid = false;
                        context.addFederalError(
                            ValidationField.EMISSION_EF.value(),
                            "emission.emissionsFactor.noMatch",
                            createValidationDetails(emission),
                            emission.getPollutant().getPollutantCode(),
                            emission.getReportingPeriod().getEmissionsProcess().getSccCode(),
                            emission.getReportingPeriod().getCalculationMaterialCode().getDescription());

                    } else {
                        Boolean epaEfRevoked = false;

                        // check for an entry in the list that also has matching UoMs and description
                        for (EmissionFactorDto ef : efList) {

                            if ((Boolean.TRUE.equals(emission.getFormulaIndicator()) && ef.getEmissionFactorFormula() != null && emission.getEmissionsFactorFormula() != null && ef.getEmissionFactorFormula().equals(emission.getEmissionsFactorFormula()) ||
                                (Boolean.FALSE.equals(emission.getFormulaIndicator()) &&
                                    (ef.getEmissionFactor() != null && emission.getEmissionsFactor() != null && ef.getEmissionFactor().compareTo(emission.getEmissionsFactor()) == 0) ||
                                    (ef.getMaxValue() != null && ef.getMinValue() != null && emission.getEmissionsFactor().compareTo(ef.getMaxValue()) <= 0 && emission.getEmissionsFactor().compareTo(ef.getMinValue()) >= 0))) &&
                                (emission.getEmissionsNumeratorUom() != null && ef.getEmissionsNumeratorUom() != null && emission.getEmissionsNumeratorUom().getCode().equals(ef.getEmissionsNumeratorUom().getCode())) &&
                                (emission.getEmissionsDenominatorUom() != null && ef.getEmissionsDenominatorUom() != null && emission.getEmissionsDenominatorUom().getCode().equals(ef.getEmissionsDenominatorUom().getCode()))) {

                                String description = "";
                                if (ef.getDescription().length() > 99) {
                                    description = ef.getDescription().substring(0, 97).concat("...");
                                } else {
                                    description = ef.getDescription();
                                }

                                // check for epa ef description and epa ef revoked status
                                if (description.equals(emission.getEmissionsFactorText())) {
                                    efFound = true;

                                    // factor considered not revoked for reports in years prior to revoked date
                                    if (ef.getRevokedDate() != null) {
                                        Calendar c = new GregorianCalendar();
                                        c.setTime(ef.getRevokedDate());
                                        epaEfRevoked = ef.getRevoked() && c.get(Calendar.YEAR) <= getReportYear(emission);
                                    } else {
                                        epaEfRevoked = ef.getRevoked();
                                    }

                                    // break out of loop if the epa ef found is not revoked
                                    if (Boolean.FALSE.equals(epaEfRevoked)) {
                                        break;
                                    }
                                }

                            }
                        }

                        // show error when all the epa ef that match SCC/Pollutant/control indicator combo are revoked
                        if (efFound && Boolean.TRUE.equals(epaEfRevoked)) {

                            valid = false;
                            context.addFederalError(
                                ValidationField.EMISSION_EF.value(),
                                "emission.emissionsFactor.epa.revoked",
                                createValidationDetails(emission));
                        }

                        // show error when epa ef is not found in db
                        if (!efFound) {

                            valid = false;
                            context.addFederalError(
                                ValidationField.EMISSION_EF.value(),
                                "emission.emissionsFactor.selectNew",
                                createValidationDetails(emission),
                                emission.getReportingPeriod().getEmissionsProcess().getSccCode(),
                                emission.getPollutant().getPollutantCode(),
                                emission.getReportingPeriod().getCalculationMaterialCode().getDescription());
                        }
                    }
                }

                EmissionFactor ef = null;
                if (emission.getWebfireEf() != null) {
                    ef = emission.getWebfireEf();
                }
                if (emission.getGhgEfId() != null) {
                    ef = ghgEfRepo.findById(emission.getGhgEfId()).orElse(null);
                }

                if (ef != null) {
                    if ("U".equals(ef.getQuality())) {
                        valid = false;
                        context.addFederalError(
                            ValidationField.EMISSION_EF.value(),
                            "emission.emissionFactor.quality",
                            createValidationDetails(emission));
                    }

                    if (emission.getEmissionsFactor() != null && ef.getMinValue() != null && ef.getMaxValue() != null) {
                        if (emission.getEmissionsFactor().compareTo(ef.getMinValue()) == -1 ||
                            emission.getEmissionsFactor().compareTo(ef.getMaxValue()) == 1) {

                            valid = false;
                            context.addFederalError(
                                ValidationField.EMISSION_EF.value(),
                                "emission.emissionsFactor.range.invalid",
                                createValidationDetails(emission),
                                ef.getMinValue().toString(),
                                ef.getMaxValue().toString());
                        }
                    }

                    // look at the condition for the factor to determine if formula variable meets criteria
                    // structure is consistent in db currently where all conditions are either > or <=. No >= or <
                    if (ef.isFormulaIndicator() && ef.getCondition() != null) {
                        String varCode = ef.getCondition().substring(0, 2);
                        for (EmissionFormulaVariable variable : emission.getVariables()) {
                            if (variable.getVariableCode().getCode().equals(varCode)) {
                                if (ef.getCondition().contains(">")) {

                                    BigDecimal compValue = new BigDecimal(ef.getCondition().substring(ef.getCondition().indexOf('>') + 1));
                                    if (variable.getValue().compareTo(compValue) < 1) {

                                        valid = false;
                                        context.addFederalError(
                                            ValidationField.EMISSION_FORMULA_VARIABLE.value(),
                                            "emission.formula.variable.condition",
                                            createValidationDetails(emission),
                                            ef.getCondition());
                                    }
                                } else {
                                    BigDecimal compValue = new BigDecimal(ef.getCondition().substring(4));
                                    if (variable.getValue().compareTo(compValue) > 0) {

                                        valid = false;
                                        context.addFederalError(
                                            ValidationField.EMISSION_FORMULA_VARIABLE.value(),
                                            "emission.formula.variable.condition",
                                            createValidationDetails(emission),
                                            ef.getCondition());
                                    }
                                }
                            }
                        }
                    }
                }

                if (emission.getEmissionsFactor() != null || (Boolean.TRUE.equals(emission.getFormulaIndicator()) && Boolean.TRUE.equals(emission.getTotalManualEntry()))) {

                    if (emission.getEmissionsFactor() != null && emission.getEmissionsFactor().compareTo(BigDecimal.ZERO) <= 0) {

                        valid = false;
                        context.addFederalError(
                            ValidationField.EMISSION_EF.value(),
                            "emission.emissionsFactor.range",
                            createValidationDetails(emission));
                    }

                    if (emission.getEmissionsNumeratorUom() == null) {

                        valid = false;
                        context.addFederalError(
                            ValidationField.EMISSION_NUM_UOM.value(),
                            "emission.emissionsNumeratorUom.required.emissionsFactor",
                            createValidationDetails(emission));

                    } else if (Boolean.TRUE.equals(emission.getEmissionsNumeratorUom().getLegacy())) {

                        valid = false;
                        context.addFederalError(
                            ValidationField.EMISSION_NUM_UOM.value(),
                            "emission.emissionsNumeratorUom.legacy",
                            createValidationDetails(emission),
                            emission.getEmissionsNumeratorUom().getDescription());
                    }

                    if (emission.getEmissionsDenominatorUom() == null) {

                        valid = false;
                        context.addFederalError(
                            ValidationField.EMISSION_DENOM_UOM.value(),
                            "emission.emissionsDenominatorUom.required.emissionsFactor",
                            createValidationDetails(emission));

                    } else if (Boolean.TRUE.equals(emission.getEmissionsDenominatorUom().getLegacy())) {

                        valid = false;
                        context.addFederalError(
                            ValidationField.EMISSION_DENOM_UOM.value(),
                            "emission.emissionsDenominatorUom.legacy",
                            createValidationDetails(emission),
                            emission.getEmissionsDenominatorUom().getDescription());
                    }

                } else if (emission.getEmissionsFactor() == null && emission.getMonthlyRate() == null
                    && Boolean.FALSE.equals(emission.getFormulaIndicator())) {

                    if (emission.getEmissionsNumeratorUom() != null) {

                        valid = false;
                        context.addFederalError(
                            ValidationField.EMISSION_NUM_UOM.value(),
                            "emission.emissionsNumeratorUom.banned.emissionsFactor",
                            createValidationDetails(emission));
                    }

                    if (emission.getEmissionsDenominatorUom() != null) {

                        valid = false;
                        context.addFederalError(
                            ValidationField.EMISSION_DENOM_UOM.value(),
                            "emission.emissionsDenominatorUom.banned.emissionsFactor",
                            createValidationDetails(emission));
                    }

                }

                if (CollectionUtils.isNotEmpty(emission.getVariables())) {

                    // check if formula variable data exists and ef does not have formula variable data
                    if (Boolean.FALSE.equals(emission.getFormulaIndicator())) {

                        valid = false;
                        context.addFederalError(
                            ValidationField.EMISSION_FORMULA_VARIABLE.value(),
                            "emission.formula.variable.invalid",
                            createValidationDetails(emission));
                    }

                    List<EmissionFormulaVariable> efvList = emission.getVariables().stream()
                        .filter(var -> var.getVariableCode() != null)
                        .collect(Collectors.toList());

                    // check for emission formula variable code % ash value to be between 0.01 and 30
                    for (EmissionFormulaVariable formulaVar : efvList) {
                        if (ASH_EMISSION_FORMULA_CODE.contentEquals(formulaVar.getVariableCode().getCode()) &&
                            (formulaVar.getValue() == null || formulaVar.getValue().compareTo(BigDecimal.valueOf(0.01)) == -1 || formulaVar.getValue().compareTo(new BigDecimal(30)) == 1)) {

                            valid = false;
                            context.addFederalError(
                                ValidationField.EMISSION_FORMULA_VARIABLE.value(),
                                "emission.formula.variable.ashRange",
                                createValidationDetails(emission));

                        }
                    }

                    // check for emission formula variable code % sulfur value to be between 0.00001 and 10
                    for (EmissionFormulaVariable formulaVar : efvList) {
                        if (SULFUR_EMISSION_FORMULA_CODE.contentEquals(formulaVar.getVariableCode().getCode()) &&
                            (formulaVar.getValue() == null || (formulaVar.getValue().compareTo(BigDecimal.valueOf(0.00001)) == -1) || (formulaVar.getValue().compareTo(new BigDecimal(10)) == 1))) {

                            valid = false;
                            context.addFederalError(
                                ValidationField.EMISSION_FORMULA_VARIABLE.value(),
                                "emission.formula.variable.sulfurRange",
                                createValidationDetails(emission));
                        }
                    }
                }

                if (emission.getEmissionsCalcMethodCode() != null && !emission.getEmissionsCalcMethodCode().getTotalDirectEntry() && !emission.getTotalManualEntry()) {

                    if (emission.getEmissionsCalcMethodCode().getEpaEmissionFactor()) {
                        if (Strings.emptyToNull(emission.getEmissionsFactorText()) == null) {
                            valid = false;
                            context.addFederalError(
                                ValidationField.EMISSION_EF_TEXT.value(),
                                "emission.emissionsFactorText.required.emissionsFactor.epa",
                                createValidationDetails(emission));
                        }
                    } else {

                        if (Strings.emptyToNull(emission.getEmissionsFactorText()) == null) {
                            valid = false;
                            context.addFederalError(
                                ValidationField.EMISSION_EF_TEXT.value(),
                                "emission.emissionsFactorText.required.emissionsFactor",
                                createValidationDetails(emission));
                        }
                    }
                }

                // Emission Calculation checks
                if (emission.getEmissionsCalcMethodCode() != null
                    && emission.getEmissionsFactor() != null
                    && emission.getTotalManualEntry() == false) {

                    if (Boolean.TRUE.equals(emission.getFormulaIndicator())) {

                        try {
                            CalculationUtils.calculateEmissionFormula(emission.getEmissionsFactorFormula(), emission.getVariables());
                        } catch (CalculationException e) {

                            valid = false;
                            context.addFederalError(
                                ValidationField.EMISSION_FORMULA_VARIABLE.value(),
                                "emission.formula.variable.missing",
                                createValidationDetails(emission),
                                String.join(", ", e.getMissingVariables()));
                        }
                    }

                    Boolean canConvert = false;
                    Boolean fuelUseSCC = false;

                    if (emission.getReportingPeriod() != null
                        && emission.getReportingPeriod().getCalculationParameterUom() != null
                        && emission.getEmissionsFactor() != null
                        && emission.getEmissionsNumeratorUom() != null
                        && emission.getEmissionsDenominatorUom() != null
                        && emission.getEmissionsUomCode() != null) {

                        PointSourceSccCode scc = pointSourceSccCodeRepo.findById(emission.getReportingPeriod().getEmissionsProcess().getSccCode()).orElse(null);
                        fuelUseSCC = scc != null ? scc.getFuelUseRequired() : false;

                        // Total emissions cannot be calculated with the given emissions factor because Throughput UoM {0} cannot be converted to Emission Factor Denominator UoM {1}.
                        // Please adjust Units of Measure or choose the option "I prefer to calculate the total emissions myself."
                        if (!emissionService.checkIfCanConvertUnits(emission.getReportingPeriod().getCalculationParameterUom(), emission.getEmissionsDenominatorUom()) && (fuelUseSCC || emission.getGhgEfId() != null)) { // All GHG EFs involve fuel
                            if ((emission.getReportingPeriod().getHeatContentUom() != null && emission.getReportingPeriod().getHeatContentValue() != null && emission.getReportingPeriod().getFuelUseUom() != null)
                                && (Boolean.TRUE.equals(emission.getReportingPeriod().getCalculationParameterUom().getFuelUseUom())
                                || Boolean.TRUE.equals(emission.getReportingPeriod().getCalculationParameterUom().getHeatContentUom()))) {
                                if (((emissionService.checkIfCanConvertUnits(emission.getReportingPeriod().getHeatContentUom(), emission.getEmissionsDenominatorUom()) && emissionService.checkIfCanConvertUnits(emission.getReportingPeriod().getFuelUseUom(), emission.getReportingPeriod().getCalculationParameterUom()))
                                    || (emissionService.checkIfCanConvertUnits(emission.getReportingPeriod().getFuelUseUom(), emission.getEmissionsDenominatorUom()) && emissionService.checkIfCanConvertUnits(emission.getReportingPeriod().getHeatContentUom(), emission.getReportingPeriod().getCalculationParameterUom())))) {
                                    canConvert = true;

                                }

                                if (!canConvert) {

                                    valid = false;
                                    context.addFederalError(
                                        ValidationField.EMISSION_DENOM_UOM.value(),
                                        "emission.emissionsDenominatorUom.mismatch",
                                        createValidationDetails(emission),
                                        emission.getReportingPeriod().getCalculationParameterUom().getDescription(),
                                        emission.getEmissionsDenominatorUom().getDescription());
                                }
                            } else {
                                valid = false;
                                context.addFederalError(
                                    ValidationField.EMISSION_DENOM_UOM.value(),
                                    "emission.emissionsDenominatorUom.mismatch",
                                    createValidationDetails(emission),
                                    emission.getReportingPeriod().getCalculationParameterUom().getDescription(),
                                    emission.getEmissionsDenominatorUom().getDescription());
                            }
                        }

                        //Total emissions cannot be calculated with the given emissions factor because Emission Factor Numerator UoM {0} cannot be converted to Total Emissions UoM {1}.
                        // Please adjust Units of Measure or choose the option "I prefer to calculate the total emissions myself."
                        if (!emissionService.checkIfCanConvertUnits(emission.getEmissionsNumeratorUom(), emission.getEmissionsUomCode())) {
                            valid = false;
                            context.addFederalError(
                                ValidationField.EMISSION_NUM_UOM.value(),
                                "emission.emissionsNumeratorUom.mismatch",
                                createValidationDetails(emission),
                                emission.getEmissionsNumeratorUom().getDescription(),
                                emission.getEmissionsUomCode().getDescription());
                        }
                    }
                }

                // curie cannot be selected for any pollutants other than radionuclides
                if (emission.getEmissionsUomCode() != null && "CURIE".contentEquals(emission.getEmissionsUomCode().getCode())
                    && emission.getPollutant() != null && !RADIONUCLIDES_ID.contentEquals(emission.getPollutant().getPollutantCode())) {

                    valid = false;
                    context.addFederalError(
                        ValidationField.EMISSION_UOM.value(),
                        "emission.emissionsCurieUom.invalid",
                        createValidationDetails(emission));
                }

                // percent overall control cannot be < 0 and cannot be >= 100 percent.
                if (emission.getOverallControlPercent() != null
                    && (emission.getOverallControlPercent().compareTo(new BigDecimal(0)) == -1
                    || emission.getOverallControlPercent().compareTo(new BigDecimal(100)) >= 0)) {

                    valid = false;
                    context.addFederalError(
                        ValidationField.EMISSION_CONTROL_PERCENT.value(),
                        "emission.controlPercent.range",
                        createValidationDetails(emission));

                }

                // if calculation method includes control efficiency (control indicator is true), then users cannot enter overall control percent
                if (emission.getEmissionsCalcMethodCode() != null && emission.getEmissionsCalcMethodCode().getControlIndicator()
                    && emission.getOverallControlPercent() != null && (emission.getOverallControlPercent().compareTo(new BigDecimal(0)) != 0)) {

                    valid = false;
                    context.addFederalError(
                        ValidationField.EMISSION_CONTROL_PERCENT.value(),
                        "emission.controlPercent.invalid",
                        createValidationDetails(emission));

                }

                // warning if pollutant has control percent but pollutant is not listed as control and control path pollutant
                boolean pollutantFound = false;
                if (emission.getOverallControlPercent() != null && emission.getOverallControlPercent().compareTo(BigDecimal.ZERO) > 0) {
                    for (ReleasePointAppt rpa : emission.getReportingPeriod().getEmissionsProcess().getReleasePointAppts()) {
                        if (rpa.getControlPath() != null) {
                            // filter down list of control path pollutants to check for the current emission's pollutant
                            List<ControlPathPollutant> cppList = rpa.getControlPath().getPollutants().stream().filter(pollutant -> pollutant.getPollutant().equals(emission.getPollutant())).collect(Collectors.toList());
                            // pollutant must exist in control path pollutant list to continue
                            if (!cppList.isEmpty()) {
                                for (ControlAssignment ca : rpa.getControlPath().getAssignments()) {
                                    if (ca.getControl() != null && ca.getControl().getPollutants() != null) {
                                        // pollutant must also be present in the control pollutants
                                        List<ControlPollutant> cpList = ca.getControl().getPollutants().stream().filter(pollutant -> pollutant.getPollutant().equals(emission.getPollutant())).collect(Collectors.toList());
                                        if (!cpList.isEmpty()) {
                                            pollutantFound = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        // quit for loop if the pollutant has already been found
                        if (pollutantFound) {
                            break;
                        }
                    }
                    // show warning if pollutant is not found
                    if (!pollutantFound) {
                        valid = false;
                        context.addFederalWarning(
                            ValidationField.EMISSION_CONTROL_PERCENT.value(),
                            "emission.controlPercent.noControl",
                            createValidationDetails(emission),
                            emission.getPollutant().getPollutantName());
                    }
                }
            }
        } else {
            valid = false;
            validatorUtil.checkNotReportingValidation(emission, context);
        }

        return valid;
    }

    private String getEmissionsUnitIdentifier(Emission emission) {
        if (emission.getReportingPeriod() != null && emission.getReportingPeriod().getEmissionsProcess() != null
                && emission.getReportingPeriod().getEmissionsProcess().getEmissionsUnit() != null) {
            return emission.getReportingPeriod().getEmissionsProcess().getEmissionsUnit().getUnitIdentifier();
        }
        return null;
    }

    private String getEmissionsProcessIdentifier(Emission emission) {
        if (emission.getReportingPeriod() != null && emission.getReportingPeriod().getEmissionsProcess() != null) {
            return emission.getReportingPeriod().getEmissionsProcess().getEmissionsProcessIdentifier();
        }
        return null;
    }

    private String getPollutantName(Emission emission) {
        if (emission.getPollutant() != null) {
            return emission.getPollutant().getPollutantName();
        }
        return null;
    }

    private int getReportYear(Emission emission) {
        return emission.getReportingPeriod().getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport().getYear().intValue();
    }

    private ValidationDetailDto createValidationDetails(Emission source) {

        String description = MessageFormat.format("Emissions Unit: {0}, Emissions Process: {1}, Pollutant: {2}",
                getEmissionsUnitIdentifier(source),
                getEmissionsProcessIdentifier(source),
                getPollutantName(source));

        ValidationDetailDto dto = new ValidationDetailDto(source.getId(), getPollutantName(source), EntityType.EMISSION, description);
        if (source.getReportingPeriod() != null) {
            dto.getParents().add(new ValidationDetailDto(source.getReportingPeriod().getId(), null, EntityType.REPORTING_PERIOD));
        }
        return dto;
    }
}
