package gov.epa.cef.web.domain;

import gov.epa.cef.web.domain.common.BaseAuditEntity;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "facility_permit")
public class FacilityPermit extends BaseAuditEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_facility_id", nullable = false)
    private MasterFacilityRecord masterFacilityRecord;

    @Column(name = "permit_number", length = 200, nullable = false)
    private String permitNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permit_type", nullable = false)
    private FacilityPermitType permitType;

    @Column(name = "other_description", length = 200)
    private String otherDescription;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "comments",length = 2000)
    private String comments;

    public FacilityPermit() {

    }

    public FacilityPermit(MasterFacilityRecord masterFacilityRecord, FacilityPermit originalPermit) {
        this.id = originalPermit.getId();
        this.masterFacilityRecord = masterFacilityRecord;
        this.permitNumber = originalPermit.getPermitNumber();
        this.permitType = originalPermit.permitType;
        this.otherDescription = originalPermit.getOtherDescription();
        this.startDate = originalPermit.getStartDate();
        this.endDate = originalPermit.getEndDate();
        this.comments = originalPermit.getComments();
    }

    public MasterFacilityRecord getMasterFacilityRecord() {
        return masterFacilityRecord;
    }

    public void setMasterFacilityRecord(MasterFacilityRecord masterFacilityRecord) {
        this.masterFacilityRecord = masterFacilityRecord;
    }

    public String getPermitNumber() {
        return permitNumber;
    }

    public void setPermitNumber(String permitNumber) {
        this.permitNumber = permitNumber;
    }

    public FacilityPermitType getPermitType() {
        return permitType;
    }

    public void setPermitType(FacilityPermitType permitType) {
        this.permitType = permitType;
    }

    public String getOtherDescription() {
        return otherDescription;
    }

    public void setOtherDescription(String otherDescription) {
        this.otherDescription = otherDescription;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}
