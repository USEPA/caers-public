/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { Process } from 'src/app/shared/models/process';
import { EmissionUnit } from 'src/app/shared/models/emission-unit';
import { EmissionUnitService } from 'src/app/core/services/emission-unit.service';
import { EmissionsProcessService } from 'src/app/core/services/emissions-process.service';
import { Component, OnInit, ViewChild, HostListener } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SharedService } from 'src/app/core/services/shared.service';
import { ControlPath } from 'src/app/shared/models/control-path';
import { ControlPathService } from 'src/app/core/services/control-path.service';
import { UserContextService } from 'src/app/core/services/user-context.service';
import { EmissionUnitInfoComponent } from '../../components/emission-unit-info/emission-unit-info.component';
import { UtilityService } from 'src/app/core/services/utility.service';
import {forkJoin} from "rxjs";
import {ConfigPropertyService} from "src/app/core/services/config-property.service";


@Component({
  selector: 'app-emission-unit-dashboard',
  templateUrl: './emission-unit-dashboard.component.html',
  styleUrls: ['./emission-unit-dashboard.component.scss']
})
export class EmissionUnitDashboardComponent implements OnInit {
  emissionsUnit: EmissionUnit;
  processes: Process[];
  controlPaths: ControlPath[];
  facilitySiteId: number;

  readOnlyMode = true;
  semiannualSubmitted = false;
  monthlyReportingEnabled = false;

  @ViewChild(EmissionUnitInfoComponent)
  private infoComponentParent: EmissionUnitInfoComponent;

  constructor(
    private emissionUnitService: EmissionUnitService,
    private processService: EmissionsProcessService,
    private controlPathService: ControlPathService,
    private userContextService: UserContextService,
    private utilityService: UtilityService,
    private route: ActivatedRoute,
    private sharedService: SharedService,
    private propertyService: ConfigPropertyService) { }

  ngOnInit() {
    this.route.paramMap
      .subscribe(map => {
        this.emissionUnitService.retrieve(+map.get('unitId'))
        .subscribe(unit => {
          this.emissionsUnit = unit;
          this.processService.retrieveForEmissionsUnit(this.emissionsUnit.id)
          .subscribe(processes => {
            this.processes = processes;
          });
          this.emissionUnitService.retrievePrevious(this.emissionsUnit.id)
          .subscribe(result => {
              this.emissionsUnit.previousUnit = result;
          });
        });

        this.controlPathService.retrieveForEmissionsUnit(+map.get('unitId'))
        .subscribe(controlPaths => {
          this.controlPaths = controlPaths;
        });

        // get the control paths again if change is emitted, in case a process has been deleted
        // and the contols paths have been removed
        this.sharedService.changeEmitted$
        .subscribe(() => {
          this.controlPathService.retrieveForEmissionsUnit(+map.get('unitId'))
          .subscribe(controlPaths => {
            this.controlPaths = controlPaths;
          });
        } );
    });
    // emits the report info to the sidebar
    this.route.data
    .subscribe((data: { facilitySite: FacilitySite }) => {
      this.facilitySiteId = data.facilitySite.id;
      this.userContextService.getUser().subscribe( user => {
        if (UtilityService.isNotReadOnlyMode(user, data.facilitySite.emissionsReport.status)) {
          this.readOnlyMode = false;
        }
      });

      this.sharedService.emitChange(data.facilitySite);

      forkJoin({
        monthlyReportingEnabled: this.propertyService.retrieveSltMonthlyFuelReportingEnabled(data.facilitySite.programSystemCode?.code),
        monthlyReportingInitialYear: this.propertyService.retrieveSltMonthlyFuelReportingInitialYear(data.facilitySite.programSystemCode?.code)
      })
      .subscribe(({monthlyReportingEnabled, monthlyReportingInitialYear}) => {
        this.monthlyReportingEnabled = monthlyReportingEnabled && (monthlyReportingInitialYear == null || data.facilitySite.emissionsReport?.year >= monthlyReportingInitialYear);

        if (this.monthlyReportingEnabled) {
          if (data.facilitySite.emissionsReport.midYearSubmissionStatus === 'SUBMITTED' || data.facilitySite.emissionsReport.midYearSubmissionStatus === 'APPROVED') {
            this.semiannualSubmitted = true;
          }
        }
      });
    });

  }

  canDeactivate(): Promise<boolean> | boolean {
    if (!this.infoComponentParent.infoComponent?.emissionUnitForm.dirty) {
      this.infoComponentParent.setEditInfo(false);
      return true;
    }

    let result = this.utilityService.canDeactivateModal();
    result.then(data => {
      if (data) {
        this.infoComponentParent.setEditInfo(false);
      }
    });
    return result;
  }

  @HostListener('window:beforeunload', ['$event'])
  beforeunloadHandler(event) {
    if ((this.infoComponentParent.infoComponent !== undefined && this.infoComponentParent.infoComponent.emissionUnitForm.dirty)) {
      event.preventDefault();
      event.returnValue = '';
    }
    return true;
  }
}
