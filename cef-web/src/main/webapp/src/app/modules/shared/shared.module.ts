/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import {PhoneNumberPipe} from 'src/app/modules/shared//pipes/phone-number.pipe';
import {FacilityInfoComponent} from 'src/app/shared/components/facility-info/facility-info.component';
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FontAwesomeModule, FaConfig, FaIconLibrary} from '@fortawesome/angular-fontawesome';
import {
    faCaretRight,
    faEdit,
    faCaretDown,
    faAngleLeft,
    faTrashAlt,
    faPlus,
    faCircle,
    faInfoCircle,
    faCheck,
    faTimesCircle,
    faFilter, faTimes, faSearch, faUpload, faPowerOff, faBan, fas, faClock
} from '@fortawesome/free-solid-svg-icons';
import {CollapseIconComponent} from 'src/app/modules/shared/components/collapse-icon/collapse-icon.component';
import {SidebarComponent} from 'src/app/modules/shared/components/sidebar/sidebar.component';
import {CollapseNavComponent} from 'src/app/modules/shared/components/collapse-nav/collapse-nav.component';
import {SidebarInnerNavComponent} from 'src/app/modules/shared/components/sidebar-inner-nav/sidebar-inner-nav.component';
import {SidebarInnerNavItemComponent} from 'src/app/modules/shared/components/sidebar-inner-nav-item/sidebar-inner-nav-item.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {SharedRoutingModule} from 'src/app/modules/shared/shared-routing';
import {SortableHeaderDirective} from 'src/app/shared/directives/sortable.directive';
import {ErrorComponent} from './pages/error/error.component';
import {ReportSummaryTableComponent} from 'src/app/modules/shared/components/report-summary-table/report-summary-table.component';
import {ValidationMessageComponent} from './components/validation-message/validation-message.component';
import {WholeNumberValidatorDirective} from './directives/whole-number-validator.directive';
import {NumberValidatorDirective} from './directives/number-validator.directive';
import {BaseConfirmationModalComponent} from './components/base-confirmation-modal/base-confirmation-modal.component';
import {BulkUploadComponent} from 'src/app/modules/shared/pages/bulk-upload/bulk-upload.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {StripPeriodEndingPipe} from './pipes/strip-period-ending.pipe';
import {LegacyUomValidatorDirective} from './directives/legacy-uom-validator.directive';
import {FileAttachmentModalComponent} from './components/file-attachment-modal/file-attachment-modal.component';
import {ReportAttachmentTableComponent} from './components/report-attachment-table/report-attachment-table.component';
import {LegacyItemValidatorDirective} from './directives/legacy-item-validator.directive';
import {EisTransactionAttachmentModalComponent} from './components/eis-transaction-attachment-modal/eis-transaction-attachment-modal.component';
import {CommentModalComponent} from './components/comment-modal/comment-modal.component';
import {HorizontalNavComponent} from './components/horizontal-nav/horizontal-nav.component';
import {RequiredAsteriskComponent} from './components/required-asterisk/required-asterisk.component';
import {EmailNotificationModalComponent} from './components/email-notification-modal/email-notification-modal.component';
import {LongitudeValidatorDirective} from './directives/longitude-validator.directive';
import {SidebarPaginateInnerNavItemComponent} from 'src/app/modules/shared/components/sidebar-paginate-inner-nav-item/sidebar-paginate-inner-nav-item.component';

@NgModule({
    declarations: [
        FacilityInfoComponent,
        CollapseIconComponent,
        SidebarComponent,
        CollapseNavComponent,
        SidebarInnerNavComponent,
        SidebarInnerNavItemComponent,
		SidebarPaginateInnerNavItemComponent,
        SortableHeaderDirective,
        PhoneNumberPipe,
        ErrorComponent,
        ReportSummaryTableComponent,
        ReportAttachmentTableComponent,
        ValidationMessageComponent,
        WholeNumberValidatorDirective,
        NumberValidatorDirective,
        BaseConfirmationModalComponent,
        FileAttachmentModalComponent,
		EmailNotificationModalComponent,
        BulkUploadComponent,
        StripPeriodEndingPipe,
        LegacyUomValidatorDirective,
		LongitudeValidatorDirective,
        LegacyItemValidatorDirective,
        EisTransactionAttachmentModalComponent,
        CommentModalComponent,
        HorizontalNavComponent,
        RequiredAsteriskComponent
    ],
    exports: [
        FacilityInfoComponent,
        CollapseIconComponent,
        FontAwesomeModule,
        SidebarComponent,
        CollapseNavComponent,
        SidebarInnerNavComponent,
        SidebarInnerNavItemComponent,
		SidebarPaginateInnerNavItemComponent,
        SortableHeaderDirective,
        PhoneNumberPipe,
        ReportSummaryTableComponent,
        ReportAttachmentTableComponent,
        ValidationMessageComponent,
        StripPeriodEndingPipe,
        HorizontalNavComponent,
		    RequiredAsteriskComponent
    ],
    imports: [
        CommonModule,
        FontAwesomeModule,
        NgbModule,
        SharedRoutingModule,
        FormsModule,
        ReactiveFormsModule,
    ],
    entryComponents: [
        BaseConfirmationModalComponent,
        FileAttachmentModalComponent,
		EmailNotificationModalComponent,
        EisTransactionAttachmentModalComponent
    ]
})
export class SharedModule {
    constructor(config: FaConfig, library: FaIconLibrary) {
        config.fallbackIcon = faBan;
        // Add an icon to the library for convenient access in other components
        library.addIconPacks(fas);
        library.addIcons(faCaretRight, faCaretDown, faAngleLeft, faTrashAlt, faPlus, faEdit, faCircle, faInfoCircle,
            faSearch, faCheck, faTimesCircle, faTimes, faFilter, faUpload, faPowerOff, faClock);

    }
}
