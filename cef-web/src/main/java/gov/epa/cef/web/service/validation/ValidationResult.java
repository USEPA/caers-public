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
package gov.epa.cef.web.service.validation;

import com.baidu.unbiz.fluentvalidator.ResultCollector;
import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ValidationResult implements Serializable {

    private boolean valid; // Needed for serialization

    private final List<ValidationError> federalErrors;

    private final List<ValidationError> federalWarnings;

    private final List<ValidationError> stateErrors;

    private final List<ValidationError> stateWarnings;

    private final List<ValidationError> facilityInventoryChanges;

    public static final int FEDERAL_ERROR_CODE = 1;

    public static final int FEDERAL_WARNING_CODE = -1;

    public static final int STATE_ERROR_CODE = 2;

    public static final int STATE_WARNING_CODE = -2;

    public static final int FACILITY_INVENTORY_CHANGE_CODE = -3;

    public ValidationResult() {

        this.federalErrors = new ArrayList<>();
        this.federalWarnings = new ArrayList<>();
        this.stateErrors = new ArrayList<>();
        this.stateWarnings = new ArrayList<>();
        this.facilityInventoryChanges = new ArrayList<>();
    }

    public void addError(ValidationError error) {

        this.federalErrors.add(error);
    }

    public void addStateError(ValidationError error) {

        this.stateErrors.add(error);
    }

    public void addStateWarning(ValidationError warning) {

        this.stateWarnings.add(warning);
    }

    public void addWarning(ValidationError warning) {

        this.federalWarnings.add(warning);
    }

    public Collection<ValidationError> getFederalErrors() {

        return Collections.unmodifiableCollection(this.federalErrors);
    }

    public void setFederalErrors(Collection<ValidationError> federalErrors) {

        this.federalErrors.clear();
        if (federalErrors != null) {
            this.federalErrors.addAll(federalErrors);
        }
    }

    public Collection<ValidationError> getFederalWarnings() {

        return Collections.unmodifiableCollection(this.federalWarnings);
    }

    public void setFederalWarnings(Collection<ValidationError> federalWarnings) {

        this.federalWarnings.clear();
        if (federalWarnings != null) {
            this.federalWarnings.addAll(federalWarnings);
        }
    }

    public Collection<ValidationError> getStateErrors() {

        return Collections.unmodifiableCollection(this.stateErrors);
    }

    public void setStateErrors(Collection<ValidationError> stateErrors) {

        this.stateErrors.clear();
        if (stateErrors != null) {
            this.stateErrors.addAll(stateErrors);
        }
    }

    public Collection<ValidationError> getStateWarnings() {

        return Collections.unmodifiableCollection(this.stateWarnings);
    }

    public void setStateWarnings(Collection<ValidationError> stateWarnings) {

        this.stateWarnings.clear();
        if (stateWarnings != null) {
            this.stateWarnings.addAll(stateWarnings);
        }
    }

    public Collection<ValidationError> getFacilityInventoryChanges() {
        return Collections.unmodifiableCollection(this.facilityInventoryChanges);
    }

    public void setFacilityInventoryChanges(Collection<ValidationError> facilityInventoryChanges) {

        this.facilityInventoryChanges.clear();
        if (facilityInventoryChanges != null) {
            this.facilityInventoryChanges.addAll(facilityInventoryChanges);
        }
    }

    public boolean hasAnyWarnings() {

        return this.hasFederalWarnings() || this.hasStateWarnings() || this.hasFacilityInventoryChanges();
    }

    public boolean hasFederalWarnings() {

        return this.federalWarnings.size() > 0;
    }

    public boolean hasStateWarnings() {

        return this.stateWarnings.size() > 0;
    }

    public boolean hasFacilityInventoryChanges() {

        return this.facilityInventoryChanges.size() > 0;
    }

    @JsonIgnore
    public boolean isFederalValid() {

        return this.federalErrors.isEmpty();
    }

    @JsonIgnore
    public boolean isStateValid() {

        return this.stateErrors.isEmpty();
    }

    public boolean isValid() {

        return isFederalValid() && isStateValid();
    }

    public void setIsValid(boolean valid) { this.valid = valid; }

    public ResultCollector<ValidationResult> resultCollector() {

        return validationResult -> {

            if (validationResult.isSuccess() == false) {

                validationResult.getErrors().forEach(error -> {

                    switch (error.getErrorCode()) {
                        case FEDERAL_ERROR_CODE:
                            this.federalErrors.add(error);
                            break;
                        case FEDERAL_WARNING_CODE:
                            this.federalWarnings.add(error);
                            break;
                        case STATE_ERROR_CODE:
                            this.stateErrors.add(error);
                            break;
                        case STATE_WARNING_CODE:
                            this.stateWarnings.add(error);
                            break;
                        case FACILITY_INVENTORY_CHANGE_CODE:
                            this.facilityInventoryChanges.add(error);
                            break;
                        default:
                            // do nothing
                    }
                });
            }

            return this;
        };
    }
}
