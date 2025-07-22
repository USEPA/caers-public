package gov.epa.cef.web.service.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class FacilityPermitDto implements Serializable {

    private Long id;
    private Long masterFacilityId;
    private String permitNumber;
    private FacilityPermitTypeDto permitType;
    private String otherDescription;
    private LocalDate startDate;
    private LocalDate endDate;
    private String comments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FacilityPermitDto withId(Long id) {
        setId(id);
        return this;
    }

    public Long getMasterFacilityId() {
        return masterFacilityId;
    }

    public void setMasterFacilityId(Long masterFacilityId) {
        this.masterFacilityId = masterFacilityId;
    }

    public String getPermitNumber() {
        return permitNumber;
    }

    public void setPermitNumber(String permitNumber) {
        this.permitNumber = permitNumber;
    }

    public FacilityPermitTypeDto getPermitType() {
        return permitType;
    }

    public void setPermitType(FacilityPermitTypeDto permitType) {
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
