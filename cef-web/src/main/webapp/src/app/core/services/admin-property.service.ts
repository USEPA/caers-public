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
import { AppProperty } from 'src/app/shared/models/app-property';
import { Emission } from 'src/app/shared/models/emission';
import { ReportingPeriod } from 'src/app/shared/models/reporting-period';

@Injectable({
  providedIn: 'root'
})
export class AdminPropertyService {

  private baseUrl = 'api/admin/property';  // URL to web api

  constructor(private http: HttpClient) { }

  retrieve(name: string): Observable<AppProperty> {
    const url = `${this.baseUrl}/${name}`;
    return this.http.get<AppProperty>(url);
  }

  retrieveAll(): Observable<AppProperty[]> {
    return this.http.get<AppProperty[]>(this.baseUrl);
  }

  update(prop: AppProperty): Observable<AppProperty> {
    const url = `${this.baseUrl}/${prop.name}`;
    return this.http.put<AppProperty>(url, prop);
  }

  bulkUpdate(props: AppProperty[]): Observable<AppProperty[]> {
    const url = `${this.baseUrl}`;
    return this.http.post<AppProperty[]>(url, props);
  }

  sendTestEmail(): Observable<{}> {
    const url = `${this.baseUrl}/sendTestEmail`;
    return this.http.post<any>(url, {});
  }

  recalculateEmissionTotalTons(reportId: number): Observable<Emission[]> {
    const url = `${this.baseUrl}/emission/recalculate/${reportId}`;
    return this.http.post<Emission[]>(url, {});
  }

  calculateNonPointStandardizedFuelUse(): Observable<ReportingPeriod[]> {
    const url = `${this.baseUrl}/calculateNonPointStandardFuelUse`;
    return this.http.post<ReportingPeriod[]>(url, {});
  }
}
