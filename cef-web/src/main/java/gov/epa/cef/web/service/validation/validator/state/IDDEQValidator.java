/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.validation.validator.state;

import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.EmissionsUnitRepository;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationFeature;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.BaseValidator;
import gov.epa.cef.web.service.validation.validator.IEmissionsReportValidator;
import gov.epa.cef.web.util.ConstantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class IDDEQValidator
    extends BaseValidator<EmissionsReport>
    implements IEmissionsReportValidator {

    @Autowired
    private EmissionsReportRepository reportRepo;

    @Autowired
    private EmissionsUnitRepository unitRepo;

    @Override
    public boolean accept(ValidatorContext context, EmissionsReport emissionsReport) {

        return getCefValidatorContext(context).isEnabled(ValidationFeature.IDDEQ);
    }

    public boolean validate(ValidatorContext validatorContext, EmissionsReport emissionsReport) {

        boolean result = true;

        CefValidatorContext context = getCefValidatorContext(validatorContext);

        // make sure all emissions have an emissions factor
        if ((!emissionsReport.getFacilitySites().get(0).getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN) &&
            !emissionsReport.getFacilitySites().get(0).getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN)) ||
            (emissionsReport.getMasterFacilityRecord().getFacilitySourceTypeCode() != null &&
            emissionsReport.getMasterFacilityRecord().getFacilitySourceTypeCode().getCode().equals(ConstantUtils.FACILITY_SOURCE_LANDFILL_CODE))) {

            List<EmissionsUnit> eus = emissionsReport.getFacilitySites().get(0).getEmissionsUnits();

            for (EmissionsUnit eu : eus) {
                if (!eu.getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN) &&
                    !eu.getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN)) {

                    for (EmissionsProcess ep : eu.getEmissionsProcesses()) {
                        if (!ep.getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN) &&
                            !ep.getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN)) {

                            for (ReportingPeriod rp : ep.getReportingPeriods()) {
                                for (Emission emission : rp.getEmissions()) {

                                    if (emission.getEmissionsFactor() == null) {

                                        result = false;
                                        context.addStateError(
                                            ValidationField.EMISSION_EF.value(),
                                            "emission.iddeq.emissionsFactor.required",
                                            createEmissionValidationDetails(emission));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return result;
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

    private ValidationDetailDto createEmissionValidationDetails(Emission source) {

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

    private ValidationDetailDto createUnitValidationDetails(EmissionsUnit source) {
        String description = MessageFormat.format("Emissions Unit: {0}", source.getUnitIdentifier());
        ValidationDetailDto dto = new ValidationDetailDto(source.getId(), source.getUnitIdentifier(), EntityType.EMISSIONS_UNIT, description);
        return dto;
    }

    private ValidationDetailDto createProcessValidationDetails(EmissionsProcess source) {
        String description = MessageFormat.format("Emissions Unit: {0}, Emissions Process: {1}",
            source.getEmissionsUnit().getUnitIdentifier(),
            source.getEmissionsProcessIdentifier());
        ValidationDetailDto dto = new ValidationDetailDto(source.getId(), source.getEmissionsProcessIdentifier(), EntityType.EMISSIONS_PROCESS, description);
        return dto;
    }

    private ValidationDetailDto createReleasePointValidationDetails(ReleasePoint source) {
        String description = MessageFormat.format("Release Point: {0}",
            source.getReleasePointIdentifier());
        ValidationDetailDto dto = new ValidationDetailDto(source.getId(), source.getReleasePointIdentifier(), EntityType.RELEASE_POINT, description);
        return dto;
    }
}
