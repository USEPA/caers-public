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

import gov.epa.cef.web.domain.common.BaseEntity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "naics_description_history")
@Immutable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class NaicsDescriptionHistory extends BaseEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code", nullable = false)
    private NaicsCode naicsCode;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "description_year")
    private Integer descriptionYear;

    public NaicsCode getCode() { return naicsCode; }

    public void setCode(NaicsCode naicsCode) { this.naicsCode = naicsCode; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Integer getDescriptionYear() { return descriptionYear; }

    public void setDescriptionYear(Integer descriptionYear) { this.descriptionYear = descriptionYear; }

}
