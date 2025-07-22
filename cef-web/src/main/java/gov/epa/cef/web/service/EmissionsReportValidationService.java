/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service;

import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.service.validation.ValidationFeature;
import gov.epa.cef.web.service.validation.ValidationResult;
import gov.epa.cef.web.service.dto.QualityCheckResultsDto;

public interface EmissionsReportValidationService {

    /**
     * Validate emissions report
     *
     * @param reportId
     * @return
     */
    ValidationResult validate(long reportId, Boolean semiannual, ValidationFeature... features);

    /**
     * Validate emissions report
     *
     * @param report
     * @return
     */
    ValidationResult validate(EmissionsReport report, Boolean semiannual, ValidationFeature... features);

    /**
     * Validates emissions report & updates validation status accordingly
     * @param reportId
     * @return
     */
    ValidationResult validateAndUpdateStatus(long reportId, Boolean semiannual, ValidationFeature... features);

    /**
     * Gets the QA Check Results DTO by the report id and annual/semiannual flag
     * @param reportId
     * @param semiannual
     * @return
     */
    QualityCheckResultsDto getQAResultsDto(Long reportId, boolean semiannual);

    /**
     * Updates the QA Check Results status by the report id, annual/semiannual flag, and the desired status
     * @param reportId
     * @param semiannual
     * @param status
     * @return
     */
    QualityCheckResultsDto updateQAResultsStatus(Long reportId, boolean semiannual, String status);

    /**
     * Initializes and gets the in-progress QA Check Results DTO by the report id and annual/semiannual flag
     * @param reportId
     * @param semiannual
     * @return
     */
    QualityCheckResultsDto createInProgressQAResults(long reportId, Boolean semiannual);

    /**
     * Update validation status for Threshold Status that do not have ability to run QA checks
     *
     * @param reportId
     */
    void validateAndUpdateThresholdNoQAStatus(long reportId);
}
