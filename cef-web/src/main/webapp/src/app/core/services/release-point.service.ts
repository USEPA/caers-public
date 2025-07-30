/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
