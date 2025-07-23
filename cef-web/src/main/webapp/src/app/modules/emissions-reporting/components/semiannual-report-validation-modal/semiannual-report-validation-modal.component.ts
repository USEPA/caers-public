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
