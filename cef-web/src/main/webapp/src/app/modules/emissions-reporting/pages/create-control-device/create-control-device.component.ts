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
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { EditControlDeviceInfoPanelComponent } from '../../components/edit-control-device-info-panel/edit-control-device-info-panel.component';
import { ControlService } from 'src/app/core/services/control.service';
import { ActivatedRoute, Router } from '@angular/router';
import { BaseReportUrl } from 'src/app/shared/enums/base-report-url';
import { Control } from 'src/app/shared/models/control';
import { SharedService } from 'src/app/core/services/shared.service';
import { UtilityService } from 'src/app/core/services/utility.service';

@Component({
  selector: 'app-create-control-device',
  templateUrl: './create-control-device.component.html',
  styleUrls: ['./create-control-device.component.scss']
})
export class CreateControlDeviceComponent implements OnInit {
  @Input() facilitySite: FacilitySite;

  controlUrl: string;
  editInfo = true;

  @ViewChild(EditControlDeviceInfoPanelComponent, { static: true })
  private controlDeviceComponent: EditControlDeviceInfoPanelComponent;

  constructor(
    private controlService: ControlService,
    private utilityService: UtilityService,
    private route: ActivatedRoute,
    private router: Router,
    private sharedService: SharedService) { }

  ngOnInit() {

    this.route.data
      .subscribe(data => {
        this.facilitySite = data.facilitySite;
        this.sharedService.emitChange(data.facilitySite);
      });

    this.route.paramMap
      .subscribe(params => {
        this.controlUrl = `/facility/${params.get('facilityId')}/report/${params.get('reportId')}/${BaseReportUrl.CONTROL_DEVICE}`;
      });

  }

  isValid() {
    return this.controlDeviceComponent.controlDeviceForm.valid;
  }

  transformDate(value): Date {
    if (value) {
        return new Date(value.year, value.month - 1, value.day);
    }
    return null;
  }

  onSubmit() {

    if (!this.isValid()) {
      this.controlDeviceComponent.controlDeviceForm.markAllAsTouched();
    } else {
      const saveControlDevice = new Control();

      Object.assign(saveControlDevice, this.controlDeviceComponent.controlDeviceForm.value);
      saveControlDevice.facilitySiteId = this.facilitySite.id;
      saveControlDevice.identifier = this.controlDeviceComponent.controlDeviceForm.controls.identifier.value.trim();
      saveControlDevice.startDate = this.transformDate(saveControlDevice.startDate);
      saveControlDevice.upgradeDate = this.transformDate(saveControlDevice.upgradeDate);
      saveControlDevice.endDate = this.transformDate(saveControlDevice.endDate);

      this.controlService.create(saveControlDevice)
        .subscribe(() => {
          this.editInfo = false;
          this.sharedService.updateReportStatusAndEmit(this.route);
          this.router.navigate([this.controlUrl]);
        });
    }

  }

  onCancel() {
    this.editInfo = false;
    this.router.navigate([this.controlUrl]);
  }

  canDeactivate(): Promise<boolean> | boolean {
    if (!this.editInfo || !this.controlDeviceComponent.controlDeviceForm.dirty) {
        return true;
    }
    return this.utilityService.canDeactivateModal();
  }

  @HostListener('window:beforeunload', ['$event'])
  beforeunloadHandler(event) {
    if (this.editInfo && this.controlDeviceComponent.controlDeviceForm.dirty) {
      event.preventDefault();
      event.returnValue = '';
    }
    return true;
  }
}
