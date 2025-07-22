/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import {Injectable} from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class ReportDownloadService {

    constructor() {}

    downloadFile(data?: any, filename = 'data', dataCsv?: string) {
      const nonDormant = (data.filter((item: any) => item.notReporting === false));
        const csvData = dataCsv ? dataCsv : this.ConvertToCSV(nonDormant, ['facilitySiteId', 'reportYear', 'emissionsUnitId', 'emissionUnitDescription',
            'processId', 'processDescription', 'sccCode', 'reportingPeriodType', 'throughputMaterial', 'throughputValue', 'throughputUom','fuelMaterial',
            'fuelValue', 'fuelUom', 'heatContentRatio', 'heatContentRatioNumerator', 'pollutantName',
            'totalEmissions', 'apportionedEmissions', 'emissionsUomCode', 'pathId', 'pathDescription', 'releasePointApportionment', 'releasePointId',
            'overallControlPercent', 'emissionsCalcMethod', 'emissionsFactor',
            'emissionsNumeratorUom', 'emissionsDenominatorUom', 'emissionsFactorText', 'emissionsComment', 'calculationComment',
            'lastModifiedBy', 'lastModifiedDate']);

        const blob = new Blob(['\ufeff' + csvData], {type: 'text/csv;charset=utf-8;'});
        const dwldLink = document.createElement('a');
        let url = URL.createObjectURL(blob);


        // add target _blank to fix issue where Google Analytics would ignore download attribute
        dwldLink.setAttribute('target', '_blank');
        dwldLink.setAttribute('href', url);
        dwldLink.setAttribute('download', filename + '.csv');
        dwldLink.style.visibility = 'hidden';
        document.body.appendChild(dwldLink);
        dwldLink.click();
        document.body.removeChild(dwldLink);
    }

    downloadReportSummary(data: any, filename: string) {
        const csvData = this.ConvertToCSV(data, ['pollutantName', 'pollutantType', 'fugitiveTonsTotal', 'stackTonsTotal',
            'emissionsTonsTotal', 'previousYearTonsTotal', 'previousYear']);

        this.downloadFile(data, filename, csvData);
    }

	  downloadMonthlyReport(data: any, filename: string) {
        const csvData = this.ConvertToCSV(data,

        new Map(Object.entries({
          agencyFacilityIdentifier: 'Agency Facility ID',
          facilityName: 'Facility Name',
          reportingPeriodName: 'Reporting Period',
          unitIdentifier: 'Agency Unit ID',
          emissionsProcessIdentifier: 'Agency Process ID',
          emissionsProcessSccCode: 'SCC',
          calculationMaterialShortName: 'Throughput Material',
          calculationParameterValue: 'Throughput Value',
          calculationParameterUomCode: 'Throughput UoM',
          fuelUseMaterialShortName: 'Fuel Material',
          fuelUseValue: 'Fuel Value',
          fuelUseUomCode: 'Fuel Value UoM',
          hoursPerPeriod: 'Hours per Reporting Period',
          avgHoursPerDay: 'Avg. Hours per Day',
          avgDaysPerWeek: 'Avg. Days per Week',
          avgWeeksPerPeriod: 'Avg. Weeks per Reporting Period',
          pollutantName: 'Pollutant',
          emissionsCalcMethodDescription: 'Calculation Method',
          monthlyRate: 'Monthly Rate',
          emissionsFactor: 'Emissions Factor',
          totalEmissions: 'Total Emissions',
          emissionsUomCode: 'Total Emissions UoM'
        })));

        this.downloadFile(data, filename, csvData);
    }

	  downloadNonPointFuelSubtractionReport(data: any, filename: string) {
        const csvData = this.ConvertToCSV(data, ['naics', 'sector', 'fuel', 'scc', 'description', 'fuelConsumption', 'units', 'unitDescription']);
        this.downloadFile(data, filename, csvData);
    }

    downloadAverageMaxNumberQasReport(data: any, filename: string) {
        const csvData = this.ConvertToCSV(data, ['slt', 'year', 'averageErrors', 'averageWarnings']);
        this.downloadFile(data, filename, csvData);
    }

    downloadSubmissionStatusAuditReport(data: any, filename: string) {
        const csvData = this.ConvertToCSV(data, ['name', 'slt', 'year', 'submitted', 'reopened', 'rejected']);
        this.downloadFile(data, filename, csvData);
    }

    ConvertToCSV(objArray: string, headerList: string[] | Map<string, string>) {
        const array = typeof objArray !== 'object' ? JSON.parse(objArray) : objArray;
        let str = '';
        let row = '';

        if (headerList instanceof Map) {
          headerList.forEach((label) => {
            row += label + ',';
          })
          str += row + '\r\n';
          for (let i = 0; i < array.length; i++) {
            let line = '';
            headerList.forEach((label, prop) => {
              if ((array[i][prop] !== null) && (array[i][prop] !== undefined)) {
                line += '"' + array[i][prop].toString() + '",';
              } else {
                // for cell with null or undefined value
                line += '"",';
              }
            });
            line = (line.indexOf(',') === line.length - 1) ? line.slice(0, -1) : line;
            line = (line.indexOf('""') === line.length - 2) ? line.slice(0, -2) : line;
            str += line + '\r\n';
          }
          return str;
        } else {
          row += 'S.No,';
          for (const index in headerList) {
            row += headerList[index] + ',';
          }
          row = row.slice(0, -1);
          str += row + '\r\n';
          for (let i = 0; i < array.length; i++) {
            let line = (i + 1) + '';
            for (const index in headerList) {
              const head = headerList[index];
              if ((array[i][head] !== null) && (array[i][head] !== undefined)) {
                line += ',"' + array[i][head].toString() + '"';
              } else {
                // for cell with null or undefined value
                line += ',""';
              }
            }
            str += line + '\r\n';
          }
          return str;
        }
    }
}
