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
