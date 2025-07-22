/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.validation.validator.federal;

import java.math.BigDecimal;
import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import gov.epa.cef.web.service.validation.ValidationRegistry;

import gov.epa.cef.web.domain.Control;
import gov.epa.cef.web.domain.ControlAssignment;
import gov.epa.cef.web.domain.ControlPath;
import gov.epa.cef.web.domain.ControlPathPollutant;
import gov.epa.cef.web.repository.ControlAssignmentRepository;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.BaseValidator;
import gov.epa.cef.web.util.ConstantUtils;

@Component
public class AnnualControlPathValidator extends BaseValidator<ControlPath> {

    @Autowired
    private ControlAssignmentRepository assignmentRepo;

    private static final String PM10FIL = "PM10-FIL";
    private static final String PM10PRI = "PM10-PRI";
    private static final String PM25FIL = "PM25-FIL";
    private static final String PM25PRI = "PM25-PRI";

    @Override
    public void compose(FluentValidator validator,
                        ValidatorContext validatorContext,
                        ControlPath controlPath) {

        ValidationRegistry registry = getCefValidatorContext(validatorContext).getValidationRegistry();

        validator.onEach(controlPath.getPollutants(),
            registry.findOneByType(AnnualControlPathPollutantValidator.class));
    }

    @Override
    public boolean validate(ValidatorContext validatorContext, ControlPath controlPath) {

        boolean result = true;
        CefValidatorContext context = getCefValidatorContext(validatorContext);

        List<ControlAssignment> controlAssignmentList = new ArrayList<ControlAssignment>();
        controlAssignmentList = controlAssignmentListBuilder(controlPath.getAssignments());

        Map<Object, List<ControlAssignment>> cdMap = controlAssignmentList.stream()
            .filter(cd -> (cd.getControl() != null))
            .collect(Collectors.groupingBy(cd -> cd.getControl().getId()));

        for (List<ControlAssignment> cdList : cdMap.values()) {
            if (cdList.size() > 1) {

                result = false;
                context.addFederalError(
                    ValidationField.CONTROL_PATH_ASSIGNMENT.value(),
                    "controlPath.assignment.controlDevice.duplicate",
                    createValidationDetails(controlPath),
                    cdList.get(0).getControl().getIdentifier(),
                    cdList.get(0).getControl().getControlMeasureCode().getDescription());
            }
        }

        Map<Object, List<ControlAssignment>> cpDuplicateMap = controlPath.getAssignments().stream()
            .filter(cpa -> (cpa.getControlPathChild() != null))
            .collect(Collectors.groupingBy(ca -> ca.getControlPathChild().getId()));

        for (List<ControlAssignment> cpList : cpDuplicateMap.values()) {
            if (cpList.size() > 1) {

                result = false;
                context.addFederalError(
                    ValidationField.CONTROL_PATH_ASSIGNMENT.value(),
                    "controlPath.assignment.controlPath.duplicate",
                    createValidationDetails(controlPath),
                    cpList.get(0).getControlPathChild().getPathId());

            }
        }

        List<ControlAssignment> percentApptRange = controlPath.getAssignments().stream()
            .filter(appt -> (appt.getPercentApportionment() != null))
            .collect(Collectors.toList());

        for (ControlAssignment ca : percentApptRange) {
        	// percent appt must be > 0.1 or < 100
            if (ca.getPercentApportionment().compareTo(BigDecimal.valueOf(0.1)) == -1 || ca.getPercentApportionment().compareTo(BigDecimal.valueOf(100)) == 1) {
                result = false;
                context.addFederalError(
                    ValidationField.CONTROL_PATH_ASSIGNMENT.value(),
                    "controlPath.assignment.percentApportionment.range",
                    createValidationDetails(controlPath),
                    ca.getControlPath().getPathId());
            }
        }

        // check if control path is a parent path without a rp appt, or if both parent path of control path and control path does not have rp appt
        List<ControlPath> contAssignList = new ArrayList<ControlPath>();
        contAssignList = buildParentPathsList(controlPath);

        List<ControlPath> cpMap = contAssignList.stream()
            .filter(cd -> (cd.getReleasePointAppts().size() > 0)).collect(Collectors.toList());

        if (controlPath.getReleasePointAppts().isEmpty()) {

            if (contAssignList.isEmpty() || cpMap.size() < 1) {
                result = false;
                context.addFederalWarning(
                    ValidationField.CONTROL_PATH_RPA_WARNING.value(),
                    "controlPath.releasePointApportionment.notAssigned",
                    createValidationDetails(controlPath));
            }
        }

        // check if the control path is assigned, but has no pollutants
        if (!controlPath.getReleasePointAppts().isEmpty() && controlPath.getPollutants().isEmpty()) {

            result = false;
            context.addFederalError(
                ValidationField.CONTROL_PATH_POLLUTANT.value(),
                "controlPath.controlPathPollutant.required",
                createValidationDetails(controlPath));
        }


        List<Control> controls = new ArrayList<Control>();
        List<Control> controlsList = buildAssignedControlsList(controlPath.getAssignments(), controls);

        List<ControlAssignment> caList = controlPath.getAssignments().stream()
            .filter(cpa -> (cpa.getControl() != null))
            .collect(Collectors.toList());

        Map<Object, List<ControlAssignment>> caEmptyMap = caList.stream()
            .collect(Collectors.groupingBy(cpa -> cpa));

        List<ControlAssignment> caPSList = caList.stream()
            .filter(c -> ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(c.getControl().getOperatingStatusCode().getCode()))
            .collect(Collectors.toList());

        // if no controls assigned or if only PS controls are assigned
        if ((caEmptyMap.size() == 0 && controlsList.size() == 0)
            || (caList.size() > 0 && caList.size() == caPSList.size() && controlsList.size() > 0)) {
            result = false;
            context.addFederalError(
                ValidationField.CONTROL_PATH_NO_CONTROL_DEVICE_ASSIGNMENT.value(),
                "controlPath.assignment.notAssigned",
                createValidationDetails(controlPath));
        }

        Map<Object, List<ControlPath>> controlPaths = controlPath.getFacilitySite().getControlPaths().stream()
            .filter(cp -> (cp.getPathId() != null))
            .collect(Collectors.groupingBy(cpi -> cpi.getPathId().trim().toLowerCase()));

    	for (List<ControlPath> cpList : controlPaths.values()) {
    	    if (cpList.size() > 1 && cpList.get(0).getPathId().trim().toLowerCase().contentEquals(controlPath.getPathId().trim().toLowerCase())) {
    	        result = false;
    	        context.addFederalError(
    	            ValidationField.CONTROL_PATH_IDENTIFIER.value(),
                    "controlPath.pathIdentifier.duplicate",
                    createValidationDetails(controlPath)
                );

            }
        }

        if (controlPath.getPercentControl() != null) {
        	// percent control must be > 1 or < 100
        	if (controlPath.getPercentControl().compareTo(BigDecimal.ONE) == -1 || controlPath.getPercentControl().compareTo(BigDecimal.valueOf(100)) == 1) {

        		result = false;
	            context.addFederalError(
	                ValidationField.CONTROL_PATH_PERCENT_CONTROL.value(),
	                "controlPath.percentControl.range",
	                createValidationDetails(controlPath));
	        }

        	Pattern pattern = Pattern.compile(ConstantUtils.REGEX_ONE_DECIMAL_PRECISION);
        	Matcher matcher = pattern.matcher(controlPath.getPercentControl().stripTrailingZeros().toPlainString());
            if(!matcher.matches()){
                result = false;
                context.addFederalError(
                    ValidationField.CONTROL_PATH_PERCENT_CONTROL.value(),
                    "controlPath.percentControl.invalidFormat",
                    createValidationDetails(controlPath));
            }
        }

        Map<String, List<ControlPathPollutant>> cppMap = controlPath.getPollutants().stream()
        		.filter(e -> e.getPollutant() != null)
        		.collect(Collectors.groupingBy(e -> e.getPollutant().getPollutantCode()));

        BigDecimal pm10Fil = cppMap.containsKey(PM10FIL) ? cppMap.get(PM10FIL).get(0).getPercentReduction() : null;
        BigDecimal pm10Pri = cppMap.containsKey(PM10PRI) ? cppMap.get(PM10PRI).get(0).getPercentReduction() : null;
        BigDecimal pm25Fil = cppMap.containsKey(PM25FIL) ? cppMap.get(PM25FIL).get(0).getPercentReduction() : null;
        BigDecimal pm25Pri = cppMap.containsKey(PM25PRI) ? cppMap.get(PM25PRI).get(0).getPercentReduction() : null;

        for (List<ControlPathPollutant> pList : cppMap.values()) {
            if (pList.size() > 1) {
                result = false;
                context.addFederalError(
                    ValidationField.CONTROL_PATH_POLLUTANT.value(),
                    "controlPath.controlPollutant.duplicate",
                    createValidationDetails(controlPath),
                    pList.get(0).getPollutant().getPollutantName());
            }
        }

        // PM2.5 Filterable should not exceed PM10 Filterable.
        if (pm25Fil != null && pm10Fil != null
        		&& pm10Fil.compareTo(pm25Fil) == -1) {

        	result = false;
            context.addFederalError(
                ValidationField.CONTROL_PATH_POLLUTANT.value(),
                "controlPath.controlPathPollutant.pm25.fil.greater.pm10fil",
                createValidationDetails(controlPath));

        }

        // PM2.5 Primary should not exceed PM10 Primary.
        if (pm25Pri != null && pm10Pri != null
        		&& pm10Pri.compareTo(pm25Pri) == -1) {

        	result = false;
            context.addFederalError(
                ValidationField.CONTROL_PATH_POLLUTANT.value(),
                "controlPath.controlPathPollutant.pm25.pri.greater.pm10pri",
                createValidationDetails(controlPath));

        }

        // if control assigned is PS status
        if ((caPSList.size() > 0)) {
            for (ControlAssignment ca : caPSList) {
                result = false;
                context.addFederalError(
                    ValidationField.CONTROL_PATH_NO_CONTROL_DEVICE_ASSIGNMENT.value(),
                    "controlPath.assignment.controlDevice.permShutdown",
                    createValidationDetails(controlPath), ca.getControl().getIdentifier());
            }
        }

        List<ControlAssignment> caTSList = caList.stream()
            .filter(c -> ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN.contentEquals(c.getControl().getOperatingStatusCode().getCode()))
            .collect(Collectors.toList());


        // if control assigned is TS status
        if ((caTSList.size() > 0)) {
            for (ControlAssignment ca : caTSList) {
                result = false;
                context.addFederalWarning(
                    ValidationField.CONTROL_PATH_NO_CONTROL_DEVICE_ASSIGNMENT.value(),
                    "controlPath.assignment.controlDevice.tempShutdown",
                    createValidationDetails(controlPath), ca.getControl().getIdentifier());
            }
        }

        List<ControlAssignment> sequenceMap = controlPath.getAssignments().stream()
            .filter(cpa -> (cpa.getSequenceNumber() != null))
            .collect(Collectors.toList());
        List<Integer> uniqueSequenceList = new ArrayList<Integer>();

        for (ControlAssignment ca : sequenceMap) {
            if (!uniqueSequenceList.contains(ca.getSequenceNumber())) {
                uniqueSequenceList.add(ca.getSequenceNumber());
            }
        }

        for (Integer sequenceNumber : uniqueSequenceList) {
        	BigDecimal totalApportionment = BigDecimal.ZERO;
            for (ControlAssignment ca : sequenceMap) {
                if (ca.getSequenceNumber() != null && ca.getSequenceNumber().equals(sequenceNumber)) {
                    totalApportionment = ca.getPercentApportionment().add(totalApportionment);
                }
            }
            if (totalApportionment.compareTo(BigDecimal.valueOf(100)) != 0) {
                result = false;
                context.addFederalError(
                    ValidationField.CONTROL_PATH_ASSIGNMENT.value(),
                    "controlPath.assignment.sequenceNumber.totalApportionment",
                    createValidationDetails(controlPath), sequenceNumber);
            }
        }

        List<ControlAssignment> sequenceNullMap = controlPath.getAssignments().stream()
            .filter(cpa -> (cpa.getSequenceNumber() == null))
            .collect(Collectors.toList());

        for (ControlAssignment ca : sequenceNullMap) {
            result = false;
            context.addFederalError(
                ValidationField.CONTROL_PATH_ASSIGNMENT.value(),
                "controlPath.assignment.sequenceNumber.required",
                createValidationDetails(controlPath, ca));
        }

        List<ControlAssignment> caPathAndControlNullMap = controlPath.getAssignments().stream()
            .filter(cpa -> (cpa.getControl() == null && cpa.getControlPathChild() == null))
            .collect(Collectors.toList());

        for (ControlAssignment ca : caPathAndControlNullMap) {
            result = false;
            context.addFederalError(
                ValidationField.CONTROL_PATH_ASSIGNMENT.value(),
                "controlPath.assignment.pathOrControl.required",
                createValidationDetails(controlPath));
        }

        if (controlPath.getPollutants().isEmpty() && caList.isEmpty() && !doesChildPathExist(controlPath.getAssignments())) {
            result = false;
            context.addFederalWarning(
                ValidationField.CONTROL_PATH_ASSIGNMENT_EMPTY.value(),
                "controlPath.assignment.empty",
                createValidationDetails(controlPath));
        }

        return result;
    }

    private ValidationDetailDto createValidationDetails(ControlPath source) {

        String description = MessageFormat.format("ControlPath: {0}", source.getPathId());

        ValidationDetailDto dto = new ValidationDetailDto(source.getId(), source.getPathId(), EntityType.CONTROL_PATH, description);
        return dto;
    }

    private ValidationDetailDto createValidationDetails(ControlPath source, ControlAssignment assignment) {

        String description;

        if (assignment.getControl() != null) {
            description = MessageFormat.format("Control Path: {0}, Control Path Assignment: {1}", source.getPathId(), assignment.getControl().getIdentifier());
        } else {
            description = MessageFormat.format("Control Path: {0}, Control Path Assignment: {1}", source.getPathId(), assignment.getControlPathChild().getPathId());
        }

        ValidationDetailDto dto = new ValidationDetailDto(source.getId(), source.getPathId(), EntityType.CONTROL_PATH, description);
        return dto;
    }

    private List<Control> buildAssignedControlsList(List<ControlAssignment> controlAssignments, List<Control> controls) {
        for (ControlAssignment ca : controlAssignments) {
            if (ca.getControl() != null) {
                controls.add(ca.getControl());
                return controls;
            }
            if (ca.getControlPathChild() != null) {
                buildAssignedControlsList(ca.getControlPathChild().getAssignments(), controls);
            }
        }
        return controls;
    }

    private List<ControlAssignment> controlAssignmentListBuilder(List<ControlAssignment> controlAssignments) {
        List<ControlAssignment> controlAssignmentList = new ArrayList<ControlAssignment>();
        for (ControlAssignment ca : controlAssignments) {
            if (ca.getControl() != null) {
                controlAssignmentList.add(ca);
            }
            if (ca.getControlPathChild() != null) {
                controlAssignmentList.addAll(controlAssignmentListBuilder(ca.getControlPathChild().getAssignments()));
            }
        }
        return controlAssignmentList;
    }

    private List<ControlPath> buildParentPathsList(ControlPath cp) {
        List<ControlPath> parentPathList = new ArrayList<ControlPath>();
        List<ControlAssignment> controlAssignmentList = assignmentRepo.findByControlPathChildId(cp.getId());
        parentPathList.add(cp);
        for (ControlAssignment c : controlAssignmentList) {
            if (controlAssignmentList.size() > 0) {
                parentPathList.addAll(buildParentPathsList(c.getControlPath()));
            }
        }
        return parentPathList;
    }

    private boolean doesChildPathExist(List<ControlAssignment> controlPathAssignments) {
        for (ControlAssignment cpa : controlPathAssignments) {
          if (cpa.getControlPathChild() != null) {
            return true;
          }
        }
        return false;
    }

}
