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
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { EmissionUnit } from 'src/app/shared/models/emission-unit';
import { EmissionUnitService } from 'src/app/core/services/emission-unit.service';
import { ActivatedRoute } from '@angular/router';
import { SharedService } from 'src/app/core/services/shared.service';

@Component({
  selector: 'app-emissions-units-summary',
  templateUrl: './emissions-units-summary.component.html',
  styleUrls: ['./emissions-units-summary.component.scss']
})
export class EmissionsUnitsSummaryComponent implements OnInit {
  facilitySite: FacilitySite;
  emissionsUnits: EmissionUnit[];

  constructor(
    private emissionUnitsService: EmissionUnitService,
    private route: ActivatedRoute,
    private sharedService: SharedService) { }

  ngOnInit() {
    // get the resolved facilitySite
    this.route.data
      .subscribe((data: { facilitySite: FacilitySite }) => {

      this.facilitySite = data.facilitySite;
      this.sharedService.emitChange(data.facilitySite);

      this.emissionUnitsService.retrieveForFacility(this.facilitySite.id)
        .subscribe(units => {
          this.emissionsUnits = units;
      });
    });
  }

}
