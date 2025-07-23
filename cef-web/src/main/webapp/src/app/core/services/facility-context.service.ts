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
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MasterFacilityRecord } from 'src/app/shared/models/master-facility-record';
import { MasterFacilityRecordService } from 'src/app/core/services/master-facility-record.service';

@Injectable({
  providedIn: 'root'
})
export class FacilityContextService {
  private facility$: Observable<MasterFacilityRecord>;
  private recordId: number;

  constructor(private mfrService: MasterFacilityRecordService) {
  }

  getFacility(recordId: number): Observable<MasterFacilityRecord> {
    if (this.recordId === recordId) {
      return this.facility$;
    } else {
      this.recordId = recordId;
      this.fetchFacility();
      return this.facility$;
    }
  }

  private fetchFacility() {
    this.facility$ = this.mfrService.getRecord(this.recordId);
  }
}
