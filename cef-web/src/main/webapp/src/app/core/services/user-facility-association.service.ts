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
import { UserFacilityAssociation } from 'src/app/shared/models/user-facility-association';
import { Observable } from 'rxjs';
import { MasterFacilityRecord } from 'src/app/shared/models/master-facility-record';

@Injectable({
  providedIn: 'root'
})
export class UserFacilityAssociationService {

    private baseUrl = 'api/userFacilityAssociation';  // URL to web api

    constructor(private http: HttpClient) { }

    getAssociation(id: number): Observable<UserFacilityAssociation> {
        const url = `${this.baseUrl}/${id}`;
        return this.http.get<UserFacilityAssociation>(url);
    }

    delete(id: number): Observable<{}> {
        const url = `${this.baseUrl}/${id}`;
        return this.http.delete(url);
    }

    selfRemoveAssociation(mfrId: number) {
        const url = `${this.baseUrl}/selfRemove`;
        return this.http.post<UserFacilityAssociation>(url, mfrId);
    }

    createAssociationRequest(facility: MasterFacilityRecord) {
        const url = `${this.baseUrl}/request`;
        return this.http.post<UserFacilityAssociation>(url, facility);
    }

    approveAssociations(associations: UserFacilityAssociation[]) {
        const url = `${this.baseUrl}/approve`;
        return this.http.post<UserFacilityAssociation[]>(url, associations);
    }

    rejectAssociations(associations: UserFacilityAssociation[], comments: string) {
        const url = `${this.baseUrl}/reject`;
        return this.http.post<UserFacilityAssociation[]>(url, {associations, comments});
    }

    getMyAssociations(): Observable<UserFacilityAssociation[]> {
        const url = `${this.baseUrl}/my`;
        return this.http.get<UserFacilityAssociation[]>(url);
    }

	getMyProgramSystemCodeAssociations(): Observable<string[]> {
        const url = `${this.baseUrl}/myPrograms`;
        return this.http.get<string[]>(url);
    }

    getApprovedAssociationDetailsForFacility(facilityId: number): Observable<UserFacilityAssociation[]> {
        const url = `${this.baseUrl}/facility/${facilityId}/approved/details`;
        return this.http.get<UserFacilityAssociation[]>(url);
    }

    getPendingAssociationDetails(): Observable<UserFacilityAssociation[]> {
        const url = `${this.baseUrl}/pending/details`;
        return this.http.get<UserFacilityAssociation[]>(url);
    }

    migrateUserAssociations(): Observable<UserFacilityAssociation[]> {
        const url = `${this.baseUrl}/migrate`;
        return this.http.get<UserFacilityAssociation[]>(url);
    }
}
