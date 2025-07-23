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
package gov.epa.cef.web.service.dto;

import java.io.Serializable;

public class AircraftEngineTypeCodeDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String code;
	private String faaAircraftType;
	private String edmsAccode;
	private String engineManufacturer;
	private String engine;
	private String edmsUid;
	private String scc;
	private Integer lastInventoryYear;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFaaAircraftType() {
		return faaAircraftType;
	}

	public void setFaaAircraftType(String faaAircraftType) {
		this.faaAircraftType = faaAircraftType;
	}

	public String getEdmsAccode() {
		return edmsAccode;
	}

	public void setEdmsAccode(String edmsAccode) {
		this.edmsAccode = edmsAccode;
	}

	public String getEngineManufacturer() {
		return engineManufacturer;
	}

	public void setEngineManufacturer(String engineManufacturer) {
		this.engineManufacturer = engineManufacturer;
	}

	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}

	public String getEdmsUid() {
		return edmsUid;
	}

	public void setEdmsUid(String edmsUid) {
		this.edmsUid = edmsUid;
	}

	public String getScc() {
		return scc;
	}

	public void setScc(String scc) {
		this.scc = scc;
	}

    public Integer getLastInventoryYear() {
        return lastInventoryYear;
    }

    public void setLastInventoryYear(Integer lastInventoryYear) {
        this.lastInventoryYear = lastInventoryYear;
    }

}
