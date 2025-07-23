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
