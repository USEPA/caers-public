/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import { MonthlyReportingPeriod } from "src/app/shared/enums/monthly-reporting-periods";
import { FacilitySite } from "src/app/shared/models/facility-site";
import { ActivatedRoute } from "@angular/router";
import {SubmissionReviewModal} from "../submission-review-modal/submission-review-modal.component";
import { User } from "src/app/shared/models/user";
import {EmissionsReportingService} from "src/app/core/services/emissions-reporting.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-semiannual-report-validation-modal',
  templateUrl: './semiannual-report-validation-modal.component.html',
  styleUrls: ['./semiannual-report-validation-modal.component.scss']
})
export class SemiannualReportValidationModalComponent implements OnInit {

  @Input() facilitySite: FacilitySite;
  @Input() route: ActivatedRoute;
  @Input() user: User;
  period = MonthlyReportingPeriod;
  baseUrl: string;
  reportId: string;

  constructor(
      private emissionsReportingService: EmissionsReportingService,
      public activeModal: NgbActiveModal,
      private modalService: NgbModal,
      private toastr: ToastrService) { }

  ngOnInit(): void {
      this.route.paramMap
          .subscribe(map => {
              this.baseUrl = `/facility/${map.get('facilityId')}/report/${map.get('reportId')}`;
              this.reportId = map.get('reportId');
          });
  }

  onClose() {
    this.activeModal.dismiss();
  }

  onSubmit() {
      const modalRef = this.modalService.open(SubmissionReviewModal, { size: 'lg', backdrop: 'static' });
      modalRef.componentInstance.report = this.facilitySite.emissionsReport;
      modalRef.componentInstance.facility = this.facilitySite;
      modalRef.componentInstance.url = this.baseUrl;
      modalRef.componentInstance.semiannualReport = true;
      this.activeModal.close(true);
  }

  notifyNeiCertifier(){
    this.emissionsReportingService.readyToCertifyNotification(this.facilitySite.emissionsReport.id, true)
      .subscribe(result => {
        if (result) {
          this.toastr.success('', 'An email notification has been sent to the NEI Certifier(s) for this facility.');
        } else {
          this.toastr.error('An error occurred and an email notification has not been sent.');
        }
      });
  }
}
