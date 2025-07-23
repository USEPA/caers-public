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
import { ReportStatus } from 'src/app/shared/enums/report-status';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { User } from 'src/app/shared/models/user';

@Injectable({
  providedIn: 'root'
})
export class UtilityService {

  constructor(
    private modalService: NgbModal,
  ) { }

  static isInProgressStatus(status: string): boolean {
    return ReportStatus.IN_PROGRESS === status || ReportStatus.RETURNED === status;
  }

  static isNotReadOnlyMode(user: User, status: string) {
    return user.canPrepare() && this.isInProgressStatus(status);
  }

  canDeactivateModal() {
    const modalMessage = 'There are unsaved edits on the screen. Leaving without saving will discard any changes. Are you sure you want to continue?';
    const modalRef = this.modalService.open(ConfirmationDialogComponent, {backdrop: 'static'});
    modalRef.componentInstance.message = modalMessage;
    modalRef.componentInstance.title = 'Unsaved Changes';
    modalRef.componentInstance.confirmButtonText = 'Confirm';
    return modalRef.result;
  }


  static removeSpecialCharacters(input: string): string {
    var regExpr = /[^a-zA-Z0-9-._()]/g;
    const output: string = input.replace(regExpr, "");
    return output;
  }

}
