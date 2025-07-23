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
import gov.epa.cef.web.provider.system.AdminPropertyProvider;
import gov.epa.cef.web.service.NotificationService;
import gov.epa.cef.web.service.dto.UserFacilityAssociationDto;
import gov.epa.cef.web.service.impl.UserFacilityAssociationServiceImpl;
import net.exchangenetwork.wsdl.register.streamlined._1.RegistrationNewUserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RemoveNullCdxUsersTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserFacilityAssociationServiceImpl userFacilityAssociationService;

    @Autowired
    private AdminPropertyProvider propertyProvider;

    /**
     * Run task to deauthorize users who no longer have CAER roles in CDX
     */
    @Transactional
    public void run() {

        if (this.propertyProvider.getBoolean(AppPropertyName.RemoveNullUsersTaskEnabled, false)) {
            try {
                logger.info("Remove Null CDX Users Task running");

                this.propertyProvider.update(AppPropertyName.LastRemoveNullUsersDate, LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

                List<UserFacilityAssociationDto> associations = this.userFacilityAssociationService.getAllActiveAssociations();
                for (UserFacilityAssociationDto association : associations) {
                    RegistrationNewUserProfile profile = this.userFacilityAssociationService.getProfile(association);
                    if (profile == null) {
                        logger.info("Removing user " + association.getCdxUserId() + " from facility " + association.getMasterFacilityRecord().getName());
                        this.userFacilityAssociationService.deleteById(association.getId(), "SYSTEM");
                    }
                }

                logger.info("Remove Null CDX Users Task finish");

            } catch (Exception e) {
                logger.error("Exception thrown while removing null CDX users", e);
                this.notificationService.sendRemoveNullUsersFailedNotification(e);
            }
        }
    }

}
