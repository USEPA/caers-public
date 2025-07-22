/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
