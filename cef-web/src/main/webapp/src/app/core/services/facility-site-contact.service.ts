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
import { HttpClient } from '@angular/common/http';
import { FacilitySiteContact } from 'src/app/shared/models/facility-site-contact';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FacilitySiteContactService {

  private baseUrl = 'api/facilitySiteContact';  // URL to web api

  constructor(private http: HttpClient) { }

  create(contact: FacilitySiteContact): Observable<FacilitySiteContact> {
    const url = `${this.baseUrl}`;
    return this.http.post<FacilitySiteContact>(url, contact);
  }

  /** GET specified facility site contact from the server */
  retrieve(id: number): Observable<FacilitySiteContact> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.get<FacilitySiteContact>(url);
  }

  retrieveForFacility(facilitySiteId: number): Observable<FacilitySiteContact[]> {
    const url = `${this.baseUrl}/facility/${facilitySiteId}`;
    return this.http.get<FacilitySiteContact[]>(url);
  }

  update(contact: FacilitySiteContact): Observable<FacilitySiteContact> {
    const url = `${this.baseUrl}/${contact.id}`;
    return this.http.put<FacilitySiteContact>(url, contact);
  }

  /** Delete specified contact from the database */
  delete(id: number): Observable<{}> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.delete(url);
  }
}
