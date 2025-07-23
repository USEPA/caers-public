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

import net.exchangenetwork.wsdl.register.sign._1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import gov.epa.cef.web.exception.ApplicationErrorCode;
import gov.epa.cef.web.exception.ApplicationException;

import java.net.URL;

/**
 * @author dfladung
 */
@Component
public class SignatureServiceClient extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(SignatureServiceClient.class);

    protected RegisterSignService getClient(URL endpoint, boolean mtom, boolean chunking) {

        return this.getClient(endpoint.toString(), RegisterSignService.class, mtom, chunking);
    }

    public String sign(URL endpoint, String token, String activityId, SignatureDocumentType document) throws ApplicationException {
        try {
            return getClient(endpoint, true, true).sign(token, activityId, document);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public SignatureDocumentType downloadByDocumentId(URL endpoint, String token, String activityId, String documentId)
            throws ApplicationException {
        try {
            return getClient(endpoint, true, true).downloadByDocumentId(token, activityId, documentId);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }

    public String authenticate(URL endpoint, String adminUserId, String adminPassword) throws ApplicationException {
        try {
            return getClient(endpoint, false, true).authenticate(adminUserId, adminPassword, DOMAIN, AUTH_METHOD);
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
