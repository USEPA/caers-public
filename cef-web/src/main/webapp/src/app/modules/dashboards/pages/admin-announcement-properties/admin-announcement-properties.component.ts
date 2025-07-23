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
