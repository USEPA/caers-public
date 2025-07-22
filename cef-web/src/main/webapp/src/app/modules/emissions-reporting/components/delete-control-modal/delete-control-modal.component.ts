/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
