/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { BaseCodeLookup } from 'src/app/shared/models/base-code-lookup';

export class UserFeedback {
    id: number;
	reportId: number;
	beneficialFunctionalityComments: string;
    difficultFunctionalityComments: string;
    enhancementComments: string;
    intuitiveRating: number;
    dataEntryScreens: number;
    dataEntryBulkUpload: number;
    calculationScreens: number;
    controlsAndControlPathAssignments: number;
    qualityAssuranceChecks: number;
    overallReportingTime: number;
    facilityName: string;
    year: number;
    userId: string;
    userName: string;
    userRole: string;
    programSystemCode: BaseCodeLookup;
    createdDate: string;
    lastModifiedBy: string;
}

export interface UserFeedbackExport {
    facilityName: string;
    userName: string;
    year: number,
    createdDate: string;
    intuitiveRating?: number;
    dataEntryScreens?: number;
    dataEntryBulkUpload?: number;
    calculationScreens?: number;
    controlsAndControlPathAssignments?: number;
    qualityAssuranceChecks?: number;
    overallReportingTime?: number;
    beneficialFunctionalityComments?: string;
    difficultFunctionalityComments?: string;
    enhancementComments?: string;
}
