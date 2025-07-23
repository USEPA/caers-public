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
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MonthlyFuelReporting } from 'src/app/shared/models/monthly-fuel-reporting';

@Injectable({
	providedIn: 'root'
})
export class MonthlyFuelReportingService {

	private baseUrl = 'api/monthlyFuelReporting';  // URL to web api

	constructor(private http: HttpClient) { }

	retrieveForMonthData(facilitySiteId: number, period: string): Observable<MonthlyFuelReporting[]> {
		const url = `${this.baseUrl}/monthlyReporting/${facilitySiteId}/${period}`;
		return this.http.get<MonthlyFuelReporting[]>(url);
	}

	saveMonthlyReporting(facilitySiteId: number, periods:MonthlyFuelReporting[]): Observable<MonthlyFuelReporting[]> {
		const url = `${this.baseUrl}/monthlyReporting/${facilitySiteId}`;
    	return this.http.put<MonthlyFuelReporting[]>(url, periods);
	}

	submitMidYearReport(reportId: number): Observable<MonthlyFuelReporting[]> {
		const url = `${this.baseUrl}/submitMidYearReport`;
    	return this.http.put<MonthlyFuelReporting[]>(url, reportId);
	}

	retrieveDownloadDtoByFacilitySiteId(facilitySiteId: number): Observable<any[]> {
		const url = `${this.baseUrl}/downloadReport/${facilitySiteId}`;
    	return this.http.get<any[]>(url);
	}
}
