/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.client.soap;

import gov.epa.cdx.shared.generatedwsdl.securitytoken._3.AuthMethod;
import gov.epa.cdx.shared.generatedwsdl.securitytoken._3.DomainTypeCode;
import gov.epa.cdx.shared.generatedwsdl.securitytoken._3.SecurityTokenPortType;
import gov.epa.cdx.shared.generatedwsdl.securitytoken._3.TokenType;
import gov.epa.cef.web.config.CdxConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.OffsetDateTime;

/**
 * A client to consume the CDX SecurityToken service
 *
 * @author ahmahfou
 */
@Component
public class SecurityTokenClient extends AbstractClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterFacilityClient.class);

    private final CdxConfig config;

    @Autowired
    public SecurityTokenClient(CdxConfig config) {

        this.config = config;
    }

    /**
     * Create a SecurityTokenPortType for invoking service methods
     *
     * @param endpoint
     * @return
     */
    protected SecurityTokenPortType getClient(URL endpoint) {

        return this.getClient(endpoint.toString(),
            SecurityTokenPortType.class, false, true);
    }

    /**
     * Creates a new NAAS toke for the user using the admin user credentials
     *
     * @param userId
     * @param ip
     * @return
     */
    public SecurityToken createSecurityToken(String userId, String ip) {

        String result = null;

        try {
            result = getClient(this.config.getNaasTokenUrl())
                .createSecurityToken(this.config.getNaasUser(), this.config.getNaasPassword(), DomainTypeCode.DEFAULT,
                    TokenType.CSM, this.config.getNaasUser(), AuthMethod.PASSWORD, userId, "userId=" + userId, ip);
        } catch (Exception e) {
            throw this.handleException(e, LOGGER);
        }

        return new SecurityToken().withCreated(OffsetDateTime.now()).withIp(ip).withToken(result);
    }
}
