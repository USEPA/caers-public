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
