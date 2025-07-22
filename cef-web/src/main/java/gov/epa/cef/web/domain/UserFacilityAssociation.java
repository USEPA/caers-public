/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import gov.epa.cef.web.domain.common.BaseAuditEntity;

@Entity
@Table(name = "user_facility_association")
public class UserFacilityAssociation extends BaseAuditEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_facility_id", nullable = false)
    private MasterFacilityRecord masterFacilityRecord;

    @Column(name = "cdx_user_id", nullable = false)
    protected String cdxUserId;

    @Column(name = "user_role_id", nullable = false)
    private Long userRoleId;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "last_modified_date", nullable = false)
    private Date lastModifiedDate;
    
    @Column(name = "authorized_by")
    protected String authorizedBy;
    
    @Column(name = "authorization_date", length = 29)
    protected Date authorizationDate;
    
    @Column(name = "deauthorized_by")
    protected String deauthorizedBy;
    
    @Column(name = "deauthorization_date", length = 29)
    protected Date deauthorizationDate;

    public MasterFacilityRecord getMasterFacilityRecord() {
        return masterFacilityRecord;
    }

    public void setMasterFacilityRecord(MasterFacilityRecord masterFacilityRecord) {
        this.masterFacilityRecord = masterFacilityRecord;
    }

    public String getCdxUserId() {
        return cdxUserId;
    }

    public void setCdxUserId(String cdxUserId) {
        this.cdxUserId = cdxUserId;
    }

    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getLastModifiedDate() { return lastModifiedDate; }

    public void setLastModifiedDate(Date lastModifiedDate) { this.lastModifiedDate = lastModifiedDate; }

	public String getAuthorizedBy() {
		return authorizedBy;
	}

	public void setAuthorizedBy(String authorizedBy) {
		this.authorizedBy = authorizedBy;
	}

	public Date getAuthorizationDate() {
		return authorizationDate;
	}

	public void setAuthorizationDate(Date authorizationDate) {
		this.authorizationDate = authorizationDate;
	}

	public String getDeauthorizedBy() {
		return deauthorizedBy;
	}

	public void setDeauthorizedBy(String deauthorizedBy) {
		this.deauthorizedBy = deauthorizedBy;
	}

	public Date getDeauthorizationDate() {
		return deauthorizationDate;
	}

	public void setDeauthorizationDate(Date deauthorizationDate) {
		this.deauthorizationDate = deauthorizationDate;
	}

}
