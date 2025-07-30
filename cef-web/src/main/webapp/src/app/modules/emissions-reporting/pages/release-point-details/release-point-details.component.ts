/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import {FacilitySite} from 'src/app/shared/models/facility-site';
import {Process} from 'src/app/shared/models/process';
import {ReleasePoint} from 'src/app/shared/models/release-point';
import {EmissionsProcessService} from 'src/app/core/services/emissions-process.service';
import {ReleasePointService} from 'src/app/core/services/release-point.service';
import {Component, OnInit, ViewChild, Input, HostListener} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {SharedService} from 'src/app/core/services/shared.service';
import {ControlPath} from 'src/app/shared/models/control-path';
import {ControlPathService} from 'src/app/core/services/control-path.service';
import {EditReleasePointPanelComponent} from '../../components/edit-release-point-panel/edit-release-point-panel.component';
import {UserContextService} from 'src/app/core/services/user-context.service';
import {UtilityService} from 'src/app/core/services/utility.service';
import { ReleasePointType } from 'src/app/shared/enums/release-point-type';
import {EntityType} from "../../../../shared/enums/entity-type";

@Component({
    selector: 'app-release-point-details',
    templateUrl: './release-point-details.component.html',
    styleUrls: ['./release-point-details.component.scss']
})
export class ReleasePointDetailsComponent implements OnInit {
    @Input() releasePoint: ReleasePoint;
    processes: Process[];
    controlPaths: ControlPath[];
    parentComponentType = 'releasePointAppt';
    facilitySite: FacilitySite;
	rpType = ReleasePointType;
    entityType = EntityType;

    fugitive = false;
    readOnlyMode = true;
    editInfo = false;

    @ViewChild(EditReleasePointPanelComponent)
    private releasePointComponent: EditReleasePointPanelComponent;

    constructor(
        private releasePointService: ReleasePointService,
        private processService: EmissionsProcessService,
        private controlPathService: ControlPathService,
        private userContextService: UserContextService,
        private utilityService: UtilityService,
        private route: ActivatedRoute,
        private sharedService: SharedService) {
    }

    ngOnInit() {
        this.route.paramMap
            .subscribe(map => {
                this.releasePointService.retrieve(+map.get('releasePointId'))
                    .subscribe(point => {
                        this.releasePoint = point;

                        if (this.releasePoint.typeCode.category === this.rpType.FUGITIVE) {
                            this.fugitive = true;
                        }

                        this.processService.retrieveForReleasePoint(this.releasePoint.id)
                            .subscribe(processes => {
                                this.processes = processes;
                            });

                        this.releasePointService.retrievePrevious(this.releasePoint.id)
                            .subscribe(result => {
                                this.releasePoint.previousReleasePoint = result;
                            });
                    });

                this.controlPathService.retrieveForReleasePoint(+map.get('releasePointId'))
                    .subscribe(controlPaths => {
                        this.controlPaths = controlPaths.sort((a, b) => (a.pathId > b.pathId) ? 1 : -1);
                    });
            });

        // emits the report info to the sidebar
        this.route.data
            .subscribe((data: { facilitySite: FacilitySite }) => {
                this.userContextService.getUser().subscribe(user => {
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

    releasePointType = (): string => {
        return this.fugitive ? this.rpType.FUGITIVE : this.rpType.STACK
    };

    isDiameter = (): boolean => {
        return !!this.releasePoint.stackDiameter;
    }

    updateReleasePoint() {
        if (!this.releasePointComponent.releasePointForm.valid) {
            this.releasePointComponent.releasePointForm.markAllAsTouched();
        } else {
            const updatedReleasePoint = new ReleasePoint();

            Object.assign(updatedReleasePoint, this.releasePointComponent.releasePointForm.value);
            updatedReleasePoint.id = this.releasePoint.id;

            if (this.releasePointComponent.releasePointForm.get('exitGasFlowRate').value === null
                || this.releasePointComponent.releasePointForm.get('exitGasFlowRate').value === '') {
                updatedReleasePoint.exitGasFlowUomCode = null;
            }
            if (this.releasePointComponent.releasePointForm.get('exitGasVelocity').value === null
                || this.releasePointComponent.releasePointForm.get('exitGasVelocity').value === '') {
                updatedReleasePoint.exitGasVelocityUomCode = null;
            }
            if (this.releasePointComponent.releasePointForm.get('fenceLineDistance').value === null
                || this.releasePointComponent.releasePointForm.get('fenceLineDistance').value === '') {
                updatedReleasePoint.fenceLineUomCode = null;
            }
            if (this.releasePointComponent.releasePointForm.get('fugitiveWidth').value === null
                || this.releasePointComponent.releasePointForm.get('fugitiveWidth').value === '') {
                updatedReleasePoint.fugitiveWidthUomCode = null;
            }
            if (this.releasePointComponent.releasePointForm.get('fugitiveLength').value === null
                || this.releasePointComponent.releasePointForm.get('fugitiveLength').value === '') {
                updatedReleasePoint.fugitiveLengthUomCode = null;
            }
            if (this.releasePointComponent.releasePointForm.get('stackWidth').value === null
                || this.releasePointComponent.releasePointForm.get('stackWidth').value === '') {
                updatedReleasePoint.stackWidthUomCode = null;
            }
            if (this.releasePointComponent.releasePointForm.get('stackLength').value === null
                || this.releasePointComponent.releasePointForm.get('stackLength').value === '') {
                updatedReleasePoint.stackLengthUomCode = null;
            }
            if (this.releasePointComponent.releasePointForm.get('fugitiveHeight').value === null
                || this.releasePointComponent.releasePointForm.get('fugitiveHeight').value === '') {
                updatedReleasePoint.fugitiveHeightUomCode = null;
            }

            this.releasePointService.update(updatedReleasePoint)
                .subscribe(result => {

                    if (updatedReleasePoint.typeCode.category === this.rpType.FUGITIVE) {
                        this.fugitive = true;
                    } else {
                        this.fugitive = false;
                    }

                    Object.assign(this.releasePoint, result);
                    this.sharedService.updateReportStatusAndEmit(this.route);
                    this.setEditInfo(false);
                });
        }
    }

    canDeactivate(): Promise<boolean> | boolean {
    if (!this.editInfo || (this.releasePointComponent !== undefined && !this.releasePointComponent.releasePointForm.dirty)) {
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
