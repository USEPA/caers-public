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
