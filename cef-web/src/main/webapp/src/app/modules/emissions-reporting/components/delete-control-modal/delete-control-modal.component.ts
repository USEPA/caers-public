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

import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { ControlAssignment } from "src/app/shared/models/control-assignment";
import { ReleasePoint } from "src/app/shared/models/release-point";

@Component({
  selector: 'app-delete-control-modal',
  templateUrl: './delete-control-modal.component.html',
  styleUrls: ['./delete-control-modal.component.scss']
})

export class DeleteControlModalComponent implements OnInit {

	@Input() title = 'Confirm';
	@Input() controlIdentifier: string;
	@Input() controlPathList: ControlAssignment[];
	@Input() controlPathIdentifier: string;
	@Input() releasePointList: ReleasePoint[];
	@Input() cancelButtonText = 'Cancel';
	@Input() confirmButtonText = 'Confirm';
	@Output() continue: EventEmitter<any> = new EventEmitter();
	
    constructor(public activeModal: NgbActiveModal,
				) { }

	ngOnInit() {
    }

	onContinue() {
    this.continue.emit(null);
    this.activeModal.close(true);
  }

  onCancel() {
    this.activeModal.close(false);
  }

  onClose() {
    this.activeModal.close(false);
  }
}
