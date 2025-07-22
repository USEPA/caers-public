/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import gov.epa.cef.web.config.AppPropertyName;
import gov.epa.cef.web.provider.system.AdminPropertyProvider;
import gov.epa.cef.web.service.NotificationService;
import gov.epa.cef.web.service.SccService;

public class SccUpdateTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SccService sccService;

    @Autowired 
    private NotificationService notificationService;

    @Autowired
    private AdminPropertyProvider propertyProvider;

    /**
     * Run task to retrieve SCC codes from the webservice that have been updated since this was
     * last run and then update the database with the information from them.
     */
    public void run() {

        if (this.propertyProvider.getBoolean(AppPropertyName.SccUpdateTaskEnabled, false)) {
            try {
                logger.info("SCC Update Task running");
                this.sccService.updatePointSourceSccCodes(this.propertyProvider.getLocalDate(AppPropertyName.LastSccUpdateDate));
                this.sccService.updateNonPointSourceSccCodes(this.propertyProvider.getLocalDate(AppPropertyName.LastSccUpdateDate));
                this.propertyProvider.update(AppPropertyName.LastSccUpdateDate, LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
                logger.info("SCC Update Task finish");
            } catch (Exception e) {
                logger.error("Exception thrown while updating SCC Codes", e);
                this.notificationService.sendSccUpdateFailedNotification(e);
            }
        }
    }

}
