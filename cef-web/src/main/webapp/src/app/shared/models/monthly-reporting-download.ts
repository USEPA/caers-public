/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { BaseCodeLookup } from './base-code-lookup';
import { CalculationMethodCode } from './calculation-method-code';

export interface MonthlyReportingDownload {

  // Facility Site
  agencyFacilityIdentifier: string;
  facilityName: string;

  // Unit
  emissionsUnitId: number;
  unitIdentifier: string;
  unitDescription: string;
  unitStatus: BaseCodeLookup;
  unitInitialMonthlyReportingPeriod: string;

  // Process
  emissionsProcessId: number;
  emissionsProcessIdentifier: string;
  emissionsProcessDescription: string;
  emissionsProcessSccCode: string;
  operatingStatusCode: BaseCodeLookup;
  processInitialMonthlyReportingPeriod: string;

  // Reporting Period
  annualReportingPeriodId: number;
  reportingPeriodId: number;
  reportingPeriodName: string;
  calculationMaterialShortName: string;
  calculationParameterValue: string;
  calculationParameterUomCode: string;
  fuelUseMaterialShortName: string;
  fuelUseValue: string;
  fuelUseUomCode: string;
  period: string;

  // Operating Detail
  operatingDetailId: number;
  hoursPerPeriod: number;
  avgHoursPerDay: number;
  avgDaysPerWeek: number;
  avgWeeksPerPeriod: number;

  // Emission
  emissionId: number;
  pollutantName: string;
  emissionsCalcMethodDescription: string;
  monthlyRate: number;
  emissionsFactorText: string;
  emissionsFactor: string;
  totalEmissions: number;
  emissionsUomCode: string;

}
