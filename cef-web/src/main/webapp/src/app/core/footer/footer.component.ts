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
import {ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {UserContextService} from 'src/app/core/services/user-context.service';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

    @ViewChild('cdxHandoffForm', { static: true }) private cdxHandoffFormEl: ElementRef;

    ssoToken = '';
    returnUrl = '';
    cdxHandoffLink = '';
    lastModified = '';

    constructor(public userContext: UserContextService, private cd: ChangeDetectorRef) { }

    ngOnInit() {
         // Fetch the Last-Modified header for the current page
                fetch(window.location.href, { method: 'HEAD' })
                    .then(response => {
                        const lastMod = response.headers.get('Last-Modified');
                        if (lastMod) {
                            this.lastModified = lastMod;
                            this.cd.detectChanges();
                        }
                    });
    }

    handoffToCdx(whereTo: string) {
        this.userContext.handoffToCdx(whereTo).subscribe(cdxHandoffInfo => {
            this.cdxHandoffLink = cdxHandoffInfo.split('?')[0];
            const params = cdxHandoffInfo.split('?')[1].split('&');
            for (const param of params) {
                const paramArray = param.split(/=(.+)/);
                if (paramArray[0] === 'ssoToken') {
                    this.ssoToken = paramArray[1].trim();
                } else if (paramArray[0] === 'returnUrl') {
                    this.returnUrl = paramArray[1].trim();
                }
            }
            this.cd.detectChanges();
            this.cdxHandoffFormEl.nativeElement.submit();
        });
    }
}
