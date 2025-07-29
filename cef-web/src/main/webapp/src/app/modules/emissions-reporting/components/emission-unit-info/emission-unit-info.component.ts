/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { EmissionUnit } from 'src/app/shared/models/emission-unit';
import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { EmissionUnitService } from 'src/app/core/services/emission-unit.service';
import { EditEmissionUnitInfoPanelComponent } from 'src/app/modules/emissions-reporting/components/edit-emission-unit-info-panel/edit-emission-unit-info-panel.component';
import { ActivatedRoute } from '@angular/router';
import { SharedService } from 'src/app/core/services/shared.service';
import { ReportStatus } from 'src/app/shared/enums/report-status';
import {EntityType} from "../../../../shared/enums/entity-type";
import {ConfigPropertyService} from "../../../../core/services/config-property.service";
import {FacilitySite} from "../../../../shared/models/facility-site";
import {forkJoin} from "rxjs";

@Component({
  selector: 'app-emission-unit-info',
  templateUrl: './emission-unit-info.component.html',
  styleUrls: ['./emission-unit-info.component.scss']
})
export class EmissionUnitInfoComponent implements OnInit {
  @Input() emissionsUnit: EmissionUnit;
  @Input() facilitySiteId: number;
  @Input() emissionsReport: ReportStatus;
  @Input() readOnlyMode: boolean;
  reportingYear: number;
  editInfo = false;
  unitId: number;
  monthlyReportingEnabled: boolean;

  @ViewChild(EditEmissionUnitInfoPanelComponent)
  infoComponent: EditEmissionUnitInfoPanelComponent;

  entityType = EntityType;

  constructor(private route: ActivatedRoute,
              private unitService: EmissionUnitService,
              private propertyService: ConfigPropertyService,
              private sharedService: SharedService) { }

  ngOnInit() {
      this.route.paramMap
      .subscribe(map => {
        this.unitId = parseInt(map.get('unitId'));
      });

      this.route.data
          .subscribe((data: { facilitySite: FacilitySite }) => {
              this.reportingYear = data.facilitySite.emissionsReport.year;
              forkJoin({
                  monthlyReportingEnabled: this.propertyService.retrieveSltMonthlyFuelReportingEnabled(data.facilitySite?.programSystemCode?.code),
                  monthlyReportingInitialYear: this.propertyService.retrieveSltMonthlyFuelReportingInitialYear(data.facilitySite?.programSystemCode?.code)
              })
              .subscribe(({monthlyReportingEnabled, monthlyReportingInitialYear}) => {
                  this.monthlyReportingEnabled = monthlyReportingEnabled && (monthlyReportingInitialYear == null || data.facilitySite?.emissionsReport?.year >= monthlyReportingInitialYear);
              });
          });
  }

  setEditInfo(value: boolean) {
    this.editInfo = value;
  }

  updateUnit() {
    if (!this.infoComponent.emissionUnitForm.valid) {
      this.infoComponent.emissionUnitForm.markAllAsTouched();
    } else {
      const updatedUnit = new EmissionUnit();

      Object.assign(updatedUnit, this.infoComponent.emissionUnitForm.value);
      updatedUnit.facilitySiteId = this.facilitySiteId;
      updatedUnit.id = this.unitId;

      this.unitService.update(updatedUnit)
      .subscribe(result => {
        Object.assign(this.emissionsUnit, result);
        this.sharedService.updateReportStatusAndEmit(this.route);
        this.sharedService.emitSideNavItemChangeNoRouteChange(this.facilitySiteId);
        this.setEditInfo(false);
      });
    }
  }

}
