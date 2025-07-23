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
