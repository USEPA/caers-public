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
