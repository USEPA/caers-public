/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { UserContextService } from 'src/app/core/services/user-context.service';
import {Component, OnInit} from '@angular/core';
import { ConfigPropertyService } from 'src/app/core/services/config-property.service';
import { SharedService } from 'src/app/core/services/shared.service';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Component( {
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss']
} )
export class HeaderComponent implements OnInit {

    adminAnnouncementText: SafeHtml;
    adminAnnouncementEnabled = false;

    constructor(
		public userContext: UserContextService,
		private sharedService: SharedService,
		private propertyService: ConfigPropertyService,
		private sanitizer: DomSanitizer) { }

    ngOnInit() {
		
        this.propertyService.retrieveAdminAnnouncementEnabled()
        .subscribe(result => {
            this.adminAnnouncementEnabled = result;

            if (this.adminAnnouncementEnabled) {
                this.propertyService.retrieveAdminAnnouncementText()
                .subscribe(text => {
					// bypassing HTML sanitation is done to preserve styles when drawing the announcements
                    this.adminAnnouncementText = this.sanitizer.bypassSecurityTrustHtml(text.value.trim().length > 0 ? text.value : null);
                });

				this.sharedService.adminBannerChangeEmitted$.subscribe(adminBanner => {
					if (adminBanner) {
						this.adminAnnouncementText = this.sanitizer.bypassSecurityTrustHtml(adminBanner.trim().length > 0 ? adminBanner : null);
					}
				});
            }
        });

    }

    logout() {
        this.userContext.logoutUser();
    }
}
