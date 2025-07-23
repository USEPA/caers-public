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

import gov.epa.cef.web.domain.NaicsCodeIndustry;
import gov.epa.cef.web.repository.NaicsCodeIndustryRepository;
import gov.epa.cef.web.service.NaicsCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class NaicsCodeServiceImpl implements NaicsCodeService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NaicsCodeIndustryRepository naicsIndustryRepo;

    /**
     * Retrieve all naics code industries
     */
    public List<NaicsCodeIndustry> getNaicsIndustries() {
        Iterable<NaicsCodeIndustry> entities = naicsIndustryRepo.findAll();

        List<NaicsCodeIndustry> result = new ArrayList<>();
        entities.forEach(result::add);

        return result;
    }
}
