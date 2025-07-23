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
package gov.epa.cef.web.util;

import java.math.BigDecimal;

public enum MassUomConversion {
    G(new BigDecimal("1.10231e-6")),
    MILLIGRM(new BigDecimal("1.10231e-9")),
    NG(new BigDecimal("1.10231e-15")),
    UG(new BigDecimal("1.10231e-12")),
    KG(new BigDecimal("1.10231e-3")),
    LB(new BigDecimal(".0005")),
    MEGAGRAM(new BigDecimal("1.10231")),
    TON(new BigDecimal("1"));

    private final BigDecimal conversionFactor;

    MassUomConversion(BigDecimal conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public String code() {
        return this.name();
    }

    public BigDecimal conversionFactor() {
        return this.conversionFactor;
    }

}
