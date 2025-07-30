/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { BaseCodeLookup } from 'src/app/shared/models/base-code-lookup';
import { EmissionUnit } from 'src/app/shared/models/emission-unit';
import { ReleasePoint } from 'src/app/shared/models/release-point';
import { EmissionsReport } from 'src/app/shared/models/emissions-report';
import { FacilitySiteContact } from 'src/app/shared/models/facility-site-contact';
import { FacilityNaicsCode } from 'src/app/shared/models/facility-naics-code';
import { Control } from 'src/app/shared/models/control';
import { FipsCounty } from 'src/app/shared/models/fips-county';
import { FipsStateCode } from 'src/app/shared/models/fips-state-code';
import { InventoryYearCodeLookup } from './inventory-year-code-lookup';

export class FacilitySite {
  id: number;
  name: string;
  latitude: number;
  longitude: number;
  streetAddress: string;
  city: string;
  stateCode: FipsStateCode;
  postalCode: string;
  countyCode: FipsCounty;
  mailingStreetAddress: string;
  mailingCity: string;
  mailingStateCode: FipsStateCode;
  mailingPostalCode: string;
  operatingStatusCode: BaseCodeLookup;
  facilityNAICS: FacilityNaicsCode[];
  emissionsReport: EmissionsReport;
  emissionsUnits: EmissionUnit[];
  releasePoints: ReleasePoint[];
  controls: Control[];
  contacts: FacilitySiteContact[];
  tribalCode: BaseCodeLookup;
  programSystemCode: BaseCodeLookup;
  statusYear: number;
  agencyFacilityIdentifier: string;
  facilityCategoryCode: BaseCodeLookup;
  facilitySourceTypeCode: InventoryYearCodeLookup;
  description: string;
  comments: string;
}
