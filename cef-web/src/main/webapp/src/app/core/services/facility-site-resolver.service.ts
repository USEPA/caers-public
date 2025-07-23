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
import { FacilitySiteContextService } from 'src/app/core/services/facility-site-context.service';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, Resolve, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { take, mergeMap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class FacilitySiteResolverService implements Resolve<FacilitySite> {

  constructor(private facilitySiteContext: FacilitySiteContextService, private router: Router) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<FacilitySite> | Observable<never> {
    const reportId = +route.paramMap.get('reportId');

    return this.facilitySiteContext.getFacilitySite(reportId).pipe(
      take(1),
      mergeMap(facilitySite => {
        if (facilitySite) {
          return of(facilitySite);
        } else { // id not found
          this.router.navigate(['/facility']);
          return EMPTY;
        }
      })
    );
  }
}
