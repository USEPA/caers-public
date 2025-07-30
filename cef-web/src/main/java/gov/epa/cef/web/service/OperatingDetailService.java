/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service;

import java.util.List;

import gov.epa.cef.web.domain.OperatingDetail;
import gov.epa.cef.web.service.dto.OperatingDetailDto;
import gov.epa.cef.web.service.dto.bulkUpload.OperatingDetailBulkUploadDto;

public interface OperatingDetailService {

    /**
     * Update an Operating Detail
     * @param dto
     * @return
     */
    OperatingDetailDto update(OperatingDetailDto dto);

    /**
     * Retrieve a list of operating details for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    List<OperatingDetailBulkUploadDto> retrieveOperatingDetails(String programSystemCode, Short emissionsReportYear);

    /**
     * Retrieve a list of all operating details for a specific program system code and emissions
     * reporting year where the associated facility is operating and associated reporting period is an annual period
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    List<OperatingDetail> retrieveAnnualOperatingDetails(String programSystemCode, Short emissionsReportYear);

    /**
     * Delete Operating Details by facility site id
     * @param facilitySiteId
     */
    void deleteByFacilitySite(Long facilitySiteId);
}
