/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
