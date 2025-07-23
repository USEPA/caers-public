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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import gov.epa.cef.web.domain.common.BaseEntity;

@Entity
@Table(name = "eis_tri_xref")
public class EisTriXref extends BaseEntity {
	
    private static final long serialVersionUID = 1L;
    
    @Column(name = "eis_id", length = 22)
    private String eisId;

    @Column(name = "trifid", length = 15)
    private String trifid;

	public String getEisId() {
		return eisId;
	}

	public void setEisId(String eisId) {
		this.eisId = eisId;
	}

	public String getTrifid() {
		return trifid;
	}

	public void setTrifid(String trifid) {
		this.trifid = trifid;
	}

}
