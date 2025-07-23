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
import { Process } from 'src/app/shared/models/process';
import { BaseCodeLookup } from 'src/app/shared/models/base-code-lookup';
import { UnitMeasureCode } from 'src/app/shared/models/unit-measure-code';

export class EmissionUnit {
  id: number;
  facilitySiteId: number;
  unitIdentifier: string;
  description: string;
  unitTypeCode: BaseCodeLookup;
  operatingStatusCode: BaseCodeLookup;
  statusYear: number;
  unitOfMeasureCode: UnitMeasureCode;
  designCapacity: number;
  comments: string;
  processes: Process[];
  emissionsProcesses: Process[];
  previousUnit: EmissionUnit;
  initialMonthlyReportingPeriod: string;
}
