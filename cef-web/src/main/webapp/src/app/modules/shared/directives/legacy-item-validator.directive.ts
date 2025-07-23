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
import { Directive, Input } from '@angular/core';
import { AbstractControl, ValidatorFn, NG_VALIDATORS, Validator } from '@angular/forms';

export function legacyItemValidator(year: number, type: string, displayField: string): ValidatorFn {
  return (control: AbstractControl): {[key: string]: any} | null => {
    if (!control.value || !control.value.lastInventoryYear) {
      return null;
    }
    return control.value.lastInventoryYear < year ? {legacyItem: {value: control.value, type, displayField}} : null;
  };
}

@Directive({
  selector: '[appLegacyItemValidator]',
  providers: [{provide: NG_VALIDATORS, useExisting: LegacyItemValidatorDirective, multi: true}]
})
export class LegacyItemValidatorDirective implements Validator {
  @Input() year: number;
  @Input() type: string;
  @Input() displayField: string;

  validate(control: AbstractControl): {[key: string]: any} | null {
    return legacyItemValidator(this.year, this.type, this.displayField)(control);
  }

}
