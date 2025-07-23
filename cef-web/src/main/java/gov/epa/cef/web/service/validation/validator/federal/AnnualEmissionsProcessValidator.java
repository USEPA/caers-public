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

import gov.epa.cef.web.config.SLTBaseConfig;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.AircraftEngineTypeCodeRepository;
import gov.epa.cef.web.repository.PointSourceSccCodeRepository;
import gov.epa.cef.web.repository.ReportHistoryRepository;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.ValidationRegistry;
import gov.epa.cef.web.service.validation.validator.BaseValidator;
import gov.epa.cef.web.util.ConstantUtils;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import gov.epa.cef.web.util.SLTConfigHelper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.google.common.base.Strings;

@Component
public class AnnualEmissionsProcessValidator extends BaseValidator<EmissionsProcess> {

	@Autowired
	private AircraftEngineTypeCodeRepository aircraftEngCodeRepo;

    @Autowired
    private SLTConfigHelper sltConfigHelper;

    @Autowired
    private PointSourceSccCodeRepository sccRepo;

    @Autowired
    private ReportHistoryRepository historyRepo;

    @Override
    public void compose(FluentValidator validator,
                        ValidatorContext validatorContext,
                        EmissionsProcess emissionsProcess) {

        ValidationRegistry registry = getCefValidatorContext(validatorContext).getValidationRegistry();

        validator.onEach(emissionsProcess.getReportingPeriods().stream().filter(period -> period.getReportingPeriodTypeCode().getShortName().equals(ConstantUtils.ANNUAL)).collect(Collectors.toList()),
            registry.findOneByType(AnnualReportingPeriodValidator.class));

        // if monthly reporting is enabled, need to validate the monthly data for July-December as part of annual report
        SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(emissionsProcess.getEmissionsUnit().getFacilitySite().getProgramSystemCode().getCode());
        Short monthlyInitialYear = sltConfig.getSltMonthlyFuelReportingInitialYear();

        if (Boolean.TRUE.equals(sltConfig.getSltMonthlyFuelReportingEnabled())) {
            validator.onEach(emissionsProcess.getReportingPeriods().stream()
                    .filter(period -> (monthlyInitialYear != null && emissionsProcess.getEmissionsUnit().getFacilitySite().getEmissionsReport().getYear() >= monthlyInitialYear)
                                        && ConstantUtils.SECOND_HALF_MONTHS.contains(period.getReportingPeriodTypeCode().getShortName()))
                    .collect(Collectors.toList()),
                registry.findOneByType(MonthlyReportingPeriodValidator.class));
        }
    }

    @Override
    public boolean validate(ValidatorContext validatorContext, EmissionsProcess emissionsProcess) {

        boolean result = true;

        CefValidatorContext context = getCefValidatorContext(validatorContext);

        boolean isProcessOperating = ConstantUtils.STATUS_OPERATING.contentEquals(emissionsProcess.getOperatingStatusCode().getCode());

        BigDecimal totalReleasePointPercent = emissionsProcess.getReleasePointAppts().stream()
            .map(ReleasePointAppt::getPercent)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        Map<Object, List<ReleasePointAppt>> rpaMap = emissionsProcess.getReleasePointAppts().stream()
            .filter(rpa -> rpa.getReleasePoint() != null)
            .collect(Collectors.groupingBy(e -> e.getReleasePoint().getId()));

        for (List<ReleasePointAppt> rpa : rpaMap.values()) {
            // release point can be used only once per rp appt collection
            if (rpa.size() > 1) {

                result = false;
                context.addFederalError(
                    ValidationField.PROCESS_RP.value(),
                    "emissionsProcess.releasePointAppts.duplicate",
                    createValidationDetails(emissionsProcess),
                    rpa.get(0).getReleasePoint().getReleasePointIdentifier());
            }

            // If release point operation is apportioned to a process, operation status must be operating
            if (isProcessOperating && !ConstantUtils.STATUS_OPERATING.contentEquals(rpa.get(0).getReleasePoint().getOperatingStatusCode().getCode())) {

                result = false;
                context.addFederalError(
                    ValidationField.PROCESS_RP.value(),
                    "emissionsProcess.releasePointAppts.statusTypeCode",
                    createValidationDetails(emissionsProcess),
                    rpa.get(0).getReleasePoint().getReleasePointIdentifier(),
                    rpa.get(0).getReleasePoint().getOperatingStatusCode().getDescription());
            }
        }

        // Release Point Apportionments Emission Percentage for the process must be between 1 and 100.
        if (emissionsProcess.getReleasePointAppts() != null) {
            for (ReleasePointAppt rpa : emissionsProcess.getReleasePointAppts()) {
                if (rpa.getPercent().compareTo(BigDecimal.ONE) == -1 || rpa.getPercent().compareTo(BigDecimal.valueOf(100)) == 1) {
                    result = false;
                    context.addFederalError(
                        ValidationField.PROCESS_RP_PCT.value(),
                        "emissionsProcess.releasePointAppts.percent.range",
                        createValidationDetails(emissionsProcess),
                        rpa.getReleasePoint().getReleasePointIdentifier());
                }
            }
        }

        if (isProcessOperating) {
            if (totalReleasePointPercent.compareTo(BigDecimal.valueOf(100)) != 0) {

                result = false;
                context.addFederalError(
                    ValidationField.PROCESS_RP_PCT.value(),
                    "emissionsProcess.releasePointAppts.percent.total",
                    createValidationDetails(emissionsProcess));
            }

            // Process must go to at least one release point
            if (CollectionUtils.sizeIsEmpty(rpaMap)) {

                result = false;
                context.addFederalError(
                    ValidationField.PROCESS_RP.value(),
                    "emissionsProcess.releasePointAppts.required",
                    createValidationDetails(emissionsProcess));
            }

            // Check for valid SCC Code
            if (Strings.emptyToNull(emissionsProcess.getSccCode()) != null) {
                List<String> aircraftEngineScc = new ArrayList<String>();
                Collections.addAll(aircraftEngineScc, "2275001000", "2275020000", "2275050011", "2275050012", "2275060011", "2275060012");

                // if SCC is an aircraft engine type, then aircraft engine code is required
                for (String code : aircraftEngineScc) {
                    if (code.contentEquals(emissionsProcess.getSccCode()) && emissionsProcess.getAircraftEngineTypeCode() == null) {

                        result = false;
                        context.addFederalError(
                            ValidationField.PROCESS_AIRCRAFT_CODE.value(),
                            "emissionsProcess.aircraftCode.required",
                            createValidationDetails(emissionsProcess));
                    }
                }
            }

            // Check for unique SCC and AircraftEngineType code combination within a facility site
            if ((emissionsProcess.getSccCode() != null) && (emissionsProcess.getAircraftEngineTypeCode() != null)) {
                String testingCombination = emissionsProcess.getAircraftEngineTypeCode().getCode() + emissionsProcess.getSccCode();
                List<String> combinationList = new ArrayList<String>();
                for (EmissionsUnit eu : emissionsProcess.getEmissionsUnit().getFacilitySite().getEmissionsUnits()) {
                    if (eu.getEmissionsProcesses() != null) {
                        for (EmissionsProcess ep : eu.getEmissionsProcesses()) {
                            if ((ep.getSccCode() != null) && (ep.getAircraftEngineTypeCode() != null) && (!ep.getId().equals(emissionsProcess.getId()))) {
                                String combination = ep.getAircraftEngineTypeCode().getCode() + ep.getSccCode();
                                combinationList.add(combination);
                            }
                        }
                    }
                }
                for (String combination : combinationList) {
                    if (combination.equals(testingCombination)) {
                        context.addFederalError(
                            ValidationField.PROCESS_AIRCRAFT_CODE_AND_SCC_CODE.value(),
                            "emissionsProcess.aircraftCodeAndSccCombination.duplicate",
                            createValidationDetails(emissionsProcess));
                        result = false;
                    }
                }
            }

            // aircraft engine code must match assigned SCC
            if (emissionsProcess.getSccCode() != null && emissionsProcess.getAircraftEngineTypeCode() != null) {
                AircraftEngineTypeCode sccHasEngineType = aircraftEngCodeRepo.findById(emissionsProcess.getAircraftEngineTypeCode().getCode()).orElse(null);

                if (sccHasEngineType != null && !emissionsProcess.getSccCode().contentEquals(sccHasEngineType.getScc())) {

                    result = false;
                    context.addFederalError(
                        ValidationField.PROCESS_AIRCRAFT_CODE.value(),
                        "emissionsProcess.aircraftCode.valid",
                        createValidationDetails(emissionsProcess));
                }
            }

            if (emissionsProcess.getAircraftEngineTypeCode() != null && emissionsProcess.getAircraftEngineTypeCode().getLastInventoryYear() != null
                && emissionsProcess.getAircraftEngineTypeCode().getLastInventoryYear() < getReportYear(emissionsProcess)) {

                result = false;
                context.addFederalError(
                    ValidationField.PROCESS_AIRCRAFT_CODE.value(),
                    "emissionsProcess.aircraftCode.legacy",
                    createValidationDetails(emissionsProcess),
                    emissionsProcess.getAircraftEngineTypeCode().getFaaAircraftType(),
                    emissionsProcess.getAircraftEngineTypeCode().getEngine());
            }
        }

        PointSourceSccCode sccCode = sccRepo.findById(emissionsProcess.getSccCode()).orElse(null);
        ReportStatus semiAnnualSubmissionStatus = emissionsProcess.getEmissionsUnit().getFacilitySite().getEmissionsReport().getMidYearSubmissionStatus();
        if ((semiAnnualSubmissionStatus == ReportStatus.SUBMITTED || semiAnnualSubmissionStatus == ReportStatus.APPROVED)
            && sccCode != null && sccCode.getMonthlyReporting()) {

            Date semiannualSubDate = historyRepo.retrieveMaxSemiannualSubmissionDateByReportId(emissionsProcess.getEmissionsUnit().getFacilitySite().getEmissionsReport().getId()).orElse(null);
            if (semiannualSubDate != null && emissionsProcess.getCreatedDate() != null && emissionsProcess.getCreatedDate().after(semiannualSubDate)) {
                result = false;
                context.addFederalWarning(
                    ValidationField.EMISSIONS_UNIT_PROCESS.value(),
                    "emissionsProcess.newAfterSemiAnnualSubmission",
                    createValidationDetails(emissionsProcess));
            }
        }

        SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(emissionsProcess.getEmissionsUnit().getFacilitySite().getProgramSystemCode().getCode());
        if (sltConfig.getSltFeatureBillingExemptEnabled() && Boolean.TRUE.equals(emissionsProcess.getSltBillingExempt())
            && (Strings.isNullOrEmpty(emissionsProcess.getComments()))) {

            result = false;
            context.addFederalError(
                ValidationField.PROCESS_COMMENTS.value(),
                "emissionsProcess.comments.required.billingExempt",
                createValidationDetails(emissionsProcess));
        }

        return result;
    }

    private String getEmissionsUnitIdentifier(EmissionsProcess process) {
        if (process.getEmissionsUnit() != null) {
            return process.getEmissionsUnit().getUnitIdentifier();
        }
        return null;
    }

    private int getReportYear(EmissionsProcess process) {
        return process.getEmissionsUnit().getFacilitySite().getEmissionsReport().getYear().intValue();
    }

    private ValidationDetailDto createValidationDetails(EmissionsProcess source) {

        String description = MessageFormat.format("Emissions Unit: {0}, Emissions Process: {1}",
                getEmissionsUnitIdentifier(source),
                source.getEmissionsProcessIdentifier());

        return new ValidationDetailDto(source.getId(), source.getEmissionsProcessIdentifier(), EntityType.EMISSIONS_PROCESS, description);
    }
}
