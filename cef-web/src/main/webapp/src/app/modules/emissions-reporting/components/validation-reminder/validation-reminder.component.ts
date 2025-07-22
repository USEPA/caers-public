/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { SharedService } from 'src/app/core/services/shared.service';
import { ValidationResult, ValidationItem } from 'src/app/shared/models/validation-result';
import { Observable } from 'rxjs';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-validation-reminder',
  templateUrl: './validation-reminder.component.html',
  styleUrls: ['./validation-reminder.component.scss']
})
export class ValidationReminderComponent implements OnInit {

  validationResult: ValidationResult;
  pageErrors: ValidationItem[] = [];
  pageWarnings: ValidationItem[] = [];

  navEnd: Observable<NavigationEnd>;
  currentUrl: string;

  constructor(private router: Router, private sharedService: SharedService) {
    this.navEnd = router.events.pipe(
      filter(evt => evt instanceof NavigationEnd)
    ) as Observable<NavigationEnd>;

    sharedService.validationResultChangeEmitted$
    .subscribe(result => {
      this.validationResult = result;
      this.generatePageValidations(this.validationResult, this.currentUrl);
    });
  }

  ngOnInit() {
    this.navEnd.subscribe(evt => {
      this.currentUrl = evt.url;
      this.generatePageValidations(this.validationResult, this.currentUrl);
    });
  }

  onCloseErrors() {
    this.validationResult.federalErrors = this.validationResult.federalErrors.filter(item => item.url !== this.currentUrl);
    this.validationResult.stateErrors = this.validationResult.stateErrors.filter(item => item.url !== this.currentUrl);
    this.generatePageValidations(this.validationResult, this.currentUrl);
  }

  onCloseWarnings() {
    this.validationResult.federalWarnings = this.validationResult.federalWarnings.filter(item => item.url !== this.currentUrl);
    this.validationResult.stateWarnings = this.validationResult.stateWarnings.filter(item => item.url !== this.currentUrl);
    this.generatePageValidations(this.validationResult, this.currentUrl);
  }

  private generatePageValidations(result: ValidationResult, url: string ): void {
    let errors: ValidationItem[] = [];
    let warnings: ValidationItem[] = [];

    if (url && result) {
      errors = errors.concat(result.federalErrors.filter(item => {
        return item.url === url;
      }));
      errors = errors.concat(result.stateErrors.filter(item => {
        return item.url === url;
      }));

      warnings = warnings.concat(result.federalWarnings.filter(item => {
        return item.url === url;
      }));
      warnings = warnings.concat(result.stateWarnings.filter(item => {
        return item.url === url;
      }));
    }

    this.pageErrors = errors;
    this.pageWarnings = warnings;

  }

}
