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
import {Component, OnInit} from '@angular/core';
import {ConfigPropertyService} from 'src/app/core/services/config-property.service';
import {UserContextService} from "src/app/core/services/user-context.service";
import {User} from "src/app/shared/models/user";

@Component({
  selector: 'app-horizontal-nav',
  templateUrl: './horizontal-nav.component.html',
  styleUrls: ['./horizontal-nav.component.scss']
})
export class HorizontalNavComponent implements OnInit {

  active = 1;
  isCollapsed = true;

  user: User;
  monthlyReportingEnabled = false;

  constructor(
	private userContext: UserContextService,
	private propertyService: ConfigPropertyService) { }

  ngOnInit(): void {

    this.userContext.getUser()
       .subscribe(result => {
          this.user = result;
		  
		  if (this.user?.isReviewer()) {
			  this.propertyService.retrieveSltMonthlyFuelReportingEnabled(this.user.programSystemCode)
			    .subscribe(result => {
			      this.monthlyReportingEnabled = result;
			    });
		  }

       });
  }

}
