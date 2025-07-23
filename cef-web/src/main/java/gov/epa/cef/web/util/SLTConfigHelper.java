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
package gov.epa.cef.web.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.epa.cef.web.config.SLTBaseConfig;
import gov.epa.cef.web.config.SLTPropertyName;
import gov.epa.cef.web.config.slt.SLTConfigImpl;
import gov.epa.cef.web.provider.system.SLTPropertyProvider;

@Service
public class SLTConfigHelper {

    private final SLTPropertyProvider propertyProvider;

    @Autowired
    public SLTConfigHelper(SLTPropertyProvider propertyProvider) {

        super();

        this.propertyProvider = propertyProvider;
    }

    //find and return the current SLT Configuration for the user/report that is currently being worked on
    public SLTBaseConfig getCurrentSLTConfig(String slt) {

        return new SLTConfigImpl(slt, propertyProvider);
    }
    
    public List<String> getAllSltByMonthlyEnabled() {
    	List<String> result = propertyProvider.getAllSltWithEnabledProperty(SLTPropertyName.MonthlyFuelReportingEnabled);
    	return result;
    }

}
