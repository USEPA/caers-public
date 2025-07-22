/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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

