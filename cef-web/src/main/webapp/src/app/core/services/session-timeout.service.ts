/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
