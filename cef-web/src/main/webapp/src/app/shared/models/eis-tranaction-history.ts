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
import { EisSubmissionStatus } from 'src/app/shared/models/eis-data';
import { EisTransactionAttachment } from 'src/app/shared/models/eis-transaction-attachment';
import { BaseCodeLookup } from 'src/app/shared/models/base-code-lookup';

export class EisTranactionHistory {
    id: number;
    programSystemCode: BaseCodeLookup;
    createdDate: Date;
    eisSubmissionStatus: EisSubmissionStatus;
    transactionId: string;
    submitterName: string;
    attachment: EisTransactionAttachment;

    checked: boolean;
}
