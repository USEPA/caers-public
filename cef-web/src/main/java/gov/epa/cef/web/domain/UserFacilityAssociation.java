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
