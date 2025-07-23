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
import {Pipe, PipeTransform} from '@angular/core';

import {split, filterBy} from "lodash";

@Pipe({
   name: 'dataFilter'
})
export class DataFilterPipe implements PipeTransform {

   transform(list: any[], value: string, csvKeys: string = ''): any[] {

      let result = list;

      if (list && csvKeys && value) {

         let keys = csvKeys
            .replace(/\\w/g, '')
            .replace(/,,/g, ',')
            .split(",");

         if (keys) {

            result = list.filter(item => {

               let match = false;
               for (let i = 0; i < keys.length; ++i) {

                  let str = item[keys[i]];

                  if (str && str.toLowerCase().indexOf(value.toLowerCase()) > -1) {

                     match = true;
                     break;
                  }
               }

               return match;
            });
         }
      }

      return result;
   }
}
