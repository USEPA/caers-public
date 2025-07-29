/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit, Input, ViewChild, HostListener } from '@angular/core';
import { ControlService } from 'src/app/core/services/control.service';
import { ActivatedRoute } from '@angular/router';
import { SharedService } from 'src/app/core/services/shared.service';
import { Control } from 'src/app/shared/models/control';
import { EmissionsReportItem } from 'src/app/shared/models/emissions-report-item';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { EditControlDeviceInfoPanelComponent } from '../../components/edit-control-device-info-panel/edit-control-device-info-panel.component';
import { UserContextService } from 'src/app/core/services/user-context.service';
import { ControlPath } from 'src/app/shared/models/control-path';
import { ControlPathService } from 'src/app/core/services/control-path.service';
import { BaseReportUrl } from 'src/app/shared/enums/base-report-url';
import { UtilityService } from 'src/app/core/services/utility.service';
import {EntityType} from "../../../../shared/enums/entity-type";

@Component({
  selector: 'app-control-device-details',
  templateUrl: './control-device-details.component.html',
  styleUrls: ['./control-device-details.component.scss']
})
export class ControlDeviceDetailsComponent implements OnInit {
  @Input() control: Control;
  emissionsReportItems: EmissionsReportItem[];
  facilitySite: FacilitySite;
  controlPaths: ControlPath[];
  baseUrl: string;
  editInfo = false;
  readOnlyMode = true;

  entityType = EntityType;

  @ViewChild(EditControlDeviceInfoPanelComponent)
  private controlComponent: EditControlDeviceInfoPanelComponent;

  constructor(
    private controlService: ControlService,
    private route: ActivatedRoute,
    private userContextService: UserContextService,
    private sharedService: SharedService,
    private utilityService: UtilityService,
    private controlPathService: ControlPathService) { }

  ngOnInit() {
    this.route.paramMap
    .subscribe(map => {
      this.baseUrl = `/facility/${map.get('facilityId')}/report/${map.get('reportId')}/${BaseReportUrl.CONTROL_PATH}`;
      this.controlService.retrieve(+map.get('controlId'))
      .subscribe(control => {
        this.control = control;
        this.control.pollutants = control.pollutants.sort((a, b) => (a.pollutant.pollutantName > b.pollutant.pollutantName ? 1 : -1));

        this.controlService.retrievePrevious(this.control.id)
        .subscribe(result => {
            this.control.previousControl = result;
        });
      });

      this.controlPathService.retrieveForControlDevice(+map.get('controlId')).subscribe((controlPaths) => {
        this.controlPaths = controlPaths;
      });

      this.controlService.retrieveComponents(+map.get('controlId'))
      .subscribe(emissionsReportItems => {
        this.emissionsReportItems = emissionsReportItems.sort((a, b) => (a.identifier > b.identifier ? 1 : -1));
      });
    });

    // emits the report info to the sidebar
    this.route.data
    .subscribe((data: { facilitySite: FacilitySite }) => {
      this.userContextService.getUser().subscribe( user => {
        if (UtilityService.isNotReadOnlyMode(user, data.facilitySite.emissionsReport.status)) {
          this.readOnlyMode = false;
        }
      });
      this.facilitySite = data.facilitySite;
      this.sharedService.emitChange(data.facilitySite);
    });
  }

  setEditInfo(value: boolean) {
    this.editInfo = value;
  }

  transformDate(value): Date {
    if (value) {
        return new Date(value.year, value.month - 1, value.day);
    }
    return null;
  }

  updateControl() {
    if (!this.controlComponent.controlDeviceForm.valid) {
      this.controlComponent.controlDeviceForm.markAllAsTouched();
    } else {
      const updatedControl = new Control();

      Object.assign(updatedControl, this.controlComponent.controlDeviceForm.value);
      updatedControl.id = this.control.id;
      updatedControl.startDate = this.transformDate(updatedControl.startDate);
      updatedControl.upgradeDate = this.transformDate(updatedControl.upgradeDate);
      updatedControl.endDate = this.transformDate(updatedControl.endDate);

      this.controlService.update(updatedControl)
      .subscribe(result => {

        Object.assign(this.control, result);
        this.sharedService.updateReportStatusAndEmit(this.route);
        this.setEditInfo(false);
      });
    }
  }

  canDeactivate(): Promise<boolean> | boolean {
    if (!this.editInfo || (this.controlComponent !== undefined && !this.controlComponent.controlDeviceForm.dirty)) {
        return true;
    }
    return this.utilityService.canDeactivateModal();
  }

  @HostListener('window:beforeunload', ['$event'])
  beforeunloadHandler(event) {
    if (this.editInfo && this.controlComponent.controlDeviceForm.dirty) {
      event.preventDefault();
      event.returnValue = '';
    }
    return true;
  }
}
