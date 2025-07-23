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
package gov.epa.cef.web.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import gov.epa.cef.web.domain.common.BaseAuditEntity;

@Entity
@Table(name = "emission_formula_variable")
public class EmissionFormulaVariable extends BaseAuditEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emission_id", nullable = false)
    private Emission emission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emission_formula_variable_code", nullable = false)
    private EmissionFormulaVariableCode variableCode;

    @Column(name = "value")
    private BigDecimal value;

    /***
     * Default constructor
     */
    public EmissionFormulaVariable() {}

    /***
     * Copy constructor 
     * @param emission The emission that the copied variable object should be associated with
     * @param originalVariable The variable object that is being copied
     */
    public EmissionFormulaVariable(Emission emission, EmissionFormulaVariable originalVariable) {
        this.id = originalVariable.getId();
        this.emission = emission;
        this.variableCode = originalVariable.getVariableCode();
        this.value = originalVariable.getValue();
    }

    public Emission getEmission() {
        return emission;
    }

    public void setEmission(Emission emission) {
        this.emission = emission;
    }

    public EmissionFormulaVariableCode getVariableCode() {
        return variableCode;
    }

    public void setVariableCode(EmissionFormulaVariableCode emissionFactorVariableCode) {
        this.variableCode = emissionFactorVariableCode;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /***
     * Set the id property to null for this object and the id for it's direct children.  This method is useful to INSERT the updated object instead of UPDATE.
     */
    public void clearId() {
        this.id = null;
    }
}
