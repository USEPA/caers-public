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
