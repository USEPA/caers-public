import {Component, Input, OnInit} from '@angular/core';
import {BaseSortableTable} from "src/app/shared/components/sortable-table/base-sortable-table";
import {MasterFacilityRecord} from "src/app/shared/models/master-facility-record";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {UserContextService} from "src/app/core/services/user-context.service";
import {ConfigPropertyService} from "src/app/core/services/config-property.service";
import {MasterFacilityRecordService} from "src/app/core/services/master-facility-record.service";
import {faPlus, faEdit} from '@fortawesome/free-solid-svg-icons';
import {
    MasterFacilityPermitModalComponent
} from "../master-facility-permit-modal/master-facility-permit-modal.component";
import {
    ConfirmationDialogComponent
} from "src/app/shared/components/confirmation-dialog/confirmation-dialog.component";
import {FacilityPermit} from "src/app/shared/models/facility-permit";
import {User} from "src/app/shared/models/user";

@Component({
    selector: 'app-master-facility-permit-table',
    templateUrl: './master-facility-permit-table.component.html',
    styleUrls: ['./master-facility-permit-table.component.scss']
})
export class MasterFacilityPermitTableComponent extends BaseSortableTable implements OnInit {
    @Input() masterFacilityId: number;
    @Input() facility: MasterFacilityRecord;
    @Input() tableData: FacilityPermit[];

    faPlus = faPlus;
    faEdit = faEdit;

    permitEntryEnabled: true;
    user: User;

    constructor(
        private modalService: NgbModal,
        private userContextService: UserContextService,
        private propertyService: ConfigPropertyService,
        private mfrService: MasterFacilityRecordService) {
        super();
    }

    ngOnInit(): void {
        this.userContextService.getUser().subscribe(user => {
            this.user = user;
        });
    }

    sortTable() {
        this.tableData.sort((a, b) => (a.permitNumber > b.permitNumber ? 1 : -1));
    }

    // delete facility permit from the database
    deleteMfrFacilityPermit(permitId: number, mfrId: number) {
        this.mfrService.deleteMasterFacilityPermit(permitId).subscribe(() => {

            // update the UI table with the current list of facility permits
            this.mfrService.getRecord(mfrId)
                .subscribe(masterFacilityResponse => {
                    this.facility.masterFacilityPermits = masterFacilityResponse.masterFacilityPermits;
                    this.sortTable();
                });
        });
    }

    openDeleteModal(permitNumber: string, permitId: number, mfrId: number) {
        const modalMessage = `Are you sure you want to delete permit ${permitNumber} from this master facility record?`;
        const modalRef = this.modalService.open(ConfirmationDialogComponent, {size: 'sm'});
        modalRef.componentInstance.message = modalMessage;
        modalRef.componentInstance.continue.subscribe(() => {
            this.deleteMfrFacilityPermit(permitId, mfrId);
        });
    }

    openEditModal(selectedPermit) {
        const modalRef = this.modalService.open(MasterFacilityPermitModalComponent, {size: 'xl', backdrop: 'static'});
        modalRef.componentInstance.masterFacilityId = this.facility.id;
        modalRef.componentInstance.masterFacilityPermits = this.tableData;
        modalRef.componentInstance.selectedPermit = selectedPermit;
        modalRef.componentInstance.edit = true;

        modalRef.result.then(() => {
            this.mfrService.getRecord(this.facility.id)
                .subscribe(facilityResponse => {

                    this.facility.masterFacilityPermits = facilityResponse.masterFacilityPermits;
                    this.sortTable();
                });

        }, () => {
            //   needed for dismissing without errors
        });
    }

    openNewPermitModal() {
        const modalRef = this.modalService.open(MasterFacilityPermitModalComponent, {size: 'xl', backdrop: 'static'});
        modalRef.componentInstance.masterFacilityId = this.facility.id;
        modalRef.componentInstance.masterFacilityPermits = this.tableData;

        modalRef.result.then(() => {
            this.mfrService.getRecord(this.facility.id)
                .subscribe(facilityResponse => {

                    this.facility.masterFacilityPermits = facilityResponse.masterFacilityPermits;
                    this.sortTable();
                });

        }, () => {
            //   needed for dismissing without errors
        });
    }

}
