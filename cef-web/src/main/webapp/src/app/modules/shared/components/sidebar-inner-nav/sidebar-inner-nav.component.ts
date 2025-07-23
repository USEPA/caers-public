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
import { EmissionUnitService } from 'src/app/core/services/emission-unit.service';
import { SharedService } from 'src/app/core/services/shared.service';
import { SideNavItem } from 'src/app/shared/models/side-nav-item';

@Component({
  selector: 'app-sidebar-inner-nav',
  templateUrl: './sidebar-inner-nav.component.html',
  styleUrls: ['./sidebar-inner-nav.component.scss'],
})
export class SidebarInnerNavComponent implements OnInit {
  @Input() navItems: SideNavItem[];
  @Input() baseUrl = '';
  paginate: boolean;
  @Input() isUnits: boolean = false;
  @Input() facilityId: number;
  totalGroups: number[] = [];

  constructor(
    private emissionsUnitService: EmissionUnitService,
    private sharedService: SharedService
  ) {}

  ngOnInit() {
    this.sharedService.changeEmitted$
      .subscribe(facilitySite => {
      if (facilitySite) {
        this.getReportNav(facilitySite?.id);
      }
    });
    if (this.facilityId) { // Needed when report is entered
       this.getReportNav(this.facilityId);
    }
    this.sharedService.sideNavItemChangeNoRouteChangeEmitted$
      .subscribe(facilitySiteId => {
        this.getReportNav(facilitySiteId);
      })
  }

  getReportNav(facilityId: number) {
	  if (this.isUnits) {
		  this.emissionsUnitService.retrieveReportNavTree(facilityId)
	      .subscribe(navItems => {
          this.navItems = navItems;
          if(this.navItems.length < 25){
            this.paginate = false;
          }
          else{
            this.paginate = true;
          }
				  this.updateUnitGroups();
			  });
		}
  }

  updateUnitGroups() {
    if (this.paginate) {
      const lastGroup = this.totalGroups[this.totalGroups?.length - 1] ? this.totalGroups[this.totalGroups?.length - 1] : 0;
      if (this.totalGroups?.length > 0) {
        for (let i = lastGroup; i < this.navItems.length;) {
          if (i < (Math.ceil(this.navItems.length / 25) * 25)) {
            i = i + 25;
            this.totalGroups.push(i);
          }
        }
        for (let i = lastGroup; i > (Math.ceil(this.navItems.length / 25) * 25);) {
          this.totalGroups.splice(this.totalGroups.length - 1, 1)
          i = this.totalGroups[this.totalGroups.length - 1];
        }
      } else {
        for (let i = 0; i < this.navItems.length;) {
          i = i + 25;
          this.totalGroups.push(i);
        }
      }
    }
  }
}
