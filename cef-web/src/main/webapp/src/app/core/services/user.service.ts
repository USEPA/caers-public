/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
