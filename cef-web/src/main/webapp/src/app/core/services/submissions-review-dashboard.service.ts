/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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

