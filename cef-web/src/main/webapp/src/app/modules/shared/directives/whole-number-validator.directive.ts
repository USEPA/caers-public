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
import { Directive } from '@angular/core';
import { ValidatorFn, AbstractControl, NG_VALIDATORS, Validator } from '@angular/forms';

export function wholeNumberValidator(): ValidatorFn {
  return (control: AbstractControl): {[key: string]: any} | null => {
    if (!control.value) {
      return null;
    }
    const result = /^[0-9]+$/.test(control.value);
    return result ? null : {wholeNumber: {value: control.value}};
  };
}

@Directive({
  selector: '[appWholeNumberValidator]',
  providers: [{provide: NG_VALIDATORS, useExisting: WholeNumberValidatorDirective, multi: true}]
})
export class WholeNumberValidatorDirective implements Validator {

  validate(control: AbstractControl): {[key: string]: any} | null {
    return wholeNumberValidator()(control);
  }

}
