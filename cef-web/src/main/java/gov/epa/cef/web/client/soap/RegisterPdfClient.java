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

import gov.epa.cdx.shared.security.ApplicationUser;
import gov.epa.cef.web.config.CdxConfig;
import gov.epa.cef.web.exception.ApplicationErrorCode;
import gov.epa.cef.web.exception.ApplicationException;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.mapper.ApplicationUserMapper;
import net.exchangenetwork.wsdl.register.pdf._1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegisterPdfClient extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(RegisterPdfClient.class);

    private final ApplicationUserMapper userMapper;

    private final SecurityService securityService;

    private final CdxConfig config;

    @Autowired
    RegisterPdfClient(ApplicationUserMapper userMapper, SecurityService securityService, CdxConfig config) {

        this.userMapper = userMapper;
        this.securityService = securityService;
        this.config = config;
    }

    protected RegisterPdfService getClient(boolean mtom, boolean chunking) {

        String endpoint = this.config.getPdfClientEndpoint();

        return this.getClient(endpoint, RegisterPdfService.class, mtom, chunking);
    }

    public PdfDocumentType renderUrlAsPdf(String url) {

        try {
            String token = authenticate();
            HandoffUserType user = userMapper.toHandoffUser(securityService.getCurrentApplicationUser());
            return getClient(false, true).renderUrlAsPdf(token, url,
                HandoffType.RSO, user, PdfRenderingEngine.HEADLESS_CHROME, 300000);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    private String authenticate() {

        try {
            String userId = this.config.getNaasUser();
            String password = this.config.getNaasPassword();

            return getClient(false, true).authenticate(userId, password, DOMAIN, AUTH_METHOD);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    ApplicationException convertFault(RegisterException fault) {
        if (fault.getFaultInfo() == null) {
            return new ApplicationException(ApplicationErrorCode.E_REMOTE_SERVICE_ERROR, fault.getMessage());
        } else {
            ApplicationErrorCode code;
            try {
                code = ApplicationErrorCode.valueOf(fault.getFaultInfo().getErrorCode().value());
            } catch (Exception e) {
                logger.warn("Could not translate fault.");
                code = ApplicationErrorCode.E_REMOTE_SERVICE_ERROR;
            }
            return new ApplicationException(code, fault.getFaultInfo().getDescription());
        }
    }
}
