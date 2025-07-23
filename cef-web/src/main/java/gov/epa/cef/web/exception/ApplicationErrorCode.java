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
package gov.epa.cef.web.exception;

/**
 * @author dfladung
 */
public enum ApplicationErrorCode {

  //This file was copied. Many of these may be unused

    //Register
    E_UNKNOWN_USER, E_INVALID_CREDENTIAL, E_ACCESS_DENIED, E_INVALID_TOKEN, E_TOKEN_EXPIRED, E_AUTH_METHOD, E_USER_ALREADY_EXISTS,
    E_INSUFFICIENT_PRIVILEGES, E_WEAK_PASSWORD, E_INVALID_ARGUMENT, E_INVALID_ANSWER_RESET_CODE, E_MAX_NUMBER_OF_RESET_ATTEMPTS_REACHED,
    E_ANSWERS_ALREADY_EXIST, E_ROLE_ALREADY_EXISTS, E_WRONG_USERID, E_REACHED_MAX_NUMBER_OF_ATTEMPTS, E_WRONG_ANSWER,
    E_DUPLICATE_ASSOCIATION, E_INVALID_SIGNATURE, E_INTERNAL_ERROR,
    //RegisterAuthErrorCode
    E_WRONG_ID_PASSWORD, E_ACCOUNT_LOCKED, E_ACCOUNT_EXPIRED,
    // OECA
    E_MESSAGING, E_REMOTE_SERVICE_ERROR, E_VERIFICATION, E_INELIGIBLE, E_SECURITY, E_VALIDATION,
    E_INVALID_PERMISSION, E_PERMISSION_ALREADY_EXISTS, E_INVALID_REQUEST, E_INVALID_REQUEST_STATUS, E_REQUEST_ALREADY_EXISTS,

    E_AUTHENTICATION, E_AUTHORIZATION, E_PERSISTENCE, E_NOT_FOUND, E_ENCRYPTION, E_COMMENT_ALREADY_EXISTS,

    // Calculation
    E_CALC_MISSING_VARIABLE,

    E_AUTHENTICATION_ERROR
}
