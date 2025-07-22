/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ThresholdStatus } from 'src/app/shared/enums/threshold-status';

@Component({
  selector: 'app-threshold-screening-gadnr-modal',
  templateUrl: './threshold-screening-gadnr-modal.component.html',
  styleUrls: ['./threshold-screening-gadnr-modal.component.scss']
})
export class ThresholdScreeningGadnrModalComponent implements OnInit {
  @Input() year: number;
  selectedStatus: string;
  belowThreshold: any;
  isTriennialReportYear = false;

  readonly potentialToEmitStr = 'Potential to emit ';
  readonly sulfurDioxideStr = 'Sulfur Dioxide (SO2)';
  readonly volatileOrganicCompoundsStr = 'Volatile Organic Compounds (VOC)';
  readonly nitrogenOxideStr = 'Nitrogen Oxides (NOX)';
  readonly carbonMonoxideStr = 'Carbon Monoxide (CO)';
  readonly primaryPM25Str = 'Primary PM2.5. As applicable, also report filterable and condensable components.';
  readonly primaryPM10Str = 'Primary PM10. As applicable, also report filterable and condensable components.';
  readonly ammoniaStr = 'Ammonia (NH3)';
  readonly greaterThanOrEqual = '&#8805; ';
  readonly oneThousand = 1000;
  readonly oneHundred = 100;
  readonly twoHundredFifty = 250;
  readonly twentyFiveHundred = 2500;
  readonly pollutantHeader = 'Pollutant';

  readonly annualThresholdHeader = 'Annual Threshold (in Tons per Year)';
  readonly annualPollutantThresholds = [
    { pollutant: this.sulfurDioxideStr, threshold: this.potentialToEmitStr + this.greaterThanOrEqual + this.twentyFiveHundred },
    { pollutant: this.volatileOrganicCompoundsStr, threshold: this.potentialToEmitStr + this.greaterThanOrEqual + this.twoHundredFifty },
    { pollutant: this.nitrogenOxideStr, threshold: this.potentialToEmitStr + this.greaterThanOrEqual + this.twentyFiveHundred },
    { pollutant: this.carbonMonoxideStr, threshold: this.potentialToEmitStr + this.greaterThanOrEqual + this.twentyFiveHundred },
    { pollutant: this.primaryPM25Str, threshold: this.potentialToEmitStr + this.greaterThanOrEqual + this.twoHundredFifty },
    { pollutant: this.primaryPM10Str, threshold: this.potentialToEmitStr + this.greaterThanOrEqual + this.twoHundredFifty },
    { pollutant: this.ammoniaStr, threshold: this.potentialToEmitStr + this.greaterThanOrEqual + this.twoHundredFifty }
  ];
  readonly triennialThresholdHeader = 'Triennial Threshold (in Tons per Year)';
  readonly triennialPollutantThresholds = [
    { pollutant: this.sulfurDioxideStr, threshold: this.potentialToEmitStr + this.greaterThanOrEqual + this.oneHundred },
    { pollutant: this.volatileOrganicCompoundsStr, threshold: this.potentialToEmitStr + this.greaterThanOrEqual + this.oneHundred },
    { pollutant: this.nitrogenOxideStr, threshold: this.potentialToEmitStr + this.greaterThanOrEqual + this.oneHundred },
    { pollutant: this.carbonMonoxideStr, threshold: this.potentialToEmitStr + this.greaterThanOrEqual + this.oneThousand },
    { pollutant: this.primaryPM25Str, threshold: this.potentialToEmitStr + this.greaterThanOrEqual + this.oneHundred },
    { pollutant: this.primaryPM10Str, threshold: this.potentialToEmitStr + this.greaterThanOrEqual + this.oneHundred },
    { pollutant: this.ammoniaStr, threshold: this.potentialToEmitStr + this.greaterThanOrEqual + this.oneHundred },
    { pollutant: 'Lead (PB)', threshold: 'Actual emissions ' + this.greaterThanOrEqual + 0.5 }
  ];

  constructor(public activeModal: NgbActiveModal) { }

  ngOnInit(): void {
    if (this.year && (this.year % 3 === 1)) {
      this.isTriennialReportYear = true;
    }
  }

  onClose() {
    this.activeModal.dismiss();
  }

  onSubmit() {
    if (this.selectedStatus === 'PS') {
        this.activeModal.close(ThresholdStatus.PERM_SHUTDOWN);
    } else if (this.selectedStatus === 'TS') {
        this.activeModal.close(ThresholdStatus.TEMP_SHUTDOWN);
    } else if (this.selectedStatus === 'OP' && this.belowThreshold == 'true') {
        this.activeModal.close(ThresholdStatus.OPERATING_BELOW_THRESHOLD);
    } else if (this.selectedStatus === 'OP' && this.belowThreshold == 'false') {
        this.activeModal.close(ThresholdStatus.OPERATING_ABOVE_THRESHOLD);
    }
  }
  
  canSubmit() {
    return this.selectedStatus === 'TS' || this.selectedStatus === 'PS' || (this.selectedStatus === 'OP' && this.belowThreshold);
  }

}
