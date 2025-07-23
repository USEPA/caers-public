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
