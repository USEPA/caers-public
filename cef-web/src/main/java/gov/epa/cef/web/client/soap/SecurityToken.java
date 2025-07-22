/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
