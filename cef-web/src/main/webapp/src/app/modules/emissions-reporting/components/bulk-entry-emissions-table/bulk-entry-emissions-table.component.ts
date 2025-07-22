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
    OnChanges,
    Input,
    Output,
    EventEmitter,
} from "@angular/core";
import { BaseSortableTable } from "src/app/shared/components/sortable-table/base-sortable-table";
import { EmissionService } from "src/app/core/services/emission.service";
import { ActivatedRoute } from "@angular/router";
import {
    FormBuilder,
    FormControl,
    Validators,
    ValidatorFn,
    FormGroup,
} from "@angular/forms";
import { ToastrService } from "ngx-toastr";
import { BulkEntryEmissionHolder } from "src/app/shared/models/bulk-entry-emission-holder";
import { Emission } from "src/app/shared/models/emission";
import { FacilitySite } from "src/app/shared/models/facility-site";
import { FormUtilsService } from "src/app/core/services/form-utils.service";
import { OperatingStatus } from "src/app/shared/enums/operating-status";

@Component({
    selector: "app-bulk-entry-emissions-table",
    templateUrl: "./bulk-entry-emissions-table.component.html",
    styleUrls: ["./bulk-entry-emissions-table.component.scss"],
})
export class BulkEntryEmissionsTableComponent
    extends BaseSortableTable
    implements OnInit, OnChanges
{
    @Input() tableData: BulkEntryEmissionHolder[];
    @Input() readOnlyMode: boolean;
    @Output() emissionsUpdated = new EventEmitter<BulkEntryEmissionHolder[]>();
    baseUrl: string;
    facilitySite: FacilitySite;
    reportYear: number;
    savingBulkInfo = false;

    emissionForm = this.fb.group({});

    operatingStatus = OperatingStatus;

    constructor(
        private emissionService: EmissionService,
        private route: ActivatedRoute,
        private fb: FormBuilder,
        private toastr: ToastrService,
        public formUtils: FormUtilsService
    ) {
        super();
    }

    ngOnInit() {
        this.route.paramMap.subscribe((map) => {
            this.baseUrl = `/facility/${map.get("facilityId")}/report/${map.get(
                "reportId"
            )}`;
        });

        this.route.data.subscribe((data: { facilitySite: FacilitySite }) => {
            this.facilitySite = data.facilitySite;
            const year = data.facilitySite.emissionsReport.year;
            this.reportYear = year;
        });
    }

    saveEmissionChanges(
        response: any,
        payload: { id: number; notReporting: boolean }
    ): void {
        // Merge payload (partial updates) into the response (full object)
        const updatedEmission = { ...response, ...payload };

        // Send the updated object to the server
        this.emissionService.update(updatedEmission).subscribe({
            next: (res) => {
                this.toastr.success(
                    `Emission ${updatedEmission.pollutant.pollutantName} reporting status updated successfully`
                );
            },
            error: (err) => {
                console.error(
                    `Failed to update emission ${updatedEmission.id}:`,
                    err
                );
                let errorMessage = "An unexpected error occurred.";
                if (err.status === 500) {
                    errorMessage =
                        "Server error occurred. Please try again later.";
                } else if (err.error && err.error.message) {
                    errorMessage = err.error.message;
                }
                this.toastr.error(errorMessage, "Error Updating Emission");
            },
        });
    }

    onCheckboxChange(event: Event, emission: any): void {
        const checkbox = event.target as HTMLInputElement;
        emission.notReporting = checkbox.checked;

        const payload = {
            id: emission.id,
            notReporting: emission.notReporting,
        };

        this.emissionService.retrieve(emission.id).subscribe({
            next: (response) => {
                this.saveEmissionChanges(response, payload);
            },
            error: (err) => {
                console.log(err);
            },
        });
    }

    ngOnChanges() {
        this.tableData.sort((a, b) => {
            if (a.unitIdentifier === b.unitIdentifier) {
                return a.emissionsProcessIdentifier >
                    b.emissionsProcessIdentifier
                    ? 1
                    : -1;
            }
            return a.unitIdentifier > b.unitIdentifier ? 1 : -1;
        });

        this.tableData.forEach((rp) => {
            rp.emissions.sort((a, b) =>
                a.pollutant.pollutantName > b.pollutant.pollutantName ? 1 : -1
            );
        });

        this.tableData.forEach((rp) => {
            rp.emissions.forEach((e) => {
                if (
                    (e.totalManualEntry ||
                        e.emissionsCalcMethodCode.totalDirectEntry) &&
                    !(
                        rp.operatingStatusCode.code ===
                            OperatingStatus.TEMP_SHUTDOWN ||
                        rp.operatingStatusCode.code ===
                            OperatingStatus.PERM_SHUTDOWN
                    )
                ) {
                    if (!this.emissionForm.contains("" + e.id)) {
                        const fc = new FormControl(
                            {
                                value: e.totalEmissions,
                                disabled: false,
                            },
                            {
                                updateOn: "blur",
                            }
                        );
                        fc.setValidators([
                            Validators.required,
                            Validators.min(0),
                            this.checkSignificantFigures(fc),
                        ]);
                        this.emissionForm.setControl("" + e.id, fc);
                    }
                } else {
                    // use setControl to make sure new values are applied
                    const fc = new FormControl(
                        {
                            value: e.totalEmissions,
                            disabled: true,
                        },
                        {
                            updateOn: "blur",
                        }
                    );
                    fc.setValidators([
                        Validators.required,
                        Validators.min(0),
                        this.checkSignificantFigures(fc),
                    ]);
                    this.emissionForm.setControl("" + e.id, fc);
                }
            });
        });
    }

    onSubmit() {
        if (!this.emissionForm.valid) {
            this.emissionForm.markAllAsTouched();
        } else {
            this.savingBulkInfo = true;
            const emissionDtos: Emission[] = [];

            for (const [key, value] of Object.entries(
                this.emissionForm.value
            )) {
                const e = new Emission();
                e.id = +key;
                e.totalEmissions = +value;
                emissionDtos.push(e);
            }

            this.emissionService
                .bulkUpdate(this.facilitySite.id, emissionDtos)
                .subscribe((result) => {
                    this.savingBulkInfo = false;
                    this.emissionsUpdated.emit(result);
                    this.toastr.success(
                        "",
                        "Changes successfully saved and Total Emissions recalculated."
                    );
                    // reset dirty flags
                    this.emissionForm.markAsPristine();
                });
        }
    }

    checkSignificantFigures(totalEmissions: FormControl): ValidatorFn {
        return (control: FormGroup): { [key: string]: any } | null => {
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
}
