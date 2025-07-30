/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit } from '@angular/core';
import { UserFeedbackService } from 'src/app/core/services/user-feedback.service';
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { FormControl } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UserFeedbackReportModalComponent } from '../../components/user-feedback-report-modal/user-feedback-report-modal.component';
import { UserFeedback } from 'src/app/shared/models/user-feedback';
import { UserContextService } from 'src/app/core/services/user-context.service';
import { UserFeedbackStats } from 'src/app/shared/models/user-feedback-stats';

@Component({
  selector: 'app-admin-user-feedback',
  templateUrl: './admin-user-feedback.component.html',
  styleUrls: ['./admin-user-feedback.component.scss']
})
export class AdminUserFeedbackComponent extends BaseSortableTable implements OnInit {

  tableData: UserFeedback[];
  stats: UserFeedbackStats;
  page = 1;
  pageSize = 25;

  cboFilterYear = new FormControl();
  cboFilterAgency = new FormControl();

  availableYears: number[];
  availableAgencies: string[];

  userProgramSystem: string;

  intuitiveRateAvg = 0;
  dataEntryScreensAvg = 0;
  dataEntryBulkUploadAvg = 0;
  calculationScreensAvg = 0;
  controlsAndControlPathAssignAvg = 0;
  qualityAssuranceChecksAvg = 0;
  overallReportingTimeAvg = 0;

  constructor(private userFeedbackService: UserFeedbackService,
              private userService: UserContextService,
              private modalService: NgbModal) {
    super();
  }

  ngOnInit() {
    const CurrentYear = new Date().getFullYear() - 1;

    this.userService.getUser().subscribe(user => {
      this.userProgramSystem = user.programSystemCode;
    });

    this.tableData = [];

    this.cboFilterYear.valueChanges.subscribe(() => {
      this.retrieveData();
    });

    this.cboFilterAgency.valueChanges.subscribe(() => {
      this.retrieveData();
    });

    this.cboFilterYear.setValue(CurrentYear,
      {emitEvent: false, emitModelToViewChange: true, emitViewToModelChange: false});

    this.cboFilterAgency.setValue('ALL_AGENCIES',
      {emitEvent: false, emitModelToViewChange: true, emitViewToModelChange: false});

    this.retrieveOptions();
    this.retrieveData();

    this.controller.paginate = true;
  }

  retrieveOptions() {
    this.userFeedbackService.retrieveAvailableYears()
      .subscribe(years => {
        this.availableYears = years;
      });
    this.userFeedbackService.retrieveAvailableProgramSystemCodes()
      .subscribe(agencies => {
        this.availableAgencies = agencies;
      });
  }

  retrieveData() {
    this.userFeedbackService.retrieveAllByYearAndProgramSystemCode(this.cboFilterYear.value, this.cboFilterAgency.value)
      .subscribe(userFB => {
        this.tableData = userFB;
        this.retrieveStats();
    });
  }

  retrieveStats() {
    this.userFeedbackService.retrieveStatsByYearAndProgramSystemCode(this.cboFilterYear.value, this.cboFilterAgency.value)
      .subscribe(stats => {
        this.stats = stats;
      });
  }

  openFeedbackModal(feedback: UserFeedback) {
    const modalWindow = this.modalService.open(UserFeedbackReportModalComponent, { size: 'lg' });
    modalWindow.componentInstance.feedback = feedback;
  }

  exportTable(): void {
    this.userFeedbackService.downloadFilteredReport(this.tableData);
  }

}
