/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
