/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.client.soap;

import net.exchangenetwork.wsdl.register.inbox._1.*;
import net.exchangenetwork.wsdl.register.inbox._1.RegisterException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.epa.cef.web.config.CdxConfig;
import gov.epa.cef.web.exception.ApplicationErrorCode;
import gov.epa.cef.web.exception.ApplicationException;

@Component
public class InboxClient extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(InboxClient.class);

    private final CdxConfig config;
    
    @Autowired
    public InboxClient(CdxConfig config) {

        this.config = config;

        logger.info("Initialised InboxService with {}", this.config);
    }

    protected RegisterInboxService getClient(boolean mtom, boolean chunking) {

        String endpoint = this.config.getInboxClientEndpoint();

        return this.getClient(endpoint, RegisterInboxService.class, mtom, chunking);
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
    
    public void sendCdxInboxMessage(String cdxUserId, String from,
						    		String subject, String body) {

        try {
            String token = authenticate();
            getClient(false, true).createInboxMessage(token, cdxUserId,
            										  from, subject, body);
        } catch (RegisterException fault) {
            throw this.handleException(convertFault(fault), logger);
        } catch (Exception e) {
            throw this.handleException(e, logger);
        }
    }
}
