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
import { Component, OnInit, Input } from '@angular/core';
import { ControlPath } from 'src/app/shared/models/control-path';
import { FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators} from '@angular/forms';
import { ControlPathService } from 'src/app/core/services/control-path.service';
import { ActivatedRoute } from '@angular/router';
import { FacilitySite } from 'src/app/shared/models/facility-site';

@Component({
  selector: 'app-edit-control-path-info-panel',
  templateUrl: './edit-control-path-info-panel.component.html',
  styleUrls: ['./edit-control-path-info-panel.component.scss']
})
export class EditControlPathInfoPanelComponent implements OnInit {
  @Input() controlPath: ControlPath;
  pathIds: string[] = [];

    controlPathForm = this.fb.group({
    pathId: ['', [Validators.required, Validators.maxLength(20)]],
    percentControl: ['', [
      Validators.max(100.0),
      Validators.min(1),
      Validators.pattern('^[0-9]{1,4}([\.][0-9]{1})?$')
    ]],
    description: ['', [Validators.maxLength(200)]],
    }, {validators: [
        this.pathIdCheck()
        ]});

  constructor(private fb: FormBuilder, private ctrlPathSvc: ControlPathService, private route: ActivatedRoute) { }

  ngOnInit() {

      this.route.data.subscribe((data: {facilitySite: FacilitySite}) => {
          this.ctrlPathSvc.retrieveForFacilitySite(data.facilitySite.id).subscribe(controlPaths => {
              if (controlPaths) {
                  for (const path of controlPaths) {
                      this.pathIds.push(path.pathId.toLowerCase().trim());
                  }
              }

              // if a control path is being edited then filter that pathId out the list so the validator check doesnt identify it as a duplicate
              if (this.controlPath) {
                this.pathIds = this.pathIds.filter(identifer => identifer.toString().toLowerCase().trim() !== this.controlPath.pathId.toLowerCase().trim());
              }
          });
      });

  }

  ngOnChanges() {
    this.controlPathForm.reset(this.controlPath);
  }

  pathIdCheck(): ValidatorFn {
      return (control: FormGroup): ValidationErrors | null => {
            if (this.pathIds) {
                const pathId: string = control.get('pathId').value.trim().toLowerCase();
                if (control.get('pathId') && this.pathIds.includes(pathId)) {
                  return {duplicatePathId: true};
              }
            }
            return null;
      }
  }


}
