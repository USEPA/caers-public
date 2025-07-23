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
