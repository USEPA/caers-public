/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FacilityNaicsCode } from 'src/app/shared/models/facility-naics-code';
import { FacilityPermit } from 'src/app/shared/models/facility-permit';

@Injectable({
    providedIn: 'root'
})
export class FacilitySiteService {

    private baseUrl = 'api/facilitySite';  // URL to web api

    constructor(private http: HttpClient) {
    }

    /** GET specified facility site from the server */
    retrieveByIdAndReportYear(id: number, year: number): Observable<FacilitySite> {
        const url = `${this.baseUrl}/${id}/reportYear/${year}`;
        return this.http.get<FacilitySite>(url);
    }

    /** GET facility site by report ID */
    retrieveForReport(reportId: number): Observable<FacilitySite> {
        const url = `${this.baseUrl}/report/${reportId}`;
        return this.http.get<FacilitySite>(url);
    }

    update(facilitySite: FacilitySite): Observable<FacilitySite> {
        const url = `${this.baseUrl}/${facilitySite.id}`;
        return this.http.put<FacilitySite>(url, facilitySite);
    }

    /** Create facility NAICS */
    createFacilityNaics(facilityNaics: FacilityNaicsCode): Observable<{}> {
        const url = `${this.baseUrl}/naics/`;
        return this.http.post<FacilityNaicsCode>(url, facilityNaics);
    }

    /** Update facility NAICS */
    updateFacilityNaics(facilityNaics: FacilityNaicsCode): Observable<FacilityNaicsCode> {
        const url = `${this.baseUrl}/naics/${facilityNaics.id}`;
        return this.http.put<FacilityNaicsCode>(url, facilityNaics);
    }

    /** DELETE facility NAICS from the database */
    deleteFacilityNaics(facilityNaicsId: number): Observable<{}> {
        const url = `${this.baseUrl}/naics/${facilityNaicsId}`;
        return this.http.delete(url);
    }

    /** GET facility permits */
    retrievePermits(facilitySiteId: number): Observable<FacilityPermit[]> {
        const url = `${this.baseUrl}/${facilitySiteId}/permit`;
        return this.http.get<FacilityPermit[]>(url);
    }
}
