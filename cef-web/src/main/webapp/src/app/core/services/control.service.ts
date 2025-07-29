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
import { Control } from 'src/app/shared/models/control';
import { EmissionsReportItem } from 'src/app/shared/models/emissions-report-item';
import { ControlPollutant } from 'src/app/shared/models/control-pollutant';
import { ControlAssignment } from 'src/app/shared/models/control-assignment';

@Injectable({
  providedIn: 'root'
})
export class ControlService {

  private baseUrl = 'api/control';  // URL to web api

  constructor(private http: HttpClient) { }

  create(control: Control): Observable<Control> {
    const url = `${this.baseUrl}`;
    return this.http.post<Control>(url, control);
  }

  /** GET specified control from the server */
  retrieve(id: number): Observable<Control> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.get<Control>(url);
  }

  retrievePrevious(id: number): Observable<Control> {
    const url = `${this.baseUrl}/${id}/previous`;
    return this.http.get<Control>(url);
  }

  retrieveForFacilitySite(facilitySiteId: number): Observable<Control[]> {
    const url = `${this.baseUrl}/facilitySite/${facilitySiteId}`;
    return this.http.get<Control[]>(url);
  }

  retrieveComponents(controlId: number): Observable<EmissionsReportItem[]> {
    const url = `${this.baseUrl}/components/${controlId}`;
    return this.http.get<EmissionsReportItem[]>(url);
  }

  update(control: Control): Observable<Control> {
    const url = `${this.baseUrl}/${control.id}`;
    return this.http.put<Control>(url, control);
  }

  delete(id: number): Observable<{}> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.delete(url);
  }

    /** Create Control Pollutant */
  createPollutant(controlPollutant: ControlPollutant): Observable<{}> {
    const url = `${this.baseUrl}/pollutant/`;
    return this.http.post<ControlPollutant>(url, controlPollutant);
  }

  /** Update Control Pollutant */
  updatePollutant(controlPollutant: ControlPollutant): Observable<{}> {
    const url = `${this.baseUrl}/pollutant/${controlPollutant.id}`;
    return this.http.put<ControlPollutant>(url, controlPollutant);
  }

    /** Delete Control Pollutant */
  deletePollutant(id: number): Observable<{}> {
    const url = `${this.baseUrl}/pollutant/${id}`;
    return this.http.delete<ControlPollutant>(url);
  }

  retrieveAssignmentsForControl(controlId: number): Observable<ControlAssignment[]> {
    const url = `${this.baseUrl}/controlAssignment/${controlId}`;
    return this.http.get<ControlAssignment[]>(url);
  }

}
