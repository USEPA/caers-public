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
import { UserService } from 'src/app/core/services/user.service';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserContextService {
  user: User;

  constructor(private userService: UserService) {
    this.loadUser();
  }

  loadUser(): void {
    this.userService.getCurrentUser()
    .subscribe(currentUser => this.user = currentUser);
  }

  handoffToCdx(whereTo): Observable<any> {
      return this.userService.initHandoffToCdx(whereTo);
  }

  logoutUser(): void {

    return this.userService.logoutUser();
  }

  getUser(): Observable<User> {
    if (this.user) {
        return of(this.user);
    } else {
        return this.userService?.getCurrentUser();
    }
  }
}
