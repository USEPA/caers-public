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

import gov.epa.cef.web.domain.FacilityNAICSXref;
import gov.epa.cef.web.domain.FacilitySite;
import gov.epa.cef.web.domain.FipsStateCode;
import gov.epa.cef.web.domain.FacilitySiteContact;
import gov.epa.cef.web.service.dto.json.FacilityIdentificationJsonDto;
import gov.epa.cef.web.service.dto.json.FacilitySiteJsonDto;
import gov.epa.cef.web.service.dto.json.shared.AddressJsonDto;
import gov.epa.cef.web.service.dto.json.shared.FacilityNAICSJsonDto;
import gov.epa.cef.web.service.dto.json.FacilitySiteContactJsonDto;
import gov.epa.cef.web.service.dto.json.shared.GeographicCoordinatesJsonDto;
import gov.epa.cef.web.service.dto.json.shared.reference.StateJsonDto;
import net.exchangenetwork.schema.cer._2._0.*;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = {JsonControlMapper.class, JsonEmissionsUnitMapper.class, JsonReleasePointMapper.class})
public interface JsonFacilitySiteMapper {

    @Mapping(source="facilityCategoryCode.code", target="facilityCategoryCode")
    @Mapping(source="operatingStatusCode", target="statusCode")
    @Mapping(source="statusYear", target="statusCodeYear")
    @Mapping(source=".", target="facilityIdentification")
    @Mapping(source=".", target="facilitySiteAddress")
    @Mapping(source=".", target="facilitySiteGeographicCoordinates")
    @Mapping(source="comments", target="comment")
    @Mapping(source="mailingStreetAddress", target="mailingAddress.addressText")
    @Mapping(source="mailingCity", target="mailingAddress.localityName")
    @Mapping(source="mailingStateCode", target="mailingAddress.state")
    @Mapping(source="mailingPostalCode", target="mailingAddress.postalCode")
    @Mapping(source="emissionsReport.eisProgramId", target="eisProgramId")
    @Mapping(source="contacts", target="facilityContacts")
    FacilitySiteJsonDto fromFacilitySite(FacilitySite source);

    @Mapping(source="streetAddress", target="streetAddress.addressText")
    @Mapping(source="city", target="streetAddress.localityName")
    @Mapping(source="countyCode", target="streetAddress.county")
    @Mapping(source="stateCode", target="streetAddress.state")
    @Mapping(source="countryCode", target="streetAddress.country.code")
    @Mapping(source="postalCode", target="streetAddress.postalCode")
    @Mapping(source="mailingStreetAddress", target="mailingStreetAddress.addressText")
    @Mapping(source="mailingCity", target="mailingStreetAddress.localityName")
    @Mapping(source="mailingStateCode", target="mailingStreetAddress.state")
    @Mapping(source="mailingCountryCode", target="mailingStreetAddress.country.code")
    @Mapping(source="mailingPostalCode", target="mailingStreetAddress.postalCode")
    FacilitySiteContactJsonDto fromFacilitySiteContact(FacilitySiteContact source);

    @Mapping(source="naicsCode.code", target="code")
    FacilityNAICSJsonDto cersNaicsFromFacilityNAICSXref(FacilityNAICSXref source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="agencyFacilityIdentifier", target="identifier")
    @Mapping(source="programSystemCode.code", target="programSystemCode")
    @Mapping(source="countyCode.code", target="stateAndCountyFIPSCode")
    FacilityIdentificationJsonDto facilityIdentificationFromFacilitySite(FacilitySite source);

    @Mapping(source="latitude", target="latitudeMeasure")
    @Mapping(source="longitude", target="longitudeMeasure")
    GeographicCoordinatesJsonDto facilityGeographicCoordinatesFromFacilitySite(FacilitySite source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="streetAddress", target="addressText")
    @Mapping(source="city", target="localityName")
    @Mapping(source="stateCode", target="state")
    @Mapping(source="countyCode", target="county")
    @Mapping(source="postalCode", target="postalCode")
    @Mapping(source="countryCode", target="country.code")
    AddressJsonDto addressFromFacilitySite(FacilitySite source);

    @Mapping(source="uspsCode", target="code")
    @Mapping(source="code", target="fipsCode")
    StateJsonDto jsonStateFromState(FipsStateCode source);



    default List<FacilityIdentificationJsonDto> facilityIdentificationListFromFacilitySite(FacilitySite source) {
        if (source == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(facilityIdentificationFromFacilitySite(source));
    }

    default List<AddressJsonDto> addressListFromFacilitySite(FacilitySite source) {
        if (source == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(addressFromFacilitySite(source));
    }

}
