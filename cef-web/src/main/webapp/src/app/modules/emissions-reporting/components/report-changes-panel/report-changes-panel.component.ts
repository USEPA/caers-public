/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
