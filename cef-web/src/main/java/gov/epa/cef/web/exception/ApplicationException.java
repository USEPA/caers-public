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
