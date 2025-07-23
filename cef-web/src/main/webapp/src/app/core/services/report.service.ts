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
