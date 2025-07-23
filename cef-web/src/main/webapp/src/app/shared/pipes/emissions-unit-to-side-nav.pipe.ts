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
import { Pipe, PipeTransform } from '@angular/core';
import { SideNavItem } from 'src/app/shared/models/side-nav-item';
import { BaseReportUrl } from 'src/app/shared/enums/base-report-url';

@Pipe( {
    name: 'emissionsUnitToSideNav'
} )
export class EmissionsUnitToSideNavPipe implements PipeTransform {

    transform( value: any ): SideNavItem {
        const children: SideNavItem[] = [];
        let sideNavItem: SideNavItem;
        if (value.emissionsProcesses) {
            for (const emissionProcess of value.emissionsProcesses) {
                const processChildren: SideNavItem[] = [];
                children.push(new SideNavItem( emissionProcess.id, emissionProcess.emissionsProcessIdentifier, emissionProcess.description, BaseReportUrl.EMISSIONS_PROCESS, processChildren));
            }
        }
        sideNavItem = new SideNavItem( value.id, value.unitIdentifier, value.description, BaseReportUrl.EMISSIONS_UNIT, children);
        return sideNavItem;
    }

}
