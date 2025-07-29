/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
