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
import { EmissionsReport } from 'src/app/shared/models/emissions-report';
import { HttpClient, HttpEvent, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ValidationResult } from 'src/app/shared/models/validation-result';
import { MasterFacilityRecord } from 'src/app/shared/models/master-facility-record';
import { EmissionsReportAgencyData } from 'src/app/shared/models/emissions-report-agency-data';
import { ReportChange } from 'src/app/shared/models/report-change';
import { QualityCheckResults } from 'src/app/shared/models/quality-check-results';

@Injectable({
    providedIn: 'root'
})
export class EmissionsReportingService {

    private baseUrl = 'api/emissionsReport';  // URL to web api
    constructor(private http: HttpClient) {}

    /** GET reports for specified facility from the server */
    getFacilityReports(facilityId: number, monthlyReportingEnabled: boolean): Observable<EmissionsReport[]> {
        const url = `${this.baseUrl}/facility/${facilityId}/monthlyReportingEnabled/${monthlyReportingEnabled}`;
        return this.http.get<EmissionsReport[]>(url);
    }

    /** GET most recent report for specified facility from the server */
    getCurrentReport(facilityId: string): Observable<EmissionsReport> {
        const url = `${this.baseUrl}/facility/${facilityId}/current`;
        return this.http.get<EmissionsReport>(url);
    }

    /** Get report by report id */
    getReport(reportId: number): Observable<EmissionsReport> {
        const url = `${this.baseUrl}/${reportId}`;
        return this.http.get<EmissionsReport>(url);
    }

    getAgencyReportedYears(): Observable<EmissionsReportAgencyData[]> {
        const url = `${this.baseUrl}/agencyYears`;
        return this.http.get<EmissionsReportAgencyData[]>(url);
    }

	  getAgencyMonthlyReportedYears(): Observable<EmissionsReportAgencyData[]> {
        const url = `${this.baseUrl}/monthlyReportingEnabled/agencyYears`;
        return this.http.get<EmissionsReportAgencyData[]>(url);
    }

    getReportChanges(reportId: number):Observable<ReportChange[]> {
        const url = `${this.baseUrl}/${reportId}/changes`;
        return this.http.get<ReportChange[]>(url);
    }

    beginAdvancedQA(reportIds: number[]): Observable<EmissionsReport[]> {
        const url = `${this.baseUrl}/beginAdvancedQA`;
        return this.http.post<EmissionsReport[]>(url, reportIds);
    }

    acceptReports(reportIds: number[], comments: string): Observable<EmissionsReport[]> {
        const url = `${this.baseUrl}/accept`;
        return this.http.post<EmissionsReport[]>(url, {
            reportIds,
            comments
        });
    }

    rejectReports(reportIds: number[], comments: string, attachmentId: number): Observable<EmissionsReport[]> {
        const url = `${this.baseUrl}/reject`;
        return this.http.post<EmissionsReport[]>(url, {
            reportIds,
            comments,
            attachmentId
        });
    }

    resetReports(reportIds: number[]): Observable<EmissionsReport[]> {
        const url = `${this.baseUrl}/reset`;
        return this.http.post<EmissionsReport[]>(url, reportIds);
    }

    /** POST request to the server to create a report for the current year from most previous copy */
    createReportFromPreviousCopy(masterFacilityRecordId: number, reportYear: number, thresholdInfo?: string): Observable<HttpResponse<EmissionsReport>> {
        const url = `${this.baseUrl}/facility/${masterFacilityRecordId}`;
        return this.http.post<EmissionsReport>(url, {
            year: reportYear,
            masterFacilityRecordId,
            source: 'previous',
            thresholdStatus: thresholdInfo
        }, {
            observe: 'response'
        });
    }

    /** POST request to the server to create a report for the current year from scratch */
    createReportFromScratch(masterFacilityRecordId: number,
                            reportYear: number, thresholdInfo?: string): Observable<HttpResponse<EmissionsReport>> {

        const url = `${this.baseUrl}/facility/${masterFacilityRecordId}`;
        return this.http.post<EmissionsReport>(url, {
            year: reportYear,
            masterFacilityRecordId,
            source: 'fromScratch',
            thresholdStatus: thresholdInfo,
        }, {
            observe: 'response'
        });
    }

    /** POST request to the server to create a report for the current year from uploaded workbook */
    createReportFromUpload(facility: MasterFacilityRecord,
                           reportYear: number,
                           workbook: File): Observable<HttpEvent<EmissionsReport>> {

        const url = `${this.baseUrl}/facility/${facility.id}`;

        const formData = new FormData();
        formData.append('workbook', workbook);
        formData.append('metadata', new Blob([JSON.stringify({
            year: reportYear,
            masterFacilityRecordId: facility.id,
            eisProgramId: facility.eisProgramId,
            agencyFacilityIdentifier: facility.agencyFacilityIdentifier,
            programSystemCode: facility.programSystemCode.code,
            source: 'fromScratch'
        })], {
            type: 'application/json'
        }));

        return this.http.post<EmissionsReport>(url, formData, {
            observe: 'events',
            reportProgress: true
        });
    }

    /** POST request to the server to create a report for the current year from uploaded workbook */
    createReportFromJsonUpload(facility: MasterFacilityRecord,
                           reportYear: number,
                           jsonFileData: string): Observable<HttpEvent<EmissionsReport>> {

        const url = `${this.baseUrl}/facility/${facility.id}/json`;

        const metadata = {
            year: reportYear,
            masterFacilityRecordId: facility.id,
            eisProgramId: facility.eisProgramId,
            agencyFacilityIdentifier: facility.agencyFacilityIdentifier,
            programSystemCode: facility.programSystemCode.code,
            jsonFileData: jsonFileData,
            source: 'fromScratch'
        };

        return this.http.post<EmissionsReport>(url, metadata, {
            observe: 'events',
            reportProgress: true
        });
    }

    /** POST request to the server to create a validation result for report */
    validateReport(reportId: number, semiannual: boolean): Observable<ValidationResult> {

        const url = `${this.baseUrl}/validation/${semiannual}`;
        return this.http.post<ValidationResult>(url, {
            id: reportId
        });
    }

    /** GET request to check if QA checks are currently in-progress */
    getQAResults(reportId: number, semiannual: boolean): Observable<QualityCheckResults> {
        const url = `${this.baseUrl}/validation/getResults/${reportId}/${semiannual}`;
        return this.http.get<QualityCheckResults>(url);
    }

    /** PUT request to update the QA results status based on validation status */
    updateQAResultsStatus(reportId: number, semiannual: boolean, status: string): Observable<QualityCheckResults> {
        const url = `${this.baseUrl}/validation/updateQAResultsStatus/${reportId}/${semiannual}/${status}`;
        return this.http.put<QualityCheckResults>(url, null);
    };

	  /** POST request to update validation status of report */
    validateThresholdNoQAReport(reportId: number): Observable<{}> {
        const url = `${this.baseUrl}/validation/thresholdNoQA`;
        return this.http.post(url, reportId);
    }

    /** POST request to the server to bulk upload an emissions report */
    uploadReport(jsonFileData: string, fileName: string): Observable<EmissionsReport> {
        const url = `${this.baseUrl}/upload/${encodeURI(fileName)}`;
        return this.http.post<EmissionsReport>(url, jsonFileData);
    }

    /** GET download excel template for specified report */
    downloadExcelExport(reportId: number): Observable<Blob> {
        const url = `${this.baseUrl}/export/${reportId}/excel`;
        return this.http.get(url, { responseType: 'blob' });
    }

    downloadJsonExport(reportId: number): Observable<Blob> {
        const url = `${this.baseUrl}/export/${reportId}`;
        return this.http.get(url, { responseType: 'blob' });
    }

    /** DELETE specified emissions report from database */
    delete(reportId: number): Observable<{}> {
        const url = `${this.baseUrl}/${reportId}`;
        return this.http.delete(url);
    }

    /** Sets hasSubmitted flag on specified emissions report to true */
    updateHasSubmittedFeedback(reportId: number): Observable<{}> {
        const url = `${this.baseUrl}/${reportId}/feedbackSubmitted`;
        return this.http.put(url, null);
    }

	  acceptSemiAnnualReports(reportIds: number[], comments: string): Observable<EmissionsReport[]> {
        const url = `${this.baseUrl}/semiannual/accept`;
        return this.http.post<EmissionsReport[]>(url, {
            reportIds,
            comments
        });
    }

    rejectSemiAnnualReports(reportIds: number[], comments: string, attachmentId: number): Observable<EmissionsReport[]> {
        const url = `${this.baseUrl}/semiannual/reject`;
        return this.http.post<EmissionsReport[]>(url, {
            reportIds,
            comments,
            attachmentId
        });
    }

    downloadCopyOfRecord(activityId: string, documentId: string): Observable<any> {
      const url = `${this.baseUrl}/downloadCopyOfRecord/${activityId}/${documentId}`;
      return this.http.get(url, { responseType: 'blob' });
    }

    generateCopyOfRecord(reportId: number, isSemiannual: boolean, signed: boolean): Observable<any> {
        const url = `${this.baseUrl}/cor/${reportId}/${isSemiannual}/${signed}`;
        return this.http.get(url, { responseType: 'blob' });

    }

    /** POST send Ready to Certify Notification email to NEI Certifiers */
    readyToCertifyNotification(reportId: number, isSemiannual: boolean): Observable<boolean> {
        const url = `${this.baseUrl}/readyToCertify/${isSemiannual}`;
        return this.http.post<boolean>(url, reportId);
    }

    getReportByMfrAndYear(mfrId: number, year: number): Observable<EmissionsReport> {
        const url = `${this.baseUrl}/facility/${mfrId}/year/${year}`;
        return this.http.get<EmissionsReport>(url);
    }

}
