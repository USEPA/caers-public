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
