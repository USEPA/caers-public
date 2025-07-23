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
import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-timeout-modal',
  templateUrl: './timeout-modal.component.html',
  styleUrls: ['./timeout-modal.component.scss']
})
export class TimeoutModalComponent implements OnInit {
  @Input() timeout: number;
  timeLeft: number;
  timeLeftText: string;
  timer = null;

  constructor(public activeModal: NgbActiveModal) { }

  ngOnInit() {
    this.timeLeft = Math.floor(this.timeout / 1000);

    this.startCountdownTimer();
  }

  onClose() {
    this.activeModal.dismiss();
  }

  onSubmit() {
    this.activeModal.close();
  }

  startCountdownTimer() {

    // Clear countdown timer
    clearTimeout(this.timer);

    // Set countdown message time value
    const secondsLeft = this.timeLeft >= 0 ? this.timeLeft : 0;

    const minLeft = Math.floor(secondsLeft / 60);
    const secRemain = secondsLeft % 60;
    let countTxt = minLeft > 0 ? minLeft + 'm' : '';
    if (countTxt.length > 0) {
      countTxt += ' ';
    }
    countTxt += secRemain.toFixed(0) + 's';

    this.timeLeftText = countTxt;

    // Countdown by one second
    this.timeLeft = this.timeLeft - 1;
    this.timer = setTimeout(() => {

      // Call self after one second
      this.startCountdownTimer();

    }, 1000);
  }

}
