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

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {})
public interface JsonControlMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="pathId", target="name")
    @Mapping(source="percentControl", target="percentPathEffectiveness")
    @Mapping(source=".", target="identification")
    @Mapping(source="assignments", target="controlPathDefinition")
    @Mapping(source="pollutants", target="controlPathPollutants")
    ControlPathJsonDto sitePathFromControlPath(ControlPath source);

    List<ControlPathJsonDto> sitePathListFromControlPath(List<ControlPath> source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="sequenceNumber", target="sequenceNumber")
    @Mapping(source="percentApportionment", target="averagePercentApportionment")
    @Mapping(source="control", target="controlIdentification")
    @Mapping(source="controlPathChild", target="pathIdentification")
    ControlAssignmentJsonDto controlPathDefinitionFromControlAssignment(ControlAssignment source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="pollutant", target="pollutantCode")
    @Mapping(source="percentReduction", target="percentControlMeasuresReductionEfficiency")
    ControlPollutantJsonDto controlPollutantFromControlPathPollutant(ControlPathPollutant source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="controlMeasureCode", target="controlMeasureCode")
    @Mapping(source="description", target="description")
    @Mapping(source="percentControl", target="percentControlEffectiveness")
    @Mapping(source="upgradeDate", target="upgradeDate")
    @Mapping(source="upgradeDescription", target="upgradeDescription")
    @Mapping(source="operatingStatusCode", target="statusCode")
    @Mapping(source="statusYear", target="statusCodeYear")
    @Mapping(source="numberOperatingMonths", target="numberOperatingMonths")
    @Mapping(source="startDate", target="startDate")
    @Mapping(source="endDate", target="endDate")
    @Mapping(source="comments", target="comment")
    @Mapping(source=".", target="identification")
    @Mapping(source="pollutants", target="controlPollutants")
    ControlJsonDto siteControlDataTypeFromControl(Control source);

    List<ControlJsonDto> siteControlDataTypeListFromControl(List<Control> source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="pollutant", target="pollutantCode")
    @Mapping(source="percentReduction", target="percentControlMeasuresReductionEfficiency")
    ControlPollutantJsonDto controlPollutantFromControlPollutant(ControlPollutant source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="pathId", target="identifier")
    @Mapping(source="facilitySite.programSystemCode.code", target="programSystemCode")
    IdentificationJsonDto identificationFromControlPath(ControlPath source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="identifier", target="identifier")
    @Mapping(source="facilitySite.programSystemCode.code", target="programSystemCode")
    IdentificationJsonDto identificationFromControl(Control source);

    default List<IdentificationJsonDto> identificationListFromControlPath(ControlPath source) {
        if (source == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(identificationFromControlPath(source));
    }

    default List<IdentificationJsonDto> identificationListFromControl(Control source) {
        if (source == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(identificationFromControl(source));
    }

}
