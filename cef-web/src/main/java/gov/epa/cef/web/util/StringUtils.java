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

public class StringUtils {

    /**
     * Return a string that can be used as a valid file name
     */
    public static String createValidFileName(String agencyFacilityIdentifier, String facilityName, Short reportYear) {
        String fileName = String.format("%s-%s-%d.xlsx", agencyFacilityIdentifier, facilityName, reportYear).replaceAll("[^a-zA-Z0-9-._()]", " ");
        return fileName;
    }

    public static String createValidShortFileName(String agencyFacilityIdentifier, String facilityName, Short reportYear) {
        String fileName = String.format("%s-%s-%d", agencyFacilityIdentifier, facilityName, reportYear).replaceAll("[^a-zA-Z0-9-._()]", " ");
        return fileName;
    }
    
    public static String createValidLongFileName(String agencyFacilityIdentifier, String facilityName, Short reportYear, String datestring) {
        String fileName = String.format("%s-%s-%d-%s", agencyFacilityIdentifier, facilityName, reportYear, datestring).replaceAll("[^a-zA-Z0-9-._()]", " ");
        return fileName;
    }

}
