/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
