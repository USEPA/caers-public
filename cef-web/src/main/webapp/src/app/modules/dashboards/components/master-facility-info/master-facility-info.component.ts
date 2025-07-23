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
import { Component, OnInit, Input, OnChanges } from '@angular/core';
import { MasterFacilityRecord } from 'src/app/shared/models/master-facility-record';
import { UserFacilityAssociationService } from 'src/app/core/services/user-facility-association.service';
import { UserFacilityAssociation } from 'src/app/shared/models/user-facility-association';

@Component({
  selector: 'app-master-facility-info',
  templateUrl: './master-facility-info.component.html',
  styleUrls: ['./master-facility-info.component.scss']
})
export class MasterFacilityInfoComponent implements OnInit, OnChanges {
  @Input() facility: MasterFacilityRecord;
  userFacilityAssociations: UserFacilityAssociation[];

  constructor(private userFacilityAssociationService: UserFacilityAssociationService) { }

  ngOnInit(): void {
  }

  ngOnChanges() {

    this.userFacilityAssociationService.getApprovedAssociationDetailsForFacility(this.facility.id)
    .subscribe(result => {
      this.userFacilityAssociations = result;
    });

  }

}
