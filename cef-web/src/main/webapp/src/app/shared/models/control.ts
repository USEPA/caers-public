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
import { ControlAssignment } from 'src/app/shared/models/control-assignment';
import { ControlPollutant } from 'src/app/shared/models/control-pollutant';
import { BaseCodeLookup } from 'src/app/shared/models/base-code-lookup';
import { InventoryYearCodeLookup } from 'src/app/shared/models/inventory-year-code-lookup';

export class Control {
  id: number;
  facilitySiteId: number;
  operatingStatusCode: BaseCodeLookup;
  statusYear: number;
  identifier: string;
  description: string;
  upgradeDescription: string;
  percentCapture: number;
  numberOperatingMonths: number;
  percentControl: number;
  startDate: Date;
  upgradeDate: Date;
  endDate: Date;
  comments: string;
  controlMeasureCode: InventoryYearCodeLookup;
  assignments: ControlAssignment[];
  pollutants: ControlPollutant[];
  previousControl: Control;
}
