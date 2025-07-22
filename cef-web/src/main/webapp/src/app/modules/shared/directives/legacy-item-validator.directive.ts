/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
