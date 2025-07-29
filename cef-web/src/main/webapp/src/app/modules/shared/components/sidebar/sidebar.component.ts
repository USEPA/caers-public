/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { FacilitySite} from 'src/app/shared/models/facility-site';
import { Component, Input, OnInit } from '@angular/core';
import { Router, Event as NavigationEvent, NavigationEnd } from '@angular/router';
import { SideNavItem } from 'src/app/shared/models/side-nav-item';
import { SharedService } from 'src/app/core/services/shared.service';
import { BaseReportUrl } from 'src/app/shared/enums/base-report-url';
import { ConfigPropertyService } from 'src/app/core/services/config-property.service';
import { MasterFacilityRecord } from 'src/app/shared/models/master-facility-record';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
})
export class SidebarComponent implements OnInit {
  @Input() facility: MasterFacilityRecord;
  facilitySite: FacilitySite;
  facilityNavItems: SideNavItem[];
  paginate: boolean;
  hideSideBar: boolean = false;

  bulkEntryEnabled: boolean;
  monthlyReportingEnabled: boolean;
  isLoaded = false;

  constructor(
    private sharedService: SharedService,
    private propertyService: ConfigPropertyService,
    private router: Router
  ) {
    sharedService.changeEmitted$
    .subscribe(facilitySite => {
      if (facilitySite != null) {
        this.facilitySite = facilitySite;
        this.getFacilityInventoryNav();
        forkJoin({
          monthlyReportingEnabled: this.propertyService.retrieveSltMonthlyFuelReportingEnabled(this.facilitySite?.programSystemCode?.code),
          monthlyReportingInitialYear: this.propertyService.retrieveSltMonthlyFuelReportingInitialYear(this.facilitySite?.programSystemCode?.code)
        })
        .subscribe(({monthlyReportingEnabled, monthlyReportingInitialYear}) => {
          this.monthlyReportingEnabled = monthlyReportingEnabled && (monthlyReportingInitialYear == null || this.facilitySite?.emissionsReport?.year >= monthlyReportingInitialYear);
          this.isLoaded = true;
        });
      } else {
        this.facilityNavItems = null;
        this.facilitySite = null;
      }
    })

    this.router.events.subscribe((event: NavigationEvent) => {
      if (
        (event instanceof NavigationEnd)
        && !(this.router.url.includes('/report/' + this.facilitySite?.emissionsReport?.id))
      ) {
        // If report switched or not in any report, clear collapsed cache
        this.sharedService.clearSideNavCollapsedMap();
      }
    })
  }

  ngOnInit() {
    this.sharedService.hideBoolChangeEmitted$.subscribe((result) => {
      this.hideSideBar = result;
    });

    this.propertyService.retrieveBulkEntryEnabled()
    .subscribe(result => {
      this.bulkEntryEnabled = result;
    });
  }

  getFacilityInventoryNav() {
    const items = [];
    items.push(new SideNavItem(null, 'Facility Information','', BaseReportUrl.FACILITY_INFO, null));
    items.push(new SideNavItem(null, 'Emissions Units', '', BaseReportUrl.EMISSIONS_UNIT, null));
    items.push(new SideNavItem(null, 'Release Points', '', BaseReportUrl.RELEASE_POINT, null));
    items.push(new SideNavItem(null, 'Control Devices', '', BaseReportUrl.CONTROL_DEVICE, null));
    items.push(new SideNavItem(null, 'Control Paths', '', BaseReportUrl.CONTROL_PATH, null));
    this.facilityNavItems = items;
  }

}
