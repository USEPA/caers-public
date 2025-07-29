/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
