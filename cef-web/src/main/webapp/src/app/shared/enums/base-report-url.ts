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
export enum BaseReportUrl {
  FACILITY_INFO = 'facilityInformation',
  FACILITY_CONTACT = 'facilitySiteContact',
  EMISSION = 'emission',
  EMISSIONS_UNIT = 'emissionUnit',
  EMISSIONS_PROCESS = 'process',
  RELEASE_POINT = 'release',
  REPORTING_PERIOD = 'period',
  CONTROL_DEVICE = 'control',
  CONTROL_PATH = 'path',
  REPORT_SUMMARY = 'summary',
  VALIDATION = 'validation',
  CHANGES = 'changes',
  BULK_ENTRY = 'bulkEntry',
  REPORT_HISTORY = 'history',
  MONTHLY_REPORTING = 'monthlyReporting'
}
