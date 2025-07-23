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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SccResourceSearchApiDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> facetName;
    private List<String> facetValue;
    private List<String> facetQualifier;
    private List<String> facetMatchType;
    private String paramCallback; 
    private String format; 
    private String filename; 
    private LocalDate lastUpdatedSince; 
    private String sortFacet; 
    private String pageNum;
    private String pageSize;

    public List<String> getFacetName() {
        return facetName;
    }

    public void setFacetName(List<String> facetName) {
        this.facetName = facetName;
    }

    public List<String> getFacetValue() {
        return facetValue;
    }

    public void setFacetValue(List<String> facetValue) {
        this.facetValue = facetValue;
    }

    public List<String> getFacetQualifier() {
        return facetQualifier;
    }

    public void setFacetQualifier(List<String> facetQualifier) {
        this.facetQualifier = facetQualifier;
    }

    public List<String> getFacetMatchType() {
        return facetMatchType;
    }

    public void setFacetMatchType(List<String> facetMatchType) {
        this.facetMatchType = facetMatchType;
    }

    public String getParamCallback() {
        return paramCallback;
    }

    public void setParamCallback(String paramCallback) {
        this.paramCallback = paramCallback;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public LocalDate getLastUpdatedSince() {
        return lastUpdatedSince;
    }

    public String getLastUpdatedSinceString() {
        return lastUpdatedSince != null ? lastUpdatedSince.format(DateTimeFormatter.ISO_LOCAL_DATE) : null;
    }

    public void setLastUpdatedSince(LocalDate lastUpdatedSince) {
        this.lastUpdatedSince = lastUpdatedSince;
    }

    public String getSortFacet() {
        return sortFacet;
    }

    public void setSortFacet(String sortFacet) {
        this.sortFacet = sortFacet;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
}
