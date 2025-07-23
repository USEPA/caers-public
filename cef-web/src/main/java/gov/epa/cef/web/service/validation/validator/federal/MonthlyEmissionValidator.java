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

import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.PointSourceSccCodeRepository;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.BaseValidator;
import gov.epa.cef.web.util.ConstantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.MessageFormat;

@Component
public class MonthlyEmissionValidator extends BaseValidator<Emission> {

    @Autowired
    private PointSourceSccCodeRepository sccRepo;

    @Override
    public boolean validate(ValidatorContext validatorContext, Emission emission) {

        boolean valid = true;

        CefValidatorContext context = getCefValidatorContext(validatorContext);

        PointSourceSccCode sccCode = sccRepo.findById(emission.getReportingPeriod().getEmissionsProcess().getSccCode()).orElse(null);

        if (!emission.getReportingPeriod().getEmissionsProcess().getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN)
                && !emission.getReportingPeriod().getEmissionsProcess().getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN)
                && sccCode != null && sccCode.getMonthlyReporting()) {

            if (emission.getTotalEmissions() == null) {
	            valid = false;
	            context.addFederalError(
	                ValidationField.EMISSION_TOTAL_EMISSIONS.value(),
                    "emission.totalEmissions.monthly.required",
	                createValidationDetails(emission));
            }

            if (ConstantUtils.CEMS_CODE.contentEquals(emission.getEmissionsCalcMethodCode().getCode()) && emission.getMonthlyRate() == null) {
                valid = false;
                context.addFederalError(
                    ValidationField.EMISSION_MONTHLY_RATE.value(),
                    "emission.monthlyRate.monthly.required",
                    createValidationDetails(emission));
            }

            if (emission.getReportingPeriod().getReportingPeriodTypeCode().getShortName().equals(ConstantUtils.SEMIANNUAL)
                && emission.getReportingPeriod().getCalculationParameterValue() != null && emission.getTotalEmissions() != null
                && emission.getReportingPeriod().getCalculationParameterValue().compareTo(BigDecimal.ZERO) > 0
                && emission.getTotalEmissions().compareTo(BigDecimal.ZERO) == 0) {

                valid = false;
                context.addFederalWarning(
                    ValidationField.EMISSION_TOTAL_EMISSIONS.value(),
                    "emission.totalEmissions.nonZero",
                    createValidationDetails(emission));
            }
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

        String description = MessageFormat.format("Emissions Unit: {0}, Emissions Process: {1}, Pollutant: {2}, Month: {3}",
                getEmissionsUnitIdentifier(source),
                getEmissionsProcessIdentifier(source),
                getPollutantName(source),
                source.getReportingPeriod().getReportingPeriodTypeCode().getShortName());

        ValidationDetailDto dto = new ValidationDetailDto(source.getId(), getPollutantName(source), EntityType.EMISSION, description);
        if (source.getReportingPeriod() != null) {
            dto.getParents().add(new ValidationDetailDto(source.getReportingPeriod().getId(), null, EntityType.SEMIANNUAL_EMISSION));
        }
        return dto;
    }
}
