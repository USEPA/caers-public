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
import { Component, OnInit, Input, ViewChild, ElementRef, TemplateRef } from '@angular/core';
import { NgbActiveModal, NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
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
  selector: 'app-email-notification-modal',
  templateUrl: './email-notification-modal.component.html',
  styleUrls: ['./email-notification-modal.component.scss']
})
export class EmailNotificationModalComponent implements OnInit {

  @Input() title: string;
  @Input() message: string;
  @Input() cancelButtonText = 'Cancel';
  @Input() confirmButtonText = 'OK';
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

  constructor(public activeModal: NgbActiveModal,
              private fb: FormBuilder,
              private userService: UserService,
              private propertyService: ConfigPropertyService,
			  private communicationService: CommunicationService,
              private modalService: NgbModal) {

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

  }

  onClose() {
    this.activeModal.dismiss();
  }

  isValid() {
    return this.attachmentForm.valid;
  }

  onSubmit() {
	this.communication = new Communication();
	Object.assign(this.communication, this.attachmentForm.value);

    this.communicationHolder.communication = this.communication;

    if (!this.selectedFile && this.user.isReviewer() && this.isValid()) {
		this.communicationService.sendEmailNotification(this.communicationHolder)
			.subscribe(resp => {
	          });
      this.activeModal.close(this.communication);

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

        const attachment = new Attachment();
        Object.assign(attachment, this.attachmentForm.value);

        this.communicationHolder.communication = this.communication;
        this.communicationService.uploadAttachment(
          this.communicationHolder, this.selectedFile)
          .subscribe(respEvent =>
            this.onUploadEvent(respEvent),
            errorResp => this.onUploadError(errorResp)
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
                this.activeModal.close(this.communication);

            default:

                console.log('Unknown event type', event);
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
        message: 'The selected file size exceeds maximum allowable upload size '  + this.maxFileSize + ' MB',
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

}
