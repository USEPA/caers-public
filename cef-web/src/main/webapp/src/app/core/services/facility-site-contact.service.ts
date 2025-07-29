/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
