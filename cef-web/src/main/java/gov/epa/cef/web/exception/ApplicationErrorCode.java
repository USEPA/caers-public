/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
