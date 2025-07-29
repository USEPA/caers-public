/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.exception;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    final ApplicationErrorCode errorCode;

    @JsonCreator
    public ApplicationException(@JsonProperty("code") ApplicationErrorCode errorCode,
                                @JsonProperty("message") String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
    
    @JsonCreator
    public ApplicationException(@JsonProperty("code") ApplicationErrorCode errorCode,
                                @JsonProperty("message") String errorMessage, @JsonProperty("exception") Throwable t) {
        super(errorMessage, t);
        this.errorCode = errorCode;
    }

    public static ApplicationException asApplicationException(Exception e, ApplicationErrorCode code, String message) {
        if (e instanceof ApplicationException) {
            return ((ApplicationException) e);
        } else {
            ApplicationErrorCode aec = (code == null) ? ApplicationErrorCode.E_INTERNAL_ERROR : code;
            String msg = (StringUtils.isEmpty(message)) ? "An internal error occurred." : message;
            return new ApplicationException(aec, msg);
        }
    }

    public static ApplicationException asApplicationException(Exception e) {
        return asApplicationException(e, null, null);
    }

    public ApplicationErrorCode getErrorCode() {
        return errorCode;
    }
}
