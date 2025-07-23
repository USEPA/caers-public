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
import { Pollutant } from 'src/app/shared/models/pollutant';
import { CalculationMethodCode } from 'src/app/shared/models/calculation-method-code';
import { EmissionFormulaVariable } from 'src/app/shared/models/emission-formula-variable';
import { UnitMeasureCode } from 'src/app/shared/models/unit-measure-code';
import { EmissionFactor } from "src/app/shared/models/emission-factor";

export class Emission {
  id: number;
  reportingPeriodId: number;
  pollutant: Pollutant;
  totalManualEntry: boolean;
  overallControlPercent: number;
  totalEmissions: number;
  emissionsUomCode: UnitMeasureCode;
  formulaIndicator: boolean;
  emissionsFactor: number;
  emissionsFactorFormula: string;
  emissionsFactorText: string;
  previousYearTotalEmissions: number;
  previousEmissionsFactor: number;
  previousEmissionsNumeratorUomCode: string;
  previousEmissionsDenominatorUomCode: string;
  emissionsCalcMethodCode: CalculationMethodCode;
  comments: string;
  calculationComment: string;
  emissionsNumeratorUom: UnitMeasureCode;
  emissionsDenominatorUom: UnitMeasureCode;
  calculatedEmissionsTons: number;
  variables: EmissionFormulaVariable[];
  emissionsFactorSource: string;
  webfireEf: EmissionFactor;
  ghgEfId: number;
  monthlyRate: number;
  notReporting: boolean;
  previousNotReporting: boolean;
}
