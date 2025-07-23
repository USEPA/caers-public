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
package gov.epa.cef.web.service.dto.bulkUpload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseWorksheetDto {

    private final String sheetName;

    @JsonProperty("_row")
    private int row;

    static final String PositiveDecimalPattern = "^\\d*(\\.\\d+)?$";

    static final String LatitudePattern = "^[+-]?\\d{0,2}(\\.\\d{1,6})?$";

    static final String LongitudePattern = "^[+-]?\\d{0,3}(\\.\\d{1,6})?$";
    
    static final String PhonePattern = "^[0-9]{10}$";

    static final String PercentPattern = "^\\d{0,3}(\\.\\d)?$";

    static final String PositiveIntPattern = "^\\d{0,10}$";

    static final String YearPattern = "^\\d{0,4}$";

    public BaseWorksheetDto(WorksheetName sheetName) {

        this.sheetName = sheetName.sheetName();
    }

    public int getRow() {

        return row;
    }

    public void setRow(int row) {

        this.row = row;
    }

    public String getSheetName() {

        return sheetName;
    }
    
    public String getErrorIdentifier() {
        return "" + row;
    }
}
