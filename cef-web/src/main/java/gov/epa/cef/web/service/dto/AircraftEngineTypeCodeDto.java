/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
