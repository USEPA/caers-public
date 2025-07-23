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
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URL;

@Validated
public class NodeEndpointConfig {

    @NotBlank
    private String dataflow;

    @NotNull
    private URL serviceUrl;

    public String getDataflow() {

        return dataflow;
    }

    public void setDataflow(String dataflow) {

        this.dataflow = dataflow;
    }

    public URL getServiceUrl() {

        return serviceUrl;
    }

    public void setServiceUrl(URL serviceUrl) {

        this.serviceUrl = serviceUrl;
    }

    @Override
    public String toString() {

        return MoreObjects.toStringHelper(this)
            .add("dataflow", dataflow)
            .add("serviceUrl", serviceUrl)
            .toString();
    }
}
