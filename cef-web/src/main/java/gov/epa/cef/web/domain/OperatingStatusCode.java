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

import gov.epa.cef.web.domain.common.BaseLookupEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;


/**
 * OperatingStatusCode entity
 */
@Entity
@Table(name = "operating_status_code")
@Immutable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class OperatingStatusCode extends BaseLookupEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "sub_facility_status", nullable = false)
    private Boolean subFacilityStatus;

    @Column(name = "facility_status", nullable = false)
    private Boolean facilityStatus;


    public Boolean getSubFacilityStatus() {
        return subFacilityStatus;
    }
    public void setSubFacilityStatus(Boolean subFacilityStatus) {
        this.subFacilityStatus = subFacilityStatus;
    }

    public Boolean getFacilityStatus() {
        return facilityStatus;
    }
    public void setFacilityStatus(Boolean facilityStatus) {
        this.facilityStatus = facilityStatus;
    }

    public OperatingStatusCode withCode(String code) {
        this.setCode(code);
        return this;
    }
}
