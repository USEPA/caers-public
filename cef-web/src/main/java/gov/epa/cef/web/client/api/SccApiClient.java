/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.client.api;

import gov.epa.cef.web.service.dto.SccResourceSearchApiDto;
import gov.epa.client.sccwebservices.ApiClient;
import gov.epa.client.sccwebservices.SccSearchApiApi;
import gov.epa.client.sccwebservices.model.SccDetail;

import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.sun.istack.NotNull;

@Component
public class SccApiClient {

    private final SccSearchApiApi client;

    private final SccConfig config;

    @Autowired
    SccApiClient(SccConfig config) {

        super();

        this.config = config;

        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(this.config.getSccwebservicesEndpoint().toString());

        this.client = new SccSearchApiApi(apiClient);
    }

    public List<SccDetail> getResourceSearchResults(SccResourceSearchApiDto dto) {

        return this.client.getResourceSearchResults(dto.getFacetName(), dto.getFacetValue(), 
                dto.getFacetQualifier(), dto.getFacetMatchType(), dto.getParamCallback(), dto.getFormat(), 
                dto.getFilename(), dto.getLastUpdatedSinceString(), dto.getSortFacet(), dto.getPageNum(), dto.getPageSize());
    }


    @Component
    @Validated
    @ConfigurationProperties(prefix = "scc")
    public static class SccConfig {

        @NotNull
        private URL sccwebservicesEndpoint;

        public URL getSccwebservicesEndpoint() {
            return sccwebservicesEndpoint;
        }

        public void setSccwebservicesEndpoint(URL sccwebservicesEndpoint) {
            this.sccwebservicesEndpoint = sccwebservicesEndpoint;
        }


    }
}
