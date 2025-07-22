/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { AppProperty } from 'src/app/shared/models/app-property';

@Injectable({
  providedIn: 'root'
})
export class ConfigPropertyService {

  private baseUrl = 'api/property';  // URL to web api

  constructor(private http: HttpClient) { }

  retrieveBulkEntryEnabled(): Observable<boolean> {
    const url = `${this.baseUrl}/bulkEntry/enabled`;
    return this.http.get<boolean>(url);
  }

  retrieveUserFeedbackEnabled(): Observable<boolean> {
    const url = `${this.baseUrl}/userFeedback/enabled`;
    return this.http.get<boolean>(url);
  }

  retrieveAdminAnnouncementEnabled(): Observable<boolean> {
    const url = `${this.baseUrl}/announcement/enabled`;
    return this.http.get<boolean>(url);
  }

  retrieveAdminAnnouncementText(): Observable<AppProperty> {
    const url = `${this.baseUrl}/announcement/text`;
    return this.http.get<AppProperty>(url);
  }

  retrieveReportAttachmentMaxSize(): Observable<AppProperty> {
    const url = `${this.baseUrl}/attachments/maxSize`;
    return this.http.get<AppProperty>(url);
  }

  retrieveExcelExportEnabled(): Observable<boolean> {
    const url = `${this.baseUrl}/excelExport/enabled`;
    return this.http.get<boolean>(url);
  }

  retrieveReportCertificationEnabled(): Observable<boolean> {
    const url = `${this.baseUrl}/reportCertification/enabled`;
    return this.http.get<boolean>(url);
  }

  retrieveFacilityNaicsEntryEnabled(slt: string): Observable<boolean> {
    const url = `${this.baseUrl}/facilityNaics/${slt}/enabled`;
    return this.http.get<boolean>(url);
  }

    retrieveProcessSccEntryEnabled(slt: string): Observable<boolean> {
        const url = `${this.baseUrl}/editScc/${slt}/enabled`;
        return this.http.get<boolean>(url);
    }

  retrieveSltAnnouncementEnabled(slt: string): Observable<boolean> {
    const url = `${this.baseUrl}/announcement/${slt}/enabled`;
    return this.http.get<boolean>(url);
  }

  retrieveSltAnnouncementText(slt: string): Observable<AppProperty> {
    const url = `${this.baseUrl}/announcement/${slt}/text`;
    return this.http.get<AppProperty>(url);
  }

  retrieveSLTThresholdScreeningGADNREnabled(slt: string): Observable<boolean> {
    const url = `${this.baseUrl}/thresholdScreening/gadnr/${slt}/enabled`;
  	return this.http.get<boolean>(url);
  }

  retrieveSltMonthlyFuelReportingEnabled(slt: string): Observable<boolean> {
    const url = `${this.baseUrl}/monthlyFuelReporting/${slt}/enabled`;
    return this.http.get<boolean>(url);
  }

  retrieveSltMonthlyFuelReportingInitialYear(slt: string): Observable<number> {
    const url = `${this.baseUrl}/monthlyFuelReportingInitialYear/${slt}`;
    return this.http.get<number>(url);
  }

  retrieveAllReportUploadPropertiesForProgramSystem(slt: string): Observable<string> {
	const url = `${this.baseUrl}/reportUploadTypes/${slt}`;
	return this.http.get<string>(url);
  }

  retrieveSltEmissionFactorCompendiumEnabled(slt: string): Observable<boolean> {
    const url = `${this.baseUrl}/emissionFactorCompendium/${slt}/enabled`;
    return this.http.get<boolean>(url);
  }

  retrieveNewReportCreationEnabled(): Observable<boolean> {
    const url = `${this.baseUrl}/newReportCreation/enabled`;
    return this.http.get<boolean>(url);
  }

  retrieveSLTNewReportCreationEnabled(slt: string): Observable<boolean> {
    const url = `${this.baseUrl}/newReportCreation/${slt}/enabled`;
  	return this.http.get<boolean>(url);
  }

  retrieveSltBillingExemptEnabled(slt: string): Observable<boolean> {
    const url = `${this.baseUrl}/sltBillingExempt/${slt}/enabled`;
    return this.http.get<boolean>(url);
  }
}
