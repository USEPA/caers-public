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
import { CalculationMaterialCode } from './calculation-material-code';
import { UnitMeasureCode } from './unit-measure-code';

export class MonthlyReportingPeriodHolder {

    // unit
    emissionsUnitId: number
    unitIdentifier: string;
    unitInitialMonthlyReportingPeriod: string;

    // process
    emissionsProcessId: number;
    emissionsProcessIdentifier: string;
    emissionsProcessDescription: string;
    emissionsProcessSccCode: string;
    processInitialMonthlyReportingPeriod: string;

    // reporting period
    reportingPeriodId: number;
    calculationMaterialCode: CalculationMaterialCode;
    calculationParameterValue: string;
    calculationParameterUom: UnitMeasureCode;
    fuelUseMaterialCode: BaseCodeLookup;
    fuelUseValue: string;
    fuelUseUom: UnitMeasureCode;

    // operating details
    actualHoursPerPeriod: number;
    avgHoursPerDay: number;
    avgDaysPerWeek: number;
    avgWeeksPerPeriod: number;

}
