/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
import {
    Component,
    OnInit,
    OnDestroy,
    Input,
    HostListener,
} from "@angular/core";
import { Emission } from "src/app/shared/models/emission";
import { ReportingPeriod } from "src/app/shared/models/reporting-period";
import { Process } from "src/app/shared/models/process";
import {
    Validators,
    FormBuilder,
    ValidatorFn,
    FormGroup,
    FormControl,
    ValidationErrors,
} from "@angular/forms";
import { CalculationMethodCode } from "src/app/shared/models/calculation-method-code";
import { Pollutant } from "src/app/shared/models/pollutant";
import { UnitMeasureCode } from "src/app/shared/models/unit-measure-code";
import { EmissionService } from "src/app/core/services/emission.service";
import { LookupService } from "src/app/core/services/lookup.service";
import { FormUtilsService } from "src/app/core/services/form-utils.service";
import { ReportingPeriodService } from "src/app/core/services/reporting-period.service";
import { ActivatedRoute, Router } from "@angular/router";
import { forkJoin, Observable } from "rxjs";
import { debounceTime, distinctUntilChanged, map } from "rxjs/operators";
import { EmissionsProcessService } from "src/app/core/services/emissions-process.service";
import { BaseReportUrl } from "src/app/shared/enums/base-report-url";
import { EmissionFactor } from "src/app/shared/models/emission-factor";
import { EmissionFactorService } from "src/app/core/services/emission-factor.service";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { EmissionFactorModalComponent } from "src/app/modules/emissions-reporting/components/emission-factor-modal/emission-factor-modal.component";
import { SharedService } from "src/app/core/services/shared.service";
import { ToastrService } from "ngx-toastr";
import { EmissionFormulaVariable } from "src/app/shared/models/emission-formula-variable";
import { VariableValidationType } from "src/app/shared/enums/variable-validation-type";
import { EmissionFormulaVariableCode } from "src/app/shared/models/emission-formula-variable-code";
import { EmissionUnitService } from "src/app/core/services/emission-unit.service";
import { legacyUomValidator } from "src/app/modules/shared/directives/legacy-uom-validator.directive";
import { UserContextService } from "src/app/core/services/user-context.service";
import { legacyItemValidator } from "src/app/modules/shared/directives/legacy-item-validator.directive";
import { UtilityService } from "src/app/core/services/utility.service";
import { ConfigPropertyService } from "src/app/core/services/config-property.service";
import { BaseCodeLookup } from "src/app/shared/models/base-code-lookup";
import { EntityType } from "src/app/shared/enums/entity-type";
import { formatPollutant } from "src/app/modules/shared/utils/format";
import { MonthlyReportingPeriod } from "src/app/shared/enums/monthly-reporting-periods";
import { ReportService } from "src/app//core/services/report.service";

@Component({
    selector: "app-emission-details",
    templateUrl: "./emission-details.component.html",
    styleUrls: ["./emission-details.component.scss"],
})
export class EmissionDetailsComponent implements OnInit, OnDestroy {
    @Input() emission: Emission;
    @Input() reportingPeriod: ReportingPeriod;
    @Input() process: Process;
    @Input() editable = false;
    @Input() createMode = false;
    epaEmissionFactor = false;
    efNumeratorMismatch = false;
    calcParamValue = true;
    calcParamUom = true;
    failedNumDesc: string;
    failedTotalDesc: string;
    efDenominatorMismatch = false;
    failedRpCalcDesc: string;
    failedDenomDesc: string;
    needsCalculation = false;
    formulaVariables: EmissionFormulaVariableCode[] = [];
    variablesNotInFormula = false;
    buttonsEnabled = true;
    sltEfCalcMethodCodes: string[] = ["9", "29"];
    sltCompendiumEnabled = false;
    isSltEfCalcMethod = false;
    liquidFuelTypeUoM: string[] = ["BBL", "VOLUME"];
    isFuelUseScc = false;
    currentEmissionFactor: EmissionFactor;
    newEmissionFactor: EmissionFactor;
    efRange = false;
    reportYear: number;
    monthlyReportingPeriods = MonthlyReportingPeriod;

    isMonthlyReportingScc = false;
    monthlyReportingEnabled: boolean;
    semiAnnualReportSubmittedOrApproved = false;
    emissionsCreatedAfterSemiannualSubmission: string[];
    emissionsReportId: number;

    readOnlyMode = true;

    processUrl: string;
    unitIdentifier: string;

    emissionForm = this.fb.group(
        {
            pollutant: [null, [Validators.required]],
            formulaIndicator: [false, Validators.required],
            emissionsFactor: ["", [Validators.required]],
            emissionsFactorFormula: [""],
            emissionsFactorText: [
                "",
                [Validators.required, Validators.maxLength(100)],
            ],
            emissionsFactorSource: [""],
            emissionsNumeratorUom: [
                null,
                [Validators.required, legacyUomValidator()],
            ],
            emissionsDenominatorUom: [
                null,
                [Validators.required, legacyUomValidator()],
            ],
            emissionsCalcMethodCode: ["", Validators.required],
            totalManualEntry: [false, Validators.required],
            overallControlPercent: [
                "",
                [Validators.min(0), Validators.max(99.999999)],
            ],
            totalEmissions: ["", [Validators.required, Validators.min(0)]],
            emissionsUomCode: [
                null,
                [Validators.required, legacyUomValidator()],
            ],
            comments: [null, [Validators.maxLength(2000)]],
            calculationComment: [
                "",
                [Validators.required, Validators.maxLength(4000)],
            ],
            formulaVariables: this.fb.group({}),
            notReporting: [false], // default value
        },
        {
            validators: [
                this.emissionsCalculatedValidator(),
                this.emissionFactorGreaterThanZeroValidator(),
                this.emissionFactorOutsideRangeValidator(),
                this.pollutantEmissionsUoMValidator(),
                this.checkPercentSulfurRange(),
                this.checkPercentAshRange(),
                this.overallControlPercentValidator(),
                this.checkSignificantFigures(),
            ],
        }
    );

    methodValues: CalculationMethodCode[];
    pollutantValues: Pollutant[];
    uomValues: UnitMeasureCode[];
    numeratorUomValues: UnitMeasureCode[];
    denominatorUomValues: UnitMeasureCode[];
    currentCalcMethod: CalculationMethodCode;
    canConvert: Boolean = false;
    programSystemCode: BaseCodeLookup;
    previousEmissions: Emission[];

    entityType = EntityType;

    constructor(
        private emissionService: EmissionService,
        private emissionUnitService: EmissionUnitService,
        private periodService: ReportingPeriodService,
        private processService: EmissionsProcessService,
        private efService: EmissionFactorService,
        private reportService: ReportService,
        private propertyService: ConfigPropertyService,
        private lookupService: LookupService,
        private userContextService: UserContextService,
        private route: ActivatedRoute,
        private router: Router,
        private sharedService: SharedService,
        private utilityService: UtilityService,
        public formUtils: FormUtilsService,
        private modalService: NgbModal,
        private toastr: ToastrService,
        private fb: FormBuilder
    ) {}

    ngOnInit() {
        this.lookupService.retrieveCalcMethod().subscribe((result) => {
            this.methodValues = result;
        });

        this.route.data.subscribe((data) => {
            const year = data.facilitySite.emissionsReport.year;
            this.reportYear = year;

            this.emissionForm
                .get("pollutant")
                .setValidators([
                    Validators.required,
                    legacyItemValidator(year, "Pollutant", "pollutantName"),
                ]);

            this.lookupService
                .retrieveCurrentPollutants(year)
                .subscribe((result) => {
                    this.pollutantValues = result.filter(r =>
                        r.programSystemCodes.length === 0 || r.programSystemCodes.some(psc => psc.code === this.programSystemCode.code)
                    );
                });

            this.emissionForm
                .get("pollutant")
                .valueChanges.pipe(debounceTime(200), distinctUntilChanged())
                .subscribe(() => {
                    const pollutantFormControl =
                        this.emissionForm.get("pollutant");
                    if (
                        this.pollutantValues &&
                        pollutantFormControl.value != null &&
                        typeof pollutantFormControl.value === "string" &&
                        pollutantFormControl.value.length > 0
                    ) {
                        // searchedPollutant === 'string' when no pollutant selected
                        this.emissionForm
                            .get("pollutant")
                            .setErrors({ invalidPollutant: true });
                    }
                });

            this.createMode = data.create === "true";
            this.editable = data.create === "true";

            this.userContextService.getUser().subscribe((user) => {
                if (
                    UtilityService.isNotReadOnlyMode(
                        user,
                        data.facilitySite.emissionsReport.status
                    )
                ) {
                    this.readOnlyMode = false;
                }
            });

            this.programSystemCode = data.facilitySite.emissionsReport
                .programSystemCode
                ? data.facilitySite.emissionsReport.programSystemCode
                : data.facilitySite?.programSystemCode;
            this.propertyService
                .retrieveSltEmissionFactorCompendiumEnabled(
                    this.programSystemCode?.code
                )
                .subscribe((result) => {
                    this.sltCompendiumEnabled = result;
                });

            this.sharedService.emitChange(data.facilitySite);

            if (
                data.facilitySite.emissionsReport.midYearSubmissionStatus ===
                    "SUBMITTED" ||
                data.facilitySite.emissionsReport.midYearSubmissionStatus ===
                    "APPROVED"
            ) {
                this.semiAnnualReportSubmittedOrApproved = true;
            }

            this.emissionsReportId = data.facilitySite.emissionsReport.id;
        });

        this.lookupService.retrieveUom(this.reportYear).subscribe((result) => {
            this.uomValues = result;
            this.numeratorUomValues = this.uomValues.filter(
                (val) => val.efNumerator
            );
            this.denominatorUomValues = this.uomValues.filter(
                (val) => val.efDenominator
            );
        });

        this.route.paramMap.subscribe((params) => {
            if (!this.createMode) {
                this.emissionService
                    .retrieveWithVariables(+params.get("emissionId"))
                    .subscribe((result) => {
                        this.emission = result;
                        this.emissionForm.reset(this.emission);
                        this.variablesNotInFormula =
                            this.findVariablesNotInFormula(this.emission);
                        this.setupVariableFormFromValues(
                            this.emission.variables
                        );
                        this.emissionForm.disable();

                        this.currentCalcMethod = this.emissionForm.get(
                            "emissionsCalcMethodCode"
                        ).value;
                        this.isSltEfCalcMethod = this.sltEfCalcMethodCodes.find(
                            (code) => code === this.currentCalcMethod?.code
                        )
                            ? true
                            : false;

                        if (
                            result.webfireEf != null ||
                            result.ghgEfId != null
                        ) {
                            if (result.webfireEf != null) {
                                this.currentEmissionFactor = result.webfireEf;
                                this.newEmissionFactor = result.webfireEf;
                                this.efRange =
                                    result.webfireEf.minValue != null ||
                                    result.webfireEf.maxValue != null;
                                const descriptionText =
                                    this.formatDescriptionText(
                                        this.currentEmissionFactor?.description
                                    );
                                this.emissionForm
                                    .get("emissionsFactorText")
                                    .setValue(descriptionText);
                            }
                            if (result.ghgEfId != null) {
                                this.efService
                                    .retrieveByGhgId(result.ghgEfId)
                                    .subscribe((ghgResult) => {
                                        this.currentEmissionFactor = ghgResult;
                                        this.newEmissionFactor = ghgResult;
                                        this.efRange =
                                            ghgResult.minValue != null ||
                                            ghgResult.maxValue != null;
                                        const descriptionText =
                                            this.formatDescriptionText(
                                                this.currentEmissionFactor
                                                    ?.description
                                            );
                                        this.emissionForm
                                            .get("emissionsFactorText")
                                            .setValue(descriptionText);
                                    });
                            }
                        } else {
                            this.emissionForm
                                .get("emissionsFactorText")
                                .setValue(this.emission?.emissionsFactorText);
                        }
                    });
            } else {
                this.emissionForm.enable();

                this.setupForm();
            }

            this.periodService
                .retrieve(+params.get("periodId"))
                .subscribe((result) => {
                    this.reportingPeriod = result;
                    this.processService
                        .retrievePrevious(this.reportingPeriod.emissionsProcessId)
                        .subscribe((process) => {
                            this.previousEmissions = process?.reportingPeriods[0]?.emissions
                            this.updateEmissionPreviousNotReporting(true);
                        })
                    this.processService
                        .retrieve(this.reportingPeriod.emissionsProcessId)
                        .subscribe((process) => {
                            this.process = process;
                            if (this.emission && this.process) {
                                // In case async request incomplete or falsy
                                this.sharedService.emitActiveLinkInfoChange({
                                    label: `; current pollutant: ${this.emission.pollutant.pollutantName}`,
                                    parentId: this.process.id,
                                    childId: this.emission.id,
                                });
                            }
                            this.emissionService
                                .retrieveEmissionsCreatedAfterSemiannualSubmission(
                                    this.process.id,
                                    this.emissionsReportId
                                )
                                .subscribe((result) => {
                                    this.emissionsCreatedAfterSemiannualSubmission =
                                        result;
                                });

                            this.processUrl = `/facility/${params.get(
                                "facilityId"
                            )}/report/${params.get("reportId")}/${
                                BaseReportUrl.EMISSIONS_PROCESS
                            }/${this.process.id}`;

                            this.emissionUnitService
                                .retrieve(process.emissionsUnitId)
                                .subscribe((unit) => {
                                    this.unitIdentifier = unit.unitIdentifier;
                                });

                            this.lookupService
                                .retrievePointSourceSccCode(process.sccCode)
                                .subscribe((sccCode) => {
                                    forkJoin({
                                        monthlyReportingEnabled:
                                            this.propertyService.retrieveSltMonthlyFuelReportingEnabled(
                                                this.programSystemCode?.code
                                            ),
                                        monthlyReportingInitialYear:
                                            this.propertyService.retrieveSltMonthlyFuelReportingInitialYear(
                                                this.programSystemCode?.code
                                            ),
                                    }).subscribe(
                                        ({
                                            monthlyReportingEnabled,
                                            monthlyReportingInitialYear,
                                        }) => {
                                            this.monthlyReportingEnabled =
                                                monthlyReportingEnabled &&
                                                (monthlyReportingInitialYear ==
                                                    null ||
                                                    this.reportYear >=
                                                        monthlyReportingInitialYear);

                                            // remove required and min value validator for total emissions if monthly reporting enabled
                                            // and scc is a monthly reporting scc
                                            if (
                                                sccCode &&
                                                sccCode.monthlyReporting &&
                                                this.monthlyReportingEnabled
                                            ) {
                                                this.isMonthlyReportingScc =
                                                    sccCode.monthlyReporting;
                                                this.emissionForm
                                                    .get("totalEmissions")
                                                    .clearValidators();
                                                this.emissionForm
                                                    .get("totalEmissions")
                                                    .updateValueAndValidity();
                                            }

                                            if (sccCode.fuelUseTypes) {
                                                this.isFuelUseScc = true;
                                            }
                                        }
                                    );
                                });
                        });
                });
        });
    }

    ngOnDestroy() {
        this.sharedService.emitActiveLinkInfoChange(null);
    }

    private onMethodChange(
        value: CalculationMethodCode,
        status: string,
        forceReset: boolean
    ) {
        this.isSltEfCalcMethod = this.sltEfCalcMethodCodes.find(
            (code) => code === value?.code
        )
            ? true
            : false;

        if ("DISABLED" !== status) {
            if (value && value.totalDirectEntry) {
                this.emissionForm
                    .get("emissionsFactorText")
                    .updateValueAndValidity();
                if (
                    this.emissionForm.get("emissionsFactor").value &&
                    !this.emissionForm
                        .get("emissionsFactorText")
                        .hasValidator(Validators.required)
                ) {
                    this.emissionForm
                        .get("emissionsFactorText")
                        .addValidators(Validators.required);
                }
                this.emissionForm
                    .get("emissionsFactor")
                    .removeValidators(Validators.required);
                this.emissionForm
                    .get("emissionsFactor")
                    .updateValueAndValidity();
                this.emissionForm
                    .get("emissionsFactorFormula")
                    .removeValidators(Validators.required);
                this.emissionForm
                    .get("emissionsFactorFormula")
                    .updateValueAndValidity();
                if (!this.emissionForm.get("emissionsFactor").value) {
                    this.emissionForm
                        .get("emissionsNumeratorUom")
                        .removeValidators(Validators.required);
                    this.emissionForm
                        .get("emissionsDenominatorUom")
                        .removeValidators(Validators.required);
                    this.emissionForm
                        .get("emissionsFactorText")
                        .removeValidators(Validators.required);
                }
                this.emissionForm
                    .get("emissionsFactorText")
                    .updateValueAndValidity();
                this.emissionForm
                    .get("emissionsNumeratorUom")
                    .updateValueAndValidity();
                this.emissionForm
                    .get("emissionsDenominatorUom")
                    .updateValueAndValidity();
                this.emissionForm
                    .get("emissionsFactorSource")
                    .updateValueAndValidity();
                this.isCommentRequired();
                this.getTotalManualEntry().setValue(false);
            } else {
                this.emissionForm
                    .get("emissionsFactor")
                    .addValidators(Validators.required);
                this.emissionForm
                    .get("emissionsFactor")
                    .updateValueAndValidity();
                this.emissionForm
                    .get("emissionsFactorText")
                    .addValidators(Validators.required);
                this.emissionForm
                    .get("emissionsFactorText")
                    .updateValueAndValidity();
                this.emissionForm
                    .get("emissionsNumeratorUom")
                    .addValidators(Validators.required);
                this.emissionForm
                    .get("emissionsNumeratorUom")
                    .updateValueAndValidity();
                this.emissionForm
                    .get("emissionsDenominatorUom")
                    .addValidators(Validators.required);
                this.emissionForm
                    .get("emissionsDenominatorUom")
                    .updateValueAndValidity();
                this.isCommentRequired();
                this.getTotalManualEntry().setValue(false);
            }

            const sameCalcMethod =
                this.currentCalcMethod &&
                this.currentCalcMethod.code === value?.code;
            if (!sameCalcMethod) {
                this.currentCalcMethod = value;
            }

            if (
                value &&
                (value.epaEmissionFactor ||
                    (this.sltCompendiumEnabled && this.isSltEfCalcMethod))
            ) {
                // set epaEmissionFactor to true for EPA calculation methods
                this.epaEmissionFactor = value.epaEmissionFactor;
                if (!sameCalcMethod || forceReset) {
                    this.resetEmissionFactorControlsSubset();
                    if (this.epaEmissionFactor) {
                        this.emissionForm.get("formulaIndicator").reset();
                    }
                }
            } else if (value == null && forceReset) {
                this.resetEmissionFactorControlsSubset();
                this.emissionForm.get("emissionsNumeratorUom").reset();
                this.emissionForm.get("emissionsDenominatorUom").reset();
                this.emissionForm.get("emissionsUomCode").reset();
                this.emissionForm.get("totalEmissions").reset();
                this.emissionForm.get("formulaIndicator").reset(false);
                this.setupVariableForm([]);
                this.epaEmissionFactor = false;
            } else {
                this.emissionForm.get("emissionsFactorSource").reset();
                this.emissionForm.get("formulaIndicator").reset(false);
                this.setupVariableForm([]);
                this.epaEmissionFactor = false;
            }

            if (value == null || !value.epaEmissionFactor) {
                this.newEmissionFactor = null;
            }
        }
    }

    private resetEmissionFactorControlsSubset() {
        this.emissionForm.get("emissionsFactor").reset();
        this.emissionForm.get("emissionsFactorText").reset();
        this.emissionForm.get("emissionsFactorFormula").reset();
        this.emissionForm.get("formulaVariables").reset();
        this.emissionForm.get("emissionsFactorSource").reset();
    }

    private setupForm() {
        const calcMethod = this.getCalcMethodCodeValue();
        if (calcMethod && calcMethod.totalDirectEntry) {
            this.emissionForm
                .get("emissionsFactor")
                .removeValidators(Validators.required);
            this.emissionForm.get("emissionsFactor").updateValueAndValidity();
            this.emissionForm
                .get("emissionsFactorFormula")
                .removeValidators(Validators.required);
            this.emissionForm
                .get("emissionsFactorFormula")
                .updateValueAndValidity();
            if (!this.emissionForm.get("emissionsFactor").value) {
                this.emissionForm
                    .get("emissionsFactorText")
                    .removeValidators(Validators.required);
                this.emissionForm
                    .get("emissionsFactorText")
                    .updateValueAndValidity();
                this.emissionForm
                    .get("emissionsNumeratorUom")
                    .removeValidators(Validators.required);
                this.emissionForm
                    .get("emissionsNumeratorUom")
                    .updateValueAndValidity();
                this.emissionForm
                    .get("emissionsDenominatorUom")
                    .removeValidators(Validators.required);
                this.emissionForm
                    .get("emissionsDenominatorUom")
                    .updateValueAndValidity();
            }
        }

        this.isCommentRequired();
        if (this.getTotalManualEntry().value) {
            this.emissionForm.get("calculationComment").enable();
            this.emissionForm
                .get("emissionsFactorText")
                .removeValidators(Validators.required);
            this.emissionForm
                .get("emissionsFactorText")
                .updateValueAndValidity();
        } else {
            this.emissionForm.get("calculationComment").reset();
            this.emissionForm.get("calculationComment").disable();
            this.emissionForm
                .get("calculationComment")
                .removeValidators(Validators.required);
            this.emissionForm
                .get("calculationComment")
                .updateValueAndValidity();
        }

        // set epaEmissionFactor to true for EPA calculation methods
        if (calcMethod && calcMethod.epaEmissionFactor) {
            this.epaEmissionFactor = true;
        } else {
            this.emissionForm.get("formulaIndicator").reset(false);
            this.setupVariableForm([]);
            this.epaEmissionFactor = false;
        }

        // set validators of emissionsFactorText to null if the "i prefer to calculate the total emissions" check box is selected
        if (this.emissionForm.get("totalManualEntry").value) {
            this.emissionForm
                .get("emissionsFactorText")
                .removeValidators(Validators.required);
            this.emissionForm.controls.emissionsFactorText.updateValueAndValidity();
        }

        // Reconfigure form after calculation method changes
        this.emissionForm
            .get("emissionsCalcMethodCode")
            .valueChanges.subscribe((value) => {
                this.efNumeratorMismatch = false;
                this.efDenominatorMismatch = false;
                if (
                    value == null &&
                    this.variablesNotInFormula &&
                    this.emissionForm.get("formulaIndicator").value === true
                ) {
                    this.onMethodChange(
                        value,
                        this.emissionForm.get("emissionsCalcMethodCode").status,
                        true
                    );
                } else {
                    this.onMethodChange(
                        value,
                        this.emissionForm.get("emissionsCalcMethodCode").status,
                        false
                    );
                }
            });

        if (this.variablesNotInFormula) {
            this.emissionForm.get("emissionsCalcMethodCode").reset(null);
        }

        // Make user calculate total emissions after changes
        this.setupCalculationFormListeners();

        this.emissionForm
            .get("formulaIndicator")
            .valueChanges.subscribe((value) => {
                if (value) {
                    this.emissionForm.get("emissionsFactorFormula").enable();
                } else {
                    this.emissionForm.get("emissionsFactorFormula").disable();
                }
            });

        // disable fields when semiannual report has been submitted unless emission was added to report after submission
        if (
            this.isMonthlyReportingProcess() &&
            this.semiAnnualReportSubmittedOrApproved &&
            !this.emissionsCreatedAfterSemiannualSubmission.includes(
                this.emission.pollutant.pollutantCode
            )
        ) {
            this.emissionForm.get("pollutant").disable();
            this.emissionForm.get("emissionsCalcMethodCode").disable();
            this.emissionForm.get("emissionsFactor").disable();
            this.emissionForm.get("emissionsFactorFormula").disable();
            this.emissionForm.get("emissionsFactorText").disable();
            this.emissionForm.get("emissionsNumeratorUom").disable();
            this.emissionForm.get("emissionsDenominatorUom").disable();
            this.emissionForm.get("overallControlPercent").disable();
            this.emissionForm.get("emissionsUomCode").disable();
        }

        if (this.emission && (this.emission.previousNotReporting || (this.emission.pollutant.pollutantType === "CAP" && this.emission.pollutant?.pollutantName !== "Lead") || (this.emission.pollutant?.pollutantName === "Lead" && this.reportYear % 3 === 1))) {
            this.emissionForm.get("notReporting")?.disable();
        } else {
            this.emissionForm.get("notReporting")?.enable();
        }
    }

    // reset ef and ef formula when pollutant is changed
    onChange() {
        if (this.emissionForm.value.emissionsCalcMethodCode) {
            this.onMethodChange(
                this.emissionForm.get("emissionsCalcMethodCode").value,
                this.emissionForm.get("emissionsCalcMethodCode").status,
                true
            );
        }
        this.emissionForm.get("notReporting")?.setValue(false);
        this.updateEmissionPreviousNotReporting(false);
    }

    isCommentRequired() {
        const engJudgment = "2";
        if (
            this.emissionForm.value.emissionsCalcMethodCode &&
            this.emissionForm.value.emissionsCalcMethodCode.code === engJudgment
        ) {
            this.emissionForm
                .get("comments")
                .setValidators([Validators.required]);
            this.emissionForm.get("comments").updateValueAndValidity();
        } else {
            this.emissionForm.get("comments").clearValidators();
            this.emissionForm.get("comments").updateValueAndValidity();
        }
    }

    isMonthlyReportingProcess() {
        return this.monthlyReportingEnabled && this.isMonthlyReportingScc;
    }

    allowEfSearch() {
        return (
            (this.sltCompendiumEnabled &&
                this.sltEfCalcMethodCodes.find(
                    (code) => code === this.currentCalcMethod?.code
                )) ||
            this.epaEmissionFactor
        );
    }

    checkUnitConversion(uom1, uom2) {
        if (
            uom1 &&
            uom2 &&
            (uom1.unitType === uom2.unitType ||
                (this.liquidFuelTypeUoM.includes(uom1.unitType) &&
                    this.liquidFuelTypeUoM.includes(uom2.unitType)))
        ) {
            return true;
        }
        return false;
    }

    onCalculate() {
        let isRecalculationErrorToastrSet = false;
        if (!this.canCalculate()) {
            this.emissionForm.markAllAsTouched();
        } else {
            this.buttonsEnabled = false;
            if (!this.reportingPeriod.calculationParameterUom) {
                this.calcParamUom = false;
            } else {
                this.calcParamUom = true;
            }

            if (
                this.reportingPeriod.calculationParameterUom &&
                this.emissionForm.get("emissionsDenominatorUom").value
            ) {
                // IF THROUGHPUT UOM TYPE DOES NOT MATCH EF DENOMINATOR UOM TYPE
                if (
                    !this.checkUnitConversion(
                        this.reportingPeriod.calculationParameterUom,
                        this.emissionForm.get("emissionsDenominatorUom").value
                    )
                ) {
                    // IF THROUGHPUT UOM UNIT IS FUEL USE UOM OR HEAT CONTENT UOM OR IF SCC INVOLVES FUEL OR A GHG EF IS USED
                    if (this.isFuelUseScc ||
                        this.newEmissionFactor?.ghgId ||
                        this.reportingPeriod.calculationParameterUom.fuelUseUom ||
                        this.reportingPeriod.calculationParameterUom.heatContentUom
                    ) {
                        // CHECK IF CAN BE CONVERTED WITH HEAT CONTENT RATIO
                        // if heat content ratio numerator uom type != ef denominator uom type, or heat content denominator (fuel use) uom type != throughput uom type
                        // or if heat content denominator uom type != ef denominator uom type, or heat content numerator uom type != throughput uom type
                        if (
                            (this.checkUnitConversion(
                                this.reportingPeriod.fuelUseUom,
                                this.reportingPeriod?.calculationParameterUom
                            ) &&
                                this.checkUnitConversion(
                                    this.reportingPeriod.heatContentUom,
                                    this.emissionForm.get(
                                        "emissionsDenominatorUom"
                                    ).value
                                )) ||
                            (this.checkUnitConversion(
                                this.reportingPeriod.fuelUseUom,
                                this.emissionForm.get("emissionsDenominatorUom")
                                    .value
                            ) &&
                                this.checkUnitConversion(
                                    this.reportingPeriod.heatContentUom,
                                    this.reportingPeriod
                                        ?.calculationParameterUom
                                ))
                        ) {
                            this.canConvert = true;
                            this.efDenominatorMismatch = false;
                            this.failedRpCalcDesc = null;
                            this.failedDenomDesc = null;
                        }
                        if (!this.canConvert) {
                            this.failedRpCalcDesc =
                                this.reportingPeriod.calculationParameterUom.description;
                            this.failedDenomDesc = this.emissionForm.get(
                                "emissionsDenominatorUom"
                            ).value.description;
                            this.efDenominatorMismatch = true;
                        }
                    } else {
                        this.failedRpCalcDesc =
                            this.reportingPeriod.calculationParameterUom.description;
                        this.failedDenomDesc = this.emissionForm.get(
                            "emissionsDenominatorUom"
                        ).value.description;
                        this.efDenominatorMismatch = true;
                        isRecalculationErrorToastrSet =
                            this.setRecalculationErrorToastr(
                                isRecalculationErrorToastrSet
                            );
                    }
                } else {
                    this.efDenominatorMismatch = false;
                    this.failedRpCalcDesc = null;
                    this.failedDenomDesc = null;
                }
            }

            if (
                !(
                    this.emissionForm.get("emissionsUomCode").value &&
                    this.emissionForm.get("emissionsNumeratorUom").value &&
                    this.checkUnitConversion(
                        this.emissionForm.get("emissionsUomCode").value,
                        this.emissionForm.get("emissionsNumeratorUom").value
                    )
                )
            ) {
                this.failedNumDesc = this.emissionForm.get(
                    "emissionsNumeratorUom"
                ).value.description;
                this.failedTotalDesc =
                    this.emissionForm.get("emissionsUomCode").value.description;
                this.efNumeratorMismatch = true;
                isRecalculationErrorToastrSet =
                    this.setRecalculationErrorToastr(
                        isRecalculationErrorToastrSet
                    );
            } else {
                this.efNumeratorMismatch = false;
                this.failedNumDesc = null;
                this.failedTotalDesc = null;
            }

            if (
                Number(this.reportingPeriod.calculationParameterValue) < 0 ||
                !this.reportingPeriod.calculationParameterValue
            ) {
                this.calcParamValue = false;
            } else {
                this.calcParamValue = true;
            }

            setTimeout(() => {
                if (
                    !(this.efNumeratorMismatch || this.efDenominatorMismatch) &&
                    this.calcParamValue
                ) {
                    const calcEmission = new Emission();
                    Object.assign(calcEmission, this.emissionForm.value);
                    calcEmission.variables = this.generateFormulaVariableDtos();
                    calcEmission.reportingPeriodId = this.reportingPeriod.id;
                    calcEmission.ghgEfId = this.newEmissionFactor
                        ? this.newEmissionFactor.ghgId
                        : null;

                    this.emissionService
                        .calculateEmissionTotal(calcEmission)
                        .subscribe((result) => {
                            this.needsCalculation = false;
                            if (result.formulaIndicator) {
                                this.emissionForm
                                    .get("emissionsFactor")
                                    .setValue(result.emissionsFactor, {
                                        emitEvent: false,
                                    }); // Recalculated in api
                            }
                            this.emissionForm
                                .get("totalEmissions")
                                .setValue(result.totalEmissions);
                            this.toastr.success(
                                "",
                                "Total emissions were successfully calculated"
                            );
                        });
                    this.buttonsEnabled = true;
                } else {
                    isRecalculationErrorToastrSet =
                        this.setRecalculationErrorToastr(
                            isRecalculationErrorToastrSet
                        );
                    this.buttonsEnabled = true;
                }
            }, 1000);
        }
    }

    setRecalculationErrorToastr(setErrorToastr: boolean): boolean {
        if (!setErrorToastr) {
            this.toastr.error(
                "",
                `Total emissions were unable to be recalculated.
        See errors within Emission Information for more details.`
            );
            setErrorToastr = true;
        }
        return setErrorToastr;
    }

    onCancelEdit() {
        this.emissionForm.enable();
        if (this.createMode) {
            this.router.navigate([this.processUrl]);
        } else {
            this.emissionService
                .retrieveWithVariables(this.emission.id)
                .subscribe((result) => {
                    this.emission = result;
                    this.currentCalcMethod =
                        this.emission.emissionsCalcMethodCode;
                    this.emissionForm.reset(this.emission);
                    this.setupVariableFormFromValues(this.emission.variables);
                    this.variablesNotInFormula = this.findVariablesNotInFormula(
                        this.emission
                    );
                    this.emissionForm.disable();
                });
        }
        this.editable = false;
        this.createMode = false;
        this.newEmissionFactor = this.currentEmissionFactor;
    }

    onEdit() {
        this.editable = true;
        // this prevents events from triggering that shouldn't be.
        this.emissionForm.enable({ emitEvent: false });
        this.setupForm();

        // reset carryover variables
        this.needsCalculation = false;
        this.efNumeratorMismatch = false;
        this.failedNumDesc = null;
        this.failedTotalDesc = null;
        this.efDenominatorMismatch = false;
        this.failedRpCalcDesc = null;
        this.failedDenomDesc = null;
    }

    onSubmit() {
        if (
            this.emissionForm.value.formulaIndicator &&
            this.getTotalManualEntry().value
        ) {
            this.emissionForm.get("emissionsFactor").disable();
        }

        if (!this.emissionForm.valid) {
            this.emissionForm.markAllAsTouched();
        } else {
            this.editable = false;

            const saveEmission = new Emission();
            Object.assign(saveEmission, this.emissionForm.getRawValue());

            saveEmission.variables = this.generateFormulaVariableDtos();
            if (this.emission) {
                // Match variable with existing id for variable
                saveEmission.variables.forEach((sv) => {
                    const oldVar = this.emission.variables.find((ov) => {
                        return sv.variableCode.code === ov.variableCode.code;
                    });
                    if (oldVar) {
                        sv.id = oldVar.id;
                    }
                });
            }

            if (this.newEmissionFactor != null) {
                this.currentEmissionFactor = this.newEmissionFactor;
                this.efRange =
                    this.newEmissionFactor.minValue != null ||
                    this.newEmissionFactor.maxValue != null;
                if (this.newEmissionFactor?.webfireId) {
                    saveEmission.webfireEf = this.newEmissionFactor;
                    saveEmission.ghgEfId = null;
                }
                if (this.newEmissionFactor?.ghgId) {
                    saveEmission.ghgEfId = this.newEmissionFactor.ghgId;
                    saveEmission.webfireEf = null;
                }
            } else {
                saveEmission.webfireEf = null;
                saveEmission.ghgEfId = null;
                this.currentEmissionFactor = null;
            }

            if (this.createMode) {
                saveEmission.reportingPeriodId = this.reportingPeriod.id;

                this.emissionService
                    .create(saveEmission)
                    .subscribe((result) => {
                        this.createMode = false;
                        this.sharedService.updateReportStatusAndEmit(
                            this.route
                        );
                        this.router.navigate([this.processUrl]);
                    });
            } else {
                saveEmission.id = this.emission.id;

                this.emissionService
                    .update(saveEmission)
                    .subscribe((result) => {
                        if (
                            this.isMonthlyReportingProcess() &&
                            this.emission &&
                            result
                        ) {
                            if (
                                this.emission.emissionsCalcMethodCode?.code !=
                                    result.emissionsCalcMethodCode?.code ||
                                this.emission.emissionsFactor !=
                                    result.emissionsFactor ||
                                this.emission.emissionsDenominatorUom?.code !=
                                    result.emissionsDenominatorUom?.code ||
                                this.emission.emissionsNumeratorUom?.code !=
                                    result.emissionsNumeratorUom?.code ||
                                this.emission.overallControlPercent !=
                                    result.overallControlPercent ||
                                this.emission.emissionsUomCode?.code !=
                                    result.emissionsUomCode?.code ||
                                this.emission.totalManualEntry !=
                                    result.totalManualEntry
                            ) {
                                this.displayMonthlyReportingWarning();
                            }
                        }

                        this.sharedService.updateReportStatusAndEmit(
                            this.route
                        );
                        this.emissionService
                            .retrieve(this.emission.id)
                            .subscribe((result) => {
                                this.emission = result;
                                this.currentCalcMethod =
                                    this.emission.emissionsCalcMethodCode;
                                this.emissionForm.reset(this.emission);
                                this.variablesNotInFormula = false; // Should not be any more variables mismatches
                                this.setupVariableFormFromValues(
                                    this.emission.variables
                                );
                                this.emissionForm.disable();
                                if (this.newEmissionFactor != null) {
                                    this.currentEmissionFactor =
                                        this.newEmissionFactor;
                                    this.efRange =
                                        this.newEmissionFactor.minValue !=
                                            null ||
                                        this.newEmissionFactor.maxValue != null;
                                }
                            });
                    });
            }
        }
    }

    openSearchEfModal() {
        if (
            !this.emissionForm.get("pollutant").valid ||
            !this.emissionForm.get("emissionsCalcMethodCode").valid
        ) {
            this.emissionForm.get("pollutant").markAsTouched();
            this.emissionForm.get("emissionsCalcMethodCode").markAsTouched();
        } else {
            const efCriteria = new EmissionFactor();
            efCriteria.sccCode = Number(this.process?.sccCode);
            efCriteria.pollutantCode =
                this.emissionForm.get("pollutant").value.pollutantCode;
            efCriteria.calculationMaterialCode =
                this.reportingPeriod.calculationMaterialCode;
            efCriteria.calculationParameterTypeCode =
                this.reportingPeriod.calculationParameterTypeCode;

            // set controlIndicator based on which calculation method is selected
            if (this.getCalcMethodCodeValue().controlIndicator) {
                efCriteria.controlIndicator = true;
            } else {
                efCriteria.controlIndicator = false;
            }

            this.efService
                .search(efCriteria, !this.epaEmissionFactor)
                .subscribe((result) => {
                    const modalRef = this.modalService.open(
                        EmissionFactorModalComponent,
                        { size: "xl", backdrop: "static" }
                    );

                    modalRef.componentInstance.tableData = result;
                    modalRef.componentInstance.isSltCalcMethod =
                        this.isSltEfCalcMethod;
                    modalRef.componentInstance.reportYear = this.reportYear;

                    // update form when modal closes successfully
                    modalRef.result.then(
                        (modalEf: EmissionFactor) => {
                            if (modalEf) {
                                this.emissionForm
                                    .get("formulaIndicator")
                                    .setValue(modalEf.formulaIndicator);
                                this.emissionForm
                                    .get("emissionsFactor")
                                    .setValue(modalEf.emissionFactor);
                                this.emissionForm
                                    .get("emissionsFactorFormula")
                                    .setValue(modalEf.emissionFactorFormula);
                                this.emissionForm
                                    .get("emissionsNumeratorUom")
                                    .setValue(modalEf.emissionsNumeratorUom);
                                this.emissionForm
                                    .get("emissionsDenominatorUom")
                                    .setValue(modalEf.emissionsDenominatorUom);
                                this.emissionForm
                                    .get("emissionsFactorSource")
                                    .setValue(modalEf.source);

                                let descriptionText =
                                    this.formatDescriptionText(
                                        modalEf.description
                                    );
                                this.emissionForm
                                    .get("emissionsFactorText")
                                    .setValue(descriptionText);

                                this.setupVariableForm(modalEf.variables || []);
                                this.onCalculate();
                                this.newEmissionFactor = modalEf;
                                this.efRange =
                                    modalEf.minValue != null ||
                                    modalEf.maxValue != null;
                            }
                        },
                        () => {
                            // needed for dismissing without errors
                            this.modalService.dismissAll();
                        }
                    );
                });
        }
    }

    canCalculate() {
        return (
            this.emissionForm.get("formulaIndicator").valid &&
            (this.emissionForm.get("formulaIndicator").value ||
                this.emissionForm.get("emissionsFactor").valid) &&
            this.emissionForm.get("emissionsNumeratorUom").valid &&
            this.emissionForm.get("emissionsDenominatorUom").valid &&
            this.emissionForm.get("overallControlPercent").valid &&
            this.emissionForm.get("emissionsUomCode").valid &&
            this.emissionForm.get("formulaVariables").valid &&
            this.emissionForm.get("totalManualEntry").valid
        );
    }

    setupCalculationFormListeners() {
        this.emissionForm
            .get("emissionsFactor")
            .valueChanges.subscribe((value) => {
                if (this.emissionForm.enabled) {
                    this.needsCalculation = true;
                    if (this.getCalcMethodCodeValue()?.totalDirectEntry) {
                        if (value) {
                            this.emissionForm
                                .get("emissionsNumeratorUom")
                                .addValidators(Validators.required);
                            this.emissionForm
                                .get("emissionsDenominatorUom")
                                .addValidators(Validators.required);
                            this.emissionForm
                                .get("emissionsFactorText")
                                .addValidators(Validators.required);
                        } else {
                            this.emissionForm
                                .get("emissionsNumeratorUom")
                                .removeValidators(Validators.required);
                            this.emissionForm
                                .get("emissionsDenominatorUom")
                                .removeValidators(Validators.required);
                            this.emissionForm
                                .get("emissionsFactorText")
                                .removeValidators(Validators.required);
                        }
                        this.emissionForm
                            .get("emissionsNumeratorUom")
                            .updateValueAndValidity();
                        this.emissionForm
                            .get("emissionsDenominatorUom")
                            .updateValueAndValidity();
                        this.emissionForm
                            .get("emissionsFactorText")
                            .updateValueAndValidity();
                    }
                }
            });

        this.emissionForm
            .get("emissionsFactorFormula")
            .valueChanges.subscribe((value) => {
                if (this.emissionForm.enabled) {
                    this.needsCalculation = true;
                }
            });

        this.emissionForm
            .get("formulaVariables")
            .valueChanges.subscribe((value) => {
                if (this.emissionForm.enabled) {
                    this.needsCalculation = true;
                }
            });

        this.emissionForm
            .get("emissionsNumeratorUom")
            .valueChanges.subscribe((value) => {
                if (this.emissionForm.enabled) {
                    this.needsCalculation = true;
                }
            });

        this.emissionForm
            .get("emissionsDenominatorUom")
            .valueChanges.subscribe((value) => {
                if (this.emissionForm.enabled) {
                    this.needsCalculation = true;
                }
            });

        this.emissionForm
            .get("overallControlPercent")
            .valueChanges.subscribe((value) => {
                if (this.emissionForm.enabled) {
                    this.needsCalculation = true;
                }
            });

        this.emissionForm
            .get("totalManualEntry")
            .valueChanges.subscribe((value) => {
                this.efDenominatorMismatch = false;
                this.efNumeratorMismatch = false;
                if (this.emissionForm.enabled) {
                    this.needsCalculation = true;
                    if (value && this.getCalcMethodCodeValue()) {
                        this.emissionForm.get("calculationComment").enable();
                        this.emissionForm
                            .get("calculationComment")
                            .addValidators(Validators.required);
                    } else {
                        this.emissionForm.get("calculationComment").disable();
                        this.emissionForm
                            .get("calculationComment")
                            .removeValidators(Validators.required);
                    }
                    this.emissionForm
                        .get("calculationComment")
                        .updateValueAndValidity();
                }
            });

        this.emissionForm
            .get("emissionsUomCode")
            .valueChanges.subscribe((value) => {
                if (this.emissionForm.enabled) {
                    this.needsCalculation = true;
                }
            });
    }

    getTotalManualEntry() {
        return this.emissionForm.get("totalManualEntry");
    }

    getEmissionsFactor() {
        // Must have factor with both numerator and denominator uoms to calculate emissions
        return (
            (this.emissionForm.get("emissionsFactor")?.value ||
                (this.emissionForm.get("emissionsFactorFormula").enabled &&
                    this.emissionForm.get("emissionsFactorFormula")?.value)) &&
            this.emissionForm.get("emissionsNumeratorUom")?.value &&
            this.emissionForm.get("emissionsDenominatorUom")?.value
        );
    }

    emissionsFactorRequired() {
        return this.emissionForm
            .get("emissionsFactor")
            .hasValidator(Validators.required);
    }

    getCalcMethodCodeValue(): CalculationMethodCode {
        return this.emissionForm.getRawValue()?.emissionsCalcMethodCode;
    }

    getFormulaVariableForm() {
        return this.emissionForm.get("formulaVariables") as FormGroup;
    }

    private findVariablesNotInFormula(emission: Emission): boolean {
        // Additional vars in formula but not in vars list added in retrieveWithVariables()

        let ret = false;
        const msg = `Your emissions information may contain an emission factor with a formula that has been updated, or an
      invalid formula variable. Please re-select the calculation method/emission factor and enter the new variable
      information.`;

        if (emission.formulaIndicator && emission.variables?.length > 0) {
            const tildes = "~~~~"; // Avoids issue with variables at beginning or end of formula
            const notLetterRegExp = "[^A-Za-z]";
            for (const v of emission.variables) {
                if (v.variableCode?.code) {
                    const formulaRegExp = new RegExp(
                        notLetterRegExp + v.variableCode.code + notLetterRegExp
                    );
                    if (
                        !formulaRegExp.test(
                            tildes + emission.emissionsFactorFormula + tildes
                        )
                    ) {
                        ret = true;
                    }
                } else {
                    // Shouldn't enter, but will force re-selecting calculation method
                    ret = true;
                }
            }
        }
        if (ret) {
            this.toastr.error(msg);
        }
        return ret;
    }

    private setupVariableFormFromValues(values: EmissionFormulaVariable[]) {
        this.setupVariableForm(values.map((v) => v.variableCode));

        values.forEach((v) => {
            this.getFormulaVariableForm()
                .get(v.variableCode.code)
                .reset(v.value);
        });
    }

    private setupVariableForm(newVars: EmissionFormulaVariableCode[]) {
        const formKeys = Object.keys(this.getFormulaVariableForm().controls);
        const varCodes = newVars.map((v) => {
            return v.code;
        });

        // Remove unneeded variables while leaving existing values
        formKeys.forEach((key) => {
            if (!varCodes.includes(key)) {
                this.getFormulaVariableForm().removeControl(key);
            }
        });

        this.formulaVariables = newVars;

        newVars.forEach((v) => {
            if (VariableValidationType.PERCENT === v.validationType) {
                this.getFormulaVariableForm().addControl(
                    v.code,
                    new FormControl(null, [
                        Validators.required,
                        Validators.min(0),
                        Validators.max(100),
                    ])
                );
            } else if (VariableValidationType.CASU === v.validationType) {
                this.getFormulaVariableForm().addControl(
                    v.code,
                    new FormControl(null, [
                        Validators.required,
                        Validators.min(1.5),
                        Validators.max(7),
                    ])
                );
            } else if (
                VariableValidationType.DAY_PER_YEAR === v.validationType
            ) {
                this.getFormulaVariableForm().addControl(
                    v.code,
                    new FormControl(null, [
                        Validators.required,
                        Validators.min(0),
                        Validators.max(365),
                    ])
                );
            }
            this.getFormulaVariableForm().addControl(
                v.code,
                new FormControl(null, Validators.required)
            );

            this.getFormulaVariableForm()
                .get(v.code)
                .valueChanges.pipe(debounceTime(400), distinctUntilChanged())
                .subscribe(
                    () => {
                        if (
                            this.editable &&
                            this.emissionForm.get("formulaIndicator")?.value &&
                            this.getFormulaVariableForm().valid
                        ) {
                            this.buttonsEnabled = false;
                            const calcEmissionFactor = new Emission();
                            Object.assign(
                                calcEmissionFactor,
                                this.emissionForm.value
                            );
                            calcEmissionFactor.variables =
                                this.generateFormulaVariableDtos();
                            this.emissionService
                                .calculateEmissionFactor(calcEmissionFactor)
                                .subscribe(
                                    (result) => {
                                        if (result.formulaIndicator) {
                                            this.emissionForm
                                                .get("emissionsFactor")
                                                .setValue(
                                                    result.emissionsFactor,
                                                    { emitEvent: false }
                                                );
                                        }
                                        this.needsCalculation = true; // for total Emissions calculated validator
                                        this.buttonsEnabled = true;
                                    },
                                    () => {
                                        this.buttonsEnabled = true;
                                    }
                                );
                        }
                    },
                    () => {
                        this.buttonsEnabled = true;
                    }
                );
        });
    }

    private generateFormulaVariableDtos(): EmissionFormulaVariable[] {
        const formulaValues: EmissionFormulaVariable[] = [];

        this.formulaVariables.forEach((fv) => {
            formulaValues.push(
                new EmissionFormulaVariable(
                    this.getFormulaVariableForm().get(fv.code).value,
                    fv
                )
            );
        });
        return formulaValues;
    }

    private displayMonthlyReportingWarning() {
        this.toastr.warning(
            "Saved changes impact monthly reporting. Please update impacted values within monthly reporting.",
            "",
            { timeOut: 20000, extendedTimeOut: 10000, closeButton: true }
        );
    }

    searchPollutants = (text$: Observable<string>) =>
        text$.pipe(
            debounceTime(200),
            distinctUntilChanged(),
            map(
                (term) =>
                    this.pollutantValues &&
                    this.pollutantValues
                        .filter(
                            (v) =>
                                v.pollutantName
                                    .toLowerCase()
                                    .indexOf(term.toLowerCase()) > -1 ||
                                v.pollutantCode
                                    .toLowerCase()
                                    .indexOf(term.toLowerCase()) > -1 ||
                                (v.pollutantCasId
                                    ? v.pollutantCasId
                                          .toLowerCase()
                                          .indexOf(term.toLowerCase()) > -1
                                    : false)
                        )
                        .slice(0, 20)
            )
        );

    pollutantFormatter = (result: Pollutant) => formatPollutant(result);

    emissionsCalculatedValidator(): ValidatorFn {
        return (control: FormGroup): { [key: string]: any } | null => {
            const efControl = control.get("emissionsFactor");
            const totalDirectEntry = control.get("emissionsCalcMethodCode")
                ?.value?.totalDirectEntry;
            if (
                ((efControl.enabled && !totalDirectEntry) ||
                    (totalDirectEntry && efControl.value)) &&
                !control.get("totalManualEntry").value &&
                !this.isMonthlyReportingProcess()
            ) {
                return this.needsCalculation
                    ? { emissionsCalculated: true }
                    : null;
            }
            return null;
        };
    }

    emissionFactorGreaterThanZeroValidator(): ValidatorFn {
        return (control: FormGroup): { [key: string]: any } | null => {
            const emissionFactor = control.get("emissionsFactor");
            if (
                emissionFactor.enabled &&
                emissionFactor.value != null &&
                emissionFactor.value != "" &&
                emissionFactor.value <= 0
            ) {
                return { efFactorLessThanOrEqualToZero: true };
            } else {
                return null;
            }
        };
    }

    emissionFactorOutsideRangeValidator(): ValidatorFn {
        return (control: FormGroup): { [key: string]: any } | null => {
            const emissionFactor = control.get("emissionsFactor");
            if (
                this.newEmissionFactor &&
                emissionFactor.enabled &&
                emissionFactor.value !== null &&
                emissionFactor.value !== "" &&
                this.newEmissionFactor?.minValue != null &&
                this.newEmissionFactor?.maxValue != null &&
                (emissionFactor.value < this.newEmissionFactor.minValue ||
                    emissionFactor.value > this.newEmissionFactor.maxValue)
            ) {
                return { efFactorOutsideRange: true };
            } else if (
                this.currentEmissionFactor &&
                !this.newEmissionFactor &&
                emissionFactor.enabled &&
                emissionFactor.value !== null &&
                emissionFactor.value !== "" &&
                this.currentEmissionFactor?.minValue != null &&
                this.currentEmissionFactor?.maxValue != null &&
                (emissionFactor.value < this.currentEmissionFactor.minValue ||
                    emissionFactor.value > this.currentEmissionFactor.maxValue)
            ) {
                return { efFactorOutsideRange: true };
            } else {
                return null;
            }
        };
    }

    pollutantEmissionsUoMValidator(): ValidatorFn {
        return (control: FormGroup): { [key: string]: any } | null => {
            const pollutant = control.get("pollutant");
            if (
                pollutant !== null &&
                pollutant.value !== undefined &&
                pollutant.value !== null &&
                control.get("emissionsUomCode") !== null
            ) {
                if (
                    pollutant.value.pollutantCode !== undefined &&
                    pollutant.value.pollutantCode === "605" &&
                    control.get("emissionsUomCode").value !== null &&
                    control.get("emissionsUomCode").value.code !== "CURIE"
                ) {
                    return { emissionsUomCodeCurie: true };
                }
                return null;
            }
        };
    }

    checkPercentSulfurRange(): ValidatorFn {
        return (control: FormGroup): ValidationErrors | null => {
            const emissionFormulaVar = control.get("formulaVariables");
            if (
                emissionFormulaVar !== undefined &&
                emissionFormulaVar !== null &&
                Object.keys(emissionFormulaVar.value)[0] === "SU"
            ) {
                if (
                    emissionFormulaVar.value.SU !== null &&
                    (emissionFormulaVar.value.SU < 0.00001 ||
                        emissionFormulaVar.value.SU > 10)
                ) {
                    return { invalidSulfurRange: true };
                }
                return null;
            }
        };
    }

    checkPercentAshRange(): ValidatorFn {
        return (control: FormGroup): ValidationErrors | null => {
            const emissionFormulaVar = control.get("formulaVariables");
            if (
                emissionFormulaVar !== undefined &&
                emissionFormulaVar !== null &&
                Object.keys(emissionFormulaVar.value)[0] === "A"
            ) {
                if (
                    emissionFormulaVar.value.A !== null &&
                    (emissionFormulaVar.value.A < 0.01 ||
                        emissionFormulaVar.value.A > 30)
                ) {
                    return { invalidAshRange: true };
                }
                return null;
            }
        };
    }

    overallControlPercentValidator(): ValidatorFn {
        return (control: FormGroup): { [key: string]: any } | null => {
            const overallControl = control.get("overallControlPercent");
            const calcMethod = control.get("emissionsCalcMethodCode");
            if (
                calcMethod.value?.controlIndicator &&
                overallControl.value &&
                overallControl.value !== 0
            ) {
                return { overallControlPercentInvalid: true };
            }
            return null;
        };
    }

    checkSignificantFigures(): ValidatorFn {
        return (control: FormGroup): { [key: string]: any } | null => {
            const totalEmissions = control.get("totalEmissions");
            if (
                totalEmissions.value &&
                this.formUtils.getSignificantFigures(totalEmissions.value) >
                    this.formUtils.emissionsMaxSignificantFigures
            ) {
                return { significantFiguresInvalid: true };
            }
            return null;
        };
    }

    canDeactivate(): Promise<boolean> | boolean {
        if ((!this.createMode && !this.editable) || !this.emissionForm.dirty) {
            return true;
        }
        return this.utilityService.canDeactivateModal();
    }

    @HostListener("window:beforeunload", ["$event"])
    beforeunloadHandler(event) {
        if ((this.createMode || this.editable) && this.emissionForm.dirty) {
            event.preventDefault();
            event.returnValue = "";
        }
        return true;
    }

    formatDescriptionText(descriptionText: string): string {
        if (descriptionText && descriptionText.length > 100) {
            descriptionText = descriptionText.substring(0, 97);
            descriptionText = descriptionText.concat("...");
        }
        return descriptionText;
    }

    updateEmissionPreviousNotReporting(hasEmission: boolean) {
        let previousEmission;

        if (hasEmission && this.emission) {
            previousEmission = this.previousEmissions?.find(emission => emission.pollutant?.pollutantCode === this.emission.pollutant?.pollutantCode);
            this.emission.previousNotReporting = previousEmission? previousEmission.notReporting : true;
        } else {
            previousEmission = this.previousEmissions?.find(emission => emission.pollutant?.pollutantCode === this.emissionForm.value.pollutant?.pollutantCode);
        }

        if ((this.emissionForm.value.pollutant?.pollutantType === "CAP" && this.emissionForm.value.pollutant?.pollutantName !== "Lead") || (this.emissionForm.value.pollutant?.pollutantName === "Lead" && this.reportYear % 3 === 1)) {
            previousEmission = null;
        }

        if (previousEmission) {
            if (previousEmission?.notReporting) {
                this.emissionForm.get("notReporting")?.disable();
            } else {
                this.emissionForm.get("notReporting")?.enable();
            }
        } else {
            this.emissionForm.get("notReporting")?.disable();
        }
    }
}
