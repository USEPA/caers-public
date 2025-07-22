/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
