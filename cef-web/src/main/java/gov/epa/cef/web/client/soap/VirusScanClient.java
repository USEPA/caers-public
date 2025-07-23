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

import com.google.common.collect.ImmutableMap;
import gov.epa.cef.web.config.CdxConfig;
import gov.epa.cef.web.exception.VirusScanException;
import gov.epa.cef.web.provider.system.IPropertyKey;
import gov.epa.cef.web.provider.system.AdminPropertyProvider;
import gov.epa.cef.web.service.NotificationService;
import gov.epa.cef.web.util.TempFile;
import net.exchangenetwork.schema.validator._3.SchemaType;
import net.exchangenetwork.schema.validator._3.ValidationType;
import net.exchangenetwork.wsdl.validator._3.ValidatorPortType3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.validation.constraints.NotNull;
import java.net.URL;
import java.util.UUID;

@Component
public class VirusScanClient extends AbstractClient {

    private static final String VIRUS_SCAN_CLEAN = "Congratulations! The document,.*, is clean, no virus detected.";
    private static final String VIRUS_SCAN_DETECTED = "Virus .* found in the submitted file. Please delete the file immediately!";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AdminPropertyProvider propertyProvider;

    private final CdxConfig cdxConfig;

    private final VirusScanConfig virusScanConfig;

    private final NotificationService notificationService;

    @Autowired
    VirusScanClient(NotificationService notificationService,
                    AdminPropertyProvider propertyProvider,
                    CdxConfig cdxConfig,
                    VirusScanConfig virusScanConfig) {

        this.notificationService = notificationService;
        this.propertyProvider = propertyProvider;
        this.cdxConfig = cdxConfig;
        this.virusScanConfig = virusScanConfig;
    }

    public boolean isEnabled() {

        return this.propertyProvider.getBoolean(VirusScanProperty.VirusScannerEnabled, false);
    }

    public void scanFile(TempFile tempFile) {

        if (isEnabled()) {

            doScanFile(tempFile);
        }
    }

    private void doScanFile(TempFile tempFile) {

        ValidatorPortType3 virusScanner = getClient(this.virusScanConfig.getEndpoint(),
                ValidatorPortType3.class, true, true);


        String result = "Waiting for response...";
        try {

            // schema type isn't used
            result = virusScanner.validateDocument(this.cdxConfig.getNaasUser(), this.cdxConfig.getNaasPassword(), 
                    "default", ValidationType.VIRUSSCAN, SchemaType.EIS_V_1_0, new DataHandler(new FileDataSource(tempFile.getFile())), "bin", null);

        } catch (Exception e) {

            // log the exception we are about to eat
            logger.warn("Eating exception", e);

            result = e.getMessage();

            // unknown exception, eat it and send email to admins
            // TODO this should be async, suggest to an event/message queue to decouple actions
            this.notificationService.sendAdminNotification(
                NotificationService.AdminEmailType.VirusScanFailure,
                ImmutableMap.of("exception", e));

        } finally {

            logger.info("VirusScan returned: {}", result);
        }

        if (result.matches(VIRUS_SCAN_DETECTED)) {

            throw new VirusScanException(tempFile, result);

        } else if (!result.matches(VIRUS_SCAN_CLEAN)) {

            // if we get here then the result should be "clean file"
            // otherwise send an email for unexpected result
            this.notificationService.sendAdminNotification(
                NotificationService.AdminEmailType.VirusScanFailure,
                ImmutableMap.of("exception",
                    new IllegalStateException("Unexpected result returned: ".concat(result))));
        }
    }

    private static enum VirusScanProperty implements IPropertyKey {

        VirusScannerEnabled("virus-scanner.enabled");

        private final String configKey;

        VirusScanProperty(String configKey) {

            this.configKey = configKey;
        }

        @Override
        public String configKey() {

            return this.configKey;
        }
    }

    @Component
    @Validated
    @ConfigurationProperties(prefix = "virus-scanner")
    public static class VirusScanConfig {

        @NotNull
        private URL endpoint;

        public URL getEndpoint() {

            return endpoint;
        }

        public void setEndpoint(URL endpoint) {

            this.endpoint = endpoint;
        }

        public VirusScanConfig withEndpoint(final URL endpoint) {

            setEndpoint(endpoint);
            return this;
        }
    }
}
