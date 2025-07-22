/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
