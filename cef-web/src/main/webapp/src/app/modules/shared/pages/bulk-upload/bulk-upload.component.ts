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
