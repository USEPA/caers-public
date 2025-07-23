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
package gov.epa.cef.web.client.soap;

import gov.epa.cef.web.config.CdxConfig;
import gov.epa.cef.web.exception.ApplicationErrorCode;
import gov.epa.cef.web.exception.ApplicationException;
import net.exchangenetwork.wsdl.register.program_facility._1.ProgramFacility;
import net.exchangenetwork.wsdl.register.program_facility._1.RegisterProgramFacilityService;
import net.exchangenetwork.wsdl.register.program_facility._1.RegistrationFacility;
import net.exchangenetwork.wsdl.register.program_facility._1.RegistrationOrganization;
import net.exchangenetwork.wsdl.register.program_facility._1.RegistrationProgramFacilityNaicsCode;
import net.exchangenetwork.wsdl.register.program_facility._1.RegistrationRoleType;
import net.exchangenetwork.wsdl.register.program_facility._1.RegistrationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class RegisterFacilityClient extends AbstractClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterFacilityClient.class);

    private final CdxConfig config;

    @Autowired
    RegisterFacilityClient(CdxConfig config) {

        this.config = config;
    }

    /**
     * Retrieve CDX facilities associated with the user
     *
     * @param user
     * @param org
     * @param roleType
     * @return
     * @throws ApplicationException
     */
    public List<RegistrationFacility> getFacilities(RegistrationUser user, RegistrationOrganization org, RegistrationRoleType roleType) {

        try {
            String token = authenticate();
            return getClient(false, true).retrieveFacilities(token, user, org, roleType);
        } catch (net.exchangenetwork.wsdl.register.program_facility._1.RegisterException fault) {
            throw this.handleException(convertFault(fault), LOGGER);
        } catch (Exception e) {
            throw this.handleException(e, LOGGER);
        }
    }

    /**
     * Retrieve CDX facilities associated with the user
     *
     * @param userRoleId
     * @return
     * @throws ApplicationException
     */
    public List<ProgramFacility> getFacilitiesByUserRoleId(Long userRoleId) {

        try {
            String token = authenticate();
            return getClient(false, true).retrieveFacilitiesByUserRoleId(token, userRoleId);
        } catch (net.exchangenetwork.wsdl.register.program_facility._1.RegisterException fault) {
            throw this.handleException(convertFault(fault), LOGGER);
        } catch (Exception e) {
            throw this.handleException(e, LOGGER);
        }
    }

    /**
     * Retrieve CDX facilities by id
     *
     * @param programId
     * @return
     * @throws ApplicationException
     */
    public List<ProgramFacility> getFacilityByProgramId(String programId) {

        return getFacilityByProgramIds(Collections.singletonList(programId));
    }

    /**
     * Retrieve CDX facilities by id
     *
     * @param programIds
     * @return
     * @throws ApplicationException
     */
    public List<ProgramFacility> getFacilityByProgramIds(List<String> programIds) {

        try {
            String token = authenticate();
            return getClient(false, true).retrieveProgramFacilitiesByProgramIds(token, programIds);
        } catch (net.exchangenetwork.wsdl.register.program_facility._1.RegisterException fault) {
            throw this.handleException(convertFault(fault), LOGGER);
        } catch (Exception e) {
            throw this.handleException(e, LOGGER);
        }
    }

    /**
     * Convert Register Service exceptions into application exceptions
     *
     * @param fault
     * @return
     */
    ApplicationException convertFault(net.exchangenetwork.wsdl.register.program_facility._1.RegisterException fault) {

        if (fault.getFaultInfo() == null) {
            return new ApplicationException(ApplicationErrorCode.E_REMOTE_SERVICE_ERROR, fault.getMessage());
        } else {
            ApplicationErrorCode code;
            try {
                code = ApplicationErrorCode.valueOf(fault.getFaultInfo().getErrorCode().value());
            } catch (Exception e) {
                LOGGER.warn("Could not translate fault.", e);
                code = ApplicationErrorCode.E_REMOTE_SERVICE_ERROR;
            }
            return new ApplicationException(code, fault.getFaultInfo().getDescription());
        }
    }

    /**
     * Create a RegisterProgramFacilityService for invoking service methods
     *
     * @param mtom
     * @param chunking
     * @return
     */
    protected RegisterProgramFacilityService getClient(boolean mtom, boolean chunking) {

        String endpoint = this.config.getRegisterProgramFacilityServiceEndpoint();

        return this.getClient(endpoint, RegisterProgramFacilityService.class, mtom, chunking);
    }

    /**
     * Create a NAAS token for authentication into Register services
     *
     * @return
     * @throws ApplicationException
     */
    private String authenticate() {

        try {
            String userId = this.config.getNaasUser();
            String password = this.config.getNaasPassword();

            return getClient(false, true).authenticate(userId, password, DOMAIN, AUTH_METHOD);
        } catch (net.exchangenetwork.wsdl.register.program_facility._1.RegisterException fault) {
            throw this.handleException(convertFault(fault), LOGGER);
        } catch (Exception e) {
            throw this.handleException(e, LOGGER);
        }
    }
}
