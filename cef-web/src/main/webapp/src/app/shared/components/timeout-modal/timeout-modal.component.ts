/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
