/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.epa.cef.web.domain.OperatingDetail;
import gov.epa.cef.web.repository.OperatingDetailRepository;
import gov.epa.cef.web.service.OperatingDetailService;
import gov.epa.cef.web.service.dto.OperatingDetailDto;
import gov.epa.cef.web.service.dto.bulkUpload.OperatingDetailBulkUploadDto;
import gov.epa.cef.web.service.mapper.BulkUploadMapper;
import gov.epa.cef.web.service.mapper.OperatingDetailMapper;


@Service
public class OperatingDetailServiceImpl implements OperatingDetailService {

    @Autowired
    private OperatingDetailRepository repo;

    @Autowired
    private OperatingDetailMapper mapper;

    @Autowired
    private EmissionsReportStatusServiceImpl reportStatusService;

    @Autowired BulkUploadMapper bulkUploadMapper;

    /* (non-Javadoc)
     * @see gov.epa.cef.web.service.impl.OperatingDetailService#update(gov.epa.cef.web.service.dto.OperatingDetailDto)
     */
    @Override
    public OperatingDetailDto update(OperatingDetailDto dto) {

        OperatingDetail entity = repo.findById(dto.getId()).orElse(null);
        mapper.updateFromDto(dto, entity);

        OperatingDetailDto result = mapper.toDto(repo.save(entity));
        reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(result.getId()), OperatingDetailRepository.class);
        return result;
    }

    /**
     * Retrieve a list of operating details for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    public List<OperatingDetailBulkUploadDto> retrieveOperatingDetails(String programSystemCode, Short emissionsReportYear) {
    	List<OperatingDetail> operatingDetails = repo.findByPscAndEmissionsReportYear(programSystemCode, emissionsReportYear);
    	return bulkUploadMapper.operatingDetailToDtoList(operatingDetails);
    }

    /**
     * Retrieve a list of all operating details for a specific program system code and emissions
     * reporting year where the associated facility is operating and associated reporting period is an annual period
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    public List<OperatingDetail> retrieveAnnualOperatingDetails(String programSystemCode, Short emissionsReportYear) {
        return repo.findAnnualByPscAndEmissionsReportYear(programSystemCode, emissionsReportYear);
    }

    /**
     * Delete Operating Details by facility site id
     * @param facilitySiteId
     */
    public void deleteByFacilitySite(Long facilitySiteId) {
        repo.deleteByFacilitySite(facilitySiteId);
    }
}
