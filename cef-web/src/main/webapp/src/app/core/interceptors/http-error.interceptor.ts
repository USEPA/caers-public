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
import { Inject, Injectable, Injector, NgZone } from '@angular/core';
import { Router } from '@angular/router';
import { HttpRequest, HttpHandler, HttpInterceptor, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, EMPTY, of } from 'rxjs';
import { retryWhen, delay, concatMap } from 'rxjs/operators';
import { UserContextService } from 'src/app/core/services/user-context.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { BaseReportUrl } from 'src/app/shared/enums/base-report-url';

const DELAY_MS: number = 1000;
const MAX_RETRIES: number = 1;

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {

    constructor(private router: Router,
                private modalService: NgbModal,
                @Inject(NgZone) private ngZone: NgZone,
                @Inject(Injector) private injector: Injector,
    ) {
    }

    private get userContext(): UserContextService {
        return this.injector.get(UserContextService);
    }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<any> {
        return next.handle(request)
            .pipe(retryWhen(errors => errors.pipe(
                delay(DELAY_MS),
                concatMap((error, count) => {

                    // console.log("In HttpErrorInterceptor", error, count);

                    if (error.status === 401) {

                        // not authenticated
                        alert(error.error.message);

                        // logout user => send back to CDX
                        this.userContext.logoutUser();

                        return EMPTY;
                    }

                    if (error.status === 403 || error.status === 410) {

                        // don't repeat the alert -
                        // some pages make multiple requests for children of the entity
                        // that causes the first 403/410
                        const url = `/facility?error=${error.status}`;

                        if (this.router.url !== url) {

                            // not authorized or not found
                            alert(error.error.message);

                            this.router.navigateByUrl(url);
                        }

                        return EMPTY;
                    }

                    if (error.status === 500) {

                        this.modalService.dismissAll();
                        this.router.navigateByUrl('/error');

                        return new Observable<HttpResponse<any>>(subscriber => {
                            subscriber.error(error);
                        });
                    }

                    if (
                        error instanceof HttpErrorResponse
                        && (
                            ([502, 503, 504].indexOf(error.status) === -1)
                            || (
                                [502, 503, 504].indexOf(error.status) !== -1
                                && request.urlWithParams.includes(BaseReportUrl.VALIDATION)
                            )
                        )
                    ) {

                        // doesn't make sense to repeat the same bad request
                        // unless (debatable) it's a bad gateway, service unavailable or gateway timeout
                        // and a retry makes sense for the requested resource

                        // let the caller handle the HTTP error
                        return new Observable<HttpResponse<any>>(subscriber => {
                            subscriber.error(error);
                        });
                    }

                    if (count < MAX_RETRIES) {

                        return of(error);
                    }

                    // give up error out...
                    let errorMsg = '';
                    if (error.error instanceof ErrorEvent) {
                        // client side error
                        errorMsg = `Error: ${error.error.message}`;
                    } else {
                        // server side error
                        errorMsg = `Error Code: ${error.status}\nMessage: ${error.message}}`;
                    }

                    return throwError(errorMsg);
                })
            )));
    }

}
