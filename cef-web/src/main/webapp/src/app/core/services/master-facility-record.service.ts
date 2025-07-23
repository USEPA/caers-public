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
