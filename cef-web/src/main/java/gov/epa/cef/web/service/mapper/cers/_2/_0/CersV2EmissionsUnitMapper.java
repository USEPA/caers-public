/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.mapper.cers._2._0;

import gov.epa.cef.web.domain.ControlPath;
import gov.epa.cef.web.domain.Emission;
import gov.epa.cef.web.domain.EmissionsProcess;
import gov.epa.cef.web.domain.EmissionsUnit;
import gov.epa.cef.web.domain.OperatingDetail;
import gov.epa.cef.web.domain.ReleasePointAppt;
import gov.epa.cef.web.domain.ReportingPeriod;
import net.exchangenetwork.schema.cer._2._0.EmissionsDataType;
import net.exchangenetwork.schema.cer._2._0.EmissionsUnitDataType;
import net.exchangenetwork.schema.cer._2._0.IdentificationDataType;
import net.exchangenetwork.schema.cer._2._0.OperatingDetailsDataType;
import net.exchangenetwork.schema.cer._2._0.ProcessDataType;
import net.exchangenetwork.schema.cer._2._0.ReleasePointApportionmentDataType;
import net.exchangenetwork.schema.cer._2._0.ReportingPeriodDataType;

import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = {CersV2ReleasePointMapper.class})
public interface CersV2EmissionsUnitMapper {

    @Mapping(source="description", target="unitDescription")
    @Mapping(source="unitTypeCode.code", target="unitTypeCode")
    @Mapping(source="designCapacity", target="unitDesignCapacity")
    @Mapping(source="unitOfMeasureCode.code", target="unitDesignCapacityUnitofMeasureCode")
    @Mapping(source="operatingStatusCode.code", target="unitStatusCode")
    @Mapping(source="statusYear", target="unitStatusCodeYear")
    @Mapping(source="comments", target="unitComment")
    @Mapping(source=".", target="unitIdentification")
    @Mapping(source="emissionsProcesses", target="unitEmissionsProcess")
    EmissionsUnitDataType fromEmissionsUnit(EmissionsUnit source);

    @Mapping(source="sccCode", target="sourceClassificationCode")
    @Mapping(source="aircraftEngineTypeCode.code", target="aircraftEngineTypeCode")
    @Mapping(source="description", target="processDescription")
    @Mapping(source="operatingStatusCode.code", target="processStatusCode")
    @Mapping(source="statusYear", target="processStatusCodeYear")
    @Mapping(source="comments", target="processComment")
    @Mapping(source=".", target="processIdentification")
    @Mapping(source="reportingPeriods", target="reportingPeriod")
    @Mapping(source="releasePointAppts", target="releasePointApportionment")
    ProcessDataType processFromEmissionsProcess(EmissionsProcess source);

    @Mapping(source="percent", target="averagePercentEmissions")
    @Mapping(source="releasePoint", target="releasePointApportionmentIdentification")
    @Mapping(target="releasePointApportionmentIsUncontrolled", expression = "java( Boolean.valueOf(source.getControlPath() == null).toString() )")
    @Mapping(source="controlPath", target="releasePointApportionmentPathIdentification")
    ReleasePointApportionmentDataType rpApptFromReleasePointAppt(ReleasePointAppt source);

    @Mapping(source="reportingPeriodTypeCode.code", target="reportingPeriodTypeCode")
    @Mapping(source="emissionsOperatingTypeCode.code", target="emissionOperatingTypeCode")
    @Mapping(source="calculationParameterTypeCode.code", target="calculationParameterTypeCode")
    @Mapping(source="calculationParameterValue", target="calculationParameterValue")
    @Mapping(source="calculationParameterUom.code", target="calculationParameterUnitofMeasure")
    @Mapping(source="calculationMaterialCode.code", target="calculationMaterialCode")
    @Mapping(source="comments", target="reportingPeriodComment")
    @Mapping(source="operatingDetails", target="operatingDetails")
    @Mapping(source="emissions", target="reportingPeriodEmissions")
    ReportingPeriodDataType periodFromReportingPeriod(ReportingPeriod source);

    @Mapping(source="actualHoursPerPeriod", target="actualHoursPerPeriod")
    @Mapping(source="avgDaysPerWeek", target="averageDaysPerWeek")
    @Mapping(source="avgHoursPerDay", target="averageHoursPerDay")
    @Mapping(source="avgWeeksPerPeriod", target="averageWeeksPerPeriod")
    @Mapping(source="percentWinter", target="percentWinterActivity")
    @Mapping(source="percentSpring", target="percentSpringActivity")
    @Mapping(source="percentSummer", target="percentSummerActivity")
    @Mapping(source="percentFall", target="percentFallActivity")
    OperatingDetailsDataType operatingDetailsFromOperatingDetail(OperatingDetail source);

    @Mapping(source="pollutant.pollutantCode", target="pollutantCode")
    @Mapping(source="totalEmissions", target="totalEmissions", numberFormat = "0.0#####")
    @Mapping(source="emissionsUomCode.code", target="emissionsUnitofMeasureCode")
    @Mapping(source="emissionsFactor", target="emissionFactor")
    @Mapping(source="emissionsNumeratorUom.code", target="emissionFactorNumeratorUnitofMeasureCode")
    @Mapping(source="emissionsDenominatorUom.code", target="emissionFactorDenominatorUnitofMeasureCode")
    @Mapping(source="emissionsFactorText", target="emissionFactorText")
    @Mapping(source="emissionsCalcMethodCode.code", target="emissionCalculationMethodCode")
    @Mapping(source="comments", target="emissionsComment")
    EmissionsDataType emissionsFromEmission(Emission source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="unitIdentifier", target="identifier")
    @Mapping(source="facilitySite.programSystemCode.code", target="programSystemCode")
    IdentificationDataType identificationFromEmissionsUnit(EmissionsUnit source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="emissionsProcessIdentifier", target="identifier")
    @Mapping(source="emissionsUnit.facilitySite.programSystemCode.code", target="programSystemCode")
    IdentificationDataType identificationFromEmissionsProcess(EmissionsProcess source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="pathId", target="identifier")
    @Mapping(source="facilitySite.programSystemCode.code", target="programSystemCode")
    IdentificationDataType identificationFromControlPath(ControlPath source);

    // TODO: the XML appears to only support 1 operating detail per reporting period, might want to change our db schema
    default OperatingDetailsDataType operatingDetailsFromOperatingDetailList(Collection<OperatingDetail> source) {

        return operatingDetailsFromOperatingDetail(source.stream().findFirst().orElse(null));
    }

    default List<IdentificationDataType> identificationListFromEmissionsUnit(EmissionsUnit source) {
        if (source == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(identificationFromEmissionsUnit(source));
    }

    // TODO: the XML appears to support multiple process identifications per process and some examples contain multiple. Might want to change our db
    default List<IdentificationDataType> identificationListFromEmissionsProcess(EmissionsProcess source) {
        if (source == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(identificationFromEmissionsProcess(source));
    }

    default List<IdentificationDataType> identificationListFromControlPath(ControlPath source) {
        if (source == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(identificationFromControlPath(source));
    }

}
