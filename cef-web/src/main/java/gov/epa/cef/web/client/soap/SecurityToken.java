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
package gov.epa.cef.web.client.soap;

import com.google.common.base.Ascii;
import com.google.common.base.Charsets;
import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Base64;

public class SecurityToken implements Serializable {

    private static final long serialVersionUID = 1L;

    private OffsetDateTime created;

    private String ip;

    private String token;

    public SecurityToken() {

        super();
    }

    public String asCombinedSsoToken() {

        String combined = String.format("token=%s&remoteIpAddress=%s", this.token, this.ip);
        return Base64.getEncoder().encodeToString(combined.getBytes(Charsets.UTF_16LE));
    }

    public OffsetDateTime getCreated() {

        return this.created;
    }

    public void setCreated(OffsetDateTime created) {

        this.created = created;
    }

    public String getIp() {

        return this.ip;
    }

    public void setIp(String ip) {

        this.ip = ip;
    }

    public String getToken() {

        return this.token;
    }

    public void setToken(String token) {

        this.token = token;
    }

    @Override
    public String toString() {

        return MoreObjects.toStringHelper(this)
                .add("created", created)
                .add("ip", ip)
                .add("token", Ascii.truncate(token, 15, "..."))
                .toString();
    }

    public SecurityToken withCreated(final OffsetDateTime created) {

        setCreated(created);
        return this;
    }

    public SecurityToken withIp(final String ip) {

        setIp(ip);
        return this;
    }

    public SecurityToken withToken(final String token) {

        setToken(token);
        return this;
    }

}
