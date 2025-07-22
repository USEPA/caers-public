/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
