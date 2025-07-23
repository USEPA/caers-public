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
import { ValidatorFn, AbstractControl, Validator, NG_VALIDATORS } from '@angular/forms';

export function legacyUomValidator(): ValidatorFn {
  return (control: AbstractControl): {[key: string]: any} | null => {
    if (!control.value) {
      return null;
    }
    return control.value.legacy ? {legacyUom: {value: control.value.code}} : null;
  };
}

@Directive({
  selector: '[appLegacyUomValidator]',
  providers: [{provide: NG_VALIDATORS, useExisting: LegacyUomValidatorDirective, multi: true}]
})
export class LegacyUomValidatorDirective implements Validator {

  validate(control: AbstractControl): {[key: string]: any} | null {
    return legacyUomValidator()(control);
  }

}
