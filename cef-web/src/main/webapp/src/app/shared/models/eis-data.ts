/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { FormControl } from '@angular/forms';
import { EisTransactionAttachment } from 'src/app/shared/models/eis-transaction-attachment';

export interface EisDataStats {

   availableYears: Set<number>;
   statuses: EisDataStat[];
}

export enum EisSubmissionStatus {

   All = "All",
   NotStarted = "Not Started",
   QaEmissions = "QA Emissions",
   QaFacility = "QA Facility",
   ProdEmissions = "Prod Emissions",
   ProdFacility = "Prod Facility",
   Complete = "Complete"
}

export interface EisSearchCriteria {

   year: number;
   status?: EisSubmissionStatus;
}

export interface EisData {

   criteria: EisSearchCriteria;
   reports: EisDataReport[];
}

export interface EisDataReport {

   emissionsReportId: number;
   agencyFacilityIdentifier: string;
   attachment: EisTransactionAttachment;
   comments: string;
   eisProgramId: string;
   facilityName: string;
   lastSubmissionStatus: EisSubmissionStatus;
   lastTransactionId: string;
   passed: boolean;
   reportingYear: number;
   reportCheckbox: FormControl;
}

export interface EisReportStatusUpdate {

   submissionStatus: EisSubmissionStatus;
   emissionsReportIds: Set<number>;
}

export interface EisDataStat {

   count: number;
   status: EisSubmissionStatus;
}
