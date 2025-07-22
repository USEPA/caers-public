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
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@Validated
@ConfigurationProperties(prefix = "cdx")
public class CdxConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Map<String, String> registerEndpointConfiguration;
    private Map<String, String> registerProgramFacilityEndpointConfiguration;
    private Map<String, String> registerSignEndpointConfiguration;
    private Map<String, String> streamlinedRegistrationEndpointConfiguration;
    private Map<String, String> inboxClientConfiguration;
    private Map<String, String> pdfClientConfiguration;

    @NotBlank
    private String naasUser;

    @NotBlank
    private String naasPassword;

    @NotNull
    private URL naasTokenUrl;

    @NotBlank
    private String naasIp;

    @NotBlank
    private String cdxBaseUrl;

    @NotBlank
    private String appBaseUrl;

    private String submissionHistoryUrl;

    private final List<String> allowedOrigins = new ArrayList<>();

    public Map<String, String> getRegisterEndpointConfiguration() {
        return registerEndpointConfiguration;
    }

    public void setRegisterEndpointConfiguration(Map<String, String> registerEndpointConfiguration) {
        this.registerEndpointConfiguration = registerEndpointConfiguration;
    }
    public String getRegisterServiceEndpoint() {
        return getRegisterEndpointConfiguration().get("serviceUrl");
    }

    public Map<String, String> getRegisterProgramFacilityEndpointConfiguration() {
        return registerProgramFacilityEndpointConfiguration;
    }

    public void setRegisterProgramFacilityEndpointConfiguration(Map<String, String> registerProgramFacilityEndpointConfiguration) {
        this.registerProgramFacilityEndpointConfiguration = registerProgramFacilityEndpointConfiguration;
    }
    public String getRegisterProgramFacilityServiceEndpoint() {
        return getRegisterProgramFacilityEndpointConfiguration().get("serviceUrl");
    }

    public Map<String, String> getRegisterSignEndpointConfiguration() {
        return registerSignEndpointConfiguration;
    }

    public void setRegisterSignEndpointConfiguration(Map<String, String> registerSignEndpointConfiguration) {
        this.registerSignEndpointConfiguration = registerSignEndpointConfiguration;
    }
    public String getRegisterSignServiceEndpoint() {
        return getRegisterSignEndpointConfiguration().get("serviceUrl");
    }

	public Map<String, String> getInboxClientConfiguration() {
		return inboxClientConfiguration;
	}

	public void setInboxClientConfiguration(Map<String, String> inboxClientConfiguration) {
		this.inboxClientConfiguration = inboxClientConfiguration;
	}
    public String getInboxClientEndpoint() {
        return getInboxClientConfiguration().get("serviceUrl");
    }

    public Map<String, String> getPdfClientConfiguration() {
        return pdfClientConfiguration;
    }

    public void setPdfClientConfiguration(Map<String, String> pdfClientConfiguration) {
        this.pdfClientConfiguration = pdfClientConfiguration;
    }
    public String getPdfClientEndpoint() {
        return getPdfClientConfiguration().get("serviceUrl");
    }

    public Map<String, String> getStreamlinedRegistrationEndpointConfiguration() {
        return streamlinedRegistrationEndpointConfiguration;
    }

    public void setStreamlinedRegistrationEndpointConfiguration(Map<String, String> streamlinedRegistrationEndpointConfiguration) {
        this.streamlinedRegistrationEndpointConfiguration = streamlinedRegistrationEndpointConfiguration;
    }
    public String getStreamlinedRegistrationServiceEndpoint() {
        return getStreamlinedRegistrationEndpointConfiguration().get("serviceUrl");
    }

    public String getNaasUser() {
        return naasUser;
    }

    public void setNaasUser(String naasUser) {
        this.naasUser = naasUser;
    }

    public String getNaasPassword() {
        return naasPassword;
    }

    public void setNaasPassword(String naasPassword) {
        this.naasPassword = naasPassword;
    }

    public URL getNaasTokenUrl() {
        return naasTokenUrl;
    }

    public void setNaasTokenUrl(URL naasTokenUrl) {
        this.naasTokenUrl = naasTokenUrl;
    }

    public String getNaasIp() {
        return naasIp;
    }

    public void setNaasIp(String naasIp) {
        this.naasIp = naasIp;
    }

    public String getCdxBaseUrl() {
        return cdxBaseUrl;
    }

    public void setCdxBaseUrl(String cdxBaseUrl) {
        this.cdxBaseUrl = cdxBaseUrl;
    }

    public String getAppBaseUrl() {
        return appBaseUrl;
    }

    public void setAppBaseUrl(String appBaseUrl) {
        this.appBaseUrl = appBaseUrl;
    }

    public String getSubmissionHistoryUrl() {
		return submissionHistoryUrl;
	}

	public void setSubmissionHistoryUrl(String submissionHistoryUrl) {
		this.submissionHistoryUrl = submissionHistoryUrl;
	}

    public List<String> getAllowedOrigins() {

        return allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {

        this.allowedOrigins.clear();
        if (allowedOrigins != null) {
            this.allowedOrigins.addAll(allowedOrigins);
        }
    }

    CorsConfigurationSource createCorsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(this.allowedOrigins);

        config.setAllowedMethods(
            Arrays.asList("GET", "POST", "DELETE", "PUT", "HEAD", "PATCH"));
        config.setAllowedHeaders(
            Arrays.asList("Authorization", "Cache-Control", "Content-Type", "X-XSRF-TOKEN", "X-Requested-With"));

        UrlBasedCorsConfigurationSource result = new UrlBasedCorsConfigurationSource();
        result.registerCorsConfiguration("/**", config);

        logger.info("Created CORS Configuration w/ allowedOrigins: {}",
            String.join(", ", this.allowedOrigins));

        return result;
    }

}
