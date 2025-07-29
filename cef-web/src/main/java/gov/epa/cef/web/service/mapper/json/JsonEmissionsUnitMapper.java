/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.mapper.json;

import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.service.dto.json.*;
import net.exchangenetwork.schema.cer._2._0.*;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = {JsonReleasePointMapper.class})
public interface JsonEmissionsUnitMapper {

    @Mapping(source="designCapacity", target="designCapacity.value")
    @Mapping(source="unitOfMeasureCode", target="designCapacity.unit")
    @Mapping(source="operatingStatusCode", target="statusCode")
    @Mapping(source="statusYear", target="statusCodeYear")
    @Mapping(source="comments", target="comment")
    @Mapping(source=".", target="identification")
    EmissionsUnitJsonDto fromEmissionsUnit(EmissionsUnit source);

    @Mapping(source="sccCode", target="sourceClassificationCode")
    @Mapping(source="aircraftEngineTypeCode", target="aircraftEngineTypeCode")
    @Mapping(source="operatingStatusCode", target="statusCode")
    @Mapping(source="statusYear", target="statusCodeYear")
    @Mapping(source="comments", target="comment")
    @Mapping(source=".", target="identification")
    @Mapping(source="annualReportingPeriods", target="reportingPeriods")
    @Mapping(source="releasePointAppts", target="releasePointApportionment")
    @Mapping(target="isBillable", expression="java( source.getSltBillingExempt() != null ? String.valueOf(!source.getSltBillingExempt()) : null )")
    EmissionsProcessJsonDto processFromEmissionsProcess(EmissionsProcess source);

    @Mapping(source="percent", target="averagePercentEmissions")
    @Mapping(source="releasePoint", target="releasePointIdentification")
    @Mapping(target="releasePointApportionmentIsUncontrolled", expression = "java( Boolean.valueOf(source.getControlPath() == null).toString() )")
    @Mapping(source="controlPath", target="pathIdentification")
    ReleasePointApptJsonDto rpApptFromReleasePointAppt(ReleasePointAppt source);

    @Mapping(source="calculationParameterValue", target="calculationParameter.value")
    @Mapping(source="calculationParameterUom", target="calculationParameter.unit")
    @Mapping(source="comments", target="comment")
    @Mapping(source="fuelUseValue", target="fuelUse.value")
    @Mapping(source="fuelUseUom", target="fuelUse.unit")
    @Mapping(source="heatContentValue", target="heatContent.value")
    @Mapping(source="heatContentUom", target="heatContent.unit")
    ReportingPeriodJsonDto periodFromReportingPeriod(ReportingPeriod source);

    @Mapping(source="actualHoursPerPeriod", target="actualHoursPerPeriod")
    @Mapping(source="avgDaysPerWeek", target="averageDaysPerWeek")
    @Mapping(source="avgHoursPerDay", target="averageHoursPerDay")
    @Mapping(source="avgWeeksPerPeriod", target="averageWeeksPerPeriod")
    @Mapping(source="percentWinter", target="percentWinterActivity")
    @Mapping(source="percentSpring", target="percentSpringActivity")
    @Mapping(source="percentSummer", target="percentSummerActivity")
    @Mapping(source="percentFall", target="percentFallActivity")
    OperatingDetailJsonDto operatingDetailsFromOperatingDetail(OperatingDetail source);

    @Mapping(source="pollutant", target="pollutantCode")
    @Mapping(source="totalEmissions", target="totalEmissions.value", numberFormat = "0.0#####")
    @Mapping(source="emissionsUomCode", target="totalEmissions.unit")
    @Mapping(source="emissionsFactor", target="emissionFactor")
    @Mapping(source="emissionsNumeratorUom", target="emissionFactorNumeratorUnitofMeasureCode")
    @Mapping(source="emissionsDenominatorUom", target="emissionFactorDenominatorUnitofMeasureCode")
    @Mapping(source="emissionsFactorText", target="emissionFactorText")
    @Mapping(source="emissionsCalcMethodCode", target="emissionCalculationMethodCode")
    @Mapping(source="comments", target="comment")
    EmissionJsonDto emissionsFromEmission(Emission source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="unitIdentifier", target="identifier")
    @Mapping(source="facilitySite.programSystemCode.code", target="programSystemCode")
    IdentificationJsonDto identificationFromEmissionsUnit(EmissionsUnit source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="emissionsProcessIdentifier", target="identifier")
    @Mapping(source="emissionsUnit.facilitySite.programSystemCode.code", target="programSystemCode")
    IdentificationJsonDto identificationFromEmissionsProcess(EmissionsProcess source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="pathId", target="identifier")
    @Mapping(source="facilitySite.programSystemCode.code", target="programSystemCode")
    IdentificationJsonDto identificationFromControlPath(ControlPath source);

    // TODO: the XML appears to only support 1 operating detail per reporting period, might want to change our db schema
    default OperatingDetailJsonDto operatingDetailsFromOperatingDetailList(Collection<OperatingDetail> source) {

        return operatingDetailsFromOperatingDetail(source.stream().findFirst().orElse(null));
    }

    default List<IdentificationJsonDto> identificationListFromEmissionsUnit(EmissionsUnit source) {
        if (source == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(identificationFromEmissionsUnit(source));
    }

    // TODO: the XML appears to support multiple process identifications per process and some examples contain multiple. Might want to change our db
    default List<IdentificationJsonDto> identificationListFromEmissionsProcess(EmissionsProcess source) {
        if (source == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(identificationFromEmissionsProcess(source));
    }

    default List<IdentificationJsonDto> identificationListFromControlPath(ControlPath source) {
        if (source == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(identificationFromControlPath(source));
    }

}
