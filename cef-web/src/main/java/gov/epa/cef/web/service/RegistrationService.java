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
package gov.epa.cef.web.service;

import gov.epa.cef.web.exception.ApplicationException;
import net.exchangenetwork.wsdl.register.program_facility._1.ProgramFacility;

import java.util.List;

public interface RegistrationService {

    /**
     * Retrieve CDX facilities associated with the user
     *
     * @param userRoleId
     * @return
     * @throws ApplicationException
     */
    List<ProgramFacility> retrieveFacilities(Long userRoleId);

    /**
     * Retrieve CDX facilities by program ids
     *
     * @param programIds
     * @return
     * @throws ApplicationException
     */
    List<ProgramFacility> retrieveFacilityByProgramIds(List<String> programIds);

    /**
     * Retrieve CDX facility by id
     *
     * @param programId
     * @return
     */
    ProgramFacility retrieveFacilityByProgramId(String programId);
}
