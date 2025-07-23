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
export enum MonthlyReportingPeriod {
	JANUARY = 'January',
	FEBRUARY = 'February',
	MARCH = 'March',
	APRIL = 'April',
	MAY = 'May',
	JUNE = 'June',
	JULY = 'July',
	AUGUST = 'August',
	SEPTEMBER = 'September',
	OCTOBER = 'October',
	NOVEMBER = 'November',
	DECEMBER = 'December',
	SEMIANNUAL = 'Semiannual',
	ANNUAL = 'Annual'
}

export const MonthsWith31Days: ReadonlyArray<string> = [
    MonthlyReportingPeriod.JANUARY,
    MonthlyReportingPeriod.MARCH,
    MonthlyReportingPeriod.MAY,
    MonthlyReportingPeriod.JULY,
    MonthlyReportingPeriod.AUGUST,
    MonthlyReportingPeriod.OCTOBER,
    MonthlyReportingPeriod.DECEMBER
];

export const MonthsWith30Days: ReadonlyArray<string> = [
    MonthlyReportingPeriod.APRIL,
    MonthlyReportingPeriod.JUNE,
    MonthlyReportingPeriod.SEPTEMBER,
    MonthlyReportingPeriod.NOVEMBER,
];
