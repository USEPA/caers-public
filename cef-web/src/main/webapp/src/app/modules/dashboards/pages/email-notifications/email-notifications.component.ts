/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit, ViewChild, ElementRef, TemplateRef } from '@angular/core';
import { NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Validators, FormBuilder } from '@angular/forms';
import bsCustomFileInput from 'bs-custom-file-input';
import { HttpEvent, HttpEventType } from '@angular/common/http';
import { EMPTY } from 'rxjs';
import { UserService } from 'src/app/core/services/user.service';
import { ConfigPropertyService } from 'src/app/core/services/config-property.service';
import { User } from 'src/app/shared/models/user';
import { Communication } from 'src/app/shared/models/communication';
import { Attachment } from 'src/app/shared/models/attachment';
import { CommunicationService } from 'src/app/core/services/communication.service';
import { ToastrService } from "ngx-toastr";
import { NaicsService } from "src/app/core/services/naics.service";
import { CommunicationHolder } from "src/app/shared/models/communication-holder";

interface PleaseWaitConfig {
    modal: NgbModalRef;
    message: string;
    progress: number;
    serverTicker: number;
    keepAliveTicker: number;
}

interface WorksheetError {
    worksheet: string;
    row: number;
    message: string;
    systemError: boolean;
}

@Component({
  selector: 'app-email-notifications',
  templateUrl: './email-notifications.component.html',
  styleUrls: ['./email-notifications.component.scss']
})
export class EmailNotificationsComponent implements OnInit {

    attachment: Attachment;
    selectedFile: File = null;
    attachmentId: number;
    maxFileSize: number;
    acceptedMIMEtypes: string [];
    user: User;
    bsflags: any;
    disableButton = false;
    communication: Communication;
    communicationHolder: CommunicationHolder = new CommunicationHolder();
    selectedUserRole = null;
    selectedReportStatus = null;
    selectedIndustrySector = null;
    selectedYear = null;

    industrySectors: string[] = [];
    yearValues: number[] = [];

    @ViewChild('fileAttachment', {static: true})
    fileAttachment: ElementRef;

    uploadFile: string;
    uploadFailed: boolean;
    uploadUserErrors: WorksheetError[];
    uploadSystemErrors: WorksheetError[];

    @ViewChild('PleaseWaitModal', {static: true})
    pleaseWaitTemplate: TemplateRef<any>;

    pleaseWait: PleaseWaitConfig;

    attachmentForm = this.fb.group({
        attachment: [null],
        subject: [null, [Validators.maxLength(255), Validators.required]],
        content: [null, [Validators.required]],
    });

    constructor(private fb: FormBuilder,
                private userService: UserService,
                private propertyService: ConfigPropertyService,
                private communicationService: CommunicationService,
                private naicsService: NaicsService,
                private modalService: NgbModal,
                private toastr: ToastrService) {

        this.bsflags = {
            showSystemErrors: false,
            showUserErrors: true
        };
    }

    ngOnInit() {
        bsCustomFileInput.init('#file-attachment');

        this.propertyService.retrieveReportAttachmentMaxSize()
            .subscribe(result => {
                this.maxFileSize = +(result.value);
            });

        this.acceptedMIMEtypes = [
            'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
            'text/plain',
            'application/pdf',
            'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
            'text/csv'
        ];

        this.userService.getCurrentUser().subscribe( user => {
            this.user = user;
        });

        this.naicsService.retrieveAllNaicsIndustries()
            .subscribe( result => {
                this.industrySectors = [...new Set(result.map(item => item.industry))].filter(item => item != null).sort((a, b) => a.localeCompare(b));
        });

        this.initializeYearDropdown();
    }

    isValid() {
        return this.attachmentForm.valid;
    }

    onStatusSelected(status: string) {
        this.selectedReportStatus = (status.length == 0) ? null : status;
    }

    onRoleSelected(role: string) {
        this.selectedUserRole = (role.length == 0) ? null : role;
    }

    onIndustrySelected(industry: string) {
        this.selectedIndustrySector = (industry.length == 0) ? null : industry;
    }

    onYearSelected(year: string) {
        this.selectedYear = (year.length == 0) ? null : Number(year);
    }

    initializeYearDropdown() {
        const currentYear = new Date().getFullYear();

        this.yearValues.push(currentYear);
        this.selectedYear = currentYear;
        this.yearValues.push(currentYear-1);
        this.yearValues.push(currentYear-2);
    }

    onSubmit() {
        this.communication = new Communication();
        Object.assign(this.communication, this.attachmentForm.value);

        this.constructCommunicationHolder();

        if (!this.selectedFile && this.user.isReviewer() && this.isValid()) {
            this.communicationService.sendEmailNotification(this.communicationHolder)
                .subscribe(resp => {
                    this.toastr.success('', 'Email notification has been sent.');
                }, error => {
                    this.toastr.error('', 'Email notification could not be sent.');
                });

        } else {

            if (!this.isValid()) {
                this.attachmentForm.markAllAsTouched();
                this.attachmentForm.controls.attachment.markAsDirty();

            } else if (this.selectedFile) {

                this.pleaseWait = {
                    keepAliveTicker: null,
                    serverTicker: null,
                    modal: this.modalService.open(this.pleaseWaitTemplate, {
                        backdrop: 'static',
                        size: 'lg'
                    }),
                    message: '',
                    progress: 0
                };

                this.uploadFailed = false;
                this.uploadFile = this.selectedFile.name;

                this.constructCommunicationHolder();

                this.communicationService.uploadAttachment(
                    this.communicationHolder, this.selectedFile)
                    .subscribe(respEvent => {
                            this.onUploadEvent(respEvent);
                            if (respEvent['body']) {
                                this.toastr.success('', 'Email notification has been sent.');
                            }
                        }, errorResp => {
                            this.onUploadError(errorResp);
                            this.toastr.error('', 'Email notification could not be sent.');
                        }
                    );
            }
        }
    }

    onUploadEvent(event: HttpEvent<any>) {

        switch (event.type) {

            case HttpEventType.Sent:
                this.pleaseWait.progress = 0;
                this.pleaseWait.message = `Uploading ${this.selectedFile.name}...`;

                this.pleaseWait.keepAliveTicker = setInterval(() => {

                    // keep alive ping
                    this.userService.getCurrentUser();

                }, 60000);

                return EMPTY;

            case HttpEventType.UploadProgress:

                const current = Math.floor(100 * (event.loaded / event.total));
                if (current < 100) {

                    if (current > this.pleaseWait.progress) {

                        this.pleaseWait.progress = current;
                    }

                } else {

                    this.pleaseWait.message = 'The server validating and saving file...';
                    this.pleaseWait.progress = 0;

                    // this could take a minute, 60000 / 100, increments
                    this.pleaseWait.keepAliveTicker = setInterval(() => {

                        if (this.pleaseWait.progress < 100) {

                            this.pleaseWait.progress += 1;
                        }

                    }, 600);

                }

                return EMPTY;

            case HttpEventType.DownloadProgress:

                this.pleaseWait.progress = 100;
                this.pleaseWait.message = 'Receiving response from server...';
                return EMPTY;

            case HttpEventType.ResponseHeader:
                return EMPTY;

            case HttpEventType.Response:

                // 200 - Success
                if(event['body']){
                    this.communication.attachmentId = event['body']['id'];
                }
                this.onUploadComplete();

            default:
        }
    }

    onUploadError(resp) {

        // we got an error response

        this.onUploadComplete();

        this.uploadUserErrors = [];
        this.uploadSystemErrors = [];

        for (let sheetError of resp.error.errors) {

            if (sheetError.systemError) {

                this.uploadSystemErrors.push(sheetError);

            } else {

                this.uploadUserErrors.push(sheetError);
            }
        }

        this.bsflags.showUserErrors = true;

        // if no user errors, show system errors
        this.bsflags.showSystemErrors = this.uploadUserErrors.length === 0;

        this.uploadFailed = true;

        // deletes selected file from the input
        this.fileAttachment.nativeElement.value = '';
        this.selectedFile = null;
        this.disableButton = true;
    }

    onUploadComplete() {

        this.pleaseWait.modal.dismiss();

        clearInterval(this.pleaseWait.serverTicker);
        this.pleaseWait.serverTicker = null;

        clearInterval(this.pleaseWait.keepAliveTicker);
        this.pleaseWait.keepAliveTicker = null;
    }

    onFileChanged(event: Event) {
        let file = (<HTMLInputElement>event.target).files;
        this.bsflags.showUserErrors = false;
        this.bsflags.showSystemErrors = false;
        this.uploadUserErrors = [];
        this.uploadSystemErrors = [];

        this.selectedFile = file.length ? file.item(0) : null;

        this.disableButton = false;
        if (this.selectedFile !== null && !this.user.isReviewer()) {
            this.attachmentForm.controls.attachment.setErrors(null);
        }

        if (file.item(0)) {
            this.selectedFile = file.item(0);
            this.disableButton = true;

            let fileSize = this.checkFileSize(this.selectedFile);
            let fileNameLength = this.checkFileNameLength(this.selectedFile.name);
            let fileType = this.checkFileFormat(this.selectedFile);

            if (fileNameLength || fileSize || fileType) {

                this.bsflags.showUserErrors = true;
                this.uploadFailed = true;

                this.selectedFile = null;
            } else {
                this.disableButton = false;
            }
        }

    }

    checkFileNameLength(fileName: string) {
        let nameLength = fileName.length;
        const maxLength = 255;

        if (nameLength > maxLength) {
            this.uploadUserErrors.push({ worksheet: fileName, row: null,
                message: 'File name "' + fileName + '" exceeds the maximum file name length of ' + maxLength + ' characters.',
                systemError: false});

            return true;
        }

        return false;
    }

    checkFileSize(file) {
        const maxSize = (this.maxFileSize * 1048576); // MB from config properties convert to Bytes
        let fileSize = file.size; // Bytes
        //1048576 byte = 1 MB, 1024 byte = 1 KB

        if (fileSize > maxSize) {
            this.uploadUserErrors.push({ worksheet: file.name, row: null,
                message: 'The selected file size exceeds the maximum allowable upload size '  + this.maxFileSize + ' MB',
                systemError: false});

            return true;
        }

        return false;
    }

    checkFileFormat(file) {
        let acceptedFormat = false;

        for (const format of this.acceptedMIMEtypes) {
            if (file.type === format || file.name.substring(file.name.indexOf('.')) === ".csv") {
                acceptedFormat = true;
                break;
            }
        }

        if (!acceptedFormat) {
            this.uploadUserErrors.push({ worksheet: file.name, row: null,
                message: 'The file MIME type "' +  file.type + '" with extension "' +
                    file.name.substring(file.name.indexOf('.'), file.name.length + 1)
                    + '" is not in an accepted file format.',
                systemError: false});

            return true;
        }

        return false;
    }

    constructCommunicationHolder() {
        this.communicationHolder.communication = this.communication;
        this.communicationHolder.reportStatus = this.selectedReportStatus;
        this.communicationHolder.userRole = this.selectedUserRole;
        this.communicationHolder.industrySector = this.selectedIndustrySector;
        this.communicationHolder.reportYear = this.selectedYear;
    }
}
