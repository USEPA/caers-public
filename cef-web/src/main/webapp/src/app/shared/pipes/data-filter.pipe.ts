/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
