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
