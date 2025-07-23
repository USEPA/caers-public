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
package gov.epa.cef.web;

import gov.epa.cdx.shared.hosting.AzureLogbackFilter;
import gov.epa.cdx.shared.hosting.HealthCheckServlet;
import gov.epa.cef.web.config.YamlPropertySourceFactory;

import java.util.TimeZone;

import javax.servlet.http.HttpServlet;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@PropertySources({
    @PropertySource(factory = YamlPropertySourceFactory.class,
        value = "file:${spring.config.dir}/oar-cef-web/cef-web-config.yml", ignoreResourceNotFound=true)
})
public class CefWebApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return configureApplication(builder);
    }

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        configureApplication(new SpringApplicationBuilder()).run(args);
    }

    private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
        return builder.sources(CefWebApplication.class);
    }

    /**
     * Servlet for AKS healthcheck
     */
    @Bean
    public ServletRegistrationBean<HttpServlet> healthCheck() {
        ServletRegistrationBean<HttpServlet> servlet = new ServletRegistrationBean<>();
        servlet.setServlet(new HealthCheckServlet());
        servlet.addUrlMappings("/HealthCheck");
        servlet.setLoadOnStartup(1);
        return servlet;
    }

    /**
     * Log filter for AKS logging
     */
    @Bean
    public FilterRegistrationBean azureLogbackFilter() {
        FilterRegistrationBean filter = new FilterRegistrationBean();
        filter.setFilter(new AzureLogbackFilter());
        filter.addUrlPatterns("/*");
        return filter;
    }
}
