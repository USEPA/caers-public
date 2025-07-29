/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EmissionsReportingService } from 'src/app/core/services/emissions-reporting.service';
import bsCustomFileInput from 'bs-custom-file-input';
import { ToastrService } from 'ngx-toastr';
import {EmissionsReport} from 'src/app/shared/models/emissions-report';

@Component({
  selector: 'app-bulk-upload',
  templateUrl: './bulk-upload.component.html',
  styleUrls: ['./bulk-upload.component.scss']
})
export class BulkUploadComponent implements OnInit {
  uploadFiles: File[] = [];
  uploadStatus: string;

  constructor(
      private emissionsReportingService: EmissionsReportingService,
      private route: ActivatedRoute,
      private toastr: ToastrService) { }

  ngOnInit() {
    bsCustomFileInput.init('#file-json-workbook');
  }

  onFileChanged(event) {
    this.uploadStatus = '';
    this.uploadFiles = [];
    for (const f of event.target.files) {
      this.uploadStatus += `'${f.name}' ready to upload<br/>`;
      this.uploadFiles.push(f);
    }
  }

  onUpload() {
    this.uploadStatus += `<br/> ------------------------------------------------------------------------------------- <br/><br/>`;
    this.uploadStatus += `Uploading process beginning<br/>`;
    for (const f of this.uploadFiles) {
      const fileReader = new FileReader();
      fileReader.readAsText(f, 'UTF-8');

      fileReader.onload = () => {
        try {
          const jfc = JSON.parse(fileReader.result.toString());
          this.emissionsReportingService.uploadReport(jfc, f.name)
            .subscribe(report => this.onUploadComplete(report),
              error => this.onUploadError(error));
        } catch (ex) {
          if (ex instanceof SyntaxError) {
            this.uploadStatus += `** '${f.name}' has an invalid file format.  Only json files may be uploaded.<br/>`;
          } else {
            this.uploadStatus += `** '${f.name}' failed to upload: ${ex.message}<br/>`;
          }
        }
      };
      fileReader.onerror = (error) => {
          this.uploadStatus += `** '${f.name}' failed to be read: ${error}<br/>`;
      };

    }
    this.uploadStatus += `Uploading process complete<br/>`;
    this.toastr.success(`Uploading process complete`);
  }


  onUploadComplete(report: EmissionsReport) {
        this.uploadStatus += `** '${decodeURI(report.fileName)}' successfully uploaded.<br/>`;
  }


  onUploadError(resp) {
    for (const err of resp.error.errors) {
        this.uploadStatus += `** ${err.message}<br/>`;
    }
  }

}
