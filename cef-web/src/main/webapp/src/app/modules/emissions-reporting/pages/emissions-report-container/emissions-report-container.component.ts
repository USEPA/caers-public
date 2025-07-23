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
import { Component, OnInit } from '@angular/core';
import { UserContextService } from 'src/app/core/services/user-context.service';

@Component({
  selector: 'app-emissions-report-container',
  templateUrl: './emissions-report-container.component.html',
  styleUrls: ['./emissions-report-container.component.scss']
})
export class EmissionsReportContainerComponent implements OnInit {
  showProgressBar = false;

  constructor(private userContext: UserContextService) { }

  ngOnInit() {
    if (this.userContext.user.canPrepare()) {
      this.showProgressBar = true;
    }
  }

}
