/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import {Component, OnInit, Input, OnChanges, AfterContentChecked} from '@angular/core';
import {LookupService} from 'src/app/core/services/lookup.service';
import {FormBuilder, Validators, ValidatorFn, FormGroup, ValidationErrors, AbstractControl} from '@angular/forms';
import {BaseCodeLookup} from 'src/app/shared/models/base-code-lookup';
import {Process} from 'src/app/shared/models/process';
import {FormUtilsService} from 'src/app/core/services/form-utils.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {SccSearchModalComponent} from 'src/app/modules/emissions-reporting/components/scc-search-modal/scc-search-modal.component';
import {AircraftEngineTypeCode} from 'src/app/shared/models/aircraft-engine-type-code';
import {FacilitySite} from 'src/app/shared/models/facility-site';
import {ActivatedRoute} from '@angular/router';
import {EmissionUnitService} from 'src/app/core/services/emission-unit.service';
import {EmissionUnit} from 'src/app/shared/models/emission-unit';
import {OperatingStatus} from 'src/app/shared/enums/operating-status';
import {SharedService} from 'src/app/core/services/shared.service';
import {VariableValidationType} from 'src/app/shared/enums/variable-validation-type';
import {PointSourceSccCode} from 'src/app/shared/models/point-source-scc-code';
import { ConfigPropertyService } from 'src/app/core/services/config-property.service';
import {EmissionsReport} from "src/app/shared/models/emissions-report";
import {forkJoin} from "rxjs";
import {MonthlyReportingPeriod} from "src/app/shared/enums/monthly-reporting-periods";
import { isNullOrUndefined } from 'src/app/shared/utils/general';

@Component({
    selector: 'app-edit-process-info-panel',
    templateUrl: './edit-process-info-panel.component.html',
    styleUrls: ['./edit-process-info-panel.component.scss']
})

export class EditProcessInfoPanelComponent implements OnInit, OnChanges, AfterContentChecked {
    @Input() process: Process;
    @Input() unitIdentifier: string;
    @Input() emissionsUnit: EmissionUnit;
    @Input() sltEditEnabled: boolean;
    @Input() sccEditMode: boolean;
    @Input() sltBillingExemptEnabled: boolean;
    sccAndAircraftCombinations: string[] = [];
    emissionsProcessIdentifiers: string[] = [];
    emissionUnit: EmissionUnit;
    emissionsReportYear: number;
    emissionsReport: EmissionsReport;
    programSystemCode: string;
    sccRetirementYear: number;
    sccWarning: string;
    aircraftSCCcheck = false;
    processHasAETC = false;
    facilityOpCode: BaseCodeLookup;
    facilitySourceTypeCode: BaseCodeLookup;
	semiAnnualReportSubmittedOrApproved = false;
	hasSemiannualData = false;
	sccIsMonthlyReporting = false;
	monthlyReportingEnabled: boolean;
    monthlyReportingPeriods = MonthlyReportingPeriod;
    readonly allMonths = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];

    processForm = this.fb.group({
        aircraftEngineTypeCode: [null],
        operatingStatusCode: [null, [
            Validators.required,
            this.newSfcOperatingValidator(),
            this.existingMonthlyReportingDataValidator()
        ]],
        emissionsProcessIdentifier: ['', [
            Validators.required,
            Validators.maxLength(20)
        ]],
        // Validators set in ngOnInit
        statusYear: [''],
        sccCode: ['', [
            Validators.required,
            Validators.maxLength(20)
        ]],
        sccDescription: ['', [
            this.requiredIfOperating(),
            Validators.maxLength(500)
        ]],
        sccCategory: [''],
        description: ['', [
            Validators.maxLength(200)
        ]],
        initialMonthlyReportingPeriod: [null],
        sltBillingExempt: [null],
        comments: ['', [Validators.maxLength(400)]]
    }, {
        validators: [
            this.checkPointSourceSccCode(),
            this.checkProcessIdentifier(),
            this.legacyAetcValidator(),
            this.checkSccAndAircraftDuplicate(),
            this.operatingStatusCheck(),
            this.statusYearRequiredCheck(),
            this.commentRequiredCheck(),
            this.initialMonthlyReportingPeriodInvalid()]
    });

    operatingSubFacilityStatusValues: BaseCodeLookup[];
    aircraftEngineTypeValue: AircraftEngineTypeCode[];
    aircraftEngineSCC: string[];

    constructor(
        private lookupService: LookupService,
        public formUtils: FormUtilsService,
        private emissionUnitService: EmissionUnitService,
		private propertyService: ConfigPropertyService,
        private route: ActivatedRoute,
        private modalService: NgbModal,
        private sharedService: SharedService,
        private fb: FormBuilder) {
    }

    ngOnInit() {
        this.lookupService.retrieveSubFacilityOperatingStatus()
            .subscribe(result => {
                this.operatingSubFacilityStatusValues = result;
            });

        this.route.data.subscribe((data: { facilitySite: FacilitySite }) => {
            this.facilityOpCode = data.facilitySite.operatingStatusCode;
            this.facilitySourceTypeCode = data.facilitySite.facilitySourceTypeCode;
            this.emissionsReportYear = data.facilitySite.emissionsReport.year;
            this.emissionsReport = data.facilitySite.emissionsReport;
            this.programSystemCode = data.facilitySite?.programSystemCode?.code;
            if (data.facilitySite.emissionsReport.midYearSubmissionStatus === 'SUBMITTED' || data.facilitySite.emissionsReport.midYearSubmissionStatus === 'APPROVED') {
                this.semiAnnualReportSubmittedOrApproved = true;
            }

            this.propertyService.retrieveSltBillingExemptEnabled(this.programSystemCode)
                .subscribe( result => {
                   this.sltBillingExemptEnabled = result;
                });

            forkJoin({
                monthlyReportingEnabled: this.propertyService.retrieveSltMonthlyFuelReportingEnabled(this.programSystemCode),
                monthlyReportingInitialYear: this.propertyService.retrieveSltMonthlyFuelReportingInitialYear(this.programSystemCode)
            })
            .subscribe(({monthlyReportingEnabled, monthlyReportingInitialYear}) => {
                this.monthlyReportingEnabled = monthlyReportingEnabled && (monthlyReportingInitialYear == null || data.facilitySite?.emissionsReport?.year >= monthlyReportingInitialYear);

                if (this.monthlyReportingEnabled) {
                    this.lookupService.retrievePointSourceSccCode(this.process?.sccCode)
                        .subscribe(result => {
                            this.sccIsMonthlyReporting = result?.monthlyReporting;

                            if (result?.monthlyReporting && this.semiAnnualReportSubmittedOrApproved && this.sccIsMonthlyReporting) {
                                for (const rp of this.process.reportingPeriods) {
                                    if (rp.reportingPeriodTypeCode.shortName == this.monthlyReportingPeriods.SEMIANNUAL &&
                                        rp.calculationParameterValue != null && Number(rp.calculationParameterValue) > 0) {

                                        this.hasSemiannualData = true;
                                        break;
                                    }
                                }
                                if (this.hasSemiannualData) {
                                    this.processForm.get('operatingStatusCode').disable();
                                    this.processForm.get('initialMonthlyReportingPeriod').disable();
                                }
                            }
                        });
                }
            });
		});

        this.processForm.get('statusYear').setValidators([
                    Validators.min(1900),
                    Validators.max(this.emissionsReportYear),
                    Validators.pattern('[0-9]*'),
					this.requiredIfNotOperating()]);

        // SCC codes associated with Aircraft Engine Type Codes
        this.aircraftEngineSCC = [
            '2275001000', '2275020000', '2275050011', '2275050012', '2275060011', '2275060012'
        ];

        this.checkAircraftSCC();

        this.processForm.get('sccCode').valueChanges
        .subscribe(value => {
            this.sharedService.emitProcessSccChange(value);
        })

        if (this.sltEditEnabled && this.sccEditMode) {
            this.processForm.disable();
            this.processForm.get('sccCode').enable();
            this.processForm.get('sccDescription').enable();
            this.processForm.get('sccCategory').enable();
        }
    }

    ngAfterContentChecked() {
        if (this.emissionsUnit && this.emissionsUnit.emissionsProcesses) {
            this.emissionsUnit.emissionsProcesses.forEach(process => {
                this.emissionsProcessIdentifiers.push(process.emissionsProcessIdentifier);
                if (process['aircraftEngineTypeCode'] && process['sccCode']) {
                    // if a process is selected to edit, then check to make sure its id isn't equal to the id of the process we are looping through
                    // to avoid comparing its own combination to itself, if it's a new process then skip this check
                    if ((!this.process) || (this.process && process['id'] !== this.process.id)) {
                        const combination = process['aircraftEngineTypeCode'].code + process['sccCode'];
                        this.sccAndAircraftCombinations.push(combination);
                    }
                }
                if (this.process) {
                    this.emissionsProcessIdentifiers = this.emissionsProcessIdentifiers.filter(identifer => identifer.toString() !== this.process.emissionsProcessIdentifier);
                }
            })
        }
    }

    ngOnChanges() {
        if (this.process) {
            this.processForm.reset(this.process);
        }

        if (this.emissionsUnit != null) {
            this.emissionUnitService.retrieve(this.emissionsUnit.id)
                .subscribe(unit => {
                    this.emissionUnit = unit;
                    this.unitIdentifier = unit.unitIdentifier;
                });
        }
        if (this.process?.previousProcess != null || this.process?.initialMonthlyReportingPeriod == null) {
            this.processForm.get('initialMonthlyReportingPeriod').setValue('January');
        }
        else {
            this.processForm.get('initialMonthlyReportingPeriod').setValue(this.process.initialMonthlyReportingPeriod);
        }
        if (!isNullOrUndefined(this.process?.sltBillingExempt)) {
            this.processForm.get('sltBillingExempt').setValue(this.process.sltBillingExempt);
        }
    }

    onChange(newValue) {
        if (newValue) {
            this.processForm.controls.statusYear.reset();

            this.sharedService.emitProcessOpStatusChange(this.processForm.get('operatingStatusCode').value.code);
        }
        this.processForm.controls.description.updateValueAndValidity();
        this.processForm.controls.sccDescription.updateValueAndValidity();
		this.processForm.controls.statusYear.updateValueAndValidity();
		// has to happen twice to catch potentially newly added validators
		// namely when adding or removing the required validator
        this.processForm.controls.description.updateValueAndValidity();
        this.processForm.controls.sccDescription.updateValueAndValidity();
		this.processForm.controls.statusYear.updateValueAndValidity();
    }

    openSccSearchModal() {
        const modalRef = this.modalService.open(SccSearchModalComponent, {
            size: 'xl',
            backdrop: 'static',
            scrollable: true
        });

        modalRef.componentInstance.currentSccCode = this.processForm.get('sccCode').value;
        modalRef.componentInstance.emissionsReport = this.emissionsReport;
        modalRef.componentInstance.process = this.process;

        // update form when modal closes successfully
        modalRef.result.then((modalScc: PointSourceSccCode) => {
            if (modalScc) {
                this.processForm.get('sccCode').setValue(modalScc.code);
                this.processForm.get('sccDescription').setValue(modalScc.description);
                this.processForm.get('sccCategory').setValue(modalScc.category);
				this.sccIsMonthlyReporting = modalScc.fuelUseRequired;
                this.checkAircraftSCC();
            }
        }, () => {
            // needed for dismissing without errors
        });
    }

    // check for aircraft type SCC and associated Aircraft Engine Type Codes
    checkAircraftSCC() {
        this.aircraftSCCcheck = false;
        this.checkForAircraftSCC();
        if (this.aircraftSCCcheck) {
            // get AETC list and set form value
            this.getAircraftEngineCodes();
        }
    }

    // check if aircraft type SCC
    checkForAircraftSCC() {
        const formSccCode = this.processForm.get('sccCode');
        this.aircraftSCCcheck = false;
        for (const scc of this.aircraftEngineSCC) {
            if (scc === formSccCode.value) {
                this.aircraftSCCcheck = true;
                this.processHasAETC = true;
                break;
            }
        }

        if (this.aircraftSCCcheck) {
            this.processForm.controls.aircraftEngineTypeCode.setValidators([Validators.required]);
            this.processForm.controls.aircraftEngineTypeCode.updateValueAndValidity();
        } else if (!this.aircraftSCCcheck) {
            this.processForm.controls.aircraftEngineTypeCode.setValue(null);
            this.processForm.controls.aircraftEngineTypeCode.setValidators(null);
            this.processForm.controls.aircraftEngineTypeCode.updateValueAndValidity();
            this.aircraftEngineTypeValue = null;
            this.processHasAETC = false;
        }
    }

    // get AETC list
    getAircraftEngineCodes() {
        let codeInList = false;
        this.lookupService.retrieveCurrentAircraftEngineCodes(this.processForm.get('sccCode').value, this.emissionsReportYear)
            .subscribe(result => {
                this.aircraftEngineTypeValue = result;

                // check if process AETC is valid
                if (this.aircraftSCCcheck && this.aircraftEngineTypeValue !== null && this.aircraftEngineTypeValue !== undefined) {
                    if (this.process !== undefined && this.process.aircraftEngineTypeCode !== null) {
                        for (const item of this.aircraftEngineTypeValue) {

                            if (item.code === this.process.aircraftEngineTypeCode.code) {
                                codeInList = true;
                                this.processForm.controls.aircraftEngineTypeCode.setValue(item);
                                break;
                            }
                        }
                    }
                    if (!codeInList) {
                        this.processForm.controls.aircraftEngineTypeCode.setValue(null);
                        this.processForm.controls.aircraftEngineTypeCode.setValidators([Validators.required]);
                        this.processForm.controls.aircraftEngineTypeCode.updateValueAndValidity();
                    }
                }
            });
    }

    onSubmit() {

        // console.log(this.processForm);

        // let process = new Process();
        // Object.assign(process, this.processForm.value);
        // console.log(process);
    }

    checkPointSourceSccCode(): ValidatorFn {
        return (control: FormGroup): ValidationErrors | null => {
            let isValidScc;

            if (control.get('sccCode') !== null && control.get('sccCode').value !== null && control.get('sccCode').value !== '') {

                this.lookupService.retrievePointSourceSccCode(control.get('sccCode').value)
                    .subscribe(result => {
                        isValidScc = result;

                        if (isValidScc !== null) {
                            if (isValidScc.lastInventoryYear !== null && (isValidScc.lastInventoryYear >= this.emissionsReportYear)) {
                                this.sccRetirementYear = isValidScc.lastInventoryYear;
                                this.sccWarning = 'Warning: ' + control.get('sccCode').value + ' has a retirement date of ' + this.sccRetirementYear
                                    + '. If applicable, you may want to add a more recent code.';
                            } else if (isValidScc.lastInventoryYear !== null && (isValidScc.lastInventoryYear < this.emissionsReportYear)) {
                                this.sccRetirementYear = isValidScc.lastInventoryYear;
                                control.get('sccCode').markAsTouched();
                                control.get('sccCode').setErrors({sccCodeRetired: true});
                                this.sccWarning = null;
                            } else if (isValidScc.lastInventoryYear === null) {
                                this.sccWarning = null;
                            }
                        } else if (result === null) {
                            control.get('sccCode').markAsTouched();
                            control.get('sccCode').setErrors({sccCodeInvalid: true});
                            this.sccWarning = null;
                        } else {
                            this.sccWarning = null;
                        }
                    });
            }
            return null;
        };
    }

    // check for duplicate process identifier
    checkProcessIdentifier(): ValidatorFn {
        return (control: FormGroup): ValidationErrors | null => {
            const procId: string = control.get('emissionsProcessIdentifier').value;
            if (this.emissionsProcessIdentifiers) {
                if (!procId || procId.trim() === '') {
                    control.get('emissionsProcessIdentifier').setErrors({required: true});
                } else {

                    for (const id of this.emissionsProcessIdentifiers) {
                        if (id.trim().toLowerCase() === procId.trim().toLowerCase()) {
                            return {invalidDuplicateProcessIdetifier: true};
                        }
                    }
                }
            }
            return null;
        };
    }

    legacyAetcValidator(): ValidatorFn {
        return (control: FormGroup): { [key: string]: any } | null => {
            // show legacy AETC error message if the process should have an AETC, if there was already an existing one,
            // and if the user hasn't selected a new code
            if (this.processHasAETC && this.process && this.process.aircraftEngineTypeCode
                && this.process.aircraftEngineTypeCode.lastInventoryYear
                && this.process.aircraftEngineTypeCode.lastInventoryYear < this.emissionsReportYear
                && (control.get('aircraftEngineTypeCode') === null || control.get('aircraftEngineTypeCode').value === null)) {
                return {legacyAetc: {value: `${this.process.aircraftEngineTypeCode.faaAircraftType} - ${this.process.aircraftEngineTypeCode.engine}`}};
            }
            return null;
        };
    }


    checkSccAndAircraftDuplicate(): ValidatorFn {
        return (control: FormGroup): ValidationErrors | null => {
            if ((control.get('aircraftEngineTypeCode').value) && (control.get('sccCode').value)) {
                const codeCombo = control.get('aircraftEngineTypeCode').value.code + control.get('sccCode').value;
                this.sccAndAircraftCombinations.forEach(combination => {
                    if (codeCombo === combination) {
                        control.get('aircraftEngineTypeCode').setErrors({invalidAircraftSCCCombination: true});
                        control.get('sccCode').setErrors({invalidAircraftSCCCombination: true});
                    } else {
                        control.get('sccCode').setErrors(null);
                        control.get('aircraftEngineTypeCode').setErrors(null);
                    }
                });
            } else {
                return null;
            }
        };
    }

    operatingStatusCheck(): ValidatorFn {
        return (control: FormGroup): ValidationErrors | null => {
            const controlStatus = control.get('operatingStatusCode').value;

            // check process operating status if facility source type is not landfill
            if (this.facilityOpCode && controlStatus) {
                if (this.facilitySourceTypeCode === null || (this.facilitySourceTypeCode?.code !== VariableValidationType.LANDFILL_SOURCE_TYPE)) {
                // if facility operating status is TS/PS, then process status must be shutdown
                if (this.facilityOpCode.code === OperatingStatus.TEMP_SHUTDOWN
                    && controlStatus.code !== OperatingStatus.PERM_SHUTDOWN
                    && controlStatus.code !== OperatingStatus.TEMP_SHUTDOWN) {
                    return {invalidStatusCodeTS: true};
                } else if (this.facilityOpCode.code === OperatingStatus.PERM_SHUTDOWN
                    && controlStatus.code !== OperatingStatus.PERM_SHUTDOWN) {
                    return {invalidStatusCodePS: true};
				// if facility is not shutdown, then process status must be shutdown if unit status is TS/PS
                } else if (this.facilityOpCode.code !== OperatingStatus.PERM_SHUTDOWN
					&& this.facilityOpCode.code !== OperatingStatus.PERM_SHUTDOWN
					&& this.emissionUnit && this.emissionUnit?.operatingStatusCode.code) {
                    if (this.emissionUnit.operatingStatusCode.code === OperatingStatus.TEMP_SHUTDOWN
                        && controlStatus.code !== OperatingStatus.PERM_SHUTDOWN
                        && controlStatus.code !== OperatingStatus.TEMP_SHUTDOWN) {
                        return {invalidStatusCodeUnitTS: true};
                    } else if (this.emissionUnit?.operatingStatusCode.code === OperatingStatus.PERM_SHUTDOWN
                        && controlStatus.code !== OperatingStatus.PERM_SHUTDOWN) {
                        return {invalidStatusCodeUnitPS: true};
                    }
                }
                }
			}
        }
        return null;
    }

    statusYearRequiredCheck(): ValidatorFn {
        return (control: FormGroup): ValidationErrors | null => {
            const statusYear = control.get('statusYear').value;
			const controlStatus = control.get('operatingStatusCode').value;

			if (controlStatus) {
	            if ((statusYear === null || statusYear === '') && controlStatus.code !== OperatingStatus.OPERATING) {
	                control.get('statusYear').setErrors({statusYearRequiredFailed: true});
	            }
			}
            return null;
        };
    }

    requiredIfOperating() {
        return (formControl => {
            if (!formControl.parent) {
                return null;
            }

            if (this.processForm.get('operatingStatusCode').value
                && this.processForm.get('operatingStatusCode').value.code.includes(OperatingStatus.OPERATING)) {
				formControl.addValidators(Validators.required);
            } else {
				if (formControl.hasValidator(Validators.required)) {
					formControl.removeValidators(Validators.required);
				}
			}
            return null;
        });
    }

    commentRequiredCheck() {
        return (control: FormGroup): ValidationErrors | null => {
            const sltBillingExempt = control.get('sltBillingExempt').value;
            const comments = control.get('comments').value;

            if (sltBillingExempt && (comments === null || comments === '')) {
                control.get('sltBillingExempt').setErrors({billingExemptCommentRequired: true});
            } else {
                control.get('sltBillingExempt').setErrors(null);
            }

            return null;
        };
    }

	requiredIfNotOperating() {
        return (formControl => {
            if (!formControl.parent) {
                return null;
            }

            if (this.processForm.get('operatingStatusCode').value
                && this.processForm.get('operatingStatusCode').value.code.includes(OperatingStatus.OPERATING)) {
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
            if (control.value && control.value.code !== OperatingStatus.OPERATING && !this.process?.previousProcess) {
                return {newSfcOperating: {value: control.value.code}};
            }
            return null;
        };
    }

    existingMonthlyReportingDataValidator(): ValidatorFn {
        return (control: AbstractControl): {[key: string]: any} | null => {
            if (control.value && control.value.code !== OperatingStatus.OPERATING && this.sccIsMonthlyReporting) {

                const initialMonthlyReportingPeriod = this.processForm.get('initialMonthlyReportingPeriod').value;
                let initialMonthIndex = this.allMonths.indexOf(initialMonthlyReportingPeriod);

                for (let i = this.process.reportingPeriods.length - 1; i >= 0; i--) {
                    const period = this.process.reportingPeriods[i];
                    const monthIndex = this.allMonths.indexOf(period.reportingPeriodTypeCode.shortName);
                    if (monthIndex >= initialMonthIndex
                        && ((period.calculationParameterValue != null && Number(period.calculationParameterValue) > 0)
                        || (period.fuelUseValue != null && Number(period.fuelUseValue) > 0))) {
                            return {processExistingMonthlyReportingData: {value: control.value.code}};
                    }
                }
            }
            return null;
        };
    }

    initialMonthlyReportingPeriodInvalid(): ValidatorFn {
        return (control: FormGroup): ValidationErrors | null => {
            const initialMonthlyReportingPeriod = control.get('initialMonthlyReportingPeriod').value;
            let procMonthIndex = this.allMonths.indexOf(initialMonthlyReportingPeriod);
            let unitMonthIndex = this.allMonths.indexOf(this.emissionUnit?.initialMonthlyReportingPeriod);

            if (this.process?.previousProcess == null && this.semiAnnualReportSubmittedOrApproved && procMonthIndex < 6
                && control.get('initialMonthlyReportingPeriod').enabled) {
                control.get('initialMonthlyReportingPeriod').setErrors({invalidMonthSemiAnnualSubmitted: true});
            }
            else if (this.monthlyReportingEnabled && procMonthIndex < unitMonthIndex) {
                control.get('initialMonthlyReportingPeriod').setErrors({processMonthPrecedesUnitMonth: true});
            }
            return null;
        };
    }

}
