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
package gov.epa.cef.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

//  security config for new API export report features , June 2023
@Configuration
@EnableWebSecurity
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ResourceServerConfig.class);

    @Value("${jwt.public-key-text}")
    private String publicKey;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        logger.info("resource server http security config started");
        http
            .antMatcher("/api/public/export/report/**")
            .authorizeRequests()
            .antMatchers("/api/public/export/report/healthCheck").permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/api/public/export/report/**").permitAll()
            .antMatchers(HttpMethod.GET, "/api/public/export/report/**").access("#oauth2.hasScope('slt:export')")
            //.antMatchers(HttpMethod.POST, "/api/public/export/report/**").access("#oauth2.hasScope('slt:export')") // seems we don't need these methods
            //.antMatchers(HttpMethod.PUT, "/api/public/export/report/**").access("#oauth2.hasScope('slt:export')")
            //.antMatchers(HttpMethod.DELETE, "/api/public/export/report/**").access("#oauth2.hasScope('slt:export')")
            .antMatchers("/api/**").denyAll() // everything else
            .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();

        try {
            accessTokenConverter.setVerifier(new RsaVerifier((publicKey)));
            logger.info("successfully read public key");
        } catch (Exception e) {
            logger.error("failed to read public key, error: " + e.toString());
        }

        JwtTokenStore tokenStore = new JwtTokenStore(accessTokenConverter);
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenStore(tokenStore);
        resources.tokenServices(tokenServices);
    }
}

