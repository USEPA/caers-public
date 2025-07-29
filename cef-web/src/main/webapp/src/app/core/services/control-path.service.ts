/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Injectable } from '@angular/core';
import { ControlPath } from 'src/app/shared/models/control-path';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { ControlAssignment } from 'src/app/shared/models/control-assignment';
import { ControlPathPollutant } from 'src/app/shared/models/control-path-pollutant';

@Injectable({
  providedIn: 'root'
})
export class ControlPathService {

  private baseUrl = 'api/controlPath';  // URL to web api

  constructor(private http: HttpClient) { }

  /** GET specified control path from the server */
  retrieve(id: number): Observable<ControlPath> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.get<ControlPath>(url);
  }

  retrieveForEmissionsProcess(processId: number): Observable<ControlPath[]> {
    const url = `${this.baseUrl}/process/${processId}`;
    return this.http.get<ControlPath[]>(url);
  }

  retrieveForFacilitySite(facilitySiteId: number): Observable<ControlPath[]> {
    const url = `${this.baseUrl}/facilitySite/${facilitySiteId}`;
    return this.http.get<ControlPath[]>(url);
  }

  retrieveForEmissionsUnit(unitId: number): Observable<ControlPath[]> {
    const url = `${this.baseUrl}/unit/${unitId}`;
    return this.http.get<ControlPath[]>(url);
  }

  retrieveForReleasePoint(pointId: number): Observable<ControlPath[]> {
    const url = `${this.baseUrl}/releasePoint/${pointId}`;
    return this.http.get<ControlPath[]>(url);
  }

  retrieveForControlDevice(deviceId: number): Observable<ControlPath[]> {
    const url = `${this.baseUrl}/controlDevice/${deviceId}`;
    return this.http.get<ControlPath[]>(url);
  }

  update(controlPath: ControlPath): Observable<ControlPath> {
    const url = `${this.baseUrl}/${controlPath.id}`;
    return this.http.put<ControlPath>(url, controlPath);
  }

  isPathPreviouslyReported(id: number): Observable<boolean> {
    const url = `${this.baseUrl}/${id}/reported`;
    return this.http.get<boolean>(url);
  }

  delete(id: number): Observable<{}> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.delete(url);
  }

  create(controlPath: ControlPath): Observable<ControlPath> {
    const url = `${this.baseUrl}`;
    return this.http.post<ControlPath>(url, controlPath);
  }

  retrieveAssignmentsForControlPath(controlPathId: number): Observable<ControlAssignment[]> {
    const url = `${this.baseUrl}/controlAssignment/${controlPathId}`;
    return this.http.get<ControlAssignment[]>(url);
  }

  retrieveParentAssignmentsForControlPathChild(controlPathId: number): Observable<ControlAssignment[]> {
    const url = `${this.baseUrl}/paretControlAssignment/${controlPathId}`;
    return this.http.get<ControlAssignment[]>(url);
  }

  createAssignmentForControlPath(controlAssignment: ControlAssignment): Observable<ControlAssignment> {
    const url = `${this.baseUrl}/controlAssignment/`;
    return this.http.post<ControlAssignment>(url, controlAssignment);
  }

  deleteAssignmentForControlPath(controlPathAssignmentId: number): Observable<{}> {
    const url = `${this.baseUrl}/controlAssignment/${controlPathAssignmentId}`;
    return this.http.delete(url);
  }

  updateAssignmentForControlPath(controlPathAssignment: ControlAssignment): Observable<ControlAssignment> {
    const url = `${this.baseUrl}/controlAssignment/${controlPathAssignment.id}`;
    return this.http.put<ControlAssignment>(url, controlPathAssignment);
  }

  /** Create Control Path Pollutant */
  createPollutant(controlPathPollutant: ControlPathPollutant): Observable<{}> {
    const url = `${this.baseUrl}/pollutant/`;
    return this.http.post<ControlPathPollutant>(url, controlPathPollutant);
  }

  /** Update Control Path Pollutant */
  updatePollutant(controlPathPollutant: ControlPathPollutant): Observable<{}> {
    const url = `${this.baseUrl}/pollutant/${controlPathPollutant.id}`;
    return this.http.put<ControlPathPollutant>(url, controlPathPollutant);
  }

  /** Delete Control Path Pollutant */
  deletePollutant(id: number): Observable<{}> {
    const url = `${this.baseUrl}/pollutant/${id}`;
    return this.http.delete<ControlPathPollutant>(url);
  }

}
