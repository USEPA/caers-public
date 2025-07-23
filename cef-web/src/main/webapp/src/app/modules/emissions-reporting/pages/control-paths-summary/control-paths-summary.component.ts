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
import { ActivatedRoute } from '@angular/router';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { SharedService } from 'src/app/core/services/shared.service';
import { ControlPathService } from 'src/app/core/services/control-path.service';
import { ControlPath } from 'src/app/shared/models/control-path';

@Component({
  selector: 'app-control-paths-summary',
  templateUrl: './control-paths-summary.component.html',
  styleUrls: ['./control-paths-summary.component.scss']
})
export class ControlPathsSummaryComponent implements OnInit {
  facilitySite: FacilitySite;
  controlPaths: ControlPath[];

  constructor(private route: ActivatedRoute,
              private sharedService: SharedService,
              private controlPathService: ControlPathService) { }

  ngOnInit() {
    this.route.data
    .subscribe((data: { facilitySite: FacilitySite }) => {
      this.facilitySite = data.facilitySite;
      this.sharedService.emitChange(data.facilitySite);
      this.controlPathService.retrieveForFacilitySite(this.facilitySite.id)
      .subscribe(controls => {
        this.controlPaths = controls;
      });
    });
  }

}
