/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { HttpClient, HttpEvent } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Attachment } from "src/app/shared/models/attachment";
import { Communication } from "src/app/shared/models/communication";
import { CommunicationHolder } from "src/app/shared/models/communication-holder";

@Injectable({
  providedIn: 'root'
})
export class CommunicationService {

	private baseUrl = 'api/communication';  // URL to web api

	  constructor(private http: HttpClient) {}

	/** POST upload communication attachment */
	uploadAttachment(metadata: CommunicationHolder,
		attachment: File): Observable<HttpEvent<Attachment>> {

		const url = `${this.baseUrl}/uploadAttachment`;

		const formData = new FormData();
		formData.append('file', attachment);
		formData.append('metadata', new Blob([JSON.stringify({
            communication: metadata.communication,
            reportStatus: metadata.reportStatus,
            userRole: metadata.userRole,
            industrySector: metadata.industrySector,
            reportYear: metadata.reportYear
		})], {
			type: 'application/json'
		}));

		return this.http.post<Attachment>(url, formData, {
			observe: 'events',
			reportProgress: true
		});
	}

    /** PUT send SLT Notification email */
    sendEmailNotification(communication: CommunicationHolder): Observable<Communication> {
        const url = `${this.baseUrl}/sendEmailNotification`;
        return this.http.put<Communication>(url, communication);
    }
}
