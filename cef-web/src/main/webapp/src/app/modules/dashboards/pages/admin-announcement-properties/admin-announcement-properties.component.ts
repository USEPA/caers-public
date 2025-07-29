/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit } from '@angular/core';
import { AppProperty } from 'src/app/shared/models/app-property';
import { Validators, FormBuilder } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { AdminPropertyService } from 'src/app/core/services/admin-property.service';
import { SharedService } from 'src/app/core/services/shared.service';

const Admin_AnnouncementText = 'feature.announcement.text';
const Admin_AnnouncementEnabled = 'feature.announcement.enabled';

@Component({
  selector: 'app-admin-announcement-properties',
  templateUrl: './admin-announcement-properties.component.html',
  styleUrls: ['./admin-announcement-properties.component.scss']
})
export class AdminAnnouncementPropertiesComponent implements OnInit {

  announcementEnabled: AppProperty;
  announcementText: AppProperty;

  propertyForm = this.fb.group({
    enabled: [false, Validators.required],
    text: ['', Validators.maxLength(2000)]
  });

  constructor(
      private propertyService: AdminPropertyService,
	  private sharedService: SharedService,
      private fb: FormBuilder,
      private toastr: ToastrService) { }

  ngOnInit() {

    this.propertyService.retrieve(Admin_AnnouncementEnabled)
    .subscribe(result => {

      this.propertyForm.get('enabled').reset(result.value === 'true');
      this.announcementEnabled = result;

    });

    this.propertyService.retrieve(Admin_AnnouncementText)
    .subscribe(result => {

      this.propertyForm.get('text').reset(result.value);
      this.announcementText = result;

    });
  }

  onSubmit() {
    if (!this.propertyForm.valid) {
      this.propertyForm.markAllAsTouched();
    } else {

      const updatedProperties: AppProperty[] = [];
      updatedProperties.push(Object.assign(new AppProperty(),
          {name: Admin_AnnouncementEnabled, value: this.propertyForm.value.enabled}));
      updatedProperties.push(Object.assign(new AppProperty(),
          {name: Admin_AnnouncementText, value: this.propertyForm.value.text}));

      this.propertyService.bulkUpdate(updatedProperties)
      .subscribe(result => {
        this.toastr.success('', 'Properties updated successfully. Changes will appear after a refresh.');
        result.forEach(prop => {
			if (prop.name === Admin_AnnouncementText) {
				this.sharedService.emitAdminBannerChange(prop.value);
			}
		});
      });

    }
  }

}
