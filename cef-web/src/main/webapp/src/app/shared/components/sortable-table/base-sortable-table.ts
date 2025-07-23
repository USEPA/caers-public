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
import { SortableHeaderDirective, SortEvent } from 'src/app/shared/directives/sortable.directive';
import { ViewChildren, QueryList, Directive, AfterViewInit, DoCheck, AfterContentInit } from '@angular/core';
import { TableController } from 'src/app/shared/utils/table-controller';

@Directive()
export abstract class BaseSortableTable implements DoCheck, AfterContentInit, AfterViewInit {

  controller = new TableController<any>();

  tableData: any[];
  tableItems: any[];
  @ViewChildren(SortableHeaderDirective) headers: QueryList<SortableHeaderDirective>;

  // override to enable filtering
  matchFunction: (item: any, searchTerm: any) => boolean;

  ngDoCheck() {
    // check if tableData has changed to update the controller
    const check = this.controller.tableData === this.tableData;
    if (!check) {
      this.resortTable();
    }
  }

  ngAfterContentInit() {
    // initiate table
    this.initTable();
  }

  ngAfterViewInit() {
    // set headers after they've loaded
    this.controller.headers = this.headers;
  }

  initTable() {
    this.controller.tableData = this.tableData;
    if (this.matchFunction) {
      this.controller.matchFunction = this.matchFunction;
    }
    this.controller.init();
    this.controller.items$.subscribe(items => {
      this.tableItems = items;
    });
  }

  onSort({column, direction}: SortEvent) {

    this.controller.onSort({column, direction});
  }

  resortTable() {

    this.controller.tableData = this.tableData;
    this.controller.refreshTable();
  }

}
