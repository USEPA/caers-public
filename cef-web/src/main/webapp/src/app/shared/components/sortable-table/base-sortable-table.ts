/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
