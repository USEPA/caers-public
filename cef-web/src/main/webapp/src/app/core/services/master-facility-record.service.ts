/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MasterFacilityRecord } from 'src/app/shared/models/master-facility-record';
import { BaseCodeLookup } from 'src/app/shared/models/base-code-lookup';
import { MasterFacilityNaicsCode } from 'src/app/shared/models/master-facility-naics-code';
import {FacilityPermit} from "src/app/shared/models/facility-permit";

@Injectable({
  providedIn: 'root'
})
export class MasterFacilityRecordService {

    private baseUrl = 'api/masterFacility';  // URL to web api

    constructor(private http: HttpClient) { }

    getRecord(recordId: number): Observable<MasterFacilityRecord> {
        const url = `${this.baseUrl}/${recordId}`;
        return this.http.get<MasterFacilityRecord>(url);
    }

    getProgramRecords(programCode: string): Observable<MasterFacilityRecord[]> {
        const url = `${this.baseUrl}/program/${programCode}`;
        return this.http.get<MasterFacilityRecord[]>(url);
    }

    search(criteria: MasterFacilityRecord): Observable<MasterFacilityRecord[]> {
        const url = `${this.baseUrl}/search`;
        return this.http.post<MasterFacilityRecord[]>(url, criteria);
    }

    getMyProgramRecords(): Observable<MasterFacilityRecord[]> {
        const url = `${this.baseUrl}/program/my`;
        return this.http.get<MasterFacilityRecord[]>(url);
    }

    getProgramSystemCodes(): Observable<BaseCodeLookup[]> {
        const url = `${this.baseUrl}/programSystemCodes`;
        return this.http.get<BaseCodeLookup[]>(url);
    }

    update(masterFacility: MasterFacilityRecord): Observable<MasterFacilityRecord> {
        const url = `${this.baseUrl}/${masterFacility.id}`;
        return this.http.put<MasterFacilityRecord>(url, masterFacility);
    }

    add(masterFacility: MasterFacilityRecord): Observable<MasterFacilityRecord> {
        const url = `${this.baseUrl}/create`;
        return this.http.post<MasterFacilityRecord>(url, masterFacility);
    }

    isDuplicateAgencyId(agencyFacilityIdentifier: string, psc: string): Observable<boolean> {
        const url = `${this.baseUrl}/isDuplicateAgencyId/${agencyFacilityIdentifier}/${psc}`;
        return this.http.get<boolean>(url);
    }

    getUserProgramSystemCode(): Observable<BaseCodeLookup> {
        const url = `${this.baseUrl}/userProgramSystemCode`;
        return this.http.get<BaseCodeLookup>(url);
    }

    /** Create Master facility NAICS */
    createMasterFacilityNaics(masterFacilityNaics: MasterFacilityNaicsCode): Observable<{}> {
        const url = `${this.baseUrl}/naics/`;
        return this.http.post<MasterFacilityNaicsCode>(url, masterFacilityNaics);
    }

    /** Update Master facility NAICS */
    updateMasterFacilityNaics(masterFacilityNaics: MasterFacilityNaicsCode): Observable<MasterFacilityNaicsCode> {
        const url = `${this.baseUrl}/naics/${masterFacilityNaics.id}`;
        return this.http.put<MasterFacilityNaicsCode>(url, masterFacilityNaics);
    }

    /** DELETE Master facility NAICS from the database */
    deleteMasterFacilityNaics(masterFacilityNaicsId: number): Observable<{}> {
        const url = `${this.baseUrl}/naics/${masterFacilityNaicsId}`;
        return this.http.delete(url);
    }

    /** Create Master facility Permit */
    createMasterFacilityPermit(permit: FacilityPermit): Observable<{}> {
        const url = `${this.baseUrl}/permit/`;
        return this.http.post<FacilityPermit>(url, permit);
    }

    /** Update Master facility Permit */
    updateMasterFacilityPermit(permit: FacilityPermit): Observable<FacilityPermit> {
        const url = `${this.baseUrl}/permit/${permit.id}`;
        return this.http.put<FacilityPermit>(url, permit);
    }

    /** DELETE Master facility Permit from the database */
    deleteMasterFacilityPermit(permitId: number): Observable<{}> {
        const url = `${this.baseUrl}/permit/${permitId}`;
        return this.http.delete(url);
    }

	getLatLongTolerance(eisId: string): Observable<number> {
        const url = `${this.baseUrl}/getLatLongTolerance/${eisId}`;
        return this.http.get<number>(url);
    }
}
