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
import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { SideNavItem } from 'src/app/shared/models/side-nav-item';
import { SharedService } from 'src/app/core/services/shared.service';

@Component({
  selector: 'app-collapse-nav',
  templateUrl: './collapse-nav.component.html',
  styleUrls: ['./collapse-nav.component.scss'],
})
export class CollapseNavComponent implements OnInit, OnDestroy {

  @Input() collapsed = false;
  @Input() desc: string;
  @Input() targetId: string;
  @Input() linkUrl: string;
  @Input() navItems: SideNavItem[];
  @Input() isUnits: boolean = false;
  @Input() facilityId: number;
  paginate: boolean;

  constructor(private sharedService: SharedService) {}

  ngOnInit() {}

  ngOnDestroy() {
    this.sharedService.clearSideNavCollapsedMap(); // resets upon browser refresh
  }
}
