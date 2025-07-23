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
