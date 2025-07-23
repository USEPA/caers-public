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
import { UnitMeasureCode } from 'src/app/shared/models/unit-measure-code';
import { BaseCodeLookup } from 'src/app/shared/models/base-code-lookup';

export class BulkEntryReportingPeriod {

  emissionsUnitId: number;
  unitIdentifier: string;
  unitDescription: string;
  unitStatus: BaseCodeLookup;

  emissionsProcessId: number;
  emissionsProcessIdentifier: string;
  emissionsProcessDescription: string;
  operatingStatusCode: BaseCodeLookup;
  emissionsProcessSccCode: string;
  fuelUseRequired: boolean;

  reportingPeriodId: number;
  reportingPeriodTypeCode: BaseCodeLookup;
  calculationParameterValue: string;
  calculationParameterUom: UnitMeasureCode;
  calculationMaterialCode: BaseCodeLookup;
  fuelUseValue: string;
  fuelUseUom: UnitMeasureCode;
  fuelUseMaterialCode: BaseCodeLookup;
  previousCalculationParameterValue: string;
  previousCalculationParameterUomCode: string;
}
