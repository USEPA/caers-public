/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
