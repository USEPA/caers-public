/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

import { Injectable } from '@angular/core';
import {Observable, Subject} from "rxjs";
import {MonthlyReportingPeriod} from "../../shared/enums/monthly-reporting-periods";

@Injectable({
  providedIn: 'root'
})
export class MonthlyReportingSyncService {

  // Observable string sources
  private emitChangeJanuaryEmissions = new Subject<any>();
  private emitChangeFebruaryEmissions = new Subject<any>();
  private emitChangeMarchEmissions = new Subject<any>();
  private emitChangeAprilEmissions = new Subject<any>();
  private emitChangeMayEmissions = new Subject<any>();
  private emitChangeJuneEmissions = new Subject<any>();
  private emitChangeJulyEmissions = new Subject<any>();
  private emitChangeAugustEmissions = new Subject<any>();
  private emitChangeSeptemberEmissions = new Subject<any>();
  private emitChangeOctoberEmissions = new Subject<any>();
  private emitChangeNovemberEmissions = new Subject<any>();
  private emitChangeDecemberEmissions = new Subject<any>();
  private emitChangeSemiannualEmissions = new Subject<any>();
  private emitChangeAnnualEmissions = new Subject<any>();

  private emitChangeSemiannualPeriod = new Subject<any>();
  private emitChangeAnnualPeriod = new Subject<any>();

  constructor() { }

  // emit based on the period
  emitEmissionsChange(change: any, month: string) {
      switch (month) {
          case MonthlyReportingPeriod.JANUARY:
              this.emitChangeJanuaryEmissions.next(change);
              break;
          case MonthlyReportingPeriod.FEBRUARY:
              this.emitChangeFebruaryEmissions.next(change);
              break;
          case MonthlyReportingPeriod.MARCH:
              this.emitChangeMarchEmissions.next(change);
              break;
          case MonthlyReportingPeriod.APRIL:
              this.emitChangeAprilEmissions.next(change);
              break;
          case MonthlyReportingPeriod.MAY:
              this.emitChangeMayEmissions.next(change);
              break;
          case MonthlyReportingPeriod.JUNE:
              this.emitChangeJuneEmissions.next(change);
              break;
          case MonthlyReportingPeriod.JULY:
              this.emitChangeJulyEmissions.next(change);
              break;
          case MonthlyReportingPeriod.AUGUST:
              this.emitChangeAugustEmissions.next(change);
              break;
          case MonthlyReportingPeriod.SEPTEMBER:
              this.emitChangeSeptemberEmissions.next(change);
              break;
          case MonthlyReportingPeriod.OCTOBER:
              this.emitChangeOctoberEmissions.next(change);
              break;
          case MonthlyReportingPeriod.NOVEMBER:
              this.emitChangeNovemberEmissions.next(change);
              break;
          case MonthlyReportingPeriod.DECEMBER:
              this.emitChangeDecemberEmissions.next(change);
              break;
          case MonthlyReportingPeriod.SEMIANNUAL:
              this.emitChangeSemiannualEmissions.next(change);
              break;
          case MonthlyReportingPeriod.ANNUAL:
              this.emitChangeAnnualEmissions.next(change);
              break;
          default:
              break;
      }
  }

  // get the observable for the respective period
  getEmissionsChangeObservable(month: string) : Observable<any> {
      switch (month) {
          case MonthlyReportingPeriod.JANUARY:
              return this.emitChangeJanuaryEmissions.asObservable();
          case MonthlyReportingPeriod.FEBRUARY:
              return this.emitChangeFebruaryEmissions.asObservable();
          case MonthlyReportingPeriod.MARCH:
              return this.emitChangeMarchEmissions.asObservable();
          case MonthlyReportingPeriod.APRIL:
              return this.emitChangeAprilEmissions.asObservable();
          case MonthlyReportingPeriod.MAY:
              return this.emitChangeMayEmissions.asObservable();
          case MonthlyReportingPeriod.JUNE:
              return this.emitChangeJuneEmissions.asObservable();
          case MonthlyReportingPeriod.JULY:
              return this.emitChangeJulyEmissions.asObservable();
          case MonthlyReportingPeriod.AUGUST:
              return this.emitChangeAugustEmissions.asObservable();
          case MonthlyReportingPeriod.SEPTEMBER:
              return this.emitChangeSeptemberEmissions.asObservable();
          case MonthlyReportingPeriod.OCTOBER:
              return this.emitChangeOctoberEmissions.asObservable();
          case MonthlyReportingPeriod.NOVEMBER:
              return this.emitChangeNovemberEmissions.asObservable();
          case MonthlyReportingPeriod.DECEMBER:
              return this.emitChangeDecemberEmissions.asObservable();
          case MonthlyReportingPeriod.SEMIANNUAL:
              return this.emitChangeSemiannualEmissions.asObservable();
          case MonthlyReportingPeriod.ANNUAL:
              return this.emitChangeAnnualEmissions.asObservable();
          default:
              return null;
      }
  }

  emitPeriodChange(change: any, month: string) {
      switch (month) {
          case MonthlyReportingPeriod.SEMIANNUAL:
              this.emitChangeSemiannualPeriod.next(change);
              break;
          case MonthlyReportingPeriod.ANNUAL:
              this.emitChangeAnnualPeriod.next(change);
              break;
          default:
              break;
      }
  }

  // get the observable for the respective period
  getPeriodChangeObservable(month: string) : Observable<any> {
      switch (month) {
          case MonthlyReportingPeriod.SEMIANNUAL:
              return this.emitChangeSemiannualPeriod.asObservable();
          case MonthlyReportingPeriod.ANNUAL:
              return this.emitChangeAnnualPeriod.asObservable();
          default:
              return null;
      }
  }
}
