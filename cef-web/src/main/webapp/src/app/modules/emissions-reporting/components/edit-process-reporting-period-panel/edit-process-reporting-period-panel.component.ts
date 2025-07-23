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
import {Component, OnInit, OnChanges, Input} from '@angular/core';
import {FormBuilder, Validators, ValidatorFn, FormGroup, ValidationErrors} from '@angular/forms';
import {LookupService} from 'src/app/core/services/lookup.service';
import {BaseCodeLookup} from 'src/app/shared/models/base-code-lookup';
import {ReportingPeriod} from 'src/app/shared/models/reporting-period';
import {FormUtilsService} from 'src/app/core/services/form-utils.service';
import {UnitMeasureCode} from 'src/app/shared/models/unit-measure-code';
import {legacyUomValidator} from 'src/app/modules/shared/directives/legacy-uom-validator.directive';
import {SharedService} from 'src/app/core/services/shared.service';
import {ToastrService} from 'ngx-toastr';
import {OperatingStatus} from 'src/app/shared/enums/operating-status';
import {CalculationMaterialCode} from 'src/app/shared/models/calculation-material-code';
import { ConfigPropertyService } from 'src/app/core/services/config-property.service';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { ActivatedRoute } from '@angular/router';
import {forkJoin} from "rxjs";

@Component({
    selector: 'app-edit-process-reporting-period-panel',
    templateUrl: './edit-process-reporting-period-panel.component.html',
    styleUrls: ['./edit-process-reporting-period-panel.component.scss']
})
export class EditProcessReportingPeriodPanelComponent implements OnInit, OnChanges {
  @Input() reportingPeriod: ReportingPeriod;
  @Input() sccCode: string;
  @Input() processOpStatus: string;
  @Input() facilitySite: FacilitySite;
  isFuelUseScc = false;
  isMonthlyReportingScc = false;
  semiAnnualReportSubmittedOrApproved = null;
  isSelectedDefaultHeatRatio: number;
  isSelectedNumeratorUom: UnitMeasureCode;
  reportingPeriodForm = this.fb.group({
    reportingPeriodTypeCode: [{code: 'A'}, Validators.required],
    emissionsOperatingTypeCode: [null, Validators.required],
    calculationParameterTypeCode: [null, Validators.required],
    calculationParameterValue: ['', [
      Validators.required,
      Validators.min(0),
      Validators.pattern('^[0-9]*\\.?[0-9]{0,6}$')
    ]],
    calculationParameterUom: [null, [Validators.required, legacyUomValidator()]],
    calculationMaterialCode: [null, Validators.required],
    fuelUseValue: ['', [
      Validators.min(0),
      Validators.pattern('^[0-9]*\\.?[0-9]{0,6}$')
    ]],
    fuelUseUom: [null, [legacyUomValidator(), legacyUomValidator()]],
    fuelUseMaterialCode: [null],
    heatContentValue: ['', [
      Validators.min(0),
      Validators.pattern('^[0-9]*\\.?[0-9]{0,5}$')
    ]],
    heatContentUom: [null, [legacyUomValidator(), legacyUomValidator()]],
    comments: [null, Validators.maxLength(400)]
  }, { validators: [
    this.checkFuelUseFields(),
	this.checkFuelAndHeatUom()
  ]});

  materialValues: BaseCodeLookup[];
  fuelUseMaterialValues: CalculationMaterialCode[];
  sccFuelUseMaterialValue: BaseCodeLookup;
  parameterTypeValues: BaseCodeLookup[];
  operatingStatusValues: BaseCodeLookup[];
  reportingPeriodValues: BaseCodeLookup[] = [];
  uomValues: UnitMeasureCode[];
  denominatorUomValues: UnitMeasureCode[];
  fuelUseUomValues: UnitMeasureCode[];
  sccFuelUseUomValues: UnitMeasureCode[];
  heatContentUomValues: UnitMeasureCode[];
  showFuelDataCopyMessage = false;
  sccFuelUsefieldsWarning = null;
  sccHeatContentfieldsWarning = null;
  monthlyReportingEnabled: boolean;
  fuelValueInputEnabled: boolean;
  heatContentInvalid: string;
  heatContentDenomUom: UnitMeasureCode;

  constructor(
	private route: ActivatedRoute,
	private propertyService: ConfigPropertyService,
    private lookupService: LookupService,
    public formUtils: FormUtilsService,
    private sharedService: SharedService,
    private toastr: ToastrService,
    private fb: FormBuilder) { }

  ngOnInit() {

      forkJoin({
          monthlyReportingEnabled: this.propertyService.retrieveSltMonthlyFuelReportingEnabled(this.facilitySite?.programSystemCode?.code),
          monthlyReportingInitialYear: this.propertyService.retrieveSltMonthlyFuelReportingInitialYear(this.facilitySite?.programSystemCode?.code)
      })
      .subscribe(({monthlyReportingEnabled, monthlyReportingInitialYear}) => {
          this.monthlyReportingEnabled = monthlyReportingEnabled && (monthlyReportingInitialYear == null || this.facilitySite?.emissionsReport?.year >= monthlyReportingInitialYear);
      });

	  	this.route.data
		  	.subscribe((data: { facilitySite: FacilitySite }) => {
			this.lookupService.retrievePointSourceSccCode(this.sccCode)
				.subscribe(result => {
					if (result) {
						if ((data.facilitySite.emissionsReport.midYearSubmissionStatus === 'SUBMITTED' || data.facilitySite.emissionsReport.midYearSubmissionStatus === 'APPROVED') && result.monthlyReporting) {
							this.semiAnnualReportSubmittedOrApproved = true;
						}
					}
				});
			});

        this.lookupService.retrieveCalcMaterial()
            .subscribe(result => {
                this.materialValues = result;
            });

        this.lookupService.retrieveFuelUseMaterial()
            .subscribe(result => {
                this.fuelUseMaterialValues = result;
            });

        this.lookupService.retrieveCalcParam()
            .subscribe(result => {
                this.parameterTypeValues = result;
            });

        this.lookupService.retrieveEmissionsOperatingType()
            .subscribe(result => {
                this.operatingStatusValues = result;
            });

        this.lookupService.retrieveReportingPeriod()
            .subscribe(result => {
                const annual = result.find(period => period.code === 'A');
                this.reportingPeriodValues.push(annual);
                this.reportingPeriodForm.get('reportingPeriodTypeCode').patchValue(annual);
            });

        this.lookupService.retrieveUom(this.facilitySite?.emissionsReport?.year)
            .subscribe(result => {
                this.uomValues = result;
                this.denominatorUomValues = this.uomValues.filter(val => val.efDenominator);
            });

        this.lookupService.retrieveFuelUseUom()
            .subscribe(result => {
                this.fuelUseUomValues = result;
                this.heatContentUomValues = this.fuelUseUomValues.filter(val => val.heatContentUom);

                if (this.sccCode) {
                    this.getPointSourceScc(this.sccCode);
                }
            });

        this.sharedService.processSccChangeEmitted$.subscribe(scc => {
            if (scc) {
                this.getPointSourceScc(scc);
            }
        });

        this.sharedService.processOpStatusChangeEmitted$.subscribe(opStatus => {
            if (opStatus) {
                this.processOpStatus = opStatus;
                this.disableWarning(this.processOpStatus);
            }
        });

        this.reportingPeriodForm.get('fuelUseMaterialCode').valueChanges
        .subscribe(value => {
            this.checkSccFuelMaterialUom();
        });

        this.reportingPeriodForm.get('fuelUseUom').valueChanges
        .subscribe(value => {
            this.checkSccFuelMaterialUom();
        });

		this.reportingPeriodForm.get('heatContentValue').valueChanges
        .subscribe(value => {
			this.setHeatContentRatioDenominator();
        });

		this.reportingPeriodForm.get('heatContentUom').valueChanges
        .subscribe(value => {
			this.setHeatContentRatioDenominator();
        });
    }

    ngOnChanges() {

        this.reportingPeriodForm.reset(this.reportingPeriod);
    }

    onSubmit() {

        // let period = new ReportingPeriod();
        // Object.assign(period, this.reportingPeriodForm.value);
        // console.log(period);
    }

    isMonthlyReportingProcess() {
        return this.monthlyReportingEnabled && this.isMonthlyReportingScc;
    }

    isMonthlyReportingProcessAndSemiannualSubmitted() {
        return this.monthlyReportingEnabled && this.isMonthlyReportingScc && this.semiAnnualReportSubmittedOrApproved;
    }

    getPointSourceScc(processScc: string) {
        this.lookupService.retrievePointSourceSccCode(processScc)
        .subscribe(result => {
			this.isFuelUseScc = false;
			this.isMonthlyReportingScc = false;
			this.fuelValueInputEnabled = false;
            if (result && result.monthlyReporting) {
				this.isMonthlyReportingScc = this.monthlyReportingEnabled;
                this.reportingPeriodForm.get('fuelUseValue').updateValueAndValidity();
                this.sccFuelUseMaterialValue = result.calculationMaterialCode;
				if (result.fuelUseTypes) {
					this.isFuelUseScc = true;
	                this.sccFuelUseUomValues = this.fuelUseUomValues.filter(
	                    val => (result.fuelUseTypes.split(',')).includes(val.fuelUseType));
				}
				this.fuelValueInputEnabled = this.monthlyReportingEnabled;

                if (this.semiAnnualReportSubmittedOrApproved) {
                    this.reportingPeriodForm.get('calculationMaterialCode').disable();
                    this.reportingPeriodForm.get('calculationParameterUom').disable();
                    this.reportingPeriodForm.get('fuelUseMaterialCode').disable();
                    this.reportingPeriodForm.get('fuelUseUom').disable();
                }
            }
			this.setHeatContentRatioDenominator();
            this.disableWarning(this.processOpStatus);

            // throughput is disabled and thus not required when scc is a monthly reporting scc
            if (this.monthlyReportingEnabled) {
                if (this.isMonthlyReportingScc) {
                    if (this.reportingPeriodForm.get('calculationParameterValue').hasValidator(Validators.required)) {
                        this.reportingPeriodForm.get('calculationParameterValue').removeValidators(Validators.required);
                        this.reportingPeriodForm.get('calculationParameterValue').updateValueAndValidity();
                    }
                } else {
                    if (!this.reportingPeriodForm.get('calculationParameterValue').hasValidator(Validators.required)) {
                        this.reportingPeriodForm.get('calculationParameterValue').addValidators(Validators.required);
                        this.reportingPeriodForm.get('calculationParameterValue').updateValueAndValidity();
                    }
                }
            }
        });
    }

    // fuel material is set to null when the fuel use material does not match the allowable materials
    checkSccFuelMaterialUom() {
        if (this.processOpStatus === OperatingStatus.OPERATING) {
            if (this.isFuelUseScc) {

                this.reportingPeriodForm.get('fuelUseMaterialCode').value === null
                || this.reportingPeriodForm.get('fuelUseMaterialCode').value?.code === this.sccFuelUseMaterialValue.code
                ? null : this.reportingPeriodForm.get('fuelUseMaterialCode').patchValue(null);

                this.checkSccUom();
            } else if (this.reportingPeriodForm.get('fuelUseMaterialCode').value) {
                this.setDefaultHeatContentRatio();
            }
        }
		this.setHeatContentRatioDenominator();
    }

    // fuel use uom is set to null when the uom does not match any in the list of allowable fuel use uoms
    checkSccUom(){
        let match = false;
        if (this.reportingPeriodForm.get('fuelUseUom').value === null) {
            match = true;
        } else {
            this.sccFuelUseUomValues?.forEach(element => {
                if (element.code === this.reportingPeriodForm.get('fuelUseUom').value.code) {
                        match = true;
                }
            });
            this.setDefaultHeatContentRatio();
        }
        match ? null : this.reportingPeriodForm.get('fuelUseUom').patchValue(null);
    }

    setDefaultHeatContentRatio(){
        const materialValue = this.reportingPeriodForm.get('fuelUseMaterialCode').value ? this.fuelUseMaterialValues?.filter(
                            val => (val.code === (this.reportingPeriodForm.get('fuelUseMaterialCode').value.code)))[0] : null;
        const fuelUom = materialValue && materialValue.heatContentRatioDenominatorUom ? materialValue.heatContentRatioDenominatorUom.code : null;
        const defaultHeatRatio = materialValue ? materialValue.defaultHeatContentRatio : null;
        const defaultHeatUom = materialValue ? materialValue.heatContentRatioNumeratorUom : null;

        if (this.reportingPeriodForm.get('fuelUseUom').value?.code === fuelUom) {
            this.reportingPeriodForm.get('heatContentValue').patchValue(defaultHeatRatio);
            this.reportingPeriodForm.get('heatContentUom').patchValue(defaultHeatUom);
            this.isSelectedNumeratorUom = defaultHeatUom;
            this.isSelectedDefaultHeatRatio = defaultHeatRatio;

            // if fuel UoM and fuel Material combination does not have default heat content values, and form values are the same as
            // previously populated default heat values, then clear the heat content value and heat numerator UoM fields.
        } else if (this.isSelectedDefaultHeatRatio === this.reportingPeriodForm.get('heatContentValue').value
            && this.isSelectedNumeratorUom === this.reportingPeriodForm.get('heatContentUom').value) {
                this.reportingPeriodForm.get('heatContentValue').patchValue(null);
                this.reportingPeriodForm.get('heatContentUom').patchValue(null);
                this.isSelectedNumeratorUom = null;
                this.isSelectedDefaultHeatRatio = null;
        }
    }

	// default heat content ratio denominator uom to fuel uom if heat content value or numerator exists.
	setHeatContentRatioDenominator() {
		if (!this.reportingPeriodForm.get('fuelUseUom').value
		|| (!this.reportingPeriodForm.get('heatContentUom').value && !this.reportingPeriodForm.get('heatContentValue').value)) {
			this.heatContentDenomUom = null;
		} else {
			this.heatContentDenomUom = this.reportingPeriodForm.get('fuelUseUom').value;
		}
		this.checkHeatCapacityUom();
	}

    disableWarning(opStatus: string) {
        if ((this.isFuelUseScc || (this.monthlyReportingEnabled && this.isMonthlyReportingScc)) && opStatus === OperatingStatus.OPERATING) {
			if (this.isFuelUseScc) {
	            this.sccFuelUsefieldsWarning = 'Fuel data for this Process SCC must be reported. If this is a duplicate process for an Alternative Throughput, only report Fuel data for one of these Processes.';
	            this.sccHeatContentfieldsWarning = 'Heat Content data for this Process SCC must be reported. If this is a duplicate process for an Alternative Throughput, only report Heat Content data for one of these Processes.';
			}
			// asphalt SCCs require fuel data for monthly reporting and do not have heat content values
			if (this.monthlyReportingEnabled && this.isMonthlyReportingScc && !this.isFuelUseScc) {
				this.sccFuelUsefieldsWarning = 'Fuel data for this Process SCC must be reported. If this is a duplicate process for an Alternative Throughput, only report Fuel data for one of these Processes.';
			}
        } else {
            this.sccFuelUsefieldsWarning = false;
            this.sccHeatContentfieldsWarning = false;
        }
    }

    checkHeatCapacityUom() {
        const heatContentValue = this.reportingPeriodForm.get('heatContentValue').value;
        const heatContentUom = this.reportingPeriodForm.get('heatContentUom').value;

        if ((heatContentUom || (heatContentValue && heatContentValue !== ''))
            && (heatContentValue === '' || !heatContentValue || !heatContentUom)) {
            this.heatContentInvalid = 'If Heat Content data is being reported then both fields must contain inputs in order to save.';
        } else {
			this.heatContentInvalid = null;
		}
    }

	checkFuelAndHeatUom(): ValidatorFn {
		return (control: FormGroup): ValidationErrors | null => {
			const fuelUom = control.get('fuelUseUom').value;
			const heatUom = control.get('heatContentUom').value;
			const fuelMaterial = control.get('fuelUseMaterialCode').value;

            if (
                ( fuelUom && heatUom ) &&
                ( fuelUom.unitType === heatUom.unitType || fuelUom.fuelUseType === heatUom.fuelUseType )
            ) {
                return {
                    heatFuelUomInvalid: 'Heat Content Ratio Numerator UoM and Fuel UoM cannot be the same unit type.'
                }
            } else {
                return null;
            }
		};
	}

    checkFuelUseFields(): ValidatorFn {
        return (control: FormGroup): ValidationErrors | null => {
        const fuelValue = control.get('fuelUseValue').value;
        const fuelMaterial = control.get('fuelUseMaterialCode').value;
        const fuelUom = control.get('fuelUseUom').value;

	        if (!this.monthlyReportingEnabled && (fuelValue || fuelMaterial || fuelUom)
	            && (fuelValue === null || fuelValue === '' || !fuelMaterial || !fuelUom)) {
				return {fuelUsefields: true};
	        }

			if (this.monthlyReportingEnabled) {
				// if monthly reporting is enabled, fuel value field is readonly for monthly reporting SCCs. can only edit fuel value from monthly reporting tabs.
				if (this.isMonthlyReportingScc && (fuelValue || fuelMaterial || fuelUom) && (!fuelMaterial || !fuelUom)) {
					return {fuelUsefields: true};

				// if monthly reporting is enabled, fuel value field is editable for non fuel use SCCs
				} else if (!this.isMonthlyReportingScc && (fuelValue || fuelMaterial || fuelUom)
		            && (fuelValue === null || fuelValue === '' || !fuelMaterial || !fuelUom)) {
					return {fuelUsefields: true};
				}
			}
        return null;
        };
    }

    copyFuelDataToThroughput() {
        const fuelMaterial = this.reportingPeriodForm.get('fuelUseMaterialCode').value;
        const fuelValue = this.reportingPeriodForm.get('fuelUseValue').value;
        const fuelUom = this.reportingPeriodForm.get('fuelUseUom').value;

        this.reportingPeriodForm.get('calculationMaterialCode').patchValue(fuelMaterial);
        this.reportingPeriodForm.get('calculationParameterValue').patchValue(fuelValue);
        this.reportingPeriodForm.get('calculationParameterUom').patchValue(fuelUom);
        const message = 'Fuel data copied to Throughput data fields will be used to calculate total emissions for these pollutants.';
        this.toastr.success(message);
    }

    isFuelUseDirty = ():boolean => {
        const fuelMaterial = this.reportingPeriodForm.get('fuelUseMaterialCode');
        const fuelValue = this.reportingPeriodForm.get('fuelUseValue');
        const fuelUom = this.reportingPeriodForm.get('fuelUseUom');

        return fuelMaterial.dirty && fuelValue.dirty && fuelUom.dirty;
    }
}
