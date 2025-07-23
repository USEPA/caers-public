import {PermitType} from "./permit-type";

export class FacilityPermit {
    id: number;
    masterFacilityId: number;
    permitNumber: string;
    permitType: PermitType;
    otherDescription: string;
    startDate: string;
    endDate: string;
    comments: string;
}