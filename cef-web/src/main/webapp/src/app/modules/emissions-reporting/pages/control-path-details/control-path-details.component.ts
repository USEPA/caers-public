/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit, Input, ViewChild, HostListener } from '@angular/core';
import { ControlPath } from 'src/app/shared/models/control-path';
import { ControlPathService } from 'src/app/core/services/control-path.service';
import { ActivatedRoute } from '@angular/router';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { EditControlPathInfoPanelComponent } from '../../components/edit-control-path-info-panel/edit-control-path-info-panel.component';
import { SharedService } from 'src/app/core/services/shared.service';
import { ControlAssignment } from 'src/app/shared/models/control-assignment';
import { UserContextService } from 'src/app/core/services/user-context.service';
import { UtilityService } from 'src/app/core/services/utility.service';
import {EntityType} from "../../../../shared/enums/entity-type";

@Component({
  selector: 'app-control-path-details',
  templateUrl: './control-path-details.component.html',
  styleUrls: ['./control-path-details.component.scss']
})
export class ControlPathDetailsComponent implements OnInit {
  @Input() controlPath: ControlPath;
  controlPathAssignments: ControlAssignment[];
  editInfo = false;
  readOnlyMode = true;
  facilitySite: FacilitySite;

  entityType = EntityType;

  @ViewChild(EditControlPathInfoPanelComponent)
  private controlPathComponent: EditControlPathInfoPanelComponent;

  constructor(private controlPathService: ControlPathService,
              private route: ActivatedRoute,
              private userContextService: UserContextService,
              private utilityService: UtilityService,
              private sharedService: SharedService) { }

  ngOnInit() {
    this.route.paramMap
    .subscribe(map => {
      this.controlPathService.retrieve(+map.get('controlPathId'))
      .subscribe(controlPath => {
        this.controlPath = controlPath;
        this.controlPath.pollutants = controlPath.pollutants.sort((a, b) => (a.pollutant.pollutantName > b.pollutant.pollutantName ? 1 : -1));

        this.controlPathService.retrieveAssignmentsForControlPath(this.controlPath.id)
        .subscribe(assignments => {
          this.controlPathAssignments = assignments;
        });
      });
    });

    this.route.data
    .subscribe((data: { facilitySite: FacilitySite }) => {
      this.facilitySite = data.facilitySite;
      this.userContextService.getUser().subscribe( user => {
        if (UtilityService.isNotReadOnlyMode(user, data.facilitySite.emissionsReport.status)) {
          this.readOnlyMode = false;
        }
      });
      this.sharedService.emitChange(data.facilitySite);
    });

  }

  setEditInfo(value: boolean) {
    this.editInfo = value;
  }

    updateControlPath() {
      if (!this.controlPathComponent.controlPathForm.valid) {
        this.controlPathComponent.controlPathForm.markAllAsTouched();
      } else {
        const updatedControlPath = new ControlPath();

        Object.assign(updatedControlPath, this.controlPathComponent.controlPathForm.value);
        updatedControlPath.id = this.controlPath.id;

        this.controlPathService.update(updatedControlPath)
        .subscribe(result => {

          Object.assign(this.controlPath, result);
          this.sharedService.updateReportStatusAndEmit(this.route);
          this.setEditInfo(false);
        });
      }
  }

  canDeactivate(): Promise<boolean> | boolean {
    if ((this.controlPathComponent !== undefined && !this.controlPathComponent.controlPathForm.dirty) || !this.editInfo) {
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
