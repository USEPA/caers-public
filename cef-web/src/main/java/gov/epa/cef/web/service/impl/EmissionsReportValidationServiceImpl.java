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
package gov.epa.cef.web.service.impl;

import com.baidu.unbiz.fluentvalidator.FluentValidator;
import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.domain.QAResultsStatus;
import gov.epa.cef.web.domain.QualityCheckResults;
import gov.epa.cef.web.exception.ApplicationErrorCode;
import gov.epa.cef.web.exception.ApplicationException;
import gov.epa.cef.web.service.dto.QualityCheckResultsDto;
import gov.epa.cef.web.domain.ValidationStatus;
import gov.epa.cef.web.exception.NotExistException;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.QualityCheckResultsRepository;
import gov.epa.cef.web.service.EmissionsReportValidationService;
import gov.epa.cef.web.service.mapper.EmissionsReportMapper;
import gov.epa.cef.web.service.validation.*;
import gov.epa.cef.web.service.validation.validator.IEmissionsReportValidator;
import gov.epa.cef.web.service.validation.validator.ISemiannualReportValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class EmissionsReportValidationServiceImpl implements EmissionsReportValidationService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final EmissionsReportRepository emissionsReportRepository;

    private final QualityCheckResultsRepository qualityCheckResultsRepository;

    private final ValidationRegistry validationRegistry;

    @Autowired
    private EmissionsReportMapper emissionsReportMapper;

    @Autowired
    EmissionsReportValidationServiceImpl(EmissionsReportRepository emissionsReportRepository,
                                         QualityCheckResultsRepository qualityCheckResultsRepository,
                                         ValidationRegistry validationRegistry) {

        this.emissionsReportRepository = emissionsReportRepository;
        this.validationRegistry = validationRegistry;
        this.qualityCheckResultsRepository = qualityCheckResultsRepository;
    }

    public ValidationResult validate(long reportId, Boolean semiannual, ValidationFeature... requestedFeatures) {

        return this.emissionsReportRepository.findById(reportId)
            .map(report -> validate(report, semiannual, requestedFeatures))
            .orElseThrow(() -> new NotExistException("Emissions Report", reportId));
    }

    public ValidationResult validate(@NotNull EmissionsReport report,
                                     Boolean semiannual,
                                     ValidationFeature... requestedFeatures) {

        ValidationResult result = new ValidationResult();

        CefValidatorContext cefContext =
            new CefValidatorContext(this.validationRegistry, "validation/emissionsreport")
                .enable(requestedFeatures);

        if (!semiannual) {
            // Run thru all validators
            cefContext.enable(ValidationFeature.ANNUAL);

            FluentValidator.checkAll().failOver()
                .withContext(cefContext)
                .on(report, this.validationRegistry.createValidatorChain(IEmissionsReportValidator.class))
                .doValidate()
                .result(result.resultCollector());
        }
        else {
            cefContext.enable(ValidationFeature.SEMIANNUAL);

            FluentValidator.checkAll().failOver()
                .withContext(cefContext)
                .on(report, this.validationRegistry.createValidatorChain(ISemiannualReportValidator.class))
                .doValidate()
                .result(result.resultCollector());
        }

        return result;
    }

    @Override
    public ValidationResult validateAndUpdateStatus(long reportId, Boolean semiannual, ValidationFeature... requestedFeatures) {

        QualityCheckResults qaCheckResults = qualityCheckResultsRepository
            .findByReportIdAndIsSemiannual(reportId, semiannual).orElse(null);

        ValidationResult result;

        EmissionsReport report = this.emissionsReportRepository.findById(reportId).orElse(null);

        if (report != null && qaCheckResults != null) {

            result = validate(reportId, semiannual, requestedFeatures);

            qaCheckResults.setCurrentResults(result);
            qaCheckResults.setFederalErrorsCount(result.getFederalErrors().size());
            qaCheckResults.setFederalWarningsCount(result.getFederalWarnings().size());
            qaCheckResults.setStateErrorsCount(result.getStateErrors().size());
            qaCheckResults.setStateWarningsCount(result.getStateWarnings().size());
            qaCheckResults.setFacilityInventoryChangesCount(result.getFacilityInventoryChanges().size());
            qaCheckResults.setQaResultsStatus(QAResultsStatus.COMPLETE);

            doUpdateValidationStatus(report, result, semiannual);

            qaCheckResults.setEmissionsReport(report);
            this.emissionsReportRepository.save(report);
            qualityCheckResultsRepository.save(qaCheckResults);
        } else {
            throw new ApplicationException(
                ApplicationErrorCode.E_INTERNAL_ERROR,
                "The Emissions Report and/or QA Check Results is/are unexpectedly null");
        }

        return result;
    }

    @Override
    public QualityCheckResultsDto getQAResultsDto(Long reportId, boolean semiannual) {

        QualityCheckResults qualityCheckResults =
            this.qualityCheckResultsRepository.findByReportIdAndIsSemiannual(reportId, semiannual)
            .orElse(null);
        if (qualityCheckResults != null) {
            QualityCheckResultsDto dto = emissionsReportMapper.toQualityCheckResultsDto(qualityCheckResults);
            if (qualityCheckResults.getCurrentResults() != null) {
                dto.getCurrentResults().setIsValid(qualityCheckResults.getCurrentResults().isValid());
            }
            return dto;
        }
        return null;
    }

    @Override
    public QualityCheckResultsDto updateQAResultsStatus(Long reportId, boolean semiannual, String status) {
        QualityCheckResults qualityCheckResults = this.qualityCheckResultsRepository
            .findByReportIdAndIsSemiannual(reportId, semiannual)
            .orElse(null);
        if (qualityCheckResults != null) {
            if (status.equals(QAResultsStatus.COMPLETE.value())) {
                qualityCheckResults.setQaResultsStatus(QAResultsStatus.COMPLETE);
            } else if (status.equals(QAResultsStatus.IN_PROGRESS.value())) {
                qualityCheckResults.setQaResultsStatus(QAResultsStatus.IN_PROGRESS);
            } else if (status.equals(QAResultsStatus.INCOMPLETE.value())) {
                qualityCheckResults.setQaResultsStatus(QAResultsStatus.INCOMPLETE);
            } else {
                throw new ApplicationException(ApplicationErrorCode.E_INTERNAL_ERROR,
                    "The QAResultsStatus provided is not a valid status for reportId: " + reportId);
            }
            return this.emissionsReportMapper
                .toQualityCheckResultsDto(this.qualityCheckResultsRepository.save(qualityCheckResults));
        }
        return null;
    }

    public QualityCheckResultsDto createInProgressQAResults(long reportId, Boolean semiannual) {

        QualityCheckResults qaCheckResults = qualityCheckResultsRepository
                .findByReportIdAndIsSemiannual(reportId, semiannual).orElse(null);

        EmissionsReport report = this.emissionsReportRepository.findById(reportId).orElse(null);

        if (qaCheckResults == null) {
            qaCheckResults = new QualityCheckResults();
        } else {
            qaCheckResults.clearResults();
        }
        qaCheckResults.setEmissionsReport(report);
        qaCheckResults.setIsSemiannual(semiannual);
        qaCheckResults.setQaResultsStatus(QAResultsStatus.IN_PROGRESS);
        return emissionsReportMapper.toQualityCheckResultsDto(qualityCheckResultsRepository.save(qaCheckResults));
    }

    public void validateAndUpdateThresholdNoQAStatus(long reportId) {
        this.emissionsReportRepository.findById(reportId).ifPresent(report -> {
            report.setValidationStatus(ValidationStatus.PASSED);
            this.emissionsReportRepository.save(report);
        });
    }

    private void doUpdateValidationStatus(EmissionsReport report, ValidationResult result, boolean semiannual) {

        if (report != null) {

            if (result.hasAnyWarnings() && result.isValid()) {

                if (semiannual) {
                    report.setValidationStatus(ValidationStatus.SEMIANNUAL_PASSED_WARNINGS);
                }
                else {
                    report.setValidationStatus(ValidationStatus.PASSED_WARNINGS);
                }

            } else if (result.isValid()) {

                if (semiannual) {
                    report.setValidationStatus(ValidationStatus.SEMIANNUAL_PASSED);
                }
                else {
                    report.setValidationStatus(ValidationStatus.PASSED);
                }

            } else {

                report.setValidationStatus(ValidationStatus.UNVALIDATED);
            }
        }
    }

}
