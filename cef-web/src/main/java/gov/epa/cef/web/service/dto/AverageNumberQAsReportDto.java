/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class AverageNumberQAsReportDto implements Serializable {

	private static final long serialVersionUID = 1L;

    private String slt;
    private short year;
    private Integer averageErrors;
    private Integer averageWarnings;

    public String getSlt() {
        return slt;
    }
    public void setSlt(String slt) {
        this.slt = slt;
    }
    public short getYear() { return year; }
    public void setYear(short year) { this.year = year; }
    public int getAverageErrors() {
        return averageErrors;
    }
    public void setAverageErrors(int averageErrors) {
        this.averageErrors = averageErrors;
    }
    public int getAverageWarnings() {
        return averageWarnings;
    }
    public void setAverageWarnings(int averageWarnings) {
        this.averageWarnings = averageWarnings;
    }
}
