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
import { Component, Input, OnInit } from '@angular/core';
import { SideNavItem } from 'src/app/shared/models/side-nav-item';
import { SharedService } from 'src/app/core/services/shared.service';
import { ActiveLinkInfo } from 'src/app/shared/models/active-link-info';

@Component({
  selector: 'app-sidebar-inner-nav-item',
  templateUrl: './sidebar-inner-nav-item.component.html',
  styleUrls: ['./sidebar-inner-nav-item.component.scss'],
})
export class SidebarInnerNavItemComponent implements OnInit {
  @Input() navItem: SideNavItem;
  @Input() paginatedGroupKey = '';
  @Input() baseUrl: string;
  itemUrl: string;
  targetId: string;
  activeEmissionLabel = '';
  activeParentProcessId: number;
  collapsedKey = '';
  navigationStartUrl = '';

  constructor(private sharedService: SharedService) {}

  ngOnInit() {
    this.itemUrl = `${this.baseUrl}${this.navItem.url}`;
    this.targetId = `${this.navItem.baseUrl}${this.navItem.id}children`;
    this.collapsedKey = this.paginatedGroupKey + this.itemUrl;
    if (this.navItem) {
      const cachedCollapse = this.sharedService.getSideNavCollapsedMapEntry(this.collapsedKey);
      if (cachedCollapse == undefined) {
        this.sharedService.setSideNavCollapsedMapEntry(this.collapsedKey, false);
      }
    }

    this.sharedService.activeLinkInfoEmitted$.subscribe((activeLinkInfo: ActiveLinkInfo) => {
      if (activeLinkInfo) {
        this.activeEmissionLabel = activeLinkInfo.label;
        this.activeParentProcessId = activeLinkInfo.parentId;
      } else {
        this.activeEmissionLabel = '';
        this.activeParentProcessId = null;
      }
    });
  }

  getCollapsedMapEntry(): boolean {
    return this.sharedService.getSideNavCollapsedMapEntry(this.collapsedKey);
  }

  toggleCollapsedMapEntry() {
    this.sharedService.setSideNavCollapsedMapEntry(
      this.collapsedKey,
      !this.sharedService.getSideNavCollapsedMapEntry(this.collapsedKey)
    );
  }

}
