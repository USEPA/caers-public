/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit } from '@angular/core';
import { FormBuilder} from '@angular/forms';
import { UserFeedbackService } from 'src/app/core/services/user-feedback.service';
import { UserFeedback } from 'src/app/shared/models/user-feedback';
import { ActivatedRoute, Router} from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { SharedService } from 'src/app/core/services/shared.service';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { EmissionsReportingService } from 'src/app/core/services/emissions-reporting.service';
import { EmissionsReport } from 'src/app/shared/models/emissions-report';
import { UserService } from 'src/app/core/services/user.service';
import { User } from 'src/app/shared/models/user';


@Component({
  selector: 'app-user-feedback',
  templateUrl: './user-feedback.component.html',
  styleUrls: ['./user-feedback.component.scss']
})
export class UserFeedbackComponent implements OnInit {
  reportId: number;
  facilityId: string;
  facilitySite: FacilitySite;
  baseUrl: string;
  report: EmissionsReport;
  user: User;
  clicked: boolean = false;

  feedbackForm = this.fb.group({
    beneficialFunctionalityComments: [null],
    difficultFunctionalityComments: [null],
    enhancementComments: [null],
    intuitiveRating: [null],
    dataEntryScreens: [null],
    dataEntryBulkUpload: [null],
    calculationScreens: [null],
    controlsAndControlPathAssignments: [null],
    qualityAssuranceChecks: [null],
    overallReportingTime: [null]
  });

  constructor(private fb: FormBuilder,
              private userFeedbackService: UserFeedbackService,
              private route: ActivatedRoute,
              private toastr: ToastrService,
              private sharedService: SharedService,
              private router: Router,
              private reportingService: EmissionsReportingService,
              private userService: UserService,
              private emissionsReportingService: EmissionsReportingService) { }


  ngOnInit() {
    this.route.paramMap
    .subscribe(params => {
      this.reportId = +params.get('reportId');
      this.facilityId = params.get('facilityId');
      this.userService.getCurrentUser().subscribe((user) => {
        this.user = user;
        });
      this.reportingService.getReport(this.reportId).subscribe((report) => {
        this.report = report;
      });

      this.baseUrl = `/facility/${params.get('facilityId')}/report`;
    });

    // emits the report info to the sidebar
    this.route.data
    .subscribe((data: { facilitySite: FacilitySite }) => {
      this.facilitySite = data.facilitySite;
      this.sharedService.emitChange(data.facilitySite);
    });
    this.sharedService.emitHideBoolChange(true);
  }

  onSubmit()  {
    this.clicked = true;
    const saveUserFeedback = new UserFeedback();

    saveUserFeedback.userName = this.user.firstName + ' ' + this.user.lastName;
    saveUserFeedback.userId = this.user.userRoleId.toString();
    saveUserFeedback.userRole = this.user.role;
    saveUserFeedback.reportId = this.reportId;
    saveUserFeedback.facilityName = this.facilitySite.name;
    saveUserFeedback.year = this.report.year;
    saveUserFeedback.programSystemCode = this.report.programSystemCode;
    Object.assign(saveUserFeedback, this.feedbackForm.value);

    this.userFeedbackService.create(saveUserFeedback).subscribe(() => {
      this.toastr.success('', "Your feedback has successfully been submitted, thank you.");
      this.sharedService.emitHideBoolChange(false);
          this.emissionsReportingService.updateHasSubmittedFeedback(this.reportId).subscribe((result) => {
              this.sharedService.emitHideBoolChange(false);
              this.router.navigateByUrl(this.baseUrl);
      });
    });
  }

  onNoThanks() {
        this.clicked = true;
        this.emissionsReportingService.updateHasSubmittedFeedback(this.reportId).subscribe((result) => {
          this.sharedService.emitHideBoolChange(false);
          this.router.navigateByUrl(this.baseUrl);
        });
  }

}


