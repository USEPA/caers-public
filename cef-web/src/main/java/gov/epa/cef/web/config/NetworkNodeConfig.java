/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.config;

import com.google.common.base.MoreObjects;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;

@Component
@Validated
@ConfigurationProperties
public class NetworkNodeConfig {

    @NotEmpty
    private Map<NetworkNodeName, NodeEndpointConfig> networkNodes = new HashMap<>();

    public NodeEndpointConfig getNetworkNode(NetworkNodeName name) {

        return this.networkNodes.get(name);
    }

    public Map<NetworkNodeName, NodeEndpointConfig> getNetworkNodes() {

        return networkNodes;
    }

    public void setNetworkNodes(Map<NetworkNodeName, NodeEndpointConfig> networkNodes) {

        this.networkNodes.clear();
        if (networkNodes != null) {
            this.networkNodes.putAll(networkNodes);
        }
    }

    @Override
    public String toString() {

        return MoreObjects.toStringHelper(this)
            .add("networkNodes", networkNodes)
            .toString();
    }
}
