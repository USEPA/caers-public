/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
