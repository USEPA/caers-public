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
import { HttpClient, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SharedService } from './shared.service';
import { Attachment } from 'src/app/shared/models/attachment';

@Injectable({
  providedIn: 'root'
})
export class AttachmentService {
  reportId: string;

  private baseUrl = 'api/reports/reportId/attachments';  // URL to web api

  constructor(private sharedService: SharedService,
              private http: HttpClient) {

    this.sharedService.reportIdChangeEmitted$.subscribe(reportId => {
      this.reportId = reportId.toString();
      this.baseUrl = 'api/reports/' + this.reportId + '/attachments';
    });
  }

  /** GET download specified report attachment */
  downloadAttachment(attachmentId: number): Observable<any> {
    const url = `${this.baseUrl}/${attachmentId}`;
    return this.http.get(url, { responseType: 'blob' });
  }

  /** POST upload report attachment */
  uploadAttachment( reportAttachment: Attachment,
                    attachment: File): Observable<HttpEvent<Attachment>> {

    const url = `${this.baseUrl}/uploadAttachment`;

    const formData = new FormData();
    formData.append('file', attachment);
    formData.append('metadata', new Blob([JSON.stringify({
      id: reportAttachment.id,
      reportId: reportAttachment.reportId,
      comments: reportAttachment.comments
    })], {
      type: 'application/json'
    }));

    return this.http.post<Attachment>(url, formData, {
      observe: 'events',
      reportProgress: true
    });
  }

  /** Delete specified attachment from the database */
  deleteAttachment(attachmentId: number): Observable<{}> {
    const url = `${this.baseUrl}/${attachmentId}`;
    return this.http.delete(url);
  }

}
