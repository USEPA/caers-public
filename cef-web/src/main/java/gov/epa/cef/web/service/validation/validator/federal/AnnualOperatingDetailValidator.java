/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.validation.validator.federal;

import gov.epa.cef.web.domain.OperatingDetail;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.BaseValidator;
import gov.epa.cef.web.util.ConstantUtils;

import java.math.BigDecimal;
import java.text.MessageFormat;

import org.springframework.stereotype.Component;

import com.baidu.unbiz.fluentvalidator.ValidatorContext;

@Component
public class AnnualOperatingDetailValidator extends BaseValidator<OperatingDetail> {

    @Override
    public boolean validate(ValidatorContext validatorContext, OperatingDetail detail) {

        boolean result = true;

        CefValidatorContext context = getCefValidatorContext(validatorContext);

        if (!ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(detail.getReportingPeriod().getEmissionsProcess().getOperatingStatusCode().getCode())
            && !ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN.contentEquals(detail.getReportingPeriod().getEmissionsProcess().getOperatingStatusCode().getCode())) {

            if (detail.getAvgHoursPerDay() == null) {

                // Average Hours/Day is required
                result = false;
                context.addFederalError(
                    ValidationField.DETAIL_AVG_HR_PER_DAY.value(),
                    "operatingDetail.avgHoursPerDay.required",
                    createValidationDetails(detail));

            } else if (detail.getAvgHoursPerDay().compareTo(BigDecimal.valueOf(24)) == 1 || detail.getAvgHoursPerDay().compareTo(BigDecimal.valueOf(0.0110)) == -1) {

                // Average Hours Per Day must be between 0.011 and 24
                result = false;
                context.addFederalError(
                    ValidationField.DETAIL_AVG_HR_PER_DAY.value(),
                    "operatingDetail.avgHoursPerDay.range",
                    createValidationDetails(detail));
            }

            if (detail.getAvgDaysPerWeek() == null) {

                // Average Days/Week is required
                result = false;
                context.addFederalError(
                    ValidationField.DETAIL_AVG_DAY_PER_WEEK.value(),
                    "operatingDetail.avgDaysPerWeek.required",
                    createValidationDetails(detail));

            } else if (detail.getAvgDaysPerWeek().compareTo(BigDecimal.valueOf(7)) == 1 || detail.getAvgDaysPerWeek().compareTo(BigDecimal.valueOf(0.0110)) == -1) {

                // Average Days Per Week must be between 0.1 and 7
                result = false;
                context.addFederalError(
                    ValidationField.DETAIL_AVG_DAY_PER_WEEK.value(),
                    "operatingDetail.avgDaysPerWeek.range",
                    createValidationDetails(detail));
            }

            if (detail.getActualHoursPerPeriod() == null) {

                //  Actual Hours/Year is required
                result = false;
                context.addFederalError(
                    ValidationField.DETAIL_ACT_HR_PER_PERIOD.value(),
                    "operatingDetail.actualHoursPerPeriod.required",
                    createValidationDetails(detail));

            } else if ((detail.getActualHoursPerPeriod().compareTo(BigDecimal.valueOf(8784)) > 0 || detail.getActualHoursPerPeriod().compareTo(BigDecimal.ONE) < 0)) {

                // Actual Hours Per Year must be between 1 and 8784
                result = false;
                context.addFederalError(
                    ValidationField.DETAIL_ACT_HR_PER_PERIOD.value(),
                    "operatingDetail.actualHoursPerPeriod.range",
                    createValidationDetails(detail));
            }


            int seasons = 0; // number of seasons with percents entered
            BigDecimal total = BigDecimal.ZERO; // total of percents for all seasons
            if (detail.getPercentSpring() == null) {

                // Spring Season % is required
                result = false;
                context.addFederalError(
                    ValidationField.DETAIL_PCT_SPRING.value(),
                    "operatingDetail.percentSpring.required",
                    createValidationDetails(detail));

            } else if (detail.getPercentSpring().compareTo(BigDecimal.ZERO) != 0) {

                seasons++;
                total = total.add(detail.getPercentSpring());
                // Spring Season % range
                if (detail.getPercentSpring().compareTo(BigDecimal.valueOf(100)) == 1 || detail.getPercentSpring().compareTo(BigDecimal.ZERO) == -1) {

                    result = false;
                    context.addFederalError(
                        ValidationField.DETAIL_PCT_SPRING.value(),
                        "operatingDetail.percentSpring.range",
                        createValidationDetails(detail));
                }
            }

            if (detail.getPercentSummer() == null) {

                // Summer Season % is required
                result = false;
                context.addFederalError(
                    ValidationField.DETAIL_PCT_SUMMER.value(),
                    "operatingDetail.percentSummer.required",
                    createValidationDetails(detail));

            } else if (detail.getPercentSummer().compareTo(BigDecimal.ZERO) != 0) {

                seasons++;
                total = total.add(detail.getPercentSummer());
                // Summer Season % range
                if (detail.getPercentSummer().compareTo(BigDecimal.valueOf(100)) == 1 || detail.getPercentSummer().compareTo(BigDecimal.ZERO) == -1) {

                    result = false;
                    context.addFederalError(
                        ValidationField.DETAIL_PCT_SUMMER.value(),
                        "operatingDetail.percentSummer.range",
                        createValidationDetails(detail));
                }
            }

            if (detail.getPercentFall() == null) {

                // Fall Season % is required
                result = false;
                context.addFederalError(
                    ValidationField.DETAIL_PCT_FALL.value(),
                    "operatingDetail.percentFall.required",
                    createValidationDetails(detail));

            } else if (detail.getPercentFall().compareTo(BigDecimal.ZERO) != 0) {

                seasons++;
                total = total.add(detail.getPercentFall());
                // Fall Season % range
                if (detail.getPercentFall().compareTo(BigDecimal.valueOf(100)) == 1 || detail.getPercentFall().compareTo(BigDecimal.ZERO) == -1) {

                    result = false;
                    context.addFederalError(
                        ValidationField.DETAIL_PCT_FALL.value(),
                        "operatingDetail.percentFall.range",
                        createValidationDetails(detail));
                }
            }

            if (detail.getPercentWinter() == null) {

                // Winter Season % is required
                result = false;
                context.addFederalError(
                    ValidationField.DETAIL_PCT_WINTER.value(),
                    "operatingDetail.percentWinter.required",
                    createValidationDetails(detail));

            } else if (detail.getPercentWinter().compareTo(BigDecimal.ZERO) != 0) {

                seasons++;
                total = total.add(detail.getPercentWinter());
                // Winter Season % range
                if (detail.getPercentWinter().compareTo(BigDecimal.valueOf(100)) == 1 || detail.getPercentWinter().compareTo(BigDecimal.ZERO) == -1) {

                    result = false;
                    context.addFederalError(
                        ValidationField.DETAIL_PCT_WINTER.value(),
                        "operatingDetail.percentWinter.range",
                        createValidationDetails(detail));
                }
            }

            if (detail.getAvgWeeksPerPeriod() == null) {

                // Average Weeks/Year is required
                result = false;
                context.addFederalError(
                    ValidationField.DETAIL_AVG_WEEK_PER_PERIOD.value(),
                    "operatingDetail.avgWeeksPerPeriod.required",
                    createValidationDetails(detail));
            } else {

                if (detail.getAvgWeeksPerPeriod().compareTo(BigDecimal.valueOf(52)) > 0 || detail.getAvgWeeksPerPeriod().compareTo(BigDecimal.ONE) < 0) {

                    // Actual Weeks Per Year must be between 1 and 52
                    result = false;
                    context.addFederalError(
                        ValidationField.DETAIL_AVG_WEEK_PER_PERIOD.value(),
                        "operatingDetail.avgWeeksPerPeriod.range",
                        createValidationDetails(detail));
                }

                if (detail.getAvgWeeksPerPeriod().compareTo(BigDecimal.valueOf(39)) > 0 && seasons < 4) {

                    // If Average Weeks/Year > 39 then all Season %'s are required to be > 0%
                    result = false;
                    context.addFederalError(
                        ValidationField.DETAIL_AVG_WEEK_PER_PERIOD.value(),
                        "operatingDetail.avgWeeksPerPeriod.seasons.4",
                        createValidationDetails(detail));

                } else if (detail.getAvgWeeksPerPeriod().compareTo(BigDecimal.valueOf(26)) > 0 && seasons < 3) {

                    // If Average Weeks/Year > 26 then at least three Season %'s are required to be > 0%
                    result = false;
                    context.addFederalError(
                        ValidationField.DETAIL_AVG_WEEK_PER_PERIOD.value(),
                        "operatingDetail.avgWeeksPerPeriod.seasons.3",
                        createValidationDetails(detail));

                } else if (detail.getAvgWeeksPerPeriod().compareTo(BigDecimal.valueOf(13)) > 0 && seasons < 2) {

                    // If Average Weeks/Year > 13 then at least two Season %'s are required to be > 0%
                    result = false;
                    context.addFederalError(
                        ValidationField.DETAIL_AVG_WEEK_PER_PERIOD.value(),
                        "operatingDetail.avgWeeksPerPeriod.seasons.2",
                        createValidationDetails(detail));

                }

                if (total.compareTo(BigDecimal.valueOf(100.5)) == 1 || total.compareTo(BigDecimal.valueOf(99.5)) == -1) {

                    // The sum of seasonal percentages must be between 99.5 and 100.5
                    result = false;
                    context.addFederalError(
                        ValidationField.DETAIL_PCT.value(),
                        "operatingDetail.percent.total",
                        createValidationDetails(detail));
                }
            }
        }


        return result;
    }

    private String getEmissionsUnitIdentifier(OperatingDetail source) {
        if (source.getReportingPeriod() != null && source.getReportingPeriod().getEmissionsProcess() != null
                && source.getReportingPeriod().getEmissionsProcess().getEmissionsUnit() != null) {
            return source.getReportingPeriod().getEmissionsProcess().getEmissionsUnit().getUnitIdentifier();
        }
        return null;
    }

    private String getEmissionsProcessIdentifier(OperatingDetail source) {
        if (source.getReportingPeriod() != null && source.getReportingPeriod().getEmissionsProcess() != null) {
            return source.getReportingPeriod().getEmissionsProcess().getEmissionsProcessIdentifier();
        }
        return null;
    }

    private ValidationDetailDto createValidationDetails(OperatingDetail source) {

        String description = MessageFormat.format("Emissions Unit: {0}, Emissions Process: {1}",
                getEmissionsUnitIdentifier(source),
                getEmissionsProcessIdentifier(source));

        ValidationDetailDto dto = new ValidationDetailDto(source.getId(), getEmissionsProcessIdentifier(source), EntityType.OPERATING_DETAIL, description);
        if (source.getReportingPeriod() != null && source.getReportingPeriod().getEmissionsProcess() != null) {

            dto.getParents().add(new ValidationDetailDto(
                    source.getReportingPeriod().getEmissionsProcess().getId(),
                    source.getReportingPeriod().getEmissionsProcess().getEmissionsProcessIdentifier(),
                    EntityType.EMISSIONS_PROCESS));
        }
        return dto;
    }
}
