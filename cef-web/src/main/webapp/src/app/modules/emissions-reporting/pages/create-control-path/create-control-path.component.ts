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
import { ActivatedRoute, Router } from '@angular/router';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { BaseReportUrl } from 'src/app/shared/enums/base-report-url';
import { EditControlPathInfoPanelComponent } from '../../components/edit-control-path-info-panel/edit-control-path-info-panel.component';
import { ControlPath } from 'src/app/shared/models/control-path';
import { ControlPathService } from 'src/app/core/services/control-path.service';
import { SharedService } from 'src/app/core/services/shared.service';
import { UtilityService } from 'src/app/core/services/utility.service';

@Component({
  selector: 'app-create-control-path',
  templateUrl: './create-control-path.component.html',
  styleUrls: ['./create-control-path.component.scss']
})
export class CreateControlPathComponent implements OnInit {
  @Input() facilitySite: FacilitySite;
  controlPathUrl: string;
  editInfo = true;

  @ViewChild(EditControlPathInfoPanelComponent, { static: true })
  private controlPathComponent: EditControlPathInfoPanelComponent;

  constructor(private route: ActivatedRoute,
              private controlPathService: ControlPathService,
              private sharedService: SharedService,
              private utilityService: UtilityService,
              private router: Router) { }

  ngOnInit() {
    this.route.data
    .subscribe(data => {
      this.facilitySite = data.facilitySite;
      this.sharedService.emitChange(data.facilitySite);
    });

    this.route.paramMap
    .subscribe(params => {
      this.controlPathUrl = `/facility/${params.get('facilityId')}/report/${params.get('reportId')}/${BaseReportUrl.CONTROL_PATH}`;
    });
  }

  isValid() {
    return this.controlPathComponent.controlPathForm.valid;
  }

  onSubmit() {

    if (!this.isValid()) {
      this.controlPathComponent.controlPathForm.markAllAsTouched();
    } else {
      const saveControlPath = new ControlPath();

      Object.assign(saveControlPath, this.controlPathComponent.controlPathForm.value);
      saveControlPath.facilitySiteId = this.facilitySite.id;

      this.controlPathService.create(saveControlPath)
        .subscribe(() => {
          this.editInfo = false;
          this.sharedService.updateReportStatusAndEmit(this.route);
          this.router.navigate([this.controlPathUrl]);
        });
    }

  }

  onCancel() {
    this.editInfo = false;
    this.router.navigate([this.controlPathUrl]);
  }

  canDeactivate(): Promise<boolean> | boolean {
    if (!this.editInfo || !this.controlPathComponent.controlPathForm.dirty) {
        return true;
    }
    return this.utilityService.canDeactivateModal();
  }

  @HostListener('window:beforeunload', ['$event'])
  beforeunloadHandler(event) {
    if (this.editInfo && this.controlPathComponent.controlPathForm.dirty) {
      event.preventDefault();
      event.returnValue = '';
    }
    return true;
  }

}
