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
