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
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { MasterFacilityRecord } from 'src/app/shared/models/master-facility-record';
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-master-facility-table',
  templateUrl: './master-facility-table.component.html',
  styleUrls: ['./master-facility-table.component.scss']
})
export class MasterFacilityTableComponent extends BaseSortableTable implements OnInit {
  @Input() tableData: MasterFacilityRecord[];
  @Output() facilitySelected = new EventEmitter<MasterFacilityRecord>();

  filteredItems: MasterFacilityRecord[] = [];
  filter = new FormControl('');

  selectedFacility: MasterFacilityRecord;

  matchFunction: (item: any, searchTerm: any) => boolean = this.matches;

  constructor() {
    super();

    this.filter.valueChanges.subscribe((text) => {
      this.controller.searchTerm = text;
    });
  }

  ngOnInit(): void {
    this.controller.paginate = true;
  }

  selectFacility(facility: MasterFacilityRecord) {

    this.selectedFacility = facility;
    this.facilitySelected.emit(this.selectedFacility);
  }

  onClearFilterClick() {
      this.filter.setValue('');
   }

  matches(item: MasterFacilityRecord, searchTerm: string): boolean {
    const term = searchTerm.toLowerCase();
    return item.name?.toLowerCase().includes(term)
        || item.agencyFacilityIdentifier?.toLowerCase().includes(term);
  }

}
