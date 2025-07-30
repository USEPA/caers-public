/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
