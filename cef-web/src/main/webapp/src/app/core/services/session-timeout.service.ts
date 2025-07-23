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
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UserService } from 'src/app/core/services/user.service';
import { TimeoutModalComponent } from 'src/app/shared/components/timeout-modal/timeout-modal.component';

@Injectable({
  providedIn: 'root'
})
export class SessionTimeoutService {
  timer = null;
  dialogTimer = null;
  showingDialog = false;
  keepAlivePinged = false;

  options = {
    warnAfter: 900000, // 15 minutes
    redirAfter: 1200000, // 20 minutes
    keepAliveInterval: 60000 // 1 minute
  };

  constructor(private modalService: NgbModal, private userService: UserService) { }

  keepAlive() {

    if (this.keepAlivePinged === false) {

      this.keepAlivePinged = true;

      // Make a rest call to keep the spring session active
      this.userService.getCurrentUser();

      setTimeout(() => {

        this.keepAlivePinged = false;

      }, this.options.keepAliveInterval);
    }
  }

  showTimeoutDialog() {

    if (this.showingDialog === false) {

      this.showingDialog = true;

      clearTimeout(this.timer);

      clearTimeout(this.dialogTimer);

      const timeRemaining = (this.options.redirAfter - this.options.warnAfter);

      // Log user out if modal is not interacted with
      this.dialogTimer = setTimeout(() => {

          this.userService.logoutUser();

      }, timeRemaining);

      const modalRef = this.modalService.open(TimeoutModalComponent, { size: 'lg', backdrop: 'static' });
      modalRef.componentInstance.timeout = timeRemaining;
      // Start timer again if they confirm they are still here
      modalRef.result.then((value: string) => {

        clearTimeout(this.dialogTimer);
        this.startSessionTimer();
        this.showingDialog = false;

      }, () => {

        clearTimeout(this.dialogTimer);
        this.userService.logoutUser();

        this.showingDialog = false;
      });
    }
  }

  startSessionTimer() {

    // Clear session timer
    clearTimeout(this.timer);

    this.keepAlive();

    // Set session timer
    this.timer = setTimeout(() => {

      this.showTimeoutDialog();

    }, this.options.warnAfter);
  }

  stopSessionTimer() {

    clearTimeout(this.timer);
    clearTimeout(this.dialogTimer);
  }

  refreshSessionTimer() {
    if (this.showingDialog === false) {
      this.startSessionTimer();
    }
  }
}
