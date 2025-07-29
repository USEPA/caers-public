/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
