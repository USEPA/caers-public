/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.domain;

import gov.epa.cef.web.domain.common.BaseLookupEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "calculation_method_code")
@Immutable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class CalculationMethodCode extends BaseLookupEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "control_indicator", nullable = false)
    private Boolean controlIndicator;

    @Column(name = "epa_emission_factor", nullable = false)
    private Boolean epaEmissionFactor;

    @Column(name = "total_direct_entry", nullable = false)
    private Boolean totalDirectEntry;

    public Boolean getControlIndicator() {
        return controlIndicator;
    }

    public void setControlIndicator(Boolean controlIndicator) {
        this.controlIndicator = controlIndicator;
    }

    public Boolean getEpaEmissionFactor() {
        return epaEmissionFactor;
    }

    public void setEpaEmissionFactor(Boolean epaEmissionFactor) {
        this.epaEmissionFactor = epaEmissionFactor;
    }

    public Boolean getTotalDirectEntry() {
        return totalDirectEntry;
    }

    public void setTotalDirectEntry(Boolean totalDirectEntry) {
        this.totalDirectEntry = totalDirectEntry;
    }


}
