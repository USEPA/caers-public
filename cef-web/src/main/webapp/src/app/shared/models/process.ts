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
import { ReleasePointApportionment } from 'src/app/shared/models/release-point-apportionment';
import { ReportingPeriod } from 'src/app/shared/models/reporting-period';
import { BaseCodeLookup } from 'src/app/shared/models/base-code-lookup';
import { EmissionUnit } from 'src/app/shared/models/emission-unit';
import { AircraftEngineTypeCode } from './aircraft-engine-type-code';

export class Process {
  id: number;
  year: number;
  emissionsUnitId: number;
  aircraftEngineTypeCode: AircraftEngineTypeCode;
  operatingStatusCode: BaseCodeLookup;
  emissionsProcessIdentifier: string;
  statusYear: number;
  sccCode: string;
  sccCategory: string;
  sccDescription: string;
  sccShortName: string;
  description: string;
  comments: string;
  releasePointAppts: ReleasePointApportionment[];
  reportingPeriods: ReportingPeriod[];
  emissionsUnit: EmissionUnit;
  previousProcess: Process;
  initialMonthlyReportingPeriod: string;
  sltBillingExempt: boolean;
  unitIdentifier: string;
  emissionsUnitDescription: string;
}
