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
package gov.epa.cef.web.client.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import gov.epa.cef.web.client.soap.SecurityToken;
import gov.epa.cef.web.client.soap.SecurityTokenClient;
import gov.epa.cef.web.util.TempFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import static java.net.HttpURLConnection.HTTP_OK;

@Component
public class ExcelParserClient {

    private final ExcelParserClientConfig config;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ObjectMapper objectMapper;

    private final SecurityTokenClient tokenClient;

    @Autowired
    ExcelParserClient(ExcelParserClientConfig config,
                      SecurityTokenClient tokenClient,
                      ObjectMapper objectMapper) {

        this.config = config;
        this.tokenClient = tokenClient;
        this.objectMapper = objectMapper;
    }

    public ExcelParserResponse parseWorkbook(TempFile tempFile) {

        return parseWorkbook(tempFile.getFileName(), tempFile.getFile());
    }

    ExcelParserResponse parseWorkbook(String filename, File workbook) {

        JsonNode result = null;
        int responseCode = -1;

        Profiler profiler = new Profiler("ExcelParser");
        profiler.setLogger(logger);

        SecurityToken token = this.tokenClient.createSecurityToken("-=>CaerUser<=-", "127.0.0.1");

        URL url = makeUrl("/parse");
        HttpURLConnection http = null;

        try {

            http = (HttpURLConnection) url.openConnection();

            String boundary = UUID.randomUUID().toString();

            http.setRequestMethod("POST");
            http.setDoOutput(true);

            http.setRequestProperty("Content-Type", "multipart/form-data;boundary=".concat(boundary));
            http.setRequestProperty("X-SSO-Token", token.asCombinedSsoToken());

            try (DataOutputStream request = new DataOutputStream(http.getOutputStream())) {

                String boundaryBegLine = String.format("--%s\r\n", boundary);
                String boundaryEndLine = String.format("\r\n--%s--\r\n", boundary);

                request.writeBytes(boundaryBegLine);
                request.writeBytes(String.format(
                    "Content-Disposition: form-data; name=\"workbook\"; filename=\"%s\"\r\n\r\n",
                    filename));

                try (InputStream fileStream = new FileInputStream(workbook)) {

                    ByteStreams.copy(fileStream, request);
                }

                request.writeBytes(boundaryEndLine);

                request.flush();
            }

            responseCode = http.getResponseCode();

            logger.debug("Parser returned response code {}", responseCode);

            try (InputStream inputStream =
                     responseCode == HTTP_OK ? http.getInputStream() : http.getErrorStream()) {

                result = this.objectMapper.readTree(inputStream);
            }

        } catch (IOException e) {

            throw new IllegalStateException(e);

        } finally {

            profiler.stop().log();

            if (http != null) {
                http.disconnect();
            }
        }

        return new ExcelParserResponse(responseCode, result);
    }

    private URL makeUrl(String path) {

        URL result = null;

        try {

            result = new URL(String.format("%s%s", this.config.getBaseUrl().toString(), path));

        } catch (MalformedURLException e) {

            throw new IllegalArgumentException(e);
        }

        return result;
    }

    @Component
    @Validated
    @ConfigurationProperties(prefix = "excel-parser")
    public static class ExcelParserClientConfig {

        @NotNull
        private URL baseUrl;

        public URL getBaseUrl() {

            return baseUrl;
        }

        public void setBaseUrl(URL baseUrl) {

            this.baseUrl = baseUrl;
        }

        public ExcelParserClientConfig withBaseUrl(final URL baseUrl) {

            setBaseUrl(baseUrl);
            return this;
        }
    }
}
