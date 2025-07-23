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
import { BaseCodeLookup } from 'src/app/shared/models/base-code-lookup';
import { EmissionFormulaVariableCode } from 'src/app/shared/models/emission-formula-variable-code';
import { UnitMeasureCode } from 'src/app/shared/models/unit-measure-code';

export class EmissionFactor {
  id: number;
  emissionsNumeratorUom: UnitMeasureCode;
  emissionsDenominatorUom: UnitMeasureCode;
  calculationParameterTypeCode: BaseCodeLookup;
  calculationMaterialCode: BaseCodeLookup;
  controlMeasureCode: BaseCodeLookup;
  controlMeasureCode2: BaseCodeLookup;
  controlMeasureCode3: BaseCodeLookup;
  controlMeasureCode4: BaseCodeLookup;
  controlMeasureCode5: BaseCodeLookup;
  sccCode: number;
  pollutantCode: string;
  formulaIndicator: boolean;
  controlIndicator: boolean;
  emissionFactor: number;
  emissionFactorFormula: string;
  description: string;
  note: string;
  source: string;
  variables: EmissionFormulaVariableCode[];
  revoked: boolean;
  revokedDate: Date;
  quality: string;
  minValue: number;
  maxValue: number;
  applicability: string;
  derivation: string;
  dateUpdated: Date;
  webfireId: number;
  ghgId: number;
  condition: string;
}
