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
