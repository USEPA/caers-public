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
import { AppRole } from 'src/app/shared/enums/app-role';

export class User {
  cdxUserId: string;
  email: string;
  firstName: string;
  fullName: string;
  lastName: string;
  role: AppRole;
  userRoleId: number;
  programSystemCode: string;

  // these methods can be used for easier role checking
  public canPrepare() {
    return this.isPreparer() || this.isNeiCertifier();
  }

  public canReview() {
    return this.isReviewer() || this.isAdmin();
  }

  public isAdmin() {
    return AppRole.CAER_ADMIN === this.role;
  }

  public isNeiCertifier() {
    return AppRole.NEI_CERTIFIER === this.role;
  }

  public isPreparer() {
    return AppRole.PREPARER === this.role;
  }

  public isReviewer() {
    return AppRole.REVIEWER === this.role;
  }
}
