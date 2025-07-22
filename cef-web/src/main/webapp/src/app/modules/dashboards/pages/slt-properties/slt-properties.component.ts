/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit } from '@angular/core';
import { Validators, FormBuilder, FormControl } from '@angular/forms';
import { SltPropertyService } from 'src/app/core/services/slt-property.service';
import { ToastrService } from 'ngx-toastr';
import { AppProperty } from 'src/app/shared/models/app-property';
import { BaseCodeLookup } from 'src/app/shared/models/base-code-lookup';
import { User } from 'src/app/shared/models/user';
import { LookupService } from 'src/app/core/services/lookup.service';
import { UserContextService } from 'src/app/core/services/user-context.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CommunicationService } from 'src/app/core/services/communication.service';

const BooleanDataType = 'boolean';
const BooleanTrue = 'true';

@Component({
  selector: 'app-slt-properties',
  templateUrl: './slt-properties.component.html',
  styleUrls: ['./slt-properties.component.scss']
})
export class SltPropertiesComponent implements OnInit {

  properties: AppProperty[];
  user: User;
  agencyDataValues: BaseCodeLookup[];
  programSystemCode: BaseCodeLookup;
  slt: string;

  propertyForm = this.fb.group({});

  constructor(
      private userContextService: UserContextService,
	  private lookupService: LookupService,
      private propertyService: SltPropertyService,
	  private communicationService: CommunicationService,
	  private modalService: NgbModal,
      private fb: FormBuilder,
      private toastr: ToastrService) { }

  ngOnInit() {
    this.userContextService.getUser()
    .subscribe( user => {
          this.user = user;
      });

    this.lookupService.retrieveProgramSystemTypeCode()
    .subscribe(result => {
        this.agencyDataValues = result.sort((a, b) => (a.code > b.code) ? 1 : -1);
      });

    if (this.user.isReviewer()) {
      this.slt = this.user.programSystemCode;
    }

    this.refreshSltPropertyList();

  }

  onAgencySelected() {
    this.slt = this.programSystemCode ? this.programSystemCode.code : null;
    this.refreshSltPropertyList();
  }


  refreshSltPropertyList() {

    if (this.slt) {
      this.propertyService.retrieveAll(this.slt)
        .subscribe(result => {
          result.sort((a, b) => (a.name > b.name) ? 1 : -1);
          result.forEach(prop => {
			if (Object.keys(this.propertyForm.controls).length === 0) {
	            if (prop.datatype !== BooleanDataType) {

				  if (!prop.required) {
					this.propertyForm.addControl(prop.name, new FormControl(prop.value));
				  } else {
	                this.propertyForm.addControl(prop.name, new FormControl(prop.value, { validators: [
	                  Validators.required
	                ]}));
				  }
	            } else {
	              const booleanValue = (prop.value.toLowerCase() === BooleanTrue);
	              this.propertyForm.addControl(prop.name, new FormControl(booleanValue));
	            }
                if (!prop.canEdit) {
                    this.propertyForm.controls[prop.name].disable();
                }
			} else {

				if (prop.datatype !== BooleanDataType) {
				  if (!prop.required) {
					this.propertyForm.setControl(prop.name, new FormControl(prop.value));
				  } else {
				    this.propertyForm.setControl(prop.name, new FormControl(prop.value, { validators: [
	                  Validators.required
	                ]}));
				  }
	            } else {
	              const booleanValue = (prop.value.toLowerCase() === BooleanTrue);
				  this.propertyForm.setControl(prop.name, new FormControl(booleanValue));
	            }
                if (!prop.canEdit) {
                    this.propertyForm.controls[prop.name].disable();
                }
			}

          });
		  this.properties = result;
        });
      }
  }

  onSubmit() {
    if (!this.propertyForm.valid) {
      this.propertyForm.markAllAsTouched();
    } else {

      const updatedProperties: AppProperty[] = [];

	  const reportAttachment: string = "report-attachment-upload";
	  var oneAttachmentUploadEnabled: boolean = false;

      this.properties.forEach(prop => {
		//if name like report attachment, check status
		if (prop.name.includes(reportAttachment)) {
			if ((this.propertyForm.get([prop.name]).value === true )) {
				oneAttachmentUploadEnabled = true;
			}
		}

        if (prop.value !== this.propertyForm.get([prop.name]).value
			|| !prop.value) {
          prop.value = this.propertyForm.get([prop.name]).value;
          updatedProperties.push(prop);
        }
      });

	  if (oneAttachmentUploadEnabled) {
	    this.propertyService.bulkUpdate(updatedProperties, this.slt)
	    .subscribe(result => {
		  this.toastr.success('', 'Properties updated successfully.');
	    });
	  } else {
		this.toastr.error('', 'At least one report attachment upload type must be enabled.');
	  }
      // disable monthly fuel initial year once a value is set
      if (this.propertyForm.controls['slt-feature.monthly-fuel-reporting.initialYear']
          && this.propertyForm.controls['slt-feature.monthly-fuel-reporting.initialYear'].value != null
          && this.propertyForm.controls['slt-feature.monthly-fuel-reporting.initialYear'].enabled) {
        this.propertyForm.controls['slt-feature.monthly-fuel-reporting.initialYear'].disable();
      }
    }
  }
}
