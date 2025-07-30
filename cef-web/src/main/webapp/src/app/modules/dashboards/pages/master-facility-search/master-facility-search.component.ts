/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
