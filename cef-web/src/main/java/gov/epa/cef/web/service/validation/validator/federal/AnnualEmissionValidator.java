/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package gov.epa.cef.web.service.validation.validator.federal;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import gov.epa.cef.web.config.CefConfig;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.EmissionRepository;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.PointSourceSccCodeRepository;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.service.impl.EmissionServiceImpl;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.BaseValidator;
import gov.epa.cef.web.util.CalculationUtils;
import gov.epa.cef.web.util.ConstantUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import gov.epa.cef.web.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.google.common.base.Strings;

@Component
public class AnnualEmissionValidator extends BaseValidator<Emission> {

    @Autowired
    private CefConfig cefConfig;

    @Autowired
    private EmissionRepository emissionRepo;

    @Autowired
    private EmissionsReportRepository reportRepo;

    @Autowired
    private PointSourceSccCodeRepository pointSourceSccCodeRepo;

    @Autowired
    private EmissionServiceImpl emissionService;

    private static final String ENGINEERING_JUDGEMENT = "2";
    private static final String RADIONUCLIDES_ID = "605";

    @Override
    public boolean validate(ValidatorContext validatorContext, Emission emission) {

        boolean valid = true;

        CefValidatorContext context = getCefValidatorContext(validatorContext);
        ValidatorUtil validatorUtil = new ValidatorUtil();

        EmissionsProcess emissionProcess = emission.getReportingPeriod().getEmissionsProcess();

        EmissionsReport currentReport = emission.getReportingPeriod().getEmissionsProcess()
            .getEmissionsUnit().getFacilitySite().getEmissionsReport();

        // Get previous report
        List<EmissionsReport> previousERList =
            reportRepo.findByMasterFacilityRecordId(currentReport.getMasterFacilityRecord().getId())
                .stream().filter(var -> (var.getYear() != null && var.getYear() < currentReport.getYear()))
                .sorted(Comparator.comparing(EmissionsReport::getYear))
                .collect(Collectors.toList());
        List<Emission> previousEmissions = new ArrayList<>();
        if(!emissionProcess.getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN) && !emissionProcess.getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN)) {
            if (!previousERList.isEmpty()) {
                previousEmissions = emissionRepo.retrieveMatchingForYear(
                    emission.getPollutant().getPollutantCode(),
                    emission.getReportingPeriod().getReportingPeriodTypeCode().getCode(),
                    getEmissionsProcessIdentifier(emission),
                    getEmissionsUnitIdentifier(emission),
                    previousERList.get(previousERList.size() - 1).getEisProgramId(),
                    previousERList.get(previousERList.size() - 1).getYear()
                );
                if (emission.getNotReporting()) {
                    if (!previousEmissions.isEmpty()) {
                        previousEmissions.forEach(pe -> {
                            if (pe.getNotReporting()) {
                                if (pe.getPollutant().getPollutantName().equals(emission.getPollutant().getPollutantName())) {
                                    validatorUtil.checkNotReportingValidationError(emission, context);
                                }
                            }
                        });
                    }
                }
            }
        }
        if(emission.getNotReporting() != null && emission.getNotReporting().booleanValue() == Boolean.FALSE) {
            if (!emission.getReportingPeriod().getEmissionsProcess().getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN)
                && !emission.getReportingPeriod().getEmissionsProcess().getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN)) {
                if (emission.getEmissionsCalcMethodCode() != null && emission.getEmissionsCalcMethodCode().getTotalDirectEntry()) {
                    // if the calculation method is engineering judgement the comment is required
                    if (Strings.emptyToNull(emission.getComments()) == null && ENGINEERING_JUDGEMENT.contentEquals(emission.getEmissionsCalcMethodCode().getCode())) {
                        valid = false;
                        context.addFederalError(
                            ValidationField.EMISSION_COMMENTS.value(),
                            "emission.comments.required.method",
                            createValidationDetails(emission));
                    }

                    // This check is commented out, to be confirmed Post MVP
//	            if (emission.getReportingPeriod() != null
//	                    && emission.getReportingPeriod().getCalculationParameterValue().compareTo(BigDecimal.ZERO) == 0
//	                    && emission.getTotalEmissions().compareTo(BigDecimal.ZERO) != 0) {
//
//	                valid = false;
//	                context.addFederalError(
//	                        ValidationField.EMISSION_TOTAL_EMISSIONS.value(),
//	                        "emission.totalEmissions.nonzero.method",
//	                        createValidationDetails(emission));
//	            }
                }
                //if not reported then do not do checks here
                //valid should be remain true if not reporting
                if (emission.getReportingPeriod().getCalculationParameterValue() != null
                    && emission.getReportingPeriod().getCalculationParameterValue().compareTo(BigDecimal.ZERO) > 0
                    && (emission.getTotalEmissions() != null && emission.getTotalEmissions().compareTo(BigDecimal.ZERO) == 0)) {

                    valid = false;
                    context.addFederalWarning(
                        ValidationField.EMISSION_TOTAL_EMISSIONS.value(),
                        "emission.totalEmissions.nonZero",
                        createValidationDetails(emission));
                }

                // Emission Calculation checks
                if (emission.getEmissionsCalcMethodCode() != null
                    && emission.getEmissionsFactor() != null
                    && !emission.getTotalManualEntry()) {

                    boolean canCalculate = true;
                    boolean canConvert = false;
                    boolean fuelUseSCC;

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
                                    canCalculate = false;
                                }
                            } else {
                                canCalculate = false;
                            }
                        }

                        // if the emission factor has no value
                        // or if "I wish to calculate..." is checked, don't show calculation QA
                        if (emission.getEmissionsFactor() == null || emission.getTotalManualEntry()) {
                            canCalculate = false;
                        }

                        // total emissions must not be null and must be >= 0
                        if (emission.getTotalEmissions() == null || emission.getTotalEmissions().compareTo(BigDecimal.ZERO) == -1) {

                            canCalculate = false;
                            valid = false;
                            context.addFederalError(
                                ValidationField.EMISSION_TOTAL_EMISSIONS.value(),
                                "emission.totalEmissions.range",
                                createValidationDetails(emission));
                        }

                        if (canCalculate) {

                            BigDecimal warningTolerance = cefConfig.getEmissionsTotalWarningTolerance();
                            BigDecimal errorTolerance = cefConfig.getEmissionsTotalErrorTolerance();

                            BigDecimal totalEmissions = calculateTotalEmissions(emission, canConvert);

                            // Total emissions listed for this pollutant are outside the acceptable range of +/-{0}% from {1} which is the calculated
                            // emissions based on the Emission Factor provided. Please recalculate the total emissions for this pollutant or choose the option
                            // "I prefer to calculate the total emissions myself."
                            if (checkTolerance(totalEmissions, emission.getTotalEmissions(), errorTolerance)) {

                                valid = false;
                                context.addFederalError(
                                    ValidationField.EMISSION_TOTAL_EMISSIONS.value(),
                                    "emission.totalEmissions.tolerance",
                                    createValidationDetails(emission),
                                    errorTolerance.multiply(new BigDecimal("100")).toString(),
                                    totalEmissions.toString());

                            } else if (checkTolerance(totalEmissions, emission.getTotalEmissions(), warningTolerance)) {

                                valid = false;
                                context.addFederalWarning(
                                    ValidationField.EMISSION_TOTAL_EMISSIONS.value(),
                                    "emission.totalEmissions.tolerance",
                                    createValidationDetails(emission),
                                    warningTolerance.multiply(new BigDecimal("100")).toString(),
                                    totalEmissions.toString());
                            }

                        }
                    }
                }

                if (emission.getPollutant() != null && (RADIONUCLIDES_ID.contentEquals(emission.getPollutant().getPollutantCode()))) {

                    if (emission.getEmissionsUomCode() == null || !"CURIE".contentEquals(emission.getEmissionsUomCode().getCode())) {
                        valid = false;
                        context.addFederalError(
                            ValidationField.EMISSION_CURIE_UOM.value(),
                            "emission.emissionsCurieUom.required",
                            createValidationDetails(emission));
                    }
                }

                // if totalManualEntry is selected and emission factor is used, calculation description is required
                if (emission.getTotalManualEntry() != null && emission.getTotalManualEntry() == true
                    && emission.getEmissionsFactor() != null && Strings.emptyToNull(emission.getCalculationComment()) == null) {

                    valid = false;
                    context.addFederalError(
                        ValidationField.EMISSION_CALC_DESC.value(),
                        "emission.calculationDescription.required",
                        createValidationDetails(emission));
                }
                if (emission.getEmissionsUomCode() != null) {
                    if (Boolean.TRUE.equals(emission.getEmissionsUomCode().getLegacy())) {
                        valid = false;
                        context.addFederalError(
                            ValidationField.EMISSION_UOM.value(),
                            "emission.emissionsUom.legacy",
                            createValidationDetails(emission),
                            emission.getEmissionsUomCode().getDescription());
                    }

                    if (
                        emission.getTotalEmissions() != null && emission.getPollutant() != null
                            && emission.getTotalEmissions().compareTo(BigDecimal.ZERO) > 0
                            && emission.getReportingPeriod() != null && getEmissionsProcessIdentifier(emission) != null
                            && getEmissionsUnitIdentifier(emission) != null
                    ) {
                        if (!previousERList.isEmpty()) {
                            if(previousEmissions.isEmpty()) {
                                previousEmissions = emissionRepo.retrieveMatchingForYear(
                                    emission.getPollutant().getPollutantCode(),
                                    emission.getReportingPeriod().getReportingPeriodTypeCode().getCode(),
                                    getEmissionsProcessIdentifier(emission),
                                    getEmissionsUnitIdentifier(emission),
                                    previousERList.get(previousERList.size() - 1).getEisProgramId(),
                                    previousERList.get(previousERList.size() - 1).getYear()
                                );
                            }
                            if (
                                !previousEmissions.isEmpty() && previousEmissions.get(0).getTotalEmissions() != null
                                    && previousEmissions.get(0).getEmissionsUomCode() != null
                            ) {
                                Emission previousEmission = previousEmissions.get(0);
                                BigDecimal emissionRange = null;
                                boolean invalidIncrease = false;
                                boolean invalidDecrease = false;
                                if (
                                    emission.getEmissionsUomCode().getCode().equals(
                                        previousEmission.getEmissionsUomCode().getCode()) // Needed for CURIES
                                ) {
                                    emissionRange = previousEmission.getTotalEmissions()
                                        .multiply(new BigDecimal(".2"));
                                    invalidIncrease = emission.getTotalEmissions().subtract(emissionRange)
                                        .compareTo(previousEmission.getTotalEmissions()) >= 0;
                                    invalidDecrease = emission.getTotalEmissions().add(emissionRange)
                                        .compareTo(previousEmission.getTotalEmissions()) <= 0;

                                } else if (
                                    emission.getCalculatedEmissionsTons() != null
                                        && previousEmission.getCalculatedEmissionsTons() != null
                                ) {
                                    emissionRange = previousEmission.getCalculatedEmissionsTons()
                                        .multiply(new BigDecimal(".2"));
                                    invalidIncrease = emission.getCalculatedEmissionsTons().subtract(emissionRange)
                                        .compareTo(previousEmission.getCalculatedEmissionsTons()) >= 0;
                                    invalidDecrease = emission.getCalculatedEmissionsTons().add(emissionRange)
                                        .compareTo(previousEmission.getCalculatedEmissionsTons()) <= 0;

                                }

                                if (emissionRange != null) {
                                    if (invalidIncrease) {
                                        valid = false;
                                        context.addFederalWarning(
                                            ValidationField.EMISSION_TOTAL_EMISSIONS.value(),
                                            "emission.totalEmissions.largeChange",
                                            createValidationDetails(emission),
                                            "higher");
                                    } else if (invalidDecrease) {
                                        valid = false;
                                        context.addFederalWarning(
                                            ValidationField.EMISSION_TOTAL_EMISSIONS.value(),
                                            "emission.totalEmissions.largeChange",
                                            createValidationDetails(emission),
                                            "lower");
                                    }
                                }
                            }
                        }
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

    private BigDecimal calculateTotalEmissions(Emission emission, boolean canConvert) {

        boolean leapYear = emission.getReportingPeriod().getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport().getYear() % 4 == 0;

        BigDecimal totalEmissions = BigDecimal.ZERO;
        if (emission.getReportingPeriod() != null && emission.getReportingPeriod().getCalculationParameterValue() != null) {
            totalEmissions = emission.getEmissionsFactor().multiply(emission.getReportingPeriod().getCalculationParameterValue());

            if (canConvert) {

                // if heat content numerator uom type == ef denominator uom type , and heat content denominator (fuel uom) uom type == throughput uom type
                if (emissionService.checkIfCanConvertUnits(emission.getReportingPeriod().getHeatContentUom(), emission.getEmissionsDenominatorUom())
                    && emissionService.checkIfCanConvertUnits(emission.getReportingPeriod().getFuelUseUom(), emission.getReportingPeriod().getCalculationParameterUom())) {

                    totalEmissions = totalEmissions.multiply(emission.getReportingPeriod().getHeatContentValue());
                    // convert units for throughput to match heat content denominator
                    if (!emission.getReportingPeriod().getFuelUseUom().getCode().equals(emission.getReportingPeriod().getCalculationParameterUom().getCode())) {
                        totalEmissions = CalculationUtils.convertUnits(emission.getReportingPeriod().getCalculationParameterUom().getCalculationVariable(), emission.getReportingPeriod().getFuelUseUom().getCalculationVariable(), leapYear).multiply(totalEmissions);
                    }

                    // convert units for heat content numerator to match ef denominator
                    if (!emission.getEmissionsDenominatorUom().getCode().equals(emission.getReportingPeriod().getHeatContentUom().getCode())) {
                        totalEmissions = CalculationUtils.convertUnits(emission.getReportingPeriod().getHeatContentUom().getCalculationVariable(), emission.getEmissionsDenominatorUom().getCalculationVariable(), leapYear).multiply(totalEmissions);
                    }
                }

                // if heat content denominator uom type == ef denominator uom type, and heat content numerator uom type == throughput uom type
                if (emissionService.checkIfCanConvertUnits(emission.getReportingPeriod().getFuelUseUom(), emission.getEmissionsDenominatorUom())
                    && emissionService.checkIfCanConvertUnits(emission.getReportingPeriod().getHeatContentUom(), emission.getReportingPeriod().getCalculationParameterUom())) {

                    totalEmissions = totalEmissions.divide(emission.getReportingPeriod().getHeatContentValue(), MathContext.DECIMAL128);

                    // convert units for throughput to match heat content numerator
                    if (!emission.getReportingPeriod().getHeatContentUom().getCode().equals(emission.getReportingPeriod().getCalculationParameterUom().getCode())) {
                        totalEmissions = CalculationUtils.convertUnits(emission.getReportingPeriod().getCalculationParameterUom().getCalculationVariable(), emission.getReportingPeriod().getHeatContentUom().getCalculationVariable(), leapYear).multiply(totalEmissions);
                    }

                    // convert units for heat content denominator to match ef denominator
                    if (!emission.getEmissionsDenominatorUom().getCode().equals(emission.getReportingPeriod().getFuelUseUom().getCode())) {
                        totalEmissions = CalculationUtils.convertUnits(emission.getReportingPeriod().getFuelUseUom().getCalculationVariable(), emission.getEmissionsDenominatorUom().getCalculationVariable(), leapYear).multiply(totalEmissions);
                    }
                }

            } else {
                // convert units for denominator and throughput
                if (emissionService.checkIfCanConvertUnits(emission.getReportingPeriod().getCalculationParameterUom(), emission.getEmissionsDenominatorUom())
                    && !emission.getReportingPeriod().getCalculationParameterUom().getCode().equals(emission.getEmissionsDenominatorUom().getCode())) {
                    totalEmissions = CalculationUtils.convertUnits(emission.getReportingPeriod().getCalculationParameterUom().getCalculationVariable(),
                        emission.getEmissionsDenominatorUom().getCalculationVariable(), leapYear).multiply(totalEmissions);
                }
            }

            // convert units for numerator and total emissions
            if (emissionService.checkIfCanConvertUnits(emission.getEmissionsUomCode(), emission.getEmissionsNumeratorUom())
                && !emission.getEmissionsUomCode().getCode().equals(emission.getEmissionsNumeratorUom().getCode())) {
                totalEmissions = CalculationUtils.convertUnits(emission.getEmissionsNumeratorUom().getCalculationVariable(),
                    emission.getEmissionsUomCode().getCalculationVariable(), leapYear).multiply(totalEmissions);
            }

            if (emission.getOverallControlPercent() != null) {
                BigDecimal controlRate = new BigDecimal("100").subtract(emission.getOverallControlPercent()).divide(new BigDecimal("100"));
                totalEmissions = totalEmissions.multiply(controlRate);
            }
        }

        return totalEmissions;
    }

    private boolean checkTolerance(BigDecimal calculatedValue, BigDecimal providedValue, BigDecimal tolerance) {

        return calculatedValue.subtract(providedValue).abs().compareTo(calculatedValue.multiply(tolerance)) > 0;
    }
}
