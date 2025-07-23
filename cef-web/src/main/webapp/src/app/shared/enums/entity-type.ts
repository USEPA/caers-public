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
export enum EntityType {
    EMISSION = 'EMISSION',
    EMISSIONS_PROCESS = 'EMISSIONS_PROCESS',
    EMISSIONS_REPORT = 'EMISSIONS_REPORT',
    EMISSIONS_UNIT = 'EMISSIONS_UNIT',
    FACILITY_SITE = 'FACILITY_SITE',
    OPERATING_DETAIL = 'OPERATING_DETAIL',
    RELEASE_POINT = 'RELEASE_POINT',
    REPORTING_PERIOD = 'REPORTING_PERIOD',
    CONTROL_PATH = 'CONTROL_PATH',
    CONTROL = 'CONTROL',
    REPORT_ATTACHMENT = 'REPORT_ATTACHMENT'
}
