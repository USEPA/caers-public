/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Injectable } from '@angular/core';
import { BaseCodeLookup } from 'src/app/shared/models/base-code-lookup';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Pollutant } from 'src/app/shared/models/pollutant';
import { CalculationMethodCode } from 'src/app/shared/models/calculation-method-code';
import { UnitMeasureCode } from 'src/app/shared/models/unit-measure-code';
import { FipsStateCode } from 'src/app/shared/models/fips-state-code';
import { FacilityNaicsCode } from 'src/app/shared/models/facility-naics-code';
import { AircraftEngineTypeCode } from 'src/app/shared/models/aircraft-engine-type-code';
import { PointSourceSccCode } from 'src/app/shared/models/point-source-scc-code';
import { FacilityCategoryCode } from 'src/app/shared/models/facility-category-code';
import { FipsCounty } from 'src/app/shared/models/fips-county';
import { InventoryYearCodeLookup } from 'src/app/shared/models/inventory-year-code-lookup';
import { CalculationMaterialCode } from 'src/app/shared/models/calculation-material-code';
import { MasterFacilityNaicsCode } from 'src/app/shared/models/master-facility-naics-code';
import {PermitType} from "src/app/shared/models/permit-type";

@Injectable({
  providedIn: 'root'
})
export class LookupService {

  private baseUrl = 'api/lookup';  // URL to web api

  constructor(private http: HttpClient) { }

  retrieveCalcMaterial(): Observable<CalculationMaterialCode[]> {
    const url = `${this.baseUrl}/calculation/material`;
    return this.http.get<CalculationMaterialCode[]>(url);
  }

  retrieveFuelUseMaterial(): Observable<CalculationMaterialCode[]> {
    const url = `${this.baseUrl}/fuelUse/material`;
    return this.http.get<CalculationMaterialCode[]>(url);
  }

	retrieveMonthlyReportingMaterial(): Observable<CalculationMaterialCode[]> {
    const url = `${this.baseUrl}/monthlyReporting/material`;
    return this.http.get<CalculationMaterialCode[]>(url);
  }

  retrieveCalcMethod(): Observable<CalculationMethodCode[]> {
    const url = `${this.baseUrl}/calculation/method`;
    return this.http.get<CalculationMethodCode[]>(url);
  }

  retrieveCalcParam(): Observable<BaseCodeLookup[]> {
    const url = `${this.baseUrl}/calculation/parameter`;
    return this.http.get<BaseCodeLookup[]>(url);
  }

  retrieveSubFacilityOperatingStatus(): Observable<BaseCodeLookup[]> {
    const url = `${this.baseUrl}/subFacilityOperatingStatus`;
    return this.http.get<BaseCodeLookup[]>(url);
  }

  retrieveFacilityOperatingStatus(): Observable<BaseCodeLookup[]> {
    const url = `${this.baseUrl}/facilityOperatingStatus`;
    return this.http.get<BaseCodeLookup[]>(url);
  }

  retrieveEmissionsOperatingType(): Observable<BaseCodeLookup[]> {
    const url = `${this.baseUrl}/emissionsOperatingType`;
    return this.http.get<BaseCodeLookup[]>(url);
  }

  retrievePollutant(): Observable<Pollutant[]> {
    const url = `${this.baseUrl}/pollutant`;
    return this.http.get<Pollutant[]>(url);
  }

  retrieveCurrentPollutants(year: number): Observable<Pollutant[]> {
    const url = `${this.baseUrl}/pollutant/${year}`;
    return this.http.get<Pollutant[]>(url);
  }

  retrieveReportingPeriod(): Observable<BaseCodeLookup[]> {
    const url = `${this.baseUrl}/reportingPeriod`;
    return this.http.get<BaseCodeLookup[]>(url);
  }

  retrieveUom(year: number): Observable<UnitMeasureCode[]> {
    const url = `${this.baseUrl}/uom/${year}`;
    return this.http.get<UnitMeasureCode[]>(url);
  }

  retrieveFuelUseUom(): Observable<UnitMeasureCode[]> {
    const url = `${this.baseUrl}/fuelUse/uom`;
    return this.http.get<UnitMeasureCode[]>(url);
  }

  retrieveFacilityContactType(): Observable<BaseCodeLookup[]> {
    const url = `${this.baseUrl}/contactType`;
    return this.http.get<BaseCodeLookup[]>(url);
  }

  retrieveFipsCounties(): Observable<FipsCounty[]> {
    const url = `${this.baseUrl}/county`;
    return this.http.get<FipsCounty[]>(url);
  }

  retrieveCurrentFipsCounties(year: number): Observable<FipsCounty[]> {
    const url = `${this.baseUrl}/county/${year}`;
    return this.http.get<FipsCounty[]>(url);
  }

  retrieveFipsCountiesForState(stateCode: string): Observable<FipsCounty[]> {
    const url = `${this.baseUrl}/county/state/${stateCode}`;
    return this.http.get<FipsCounty[]>(url);
  }

  retrieveCurrentFipsCountiesForState(stateCode: string, year: number): Observable<FipsCounty[]> {
    const url = `${this.baseUrl}/county/state/${stateCode}/${year}`;
    return this.http.get<FipsCounty[]>(url);
  }

  retrieveFipsStateCode(): Observable<FipsStateCode[]> {
    const url = `${this.baseUrl}/stateCode`;
    return this.http.get<FipsStateCode[]>(url);
  }

  retrieveUnitType(): Observable<BaseCodeLookup[]> {
    const url = `${this.baseUrl}/unitType`;
    return this.http.get<BaseCodeLookup[]>(url);
  }

  retrieveReleaseTypeCode(): Observable<BaseCodeLookup[]> {
    const url = `${this.baseUrl}/releaseType`;
    return this.http.get<BaseCodeLookup[]>(url);
  }

  retrieveCurrentReleaseTypeCodes(year: number): Observable<InventoryYearCodeLookup[]> {
    const url = `${this.baseUrl}/releasePointType/${year}`;
    return this.http.get<InventoryYearCodeLookup[]>(url);
  }

  retrieveProgramSystemTypeCode(): Observable<BaseCodeLookup[]> {
    const url = `${this.baseUrl}/programSystemType`;
    return this.http.get<BaseCodeLookup[]>(url);
  }

  retrieveProgramSystemCodeByDescription(description: string): Observable<BaseCodeLookup> {
    const url = `${this.baseUrl}/programSystem/description/${description}`;
    return this.http.get<BaseCodeLookup>(url);
  }

  retrieveControlMeasureCodes(): Observable<BaseCodeLookup[]> {
    const url = `${this.baseUrl}/controlMeasure`;
    return this.http.get<BaseCodeLookup[]>(url);
  }

  retrieveCurrentControlMeasureCodes(year: number): Observable<InventoryYearCodeLookup[]> {
    const url = `${this.baseUrl}/controlMeasure/${year}`;
    return this.http.get<InventoryYearCodeLookup[]>(url);
  }

  retrieveTribalCode(): Observable<BaseCodeLookup[]> {
    const url = `${this.baseUrl}/tribalCode`;
    return this.http.get<BaseCodeLookup[]>(url);
  }

  retrieveNaicsCode(): Observable<FacilityNaicsCode[]> {
    const url = `${this.baseUrl}/naicsCode`;
    return this.http.get<FacilityNaicsCode[]>(url);
  }

  retrieveNaicsCodesByYear(year: number): Observable<FacilityNaicsCode[]> {
    const url = `${this.baseUrl}/naicsCode/${year}`;
    return this.http.get<FacilityNaicsCode[]>(url);
  }

  retrieveCurrentMFNaicsCodes(year: number): Observable<MasterFacilityNaicsCode[]> {
    const url = `${this.baseUrl}/naicsCode/${year}`;
    return this.http.get<MasterFacilityNaicsCode[]>(url);
  }

  retrievePermitTypes(): Observable<PermitType[]> {
    const url = `${this.baseUrl}/permitType`;
    return this.http.get<PermitType[]>(url);
  }

  retrieveAircraftEngineCodes(scc: string): Observable<AircraftEngineTypeCode[]> {
    const url = `${this.baseUrl}/aircraftEngineCode/${scc}`;
    return this.http.get<AircraftEngineTypeCode[]>(url);
  }

  retrieveCurrentAircraftEngineCodes(scc: string, year: number): Observable<AircraftEngineTypeCode[]> {
    const url = `${this.baseUrl}/aircraftEngineCode/${scc}/${year}`;
    return this.http.get<AircraftEngineTypeCode[]>(url);
  }

  retrievePointSourceSccCode(code: string): Observable<PointSourceSccCode> {
    const url = `${this.baseUrl}/pointSourceSccCode/${code}`;
    return this.http.get<PointSourceSccCode>(url);
  }

  retrieveFacilityCategory(): Observable<FacilityCategoryCode[]> {
    const url = `${this.baseUrl}/facility/category`;
    return this.http.get<FacilityCategoryCode[]>(url);
  }

  retrieveFacilitySourceType(): Observable<BaseCodeLookup[]> {
    const url = `${this.baseUrl}/facility/sourceType`;
    return this.http.get<BaseCodeLookup[]>(url);
  }

  retrieveCurrentFacilitySourceType(year: number): Observable<InventoryYearCodeLookup[]> {
    const url = `${this.baseUrl}/facility/sourceType/${year}`;
    return this.http.get<InventoryYearCodeLookup[]>(url);
  }

  basicSccSearch(searchTerm: string, slt: string): Observable<PointSourceSccCode[]> {
	const url = `${this.baseUrl}/scc/${slt}/${searchTerm}`;
	return this.http.get<PointSourceSccCode[]>(url);
  }

}
