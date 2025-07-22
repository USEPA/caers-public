/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.domain;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Date;

/**
 *  PointSourceSccCode entity.
 */
@Entity
@Table(name = "point_source_scc_code")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PointSourceSccCode implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	//Fields

  @Id
  @Column(name = "code", unique = true, nullable = false, precision = 10, scale = 0)
  private String code;

  @Column(name = "last_inventory_year")
  private Short lastInventoryYear;

  @Column(name = "fuel_use_required", nullable = false)
  private Boolean fuelUseRequired;

  @Column(name = "monthly_reporting", nullable = false)
  private Boolean monthlyReporting;

  @Column(name = "scc_level_one")
  private String sccLevelOne;

  @Column(name = "scc_level_two")
  private String sccLevelTwo;

  @Column(name = "scc_level_three")
  private String sccLevelThree;

  @Column(name = "scc_level_four")
  private String sccLevelFour;

  @Column(name = "sector")
  private String sector;

  @Column(name = "short_name")
  private String shortName;

  @Column(name = "fuel_use_types")
  private String fuelUseTypes;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "calculation_material_code")
  private CalculationMaterialCode calculationMaterialCode;

  @Column(name = "dual_throughput", nullable = false)
  private boolean dualThroughput;

  @Enumerated(EnumType.STRING)
  @Column(name = "category", nullable = false)
  private SccCategory category;

  @Column(name = "last_updated_date", nullable = true)
  private Date lastUpdatedDate;

  // Property accessors

  public String getCode() {
      return this.code;
  }

  public void setCode(String code) {
      this.code = code;
  }

  public Short getLastInventoryYear() {
      return this.lastInventoryYear;
  }

  public void setLastInventoryYear(Short lastInventoryYear) {
      this.lastInventoryYear = lastInventoryYear;
  }

  public Boolean getFuelUseRequired() {
	  return fuelUseRequired;
  }

  public void setFuelUseRequired(Boolean fuelUseRequired) {
	  this.fuelUseRequired = fuelUseRequired;
  }

  public String getSccLevelOne() {
      return sccLevelOne;
  }

  public void setSccLevelOne(String sccLevelOne) {
	  this.sccLevelOne = sccLevelOne;
  }

  public String getSccLevelTwo() {
	  return sccLevelTwo;
  }

  public void setSccLevelTwo(String sccLevelTwo) {
	  this.sccLevelTwo = sccLevelTwo;
  }

  public String getSccLevelThree() {
	  return sccLevelThree;
  }

  public void setSccLevelThree(String sccLevelThree) {
	  this.sccLevelThree = sccLevelThree;
  }

  public String getSccLevelFour() {
	  return sccLevelFour;
  }

  public void setSccLevelFour(String sccLevelFour) {
	  this.sccLevelFour = sccLevelFour;
  }

  public String getSector() {
	  return sector;
  }

  public void setSector(String sector) {
	  this.sector = sector;
  }

  public String getFuelUseTypes() {
	  return fuelUseTypes;
  }

  public void setFuelUseTypes(String fuelUseTypes) {
	  this.fuelUseTypes = fuelUseTypes;
  }

  public CalculationMaterialCode getCalculationMaterialCode() {
	  return calculationMaterialCode;
  }

  public void setCalculationMaterialCode(CalculationMaterialCode calculationMaterialCode) {
	  this.calculationMaterialCode = calculationMaterialCode;
  }

  public String getShortName() {
	  return shortName;
  }

  public void setShortName(String shortName) {
	  this.shortName = shortName;
  }

  public Boolean getMonthlyReporting() {
	  return monthlyReporting;
  }

  public void setMonthlyReporting(Boolean monthlyReporting) {
	  this.monthlyReporting = monthlyReporting;
  }

  public boolean getDualThroughput() {
        return dualThroughput;
    }

  public void setDualThroughput(boolean dualThroughput) {
        this.dualThroughput = dualThroughput;
    }

  public SccCategory getCategory() {
    return category;
  }

  public void setCategory(SccCategory category) {
    this.category = category;
  }

  public Date getLastUpdatedDate() {
    return lastUpdatedDate;
  }

  public void setLastUpdatedDate(Date lastUpdatedDate) {
    this.lastUpdatedDate = lastUpdatedDate;
  }
}
