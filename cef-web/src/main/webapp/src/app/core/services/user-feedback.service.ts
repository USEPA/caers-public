/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {UserFeedback, UserFeedbackExport} from 'src/app/shared/models/user-feedback';
import {UserFeedbackStats} from 'src/app/shared/models/user-feedback-stats';
import {Observable} from 'rxjs';
import {ReportDownloadService} from "src/app/core/services/report-download.service";

@Injectable({
    providedIn: 'root'
})
export class UserFeedbackService {

    private baseUrl = 'api/userFeedback';  // URL to web api

    constructor(private http: HttpClient, private downloadService: ReportDownloadService) {
    }

    create(userFeedback: UserFeedback): Observable<UserFeedback> {
        const url = `${this.baseUrl}`;
        return this.http.post<UserFeedback>(url, userFeedback);
    }

    retrieveAllByYearAndProgramSystemCode(year: number, programSystemCode: string): Observable<UserFeedback[]> {
        const url = `${this.baseUrl}/byYearAndProgramSystemCode`;
        const params = new HttpParams()
            .append('year', year.toString())
            .append('programSystemCode', programSystemCode);
        return this.http.get<UserFeedback[]>(url, {params});
    }

    retrieveAvailableProgramSystemCodes(): Observable<string[]> {
        const url = `${this.baseUrl}/programSystemCodes`;
        return this.http.get<string[]>(url);
    }

    retrieveAvailableYears(): Observable<number[]> {
        const url = `${this.baseUrl}/years`;
        return this.http.get<number[]>(url);
    }

    retrieveStatsByYearAndProgramSystemCode(year: number, programSystemCode: string): Observable<UserFeedbackStats> {
        const params = new HttpParams()
            .append('year', year.toString())
            .append('programSystemCode', programSystemCode);
        const url = `${this.baseUrl}/stats/byYearAndProgramSystemCode`;
        return this.http.get<UserFeedbackStats>(url, {params});
    }

    downloadFilteredReport(feedbackData: UserFeedback[]) {

        const cleanText = (text: string): string => {
            if (text == null || text == undefined) {
                // optional chaining returns undefined if null
                return '';
            } else {
                return text
                    .replace(/,/g, '')
                    .replace(/\r?\n|\r/g, '');
            }
        };

        const data: UserFeedbackExport[] = [];
        for (const item of feedbackData) {
            const exportItem: UserFeedbackExport = {
                facilityName: cleanText(item.facilityName),
                userName: item.userName,
                year: item.year,
                createdDate: item.createdDate,
                intuitiveRating: item?.intuitiveRating,
                dataEntryScreens: item?.dataEntryScreens,
                calculationScreens: item?.calculationScreens,
                controlsAndControlPathAssignments: item?.controlsAndControlPathAssignments,
                dataEntryBulkUpload: item?.dataEntryBulkUpload,
                qualityAssuranceChecks: item?.qualityAssuranceChecks,
                overallReportingTime: item?.overallReportingTime,
                beneficialFunctionalityComments: cleanText(item?.beneficialFunctionalityComments),
                difficultFunctionalityComments: cleanText(item?.difficultFunctionalityComments),
                enhancementComments: cleanText(item?.enhancementComments)
            };
            data.push(exportItem)
        }
        const headers: string[] = Object.keys(data[0]);
        const csvData = this.downloadService.ConvertToCSV(JSON.stringify(data), headers);
        this.downloadService.downloadFile(null, 'UserFeedbackReport', csvData);
    }

}
