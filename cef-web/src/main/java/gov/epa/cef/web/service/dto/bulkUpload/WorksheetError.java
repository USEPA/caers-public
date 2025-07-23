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

import java.io.Serializable;

public class WorksheetError implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String message;

    private final String identifier;

    private final int row;

    private final boolean systemError;

    private final String worksheet;

    private WorksheetError(String worksheet, int row, String identifier, String message, boolean systemError) {

        this.row = row;
        this.identifier = identifier;
        this.worksheet = worksheet;
        this.message = message;
        this.systemError = systemError;
    }

    public WorksheetError(String worksheet, int row, String message) {

        this(worksheet, row, null, message, false);
    }

    public WorksheetError(String worksheet, String identifier, String message) {

        this(worksheet, -1, identifier, message, false);
    }
    
    public WorksheetError(String worksheet, int row, String identifier, String message) {

        this(worksheet, row, identifier, message, false);
    }

    public static WorksheetError createSystemError(String message) {

        return new WorksheetError("*", -1, null, message, true);
    }

    public String getMessage() {

        return message;
    }

    public String getIdentifier() {

        return identifier;
    }

    public int getRow() {

        return row;
    }

    public String getWorksheet() {

        return worksheet;
    }

    public boolean isSystemError() {

        return systemError;
    }
}
