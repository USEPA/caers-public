/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
