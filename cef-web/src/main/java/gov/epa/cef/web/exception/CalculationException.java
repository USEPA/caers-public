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

import java.util.ArrayList;
import java.util.List;

public class CalculationException extends ApplicationException {

    private List<String> missingVariables;

    public CalculationException(List<String> variables) {

        super(ApplicationErrorCode.E_CALC_MISSING_VARIABLE, 
                String.format("Emission Factor could not be calculated. Variable(s) [%s] are missing.", String.join(", ", variables)));

        this.missingVariables = new ArrayList<>();
        if (variables != null) {
            this.missingVariables.addAll(variables);
        }
    }

    public List<String> getMissingVariables() {
        return missingVariables;
    }
}
