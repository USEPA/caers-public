/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
