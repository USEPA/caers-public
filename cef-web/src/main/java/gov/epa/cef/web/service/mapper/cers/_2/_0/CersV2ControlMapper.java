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
package gov.epa.cef.web.service.mapper.cers._2._0;

import java.util.Collections;
import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import gov.epa.cef.web.domain.Control;
import gov.epa.cef.web.domain.ControlAssignment;
import gov.epa.cef.web.domain.ControlPath;
import gov.epa.cef.web.domain.ControlPathPollutant;
import gov.epa.cef.web.domain.ControlPollutant;
import net.exchangenetwork.schema.cer._2._0.ControlPathDefinitionDataType;
import net.exchangenetwork.schema.cer._2._0.ControlPollutantDataType;
import net.exchangenetwork.schema.cer._2._0.IdentificationDataType;
import net.exchangenetwork.schema.cer._2._0.SiteControlDataType;
import net.exchangenetwork.schema.cer._2._0.SitePathDataType;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {})
public interface CersV2ControlMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="pathId", target="pathName")
    @Mapping(source="percentControl", target="percentPathEffectiveness")
    @Mapping(source="description", target="pathDescription")
    @Mapping(source=".", target="controlPathIdentification")
    @Mapping(source="assignments", target="controlPathDefinition")
    @Mapping(source="pollutants", target="controlPathControlPollutant")
    SitePathDataType sitePathFromControlPath(ControlPath source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="sequenceNumber", target="sequenceNumber")
    @Mapping(source="percentApportionment", target="averagePercentApportionment")
    @Mapping(source="control", target="controlPathDefinitionControlIdentification")
    @Mapping(source="controlPathChild", target="controlPathDefinitionPathIdentification")
    ControlPathDefinitionDataType controlPathDefinitionFromControlAssignment(ControlAssignment source);
    
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="pollutant.pollutantCode", target="pollutantCode")
    @Mapping(source="percentReduction", target="percentControlMeasuresReductionEfficiency")
    ControlPollutantDataType controlPollutantFromControlPathPollutant(ControlPathPollutant source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="controlMeasureCode.code", target="controlMeasureCode")
    @Mapping(source="description", target="controlDescription")
    @Mapping(source="percentControl", target="percentControlEffectiveness")
    @Mapping(source="upgradeDate", target="controlUpgradeDate")
    @Mapping(source="upgradeDescription", target="controlUpgradeDescription")
    @Mapping(source="operatingStatusCode.code", target="controlStatusCode")
    @Mapping(source="statusYear", target="controlStatusCodeYear")
    @Mapping(source="numberOperatingMonths", target="controlNumberOperatingMonths")
    @Mapping(source="startDate", target="controlStartDate")
    @Mapping(source="endDate", target="controlEndDate")
    @Mapping(source="comments", target="controlComment")
    @Mapping(source=".", target="siteControlIdentification")
    @Mapping(source="pollutants", target="siteControlPollutant")
    SiteControlDataType siteControlDataTypeFromControl(Control source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="pollutant.pollutantCode", target="pollutantCode")
    @Mapping(source="percentReduction", target="percentControlMeasuresReductionEfficiency")
    ControlPollutantDataType controlPollutantFromControlPollutant(ControlPollutant source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="pathId", target="identifier")
    @Mapping(source="facilitySite.programSystemCode.code", target="programSystemCode")
    IdentificationDataType identificationFromControlPath(ControlPath source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="identifier", target="identifier")
    @Mapping(source="facilitySite.programSystemCode.code", target="programSystemCode")
    IdentificationDataType identificationFromControl(Control source);

    default List<IdentificationDataType> identificationListFromControlPath(ControlPath source) {
        if (source == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(identificationFromControlPath(source));
    }

    default List<IdentificationDataType> identificationListFromControl(Control source) {
        if (source == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(identificationFromControl(source));
    }

}
