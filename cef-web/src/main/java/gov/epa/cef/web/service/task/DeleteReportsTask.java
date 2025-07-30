/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.task;

import gov.epa.cef.web.config.AppPropertyName;
import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.provider.system.AdminPropertyProvider;
import gov.epa.cef.web.service.EmissionsReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DeleteReportsTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EmissionsReportService reportService;

    @Autowired
    private AdminPropertyProvider propertyProvider;

    /**
     * Run task to delete reports that are marked for deletion
     */
    public void run() {

        if (this.propertyProvider.getBoolean(AppPropertyName.DeleteReportsTaskEnabled, false)) {
            try {
                logger.info("Delete Reports Task running");

                this.propertyProvider.update(AppPropertyName.LastDeleteReportsDate, LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

                int deletedCount = 0;
                List<EmissionsReport> reportsToDelete = reportService.getReportsToDelete();
                if (reportsToDelete != null && reportsToDelete.size() > 0) {
                    for (EmissionsReport er : reportsToDelete) {
                        reportService.delete(er.getId());
                        deletedCount += 1;
                    }
                }
                logger.info("Delete Reports Task finish - reports delete: " + deletedCount);
            } catch (Exception e) {
                logger.error("Exception thrown while deleting reports", e);
            }
        }
    }

}
