/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ValidationStatus } from 'src/app/shared/enums/validation-status.enum';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { ToastrService } from 'ngx-toastr';
import { Subject } from 'rxjs';
import { ValidationResult } from 'src/app/shared/models/validation-result';
import { ControlPath } from 'src/app/shared/models/control-path';
import { ThresholdStatus } from 'src/app/shared/enums/threshold-status';
import { ActiveLinkInfo } from 'src/app/shared/models/active-link-info';

@Injectable({
  providedIn: 'root'
})
export class SharedService {
  // Observable string sources
  private emitChangeSource = new Subject<any>();
  private emitSubmissionReviewChangeSource = new Subject<any>();
  private emitValidationResultChangeSource = new Subject<ValidationResult>();
  private emitControlsSource = new Subject<ControlPath[]>();
  private emitHideBoolSource = new Subject<any>();
  private emitReportIdSource = new Subject<any>();
  private emitProcessSccSource = new Subject<any>();
  private emitProcessOpStatus = new Subject<any>();
  private emitAdminBannerSource = new Subject<any>();
  private emitUserFacilityAssociationChangeSource = new Subject<any>();
  private sideNavCollapsedMap = new Map<string, boolean>();
  private emitActiveLinkInfoSource = new Subject<ActiveLinkInfo>();
  private emitSideNavItemNoRouteChangeSource = new Subject<number>();

  constructor(private toastr: ToastrService) { }

  // Observable string streams
  changeEmitted$ = this.emitChangeSource.asObservable();
  submissionReviewChangeEmitted$ = this.emitSubmissionReviewChangeSource.asObservable();
  validationResultChangeEmitted$ = this.emitValidationResultChangeSource.asObservable();
  controlsResultChangeEmitted$ = this.emitControlsSource.asObservable();
  hideBoolChangeEmitted$ = this.emitHideBoolSource.asObservable();
  reportIdChangeEmitted$ = this.emitReportIdSource.asObservable();
  processSccChangeEmitted$ = this.emitProcessSccSource.asObservable();
  processOpStatusChangeEmitted$ = this.emitProcessOpStatus.asObservable();
  adminBannerChangeEmitted$ = this.emitAdminBannerSource.asObservable();
  userFacilityAssociationChangeEmitted$ = this.emitUserFacilityAssociationChangeSource.asObservable();
  activeLinkInfoEmitted$ = this.emitActiveLinkInfoSource.asObservable();
  sideNavItemChangeNoRouteChangeEmitted$ = this.emitSideNavItemNoRouteChangeSource.asObservable();

  // Service message commands
  emitChange(change: any) {
    this.emitChangeSource.next(change);
  }

  emitHideBoolChange(change: any) {
    this.emitHideBoolSource.next(change);
  }

  emitSubmissionChange(change: any) {
    this.emitSubmissionReviewChangeSource.next(change);
  }

  emitValidationResultChange(change: ValidationResult) {
    this.emitValidationResultChangeSource.next(change);
  }

  emitControlsChange(change: ControlPath[]){
    this.emitControlsSource.next(change);
  }

  emitReportIdChange(change: any) {
    this.emitReportIdSource.next(change);
  }

  emitProcessSccChange(change: any) {
    this.emitProcessSccSource.next(change);
  }

  emitProcessOpStatusChange(change: any) {
    this.emitProcessOpStatus.next(change);
  }

  emitAdminBannerChange(change: any) {
    this.emitAdminBannerSource.next(change);
  }

  emitUserFacilityAssociationChange(change: any) {
    this.emitUserFacilityAssociationChangeSource.next(change);
  }

  emitActiveLinkInfoChange(change: ActiveLinkInfo) {
    this.emitActiveLinkInfoSource.next(change);
  }

  emitSideNavItemChangeNoRouteChange(facilitySiteId: number) {
    this.emitSideNavItemNoRouteChangeSource.next(facilitySiteId);
  }

  updateReportStatusAndEmit(route: ActivatedRoute) {
    route.data.subscribe((data: { facilitySite: FacilitySite }) => {

      this.updateReportStatusAndEmitFacilitySiteChange(data.facilitySite);
    });
  }

  updateReportStatusAndEmitFacilitySiteChange(facilitySite: FacilitySite) {

      if (facilitySite.emissionsReport.validationStatus !== ValidationStatus.UNVALIDATED) {
          facilitySite.emissionsReport.validationStatus = ValidationStatus.UNVALIDATED;

          if (!facilitySite.emissionsReport.thresholdStatus || facilitySite.emissionsReport.thresholdStatus === ThresholdStatus.OPERATING_ABOVE_THRESHOLD) {
              this.toastr.warning('You must run the Quality Checks on your report again since changes have been made to the report data.');
          }

          this.emitChange(facilitySite);
      }
  }

  setSideNavCollapsedMapEntry(key: string, collapsed: boolean) {
    this.sideNavCollapsedMap.set(key, collapsed);
  }

  getSideNavCollapsedMapEntry(key: string) {
    return this.sideNavCollapsedMap.get(key);
  }

  clearSideNavCollapsedMap() {
    this.sideNavCollapsedMap.clear();
  }

}
