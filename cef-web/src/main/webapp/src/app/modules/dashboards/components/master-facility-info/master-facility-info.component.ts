/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
