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
import { Component, OnInit, ViewChild } from '@angular/core';
import { MasterFacilityRecordService } from 'src/app/core/services/master-facility-record.service';
import { MasterFacilityRecord } from 'src/app/shared/models/master-facility-record';
import { EditMasterFacilityInfoComponent } from 'src/app/modules/dashboards/components/edit-master-facility-info/edit-master-facility-info.component';
import { BaseCodeLookup } from 'src/app/shared/models/base-code-lookup';
import { UserContextService } from 'src/app/core/services/user-context.service';
import { User } from 'src/app/shared/models/user';
import { ConfirmationDialogComponent } from "../../../../shared/components/confirmation-dialog/confirmation-dialog.component";
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { EmissionsReportingService } from 'src/app/core/services/emissions-reporting.service';
import {FacilitySiteService} from "../../../../core/services/facility-site.service";

@Component({
  selector: 'app-master-facility-information',
  templateUrl: './master-facility-information.component.html',
  styleUrls: ['./master-facility-information.component.scss']
})
export class MasterFacilityInformationComponent implements OnInit {

  records: MasterFacilityRecord[] = [];
  selectedFacility: MasterFacilityRecord;
  editInfo = false;
  addFacility = false;
  programSystemCode: BaseCodeLookup;
  agencyDataValues: BaseCodeLookup[];
  user: User;
  mostRecentReportYear: number;
  readonly defaultLatLongTolerance = 0.003;

  @ViewChild(EditMasterFacilityInfoComponent)
  private masterFacilityRecordComponent: EditMasterFacilityInfoComponent;

  constructor(
              private reportService: EmissionsReportingService,
              private facilitySiteService: FacilitySiteService,
              private modalService: NgbModal,
              private userContextService: UserContextService,
              private mfrService: MasterFacilityRecordService) { }

  ngOnInit(): void {
      this.userContextService.getUser().subscribe( user => {
          this.user = user;
      });

      this.mfrService.getProgramSystemCodes()
      .subscribe(result => {
        this.agencyDataValues = result.sort((a, b) => (a.code > b.code) ? 1 : -1);
      });

      this.refreshFacilityList();
      if (this.user.isReviewer()) {
        this.mfrService.getUserProgramSystemCode()
          .subscribe(result => {
                  this.programSystemCode = result;
          });
      }
  }

  onAgencySelected() {
    this.refreshFacilityList();
  }

  refreshFacilityList() {
    if (this.user.isReviewer()) {
      this.mfrService.getMyProgramRecords()
      .subscribe(result =>
        this.records = result.sort((a, b) => (a.name > b.name) ? 1 : -1)
        );
    } else if (this.user.isAdmin() && this.programSystemCode) {
      this.mfrService.getProgramRecords(this.programSystemCode.code)
      .subscribe(result => {
        this.records = result.sort((a, b) => (a.name > b.name) ? 1 : -1);
    });
    } else {
      this.records = [];
      this.selectedFacility = null;
    }
  }

  onFacilitySelected(facility: MasterFacilityRecord) {
    this.setEditInfo(false);
    this.setAddFacility(false);
    this.selectedFacility = facility;
  }

  onEditClick(facility: MasterFacilityRecord) {
      this.setEditInfo(true);
      this.selectedFacility = facility;
  }

  setEditInfo(value: boolean) {
    this.editInfo = value;
  }

  setAddFacility(value: boolean) {
    this.addFacility = value;
  }

  onCancelEdit() {
    if (this.addFacility) {
        this.selectedFacility = null;
    }
    this.setEditInfo(false);
    this.setAddFacility(false);
  }

  updateMasterFacilityRecord() {

      if (!this.masterFacilityRecordComponent.facilitySiteForm.valid) {
        this.masterFacilityRecordComponent.facilitySiteForm.markAllAsTouched();
      } else {
          const updatedMasterFacility = new MasterFacilityRecord();
          Object.assign(updatedMasterFacility, this.masterFacilityRecordComponent.facilitySiteForm.value);

          if (!this.addFacility) {
              this.reportService.getReportByMfrAndYear(this.selectedFacility.id, updatedMasterFacility.statusYear)
                  .subscribe(report => {
                      if (report) {
                          if (updatedMasterFacility.operatingStatusCode?.code == "ONRE") {
                              const modalRef = this.modalService.open(ConfirmationDialogComponent, {size: 'md'});
                              const modalMessage = `You cannot change the Operating Status for this Facility
                                                (Agency Id ${updatedMasterFacility.agencyFacilityIdentifier}) to ONRE
                                                with Status Year ${updatedMasterFacility.statusYear}
                                                because there is already a report for that year.`;
                              modalRef.componentInstance.message = modalMessage;
                              modalRef.componentInstance.continue.subscribe(() => {
                                  // needed to make sure process completes
                              });
                          } else {
                              this.facilitySiteService.retrieveForReport(report.id)
                                  .subscribe(facilitySite => {
                                      if (facilitySite.operatingStatusCode.code == "ONRE" && report.year == this.selectedFacility.statusYear && report.eisLastSubmissionStatus != 'NotStarted') {
                                          const modalRef = this.modalService.open(ConfirmationDialogComponent, {size: 'md'});
                                          const modalMessage = `This Facility (Agency Id ${updatedMasterFacility.agencyFacilityIdentifier})
                                                            is in the ONRE Operating Status and already has a submitted report for this reporting year.
                                                            The Operating Status cannot be changed at this time.`;
                                          modalRef.componentInstance.message = modalMessage;
                                          modalRef.componentInstance.continue.subscribe(() => {
                                              // blank to make sure process completes
                                          });
                                      } else if (facilitySite.operatingStatusCode.code == "ONRE" && report.year == this.selectedFacility.statusYear && report.eisLastSubmissionStatus == 'NotStarted') {
                                          this.reportService.delete(report.id).subscribe(() => {
                                              this.addOrUpdateMasterFacilityInfo(updatedMasterFacility);
                                          });
                                      } else {
                                          this.addOrUpdateMasterFacilityInfo(updatedMasterFacility);
                                      }
                                  });
                          }
                      } else {
                          this.reportService.getCurrentReport(this.selectedFacility.id.toString())
                              .subscribe(currentReport => {
                                  if (currentReport) {
                                      this.mostRecentReportYear = currentReport.year;
                                  }
                                  if (updatedMasterFacility.operatingStatusCode?.code == "ONRE") {
                                      if (this.mostRecentReportYear == null || updatedMasterFacility.statusYear > this.mostRecentReportYear) {

                                          const modalRef = this.modalService.open(ConfirmationDialogComponent, {size: 'md'});
                                          const modalMessage = `Are you sure you want to change the operating status for this Facility
                                                        (Agency Id ${updatedMasterFacility.agencyFacilityIdentifier}) to ONRE? This Facility will be
                                                        removed from CAERS. You may not be able to undo this change. `;
                                          modalRef.componentInstance.message = modalMessage;
                                          modalRef.componentInstance.continue.subscribe(() => {

                                              updatedMasterFacility.id = this.selectedFacility.id;
                                              updatedMasterFacility.eisProgramId = this.selectedFacility.eisProgramId;
                                              updatedMasterFacility.programSystemCode = this.selectedFacility.programSystemCode;

                                              this.mfrService.update(updatedMasterFacility)
                                                  .subscribe(result => {

                                                      Object.assign(this.selectedFacility, result);
                                                      this.setEditInfo(false);
                                                      this.setAddFacility(false);

                                                      this.reportService.createReportFromScratch(this.selectedFacility.id, this.selectedFacility.statusYear)
                                                          .subscribe(reportResp => {
                                                              this.reportService.acceptReports([reportResp.body.id], '')
                                                                  .subscribe(() => {
                                                                      // needed to make sure process completes
                                                                  });
                                                          });
                                                  });
                                          });
                                      } else {
                                          const modalRef = this.modalService.open(ConfirmationDialogComponent, {size: 'md'});
                                          const modalMessage = `This Facility (Agency Id ${updatedMasterFacility.agencyFacilityIdentifier})
                                                            has a submitted report for a year after the selected Status Year (${updatedMasterFacility.statusYear}).
                                                            The Operating Status cannot be changed to ONRE for that year.`;
                                          modalRef.componentInstance.message = modalMessage;
                                          modalRef.componentInstance.continue.subscribe(() => {
                                              // blank to make sure process completes
                                          });
                                      }
                                  }
                                   else {
                                      this.addOrUpdateMasterFacilityInfo(updatedMasterFacility);
                                  }
                              });
                      }
                  })
          } else {
              updatedMasterFacility.programSystemCode = this.programSystemCode;
              updatedMasterFacility.coordinateTolerance = this.defaultLatLongTolerance;

              this.mfrService.add(updatedMasterFacility)
                  .subscribe(result => {
                      Object.assign(this.selectedFacility, result);
                      this.setEditInfo(false);
                      this.setAddFacility(false);
                      this.refreshFacilityList();
                  });
          }
      }
  }

  addMasterFacilityRecord() {
      const emptyMfr: MasterFacilityRecord = new MasterFacilityRecord();
      emptyMfr.agencyFacilityIdentifier = '';
      this.selectedFacility = emptyMfr;
      this.setEditInfo(false);
      this.setAddFacility(true);
  }

  addOrUpdateMasterFacilityInfo(mfr: MasterFacilityRecord) {
      if (!this.addFacility) {
          mfr.id = this.selectedFacility.id;
          mfr.eisProgramId = this.selectedFacility.eisProgramId;
          mfr.programSystemCode = this.selectedFacility.programSystemCode;

          this.mfrService.update(mfr)
              .subscribe(result => {
                  Object.assign(this.selectedFacility, result);
                  this.setEditInfo(false);
                  this.setAddFacility(false);
              });
      } else {
          mfr.programSystemCode = this.programSystemCode;
          mfr.coordinateTolerance = this.defaultLatLongTolerance;

          this.mfrService.add(mfr)
              .subscribe(result => {
                  Object.assign(this.selectedFacility, result);
                  this.setEditInfo(false);
                  this.setAddFacility(false);
                  this.refreshFacilityList();
              });
      }
  }

}
