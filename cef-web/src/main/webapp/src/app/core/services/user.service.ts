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
import { User } from 'src/app/shared/models/user';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserToken } from 'src/app/shared/models/user-token';
import { HttpHeaders } from "@angular/common/http";
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userUrl = 'api/user';  // URL to web api

  constructor(private http: HttpClient) { }

    /** GET the current user from the server */
  getCurrentUser(): Observable<User> {
    const url = `${this.userUrl}/me`;
    // retrieve user and then create a User class from it so functions work
    return this.http.get<User>(url).pipe(
      map(user => Object.assign(new User(), user))
    );
  }

  /** GET the current user new NAAS token from the server */
  getCurrentUserNaasToken(): Observable<UserToken> {
    const url = `${this.userUrl}/token`;
    return this.http.get<UserToken>(url);
  }

  /** Initiate CDX Handoff for a user */
  initHandoffToCdx(whereTo): Observable<any> {
    const url = `J2AHandoff?URL=${whereTo}`;
      return this.http.post(url,'', {responseType: 'text'});
  }

  /** Logout user from CAER app and redirect to next hop logout  */
  logoutUser() : void {

      this.http.post("logout", '', {responseType: 'text'})
          .subscribe((nextHop) => {

          window.location.href = nextHop;
      });
  }
}
