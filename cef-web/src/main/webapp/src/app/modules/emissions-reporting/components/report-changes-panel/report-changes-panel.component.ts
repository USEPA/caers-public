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
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, UrlTree } from '@angular/router';
import { EmissionsReportingService } from 'src/app/core/services/emissions-reporting.service';
import { SharedService } from 'src/app/core/services/shared.service';
import { BaseReportUrl } from 'src/app/shared/enums/base-report-url';
import { EntityType } from 'src/app/shared/enums/entity-type';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { ReportChange } from 'src/app/shared/models/report-change';
import { ValidationDetail } from 'src/app/shared/models/validation-detail';

@Component({
  selector: 'app-report-changes-panel',
  templateUrl: './report-changes-panel.component.html',
  styleUrls: ['./report-changes-panel.component.scss']
})
export class ReportChangesPanelComponent implements OnInit {

  reportChanges: ReportChange[] = [];

  constructor(
      private route: ActivatedRoute,
      private router: Router,
      private sharedService: SharedService,
      private emissionsReportingService: EmissionsReportingService) { }

  ngOnInit(): void {
    
    this.route.data.subscribe((data: { facilitySite: FacilitySite }) => {

          this.emissionsReportingService.getReportChanges(data.facilitySite.emissionsReport.id)
              .subscribe(changes => {

                  this.reportChanges = changes;

                  this.reportChanges.forEach(item => {
                    item.url = this.generateUrl(item.details);
                  });
              });
      });
  }

  generateUrl(detail: ValidationDetail): string {

    let tree: UrlTree;
    if (detail) {
      if (EntityType.EMISSION === detail.type) {

        const period = detail.parents.find(p => p.type === EntityType.REPORTING_PERIOD);
        if (period) {
          tree = this.router.createUrlTree([
              BaseReportUrl.REPORTING_PERIOD,
              period.id,
              BaseReportUrl.EMISSION,
              detail.id
            ], {relativeTo: this.route.parent});
        }
      } else if (EntityType.EMISSIONS_PROCESS === detail.type) {

        tree = this.router.createUrlTree(
          [BaseReportUrl.EMISSIONS_PROCESS, detail.id],
          {relativeTo: this.route.parent});

      } else if (EntityType.EMISSIONS_REPORT === detail.type) {

        tree = this.router.createUrlTree(
          [BaseReportUrl.REPORT_SUMMARY],
          {relativeTo: this.route.parent});

      } else if (EntityType.EMISSIONS_UNIT === detail.type) {

        tree = this.router.createUrlTree(
          [BaseReportUrl.EMISSIONS_UNIT, detail.id],
          {relativeTo: this.route.parent});

      } else if (EntityType.FACILITY_SITE === detail.type) {

        tree = this.router.createUrlTree(
          [BaseReportUrl.FACILITY_INFO],
          {relativeTo: this.route.parent});

      } else if (EntityType.CONTROL_PATH === detail.type) {

        tree = this.router.createUrlTree(
          [BaseReportUrl.CONTROL_PATH, detail.id],
          {relativeTo: this.route.parent});

      } else if (EntityType.CONTROL === detail.type) {

        tree = this.router.createUrlTree(
          [BaseReportUrl.CONTROL_DEVICE, detail.id],
          {relativeTo: this.route.parent});

      } else if (EntityType.RELEASE_POINT === detail.type) {

        tree = this.router.createUrlTree(
          [BaseReportUrl.RELEASE_POINT, detail.id],
          {relativeTo: this.route.parent});

      } else if (EntityType.REPORT_ATTACHMENT === detail.type) {

        tree = this.router.createUrlTree(
          [BaseReportUrl.REPORT_SUMMARY],
          {relativeTo: this.route.parent});

      } else if (EntityType.OPERATING_DETAIL === detail.type
        || EntityType.REPORTING_PERIOD === detail.type) {

        const process = detail.parents.find(p => p.type === EntityType.EMISSIONS_PROCESS);
        if (process) {
          tree = this.router.createUrlTree(
            [BaseReportUrl.EMISSIONS_PROCESS, process.id],
            {relativeTo: this.route.parent});
        }
      }
    }

    return tree ? this.router.serializeUrl(tree) : '.';
  }

}
