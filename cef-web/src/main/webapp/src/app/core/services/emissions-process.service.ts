/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Process } from 'src/app/shared/models/process';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EmissionsProcessService {

  private baseUrl = 'api/emissionsProcess';  // URL to web api

  constructor(private http: HttpClient) { }

  create(process: Process): Observable<Process> {
    const url = `${this.baseUrl}`;
    return this.http.post<Process>(url, process);
  }

  update(process: Process): Observable<Process> {
    const url = `${this.baseUrl}/${process.id}`;
    return this.http.put<Process>(url, process);
  }

    updateScc(process: Process): Observable<Process> {
        const url = `${this.baseUrl}/scc/${process.id}`;
        return this.http.put<Process>(url, process);
    }

  /** GET specified release point from the server */
  retrieve(id: number): Observable<Process> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.get<Process>(url);
  }

  retrievePrevious(id: number): Observable<Process> {
    const url = `${this.baseUrl}/${id}/previous`;
    return this.http.get<Process>(url);
  }

  retrieveForReleasePoint(releasePointId: number): Observable<Process[]> {
    const url = `${this.baseUrl}/releasePoint/${releasePointId}`;
    return this.http.get<Process[]>(url);
  }

  /**
   * GET all of the emissions processes for a specified emissions unit
   */
  retrieveForEmissionsUnit(emissionsUnitId: number): Observable<Process[]> {
    const url = `${this.baseUrl}/emissionsUnit/${emissionsUnitId}`;
    return this.http.get<Process[]>(url);
  }

    /** Delete specified emissions process from the database */
  delete(id: number): Observable<{}> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.delete(url);
  }
}
