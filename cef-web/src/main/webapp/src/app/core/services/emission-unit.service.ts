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
import { EmissionUnit } from 'src/app/shared/models/emission-unit';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SideNavItem } from 'src/app/shared/models/side-nav-item';
import { EmissionsUnitToSideNavPipe } from 'src/app/shared/pipes/emissions-unit-to-side-nav.pipe';


@Injectable({
  providedIn: 'root'
})
export class EmissionUnitService {

    private staticJsonUrl = 'assets/json/emissionUnits.json';  // URL to web api
    private baseUrl = 'api/emissionsUnit';  // URL to web api
    emissionsUnitToSideNavPipe: EmissionsUnitToSideNavPipe;

  constructor(private http: HttpClient) { 
      this.emissionsUnitToSideNavPipe = new EmissionsUnitToSideNavPipe();
  }

  create(emissionUnit: EmissionUnit): Observable<EmissionUnit> {
    const url = `${this.baseUrl}`;
    return this.http.post<EmissionUnit>(url, emissionUnit);
  }

  update(emissionUnit: EmissionUnit): Observable<EmissionUnit> {
    const url = `${this.baseUrl}/${emissionUnit.id}`;
    return this.http.put<EmissionUnit>(url, emissionUnit);
  }

  retrieveForFacility(facilityId: number): Observable<EmissionUnit[]> {
    const url = `${this.baseUrl}/facility/${facilityId}`;
    return this.http.get<EmissionUnit[]>(url);
  }

  /** GET specified emission unit from the server */
  retrieve(id: number): Observable<EmissionUnit> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.get<EmissionUnit>(url);
  }

  retrievePrevious(id: number): Observable<EmissionUnit> {
    const url = `${this.baseUrl}/${id}/previous`;
    return this.http.get<EmissionUnit>(url);
  }

  retrieveReportNavTree(facilityId: number): Observable<SideNavItem[]> {
    const url = `${this.baseUrl}/nav/facility/${facilityId}`;
    return this.http.get<SideNavItem[]>(url);
  }

  /** Delete specified emission unit from the database */
  delete(id: number): Observable<{}> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.delete(url);
  }

}
