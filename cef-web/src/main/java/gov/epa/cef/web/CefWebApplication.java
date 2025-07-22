/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
