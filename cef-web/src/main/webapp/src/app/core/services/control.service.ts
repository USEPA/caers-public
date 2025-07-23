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
