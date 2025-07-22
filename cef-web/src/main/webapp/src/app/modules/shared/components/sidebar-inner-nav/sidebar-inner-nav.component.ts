/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
