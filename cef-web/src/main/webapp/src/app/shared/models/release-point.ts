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
import {BaseCodeLookup} from 'src/app/shared/models/base-code-lookup';
import {InventoryYearCodeLookup} from './inventory-year-code-lookup';

export class ReleasePoint {
  id: number;
  description: string;
  operatingStatusCode: BaseCodeLookup;
  facilitySiteId: number;
  releasePointIdentifier: string;
  typeCode: InventoryYearCodeLookup;
  stackHeight: number;
  stackHeightUomCode: BaseCodeLookup;
  stackDiameter: number;
  stackDiameterUomCode: BaseCodeLookup;
  stackWidth: number;
  stackWidthUomCode: BaseCodeLookup;
  stackLength: number;
  stackLengthUomCode: BaseCodeLookup;
  exitGasVelocity: number;
  exitGasVelocityUomCode: BaseCodeLookup;
  exitGasTemperature: number;
  exitGasFlowRate: number;
  exitGasFlowUomCode: BaseCodeLookup;
  statusYear: number;
  fugitiveMidPt2Latitude: number;
  fugitiveMidPt2Longitude: number;
  latitude: number;
  longitude: number;
  comments: string;
  fugitiveLength: number;
  fugitiveHeight: number;
  fugitiveWidth: number;
  fugitiveAngle: number;
  fenceLineDistance: number;
  fugitiveLengthUomCode: BaseCodeLookup;
  fugitiveHeightUomCode: BaseCodeLookup;
  fugitiveWidthUomCode: BaseCodeLookup;
  fenceLineUomCode: BaseCodeLookup;
  previousReleasePoint: ReleasePoint;
}
