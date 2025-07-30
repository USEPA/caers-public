/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.mapper.cers._2._0;

import gov.epa.cef.web.domain.FacilityNAICSXref;
import gov.epa.cef.web.domain.FacilitySite;
import net.exchangenetwork.schema.cer._2._0.AddressDataType;
import net.exchangenetwork.schema.cer._2._0.FacilityIdentificationDataType;
import net.exchangenetwork.schema.cer._2._0.FacilityNAICSDataType;
import net.exchangenetwork.schema.cer._2._0.FacilitySiteDataType;
import net.exchangenetwork.schema.cer._2._0.GeographicCoordinatesDataType;

import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = {CersV2ControlMapper.class, CersV2EmissionsUnitMapper.class, CersV2ReleasePointMapper.class})
public interface CersV2FacilitySiteMapper {

    @Mapping(source="facilityCategoryCode.code", target="facilityCategoryCode")
    @Mapping(source="facilitySourceTypeCode.code", target="facilitySourceTypeCode")
    @Mapping(source="name", target="facilitySiteName")
    @Mapping(source="description", target="facilitySiteDescription")
    @Mapping(source="operatingStatusCode.code", target="facilitySiteStatusCode")
    @Mapping(source="statusYear", target="facilitySiteStatusCodeYear")
    @Mapping(source="facilityNAICS", target="facilityNAICS")
    @Mapping(source=".", target="facilityIdentification")
    @Mapping(source=".", target="facilitySiteAddress")
    @Mapping(source=".", target="facilitySiteGeographicCoordinates")
    @Mapping(source="comments", target="facilitySiteComment")
    @Mapping(source="controlPaths", target="facilitySitePath")
    @Mapping(source="controls", target="facilitySiteControl")
    @Mapping(source="emissionsUnits", target="emissionsUnit")
    @Mapping(source="releasePoints", target="releasePoint")
    FacilitySiteDataType fromFacilitySite(FacilitySite source);

    @Mapping(source="naicsCode.code", target="NAICSCode")
    @Mapping(source="naicsCodeType", target="NAICSType")
    FacilityNAICSDataType cersNaicsFromFacilityNAICSXref(FacilityNAICSXref source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="agencyFacilityIdentifier", target="facilitySiteIdentifier")
    @Mapping(source="programSystemCode.code", target="programSystemCode")
    @Mapping(source="countyCode.code", target="stateAndCountyFIPSCode")
    @Mapping(source="tribalCode.code", target="tribalCode")
    FacilityIdentificationDataType facilityIdentificationFromFacilitySite(FacilitySite source);

    @Mapping(source="latitude", target="latitudeMeasure")
    @Mapping(source="longitude", target="longitudeMeasure")
    @Mapping(target="horizontalReferenceDatumCode", constant="003")
    GeographicCoordinatesDataType facilityGeographicCoordinatesFromFacilitySite(FacilitySite source);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source="streetAddress", target="locationAddressText")
    @Mapping(source="city", target="localityName")
    @Mapping(source="stateCode.uspsCode", target="locationAddressStateCode")
    @Mapping(source="postalCode", target="locationAddressPostalCode")
    @Mapping(source="countryCode", target="locationAddressCountryCode")
    AddressDataType addressFromFacilitySite(FacilitySite source);

    

    default List<FacilityIdentificationDataType> facilityIdentificationListFromFacilitySite(FacilitySite source) {
        if (source == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(facilityIdentificationFromFacilitySite(source));
    }

    default List<AddressDataType> addressListFromFacilitySite(FacilitySite source) {
        if (source == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(addressFromFacilitySite(source));
    }

//    default List<AffiliationDataType> contactListFromFacilitySite(FacilitySite source) {
//      if (source == null) {
//          return Collections.emptyList();
//      }
//      return Collections.singletonList(facilitySiteAffiliation(source));
//    }
//
//    default List<AddressDataType> addressListFromFacilitySiteContact(FacilitySiteContact source) {
//      if (source == null) {
//          return Collections.emptyList();
//      }
//      return Collections.singletonList(addressFromFacilitySiteContact(source));
//    }
//
//    default List<CommunicationDataType> contactCommunicationListFromFacilitySiteContact(FacilitySiteContact source) {
//      if (source == null) {
//          return Collections.emptyList();
//      }
//      return Collections.singletonList(contactCommunicationFacilitySiteContact(source));
//    }

}
