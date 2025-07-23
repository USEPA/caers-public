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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "emission_factor")
@Immutable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class WebfireEmissionFactor extends EmissionFactor implements ISourceClassificationCode {

    private static final long serialVersionUID = 1L;

    @Column(name = "scc_code", nullable = false, length = 20)
    private String sccCode;

    @Column(name = "webfire_id")
    private Long webfireId;

    @Override
    public String getSccCode() {
        return sccCode;
    }

    @Override
    public void setSccCode(String sccCode) {
        this.sccCode = sccCode;
    }

    public Long getWebfireId() {
        return webfireId;
    }

    public void setWebfireId(Long webfireId) {
        this.webfireId = webfireId;
    }

}
