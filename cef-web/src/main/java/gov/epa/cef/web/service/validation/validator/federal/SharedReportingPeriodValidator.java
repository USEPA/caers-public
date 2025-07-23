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
package gov.epa.cef.web.service.validation.validator.federal;

import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import gov.epa.cef.web.config.SLTBaseConfig;
import gov.epa.cef.web.domain.Emission;
import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.domain.PointSourceSccCode;
import gov.epa.cef.web.domain.ReportingPeriod;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.PointSourceSccCodeRepository;
import gov.epa.cef.web.repository.ReportingPeriodRepository;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.ValidationRegistry;
import gov.epa.cef.web.service.validation.validator.BaseValidator;
import gov.epa.cef.web.util.ConstantUtils;
import gov.epa.cef.web.util.SLTConfigHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SharedReportingPeriodValidator extends BaseValidator<ReportingPeriod> {
    private static final String PMFIL = "PM-FIL";
    private static final String PM10FIL = "PM10-FIL";
    private static final String PM10PRI = "PM10-PRI";
    private static final String PM25FIL = "PM25-FIL";
    private static final String PM25PRI = "PM25-PRI";
    private static final String PMCON = "PM-CON";
    private static final String NMOC = "NMOC";
    private static final String VOC = "VOC";
    private static final String FLUORIDES = "16984488";
    private static final String HF = "7664393";
    private static final List<String> PM_ERRORS_STATES =
        Collections.unmodifiableList(Arrays.asList("GADNR", "IDDEQ", "MEDEP"));
    private static final List<String> PM_ERRORS_SUBSET_STATES =
        Collections.unmodifiableList(Arrays.asList("IDDEQ", "MEDEP"));
    private static final List<String> PM_WARNINGS_TWO_OF_THREE_STATES =
        Collections.unmodifiableList(Arrays.asList("GADNR"));

    @Autowired
    private EmissionsReportRepository reportRepo;

    @Autowired
    private ReportingPeriodRepository reportingPeriodRepo;

    @Autowired
    private PointSourceSccCodeRepository sccRepo;

    @Autowired
    private SLTConfigHelper sltConfigHelper;

    @Override
    public void compose(FluentValidator validator,
                        ValidatorContext validatorContext,
                        ReportingPeriod reportingPeriod) {

        ValidationRegistry registry = getCefValidatorContext(validatorContext).getValidationRegistry();

        // add more validators as needed
        validator.onEach(reportingPeriod.getEmissions(),
            registry.findOneByType(SharedEmissionValidator.class));

        validator.onEach(reportingPeriod.getOperatingDetails(),
            registry.findOneByType(SharedOperatingDetailValidator.class));
    }

    private boolean validatePMFILPollutant(Map<String, List<Emission>> emissionMap,ReportingPeriod period, boolean valid, CefValidatorContext context){
        if(emissionMap.containsKey(PMFIL)){
            boolean containsAtLeastOnePMFIL = emissionMap.containsKey(PM10FIL)
                || emissionMap.containsKey(PM10PRI)
                || emissionMap.containsKey(PM25FIL)
                || emissionMap.containsKey(PM25PRI);
            if(!containsAtLeastOnePMFIL){
                valid = false;
                context.addFederalError(
                    ValidationField.PERIOD_EMISSION.value(),
                    "reportingPeriod.emission.pollutantPMFIL",
                    createValidationDetails(period)
                );
            }
        }
        return valid;
    }

    private boolean validatePMCONPollutant(Map<String, List<Emission>> emissionMap,ReportingPeriod period, boolean valid, CefValidatorContext context){
        if(emissionMap.containsKey(PMCON)) {
            boolean containsAtLeastOnePMCON = emissionMap.containsKey(PM10FIL)
                || emissionMap.containsKey(PM10PRI)
                || emissionMap.containsKey(PM25FIL)
                || emissionMap.containsKey(PM25PRI);
            if(!containsAtLeastOnePMCON){
                valid=false;
                context.addFederalError(
                    ValidationField.PERIOD_EMISSION.value(),
                    "reportingPeriod.emission.pollutantCON",
                    createValidationDetails(period)
                );
            }
        }
        return valid;
    }

    private boolean validateNMOCPollutant(Map<String, List<Emission>> emissionMap, ReportingPeriod period, boolean valid, CefValidatorContext context){
        if(emissionMap.containsKey(NMOC)){
            boolean containsVOC = emissionMap.containsKey(VOC);
            if(!containsVOC) {
                valid = false;
                context.addFederalError(
                    ValidationField.PERIOD_EMISSION.value(),
                    "reportingPeriod.emission.pollutantNMOC",
                    createValidationDetails(period)
                );
            }
        }
        return valid;
    }

    @Override
    public boolean validate(ValidatorContext validatorContext, ReportingPeriod period) {

        boolean valid = true;

        CefValidatorContext context = getCefValidatorContext(validatorContext);

        if (!ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(period.getEmissionsProcess().getOperatingStatusCode().getCode())
            && !ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN.contentEquals(period.getEmissionsProcess().getOperatingStatusCode().getCode())) {

            if (period.getEmissionsOperatingTypeCode() == null) {

                valid = false;
                context.addFederalError(
                    ValidationField.PERIOD_OPERATING_TYPE_CODE.value(),
                    "reportingPeriod.operatingTypeCode.required",
                    createValidationDetails(period));
            }

            if (period.getCalculationMaterialCode() == null) {

                valid = false;
                context.addFederalError(
                    ValidationField.PERIOD_CALC_MAT_CODE.value(),
                    "reportingPeriod.calculationMaterialCode.required",
                    createValidationDetails(period));
            }

            if (period.getCalculationParameterTypeCode() == null) {

                valid = false;
                context.addFederalError(
                    ValidationField.PERIOD_CALC_TYPE_CODE.value(),
                    "reportingPeriod.calculationParameterTypeCode.required",
                    createValidationDetails(period));
            }

            if (period.getCalculationParameterUom() == null) {

                valid = false;
                context.addFederalError(
                    ValidationField.PERIOD_CALC_UOM.value(),
                    "reportingPeriod.calculationParameterUom.required",
                    createValidationDetails(period));

            } else if (Boolean.TRUE.equals(period.getCalculationParameterUom().getLegacy())) {

                valid = false;
                context.addFederalError(
                    ValidationField.PERIOD_CALC_UOM.value(),
                    "reportingPeriod.calculationParameterUom.legacy",
                    createValidationDetails(period),
                    period.getCalculationParameterUom().getDescription());
            }



            Map<String, List<Emission>> emissionMap = period.getEmissions().stream()
                .filter(e -> e.getPollutant() != null)
                .collect(Collectors.groupingBy(e -> e.getPollutant().getPollutantCode()));

            for (List<Emission> emissions: emissionMap.values()) {

                if (emissions.size() > 1) {

                    valid = false;
                    context.addFederalError(
                        ValidationField.PERIOD_EMISSION.value(),
                        "reportingPeriod.emission.duplicate",
                        createValidationDetails(period),
                        emissions.get(0).getPollutant().getPollutantName());
                }
            }

            valid = validatePMFILPollutant(emissionMap, period, valid, context);
            valid = validatePMCONPollutant(emissionMap, period, valid, context);
            valid = validateNMOCPollutant(emissionMap, period, valid, context);

            // start batch of PM validations
            if (emissionMap.containsKey(PM10FIL) || emissionMap.containsKey(PM10PRI)
                || emissionMap.containsKey(PM25FIL) || emissionMap.containsKey(PM25PRI)
                || emissionMap.containsKey(PMCON)) {

                // get the values of all the pm emissions
                BigDecimal pm10Fil = emissionMap.containsKey(PM10FIL) ? emissionMap.get(PM10FIL).get(0).getCalculatedEmissionsTons() : null;
                BigDecimal pm10Pri = emissionMap.containsKey(PM10PRI) ? emissionMap.get(PM10PRI).get(0).getCalculatedEmissionsTons() : null;
                BigDecimal pm25Fil = emissionMap.containsKey(PM25FIL) ? emissionMap.get(PM25FIL).get(0).getCalculatedEmissionsTons() : null;
                BigDecimal pm25Pri = emissionMap.containsKey(PM25PRI) ? emissionMap.get(PM25PRI).get(0).getCalculatedEmissionsTons() : null;
                BigDecimal pmCon = emissionMap.containsKey(PMCON) ? emissionMap.get(PMCON).get(0).getCalculatedEmissionsTons() : null;

                // PM10 Filterable should not exceed PM10 Primary
                if (pm10Fil != null && pm10Pri != null && pm10Fil.compareTo(pm10Pri) > 0) {

                    valid = false;
                    context.addFederalError(
                        ValidationField.PERIOD_EMISSION.value(),
                        "reportingPeriod.emission.pm10.fil.greater.pri",
                        createValidationDetails(period));
                }

                // PM2.5 Filterable should not exceed PM2.5 Primary
                if (pm25Fil != null && pm25Pri != null && pm25Fil.compareTo(pm25Pri) > 0) {

                    valid = false;
                    context.addFederalError(
                        ValidationField.PERIOD_EMISSION.value(),
                        "reportingPeriod.emission.pm25.fil.greater.pri",
                        createValidationDetails(period));
                }

                // PM Condensable should not exceed PM10 Primary
                if (pmCon != null && pm10Pri != null && pmCon.compareTo(pm10Pri) > 0) {

                    valid = false;
                    context.addFederalError(
                        ValidationField.PERIOD_EMISSION.value(),
                        "reportingPeriod.emission.pm10.con.greater.pri",
                        createValidationDetails(period));
                }

                // PM Condensable should not exceed PM2.5 Primary
                if (pmCon != null && pm25Pri != null && pmCon.compareTo(pm25Pri) > 0) {

                    valid = false;
                    context.addFederalError(
                        ValidationField.PERIOD_EMISSION.value(),
                        "reportingPeriod.emission.pm25.con.greater.pri",
                        createValidationDetails(period));
                }

                // PM10-FIL + PM-CON must equal PM10-PRI
                BigDecimal negative1 = new BigDecimal(-1);
                if (pmCon != null && pm10Fil != null && pm10Pri != null) {

                    final BigDecimal pm10Calc = pmCon.add(pm10Fil).subtract(pm10Pri);
                    if (pm10Calc.compareTo(BigDecimal.ONE) > 0 || pm10Calc.compareTo(negative1) < 0) {

                        valid = false;
                        context.addFederalError(
                            ValidationField.PERIOD_EMISSION.value(),
                            "reportingPeriod.emission.pm10.invalid",
                            createValidationDetails(period));
                    }
                }

                // PM25-FIL + PM-CON must equal PM25-PRI
                if (pmCon != null && pm25Fil != null && pm25Pri != null) {

                    final BigDecimal pm25Calc = pmCon.add(pm25Fil).subtract(pm25Pri);
                    if (pm25Calc.compareTo(BigDecimal.ONE) > 0 || pm25Calc.compareTo(negative1) < 0) {

                        valid = false;
                        context.addFederalError(
                            ValidationField.PERIOD_EMISSION.value(),
                            "reportingPeriod.emission.pm25.invalid",
                            createValidationDetails(period));
                    }
                }

                // PM2.5 Primary should not exceed PM10 Primary
                if (pm10Pri != null && pm25Pri != null && pm25Pri.compareTo(pm10Pri) > 0) {

                    valid = false;
                    context.addFederalError(
                        ValidationField.PERIOD_EMISSION.value(),
                        "reportingPeriod.emission.pm25.pri.greater.pm10",
                        createValidationDetails(period));
                }

                // PM2.5 Filterable should not exceed PM10 Filterable
                if (pm10Fil != null && pm25Fil != null && pm25Fil.compareTo(pm10Fil) > 0) {

                    valid = false;
                    context.addFederalError(
                        ValidationField.PERIOD_EMISSION.value(),
                        "reportingPeriod.emission.pm25.fil.greater.pm10",
                        createValidationDetails(period));
                }

                String psc = period.getEmissionsProcess().getEmissionsUnit().getFacilitySite().getProgramSystemCode().getCode();
                if (PM_ERRORS_STATES.contains(psc)) {
                    if (pm10Fil != null && pm25Fil == null) {

                        valid = false;
                        context.addFederalWarning(
                            ValidationField.PERIOD_EMISSION.value(),
                            "reportingPeriod.emission.pm10.fil.andNot.pm25.fil",
                            createValidationDetails(period));
                    }

                    if (pm10Pri != null && pm25Pri == null) {

                        valid = false;
                        context.addFederalWarning(
                            ValidationField.PERIOD_EMISSION.value(),
                            "reportingPeriod.emission.pm10.pri.andNot.pm25.pri",
                            createValidationDetails(period));
                    }
                }
                if (PM_ERRORS_SUBSET_STATES.contains(psc)) {

                    if (pm10Fil != null && pm25Pri != null) {

                        valid = false;
                        context.addFederalWarning(
                            ValidationField.PERIOD_EMISSION.value(),
                            "reportingPeriod.emission.pm10.fil.and.pm25.pri",
                            createValidationDetails(period));
                    }

                    if (pm10Pri != null && pm25Fil != null) {

                        valid = false;
                        context.addFederalWarning(
                            ValidationField.PERIOD_EMISSION.value(),
                            "reportingPeriod.emission.pm10.pri.and.pm25.fil",
                            createValidationDetails(period));
                    }
                }
                if (PM_WARNINGS_TWO_OF_THREE_STATES.contains(psc)) {
                    //if already have con, it already counts as one of the 3 for both scenarios so warnings won't trigger
                    if (pmCon == null) {
                        if (Boolean.logicalXor(pm10Pri != null, pm10Fil != null)) {

                            valid = false;
                            context.addFederalWarning(
                                ValidationField.PERIOD_EMISSION.value(),
                                "reportingPeriod.emission.pm10.twoOfThree",
                                createValidationDetails(period));

                        }

                        if (Boolean.logicalXor(pm25Pri != null, pm25Fil != null)) {

                            valid = false;
                            context.addFederalWarning(
                                ValidationField.PERIOD_EMISSION.value(),
                                "reportingPeriod.emission.pm25.twoOfThree",
                                createValidationDetails(period));

                        }
                    }

                }

            }

            // Fluorides/16984488 value must be greater than or equal to HF/7664393; Flourides is not currently in the db so validation cannot be triggered
            if (emissionMap.containsKey(FLUORIDES) && emissionMap.containsKey(HF)
                && emissionMap.get(HF).get(0).getCalculatedEmissionsTons().compareTo(emissionMap.get(FLUORIDES).get(0).getCalculatedEmissionsTons()) > 0) {
                valid = false;
                context.addFederalError(
                    ValidationField.PERIOD_EMISSION.value(),
                    "reportingPeriod.emission.hf.greater.fluorides",
                    createValidationDetails(period));
            }

            // check fuel material and fuel UoM for selected SCC
            PointSourceSccCode scc = null;
            if (period.getEmissionsProcess().getSccCode() != null) {
                scc = sccRepo.findById(period.getEmissionsProcess().getSccCode()).orElse(null);
            }

            if (scc != null) {
                Boolean fuelDataRequired = scc.getFuelUseRequired();

                SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(period.getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport().getProgramSystemCode().getCode());

                // asphalt SCCs that require fuel use data for monthly reporting are included in QA when monthly reporting is enabled
                Short monthlyInitialYear = sltConfig.getSltMonthlyFuelReportingInitialYear();
                if (Boolean.TRUE.equals(sltConfig.getSltMonthlyFuelReportingEnabled())
                    && (monthlyInitialYear == null || period.getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport().getYear() >= monthlyInitialYear)) {
                    fuelDataRequired = scc.getMonthlyReporting();
                }

                // check if selected fuel material matches monthly/fuel use required SCC fuel material
                if (fuelDataRequired) {
                    if (scc != null && scc.getCalculationMaterialCode() != null) {
                        if ((period.getFuelUseMaterialCode() != null && !scc.getCalculationMaterialCode().getCode().contentEquals(period.getFuelUseMaterialCode().getCode()))) {

                            valid = false;
                            context.addFederalError(
                                ValidationField.PERIOD_SCC_FUEL_MATERIAL.value(),
                                "reportingPeriod.fuelUseMaterial.required",
                                createValidationDetails(period),
                                period.getFuelUseMaterialCode().getDescription(),
                                scc.getCode());
                        }

                        // if SCC requires fuel use, check fuel uom for selected fuel material
                        String[] fuelState = {};

                        // retrieve fuel types for SCCs that require fuel use; asphalt SCCs for monthly reporting do not have associated fuel types
                        if (scc.getFuelUseRequired()) {
                            fuelState = scc.getFuelUseTypes().split(",");
                        }

                        if (period.getFuelUseUom() != null) {
                            // if SCC requires fuel use check if selected UoM is fuel use type and if UoM fuel type matches SCC fuel type
                            // asphalt SCCs for monthly reporting do not have associated fuel types, check if selected UoM is fuel use type
                            if (((scc.getFuelUseRequired() && (period.getFuelUseUom().getFuelUseType() == null || !Arrays.asList(fuelState).contains(period.getFuelUseUom().getFuelUseType())))
                                || (scc.getMonthlyReporting() && period.getFuelUseUom().getFuelUseType() == null)) && period.getFuelUseMaterialCode() != null) {

                                valid = false;
                                context.addFederalError(
                                    ValidationField.PERIOD_SCC_FUEL_MATERIAL.value(),
                                    "reportingPeriod.fuelUseMaterial.uom",
                                    createValidationDetails(period),
                                    period.getFuelUseUom().getDescription(),
                                    period.getFuelUseMaterialCode().getDescription());
                            }
                        }

                        if (scc.getFuelUseRequired() && period.getHeatContentUom() != null && !Arrays.asList(fuelState).contains(period.getHeatContentUom().getFuelUseType())) {

                            valid = false;
                            context.addFederalError(
                                ValidationField.PERIOD_HEAT_CONTENT_UOM.value(),
                                "reportingPeriod.heatContentUom.uom",
                                createValidationDetails(period),
                                period.getHeatContentUom().getDescription());
                        }
                    }
                }

                if (period.getHeatContentUom() != null && period.getFuelUseUom() != null
                    && period.getFuelUseUom().getUnitType().equals(period.getHeatContentUom().getUnitType())) {

                    valid = false;
                    context.addFederalError(
                        ValidationField.PERIOD_HEAT_CONTENT_UOM.value(),
                        "reportingPeriod.heatContentUom.invalid",
                        createValidationDetails(period));
                }
            }

            if (period.getFuelUseUom() != null && Boolean.TRUE.equals(period.getFuelUseUom().getLegacy())) {

                valid = false;
                context.addFederalError(
                    ValidationField.PERIOD_FUEL_UOM.value(),
                    "reportingPeriod.fuelUseUom.legacy",
                    createValidationDetails(period),
                    period.getFuelUseUom().getDescription());
            }

            if (period.getHeatContentUom() != null && Boolean.TRUE.equals(period.getHeatContentUom().getLegacy())) {

                valid = false;
                context.addFederalError(
                    ValidationField.PERIOD_HEAT_CONTENT_UOM.value(),
                    "reportingPeriod.heatContentUom.legacy",
                    createValidationDetails(period),
                    period.getHeatContentUom().getDescription());
            }

            if (period.getCalculationParameterValue() != null && period.getFuelUseValue() != null &&
                period.getCalculationMaterialCode() != null && period.getFuelUseMaterialCode() != null &&
                period.getCalculationParameterUom() != null && period.getFuelUseUom() != null
            ) {
                if (period.getCalculationParameterValue().compareTo(period.getFuelUseValue()) != 0 &&
                    period.getCalculationMaterialCode().getCode().equals(period.getFuelUseMaterialCode().getCode()) &&
                    period.getCalculationParameterUom().getCode().equals(period.getFuelUseUom().getCode())
                ) {
                    valid = false;
                    context.addFederalWarning(
                        ValidationField.PERIOD_CALC_VALUE.value(),
                        "reportingPeriod.calculationParameterValue.fuelUseValue.notEqual",
                        createValidationDetails(period));
                } else if (
                    period.getCalculationParameterValue().compareTo(period.getFuelUseValue()) == 0 &&
                        !period.getCalculationMaterialCode().getCode().equals(period.getFuelUseMaterialCode().getCode()) &&
                        period.getCalculationParameterUom().getCode().equals(period.getFuelUseUom().getCode())
                ) {
                    valid = false;
                    context.addFederalWarning(
                        ValidationField.PERIOD_CALC_MAT_CODE.value(),
                        "reportingPeriod.calculationMaterialCode.fuelUseMaterial.notEqual",
                        createValidationDetails(period));
                } else if (
                    !period.getCalculationParameterUom().getLegacy() && !period.getFuelUseUom().getLegacy() &&
                        period.getCalculationParameterValue().compareTo(period.getFuelUseValue()) == 0 &&
                        period.getCalculationMaterialCode().getCode().equals(period.getFuelUseMaterialCode().getCode()) &&
                        !period.getCalculationParameterUom().getCode().equals(period.getFuelUseUom().getCode())
                ) {
                    valid = false;
                    context.addFederalWarning(
                        ValidationField.PERIOD_CALC_UOM.value(),
                        "reportingPeriod.calculationParameterUom.fuelUseUom.notEqual",
                        createValidationDetails(period));
                }

                EmissionsReport currentReport = period.getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport();
                // find previous report
                List<EmissionsReport> previousERList = reportRepo.findByMasterFacilityRecordId(currentReport.getMasterFacilityRecord().getId()).stream()
                    .filter(var -> (var.getYear() != null && var.getYear() < currentReport.getYear()))
                    .sorted(Comparator.comparing(EmissionsReport::getYear))
                    .collect(Collectors.toList());

                if (!previousERList.isEmpty()) {
                    List<ReportingPeriod> previousPeriods = reportingPeriodRepo.retrieveByTypeIdentifierParentFacilityYear(
                            period.getReportingPeriodTypeCode().getCode(),
                            period.getEmissionsProcess().getEmissionsProcessIdentifier(),
                            period.getEmissionsProcess().getEmissionsUnit().getUnitIdentifier(),
                            currentReport.getMasterFacilityRecord().getId(),
                            previousERList.get(previousERList.size()-1).getYear()
                        ).stream()
                        .filter(var -> (
                            var.getReportingPeriodTypeCode() != null &&
                                var.getReportingPeriodTypeCode().getShortName().equals(ConstantUtils.ANNUAL)
                        )).collect(Collectors.toList());

                    if (!previousPeriods.isEmpty()) {
                        ReportingPeriod previousPeriod = previousPeriods.get(0);

                        if (
                            previousPeriod.getCalculationParameterUom() != null
                                && previousPeriod.getCalculationParameterValue() != null
                        ) {

                            if (
                                previousPeriod.getCalculationMaterialCode() != null
                                    && previousPeriod.getCalculationMaterialCode().getCode().equals(
                                    period.getCalculationMaterialCode().getCode()
                                ) &&
                                    !previousPeriod.getCalculationParameterUom().getCode().equals(
                                        period.getCalculationParameterUom().getCode()
                                    )
                            ) {
                                valid = false;
                                context.addFederalWarning(
                                    ValidationField.PERIOD_CALC_UOM.value(),
                                    "reportingPeriod.calculationParameterUom.changed",
                                    createValidationDetails(period));
                            }
                        }

                        if (
                            previousPeriod.getFuelUseUom() != null
                                && previousPeriod.getFuelUseValue() != null
                        ) {

                            if ( // Null checks necessary - see QA for only some fuel values
                                previousPeriod.getFuelUseMaterialCode() != null &&
                                    previousPeriod.getFuelUseMaterialCode().getCode().equals(
                                        period.getFuelUseMaterialCode().getCode()
                                    ) &&
                                    !previousPeriod.getFuelUseUom().getCode().equals(
                                        period.getFuelUseUom().getCode()
                                    )
                            ) {
                                valid = false;
                                context.addFederalWarning(
                                    ValidationField.PERIOD_FUEL_UOM.value(),
                                    "reportingPeriod.fuelUseUom.changed",
                                    createValidationDetails(period));
                            }
                        }
                    }
                }
            }
        }

        return valid;
    }

    private String getEmissionsUnitIdentifier(ReportingPeriod period) {
        if (period.getEmissionsProcess() != null && period.getEmissionsProcess().getEmissionsUnit() != null) {
            return period.getEmissionsProcess().getEmissionsUnit().getUnitIdentifier();
        }
        return null;
    }

    private String getEmissionsProcessIdentifier(ReportingPeriod period) {
        if (period.getEmissionsProcess() != null) {
            return period.getEmissionsProcess().getEmissionsProcessIdentifier();
        }
        return null;
    }

    private ValidationDetailDto createValidationDetails(ReportingPeriod source) {

        String description = MessageFormat.format("Emissions Unit: {0}, Emissions Process: {1}",
            getEmissionsUnitIdentifier(source),
            getEmissionsProcessIdentifier(source));

        ValidationDetailDto dto = new ValidationDetailDto(source.getId(), getEmissionsProcessIdentifier(source), EntityType.REPORTING_PERIOD, description);
        if (source.getEmissionsProcess() != null) {
            dto.getParents().add(new ValidationDetailDto(
                source.getEmissionsProcess().getId(),
                source.getEmissionsProcess().getEmissionsProcessIdentifier(),
                EntityType.EMISSIONS_PROCESS));
        }
        return dto;
    }
}
