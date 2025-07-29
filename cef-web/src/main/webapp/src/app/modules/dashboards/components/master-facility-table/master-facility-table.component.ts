/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
