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
package gov.epa.cef.web.exception;

import gov.epa.cef.web.util.TempFile;

public class VirusScanException extends ApplicationException {

    private final String filename;

    public VirusScanException(TempFile file, String errorMessage) {

        super(ApplicationErrorCode.E_VALIDATION, errorMessage);
        this.filename = file.getFileName();
    }

    public String getFilename() {

        return this.filename;
    }
}
