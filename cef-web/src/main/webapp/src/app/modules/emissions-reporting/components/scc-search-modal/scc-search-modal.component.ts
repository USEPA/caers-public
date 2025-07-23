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
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormControl, Validators } from '@angular/forms';
import { environment } from 'src/environments/environment';
import { LookupService } from 'src/app/core/services/lookup.service';
import { PointSourceSccCode } from 'src/app/shared/models/point-source-scc-code';
import { EmissionsReport } from "src/app/shared/models/emissions-report";
import { Process } from "src/app/shared/models/process";

@Component({
  selector: 'app-scc-search-modal',
  templateUrl: './scc-search-modal.component.html',
  styleUrls: ['./scc-search-modal.component.scss']
})
export class SccSearchModalComponent extends BaseSortableTable implements OnInit {
  @Input() currentSccCode: string;
  @Input() emissionsReport: EmissionsReport;
  @Input() process: Process;

  tableData: PointSourceSccCode[];
  searchControl = new FormControl('', Validators.required);

  currentCode: PointSourceSccCode;
  sccSearchUrl = environment.sccSearchUrl;

  constructor(public activeModal: NgbActiveModal, private lookupService: LookupService) {
    super();
  }

  ngOnInit() {
    if (this.currentSccCode?.length > 0) {
      this.lookupService.retrievePointSourceSccCode(this.currentSccCode)
        .subscribe(result => {
          if (result) {
            this.currentCode = result;
          }
        });
    }
  }

  onSearch() {
     if (!this.searchControl.valid) {
      this.searchControl.markAsTouched();
    } else {
      this.lookupService.basicSccSearch(this.searchControl.value, this.emissionsReport.programSystemCode.code)
      .subscribe(result => {

        this.tableData = result;
        this.searchControl.setErrors(null);
      }, error => {
        console.log(error);
      });
    }
  }

  onClose() {
    this.activeModal.dismiss();
  }

  onSubmit(selectedCode: PointSourceSccCode) {
    if (!this.process?.previousProcess) {
        this.searchControl.setErrors(null);
      this.activeModal.close(selectedCode);
    }
    else {
      if (selectedCode.fuelUseRequired != this.currentCode.fuelUseRequired
          || selectedCode.monthlyReporting != this.currentCode.monthlyReporting
          || this.emissionsReport.midYearSubmissionStatus === 'SUBMITTED'
          || this.emissionsReport.midYearSubmissionStatus === 'APPROVED') {
            if (!this.searchControl.hasError('invalidNewScc')) {
              this.searchControl.setErrors({invalidNewScc: true});
            }
      }
      else {
          this.searchControl.setErrors(null);
          this.activeModal.close(selectedCode);
      }
    }
  }

}
