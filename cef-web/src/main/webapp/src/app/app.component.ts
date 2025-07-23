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
import { Component } from '@angular/core';
import { SessionTimeoutService } from 'src/app/core/services/session-timeout.service';
import { NavigationEnd, Router } from '@angular/router';

declare const gtag: Function;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  constructor(
    private timeoutService: SessionTimeoutService,
    private router: Router
  ) {

      this.timeoutService.startSessionTimer();
      this.router.events.subscribe((event) => {
          if (event instanceof NavigationEnd) {
              gtag('config', 'UA-32633028-1', { 'page_path': event.urlAfterRedirects });
          }
      })
  }

  keepAlive() {

    // Keep timeout modal from appearing while user is active
    this.timeoutService.refreshSessionTimer();
  }

}
