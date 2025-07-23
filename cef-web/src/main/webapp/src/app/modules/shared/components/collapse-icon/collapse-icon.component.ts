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

@Component({
  selector: 'app-collapse-icon',
  templateUrl: './collapse-icon.component.html',
  styleUrls: ['./collapse-icon.component.scss']
})
export class CollapseIconComponent implements OnInit {
  @Input() collapsed: boolean;
  @Input() collapsedIcon = 'caret-right';
  @Input() expandedIcon = 'caret-down';

  constructor() { }

  ngOnInit() {
  }

}
