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
import { MasterFacilityRecordService } from 'src/app/core/services/master-facility-record.service';
import { FormBuilder, Validators } from '@angular/forms';
import { MasterFacilityRecord } from 'src/app/shared/models/master-facility-record';
import { LookupService } from 'src/app/core/services/lookup.service';
import { FipsStateCode } from 'src/app/shared/models/fips-state-code';
import { BaseCodeLookup } from 'src/app/shared/models/base-code-lookup';
import { UserFacilityAssociationService } from 'src/app/core/services/user-facility-association.service';
import { UserFacilityAssociation } from 'src/app/shared/models/user-facility-association';

@Component({
  selector: 'app-master-facility-search',
  templateUrl: './master-facility-search.component.html',
  styleUrls: ['./master-facility-search.component.scss']
})
export class MasterFacilitySearchComponent implements OnInit {

  searchForm = this.fb.group({
    name: [''],
    city: [''],
    stateCode: [null],
    postalCode: [''],
    programSystemCode: [null, Validators.required],
    agencyFacilityIdentifier: ['']
  });

  searchResults: MasterFacilityRecord[];

  stateValues: FipsStateCode[];
  programSystemCodeValues: BaseCodeLookup[];

  myFacilityAssociations = new Map<number, UserFacilityAssociation>();

  constructor(private mfrService: MasterFacilityRecordService,
              private ufaService: UserFacilityAssociationService,
              private lookupService: LookupService,
              private fb: FormBuilder) { }

  ngOnInit(): void {

    this.ufaService.getMyAssociations()
    .subscribe(result => {
      result.forEach(a => {
        this.myFacilityAssociations.set(a.masterFacilityRecord.id, a);
      });
    });

    this.mfrService.getProgramSystemCodes()
    .subscribe(result => {
      this.programSystemCodeValues = result.sort((a, b) => (a.code > b.code) ? 1 : -1);
    });

    this.lookupService.retrieveFipsStateCode()
    .subscribe(result => {
      this.stateValues = result;
    });
  }

  onSubmit() {

    if (!this.searchForm.valid) {
      this.searchForm.markAllAsTouched();
    } else {

      const criteria = new MasterFacilityRecord();
      Object.assign(criteria, this.searchForm.value);

      this.mfrService.search(criteria)
      .subscribe(result => {
        // remove facilities already associated with
        this.searchResults = result;
        this.searchResults.forEach(f => {
          if (this.myFacilityAssociations.has(f.id)) {
            f.associationStatus = this.myFacilityAssociations.get(f.id).active ? 'ACTIVE' : 'PENDING';
          }
        });
      });
    }
  }

  onAccessRequested(ufa: UserFacilityAssociation) {

    this.myFacilityAssociations.set(ufa.masterFacilityRecord.id, ufa);
  }

}
