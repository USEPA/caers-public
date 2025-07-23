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
import { OperatingDetail } from './operating-detail';
import { Emission } from './emission';
import { BaseCodeLookup } from './base-code-lookup';
import { UnitMeasureCode } from 'src/app/shared/models/unit-measure-code';
import { MonthlyFuelReporting } from './monthly-fuel-reporting';

export class ReportingPeriod {
  id: number;
  emissionsProcessId: number;
  reportingPeriodTypeCode: BaseCodeLookup;
  emissionsOperatingTypeCode: BaseCodeLookup;
  calculationParameterTypeCode: BaseCodeLookup;
  calculationParameterValue: string;
  calculationParameterUom: UnitMeasureCode;
  calculationMaterialCode: BaseCodeLookup;
  fuelUseValue: string;
  fuelUseUom: UnitMeasureCode;
  fuelUseMaterialCode: BaseCodeLookup;
  heatContentValue: string;
  heatContentUom: UnitMeasureCode;
  comments: string;
  emissions: Emission[];
  operatingDetails: OperatingDetail[];
  monthlyFuelReporting: MonthlyFuelReporting;
  standardizedNonPointFuelUse: string;
}
