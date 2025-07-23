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
import { Emission } from 'src/app/shared/models/emission';
import { Observable } from 'rxjs';
import { BulkEntryEmissionHolder } from 'src/app/shared/models/bulk-entry-emission-holder';
import { MonthlyReportingEmissionHolder } from 'src/app/shared/models/monthly-reporting-emission-holder';
import { MonthlyReportingDownload } from 'src/app/shared/models/monthly-reporting-download';

@Injectable({
  providedIn: 'root'
})
export class EmissionService {

  private baseUrl = 'api/emission';  // URL to web api

  constructor(private http: HttpClient) { }

  create(emission: Emission): Observable<Emission> {
    const url = `${this.baseUrl}`;
    return this.http.post<Emission>(url, emission);
  }

  retrieve(id: number): Observable<Emission> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.get<Emission>(url);
  }

  retrieveWithVariables(id: number): Observable<Emission> {
    const url = `${this.baseUrl}/${id}/variables`;
    return this.http.get<Emission>(url);
  }

  update(emission: Emission): Observable<Emission> {
    const url = `${this.baseUrl}/${emission.id}`;
    return this.http.put<Emission>(url, emission);
  }

    /** Delete specified emissions process from the database */
  delete(id: number): Observable<{}> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.delete(url);
  }

  calculateEmissionTotal(emission: Emission): Observable<Emission> {
    const url = `${this.baseUrl}/calculate`;
    return this.http.post<Emission>(url, emission);
  }

  calculateEmissionFactor(emission: Emission): Observable<Emission> {
      const url = `${this.baseUrl}/calculateFactor`;
      return this.http.post<Emission>(url, emission);
  }

  retrieveForBulkEntry(facilitySiteId: number): Observable<BulkEntryEmissionHolder[]> {
    const url = `${this.baseUrl}/bulkEntry/${facilitySiteId}`;
    return this.http.get<BulkEntryEmissionHolder[]>(url);
  }

  retrieveForMonthlyReporting(facilitySiteId: number, period: string): Observable<MonthlyReportingEmissionHolder[]> {
    const url = `${this.baseUrl}/monthlyEmissions/${facilitySiteId}/${period}`;
    return this.http.get<MonthlyReportingEmissionHolder[]>(url);
  }

  bulkUpdate(facilitySiteId: number, emissions: Emission[]): Observable<BulkEntryEmissionHolder[]> {
    const url = `${this.baseUrl}/bulkEntry/${facilitySiteId}`;
    return this.http.put<BulkEntryEmissionHolder[]>(url, emissions);
  }

  monthlyUpdate(facilitySiteId: number, period: string, emissions: Emission[]): Observable<MonthlyReportingEmissionHolder[]> {
    const url = `${this.baseUrl}/monthlyEmissions/${facilitySiteId}/${period}`;
    return this.http.put<MonthlyReportingEmissionHolder[]>(url, emissions);
  }

  retrieveEmissionsCreatedAfterSemiannualSubmission(processId: number, reportId: number): Observable<string[]> {
    const url = `${this.baseUrl}/createdAfterSemiannualSubmission/${processId}/${reportId}`;
    return this.http.get<string[]>(url);
  }

  retrieveMonthlyReportingDownloadDtoList(reportId: number, isSemiAnnual: boolean) {
    const url = `${this.baseUrl}/downloadMonthlyEmissions/${reportId}/${isSemiAnnual}`;
    return this.http.get<MonthlyReportingDownload[]>(url);
  }
}
