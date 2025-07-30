/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit, Input } from '@angular/core';
import { ReportSummary } from 'src/app/shared/models/report-summary';
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';

@Component({
  selector: 'app-report-summary-table',
  templateUrl: './report-summary-table.component.html',
  styleUrls: ['./report-summary-table.component.scss']
})
export class ReportSummaryTableComponent extends BaseSortableTable implements OnInit {
    showDormants: boolean;

    @Input() emissionsReportYear: number;
    @Input() tableData: ReportSummary[];
    @Input() radiationData: ReportSummary[];

    nonDormantData = [];
    dormantData = [];

    constructor() {
        super();
     }

    ngOnInit() {
        this.nonDormantData = this.tableData.filter((item) => !item.notReporting);
        this.dormantData = this.tableData.filter((item) => item.notReporting);
        this.showDormants = false;
    }

toggleDormants() {
    this.showDormants = !this.showDormants;
  }

    /***
     * Calculate the total number of tons for all pollutants
     */
    getPollutantTonsTotal(): number {
        let currentYearTonsTotal = 0;
        let precision = 0;

        if (this.nonDormantData) {
            this.nonDormantData.forEach(reportSummary => {
                currentYearTonsTotal += reportSummary.emissionsTonsTotal;

                if (this.getPrecision(reportSummary.emissionsTonsTotal) > precision) {
                    precision = this.getPrecision(reportSummary.emissionsTonsTotal);
                }
            });
        }

        return Math.round(currentYearTonsTotal*Math.pow(10, precision))/Math.pow(10, precision);
    }


    /***
     * Calculate the total number of tons for all pollutants from the previous year
     */
    getPreviousPollutantTonsTotal(): number {
        let previousYearTonsTotal = 0;
        let precision = 0;

        if (this.nonDormantData) {
            this.nonDormantData.forEach(reportSummary => {
                if (reportSummary.previousYear) {
                    previousYearTonsTotal += reportSummary.previousYearTonsTotal;

                    if (this.getPrecision(reportSummary.previousYearTonsTotal) > precision) {
                        precision = this.getPrecision(reportSummary.previousYearTonsTotal);
                    }
                }
            });

            return Math.round(previousYearTonsTotal*Math.pow(10, precision))/Math.pow(10, precision);
        }

    }


    getPrecision(value: number) {
        if (value.toString().includes('.')) {
            return value.toString().split('.')[1].length;
        } else {
            return 0;
        }
    }

}
