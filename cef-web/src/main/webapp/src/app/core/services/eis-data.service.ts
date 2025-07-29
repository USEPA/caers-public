/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import {Injectable} from '@angular/core';
import {HttpClient, HttpParams, HttpEvent} from "@angular/common/http";
import {
   EisData,
   EisDataReport,
   EisDataStats,
   EisReportStatusUpdate,
   EisSearchCriteria,
   EisSubmissionStatus
} from "src/app/shared/models/eis-data";
import {Observable} from "rxjs";
import { EisTranactionHistory } from 'src/app/shared/models/eis-tranaction-history';
import { EisTransactionAttachment } from 'src/app/shared/models/eis-transaction-attachment';

@Injectable({
   providedIn: 'root'
})
export class EisDataService {

   private baseUrl = 'api/eis';  // URL to web api

   constructor(private http: HttpClient) {
   }

   retrieveStatsByYear(year: number): Observable<EisDataStats> {
      const params = new HttpParams()
         .append('year', year.toString());

      return this.http.get<EisDataStats>(`${this.baseUrl}/emissionsReport/stats`, { params });
   }

   retrieveTransactionHistory(): Observable<EisTranactionHistory[]> {

      return this.http.get<EisTranactionHistory[]>(`${this.baseUrl}/history`);
   }

   searchData(criteria: EisSearchCriteria): Observable<EisData> {

      let status = this.convertStatusToEnum(criteria.status);

      let params = new HttpParams()
         .append("year", criteria.year.toString())
         .append("status", status);

      return this.http.get<EisData>(`${this.baseUrl}/emissionsReport`, {params: params});
   }

   submitReports(statusUpdate: EisReportStatusUpdate) : Observable<EisData>{

      return this.http.post<EisData>(`${this.baseUrl}/transaction`, {

         submissionStatus: this.convertStatusToEnum(statusUpdate.submissionStatus),
         emissionsReports: Array.from(statusUpdate.emissionsReportIds.values())
      });
   }

   updateComment(id: number, comment: string) : Observable<EisDataReport> {

      return this.http.put<EisDataReport>(`${this.baseUrl}/emissionsReport/${id}/comment`, {
         value: comment
      });
   }

   updateEisPassedStatus(id: number, passed: boolean): Observable<EisDataReport> {
      return this.http.put<EisDataReport>(`${this.baseUrl}/emissionsReport/${id}/passed`, {value: passed});
   }

   private convertStatusToEnum(status: EisSubmissionStatus) {

      let result = "";
      if (status && status !== EisSubmissionStatus.All) {

         for (const key in EisSubmissionStatus) {
            if (status === EisSubmissionStatus[key]) {
               result = key;
               break;
            }
         }
      }

      return result;
   }

  deleteFromTransactionHistory(ids: number[]): Observable<{}> {
    const url = `${this.baseUrl}/history/delete`;
    return this.http.post(url, ids);
  }

   downloadAttachment(attachmentId: number, ): Observable<any> {
    const url = `${this.baseUrl}/history/attachment/${attachmentId}`;
    return this.http.get(url, { responseType: 'blob' });
  }

  /** POST upload report attachment */
  uploadAttachment( metadata: EisTransactionAttachment,
                    attachment: File): Observable<HttpEvent<EisTransactionAttachment>> {

    const url = `${this.baseUrl}/history/attachment/uploadAttachment`;

    const formData = new FormData();
    formData.append('file', attachment);
    formData.append('metadata', new Blob([JSON.stringify({
      id: metadata.id,
      transactionHistoryId: metadata.transactionHistoryId,
    })], {
      type: 'application/json'
    }));

    return this.http.post<EisTransactionAttachment>(url, formData, {
      observe: 'events',
      reportProgress: true
    });
  }

  /** Delete specified eport attachment from the database */
  deleteAttachment(attachmentId: number): Observable<{}> {
    const url = `${this.baseUrl}/history/attachment/${attachmentId}`;
    return this.http.delete(url);
  }
}
