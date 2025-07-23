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
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SubmissionUnderReview } from 'src/app/shared/models/submission-under-review';


@Injectable( {
    providedIn: 'root'
} )
export class SubmissionsReviewDashboardService {

    private baseUrl = 'api/submissionsReview';  // URL to web api

    constructor( private http: HttpClient ) {
    }

    retrieveReviewerSubmissions(reportYear: number, reportStatus: string): Observable<SubmissionUnderReview[]> {

        const url = `${this.baseUrl}/dashboard`;

        let params = new HttpParams();
        params = reportYear ? params.append('reportYear', reportYear?.toString()) : params;
        params = reportStatus ? params.append('reportStatus', reportStatus) : params;

        return this.http.get<SubmissionUnderReview[]>(url, {params});
    }

    retrieveSubmissions(reportYear: number, reportStatus: string, agency: string): Observable<SubmissionUnderReview[]> {

        const url = `${this.baseUrl}/dashboard/${agency}`;

        let params = new HttpParams();
        params = reportYear ? params.append('reportYear', reportYear?.toString()) : params;
        params = reportStatus ? params.append('reportStatus', reportStatus) : params;

        return this.http.get<SubmissionUnderReview[]>(url, {params});
    }

	retrieveReviewerNotStartedSubmissions(reportYear: number): Observable<SubmissionUnderReview[]> {

        const url = `${this.baseUrl}/dashboard/notStarted`;

        let params = new HttpParams();
        params = reportYear ? params.append('reportYear', reportYear?.toString()) : params;

        return this.http.get<SubmissionUnderReview[]>(url, {params});
    }

	retrieveNotStartedSubmissions(reportYear: number, agency: string): Observable<SubmissionUnderReview[]> {

        const url = `${this.baseUrl}/dashboard/notStarted/${agency}`;

        let params = new HttpParams();
        params = reportYear ? params.append('reportYear', reportYear?.toString()) : params;

        return this.http.get<SubmissionUnderReview[]>(url, {params});
    }

	retrieveReviewerSemiAnnualSubmissions(reportYear: number, midYearSubmissionStatus: string): Observable<SubmissionUnderReview[]> {

        const url = `${this.baseUrl}/dashboard/semiAnnual`;

        let params = new HttpParams();
        params = reportYear ? params.append('reportYear', reportYear?.toString()) : params;
        params = midYearSubmissionStatus ? params.append('monthlyReportStatus', midYearSubmissionStatus) : params;

        return this.http.get<SubmissionUnderReview[]>(url, {params});
    }

	retrieveSemiAnnualSubmissionsByAgency(reportYear: number, midYearSubmissionStatus: string, agency: string): Observable<SubmissionUnderReview[]> {

        const url = `${this.baseUrl}/dashboard/semiAnnual/${agency}`;

        let params = new HttpParams();
        params = reportYear ? params.append('reportYear', reportYear?.toString()) : params;
        params = midYearSubmissionStatus ? params.append('monthlyReportStatus', midYearSubmissionStatus) : params;

        return this.http.get<SubmissionUnderReview[]>(url, {params});
    }
}

