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
