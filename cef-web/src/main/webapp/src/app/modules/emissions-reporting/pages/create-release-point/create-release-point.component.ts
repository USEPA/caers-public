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
import { Component, OnInit, ViewChild, Input, HostListener } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EditReleasePointPanelComponent } from '../../components/edit-release-point-panel/edit-release-point-panel.component';
import { ReleasePointService } from 'src/app/core/services/release-point.service';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { ReleasePoint } from 'src/app/shared/models/release-point';
import { BaseReportUrl } from 'src/app/shared/enums/base-report-url';
import { SharedService } from 'src/app/core/services/shared.service';
import { UtilityService } from 'src/app/core/services/utility.service';

@Component({
  selector: 'app-create-release-point',
  templateUrl: './create-release-point.component.html',
  styleUrls: ['./create-release-point.component.scss']
})
export class CreateReleasePointComponent implements OnInit {
  @Input() facilitySite: FacilitySite;

  releaseUrl: string;
  editInfo = true;

  @ViewChild(EditReleasePointPanelComponent, { static: true })
  private releasePointComponent: EditReleasePointPanelComponent;

  constructor(
    private releasePointService: ReleasePointService,
    private route: ActivatedRoute,
    private router: Router,
    private utilityService: UtilityService,
    private sharedService: SharedService) { }

  ngOnInit() {

    this.route.data
    .subscribe(data => {
      this.facilitySite = data.facilitySite;
      this.sharedService.emitChange(data.facilitySite);
    });

    this.route.paramMap
    .subscribe(params => {
      this.releaseUrl = `/facility/${params.get('facilityId')}/report/${params.get('reportId')}/${BaseReportUrl.RELEASE_POINT}`;
    });

  }

  isValid() {
    return this.releasePointComponent.releasePointForm.valid;
  }

  onSubmit() {

    if (!this.isValid()) {
      this.releasePointComponent.releasePointForm.markAllAsTouched();
    } else {
      const saveReleasePoint = new ReleasePoint();

      Object.assign(saveReleasePoint, this.releasePointComponent.releasePointForm.value);
      saveReleasePoint.facilitySiteId = this.facilitySite.id;
      saveReleasePoint.releasePointIdentifier = this.releasePointComponent.releasePointForm.controls.releasePointIdentifier.value.trim();

      if (this.releasePointComponent.releasePointForm.get('exitGasFlowRate').value === null
          || this.releasePointComponent.releasePointForm.get('exitGasFlowRate').value === '') {
        saveReleasePoint.exitGasFlowUomCode = null;
      }
      if (this.releasePointComponent.releasePointForm.get('exitGasVelocity').value === null
          || this.releasePointComponent.releasePointForm.get('exitGasVelocity').value === '') {
        saveReleasePoint.exitGasVelocityUomCode = null;
      }
      if (this.releasePointComponent.releasePointForm.get('fenceLineDistance').value === null
          || this.releasePointComponent.releasePointForm.get('fenceLineDistance').value === '') {
        saveReleasePoint.fenceLineUomCode = null;
      }
      if (this.releasePointComponent.releasePointForm.get('fugitiveWidth').value === null
          || this.releasePointComponent.releasePointForm.get('fugitiveWidth').value === '') {
        saveReleasePoint.fugitiveWidthUomCode = null;
      }
      if (this.releasePointComponent.releasePointForm.get('fugitiveLength').value === null
          || this.releasePointComponent.releasePointForm.get('fugitiveLength').value === '') {
        saveReleasePoint.fugitiveLengthUomCode = null;
      }
      if (this.releasePointComponent.releasePointForm.get('stackWidth').value === null
          || this.releasePointComponent.releasePointForm.get('stackWidth').value === '') {
        saveReleasePoint.stackWidthUomCode = null;
      }
      if (this.releasePointComponent.releasePointForm.get('stackLength').value === null
          || this.releasePointComponent.releasePointForm.get('stackLength').value === '') {
        saveReleasePoint.stackLengthUomCode = null;
      }
      if (this.releasePointComponent.releasePointForm.get('fugitiveHeight').value === null
          || this.releasePointComponent.releasePointForm.get('fugitiveHeight').value === '') {
        saveReleasePoint.fugitiveHeightUomCode = null;
      }

      this.releasePointService.create(saveReleasePoint)
      .subscribe(() => {
        this.editInfo = false;
        this.sharedService.updateReportStatusAndEmit(this.route);
        this.router.navigate([this.releaseUrl]);
      });
    }

  }

  onCancel() {
    this.editInfo = false;
    this.router.navigate([this.releaseUrl]);
  }

  canDeactivate(): Promise<boolean> | boolean {
    if (!this.editInfo || !this.releasePointComponent.releasePointForm.dirty) {
        return true;
    }
    return this.utilityService.canDeactivateModal();
  }

  @HostListener('window:beforeunload', ['$event'])
  beforeunloadHandler(event) {
    if (this.editInfo && this.releasePointComponent.releasePointForm.dirty) {
      event.preventDefault();
      event.returnValue = '';
    }
    return true;
  }

}
