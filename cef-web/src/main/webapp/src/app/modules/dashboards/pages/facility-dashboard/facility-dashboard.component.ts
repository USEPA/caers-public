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
import { UserContextService } from 'src/app/core/services/user-context.service';
import { MasterFacilityRecord } from 'src/app/shared/models/master-facility-record';
import { UserFacilityAssociationService } from 'src/app/core/services/user-facility-association.service';
import { SharedService } from "src/app/core/services/shared.service";

@Component({
  selector: 'app-facility-dashboard',
  templateUrl: './facility-dashboard.component.html',
  styleUrls: ['./facility-dashboard.component.scss']
})
export class FacilityDashboardComponent implements OnInit {
  facilities: MasterFacilityRecord[] = [];

  constructor( private ufaService: UserFacilityAssociationService,
               private userContext: UserContextService,
               private sharedService: SharedService) { }

  ngOnInit() {
    this.getFacilities();
    //console.log(this);
    this.sharedService.userFacilityAssociationChangeEmitted$.subscribe(userFacilityAssociations => {
      this.facilities = userFacilityAssociations.filter(a => a.active)
                                                .map(a => a.masterFacilityRecord)
                                                .sort((a, b) => (a.name > b.name) ? 1 : -1);
    });
  }

  getFacilities(): void {
    this.ufaService.getMyAssociations()
    .subscribe(associations => {
      this.facilities = associations.filter(a => a.active)
                                    .map(a => a.masterFacilityRecord)
                                    .sort((a, b) => (a.name > b.name) ? 1 : -1);
    });
  }

}
