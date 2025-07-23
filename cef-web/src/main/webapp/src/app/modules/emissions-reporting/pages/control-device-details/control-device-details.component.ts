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
