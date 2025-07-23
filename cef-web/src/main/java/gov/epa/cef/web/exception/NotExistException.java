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

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.GONE)
public class NotExistException extends ApplicationException {

    public NotExistException(String entityType, long id) {

        super(ApplicationErrorCode.E_NOT_FOUND,
            String.format("Entity {%s} with ID %d was not found.", entityType, id));
    }
    
    public NotExistException(String entityType, String id) {

        super(ApplicationErrorCode.E_NOT_FOUND,
            String.format("Entity {%s} with ID %s was not found.", entityType, id));
    }
}
