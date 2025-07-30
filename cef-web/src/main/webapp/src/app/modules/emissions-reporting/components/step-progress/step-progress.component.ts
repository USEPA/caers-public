/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { Location } from '@angular/common';
import { SharedService } from 'src/app/core/services/shared.service';

@Component({
  selector: 'app-step-progress',
  templateUrl: './step-progress.component.html',
  styleUrls: ['./step-progress.component.scss']
})
export class StepProgressComponent implements OnInit {
  facilitySite: FacilitySite;
  hideStepBar: boolean = false;

  constructor(private route: ActivatedRoute,
              private location: Location,
              private router: Router,
              private sharedService: SharedService) { }

  ngOnInit() {
    this.route.data
    .subscribe(data => {

      this.facilitySite = data.facilitySite;
    });

    this.sharedService.hideBoolChangeEmitted$.subscribe((result) => {
      this.hideStepBar = result;
    });
  }

  isOneOf(baseValue: string, testValues: string[]): boolean {
    for (const value of testValues) {
      if (baseValue === value) {
        return true;
      }
    }
    return false;
  }

}
