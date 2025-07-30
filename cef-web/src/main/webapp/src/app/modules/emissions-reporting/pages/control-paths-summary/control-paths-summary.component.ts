/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { SharedService } from 'src/app/core/services/shared.service';
import { ControlPathService } from 'src/app/core/services/control-path.service';
import { ControlPath } from 'src/app/shared/models/control-path';

@Component({
  selector: 'app-control-paths-summary',
  templateUrl: './control-paths-summary.component.html',
  styleUrls: ['./control-paths-summary.component.scss']
})
export class ControlPathsSummaryComponent implements OnInit {
  facilitySite: FacilitySite;
  controlPaths: ControlPath[];

  constructor(private route: ActivatedRoute,
              private sharedService: SharedService,
              private controlPathService: ControlPathService) { }

  ngOnInit() {
    this.route.data
    .subscribe((data: { facilitySite: FacilitySite }) => {
      this.facilitySite = data.facilitySite;
      this.sharedService.emitChange(data.facilitySite);
      this.controlPathService.retrieveForFacilitySite(this.facilitySite.id)
      .subscribe(controls => {
        this.controlPaths = controls;
      });
    });
  }

}
