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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import gov.epa.cef.web.domain.SccCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.epa.cef.web.client.api.SccApiClient;
import gov.epa.cef.web.domain.PointSourceSccCode;
import gov.epa.cef.web.repository.PointSourceSccCodeRepository;
import gov.epa.cef.web.service.SccService;
import gov.epa.cef.web.service.dto.SccAttributeDto;
import gov.epa.cef.web.service.dto.SccDetailDto;
import gov.epa.cef.web.service.dto.SccResourceSearchApiDto;
import gov.epa.client.sccwebservices.model.SccDetail;

@Service
public class SccServiceImpl implements SccService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PointSourceSccCodeRepository pointSourceSccCodeRepo;

    @Autowired
    private SccApiClient sccClient;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Retrieve Point SCCs from the webservice since a certain date
     * @param lastUpdated
     * @return
     */
    private List<SccDetailDto> retrievePointSccDetailsSince(LocalDate lastUpdated) {

        SccResourceSearchApiDto dto = new SccResourceSearchApiDto();
        dto.setFacetName(Collections.singletonList("Data Category"));
        dto.setFacetValue(Collections.singletonList("Point"));
        dto.setFacetQualifier(Collections.singletonList("exact"));
        dto.setFacetMatchType(Collections.singletonList("whole_phrase"));

        // grab SCCs from the last 3 days instead of just the prior day to ensure correct updates
        dto.setLastUpdatedSince(lastUpdated.minusDays(2));

        List<SccDetailDto> result = this.sccClient.getResourceSearchResults(dto)
                .stream()
                .map(scc -> mapSccDetail(scc))
                .collect(Collectors.toList());

        return result;
    }

    private List<SccDetailDto> retrieveNonPointSccDetailsSince(LocalDate lastUpdated) {

        SccResourceSearchApiDto dto = new SccResourceSearchApiDto();
        dto.setFacetName(Collections.singletonList("SCC Level Two"));
        dto.setFacetValue(Collections.singletonList("Oil and Gas Exploration and Production"));
        dto.setFacetQualifier(Collections.singletonList("contains"));
        dto.setFacetMatchType(Collections.singletonList("whole_phrase"));

        // grab SCCs from the last 3 days instead of just the prior day to ensure correct updates
        dto.setLastUpdatedSince(lastUpdated.minusDays(2));

        List<SccDetailDto> result = this.sccClient.getResourceSearchResults(dto)
            .stream()
            .map(scc -> mapSccDetail(scc))
            .collect(Collectors.toList());

        return result;
    }

    /**
     * Retrieve Point SCCs from the webservice since a certain date and update the database with them
     * @param lastUpdated
     * @return
     */
    @Override
    public Iterable<PointSourceSccCode> updatePointSourceSccCodes(LocalDate lastUpdated) {

        List<SccDetailDto> updatedCodes = this.retrievePointSccDetailsSince(lastUpdated);

        logger.info("Found {} updated Point SCCs", updatedCodes.size());

        Iterable<PointSourceSccCode> result = updateSccs(updatedCodes);

        logger.info("Successfully updated Point SCCs");

        return result;
    }

    /**
     * Retrieve Non-Point SCCs from the webservice since a certain date and update the database with them
     * @param lastUpdated
     * @return
     */
    @Override
    public Iterable<PointSourceSccCode> updateNonPointSourceSccCodes(LocalDate lastUpdated) {

        List<SccDetailDto> updatedCodes = this.retrieveNonPointSccDetailsSince(lastUpdated);

        logger.info("Found {} updated Non-Point SCCs", updatedCodes.size());

        Iterable<PointSourceSccCode> result = updateSccs(updatedCodes);

        logger.info("Successfully updated Non-Point SCCs");

        return result;
    }

    private Iterable<PointSourceSccCode> updateSccs(List<SccDetailDto> updatedCodes) {

        List<PointSourceSccCode> codeEntities = updatedCodes.stream().map(dto -> {
            PointSourceSccCode entity = this.pointSourceSccCodeRepo.findById(dto.getCode()).orElse(new PointSourceSccCode());

            if (entity.getCode() == null) {
                entity.setCode(dto.getCode());
                entity.setFuelUseRequired(false);
                entity.setMonthlyReporting(false);
            }
            if (dto.getAttributes().containsKey("last inventory year")) {
                entity.setLastInventoryYear(Short.valueOf(dto.getAttributes().get("last inventory year").getText()));
            }
            if (dto.getAttributes().containsKey("scc level one")) {
                entity.setSccLevelOne(dto.getAttributes().get("scc level one").getText());
            }
            if (dto.getAttributes().containsKey("scc level two")) {
                entity.setSccLevelTwo(dto.getAttributes().get("scc level two").getText());
            }
            if (dto.getAttributes().containsKey("scc level three")) {
                entity.setSccLevelThree(dto.getAttributes().get("scc level three").getText());
            }
            if (dto.getAttributes().containsKey("scc level four")) {
                entity.setSccLevelFour(dto.getAttributes().get("scc level four").getText());
            }
            if (dto.getAttributes().containsKey("sector")) {
                entity.setSector(dto.getAttributes().get("sector").getText());
            }
            if (dto.getAttributes().containsKey("short name")) {
                entity.setShortName(dto.getAttributes().get("short name").getText());
            }
            if (dto.getAttributes().containsKey("data category")) {
                entity.setCategory(SccCategory.valueOf(dto.getAttributes().get("data category").getText()));
            }
            entity.setLastUpdatedDate(dto.getLastUpdatedDate());

            return entity;
        }).collect(Collectors.toList());

        return this.pointSourceSccCodeRepo.saveAll(codeEntities);
    }
    /**
     * Map SCCs from the webservice into a class with the correct datatypes
     * @param detail
     * @return
     */
    private SccDetailDto mapSccDetail(SccDetail detail) {
        SccDetailDto result = new SccDetailDto();

        result.setUid(detail.getUid());
        result.setCode(detail.getCode());
        result.setLastUpdated(detail.getLastUpdated());
        try {
            DateFormat sccLastUpdatedFormat = new SimpleDateFormat("MMM d, yyyy h:mm:ss aaa");
            sccLastUpdatedFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            result.setLastUpdatedDate(sccLastUpdatedFormat.parse(detail.getLastUpdated()));
        } catch (Exception e) {
            logger.error("Could not map date {} - {}", detail.getLastUpdated(), e.getMessage(), e);
        }
        result.setAttributes(objectMapper.convertValue(detail.getAttributes(),  new TypeReference<Map<String, SccAttributeDto>>() {}));

        return result;
    }
}
