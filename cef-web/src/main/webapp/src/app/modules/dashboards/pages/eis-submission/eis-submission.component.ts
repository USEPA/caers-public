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
import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {EisDataService} from 'src/app/core/services/eis-data.service';
import {EisDataReport, EisSubmissionStatus} from 'src/app/shared/models/eis-data';
import {BaseSortableTable} from 'src/app/shared/components/sortable-table/base-sortable-table';
import {FormControl, FormArray} from '@angular/forms';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap/modal/modal-ref';
import { ToastrService } from 'ngx-toastr';
import { EisTransactionAttachment } from 'src/app/shared/models/eis-transaction-attachment';
import { FileDownloadService } from 'src/app/core/services/file-download.service';

interface EisDataStats {
   notStarted: number;
   qaFacility: number;
   prodFacility: number;
   qaEmissions: number;
   prodEmissions: number;
   complete: number;
}

enum SubmissionType {

   QA = 'QA',
   PRODUCTION = 'Production'
}

enum DataCategoryType {

   FACILITY_INVENTORY = 'Facility Inventory',
   POINT_EMISSIONS = 'Point Emissions'
}

interface DlgEditComment {
   modalRef: NgbModalRef;
   waiting: boolean;
   maxlength: number;
   report: EisDataReport;
}

const CommentMaxLength = 2000;

const CurrentYear = new Date().getFullYear() - 1;

@Component({
   selector: 'app-eis-submission',
   templateUrl: './eis-submission.component.html',
   styleUrls: ['./eis-submission.component.scss']
})
export class EisSubmissionComponent extends BaseSortableTable implements OnInit {

   @ViewChild('EditCommentModal', {static: true})
   editCommentTemplate: TemplateRef<any>;

   cboFilterStatus = new FormControl();
   cboFilterYear = new FormControl();

   txtFilter = new FormControl();

   txtComment = new FormControl();

   cboSubmitCategory = new FormControl();
   cboSubmitType = new FormControl();

   chkSelectAll = new FormControl();
   reportCheckboxes = new FormArray([]);

   dlgEditComment: DlgEditComment;

   stats: EisDataStats;

   tableData: EisDataReport[];

   selectedReports: Set<number>;

   availableYears: number[];

   availableStatuses: EisSubmissionStatus[];

   dataCategories = DataCategoryType;
   submissionTypes = SubmissionType;

   selectedYear = CurrentYear;
   invalidSelection = false;

   eisSubmissionStatus = EisSubmissionStatus;

   constructor(private modalService: NgbModal,
               private eisDataService: EisDataService,
               private fileDownloadService: FileDownloadService,
               private toastr: ToastrService) {

      super();
   }

   ngOnInit() {

      this.availableYears = [];
      this.availableStatuses = [];

      this.tableData = [];
      this.reportCheckboxes = new FormArray([]);
      this.selectedReports = new Set<number>();

      this.stats = {
         notStarted: 0,
         qaFacility: 0,
         prodFacility: 0,
         qaEmissions: 0,
         prodEmissions: 0,
         complete: 0
      };

      this.cboFilterYear.valueChanges.subscribe(() => {
         this.selectedYear = this.cboFilterYear.value;
         this.txtFilter.setValue(null, {emitEvent: false});
         this.cboFilterStatus.setValue(EisSubmissionStatus.All, {emitEvent: false});
         this.retrieveDataStats(() => {
              this.retrieveData();
          });
      });

      this.cboFilterStatus.valueChanges.subscribe(() => {
         this.txtFilter.setValue(null, {emitEvent: false});
         this.retrieveData();
      });

      this.chkSelectAll.valueChanges.subscribe(() => {
          this.onSelectAll();
      });

      this.txtFilter.valueChanges.subscribe(() => {
          this.retrieveData();
      });

      this.txtComment.valueChanges.subscribe(() => {

         const comment = this.txtComment.value;
         if (comment && comment.length > this.dlgEditComment.maxlength) {
            this.txtComment.setValue(comment.substring(0, this.dlgEditComment.maxlength),
               {emitEvent: false, emitViewToModelChange: false, emitModelToViewChange: true});
         }

      });

      this.availableYears.push(CurrentYear);
      this.cboFilterYear.setValue(CurrentYear,
         {emitEvent: false, emitModelToViewChange: true, emitViewToModelChange: false});

      this.availableStatuses.push(EisSubmissionStatus.All);

      this.cboFilterStatus.setValue(EisSubmissionStatus.All,
         {emitEvent: false, emitModelToViewChange: true, emitViewToModelChange: false});

      this.cboFilterYear.setValue(CurrentYear);
      this.filterMenu();
      this.retrieveDataStats(() => {

         this.retrieveData();
      });

      this.controller.paginate = true;
   }

   retrieveDataStats(onComplete?: () => void) {

      this.eisDataService.retrieveStatsByYear(this.selectedYear).subscribe({
         next: (stats) => {

            stats.availableYears.forEach(year => {
               if (this.availableYears.indexOf(year) < 0) {

                  this.availableYears.push(year);
               }
            });

            this.stats.notStarted = 0;
            this.stats.qaEmissions = 0;
            this.stats.qaFacility = 0;
            this.stats.prodEmissions = 0;
            this.stats.prodFacility = 0;
            this.stats.complete = 0;

            stats.statuses.forEach(stat => {

               const status: EisSubmissionStatus = EisSubmissionStatus[stat.status] as EisSubmissionStatus;

               switch (status) {

                  case EisSubmissionStatus.NotStarted:
                     this.stats.notStarted = stat.count;
                     break;
                  case EisSubmissionStatus.QaEmissions:
                     this.stats.qaEmissions = stat.count;
                     break;
                  case EisSubmissionStatus.QaFacility:
                     this.stats.qaFacility = stat.count;
                     break;
                  case EisSubmissionStatus.ProdEmissions:
                     this.stats.prodEmissions = stat.count;
                     break;
                  case EisSubmissionStatus.ProdFacility:
                     this.stats.prodFacility = stat.count;
                     break;
                  case EisSubmissionStatus.Complete:
                     this.stats.complete = stat.count;
                     break;
               }
            });

         },
         complete: () => {

            if (onComplete) {
               onComplete();
            }
         }
      });
   }

   filterMenu(onComplete?: () => void) {
      this.availableStatuses = [];
      this.availableStatuses.push(EisSubmissionStatus.All);

      this.eisDataService.retrieveStatsByYear(this.cboFilterYear.value).subscribe({
         next: (stats) => {
            stats.statuses.forEach(stat => {

               const status: EisSubmissionStatus = EisSubmissionStatus[stat.status] as EisSubmissionStatus;

               if (status) {

                  const idx = this.availableStatuses.indexOf(status);

                  if (stat.count) {
                     if (idx < 0) {
                        this.availableStatuses.push(status);
                     }
                  } else {
                     if (idx > -1) {
                        this.availableStatuses.splice(idx, 1);
                     }
                  }
               }
            });

            return stats;
         },
         complete: () => {

            if (onComplete) {
               onComplete();
            }
         }
      });
   }

   retrieveData() {
      const csvKeys = 'facilityName,agencyFacilityIdentifier,eisProgramId,lastTransactionId';
      const keys = csvKeys
          .replace(/\\w/g, '')
          .replace(/,,/g, ',')
          .split(',');
      this.filterMenu();
      this.eisDataService.searchData({
         year: this.cboFilterYear.value,
         status: this.cboFilterStatus.value

      }).subscribe(resp => {

         this.reportCheckboxes = new FormArray([]);
         this.tableData = resp.reports.filter(item => {
            if (this.txtFilter.value) {
                let match = false;
                for (const key of keys) {
                    const str = item[key];

                    if (str && str.toLowerCase().indexOf(this.txtFilter.value.toLowerCase()) > -1) {
                        match = true;
                        break;
                    }
                }

                return match;
            } else {
                return true;
            }
         }).map(report => {
            if (report.lastSubmissionStatus) {
               report.lastSubmissionStatus = EisSubmissionStatus[report.lastSubmissionStatus];
            }
            report.reportCheckbox = new FormControl('');
            report.reportCheckbox.valueChanges.subscribe(() => {
                this.onSelectChange(report.reportCheckbox.value, report.emissionsReportId);
            });
            this.reportCheckboxes.push(report.reportCheckbox);
            return report;
         });

         this.onSort({column: 'facilityName', direction: 'asc'});
         this.chkSelectAll.setValue(false);
      });
   }

   onFilterQaFacility() {
      this.cboFilterYear.setValue(this.selectedYear, {emitEvent: false});
      this.cboFilterStatus.setValue(EisSubmissionStatus.QaFacility);
   }

   onFilterAll() {
      this.cboFilterYear.setValue(this.selectedYear, {emitEvent: false});
      this.cboFilterStatus.setValue(EisSubmissionStatus.All);
   }

   onFilterNotStarted() {
      this.cboFilterYear.setValue(this.selectedYear, {emitEvent: false});
      this.cboFilterStatus.setValue(EisSubmissionStatus.NotStarted);
   }

   onFilterProdEmissions() {
      this.cboFilterYear.setValue(this.selectedYear, {emitEvent: false});
      this.cboFilterStatus.setValue(EisSubmissionStatus.ProdEmissions);
   }

   onFilterQaEmissions() {
      this.cboFilterYear.setValue(this.selectedYear, {emitEvent: false});
      this.cboFilterStatus.setValue(EisSubmissionStatus.QaEmissions);
   }

   onFilterProdFacility() {
      this.cboFilterYear.setValue(this.selectedYear, {emitEvent: false});
      this.cboFilterStatus.setValue(EisSubmissionStatus.ProdFacility);
   }

   onFilterComplete() {
      this.cboFilterYear.setValue(this.selectedYear, {emitEvent: false});
      this.cboFilterStatus.setValue(EisSubmissionStatus.Complete);
   }

   onEditCommentClick(report: EisDataReport) {

      this.dlgEditComment = {

         report,
         waiting: false,
         maxlength: CommentMaxLength,
         modalRef: this.modalService.open(this.editCommentTemplate, {
            backdrop: 'static'
         })
      };

      this.txtComment.setValue(report.comments);
      this.txtComment.enable();
   }

   onSubmitClick() {

      if (this.selectedReports.size < 1 || this.convertTypeAndCategory() === null) {
         this.invalidSelection = true;
      }

      if (this.selectedReports.size > 0 && this.convertTypeAndCategory() !== null) {

         const submissionStatus: EisSubmissionStatus = this.convertTypeAndCategory();

         if (submissionStatus) {
            this.invalidSelection = false;

            this.eisDataService.submitReports({
               submissionStatus,
               emissionsReportIds: this.selectedReports

            }).subscribe(data => {

               this.toastr.success('', data.reports.length + ' report(s) were successfully transmitted to EIS.');
               // grab new stats
               this.retrieveDataStats(() => {

                  this.retrieveData();
               });
            }, error => {

               console.log(error);
               this.toastr.error('', 'An error occurred while trying to transmit reports to EIS and the reports were not transmitted successfully.');
            });
         }

         this.selectedReports.clear();
         this.cboSubmitType.setValue(null);
         this.cboSubmitCategory.setValue(null);
         this.cboFilterStatus.setValue(EisSubmissionStatus.All);
      }

   }

   onCancelCommentClick() {

      this.dlgEditComment.modalRef.dismiss();

      this.dlgEditComment = {
         report: null,
         waiting: false,
         maxlength: CommentMaxLength,
         modalRef: null
      };
   }

   onUpdateCommentClick() {

      this.dlgEditComment.waiting = true;
      this.txtComment.disable();

      let comment = this.txtComment.value;
      if (comment && comment.length > this.dlgEditComment.maxlength) {
         comment = comment.substring(0, this.dlgEditComment.maxlength);
      }

      this.eisDataService.updateComment(this.dlgEditComment.report.emissionsReportId, comment)
         .subscribe(eisDataReport => {

            this.dlgEditComment.report.comments = eisDataReport.comments;

            this.onCancelCommentClick();
         });
   }

   onPassedCheck(report: EisDataReport) {
      this.eisDataService.updateEisPassedStatus(report.emissionsReportId, !report.passed)
         .subscribe(eisDataReport => {

            // grab new stats
            this.retrieveDataStats(() => {

               this.retrieveData();
            });
         });
   }

   onClearFilterClick() {
      this.txtFilter.setValue(null);
   }


   onSelectAll() {
       this.selectedReports.clear();
       this.reportCheckboxes.controls.forEach(cb => {
           cb.setValue(this.chkSelectAll.value);
        });
   }

   onSelectChange(selected: boolean, reportId: number) {
       if (selected) {
           if (this.areAllChecked() === true) {
               this.chkSelectAll.setValue(true, {emitEvent: false});
           }
           this.selectedReports.add(reportId);
        } else {
            this.chkSelectAll.setValue(false, {emitEvent: false});
            this.selectedReports.delete(reportId);
        }
   }

   areAllChecked(): boolean {
       let allChecked = true;
       this.reportCheckboxes.controls.forEach(cb => {
           if (cb.value === false) {
               allChecked = false;
           }
        });
       return allChecked;
   }

   download(data: EisTransactionAttachment) {
    this.eisDataService.downloadAttachment(data.id)
    .subscribe(file => {
        this.fileDownloadService.downloadFile(file, data.fileName);
        error => {
          console.error(error);
          this.toastr.error('', 'An error occurred while trying to download this report.');
        };
    });
  }

   private convertTypeAndCategory(): EisSubmissionStatus {

      let result: EisSubmissionStatus = null;

      const type = this.cboSubmitType.value;
      const category = this.cboSubmitCategory.value;

      if (category === DataCategoryType.FACILITY_INVENTORY) {

         if (type === SubmissionType.QA) {

            result = EisSubmissionStatus.QaFacility;

         } else if (type === SubmissionType.PRODUCTION) {

            result = EisSubmissionStatus.ProdFacility;
         }

      } else if (category === DataCategoryType.POINT_EMISSIONS) {

         if (type === SubmissionType.QA) {

            result = EisSubmissionStatus.QaEmissions;

         } else if (type === SubmissionType.PRODUCTION) {

            result = EisSubmissionStatus.ProdEmissions;
         }
      }

      return result;
   }
}


