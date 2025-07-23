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
import { AppProperty } from 'src/app/shared/models/app-property';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SltPropertyService {

    private baseUrl = 'api/slt/property';  // URL to web api

  constructor(private http: HttpClient) { }

  retrieve(name: string): Observable<AppProperty> {
    const url = `${this.baseUrl}/${name}`;
    return this.http.get<AppProperty>(url);
  }

  retrieveAll(slt: string): Observable<AppProperty[]> {
    const url = `${this.baseUrl}/property/${slt}`;
    return this.http.get<AppProperty[]>(url);
  }

  update(prop: AppProperty): Observable<AppProperty> {
    const url = `${this.baseUrl}/${prop.name}`;
    return this.http.put<AppProperty>(url, prop);
  }

  bulkUpdate(props: AppProperty[], slt: string): Observable<AppProperty[]> {
    const url = `${this.baseUrl}/${slt}`;
    return this.http.post<AppProperty[]>(url, props);
  }
}
