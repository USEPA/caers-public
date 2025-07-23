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
