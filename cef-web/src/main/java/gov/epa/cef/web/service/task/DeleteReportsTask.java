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
