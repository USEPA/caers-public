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
import {Component, OnInit, TemplateRef, ViewChild, ElementRef} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import bsCustomFileInput from 'bs-custom-file-input';
import {EmissionsReportingService} from 'src/app/core/services/emissions-reporting.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap/modal/modal-ref';
import {HttpEvent, HttpEventType} from '@angular/common/http';
import {EMPTY} from 'rxjs';
import {UserService} from 'src/app/core/services/user.service';
import { MasterFacilityRecord } from 'src/app/shared/models/master-facility-record';
import { FormBuilder, Validators, ValidatorFn, FormGroup, ValidationErrors } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';

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
    identifier: string;
    message: string;
    systemError: boolean;
}

@Component({
  selector: 'app-report-json-upload',
  templateUrl: './report-json-upload.component.html',
  styleUrls: ['./report-json-upload.component.scss']
})
export class ReportJsonUploadComponent implements OnInit {

  reportingYear: number;
    facility: MasterFacilityRecord;
    selectedFile: File = null;

    bsflags: any;

    @ViewChild('workbookFile', {static: true})
    workbookFile: ElementRef;

    uploadFile: string;
    uploadUserErrors: WorksheetError[];
    uploadSystemErrors: WorksheetError[];
    uploadFailed: boolean;

    @ViewChild('PleaseWaitModal', {static: true})
    pleaseWaitTemplate: TemplateRef<any>;

    pleaseWait: PleaseWaitConfig;


    fileUploadForm = this.fb.group({
        excelFileUpload: ['', Validators.required]
    }, {
        validators: [
            this.checkFileName()]
    });

    constructor(private emissionsReportingService: EmissionsReportingService,
                private userService: UserService,
                private modalService: NgbModal,
                public router: Router,
                private route: ActivatedRoute,
                private fb: FormBuilder,
                private toastr: ToastrService) {

        this.bsflags = {
            showSystemErrors: false,
            showUserErrors: true
        };
    }

    ngOnInit() {

        bsCustomFileInput.init('#file-excel-workbook');

        this.route.data
            .subscribe((data: { facility: MasterFacilityRecord }) => {
                this.facility = data.facility;
            });

        this.route.paramMap
            .subscribe(params => {

                this.reportingYear = +params.get('year');
            });
    }

    onUploadClick() {

        if (this.selectedFile) {

            this.uploadFailed = false;
            this.uploadFile = this.selectedFile.name;

              const fileReader = new FileReader();
              fileReader.readAsText(this.selectedFile, 'UTF-8');
        
              fileReader.onload = () => {
                try {
                  const jfc = JSON.parse(fileReader.result.toString());
                  
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
                  
                  this.emissionsReportingService.createReportFromJsonUpload(this.facility, this.reportingYear, jfc)
                    .subscribe(respEvent => this.onUploadEvent(respEvent),
                        errorResp => this.onUploadError(errorResp));
                } catch (ex) {
                  if (ex instanceof SyntaxError) {
                    this.toastr.error(`'${this.selectedFile.name}' has an invalid file format.  Only json files may be uploaded.`);
                  } else {
                    this.toastr.error(`'${this.selectedFile.name}' failed to upload: ${ex.message}`);
                  }
                }
              };
              fileReader.onerror = (error) => {
                  this.toastr.error(`'${this.selectedFile.name}' failed to be read: ${error}`);
              };
        

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

                    this.pleaseWait.message = 'The server is parsing, validating and saving file...';
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
                this.onUploadComplete();

                const newReport = event.body;

				return this.router.navigate([
                    `/facility/${newReport.masterFacilityRecordId}/report/${newReport.id}/summary/fromUpload`], { state: { prevPage: this.router.url } });

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
        this.workbookFile.nativeElement.value = '';
        this.selectedFile = null;
    }

    onUploadComplete() {

        this.pleaseWait.modal.dismiss();

        clearInterval(this.pleaseWait.serverTicker);
        this.pleaseWait.serverTicker = null;

        clearInterval(this.pleaseWait.keepAliveTicker);
        this.pleaseWait.keepAliveTicker = null;
    }

    onFileChanged(files: FileList) {
        if (this.fileUploadForm.valid) {
            this.selectedFile = files.length ? files.item(0) : null;
        }
    }

    

    // make sure file name is using valid characters
    checkFileName(): ValidatorFn {
        return (control: FormGroup): ValidationErrors | null => {
            const excelFile = control.get('excelFileUpload').value;
            if (excelFile) {
                const fileParts: string[] = excelFile.split('\\');
                if (fileParts.length > 0) {
                    const fileName = fileParts[fileParts.length-1];
                    const regex = /^[a-zA-Z0-9. -_()]+\.(json)$/;
                    if (!regex.test(fileName)) {
                        this.selectedFile = null;
                        return {invalidFileName: true};
                    }
                }
            }
            return null;
        };
    }
}
