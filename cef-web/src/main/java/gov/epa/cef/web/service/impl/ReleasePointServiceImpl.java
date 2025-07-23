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
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import gov.epa.cef.web.domain.ControlPath;
import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.domain.ReleasePoint;
import gov.epa.cef.web.domain.ReleasePointAppt;
import gov.epa.cef.web.exception.AppValidationException;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.ReleasePointApptRepository;
import gov.epa.cef.web.repository.ReleasePointRepository;
import gov.epa.cef.web.service.ReleasePointService;
import gov.epa.cef.web.service.dto.ControlPathDto;
import gov.epa.cef.web.service.dto.ReleasePointApptDto;
import gov.epa.cef.web.service.dto.ReleasePointDto;
import gov.epa.cef.web.service.dto.bulkUpload.ReleasePointApptBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ReleasePointBulkUploadDto;
import gov.epa.cef.web.service.mapper.ReleasePointMapper;
import gov.epa.cef.web.service.mapper.BulkUploadMapper;
import gov.epa.cef.web.service.mapper.ControlPathMapper;
import gov.epa.cef.web.service.mapper.ReleasePointApptMapper;
import gov.epa.cef.web.util.ConstantUtils;

@Service
public class ReleasePointServiceImpl implements ReleasePointService {

    @Autowired
    private EmissionsReportRepository reportRepo;

    @Autowired
    private ReleasePointRepository releasePointRepo;

    @Autowired
    private ReleasePointApptRepository releasePointApptRepo;

    @Autowired
    private ReleasePointMapper releasePointMapper;

    @Autowired
    private EmissionsReportStatusServiceImpl reportStatusService;

    @Autowired
    private ReleasePointApptMapper releasePointApptMapper;

    @Autowired
    private ControlPathServiceImpl controlPathService;

    @Autowired
    private ControlPathMapper controlPathMapper;

    @Autowired
    private BulkUploadMapper bulkUploadMapper;

    /**
     * Create a new Release Point from a DTO object
     */
    public ReleasePointDto create(ReleasePointDto dto) {
    	ReleasePoint releasePoint = releasePointMapper.fromDto(dto);

    	ReleasePointDto result = releasePointMapper.toDto(releasePointRepo.save(releasePoint));
    	reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(result.getId()), ReleasePointRepository.class);

    	return result;
    }

    /* (non-Javadoc)
     * @see gov.epa.cef.web.service.impl.ReleasePointService#retrieveById(java.lang.Long)
     */
    @Override
    public ReleasePointDto retrieveById(Long releasePointId) {
        ReleasePoint releasePoint= releasePointRepo
            .findById(releasePointId)
            .orElse(null);
        return releasePointMapper.toDto(releasePoint);
    }

    /**
     * Retrieve versions of this rp from the last year reported
     * @param releasePointId
     * @return
     */
    public ReleasePointDto retrievePreviousById(Long releasePointId) {
        ReleasePoint releasePoint= releasePointRepo
            .findById(releasePointId)
            .orElse(null);

        Long mfrId = releasePointRepo.retrieveMasterFacilityRecordIdById(releasePointId).orElse(null);

        ReleasePoint result = this.findPrevious(mfrId, releasePoint.getFacilitySite().getEmissionsReport().getYear(), releasePoint.getReleasePointIdentifier())
                .stream()
                .findFirst()
                .orElse(null);
        return releasePointMapper.toDto(result);
    }

    /* (non-Javadoc)
     * @see gov.epa.cef.web.service.impl.ReleasePointService#retrieveByFacilityId(java.lang.Long)
     */
    public List<ReleasePointDto> retrieveByFacilitySiteId(Long facilitySiteId) {
        List<ReleasePoint> releasePoints=releasePointRepo.findByFacilitySiteIdOrderByReleasePointIdentifier(facilitySiteId);
        return releasePointMapper.toDtoList(releasePoints);
    }

    /**
     * Update an existing Release Point from a DTO
     */
    public ReleasePointDto update(ReleasePointDto dto) {

    	ReleasePoint releasePoint = releasePointRepo.findById(dto.getId()).orElse(null);
    	releasePointMapper.updateFromDto(dto, releasePoint);

    	//update UoMs in all release points
    	releasePoint = sanitizeReleasePointUoMs(releasePoint);

    	ReleasePointDto result = releasePointMapper.toDto(releasePointRepo.save(releasePoint));
    	reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(result.getId()), ReleasePointRepository.class);

        return result;
    }

    /**
     * Delete Release Point for a given id
     * @param releasePointId
     */
    public void delete(Long releasePointId) {
        ReleasePoint rp = releasePointRepo
                .findById(releasePointId)
                .orElse(null);

        Long mfrId = releasePointRepo.retrieveMasterFacilityRecordIdById(releasePointId).orElse(null);

        this.findPrevious(mfrId, rp.getFacilitySite().getEmissionsReport().getYear(), rp.getReleasePointIdentifier())
            .stream()
            .findFirst()
            .ifPresent(oldRp -> {
                throw new AppValidationException("This Release Point has been submitted on previous years' facility reports, so it cannot be deleted. "
                        + "If this Release Point is no longer operational, please use the \"Operating Status\" field to mark this Release Point as \"Permanently Shutdown\".");
            });

        reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(releasePointId), ReleasePointRepository.class);
    	releasePointRepo.deleteById(releasePointId);
    }

    /**
     * Delete Release Point Apportionment for a given id
     * @param releasePointApptId
     */
    public void deleteAppt(Long releasePointApptId) {
        reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(releasePointApptId), ReleasePointApptRepository.class);
    	releasePointApptRepo.deleteById(releasePointApptId);
    }

    /**
     * Create a new Release Point Apportionment from a DTO object
     */
    public ReleasePointApptDto createAppt(ReleasePointApptDto dto) {
    	ReleasePointAppt releasePointAppt = releasePointApptMapper.fromDto(dto);

    	ReleasePointApptDto results = releasePointApptMapper.toDto(releasePointApptRepo.save(releasePointAppt));

    	reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(results.getReleasePointId()), ReleasePointRepository.class);
    	return results;
    }

    /**
     * Update an existing Release Point Apportionment from a DTO
     */
    public ReleasePointApptDto updateAppt(ReleasePointApptDto dto) {
    	ControlPathDto controlPathDto = new ControlPathDto();

    	if(dto.getControlPath() != null){
        	controlPathDto = controlPathService.retrieveById(dto.getControlPath().getId());
    	}

    	ReleasePointAppt releasePointAppt = releasePointApptRepo.findById(dto.getId()).orElse(null);
    	releasePointAppt.setReleasePoint(null);
    	dto.setControlPath(null);
    	releasePointApptMapper.updateFromDto(dto, releasePointAppt);

    	if(controlPathDto.getId() != null){
        	ControlPath controlPath = controlPathMapper.fromDto(controlPathDto);
        	releasePointAppt.setControlPath(controlPath);
    	}

    	ReleasePointApptDto result = releasePointApptMapper.toDto(releasePointApptRepo.save(releasePointAppt));
    	reportStatusService.resetEmissionsReportForEntity(Collections.singletonList(result.getId()), ReleasePointApptRepository.class);

        return result;
    }

    /**
     * Retrieve Release Points associated with a control path id
     */
    public List<ReleasePointDto> retrieveByControlPathId(Long controlPathId) {
    	List<ReleasePoint> releasePoints = releasePointRepo.retrieveByControlPathId(controlPathId);
    	return releasePointMapper.toDtoList(releasePoints);
    }

    /**
     * Find versions of this rp from the last year reported
     * @param mfrId
     * @param year
     * @param identifier
     * @return
     */
    private List<ReleasePoint> findPrevious(Long mfrId, Short year, String identifier) {

        // find the last year reported
        Optional<EmissionsReport> lastReport = reportRepo.findFirstByMasterFacilityRecordIdAndIsDeletedIsFalseAndYearLessThanOrderByYearDesc(mfrId,
                year);

        // check if the release point was reported last year
        if (lastReport.isPresent()) {
            return releasePointRepo.retrieveByIdentifierFacilityYear(identifier,
                    mfrId,
                    lastReport.get().getYear());
        }

        return Collections.emptyList();
    }


    /**
     * Retrieve a list of release points for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    public List<ReleasePointBulkUploadDto> retrieveReleasePoints(String programSystemCode, Short emissionsReportYear) {
    	List<ReleasePoint> releasePoints = releasePointRepo.findByPscAndEmissionsReportYear(programSystemCode, emissionsReportYear);
        for (ReleasePoint rp : releasePoints) {
            rp = sanitizeReleasePointUoMs(rp);
        }
    	return bulkUploadMapper.releasePointToDtoList(releasePoints);
    }


    /**
     * Retrieve a list of release point apportionments for the given program system code and emissions report year
     * @param programSystemCode
     * @param emissionsReportYear
     * @return
     */
    public List<ReleasePointApptBulkUploadDto> retrieveReleasePointAppts(String programSystemCode, Short emissionsReportYear) {
    	List<ReleasePointAppt> releasePointAppts = releasePointApptRepo.findByPscAndEmissionsReportYear(programSystemCode, emissionsReportYear);
    	return bulkUploadMapper.releasePointApptToDtoList(releasePointAppts);
    }

    /***
	 * Sanitize UoMs for RPs based on type and if data is present
	 * @param releasePoint
	 * @return
	 */
    private ReleasePoint sanitizeReleasePointUoMs(ReleasePoint releasePoint) {
    	if (releasePoint.getExitGasFlowRate() == null) {
    		releasePoint.setExitGasFlowUomCode(null);
    	}
    	if (releasePoint.getExitGasVelocity() == null) {
    		releasePoint.setExitGasVelocityUomCode(null);
    	}
    	if (releasePoint.getFenceLineDistance() == null) {
    		releasePoint.setFenceLineUomCode(null);
    	}

    	if (releasePoint.getTypeCode().getCategory().equals("Fugitive")) {
    		releasePoint.setStackDiameter(null);
    		releasePoint.setStackDiameterUomCode(null);
    		releasePoint.setStackHeight(null);
    		releasePoint.setStackHeightUomCode(null);
    		releasePoint.setStackLength(null);
    		releasePoint.setStackLengthUomCode(null);
    		releasePoint.setStackWidth(null);
    		releasePoint.setStackWidthUomCode(null);

    		if (releasePoint.getFugitiveHeight() == null) {
    			releasePoint.setFugitiveHeightUomCode(null);
    		}
    		if (releasePoint.getFugitiveLength() == null) {
    			releasePoint.setFugitiveLengthUomCode(null);
    		}
    		if (releasePoint.getFugitiveWidth() == null) {
    			releasePoint.setFugitiveWidthUomCode(null);
    		}

    		if (releasePoint.getTypeCode().getCode().equals(ConstantUtils.FUGITIVE_RELEASE_PT_2D_TYPE)
    			|| releasePoint.getTypeCode().getCode().equals(ConstantUtils.FUGITIVE_RELEASE_PT_3D_TYPE)) {
    			releasePoint.setFugitiveAngle(null);
    		}

    	} else {
    		releasePoint.setFugitiveHeight(null);
			releasePoint.setFugitiveHeightUomCode(null);
    		releasePoint.setFugitiveLength(null);
			releasePoint.setFugitiveLengthUomCode(null);
    		releasePoint.setFugitiveWidth(null);
			releasePoint.setFugitiveWidthUomCode(null);
			releasePoint.setFugitiveAngle(null);
			releasePoint.setFugitiveMidPt2Latitude(null);
			releasePoint.setFugitiveMidPt2Longitude(null);

			if (releasePoint.getStackDiameter() == null) {
    			releasePoint.setStackDiameterUomCode(null);
    		}
			if (releasePoint.getStackHeight() == null) {
    			releasePoint.setStackHeightUomCode(null);
    		}
			if (releasePoint.getStackLength() == null) {
    			releasePoint.setStackLengthUomCode(null);
    		}
			if (releasePoint.getStackWidth() == null) {
    			releasePoint.setStackWidthUomCode(null);
    		}
    	}

    	return releasePoint;
    }

}
