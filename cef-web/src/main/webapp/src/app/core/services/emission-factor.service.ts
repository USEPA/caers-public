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
import { EmissionFactor } from 'src/app/shared/models/emission-factor';

@Injectable({
  providedIn: 'root'
})
export class EmissionFactorService {

  private baseUrl = 'api/emissionFactor';  // URL to web api

  constructor(private http: HttpClient) { }

  search(criteria: EmissionFactor, slt?: boolean): Observable<EmissionFactor[]> {
    let url = `${this.baseUrl}`;

    if (slt) {
        url += '/slt'
    }

    return this.http.post<EmissionFactor[]>(url, criteria);
  }

  retrieveByWebfireId(webfireId: number): Observable<EmissionFactor> {
    const url = `${this.baseUrl}/webfireId/${webfireId}`;
    return this.http.get<EmissionFactor>(url);
  }

  retrieveByGhgId(ghgId: number): Observable<EmissionFactor> {
      const url = `${this.baseUrl}/ghgId/${ghgId}`;
      return this.http.get<EmissionFactor>(url);
  }
}
