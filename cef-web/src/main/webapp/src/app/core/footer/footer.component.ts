/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import {ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {UserContextService} from 'src/app/core/services/user-context.service';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

    @ViewChild('cdxHandoffForm', { static: true }) private cdxHandoffFormEl: ElementRef;

    ssoToken = '';
    returnUrl = '';
    cdxHandoffLink = '';
    lastModified = '';

    constructor(public userContext: UserContextService, private cd: ChangeDetectorRef) { }

    ngOnInit() {
         // Fetch the Last-Modified header for the current page
                fetch(window.location.href, { method: 'HEAD' })
                    .then(response => {
                        const lastMod = response.headers.get('Last-Modified');
                        if (lastMod) {
                            this.lastModified = lastMod;
                            this.cd.detectChanges();
                        }
                    });
    }

    handoffToCdx(whereTo: string) {
        this.userContext.handoffToCdx(whereTo).subscribe(cdxHandoffInfo => {
            this.cdxHandoffLink = cdxHandoffInfo.split('?')[0];
            const params = cdxHandoffInfo.split('?')[1].split('&');
            for (const param of params) {
                const paramArray = param.split(/=(.+)/);
                if (paramArray[0] === 'ssoToken') {
                    this.ssoToken = paramArray[1].trim();
                } else if (paramArray[0] === 'returnUrl') {
                    this.returnUrl = paramArray[1].trim();
                }
            }
            this.cd.detectChanges();
            this.cdxHandoffFormEl.nativeElement.submit();
        });
    }
}
