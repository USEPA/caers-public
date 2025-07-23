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

@Injectable({
  providedIn: 'root'
})
export class GeneratedReportsService {

    private baseUrl = 'api/generatedReports';  // URL to web api

  constructor(private http: HttpClient) { }

	generateNonPointFuelSubtractionReport(year: number, slt: string): Observable<any[]> {
		const url = `${this.baseUrl}/nonPointFuelSubtraction/${year}/${slt}`;
		return this.http.get<any[]>(url);
	}

    generateSubmissionStatusAuditReport(slt: string): Observable<any[]> {
        const url = `${this.baseUrl}/submissionStatusAudit/${slt}`;
        return this.http.get<any[]>(url);
    }

	annualEmissionsReportData(component: string, year: number, slt: string): Observable<any> {
		const url = `${this.baseUrl}/annualEmissionsReportData/${component}/${year}/${slt}`;
		return this.http.get(url, { responseType: 'blob' });
	}

    generateAverageMaxNumberQasReport(): Observable<any[]> {
        const url = `${this.baseUrl}/averageNumberQAsReport`;
        return this.http.get<any[]>(url);
    }

    generateAverageMaxNumberQasReportAdmin(slt: string): Observable<any[]> {
        const url = `${this.baseUrl}/averageNumberQAsReportAdmin/${slt}`;
        return this.http.get<any[]>(url);
    }

    generateStandaloneEmissionsProcessesReport(year: number, slt: string): Observable<any> {
        const url = `${this.baseUrl}/standaloneEmissionsProcesses/${year}/${slt}`;
        return this.http.get(url, { responseType: 'blob' });
    }

    generateFacilityReportingStatusReport(year: number, slt: string): Observable<any> {
        const url = `${this.baseUrl}/facilityReportingStatus/${year}/${slt}`;
        return this.http.get(url, { responseType: 'blob' });
    }
}
