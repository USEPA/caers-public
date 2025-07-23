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

import gov.epa.cdx.shared.security.naas.CdxHandoffPreAuthenticationFilter;
import gov.epa.cdx.shared.security.naas.HandoffToCdxServlet;
import gov.epa.cef.web.security.AccessDeniedHandlerImpl;
import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.security.AuthenticationEntryPointImpl;
import gov.epa.cef.web.security.AuthenticationSuccessHandlerImpl;
import gov.epa.cef.web.security.LogoutSuccessHandlerImpl;
import gov.epa.cef.web.security.UserDetailsServiceImpl;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.servlet.http.HttpServlet;
import java.util.ArrayList;
import java.util.List;

import static gov.epa.cef.web.controller.HandoffLandingController.HANDOFF_LANDING_PATH;

@Profile("prod")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@ComponentScan(basePackages = {"gov.epa.cdx.shared"})
@ImportResource(locations = {"file:${spring.config.dir}/oar-cef-web/cdx-shared-config.xml"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String J2AHandoffUrl = "/J2AHandoff";

    @Override
    @DependsOn("cdxConfig")
    protected void configure(HttpSecurity http) throws Exception {

        CdxConfig cdxConfig = getApplicationContext().getBean(CdxConfig.class);
        String loginUrl = cdxConfig.getCdxBaseUrl().concat("/CDX/Login");
        String logoutUrl = cdxConfig.getCdxBaseUrl().concat("/CDX/Logout");

        http.exceptionHandling()
            .authenticationEntryPoint(new AuthenticationEntryPointImpl(loginUrl))
            .accessDeniedHandler(new AccessDeniedHandlerImpl(loginUrl)).and()
            .headers().cacheControl().and()
            .frameOptions().disable().and()
            .csrf().ignoringAntMatchers(HANDOFF_LANDING_PATH)
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
            .cors().configurationSource(cdxConfig.createCorsConfigurationSource()).and()
            .addFilter(cdxWebPreAuthFilter())
            .authorizeRequests()
            .antMatchers("/api/public/**").permitAll()
            .antMatchers(
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/webjars/**"
            ).hasRole(AppRole.RoleType.CAERS_ADMIN.roleName())
            .antMatchers("/**")
            .hasAnyRole(
                AppRole.RoleType.PREPARER.roleName(),
                AppRole.RoleType.NEI_CERTIFIER.roleName(),
                AppRole.RoleType.REVIEWER.roleName(),
                AppRole.RoleType.CAERS_ADMIN.roleName())
            .anyRequest().denyAll().and()
            .logout().logoutSuccessHandler(new LogoutSuccessHandlerImpl(logoutUrl));
    }

    /**
     * Remove security for /HealthCheck servlet so AKS can access it
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
         web.ignoring()
            .antMatchers("/HealthCheck");
    }

    @Bean
    ServletRegistrationBean<HttpServlet> ngnToCdxWebHandoff() {
        ServletRegistrationBean<HttpServlet> servRegBean = new ServletRegistrationBean<>();
        servRegBean.setServlet(new HandoffToCdxServlet());
        servRegBean.addUrlMappings(J2AHandoffUrl);
        servRegBean.setLoadOnStartup(1);
        return servRegBean;
    }

    @Bean
    AbstractPreAuthenticatedProcessingFilter cdxWebPreAuthFilter() {

        CdxHandoffPreAuthenticationFilter result = new CdxHandoffPreAuthenticationFilter();
        result.setAuthenticationManager(authenticationManager());
        result.setAuthenticationSuccessHandler(new AuthenticationSuccessHandlerImpl());

        return result;
    }

    @Bean
    @Override
    @DependsOn("userDetailsServiceImpl")
    protected AuthenticationManager authenticationManager() {

        List<AuthenticationProvider> providers = new ArrayList<>();

        PreAuthenticatedAuthenticationProvider cdxPreAuth = new PreAuthenticatedAuthenticationProvider();
        UserDetailsServiceImpl cefUserDetailService =
            getApplicationContext().getBean(UserDetailsServiceImpl.class);
        cdxPreAuth.setPreAuthenticatedUserDetailsService(cefUserDetailService);
        providers.add(cdxPreAuth);

        return new ProviderManager(providers);
    }
}
