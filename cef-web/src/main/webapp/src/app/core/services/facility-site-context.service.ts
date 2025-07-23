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
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { FacilitySiteService } from './facility-site.service';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FacilitySiteContextService {
  private facilitySite$: Observable<FacilitySite>;
  private reportId: number;

  constructor(private facilitySiteService: FacilitySiteService) { }

  getFacilitySite(reportId: number): Observable<FacilitySite> {
    if (this.reportId === reportId) {
      return this.facilitySite$;
    } else {
      this.reportId = reportId;
      this.fetchFacilitySite();
      return this.facilitySite$;
    }
  }

  private fetchFacilitySite() {
    this.facilitySite$ = this.facilitySiteService.retrieveForReport(this.reportId);
  }

}
