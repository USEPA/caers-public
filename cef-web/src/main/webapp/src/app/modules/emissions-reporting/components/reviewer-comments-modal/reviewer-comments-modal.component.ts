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
import {ReviewerComment} from "../../../../shared/models/reviewer-comment";

@Component({
  selector: 'app-reviewer-comments-modal',
  templateUrl: './reviewer-comments-modal.component.html',
  styleUrls: ['./reviewer-comments-modal.component.scss']
})
export class ReviewerCommentsModalComponent implements OnInit {
    @Input() reviewerComments: ReviewerComment[];

  constructor(public activeModal: NgbActiveModal) { }

  ngOnInit(): void {
  }

    onClose() {
        this.activeModal.dismiss();
    }

}
