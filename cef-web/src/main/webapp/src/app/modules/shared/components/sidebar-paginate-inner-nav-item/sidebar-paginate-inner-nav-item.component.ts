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
import { SideNavItem } from 'src/app/shared/models/side-nav-item';
import { SharedService } from 'src/app/core/services/shared.service';

@Component({
  selector: 'app-sidebar-paginate-inner-nav-item',
  templateUrl: './sidebar-paginate-inner-nav-item.component.html',
  styleUrls: ['./sidebar-paginate-inner-nav-item.component.scss']
})
export class SidebarPaginateInnerNavItemComponent implements OnInit {
  @Input() group: number;
  @Input() navItems: SideNavItem[];
  @Input() baseUrl = '';
  @Input() paginate: boolean;
  paginatedGroupKey = '';
  collapsedKey = ''
  groupMin: number = 0;
  groupRange = '';

  constructor(private sharedService: SharedService) { }

  ngOnInit() {
    this.groupMin = this.group - 24;
    this.groupRange = this.groupMin + '-' + this.group;
    this.paginatedGroupKey = this.groupRange + '_';
    if (this.baseUrl) {
      this.collapsedKey = this.paginatedGroupKey + this.baseUrl;
      const cachedCollapse = this.sharedService.getSideNavCollapsedMapEntry(this.collapsedKey);
      if (cachedCollapse == undefined) {
        this.sharedService.setSideNavCollapsedMapEntry(this.collapsedKey, false);
      }
    }
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
