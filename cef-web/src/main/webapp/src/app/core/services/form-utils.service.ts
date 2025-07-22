/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Injectable } from '@angular/core';
import { BaseCodeLookup } from 'src/app/shared/models/base-code-lookup';
import { Pollutant } from 'src/app/shared/models/pollutant';

@Injectable({
  providedIn: 'root'
})
export class FormUtilsService {
  emissionsMaxSignificantFigures = 6;

  constructor() { }

  compareCode(c1: BaseCodeLookup, c2: BaseCodeLookup) {
    return c1 && c2 ? c1.code === c2.code : c1 === c2;
  }

  comparePollutantCode(c1: Pollutant, c2: Pollutant) {
    return c1 && c2 ? c1.pollutantCode === c2.pollutantCode : c1 === c2;
  }


  /**
   * Return the number of significant figures contained in the value of the currentValue argument
   */
  getSignificantFigures(currentValue: number): number {
    currentValue = Math.abs(parseInt(String(currentValue).replace('.', ''), 10));
    if (currentValue === 0) {
        return 0;
    }

    // Remove trailing zeroes
    while (currentValue !== 0 && currentValue % 10 === 0) {
        currentValue /= 10;
    }

    return Math.floor(Math.log(currentValue) / Math.log(10)) + 1;
  }
}
