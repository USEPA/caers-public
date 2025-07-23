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
import { Component, OnInit, ViewChild, HostListener } from '@angular/core';
import { EditEmissionUnitInfoPanelComponent } from 'src/app/modules/emissions-reporting/components/edit-emission-unit-info-panel/edit-emission-unit-info-panel.component';
import { EmissionUnit } from 'src/app/shared/models/emission-unit';
import { EmissionUnitService } from 'src/app/core/services/emission-unit.service';
import { FacilitySiteService } from 'src/app/core/services/facility-site.service';
import { ActivatedRoute, Router } from '@angular/router';
import { SharedService } from 'src/app/core/services/shared.service';
import { UtilityService } from 'src/app/core/services/utility.service';


@Component({
  selector: 'app-create-emissions-unit',
  templateUrl: './create-emissions-unit.component.html',
  styleUrls: ['./create-emissions-unit.component.scss']
})
export class CreateEmissionsUnitComponent implements OnInit {


  @ViewChild(EditEmissionUnitInfoPanelComponent, { static: true })
  private infoComponent: EditEmissionUnitInfoPanelComponent;

  facilitySiteId: number;
  reportId: number;
  editInfo = true;

  constructor(private emissionUnitService: EmissionUnitService,
              private router: Router,
              private route: ActivatedRoute,
              private sharedService: SharedService,
              private utilityService: UtilityService,
              private facilitySiteService: FacilitySiteService) { }

  ngOnInit() {
      this.route.paramMap
      .subscribe(map => {
        this.reportId = parseInt(map.get('reportId'));
      });

      this.facilitySiteService.retrieveForReport(this.reportId)
      .subscribe(result => {
        this.facilitySiteId = result.id;
      });

      this.route.data
      .subscribe(data => {
        this.sharedService.emitChange(data.facilitySite);
      });
  }

  isValid() {
    return this.infoComponent.emissionUnitForm.valid;
  }

  onSubmit() {
        if (!this.isValid()) {
          this.infoComponent.emissionUnitForm.markAllAsTouched();
        }
        else {
          const emissionUnit = new EmissionUnit();

          Object.assign(emissionUnit, this.infoComponent.emissionUnitForm.value);
          emissionUnit.facilitySiteId = this.facilitySiteId;
          emissionUnit.unitIdentifier = this.infoComponent.emissionUnitForm.controls.unitIdentifier.value.trim();

          this.emissionUnitService.create(emissionUnit)
          .subscribe(() => {
            this.editInfo = false;
            this.sharedService.updateReportStatusAndEmit(this.route);
            this.route.data.subscribe(data => {
              this.router.navigate(['../..'], { relativeTo: this.route });
            })
          });
        }
  }

  onCancel() {
    this.editInfo = false;
    this.router.navigate(['../..'], { relativeTo: this.route });
  }

  canDeactivate(): Promise<boolean> | boolean {
    // Allow synchronous navigation (`true`) if both forms are clean
    if (!this.editInfo || !this.infoComponent.emissionUnitForm.dirty) {
        return true;
    }
    // Otherwise ask the user with the dialog service and return its promise which resolves to true or false when the user decides
    return this.utilityService.canDeactivateModal();
  }

  @HostListener('window:beforeunload', ['$event'])
  beforeunloadHandler(event) {
    if (this.editInfo && this.infoComponent.emissionUnitForm.dirty) {
      event.preventDefault();
      event.returnValue = '';
    }
    return true;
  }

}
