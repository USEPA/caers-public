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
import {Component, OnInit, Input, OnChanges} from '@angular/core';
import {FormBuilder, Validators, ValidatorFn, FormGroup, ValidationErrors, AbstractControl} from '@angular/forms';
import {EmissionUnit} from 'src/app/shared/models/emission-unit';
import {LookupService} from 'src/app/core/services/lookup.service';
import {BaseCodeLookup} from 'src/app/shared/models/base-code-lookup';
import {FormUtilsService} from 'src/app/core/services/form-utils.service';
import {UnitMeasureCode} from 'src/app/shared/models/unit-measure-code';
import {ToastrService} from 'ngx-toastr';
import {EmissionUnitService} from 'src/app/core/services/emission-unit.service';
import {ActivatedRoute} from '@angular/router';
import {FacilitySite} from 'src/app/shared/models/facility-site';
import {OperatingStatus} from 'src/app/shared/enums/operating-status';
import {VariableValidationType} from 'src/app/shared/enums/variable-validation-type';
import {ConfigPropertyService} from 'src/app/core/services/config-property.service';
import {forkJoin} from "rxjs";
import {MonthlyReportingPeriod} from "src/app/shared/enums/monthly-reporting-periods";

@Component({
    selector: 'app-edit-emission-unit-info-panel',
    templateUrl: './edit-emission-unit-info-panel.component.html',
    styleUrls: ['./edit-emission-unit-info-panel.component.scss']
})
export class EditEmissionUnitInfoPanelComponent implements OnInit, OnChanges {
    @Input() emissionUnit: EmissionUnit;
    designCapacityWarning: any;
    facilitySite: FacilitySite;
    emissionUnitIdentifiers: string[] = [];
    facilityOpCode: BaseCodeLookup;
    facilitySourceTypeCode: BaseCodeLookup;
    edit = true;
	sccIsMonthlyReporting = false;
	hasMonthlyFuelData = false;
	processWithMonthlyData: string[] = [];
	monthlyReportingEnabled: boolean;
    semiAnnualReportSubmittedOrApproved = false;
    monthlyReportingPeriods = MonthlyReportingPeriod;
    statusYear: number;
    readonly allMonths = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];

    emissionUnitForm = this.fb.group({
        unitTypeCode: [null, Validators.required],
        operatingStatusCode: [null, [
            Validators.required,
            this.newSfcOperatingValidator(),
            this.existingMonthlyReportingDataValidator()
        ]],
        unitOfMeasureCode: [null],
        unitIdentifier: ['', [
            Validators.required,
            Validators.maxLength(20)
        ]],
        // Validators set in ngOnInit
        statusYear: [''],
        designCapacity: ['', [
            Validators.min(0.01),
            Validators.max(100000000),
            Validators.pattern('^[0-9]*\\.?[0-9]+$'),
            Validators.maxLength(20)
        ]],
        description: ['', [
            Validators.maxLength(100)
        ]],
        initialMonthlyReportingPeriod: [null],
        comments: [null, Validators.maxLength(2000)]
    }, {
        validators: [
            this.unitTypeCheck(),
            this.emissionUnitIdentifierCheck(),
            this.facilitySiteStatusCheck(),
            this.capacityUomCheck(),
            this.capacityLegacyUomCheck(),
            this.statusYearRequiredCheck(),
            this.initialMonthlyReportingPeriodInvalid()]
    });

    subFacilityOperatingStatusValues: BaseCodeLookup[];
    unitTypeValues: BaseCodeLookup[];
    uomValues: UnitMeasureCode[];
    designCapacityUomValues: UnitMeasureCode[];

    // Unit type codes that should have a design capacity
    typeCodeDesignCapacity = ['100', '120', '140', '160', '180'];

    constructor(private fb: FormBuilder,
                private lookupService: LookupService,
                public formUtils: FormUtilsService,
                private toastr: ToastrService,
                private emissionUnitService: EmissionUnitService,
				private propertyService: ConfigPropertyService,
                private route: ActivatedRoute) {
    }

    ngOnInit() {

        this.route.data
            .subscribe((data: { facilitySite: FacilitySite }) => {
                this.statusYear = data.facilitySite.emissionsReport.year;
				this.facilitySite = data.facilitySite;
                this.facilitySourceTypeCode = data.facilitySite.facilitySourceTypeCode;
                this.facilityOpCode = data.facilitySite.operatingStatusCode;
                this.emissionUnitForm.get('statusYear').setValidators([
                    Validators.min(1900),
                    Validators.max(data.facilitySite.emissionsReport.year),
                    Validators.pattern('[0-9]*'),
					this.requiredIfNotOperating()]);
                this.emissionUnitService.retrieveForFacility(data.facilitySite.id)
                    .subscribe(emissionUnits => {
                        emissionUnits.forEach(eu => {
                            this.emissionUnitIdentifiers.push(eu.unitIdentifier);
                        });

                        // if an emission unit is being edited then filter that identifer out the list so the validator
                        // check doesnt identify it as a duplicate
                        if (this.emissionUnit) {
                            this.emissionUnitIdentifiers = this.emissionUnitIdentifiers.filter(identifer =>
                                identifer.toString() !== this.emissionUnit.unitIdentifier);

                        } else {
                            this.edit = false;
                        }

                    });

                forkJoin({
                    monthlyReportingEnabled: this.propertyService.retrieveSltMonthlyFuelReportingEnabled(this.facilitySite?.programSystemCode?.code),
                    monthlyReportingInitialYear: this.propertyService.retrieveSltMonthlyFuelReportingInitialYear(this.facilitySite?.programSystemCode?.code)
                })
                .subscribe(({monthlyReportingEnabled, monthlyReportingInitialYear}) => {
                    this.monthlyReportingEnabled = monthlyReportingEnabled && (monthlyReportingInitialYear == null || this.facilitySite?.emissionsReport?.year >= monthlyReportingInitialYear);

                    if (this.monthlyReportingEnabled) {
                        if (data.facilitySite.emissionsReport.midYearSubmissionStatus === 'SUBMITTED' || data.facilitySite.emissionsReport.midYearSubmissionStatus === 'APPROVED') {
                            this.semiAnnualReportSubmittedOrApproved = true;
                        }
                        this.emissionUnit?.emissionsProcesses?.forEach(p => {
                            this.lookupService.retrievePointSourceSccCode(p.sccCode)
                                .subscribe(result => {
                                    if (result.monthlyReporting) {
                                        this.sccIsMonthlyReporting = result.monthlyReporting;

                                        for (const rp of p.reportingPeriods) {
                                            if (rp.reportingPeriodTypeCode.shortName == this.monthlyReportingPeriods.SEMIANNUAL &&
                                                rp.calculationParameterValue != null && Number(rp.calculationParameterValue) > 0 &&
                                                this.semiAnnualReportSubmittedOrApproved) {

                                                    this.processWithMonthlyData.push(p.emissionsProcessIdentifier);
                                                    this.emissionUnitForm.get('operatingStatusCode').disable();
                                                    this.emissionUnitForm.get('initialMonthlyReportingPeriod').disable();
                                            }
                                        }
                                    }
                                });
                        });
                    }
                });
            });

        this.lookupService.retrieveUom(this.statusYear)
            .subscribe(result => {
                this.uomValues = result;
                this.designCapacityUomValues = this.uomValues.filter(val => val.unitDesignCapacity);
            });

        this.lookupService.retrieveSubFacilityOperatingStatus()
            .subscribe(result => {
                this.subFacilityOperatingStatusValues = result;
            });

        this.lookupService.retrieveUnitType()
            .subscribe(result => {
                result.sort((a, b) => {
                    if (a.description < b.description) {
                        return -1;
                    }
                    if (a.description > b.description) {
                        return 1;
                    }
                    return 0;
                });
                this.unitTypeValues = result;
            });

        if (this.emissionUnit?.previousUnit != null || this.emissionUnit?.initialMonthlyReportingPeriod == null) {
            this.emissionUnitForm.get('initialMonthlyReportingPeriod').setValue('January');
        }
    }

    ngOnChanges() {
        this.emissionUnitForm.reset(this.emissionUnit);
    }

    onChange(newValue) {
        if (newValue && this.edit) {
            this.emissionUnitForm.controls.statusYear.reset();
            this.toastr.warning('', 'If the operating status of the Emission Unit is changed,' +
                ' then the operating status of all the child Emission Processes that are underneath ' +
                ' this unit will also be updated, unless they are already Permanently Shutdown.');
        }
        this.emissionUnitForm.controls.statusYear.markAsTouched();
        this.emissionUnitForm.controls.description.updateValueAndValidity();
		this.emissionUnitForm.controls.statusYear.updateValueAndValidity();
		// has to happen twice to catch potentially newly added validators
		// namely when adding or removing the required validator
        this.emissionUnitForm.controls.description.updateValueAndValidity();
		this.emissionUnitForm.controls.statusYear.updateValueAndValidity();
    }

    // Design capacity should be entered if type code is 100, 120, 140, 160, or 180.
    unitTypeCheck(): ValidatorFn {
        return (control: FormGroup): ValidationErrors | null => {
            const type = control.get('unitTypeCode');
            const designCapacity = control.get('designCapacity');

            if ((designCapacity.value === null || designCapacity.value === '') && type.value !== null) {
                for (const item of this.typeCodeDesignCapacity) {
                    if (type.value.code === item) {
                        this.designCapacityWarning = {invalidDesignCapacity: {designCapacity}};
                        break;
                    } else {
                        this.designCapacityWarning = null;
                    }
                }
            } else {
                this.designCapacityWarning = null;
            }
            return null;
        };
    }

    emissionUnitIdentifierCheck(): ValidatorFn {
        return (control: FormGroup): ValidationErrors | null => {
            const emissionId: string = control.get('unitIdentifier').value;
            if (this.emissionUnitIdentifiers) {
                if (!emissionId || emissionId.trim() === '') {
                    control.get('unitIdentifier').setErrors({required: true});
                } else {

                    for (const id of this.emissionUnitIdentifiers) {
                        if (id.trim().toLowerCase() === emissionId.trim().toLowerCase()) {
                            return {duplicateEmissionUnitIdentifier: true};
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

            if (this.facilityOpCode && controlStatus) {
	             if (this.facilitySourceTypeCode === null || (this.facilitySourceTypeCode.code !== VariableValidationType.LANDFILL_SOURCE_TYPE)) {

	                if (this.facilityOpCode.code === OperatingStatus.TEMP_SHUTDOWN
	                    && controlStatus.code !== OperatingStatus.PERM_SHUTDOWN
	                    && controlStatus.code !== OperatingStatus.TEMP_SHUTDOWN) {
	                    return {invalidStatusCodeTS: true};

	                } else if (this.facilityOpCode.code === OperatingStatus.PERM_SHUTDOWN
	                    && controlStatus.code !== OperatingStatus.PERM_SHUTDOWN) {
	                    return {invalidStatusCodePS: true};

	                }
	            }

				if (this.monthlyReportingEnabled) {
					// if monthly fuel data is not null or > 0, cannot TS/PS
					if (this.hasMonthlyFuelData && controlStatus.code !== OperatingStatus.OPERATING) {
						return {midYearReportSubmitted: true};
					}
				}
			}
			return null;
        }
    }

    capacityUomCheck(): ValidatorFn {
        return (control: FormGroup): ValidationErrors | null => {
            const designCapacity = control.get('designCapacity').value;
            const designCapacityUom = control.get('unitOfMeasureCode').value;

            if ((designCapacity && !designCapacityUom) || (!designCapacity && designCapacityUom)) {
                return {invalidUom: true};
            }
            return null;
        };
    }

    capacityLegacyUomCheck(): ValidatorFn {
        return (control: FormGroup): ValidationErrors | null => {
            const designCapacityUom = control.get('unitOfMeasureCode').value;

            if (control.get('operatingStatusCode').value
                && control.get('operatingStatusCode').value.code.includes(OperatingStatus.OPERATING)) {
                if (designCapacityUom && (designCapacityUom.legacy || !designCapacityUom.unitDesignCapacity)) {
                    return {eisUomInvalid: true};
                }
            }
            return null;
        };
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

            if (this.emissionUnitForm.get('operatingStatusCode').value
                && this.emissionUnitForm.get('operatingStatusCode').value.code.includes(OperatingStatus.OPERATING)) {
					formControl.addValidators(Validators.required);
            } else {
				if (formControl.hasValidator(Validators.required)) {
					formControl.removeValidators(Validators.required);
				}
			}
            return null;
        });
    }

	requiredIfNotOperating() {
        return (formControl => {
            if (!formControl.parent) {
                return null;
            }

            if (this.emissionUnitForm.get('operatingStatusCode').value
                && this.emissionUnitForm.get('operatingStatusCode').value.code.includes(OperatingStatus.OPERATING)) {
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
            if (control.value && control.value.code !== OperatingStatus.OPERATING && !this.emissionUnit?.previousUnit) {
                return {newSfcOperating: {value: control.value.code}};
            }
            return null;
        };
    }

    existingMonthlyReportingDataValidator(): ValidatorFn {
        return (control: AbstractControl): {[key: string]: any} | null => {
            if (control.value && control.value.code !== OperatingStatus.OPERATING && this.emissionUnit?.previousUnit && this.sccIsMonthlyReporting) {

                const initialMonthlyReportingPeriod = this.emissionUnitForm.get('initialMonthlyReportingPeriod').value;
                let unitInitialMonthIndex = this.allMonths.indexOf(initialMonthlyReportingPeriod);

                let procList: string[] = [];

                for (let i = this.emissionUnit.emissionsProcesses.length - 1; i >= 0; i--) {
                    const process = this.emissionUnit.emissionsProcesses[i];
                    const processInitialMonthIndex = this.allMonths.indexOf(process.initialMonthlyReportingPeriod);

                    for (let j = process.reportingPeriods.length - 1; j >= 0; j--) {
                        const period = process.reportingPeriods[j];
                        const monthIndex = this.allMonths.indexOf(period.reportingPeriodTypeCode.shortName);

                        if (monthIndex >= unitInitialMonthIndex && monthIndex >= processInitialMonthIndex
                            && ((period.calculationParameterValue != null && Number(period.calculationParameterValue) > 0)
                                || (period.fuelUseValue != null && Number(period.fuelUseValue) > 0))) {
                            procList.push(process.emissionsProcessIdentifier);
                            break;
                        }
                    }
                }
                if (procList.length > 0) {
                    return {unitExistingMonthlyReportingData: {value: procList.join(', ')}};
                }
            }
            return null;
        };
    }

    initialMonthlyReportingPeriodInvalid(): ValidatorFn {
        return (control: FormGroup): ValidationErrors | null => {
            const initialMonthlyReportingPeriod = control.get('initialMonthlyReportingPeriod').value;
            let unitMonthIndex = this.allMonths.indexOf(initialMonthlyReportingPeriod);

            if (this.emissionUnit?.previousUnit == null && this.semiAnnualReportSubmittedOrApproved && unitMonthIndex < 6
                && control.get('initialMonthlyReportingPeriod').enabled) {
                control.get('initialMonthlyReportingPeriod').setErrors({invalidMonthSemiAnnualSubmitted: true});
            }

            return null;
        };
    }

}
