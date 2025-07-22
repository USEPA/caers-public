/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service;

import gov.epa.cef.web.service.dto.ReleasePointApptDto;
import gov.epa.cef.web.service.dto.ReleasePointDto;
import gov.epa.cef.web.service.dto.bulkUpload.ReleasePointApptBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ReleasePointBulkUploadDto;

import java.util.List;

public interface ReleasePointService {
	
	/**
     * Create a new Release Point
     * @param dto
     * @return
     */
	ReleasePointDto create(ReleasePointDto dto);

    /**
     * Retrieve Release Point by id
     * @param releasePointId
     * @return
     */
    ReleasePointDto retrieveById(Long releasePointId);

    /**
     * Retrieve versions of this rp from the last year reported
     * @param releasePointId
     * @return
     */
    ReleasePointDto retrievePreviousById(Long releasePointId);

    /**
     * Retrieve Release Points by facility id
     * @param facilitySiteId
     * @return
     */
    List<ReleasePointDto> retrieveByFacilitySiteId(Long facilitySiteId);
    
    /**
     * Update an existing Release Point by id
     * @param dto
     * @return
     */
    ReleasePointDto update(ReleasePointDto dto);
    
    /**
     * Delete Release Point by id
     * @param releasePointId
     */
    void delete(Long releasePointId);
    
    /**
     * Delete Release Point Apportionment by id
     * @param releasePointApptId
     */
    void deleteAppt(Long releasePointApptId);
    
	/**
     * Create a new Release Point Apportionment
     * @param dto
     * @return
     */
	ReleasePointApptDto createAppt(ReleasePointApptDto dto);
	
    /**
     * Update an existing Release Point Apportionment by id
     * @param dto
     * @return
     */
    ReleasePointApptDto updateAppt(ReleasePointApptDto dto);
    
    /**
     * Retrieve Release Points associated with a control path id
     * @param controlPathId
     * @return
     */
    List<ReleasePointDto> retrieveByControlPathId(Long controlPathId);

    /**
     * Retrieve a list of release points for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    List<ReleasePointBulkUploadDto> retrieveReleasePoints(String programSystemCode, Short emissionsReportYear);

    /**
     * Retrieve a list of release point apportionments for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    List<ReleasePointApptBulkUploadDto> retrieveReleasePointAppts(String programSystemCode, Short emissionsReportYear);
}
