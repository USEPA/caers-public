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

import gov.epa.cef.web.config.SLTPropertyName;
import gov.epa.cef.web.domain.Emission;
import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.domain.ReportHistory;
import gov.epa.cef.web.repository.EmissionRepository;
import gov.epa.cef.web.repository.ReportHistoryRepository;
import gov.epa.cef.web.provider.system.SLTPropertyProvider;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.ValidationRegistry;
import gov.epa.cef.web.service.validation.validator.BaseValidator;
import gov.epa.cef.web.service.validation.validator.IEmissionsReportValidator;
import gov.epa.cef.web.util.ConstantUtils;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnnualEmissionsReportValidator
    extends BaseValidator<EmissionsReport>
    implements IEmissionsReportValidator {

    Logger logger = LoggerFactory.getLogger(AnnualEmissionsReportValidator.class);

    @Autowired
    private EmissionRepository emissionRepo;

    @Autowired
    private ReportHistoryRepository historyRepo;

    @Autowired
    private SLTPropertyProvider	sltPropertyProvider;

    private static final String USER_ROLE_REVIEWER = "Reviewer";

    @Override
    public void compose(FluentValidator validator,
                        ValidatorContext validatorContext,
                        EmissionsReport emissionsReport) {

        ValidationRegistry registry = getCefValidatorContext(validatorContext).getValidationRegistry();

        // add more validators as needed
        validator.onEach(emissionsReport.getFacilitySites(),
            registry.findOneByType(AnnualFacilitySiteValidator.class));
    }

    @Override
    public boolean validate(ValidatorContext validatorContext, EmissionsReport report) {

        boolean valid = true;

        CefValidatorContext context = getCefValidatorContext(validatorContext);

        if (report.getYear() == null) {

            // prevented by db constraints
            valid = false;
            context.addFederalError(ValidationField.REPORT_YEAR.value(), "report.year.required");

        } else if (report.getYear().intValue() < Calendar.getInstance().get(Calendar.YEAR) - 1) {

            valid = false;
            context.addFederalWarning(ValidationField.REPORT_YEAR.value(), "report.year.min", "" + (Calendar.getInstance().get(Calendar.YEAR) - 1));
        }

        if (report.getProgramSystemCode() == null) {

            // prevented by db constraints
            valid = false;
            context.addFederalError(ValidationField.REPORT_PROGRAM_SYSTEM_CODE.value(), "report.programSystemCode.required");
        }

        if (!report.getFacilitySites().isEmpty() && ConstantUtils.STATUS_OPERATING.equals(report.getFacilitySites().get(0).getOperatingStatusCode().getCode())) {

            List<Emission> emissionsList = emissionRepo.findAllByReportId(report.getId()).stream()
                .filter(e -> (e.getEmissionsCalcMethodCode().getTotalDirectEntry() == true || e.getTotalManualEntry() == true))
                .collect(Collectors.toList());

            List<ReportHistory> attachmentList = historyRepo.findByEmissionsReportIdOrderByActionDate(report.getId()).stream()
                .filter(a -> (a.getReportAttachmentId() != null && a.isFileDeleted() == false && !USER_ROLE_REVIEWER.equalsIgnoreCase(a.getUserRole())))
                .collect(Collectors.toList());

            if (emissionsList.size() > 0 && attachmentList.isEmpty() &&
                sltPropertyProvider.getBoolean(SLTPropertyName.SLTFeatureAttachmentRequired, report.getProgramSystemCode().getCode())
            ) {
                valid = false;
                context.addFederalError(
                    ValidationField.REPORT_ATTACHMENT.value(),
                    "report.reportAttachment.required",
                    createValidationDetails(report));
            }
        }

        return valid;
    }

    private ValidationDetailDto createValidationDetails(EmissionsReport source) {

        String description = MessageFormat.format("Attachments ", source.getId());

        ValidationDetailDto dto = new ValidationDetailDto(source.getId(), source.getId().toString(), EntityType.REPORT_ATTACHMENT, description);
        return dto;
    }
}
