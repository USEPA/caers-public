/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import {Component, Input, OnInit, OnChanges} from '@angular/core';
import {Control} from 'src/app/shared/models/control';
import {BaseCodeLookup} from 'src/app/shared/models/base-code-lookup';
import {FormBuilder, Validators, ValidatorFn, FormGroup, ValidationErrors, AbstractControl} from '@angular/forms';
import {LookupService} from 'src/app/core/services/lookup.service';
import {FormUtilsService} from 'src/app/core/services/form-utils.service';
import {ControlService} from 'src/app/core/services/control.service';
import {FacilitySite} from 'src/app/shared/models/facility-site';
import {ActivatedRoute} from '@angular/router';
import {InventoryYearCodeLookup} from 'src/app/shared/models/inventory-year-code-lookup';
import {legacyItemValidator} from 'src/app/modules/shared/directives/legacy-item-validator.directive';
import {VariableValidationType} from 'src/app/shared/enums/variable-validation-type';
import {OperatingStatus} from 'src/app/shared/enums/operating-status';
import {NgbDate} from '@ng-bootstrap/ng-bootstrap';
import { numberValidator } from 'src/app/modules/shared/directives/number-validator.directive';
import { wholeNumberValidator } from 'src/app/modules/shared/directives/whole-number-validator.directive';

@Component({
    selector: 'app-edit-control-device-info-panel',
    templateUrl: './edit-control-device-info-panel.component.html',
    styleUrls: ['./edit-control-device-info-panel.component.scss'],
})

export class EditControlDeviceInfoPanelComponent implements OnInit, OnChanges {
    @Input() control: Control;
    @Input() year: number;
    controlIdentifiers: string[] = [];
    facilityOpCode: BaseCodeLookup;
    facilitySourceTypeCode: BaseCodeLookup;
    startDateErrorMsg: string;
    upgradeDateErrorMsg: string;
    endDateErrorMsg: string;

    controlDeviceForm = this.fb.group({
        identifier: ['', Validators.required],
        percentControl: ['', [
            Validators.max(100.0),
            Validators.min(1),
            Validators.pattern('^[0-9]{1,4}([\.][0-9]{1})?$')
        ]],
        operatingStatusCode: [null, [
            Validators.required,
            this.newSfcOperatingValidator()
        ]],
        // Validators set in ngOnInit
        statusYear: [''],
        controlMeasureCode: [null, [Validators.required]],
        numberOperatingMonths: [null, [
            Validators.max(12.0),
            Validators.min(1),
            wholeNumberValidator()
        ]],
        upgradeDescription: [null, [
            Validators.maxLength(200)
        ]],
        startDate: [null],
        upgradeDate: [null],
        endDate: [null],
        description: ['', [
            Validators.maxLength(200)
        ]],
        comments: [null, Validators.maxLength(400)]
    }, {
        validators: [
            this.controlIdentifierCheck(),
            this.facilitySiteStatusCheck(),
            this.controlDatesCheck(),
            this.percentControlRequiredValidator()
        ]
    });

    subFacilityOperatingStatusValues: BaseCodeLookup[];
    controlMeasureCode: InventoryYearCodeLookup[];

    constructor(private fb: FormBuilder,
                private lookupService: LookupService,
                public formUtils: FormUtilsService,
                private controlService: ControlService,
                private route: ActivatedRoute
    ) {
    }

    ngOnInit() {

        this.lookupService.retrieveSubFacilityOperatingStatus()
            .subscribe(result => {
                this.subFacilityOperatingStatusValues = result;
            });

        this.controlDeviceForm.get('controlMeasureCode').setValidators([Validators.required, legacyItemValidator(this.year, 'Control Measure Code', 'description')]);

        this.lookupService.retrieveCurrentControlMeasureCodes(this.year)
            .subscribe(result => {
                this.controlMeasureCode = result;
            });

        this.route.data
            .subscribe((data: { facilitySite: FacilitySite }) => {
                this.facilityOpCode = data.facilitySite.operatingStatusCode;
                this.facilitySourceTypeCode = data.facilitySite.facilitySourceTypeCode;
                this.controlService.retrieveForFacilitySite(data.facilitySite.id)
                    .subscribe(controls => {
                        controls.forEach(c => {
                            this.controlIdentifiers.push(c.identifier);
                        });

                        // if a control is being edited then filter that identifer out the list so the validator check doesnt identify it as a duplicate
                        if (this.control) {
                            this.controlIdentifiers = this.controlIdentifiers.filter(identifer => identifer.toString() !== this.control.identifier);

                        }

                    });
                this.controlDeviceForm.get('statusYear').setValidators([
                    this.requiredIfNotOperating(),
                    Validators.min(1900),
                    Validators.max(data.facilitySite.emissionsReport.year),
                    numberValidator()])
            });

        if (this.control) {
            this.controlDeviceForm.get('startDate').setValue(this.transformDate(this.control.startDate));
            this.controlDeviceForm.get('upgradeDate').setValue(this.transformDate(this.control.upgradeDate));
            this.controlDeviceForm.get('endDate').setValue(this.transformDate(this.control.endDate));
        }

    }

    ngOnChanges() {

        this.controlDeviceForm.reset(this.control);
    }

    onChange(newValue) {
        if (newValue) {
            this.controlDeviceForm.controls.statusYear.reset();
        }
        this.controlDeviceForm.controls.statusYear.markAsTouched();

		this.controlDeviceForm.controls.statusYear.updateValueAndValidity();
		// has to happen twice to catch potentially newly added validators
		// namely when adding or removing the required validator
		this.controlDeviceForm.controls.statusYear.updateValueAndValidity();
    }

    transformDate(date) {
        if (date) {
            const existingDate = new Date(date);
            let transformedDate = null;
            date = new Date(existingDate.setMinutes(existingDate.getMinutes() + existingDate.getTimezoneOffset()));

            transformedDate = new NgbDate(date.getFullYear(),
                date.getMonth() + 1,
                date.getDate());

            return transformedDate;
        }

        return null;
    }

    controlIdentifierCheck(): ValidatorFn {
        return (control: FormGroup): ValidationErrors | null => {
            const controlId: string = control.get('identifier').value;
            if (this.controlIdentifiers) {
                if (!controlId || controlId.trim() === '') {
                    control.get('identifier').setErrors({required: true});
                } else {

                    for (const id of this.controlIdentifiers) {
                        if (id.trim().toLowerCase() === controlId.trim().toLowerCase()) {
                            return {duplicateControlIdentifier: true};
                        }
                    }
                }

            }
            return null;
        };
    }

    facilitySiteStatusCheck(): ValidatorFn {
        return (control: FormGroup): ValidationErrors | null => {
            const controlStatus = control.get('operatingStatusCode').value;

            if (this.facilityOpCode && controlStatus
                && (this.facilitySourceTypeCode === null || (this.facilitySourceTypeCode.code !== VariableValidationType.LANDFILL_SOURCE_TYPE))) {

                if (this.facilityOpCode.code === OperatingStatus.TEMP_SHUTDOWN
                    && controlStatus.code !== OperatingStatus.PERM_SHUTDOWN
                    && controlStatus.code !== OperatingStatus.TEMP_SHUTDOWN) {
                    return {invalidStatusCodeTS: true};
                } else if (this.facilityOpCode.code === OperatingStatus.PERM_SHUTDOWN
                    && controlStatus.code !== OperatingStatus.PERM_SHUTDOWN) {
                    return {invalidStatusCodePS: true};
                }
            }
            return null;
        };
    }

    controlDatesCheck(): ValidatorFn {
        return (control: FormGroup): ValidationErrors | null => {
            const start = control.get('startDate').value;
            const upgrade = control.get('upgradeDate').value;
            const end = control.get('endDate').value;
            const maxDateRange = new Date(2050, 11, 31);
            const minDateRange = new Date(1900, 0, 1);

            const startDate = start ? new Date(start.year, start.month - 1, start.day) : null;
            const endDate = end ? new Date(end.year, end.month - 1, end.day) : null;
            const upgradeDate = upgrade ? new Date(upgrade.year, upgrade.month - 1, upgrade.day) : null;

            if (endDate) {
                if (endDate.toString() === 'Invalid Date') {
                    this.endDateErrorMsg = 'Date is invalid.';
                } else if (endDate > maxDateRange || endDate < minDateRange) {
                    this.endDateErrorMsg = 'Date cannot be before 1900-01-01 or after 2050-12-31.';
                } else {
                    this.endDateErrorMsg = null;
                }
            }
            if (!this.endDateErrorMsg || !endDate) {
                control.get('endDate').setErrors(null);
            } else {
                control.get('endDate').markAsTouched();
                control.get('endDate').setErrors({endDateInvalid: true});
            }

            if (startDate) {
                if (startDate.toString() === 'Invalid Date') {
                    this.startDateErrorMsg = 'Date is invalid.';
                } else if (startDate && (startDate > maxDateRange || startDate < minDateRange)) {
                    this.startDateErrorMsg = 'Date cannot be before 1900-01-01 or after 2050-12-31.';
                } else if (startDate && endDate && startDate > endDate) {
                    this.startDateErrorMsg = 'Control Start Date must be before Control End Date.';
                } else {
                    this.startDateErrorMsg = null;
                }
            }
            if (!this.startDateErrorMsg || !startDate) {
                control.get('startDate').setErrors(null);
            } else {
                control.get('startDate').markAsTouched();
                control.get('startDate').setErrors({startDateInvalid: true});
            }

            if (upgradeDate) {
                if (upgradeDate.toString() === 'Invalid Date') {
                    this.upgradeDateErrorMsg = 'Date is invalid.';
                } else if (upgradeDate > maxDateRange || upgradeDate < minDateRange) {
                    this.upgradeDateErrorMsg = 'Date cannot be before 1900-01-01 or after 2050-12-31.';
                } else if ((startDate && startDate > upgradeDate) || (endDate && upgradeDate > endDate)) {
                    this.upgradeDateErrorMsg = 'Control Upgrade Date must be after Control Start Date and before Control End Date.';
                } else {
                    this.upgradeDateErrorMsg = null;
                }
            }
            if (!this.upgradeDateErrorMsg || !upgradeDate) {
                control.get('upgradeDate').setErrors(null);
            } else {
                control.get('upgradeDate').markAsTouched();
                control.get('upgradeDate').setErrors({upgradeDateInvalid: true});
            }

            return null;
        };
    }

	requiredIfNotOperating() {
        return (formControl => {
            if (!formControl.parent) {
                return null;
            }

            if (this.controlDeviceForm.get('operatingStatusCode').value
                && this.controlDeviceForm.get('operatingStatusCode').value.code.includes(OperatingStatus.OPERATING)) {
					if (formControl.hasValidator(Validators.required)) {
						formControl.removeValidators(Validators.required);
					}
            } else {
				formControl.addValidators(Validators.required);
			}
            return null;
        });
    }

    /**
     * Require newly created Sub-Facility Components to be Operating
     */
    newSfcOperatingValidator(): ValidatorFn {
        return (control: AbstractControl): {[key: string]: any} | null => {
            if (control.value && control.value.code !== OperatingStatus.OPERATING && !this.control?.previousControl) {
                return {newSfcOperating: {value: control.value.code}};
            }
            return null;
        };
    }

    percentControlRequiredValidator(): ValidatorFn {
        return (formGroup: FormGroup): ValidationErrors | null => {
            const controlStatusCode = formGroup.controls.operatingStatusCode.value?.code;
            const percentControl = formGroup.controls.percentControl.value;

            if (!percentControl &&
                controlStatusCode &&
                controlStatusCode !== OperatingStatus.PERM_SHUTDOWN &&
                controlStatusCode !== OperatingStatus.TEMP_SHUTDOWN
            ) {
                formGroup.controls.percentControl.markAsTouched();
                formGroup.controls.percentControl.setErrors({percentControlRequired: true});
                formGroup.controls.percentControl.addValidators(Validators.required);
            } else {
                formGroup.controls.percentControl.setErrors(null);
                formGroup.controls.percentControl.removeValidators(Validators.required);
            }

            return null;
        };
    }

}
