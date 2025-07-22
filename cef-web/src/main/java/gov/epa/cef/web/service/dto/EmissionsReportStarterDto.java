/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.MoreObjects;

import gov.epa.cef.web.domain.ThresholdStatus;

public class EmissionsReportStarterDto {

    public enum SourceType {
        previous, frs, fromScratch
    }

    private String eisProgramId;

    private FacilitySiteDto facilitySite;

    private Long masterFacilityRecordId;
    
    private String programSystemCode;

    private SourceType source;

    private String agencyFacilityIdentifier;

    private ThresholdStatus thresholdStatus;

    private Short year;
    
    private JsonNode jsonFileData;

    public String getAgencyFacilityIdentifier() {

        return agencyFacilityIdentifier;
    }

    public void setAgencyFacilityIdentifier(String agencyFacilityIdentifier) {

        this.agencyFacilityIdentifier = agencyFacilityIdentifier;
    }

    public String getEisProgramId() {

        return eisProgramId;
    }

    public void setEisProgramId(String eisProgramId) {

        this.eisProgramId = eisProgramId;
    }

    public FacilitySiteDto getFacilitySite() {

        return facilitySite;
    }

    public void setFacilitySite(FacilitySiteDto facilitySite) {

        this.facilitySite = facilitySite;
    }

    public Long getMasterFacilityRecordId() {
        return masterFacilityRecordId;
    }

    public void setMasterFacilityRecordId(Long masterFacilityRecordId) {
        this.masterFacilityRecordId = masterFacilityRecordId;
    }

    public String getProgramSystemCode() {

        return programSystemCode;
    }

    public void setProgramSystemCode(String programSystemCode) {

        this.programSystemCode = programSystemCode;
    }

    public SourceType getSource() {

        return source;
    }

    public void setSource(SourceType source) {

        this.source = source;
    }

    public ThresholdStatus getThresholdStatus() {
        return thresholdStatus;
    }

    public void setThresholdStatus(ThresholdStatus thresholdStatus) {
        this.thresholdStatus = thresholdStatus;
    }

    public Short getYear() {

        return year;
    }

    public void setYear(Short year) {

        this.year = year;
    }

	public JsonNode getJsonFileData() {
        return jsonFileData;
    }

    public void setJsonFileData(JsonNode jsonFileData) {
        this.jsonFileData = jsonFileData;
    }

    @Override
    public String toString() {

        return MoreObjects.toStringHelper(this)
            .add("eisProgramId", eisProgramId)
            .add("facilitySite", facilitySite)
            .add("masterFacilityRecordId", masterFacilityRecordId)
            .add("programSystemCode", programSystemCode)
            .add("source", source)
            .add("agencyFacilityIdentifier", agencyFacilityIdentifier)
            .add("year", year)
            .toString();
    }
}
