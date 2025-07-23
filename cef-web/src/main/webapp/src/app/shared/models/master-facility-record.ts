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
import { FipsStateCode } from 'src/app/shared/models/fips-state-code';
import { FipsCounty } from 'src/app/shared/models/fips-county';
import { BaseCodeLookup } from 'src/app/shared/models/base-code-lookup';
import { InventoryYearCodeLookup } from 'src/app/shared/models/inventory-year-code-lookup';
import { FacilityCategoryCode } from 'src/app/shared/models/facility-category-code';
import { MasterFacilityNaicsCode } from 'src/app/shared/models/master-facility-naics-code';
import {FacilityPermit} from "./facility-permit";

export class MasterFacilityRecord {
  id: number;
  name: string;
  eisProgramId: string;
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
  tribalCode: BaseCodeLookup;
  programSystemCode: BaseCodeLookup;
  statusYear: number;
  agencyFacilityIdentifier: string;
  facilityCategoryCode: FacilityCategoryCode;
  facilitySourceTypeCode: InventoryYearCodeLookup;
  description: string;
  masterFacilityNAICS: MasterFacilityNaicsCode[];
  masterFacilityPermits: FacilityPermit[];
  coordinateTolerance: number;

  associationStatus: string;
}
