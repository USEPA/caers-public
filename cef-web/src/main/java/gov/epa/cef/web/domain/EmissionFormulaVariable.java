/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
