/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
