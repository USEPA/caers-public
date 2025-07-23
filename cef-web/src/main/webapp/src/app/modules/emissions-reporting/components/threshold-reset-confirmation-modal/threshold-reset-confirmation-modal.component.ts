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
import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-threshold-reset-confirmation-modal',
  templateUrl: './threshold-reset-confirmation-modal.component.html',
  styleUrls: ['./threshold-reset-confirmation-modal.component.scss']
})
export class ThresholdResetConfirmationModalComponent implements OnInit {

    @Input() year: number;
    @Input() thresholdStatus: string;

    constructor(public activeModal: NgbActiveModal) { }

    ngOnInit(): void {
    }

    // option for when user wants to change the report opt in/out status
    onSelectYes() {
        this.activeModal.close(true);
    }

    // option for when user wants to keep the previous opt in/out status
    onSelectNo() {
        this.activeModal.close(false);
    }

}
