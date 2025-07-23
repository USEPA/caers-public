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
import { ReleasePoint } from 'src/app/shared/models/release-point';
import { ReleasePointApportionment } from 'src/app/shared/models/release-point-apportionment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReleasePointService {

  private baseUrl = 'api/releasePoint';  // URL to web api

  constructor(private http: HttpClient) { }

  create(releasePoint: ReleasePoint): Observable<ReleasePoint> {
    const url = `${this.baseUrl}`;
    return this.http.post<ReleasePoint>(url, releasePoint);
  }

  update(releasePoint: ReleasePoint): Observable<ReleasePoint> {
    const url = `${this.baseUrl}/${releasePoint.id}`;
    return this.http.put<ReleasePoint>(url, releasePoint);
  }

  /** GET specified release point from the server */
  retrieve(id: number): Observable<ReleasePoint> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.get<ReleasePoint>(url);
  }

  retrievePrevious(id: number): Observable<ReleasePoint> {
    const url = `${this.baseUrl}/${id}/previous`;
    return this.http.get<ReleasePoint>(url);
  }

  retrieveForFacility(facilitySiteId: number): Observable<ReleasePoint[]> {
    const url = `${this.baseUrl}/facility/${facilitySiteId}`;
    return this.http.get<ReleasePoint[]>(url);
  }

  /** Delete specified release point from the database */
  delete(id: number): Observable<{}> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.delete(url);
  }

  /** Delete specified release point apportionment from the database */
  deleteAppt(id: number): Observable<{}> {
    const url = `${this.baseUrl}/appt/${id}`;
    return this.http.delete(url);
  }

  /** Create release point apportionment */
  createAppt(releasePointAppt: ReleasePointApportionment): Observable<{}> {
    const url = `${this.baseUrl}/appt/`;
    return this.http.post<ReleasePointApportionment>(url, releasePointAppt);
  }

  /** Update release point apportionment */
  updateAppt(releasePointAppt: ReleasePointApportionment): Observable<ReleasePointApportionment> {
    const url = `${this.baseUrl}/appt/${releasePointAppt.id}`;
    return this.http.put<ReleasePointApportionment>(url, releasePointAppt);
  }

  retrieveReleasePointsForControlPath(controlPathId: number): Observable<ReleasePoint[]> {
    const url = `${this.baseUrl}/appt/${controlPathId}`;
    return this.http.get<ReleasePoint[]>(url);
  }

}
