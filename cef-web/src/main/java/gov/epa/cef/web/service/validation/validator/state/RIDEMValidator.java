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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RIDEMValidator extends BaseValidator<EmissionsReport>
    implements IEmissionsReportValidator {

    @Autowired
    private EmissionsReportRepository reportRepo;

    @Autowired
    private EmissionsUnitRepository unitRepo;

    @Override
    public boolean accept(ValidatorContext context, EmissionsReport emissionsReport) {

        return getCefValidatorContext(context).isEnabled(ValidationFeature.MEDEP);
    }

    public boolean validate(ValidatorContext validatorContext, EmissionsReport emissionsReport) {

        boolean result = true;

        CefValidatorContext context = getCefValidatorContext(validatorContext);

        List<EmissionsUnit> eus = emissionsReport.getFacilitySites().get(0).getEmissionsUnits();

        List<EmissionsReport> erList = reportRepo.findByMasterFacilityRecordId(emissionsReport.getMasterFacilityRecord().getId()).stream()
            .filter(var -> (var.getYear() != null && var.getYear() < emissionsReport.getYear()))
            .sorted(Comparator.comparing(EmissionsReport::getYear))
            .collect(Collectors.toList());

        // check if previous report exists then check if units exist in that report
        if (!erList.isEmpty()) {
            Short previousReportYr = erList.get(erList.size() - 1).getYear();

            List<EmissionsUnit> previousUnits = unitRepo.retrieveByFacilityYear(
                emissionsReport.getMasterFacilityRecord().getId(),
                previousReportYr);
            boolean ridem = false;
            if (!previousUnits.isEmpty()) {
                for (EmissionsUnit eu : eus) {
                    List<EmissionsProcess> epList = eu.getEmissionsProcesses();
                    for (EmissionsProcess ep : epList) {
                        List<ReportingPeriod> rpList = ep.getReportingPeriods();
                        for (ReportingPeriod rp : rpList) {
                            List<Emission> emList = rp.getEmissions();
                            for (Emission em : emList) {
                                for (String polluntantCode : ConstantUtils.POLLUTANT_CODES_RIDEM) {
                                    if (em.getPollutant().getPollutantCode().equals(polluntantCode)) {
                                        ridem = true;
                                    }
                                }
                            }
                        }
                    }
                    FacilitySite fs = emissionsReport.getFacilitySites().get(0);
                    List<Control> controlList = fs.getControls();
                    for(Control c : controlList) {
                        List<ControlPollutant> cpList = c.getPollutants();
                        for(ControlPollutant cp : cpList) {
                            for (String polluntantCode : ConstantUtils.CONTROL_POLLUTANT_CODES) {
                                if(polluntantCode.equals(cp.getPollutant().getPollutantCode())) {
                                    ridem = true;
                                }
                            }
                        }
                    }
                    List<ControlPath> controlPathList = fs.getControlPaths();
                    for(ControlPath cPath : controlPathList) {
                        List<ControlPathPollutant> cPathList = cPath.getPollutants();
                        for(ControlPathPollutant cPathData : cPathList) {
                            for (String polluntantCode : ConstantUtils.CONTROL_POLLUTANT_CODES) {
                                if(polluntantCode.equals(cPathData.getPollutant().getPollutantCode())) {
                                    ridem = true;
                                }
                            }
                        }
                    }
                    if(!ridem) {
                        boolean pyUnitExists = false;


                        for (EmissionsUnit previousUnit : previousUnits) {

                            if (eu.getUnitIdentifier().equals(previousUnit.getUnitIdentifier())) {
                                pyUnitExists = true;

                                if (!eu.equals(previousUnit)) {
                                    result = false;
                                    context.addStateWarning(
                                        ValidationField.EMISSIONS_UNIT_STATUS_CODE.value(),
                                        "emissionsUnit.ridem.newOrEdited",
                                        createUnitValidationDetails(eu));
                                }
                                break;
                            }
                        }
                        if (!pyUnitExists) {

                            result = false;
                            context.addStateWarning(
                                ValidationField.EMISSIONS_UNIT_STATUS_CODE.value(),
                                "emissionsUnit.ridem.newOrEdited",
                                createUnitValidationDetails(eu));
                        }}
                }
            }
        }
        return result;
    }

    private ValidationDetailDto createUnitValidationDetails(EmissionsUnit source) {
        String description = MessageFormat.format("Emissions Unit: {0}", source.getUnitIdentifier());
        ValidationDetailDto dto = new ValidationDetailDto(source.getId(), source.getUnitIdentifier(), EntityType.EMISSIONS_UNIT, description);
        return dto;
    }
}
