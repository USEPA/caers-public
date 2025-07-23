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
