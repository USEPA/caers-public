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
