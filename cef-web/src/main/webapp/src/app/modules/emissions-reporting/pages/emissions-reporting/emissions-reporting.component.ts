/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit, ChangeDetectorRef, AfterViewChecked } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MasterFacilityRecord } from 'src/app/shared/models/master-facility-record';

@Component( {
    selector: 'app-emissions-reporting',
    templateUrl: './emissions-reporting.component.html',
    styleUrls: ['./emissions-reporting.component.scss']
} )
export class EmissionsReportingComponent implements OnInit, AfterViewChecked {
    facility: MasterFacilityRecord;

    constructor( private route: ActivatedRoute, private cdRef: ChangeDetectorRef) { }

    ngOnInit() {
        this.route.data
        .subscribe(( data: { facility: MasterFacilityRecord } ) => {
            this.facility = data.facility;
        });
    }

    // This might be bad coding practices allowing questionable code to work.
    // We should look at removing this and updating our code so it isn't needed at some point.
    ngAfterViewChecked() {
        this.cdRef.detectChanges();
    }

}
