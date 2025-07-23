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


