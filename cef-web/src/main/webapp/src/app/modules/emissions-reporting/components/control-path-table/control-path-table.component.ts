/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit, Input } from '@angular/core';
import { ControlAssignment } from 'src/app/shared/models/control-assignment';
import { BaseSortableTable } from 'src/app/shared/components/sortable-table/base-sortable-table';
import { ActivatedRoute } from '@angular/router';
import { BaseReportUrl } from 'src/app/shared/enums/base-report-url';
import { SharedService } from 'src/app/core/services/shared.service';

@Component({
  selector: 'app-control-path-table',
  templateUrl: './control-path-table.component.html',
  styleUrls: ['./control-path-table.component.scss']
})
export class ControlPathTableComponent extends BaseSortableTable implements OnInit {
    @Input() tableData;
    @Input() readOnlyMode: boolean;
    @Input() pathDescription: string;
    baseUrl: string;
    controlPathUrl: string;

  constructor(private route: ActivatedRoute,
              private sharedService: SharedService) {
      super();
      this.sharedService.controlsResultChangeEmitted$.subscribe(controlPaths =>{
        this.tableData = controlPaths;
      });
  }

  ngOnInit() {
    this.route.paramMap
      .subscribe(map => {
        this.baseUrl = `/facility/${map.get('facilityId')}/report/${map.get('reportId')}/${BaseReportUrl.CONTROL_DEVICE}`;
        this.controlPathUrl = `/facility/${map.get('facilityId')}/report/${map.get('reportId')}/${BaseReportUrl.CONTROL_PATH}`;
    });
  }

}
