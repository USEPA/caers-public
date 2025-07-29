/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
