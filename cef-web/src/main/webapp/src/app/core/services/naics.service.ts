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
import { HttpClient } from '@angular/common/http';
import {NaicsIndustry} from "src/app/shared/models/naics-industry";

@Injectable({
  providedIn: 'root'
})
export class NaicsService {

  private baseUrl = 'api/naics';  // URL to web api

  constructor(private http: HttpClient) { }

  retrieveAllNaicsIndustries(): Observable<NaicsIndustry[]> {
    const url = `${this.baseUrl}/industries`;
    return this.http.get<NaicsIndustry[]>(url);
  }

}
