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
import { ReportSummary } from 'src/app/shared/models/report-summary';
import { ReportHistory } from 'src/app/shared/models/report-history';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  private baseUrl = 'api/report';  // URL to web api

  constructor(private http: HttpClient) { }

  /** GET report summary data from server for specified year and facility */
  retrieve(reportId: number): Observable<ReportSummary[]> {
    const url = `${this.baseUrl}/emissionsSummary/report/${reportId}`;
    return this.http.get<ReportSummary[]>(url);
  }

  /** GET report history data from server for specified report and facility */
  retrieveHistory(reportId: number, facilitySiteId: number): Observable<ReportHistory[]> {
    const url = `${this.baseUrl}/reportHistory/report/${reportId}/facilitySiteId/${facilitySiteId}`;
    return this.http.get<ReportHistory[]>(url);
  }
    /** GET ReportDownloadDto for report id */
  retrieveReportDownloadDto(reportId: number, facilitySiteId: number): Observable<any[]> {
    const url = `${this.baseUrl}/downloadReport/reportId/${reportId}/facilitySiteId/${facilitySiteId}`;
    return this.http.get<any[]>(url);
  }
}
