/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
