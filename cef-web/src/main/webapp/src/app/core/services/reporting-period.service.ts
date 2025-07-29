/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReportingPeriod } from 'src/app/shared/models/reporting-period';
import { ReportingPeriodUpdateResult } from 'src/app/shared/models/reporting-period-update-result';
import { BulkEntryReportingPeriod } from 'src/app/shared/models/bulk-entry-reporting-period';
import { BulkEntryEmissionHolder } from 'src/app/shared/models/bulk-entry-emission-holder';
import { MonthlyReportingPeriodHolder } from "src/app/shared/models/monthly-reporting-period-holder";
import { MonthlyReportingEmissionHolder } from "src/app/shared/models/monthly-reporting-emission-holder";

@Injectable({
  providedIn: 'root'
})
export class ReportingPeriodService {

  private baseUrl = 'api/reportingPeriod';  // URL to web api

  constructor(private http: HttpClient) { }

  create(period: ReportingPeriod): Observable<ReportingPeriod> {
    const url = `${this.baseUrl}`;
    return this.http.post<ReportingPeriod>(url, period);
  }

  update(period: ReportingPeriod): Observable<ReportingPeriodUpdateResult> {
    const url = `${this.baseUrl}/${period.id}`;
    return this.http.put<ReportingPeriodUpdateResult>(url, period);
  }

  /** GET specified Reporting Period from the server */
  retrieve(id: number): Observable<ReportingPeriod> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.get<ReportingPeriod>(url);
  }

  retrieveForEmissionsProcess(processId: number): Observable<ReportingPeriod[]> {
    const url = `${this.baseUrl}/process/${processId}`;
    return this.http.get<ReportingPeriod[]>(url);
  }

    retrieveAnnualForEmissionsProcess(processId: number): Observable<ReportingPeriod[]> {
        const url = `${this.baseUrl}/process/${processId}/annual`;
        return this.http.get<ReportingPeriod[]>(url);
    }

  retrieveForBulkEntry(facilitySiteId: number): Observable<BulkEntryReportingPeriod[]> {
    const url = `${this.baseUrl}/bulkEntry/${facilitySiteId}`;
    return this.http.get<BulkEntryReportingPeriod[]>(url);
  }

  bulkUpdate(facilitySiteId: number, periods: BulkEntryReportingPeriod[]): Observable<BulkEntryEmissionHolder[]> {
    const url = `${this.baseUrl}/bulkEntry/${facilitySiteId}`;
    return this.http.put<BulkEntryEmissionHolder[]>(url, periods);
  }

  /** Get Monthly Reporting Emissions Processes Data by facilitySiteId and month */
  retrieveMonthlyReportingPeriodData(facilitySiteId: number, period: string): Observable<MonthlyReportingPeriodHolder[]> {
    const url = `${this.baseUrl}/monthlyReportingPeriods/${facilitySiteId}/${period}`;
    return this.http.get<MonthlyReportingPeriodHolder[]>(url);
  }

  monthlyUpdate(facilitySiteId: number, periods: MonthlyReportingPeriodHolder[], period: string): Observable<MonthlyReportingEmissionHolder[]> {
    const url = `${this.baseUrl}/monthlyReportingPeriods/${facilitySiteId}/${period}`;
    return this.http.put<MonthlyReportingEmissionHolder[]>(url, periods);
  }

}
