package gov.epa.cef.web.util;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ValidatorUtil {

    private boolean findDescriptionData(Emission e, CefValidatorContext context) {
        boolean found = false;
        String emissionUnitId = getEmissionsUnitIdentifier(e);
        String pollutant = e.getPollutant().getPollutantName();
        List<ValidationError> errorList = context.result.getErrors();
        if(errorList != null) {
            for(ValidationError errorData : errorList) {
                ValidationDetailDto dto = (ValidationDetailDto) errorData.getInvalidValue();
                String dtoDescription = dto.getDescription();
                if(dtoDescription.contains(emissionUnitId) && dtoDescription.contains(pollutant)) {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    public void checkNotReportingValidation(Emission e, CefValidatorContext context) {
        boolean found = false;
        if(e.getPollutant() != null) {
            found = findDescriptionData(e, context);
        }
        if(!found) {
            context.addFederalWarning(
                ValidationField.NOT_REPORTING.value(),
                "reportingPeriod.data.not.reported",
                createValidationDetails(e),
                e.getPollutant().getPollutantName());
            
            if(e.getPollutant().getPollutantName().equalsIgnoreCase("Lead")) {
                if (getReportYear(e) % 3 == 1){
                    context.addFederalError(
                        ValidationField.TRIENNIAL_NOT_REPORTING.value(),
                        "reportingPeriod.data.triennial.not.reported",
                        createValidationDetails(e),
                        e.getPollutant().getPollutantName());
                }
            } else if (e.getPollutant().getPollutantType().equalsIgnoreCase("CAP")) {
                context.addFederalError(
                ValidationField.CAP_NOT_REPORTING.value(),
                "reportingPeriod.data.cap.not.reported",
                createValidationDetails(e),
                e.getPollutant().getPollutantName());
            }
        }
    }

    private int getReportYear(Emission emission) {
        return emission.getReportingPeriod().getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport().getYear().intValue();
    }

    public void checkNotReportingValidationError(Emission e, CefValidatorContext context) {
        boolean found = false;
        List<ValidationError> errorList = context.result.getErrors();
        if(errorList != null) {
            for(ValidationError errorData : errorList) {
                ValidationDetailDto dto = (ValidationDetailDto) errorData.getInvalidValue();
                String dtoDescription = dto.getDescription();
                String[] dtoDescriptionValues = dtoDescription.split(":");
                if(dtoDescriptionValues.length == 4) {
                    if(dtoDescription.contains("You may not mark" + dtoDescriptionValues[3] + "as not reporting this year, as this emission was not included in the previous year's submission")) {
                        found = true;
                        break;
                    }
                }
            }
        }
        if(!found) {
            context.addFederalError(
                ValidationField.NOT_REPORTING.value(),
                "reportingPeriod.data.not.reported.previous.year",
                createValidationDetails(e),
                e.getPollutant().getPollutantName());
        }
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
}
