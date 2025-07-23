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
import { QueryList } from '@angular/core';
import { BehaviorSubject, Observable, of, Subject } from 'rxjs';
import { debounceTime, delay, switchMap, tap } from 'rxjs/operators';
import { SortableHeaderDirective, compare, SortEvent, SortDirection } from 'src/app/shared/directives/sortable.directive';

type SortColumn = string | '';

interface SearchResult<T> {
  items: T[];
  total: number;
  min: number;
  max: number;
}

interface State {
  page: number;
  pageSize: number;
  paginate: boolean;
  searchTerm: string;
  sortColumn: SortColumn;
  sortDirection: SortDirection;
  headers: QueryList<SortableHeaderDirective>;
}

export class TableController<T> {

  private _search$ = new Subject<void>();
  private _items$ = new BehaviorSubject<T[]>([]);
  private _total$ = new BehaviorSubject<number>(0);
  private _min$ = new BehaviorSubject<number>(0);
  private _max$ = new BehaviorSubject<number>(0);

  public tableData: T[];
  public matchFunction: (item: T, searchTerm: any) => boolean;

  private _state: State = {
    page: 1,
    pageSize: 10,
    paginate: false,
    searchTerm: '',
    sortColumn: '',
    sortDirection: '',
    headers: new QueryList<SortableHeaderDirective>()
  };

  get items$() { return this._items$.asObservable(); }
  get total$() { return this._total$.asObservable(); }
  get min$() { return this._min$.asObservable(); }
  get max$() { return this._max$.asObservable(); }

  get page() { return this._state.page; }
  get pageSize() { return this._state.pageSize; }
  get searchTerm() { return this._state.searchTerm; }

  get headers() {return this._state.headers;}

  set page(page: number) { this._set({page}); }
  set pageSize(pageSize: number) { this._set({pageSize}); }
  set paginate(paginate: boolean) { this._set({paginate}); }
  set searchTerm(searchTerm: any) { this._set({searchTerm}); }
  set sortColumn(sortColumn: SortColumn) { this._set({sortColumn}); }
  set sortDirection(sortDirection: SortDirection) { this._set({sortDirection}); }

  set headers(headers: QueryList<SortableHeaderDirective>) {this._state.headers = headers;}

  public init() {
    this._search$.pipe(
      switchMap(() => this._search()),
    ).subscribe(result => {
      this._populateTable(result);
    });
    this._search$.next();
  }

  public onSort({column, direction}: SortEvent) {

    // resetting other headers
    this.headers.forEach(header => {
      if (header.sortable !== column) {
        header.direction = '';
      }
    });

    this.sortColumn = column;
    this.sortDirection = direction;
  }

  private _populateTable(result: SearchResult<T>) {
    this._items$.next(result.items);
    this._total$.next(result.total);
    this._min$.next(result.min);
    this._max$.next(result.max);
  }

  private _set(patch: Partial<State>) {
    Object.assign(this._state, patch);
    this._search$.next();
  }

  private sort(items: T[], column: SortColumn, direction: string): T[] {
    if (direction === '' || column === '') {
      return items;
    } else {
      return [...items].sort((a, b) => {
        let aVal = a;
        let bVal = b;
        // flatten out nested references
        for (const colName of column.split('.')) {
          aVal = aVal[colName];
          bVal = bVal[colName];
        }
        const res = compare(aVal, bVal);
        return direction === 'asc' ? res : -res;
      });
    }
  }

  private _search(): Observable<SearchResult<T>> {
    const {sortColumn, sortDirection, pageSize, page, paginate, searchTerm} = this._state;

    // 1. sort
    let items = this.sort(this.tableData || [], sortColumn, sortDirection);

    // 2. filter
    if (this.matchFunction) {
      items = items.filter(item => this.matchFunction(item, searchTerm));
    }
    const total = items.length;

    // 3. paginate
    let min: number;
    let max: number;
    if (paginate) {
      const minItem = (page - 1) * pageSize;
      const maxItem = minItem + pageSize;
      min = minItem + 1;
      max = (maxItem > total) ? total : maxItem;
      items = items.slice(minItem, maxItem);
    }

    return of({items, total, min, max});
  }

  public refreshTable(): void {
    this._search$.next();
  }

  public resetSort(): void {
    this.headers.forEach(header => {
        header.direction = '';
    });
  }
}
