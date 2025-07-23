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
