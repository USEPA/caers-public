/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.validation.validator.federal;

import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.google.common.base.Strings;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.FacilitySiteRepository;
import gov.epa.cef.web.service.LookupService;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.ValidationRegistry;
import gov.epa.cef.web.service.validation.validator.BaseValidator;
import gov.epa.cef.web.util.ConstantUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class AnnualFacilitySiteValidator extends BaseValidator<FacilitySite> {

	@Autowired
	private FacilitySiteRepository facilitySiteRepo;

    @Autowired
	private EmissionsReportRepository reportRepo;

    @Autowired
    private LookupService lookupService;

    private static final String STATUS_OPERATING_REPORTING_NONPOINT = "ONP";
    private static final String STATUS_OPERATING_NOT_REPORTING = "ONRE";

    @Override
    public void compose(FluentValidator validator,
                        ValidatorContext validatorContext,
                        FacilitySite facilitySite) {

        ValidationRegistry registry = getCefValidatorContext(validatorContext).getValidationRegistry();

        // add more validators as needed
        if ((facilitySite.getFacilitySourceTypeCode() != null && ConstantUtils.FACILITY_SOURCE_LANDFILL_CODE.contentEquals(facilitySite.getFacilitySourceTypeCode().getCode()))
                || ConstantUtils.STATUS_OPERATING.contentEquals(facilitySite.getOperatingStatusCode().getCode())) {
            validator.onEach(facilitySite.getEmissionsUnits(),
                registry.findOneByType(SharedEmissionsUnitValidator.class));

            validator.onEach(facilitySite.getEmissionsUnits(),
                registry.findOneByType(AnnualEmissionsUnitValidator.class));

            validator.onEach(facilitySite.getReleasePoints(),
                    registry.findOneByType(AnnualReleasePointValidator.class));

            validator.onEach(facilitySite.getControls(),
            		registry.findOneByType(AnnualControlValidator.class));

            validator.onEach(facilitySite.getControlPaths(),
            		registry.findOneByType(AnnualControlPathValidator.class));
        }
    }

    @Override
    public boolean validate(ValidatorContext validatorContext, FacilitySite facilitySite) {

        boolean result = true;

        CefValidatorContext context = getCefValidatorContext(validatorContext);

        // If facility operation status is not operating, status year is required
        if (!ConstantUtils.STATUS_OPERATING.contentEquals(facilitySite.getOperatingStatusCode().getCode()) && facilitySite.getStatusYear() == null) {

        	result = false;
        	context.addFederalError(
        			ValidationField.FACILITY_STATUS.value(), "facilitysite.status.required",
        			createValidationDetails(facilitySite));
        }

        // Status year must be between 1900 and the report year
        if (facilitySite.getStatusYear() != null && (facilitySite.getStatusYear() < 1900 || facilitySite.getStatusYear() > facilitySite.getEmissionsReport().getYear())) {

        	result = false;
        	context.addFederalError(
        			ValidationField.FACILITY_STATUS.value(), "facilitysite.status.range",
        			createValidationDetails(facilitySite),
        			facilitySite.getEmissionsReport().getYear().toString());
        }

        if (facilitySite.getCountyCode() == null) {

            result = false;
            context.addFederalError(
                    ValidationField.FACILITY_COUNTY.value(), "facilitySite.county.required",
                    createValidationDetails(facilitySite));

        } else if (!facilitySite.getCountyCode().getFipsStateCode().getUspsCode().equals(facilitySite.getStateCode().getUspsCode())) {

            result = false;
            context.addFederalError(
                    ValidationField.FACILITY_COUNTY.value(), "facilitySite.county.invalidState",
                    createValidationDetails(facilitySite),
                    facilitySite.getCountyCode().getName(),
                    facilitySite.getStateCode().getUspsCode());
        }

        if (facilitySite.getCountyCode() != null && facilitySite.getCountyCode().getLastInventoryYear() != null
            && facilitySite.getCountyCode().getLastInventoryYear() < facilitySite.getEmissionsReport().getYear()) {

            result = false;
            context.addFederalError(
                    ValidationField.FACILITY_COUNTY.value(), "facilitysite.county.legacy",
                    createValidationDetails(facilitySite),
                    facilitySite.getCountyCode().getName());

        }

        if (facilitySite.getFacilitySourceTypeCode() != null && facilitySite.getFacilitySourceTypeCode().getLastInventoryYear() != null
        	&& facilitySite.getFacilitySourceTypeCode().getLastInventoryYear() < facilitySite.getEmissionsReport().getYear()) {

        	result = false;
        	context.addFederalError(
        			ValidationField.FACILITY_SOURCE_TYPE_CODE.value(),
        			"facilitySite.sourceTypeCode.legacy",
        			createValidationDetails(facilitySite),
        			facilitySite.getFacilitySourceTypeCode().getDescription());

        }

        // Phone number must be entered as 10 digits
        String regex = "^[0-9]{10}";
        Pattern pattern = Pattern.compile(regex);
    	for(FacilitySiteContact fc: facilitySite.getContacts()){
        	if(!StringUtils.isEmpty(fc.getPhone())){
            	Matcher matcher = pattern.matcher(fc.getPhone());
            	if(!matcher.matches()){
                	result = false;
                	context.addFederalError(
                			ValidationField.FACILITY_CONTACT_PHONE.value(),
                			"facilitySite.contacts.phoneNumber.requiredFormat",
                			createContactValidationDetails(facilitySite));
            	}
        	} else {
            	result = false;
            	context.addFederalError(
            			ValidationField.FACILITY_CONTACT_PHONE.value(),
            			"facilitySite.contacts.phoneNumber.requiredFormat",
            			createContactValidationDetails(facilitySite));
        	}
    	}

        // Postal codes must be entered as 5 digits (XXXXX) or 9 digits (XXXXX-XXXX).
    	regex = "^[0-9]{5}(?:-[0-9]{4})?$";
    	pattern = Pattern.compile(regex);
    	for(FacilitySiteContact fc: facilitySite.getContacts()){
        	if(!StringUtils.isEmpty(fc.getPostalCode())){
            	Matcher matcher = pattern.matcher(fc.getPostalCode());
            	if(!matcher.matches()){
                	result = false;
                	context.addFederalError(
                			ValidationField.FACILITY_CONTACT_POSTAL.value(),
                			"facilitySite.contacts.postalCode.requiredFormat",
                			createContactValidationDetails(facilitySite));
            	}
        	}
        	if(!StringUtils.isEmpty(fc.getMailingPostalCode())){
            	Matcher matcher = pattern.matcher(fc.getMailingPostalCode());
            	if(!matcher.matches()){
                	result = false;
                	context.addFederalError(
                			ValidationField.FACILITY_CONTACT_POSTAL.value(),
                			"facilitySite.contacts.postalCode.requiredFormat",
                			createContactValidationDetails(facilitySite));
            	}
        	}
    	}

    	if(!StringUtils.isEmpty(facilitySite.getPostalCode())) {
        	Matcher matcher = pattern.matcher(facilitySite.getPostalCode());
        	if(!matcher.matches()){
            	result = false;
            	context.addFederalError(
            			ValidationField.FACILITY_CONTACT_POSTAL.value(),
            			"facilitysite.postalCode.requiredFormat",
            			createValidationDetails(facilitySite));
        	}
    	}
    	if(!StringUtils.isEmpty(facilitySite.getMailingPostalCode())){
        	Matcher matcher = pattern.matcher(facilitySite.getMailingPostalCode());
        	if(!matcher.matches()){
            	result = false;
            	context.addFederalError(
            			ValidationField.FACILITY_CONTACT_POSTAL.value(),
            			"facilitysite.postalCode.requiredFormat",
            			createValidationDetails(facilitySite));
        	}
    	}

    	// Email address must be in a valid format.
    	pattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+[\\.][A-Za-z]{2,}$");
    	for(FacilitySiteContact fc: facilitySite.getContacts()){
        	if(!StringUtils.isEmpty(fc.getEmail())){
            	Matcher matcher = pattern.matcher(fc.getEmail());
            	if(!pattern.matcher(fc.getEmail()).matches()){
                	result = false;
                	context.addFederalError(
                			ValidationField.FACILITY_EMAIL_ADDRESS.value(),
                			"facilitySite.contacts.emailAddress.requiredFormat",
                			createContactValidationDetails(facilitySite));
            	}
        	}
    	}

        // Facility must have a facility NAICS code reported
        List<FacilityNAICSXref> fsNAICSList = facilitySite.getFacilityNAICS();

        if (CollectionUtils.isEmpty(fsNAICSList)) {

        	result = false;
        	context.addFederalError(ValidationField.FACILITY_NAICS.value(), "facilitysite.naics.required",
        			createValidationDetails(facilitySite));
        }

        for (FacilityNAICSXref naics : fsNAICSList) {
            if (isNaicsCodeNotNull(naics.getNaicsCode())
                && isNaicsLastIYNotNullAndLessThanFacilityIY(facilitySite, naics.getNaicsCode())
            ) {

                result = false;

                NaicsCode alternateNaicsCode = findAlternateNaicsByInventoryYear(facilitySite, naics.getNaicsCode());

                if (alternateNaicsCode != null) {

                    addNAICSLegacyMapError(context, facilitySite, naics.getNaicsCode().getCode().toString(),
                        alternateNaicsCode.getCode().toString());
                } else {

                    addNAICSLegacyError(context, facilitySite, naics.getNaicsCode().getCode().toString(), naics.getNaicsCode().getLastInventoryYear().toString());
                }
            }
        }

        Map<Integer, List<FacilityNAICSXref>> fsNAICSMap = facilitySite.getFacilityNAICS().stream()
                .collect(Collectors.groupingBy(fn -> fn.getNaicsCode().getCode()));

        // check for duplicate NAICS
        for (Entry<Integer, List<FacilityNAICSXref>> entry : fsNAICSMap.entrySet()) {

            if (entry.getValue().size() > 1) {

                result = false;
                context.addFederalError(
                        ValidationField.FACILITY_NAICS.value(),
                        "facilitysite.naics.duplicate",
                        createValidationDetails(facilitySite),
                        entry.getKey().toString());
            }
        }

        // Facility NAICS must have one and only one primary assigned
        fsNAICSList = facilitySite.getFacilityNAICS().stream()
            .filter(fn -> fn.getNaicsCodeType().equals(NaicsCodeType.PRIMARY))
            .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(fsNAICSList) || fsNAICSList.size() != 1) {
        	result = false;
        	context.addFederalError(ValidationField.FACILITY_NAICS.value(), "facilitysite.naics.primary.required",
        			createValidationDetails(facilitySite));
        }


        // Facility must have an Emissions Inventory Contact
        List<FacilitySiteContact> contactList = facilitySite.getContacts().stream()
        .filter(fc -> fc.getType().getCode().equals("EI"))
        .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(contactList)) {

        	result = false;
        	context.addFederalError(ValidationField.FACILITY_CONTACT.value(), "facilitysite.contacts.required",
        			createContactValidationDetails(facilitySite));
        }


        for (FacilitySiteContact contact: facilitySite.getContacts()) {
            // Facility contact must have an email
        	if (Strings.emptyToNull(contact.getEmail()) == null) {

        		result = false;
        		context.addFederalError(ValidationField.FACILITY_CONTACT.value(), "facilitysite.contacts.email.required",
        				createContactValidationDetails(facilitySite));
        	}

        	// Facility contact county must be for selected state
        	if (contact.getCountyCode() != null && !contact.getCountyCode().getFipsStateCode().getUspsCode().equals(contact.getStateCode().getUspsCode())) {

                result = false;
                context.addFederalError(
                        ValidationField.FACILITY_CONTACT_COUNTY.value(), "facilitySite.contacts.county.invalidState",
                        createContactValidationDetails(facilitySite),
                        contact.getCountyCode().getName(),
                        contact.getStateCode().getUspsCode());
            }

        	if (contact.getCountyCode() != null && contact.getCountyCode().getLastInventoryYear() != null
                && contact.getCountyCode().getLastInventoryYear() < contact.getFacilitySite().getEmissionsReport().getYear()) {

                result = false;
                context.addFederalError(
                        ValidationField.FACILITY_CONTACT_COUNTY.value(), "facilitysite.contacts.county.legacy",
                        createContactValidationDetails(facilitySite),
                        contact.getCountyCode().getName());

            }
        }

        if (facilitySite.getStatusYear() != null && facilitySite.getFacilitySourceTypeCode() != null) {

	        // warning total emissions will not be accepted if facility site operation status is not OP,
	        // except when source type is landfill or status year is greater than inventory cycle year.
	        if ((!ConstantUtils.FACILITY_SOURCE_LANDFILL_CODE.contentEquals(facilitySite.getFacilitySourceTypeCode().getCode()))
	        	&& facilitySite.getStatusYear() <= facilitySite.getEmissionsReport().getYear()
	        	&& (!ConstantUtils.STATUS_OPERATING.contentEquals(facilitySite.getOperatingStatusCode().getCode()))) {
		        	result = false;
		        	context.addFederalWarning(
		        			ValidationField.FACILITY_EMISSION_REPORTED.value(),
		        			"facilitysite.reportedEmissions.invalidWarning",
		        			createValidationDetails(facilitySite));
	      	}
        }

        if (STATUS_OPERATING_REPORTING_NONPOINT.contentEquals(facilitySite.getOperatingStatusCode().getCode())
        	|| STATUS_OPERATING_NOT_REPORTING.contentEquals(facilitySite.getOperatingStatusCode().getCode())) {

	        	result = false;
	        	context.addFederalError(
	        			ValidationField.FACILITY_STATUS.value(),
	        			"facilitySite.status.invalid",
	        			createValidationDetails(facilitySite),
	        			facilitySite.getOperatingStatusCode().getDescription());
        }



        EmissionsReport currentReport = facilitySite.getEmissionsReport();

        List<EmissionsReport> erList = reportRepo.findByMasterFacilityRecordId(currentReport.getMasterFacilityRecord().getId()).stream()
            .filter(var -> (var.getYear() != null && var.getYear() < currentReport.getYear()))
            .sorted(Comparator.comparing(EmissionsReport::getYear))
            .collect(Collectors.toList());

        // check if previous report exists
        if (!erList.isEmpty()) {
            Short previousReportYr = erList.get(erList.size() - 1).getYear();

            FacilitySite previousFacilitySite = facilitySiteRepo.findByAgencyFacilityIdentifierAndEmissionsReportYear(
                facilitySite.getAgencyFacilityIdentifier(),
                facilitySite.getProgramSystemCode().getCode(),
                previousReportYr);

            if (previousFacilitySite != null) {

                // check operating status/status year of current report to operating status/status year of previous report
                if (!facilitySite.getOperatingStatusCode().getCode().equals(previousFacilitySite.getOperatingStatusCode().getCode())
                    && previousFacilitySite.getStatusYear() != null && facilitySite.getStatusYear() <= previousFacilitySite.getStatusYear()) {

                    result = false;
                    context.addFederalError(
                        ValidationField.FACILITY_STATUS.value(),
                        "facilitySite.statusYear.invalid",
                        createValidationDetails(facilitySite));
                }
            }
        }

        return result;
    }

    private ValidationDetailDto createValidationDetails(FacilitySite source) {

    	String description = MessageFormat.format("Facility Site ", source.getEmissionsReport().getMasterFacilityRecord().getEisProgramId());

    	ValidationDetailDto dto = new ValidationDetailDto(source.getId(), source.getEmissionsReport().getMasterFacilityRecord().getEisProgramId(), EntityType.FACILITY_SITE, description);
    	return dto;
    }

    private ValidationDetailDto createContactValidationDetails(FacilitySite source) {

    	String description = MessageFormat.format("Facility Contact ", source.getEmissionsReport().getMasterFacilityRecord().getEisProgramId());

    	ValidationDetailDto dto = new ValidationDetailDto(source.getId(), source.getEmissionsReport().getMasterFacilityRecord().getEisProgramId(), EntityType.FACILITY_SITE, description);
    	return dto;
    }

    private ValidationDetailDto createEmissionsUnitValidationDetails(EmissionsUnit source) {

      String description = MessageFormat.format("Emissions Unit: {0}", source.getUnitIdentifier());

      ValidationDetailDto dto = new ValidationDetailDto(source.getId(), source.getUnitIdentifier(), EntityType.EMISSIONS_UNIT, description);
      return dto;
    }

    private ValidationDetailDto createReleasePointValidationDetails(ReleasePoint source) {

      String description = MessageFormat.format("Release Point: {0}", source.getReleasePointIdentifier());

      ValidationDetailDto dto = new ValidationDetailDto(source.getId(), source.getReleasePointIdentifier(), EntityType.RELEASE_POINT, description);
      return dto;
    }

    private ValidationDetailDto createControlValidationDetails(Control source) {

      String description = MessageFormat.format("Control: {0}", source.getIdentifier());

      ValidationDetailDto dto = new ValidationDetailDto(source.getId(), source.getIdentifier(), EntityType.CONTROL, description);
      return dto;
    }

    private void addNAICSLegacyError(CefValidatorContext context, FacilitySite facilitySite, String naicsCode, String year) {

        context.addFederalError(
            ValidationField.FACILITY_NAICS.value(),
            "facilitysite.naics.legacy",
            createValidationDetails(facilitySite),
            naicsCode,
            year);
    }

    private void addNAICSLegacyMapError(CefValidatorContext context, FacilitySite facilitySite, String naicsCode, String mapTo) {

        context.addFederalError(
            ValidationField.FACILITY_NAICS.value(),
            "facilitysite.naics.legacy.map",
            createValidationDetails(facilitySite),
            naicsCode,
            mapTo);
    }

    private NaicsCode findAlternateNaicsByInventoryYear(FacilitySite facilitySite,
                                                        NaicsCode currentAlternateNaicsCode) {
        if (isNaicsCodeNotNull(currentAlternateNaicsCode)
            && isNaicsLastIYNotNullAndLessThanFacilityIY(facilitySite, currentAlternateNaicsCode)
            && Strings.emptyToNull(currentAlternateNaicsCode.getMapTo()) != null
        ) { // Alternate NAICS retired for IY, but another alternate exists

            currentAlternateNaicsCode = this.lookupService
                .retrieveNAICSCodeEntityByCode(Integer.valueOf(currentAlternateNaicsCode.getMapTo()));

            return findAlternateNaicsByInventoryYear(facilitySite, currentAlternateNaicsCode);

        } else if (isNaicsCodeNotNull(currentAlternateNaicsCode)
            && isNaicsLastIYNotNullAndLessThanFacilityIY(facilitySite, currentAlternateNaicsCode)
        ) { // Alternate NAICS is retired for report year, but no additional alternate

            return null;

        } else if (isNaicsCodeNotNull(currentAlternateNaicsCode)) { // Alternate is not retired for given report year

            return currentAlternateNaicsCode;

        } else { /* currentNaicsCode is retired and mapTo is null,
            or mapTo (alternate) NAICS doesn't exist in CAERS (Unlikely, but fail-safe). */

            return null;

        }
    }

    private boolean isNaicsLastIYNotNullAndLessThanFacilityIY(FacilitySite facilitySite, NaicsCode naicsCode) {
        return naicsCode.getLastInventoryYear() != null
            && naicsCode.getLastInventoryYear() < facilitySite.getEmissionsReport().getYear();
    }

    private boolean isNaicsCodeNotNull(NaicsCode naicsCode) { return naicsCode != null && naicsCode.getCode() != null; }
}
