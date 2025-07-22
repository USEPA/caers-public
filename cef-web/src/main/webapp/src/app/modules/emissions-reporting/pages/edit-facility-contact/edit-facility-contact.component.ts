/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit, Input, HostListener } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { LookupService } from 'src/app/core/services/lookup.service';
import { FacilitySiteContact } from 'src/app/shared/models/facility-site-contact';
import { FacilitySite } from 'src/app/shared/models/facility-site';
import { FacilitySiteContactService } from 'src/app/core/services/facility-site-contact.service';
import { SharedService } from 'src/app/core/services/shared.service';
import { ActivatedRoute, Router } from '@angular/router';
import { BaseReportUrl } from 'src/app/shared/enums/base-report-url';
import { BaseCodeLookup } from 'src/app/shared/models/base-code-lookup';
import { FipsStateCode } from 'src/app/shared/models/fips-state-code';
import { FormUtilsService } from 'src/app/core/services/form-utils.service';
import { numberValidator } from 'src/app/modules/shared/directives/number-validator.directive';
import { FipsCounty } from 'src/app/shared/models/fips-county';
import { legacyItemValidator } from 'src/app/modules/shared/directives/legacy-item-validator.directive';
import { UtilityService } from 'src/app/core/services/utility.service';

@Component({
  selector: 'app-edit-facility-contact',
  templateUrl: './edit-facility-contact.component.html',
  styleUrls: ['./edit-facility-contact.component.scss']
})
export class EditFacilityContactComponent implements OnInit {
  @Input() facilityContact: FacilitySiteContact;
  @Input() facilitySite: FacilitySite;
  @Input() createMode = false;
  mailingStreetAddress: string;
  sameAddress = false;

  readOnlyMode = true;
  editInfo = false;

  facilityUrl: string;

  contactForm = this.fb.group({
    type: [null, Validators.required],
    phone: ['', [
      Validators.required,
      Validators.maxLength(10),
      Validators.pattern('^[0-9]{10}$')]],
    phoneExt: ['', [
      Validators.maxLength(5),
      numberValidator()]],
    prefix: ['', Validators.maxLength(15)],
    firstName: ['', [
      Validators.required,
      Validators.maxLength(20)]],
    lastName: ['', [
      Validators.required,
      Validators.maxLength(20)]],
    email: ['', [
      Validators.required,
      Validators.maxLength(255),
      Validators.pattern('^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+[\.][A-Za-z]{2,}$')]],
    streetAddress: ['', [
      Validators.maxLength(100),
      Validators.required]],
    city: ['', [
      Validators.required,
      Validators.maxLength(60)]],
    stateCode: [null, Validators.required],
    postalCode: ['', [
      Validators.required,
      Validators.pattern('^\\d{5}(-\\d{4})?$')]],
    mailingStreetAddress: [''],
    mailingCity: [''],
    mailingStateCode: [null],
    mailingPostalCode: ['', Validators.pattern('^\\d{5}(-\\d{4})?$')],
    countyCode: [null]
  });

  facilityContactType: BaseCodeLookup[];
  fipsStateCode: FipsStateCode[];
  counties: FipsCounty[];

  constructor(
    private contactService: FacilitySiteContactService,
    private lookupService: LookupService,
    private sharedService: SharedService,
    public formUtils: FormUtilsService,
    private utilityService: UtilityService,
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder) { }

  ngOnInit() {

    this.contactForm.get('stateCode').valueChanges
    .subscribe(value => {
      if (value) {
        if (this.contactForm.value.countyCode && value.code && this.contactForm.value.countyCode.fipsStateCode.code !== value.code) {
          this.contactForm.get('countyCode').setValue(null);
        }
        this.lookupService.retrieveCurrentFipsCountiesForState(value.code, this.facilitySite.emissionsReport.year)
        .subscribe(result => {
          this.counties = result;
        });
      } else {
        this.counties = [];
        this.contactForm.get('countyCode').setValue(null);
      }
    });

    this.lookupService.retrieveFacilityContactType()
    .subscribe(result => {
      this.facilityContactType = result;
    });

    this.lookupService.retrieveFipsStateCode()
    .subscribe(result => {
      this.fipsStateCode = result;
    });

    this.route.data
    .subscribe(data => {
      this.facilitySite = data.facilitySite;

      this.createMode = data.create === 'true';

      this.readOnlyMode = !UtilityService.isInProgressStatus(data.facilitySite.emissionsReport.status);

      this.sharedService.emitChange(data.facilitySite);
    });

    this.route.paramMap
    .subscribe(params => {

      if (!this.createMode) {
        this.contactService.retrieve(+params.get('contactId'))
          .subscribe(result => {
            this.facilityContact = result;

            this.contactForm.enable();
            this.contactForm.reset(this.facilityContact);
            this.mailingStreetAddress = this.facilityContact.mailingStreetAddress;
            this.editInfo = true;
        });
      } else {
        this.contactForm.enable();
      }

      this.facilityUrl = `/facility/${params.get('facilityId')}/report/${params.get('reportId')}/${BaseReportUrl.FACILITY_INFO}`;
    });

    this.contactForm.get('countyCode').setValidators([Validators.required, legacyItemValidator(this.facilitySite.emissionsReport.year, 'County', 'name')]);
  }

  setMailAddress() {
    this.sameAddress = !this.sameAddress;
  }

  onSubmit() {
    this.checkMailingAddress();

    if (!this.contactForm.valid) {
      this.contactForm.markAllAsTouched();
    } else {

      const saveContact = new FacilitySiteContact();
      Object.assign(saveContact, this.contactForm.value);

      if (this.createMode) {

        saveContact.facilitySiteId = this.facilitySite.id;

        this.contactService.create(saveContact)
        .subscribe(() => {

          this.createMode = false;
          this.sharedService.updateReportStatusAndEmit(this.route);
          this.router.navigate([this.facilityUrl]);
        });
      } else {

        saveContact.id = this.facilityContact.id;
        this.contactService.update(saveContact)
        .subscribe(() => {

          this.editInfo = false;
          this.sharedService.updateReportStatusAndEmit(this.route);
          this.router.navigate([this.facilityUrl]);
        });
      }
    }
  }

  onCancel() {
    this.createMode = false;
    this.editInfo = false;
    this.router.navigate([this.facilityUrl]);
  }

  checkMailingAddress() {
    if (this.contactForm.get('mailingStreetAddress').value && this.contactForm.get('mailingStreetAddress').value.toString().length > 0) {
      if (this.contactForm.get('mailingStateCode').value === null || this.contactForm.get('mailingCity').value === '' || this.contactForm.get('mailingPostalCode').value === '') {
        this.contactForm.setErrors({mailingStreetAddress: true});
      }
    } else {
      this.contactForm.get('mailingCity').reset();
      this.contactForm.get('mailingStateCode').reset();
      this.contactForm.get('mailingPostalCode').reset();
    }

    if (this.sameAddress) {
      this.contactForm.get('mailingStreetAddress').setValue(this.contactForm.get('streetAddress').value);
      this.contactForm.get('mailingCity').setValue(this.contactForm.get('city').value);
      this.contactForm.get('mailingStateCode').setValue(this.contactForm.get('stateCode').value);
      this.contactForm.get('mailingPostalCode').setValue(this.contactForm.get('postalCode').value);
    }
  }

  canDeactivate(): Promise<boolean> | boolean {
    if (!this.contactForm.dirty || (!this.createMode && !this.editInfo)) {
        return true;
    }
    return this.utilityService.canDeactivateModal();
  }

  @HostListener('window:beforeunload', ['$event'])
  beforeunloadHandler(event) {
    if ((this.createMode || this.editInfo) && this.contactForm.dirty) {
      event.preventDefault();
      event.returnValue = '';
    }
    return true;
  }

}
