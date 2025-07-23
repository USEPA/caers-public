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

import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.PointSourceSccCodeRepository;
import gov.epa.cef.web.repository.ReportingPeriodRepository;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.service.impl.EmissionServiceImpl;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.ValidationRegistry;
import gov.epa.cef.web.service.validation.validator.BaseValidator;
import gov.epa.cef.web.util.CalculationUtils;
import gov.epa.cef.web.util.ConstantUtils;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;

@Component
public class AnnualReportingPeriodValidator extends BaseValidator<ReportingPeriod> {

    @Autowired
    private EmissionsReportRepository reportRepo;

    @Autowired
    private ReportingPeriodRepository reportingPeriodRepo;

    @Autowired
    private PointSourceSccCodeRepository sccRepo;

    @Autowired
    private EmissionServiceImpl emissionService;

    @Override
    public void compose(FluentValidator validator,
                        ValidatorContext validatorContext,
                        ReportingPeriod reportingPeriod) {

        ValidationRegistry registry = getCefValidatorContext(validatorContext).getValidationRegistry();

        // add more validators as needed
        validator.onEach(reportingPeriod.getEmissions(),
            registry.findOneByType(AnnualEmissionValidator.class));

        validator.onEach(reportingPeriod.getOperatingDetails(),
            registry.findOneByType(AnnualOperatingDetailValidator.class));
    }

    @Override
    public boolean validate(ValidatorContext validatorContext, ReportingPeriod period) {

        boolean valid = true;

        CefValidatorContext context = getCefValidatorContext(validatorContext);

        if (!ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(period.getEmissionsProcess().getOperatingStatusCode().getCode())
            && !ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN.contentEquals(period.getEmissionsProcess().getOperatingStatusCode().getCode())) {

            if (period.getCalculationParameterValue() == null) {

                valid = false;
                context.addFederalError(
                    ValidationField.PERIOD_CALC_VALUE.value(),
                    "reportingPeriod.calculationParameterValue.required",
                    createValidationDetails(period));

            } else if (period.getCalculationParameterValue().compareTo(BigDecimal.ZERO) < 0) {

                valid = false;
                context.addFederalError(
                    ValidationField.PERIOD_CALC_VALUE.value(),
                    "reportingPeriod.calculationParameterValue.min",
                    createValidationDetails(period));
            }

            PointSourceSccCode sccCode = sccRepo.findById(period.getEmissionsProcess().getSccCode()).orElse(null);
            ReportStatus semiAnnualSubmissionStatus = period.getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport().getMidYearSubmissionStatus();
            if ((semiAnnualSubmissionStatus == ReportStatus.SUBMITTED || semiAnnualSubmissionStatus == ReportStatus.APPROVED)
                && sccCode != null && sccCode.getMonthlyReporting()) {
                List<String> newPollutants = emissionService.getEmissionsCreatedAfterSemiannualSubmission(period.getEmissionsProcess().getId(),
                    period.getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport().getId());

                for (Emission e : period.getEmissions()) {
                    if (newPollutants.contains(e.getPollutant().getPollutantCode())) {
                        valid = false;
                        context.addFederalWarning(
                            ValidationField.PERIOD_EMISSION.value(),
                            "emission.newAfterSemiAnnualSubmission",
                            createValidationDetails(period),
                            e.getPollutant().getPollutantName());
                    }
                }
            }

            if ( period.getCalculationParameterValue() != null && period.getFuelUseValue() != null &&
                period.getCalculationMaterialCode() != null && period.getFuelUseMaterialCode() != null &&
                period.getCalculationParameterUom() != null && period.getFuelUseUom() != null
            ) {

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
                                emissionService.checkIfCanConvertUnits(
                                    period.getCalculationParameterUom(),
                                    previousPeriod.getCalculationParameterUom())
                            ) {
                                BigDecimal currentThroughput = period.getCalculationParameterUom().getCode().equals(
                                    previousPeriod.getCalculationParameterUom().getCode())
                                    ? period.getCalculationParameterValue()
                                    : CalculationUtils.convertUnits(
                                    period.getCalculationParameterUom().getCalculationVariable(),
                                    previousPeriod.getCalculationParameterUom().getCalculationVariable()
                                ).multiply(period.getCalculationParameterValue());

                                BigDecimal throughputRange = previousPeriod.getCalculationParameterValue()
                                    .multiply(new BigDecimal(".2"));
                                if (
                                    currentThroughput.subtract(throughputRange)
                                        .compareTo(previousPeriod.getCalculationParameterValue()) >= 0
                                ) {
                                    valid = false;
                                    context.addFederalWarning(
                                        ValidationField.PERIOD_CALC_VALUE.value(),
                                        "reportingPeriod.calculationParameterValue.largeChange",
                                        createValidationDetails(period),
                                        "higher");
                                } else if (
                                    currentThroughput.add(throughputRange)
                                        .compareTo(previousPeriod.getCalculationParameterValue()) <= 0
                                ) {
                                    valid = false;
                                    context.addFederalWarning(
                                        ValidationField.PERIOD_CALC_VALUE.value(),
                                        "reportingPeriod.calculationParameterValue.largeChange",
                                        createValidationDetails(period),
                                        "lower");
                                }
                            }
                        }

                        if (
                            previousPeriod.getFuelUseUom() != null
                                && previousPeriod.getFuelUseValue() != null
                        ) {

                            if (
                                emissionService.checkIfCanConvertUnits(
                                    period.getFuelUseUom(),
                                    previousPeriod.getFuelUseUom())
                            ) {
                                BigDecimal currentFuelValue = period.getFuelUseUom().getCode().equals(
                                    previousPeriod.getFuelUseUom().getCode())
                                    ? period.getFuelUseValue()
                                    : CalculationUtils.convertUnits(
                                    period.getFuelUseUom().getCalculationVariable(),
                                    previousPeriod.getFuelUseUom().getCalculationVariable()
                                ).multiply(period.getFuelUseValue());

                                BigDecimal fuelRange = previousPeriod.getFuelUseValue().multiply(new BigDecimal(".2"));
                                if (
                                    currentFuelValue.subtract(fuelRange)
                                        .compareTo(previousPeriod.getFuelUseValue()) >= 0
                                ) {
                                    valid = false;
                                    context.addFederalWarning(
                                        ValidationField.PERIOD_FUEL_VALUE.value(),
                                        "reportingPeriod.fuelUseValue.largeChange",
                                        createValidationDetails(period),
                                        "higher");
                                } else if (
                                    currentFuelValue.add(fuelRange)
                                        .compareTo(previousPeriod.getFuelUseValue()) <= 0
                                ) {
                                    valid = false;
                                    context.addFederalWarning(
                                        ValidationField.PERIOD_FUEL_VALUE.value(),
                                        "reportingPeriod.fuelUseValue.largeChange",
                                        createValidationDetails(period),
                                        "lower");
                                }
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
