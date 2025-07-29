/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import {Component, OnInit} from '@angular/core';
import {ConfigPropertyService} from 'src/app/core/services/config-property.service';
import {UserContextService} from "src/app/core/services/user-context.service";
import {User} from "src/app/shared/models/user";

@Component({
  selector: 'app-horizontal-nav',
  templateUrl: './horizontal-nav.component.html',
  styleUrls: ['./horizontal-nav.component.scss']
})
export class HorizontalNavComponent implements OnInit {

  active = 1;
  isCollapsed = true;

  user: User;
  monthlyReportingEnabled = false;

  constructor(
	private userContext: UserContextService,
	private propertyService: ConfigPropertyService) { }

  ngOnInit(): void {

    this.userContext.getUser()
       .subscribe(result => {
          this.user = result;
		  
		  if (this.user?.isReviewer()) {
			  this.propertyService.retrieveSltMonthlyFuelReportingEnabled(this.user.programSystemCode)
			    .subscribe(result => {
			      this.monthlyReportingEnabled = result;
			    });
		  }

       });
  }

}
