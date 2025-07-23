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
