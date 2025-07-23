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
package gov.epa.cef.web.api.rest;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.epa.cef.web.config.AppPropertyName;
import gov.epa.cef.web.config.CefConfig;
import gov.epa.cef.web.config.SLTPropertyName;
import gov.epa.cef.web.provider.system.AdminPropertyProvider;
import gov.epa.cef.web.provider.system.SLTPropertyProvider;
import gov.epa.cef.web.service.dto.PropertyDto;

@RestController
@RequestMapping("/api/property")
public class PropertyApi {

    @Autowired
    private AdminPropertyProvider propertyProvider;

    @Autowired
    private SLTPropertyProvider sltPropertyProvider;

    @Autowired
    private CefConfig cefConfig;

    /**
     * Retrieve announcement enabled property
     * @return
     */
    @GetMapping(value = "/announcement/enabled")
    @ResponseBody
    @Operation(summary = "Get property announcement enabled",
        description = "Get property announcement enabled",
        tags = "Property")
    public ResponseEntity<Boolean> retrieveAnnouncementEnabled() {
        Boolean result = propertyProvider.getBoolean(AppPropertyName.FeatureAnnouncementEnabled);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve announcement text property
     * @return
     */
    @GetMapping(value = "/announcement/text")
    @ResponseBody
    @Operation(summary = "Get property announcement text",
        description = "Get property announcement text",
        tags = "Property")
    public ResponseEntity<PropertyDto> retrieveAnnouncementText() {
        String result = propertyProvider.getString(AppPropertyName.FeatureAnnouncementText);
        return new ResponseEntity<>(new PropertyDto().withValue(result), HttpStatus.OK);
    }

    /**
     * Retrieve bulk entry enabled property
     * @return
     */
    @GetMapping(value = "/bulkEntry/enabled")
    @ResponseBody
    @Operation(summary = "Get property bulk entry enabled",
        description = "Get property bulk entry enabled",
        tags = "Property")
    public ResponseEntity<Boolean> retrieveBulkEntryEnabled() {
        Boolean result = propertyProvider.getBoolean(AppPropertyName.FeatureBulkEntryEnabled);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve user feedback enabled property
     * @return
     */
    @GetMapping(value = "/userFeedback/enabled")
    @ResponseBody
    @Operation(summary = "Get property user feedback enabled",
        description = "Get property user feedback enabled",
        tags = "Property")
    public ResponseEntity<Boolean> retrieveUserFeedbackEnabled() {
        Boolean result = propertyProvider.getBoolean(AppPropertyName.FeatureUserFeedbackEnabled);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve multipartfile maximum file upload size
     * @return
     */
    @GetMapping(value = "/attachments/maxSize")
    @ResponseBody
    @Operation(summary = "Get property report attachment max size",
        description = "Get property report attachment max size",
        tags = "Property")
    public ResponseEntity<PropertyDto> retrieveReportAttachmentMaxSize() {
    	Long result = Long.valueOf(this.cefConfig.getMaxFileSize().replaceAll("[^0-9]", ""));
        return new ResponseEntity<>(new PropertyDto().withValue(result.toString()), HttpStatus.OK);
    }

    /**
     * Retrieve excel export enabled property
     * @return
     */
    @GetMapping(value = "/excelExport/enabled")
    @ResponseBody
    @Operation(summary = "Get property excel export enabled",
        description = "Get property excel export enabled",
        tags = "Property")
    public ResponseEntity<Boolean> retrieveExcelExportEnabled() {
        Boolean result = propertyProvider.getBoolean(AppPropertyName.FeatureExcelExportEnabled);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve GADNR threshold screening enabled property
     * @return
     */
    @GetMapping(value = "/thresholdScreening/gadnr/{slt}/enabled")
    @ResponseBody
    @Operation(summary = "Get property threshold screening GADNR enabled",
        description = "Get property threshold screening GADNR enabled",
        tags = "Property")
    public ResponseEntity<Boolean> retrieveThresholdScreeningGADNREnabled(@NotNull @PathVariable String slt) {
        Boolean result = sltPropertyProvider.getBoolean(SLTPropertyName.SLTFeatureThresholdScreeningGADNREnabled, slt);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve monthly fuel reporting enabled property
     * @return
     */
    @GetMapping(value = "/monthlyFuelReporting/{slt}/enabled")
    @ResponseBody
    @Operation(summary = "Get property SLT monthly fuel reporting enabled",
        description = "Get property SLT monthly fuel reporting enabled",
        tags = "Property")
    public ResponseEntity<Boolean> retrieveSltMonthlyFuelReportingEnabled(@NotNull @PathVariable String slt) {
        Boolean result = sltPropertyProvider.getBoolean(SLTPropertyName.MonthlyFuelReportingEnabled, slt);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve monthly fuel reporting initial year property
     * @return
     */
    @GetMapping(value = "/monthlyFuelReportingInitialYear/{slt}")
    @ResponseBody
    @Operation(summary = "Get property SLT monthly fuel reporting initial year",
        description = "Get property SLT monthly fuel reporting initial year",
        tags = "Property")
    public ResponseEntity<Short> retrieveSltMonthlyFuelReportingInitialYear(@NotNull @PathVariable String slt) {
        Short result = sltPropertyProvider.getShort(SLTPropertyName.MonthlyFuelReportingInitialYear, slt);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve Facility Naics enabled property
     * @return
     */
    @GetMapping(value = "/facilityNaics/{slt}/enabled")
    @ResponseBody
    @Operation(summary = "Get property NAICS enabled",
        description = "Get property NAICS enabled",
        tags = "Property")
    public ResponseEntity<Boolean> retrieveFacilityNaicsEnabled(@NotNull @PathVariable String slt) {

        Boolean result = sltPropertyProvider.getBoolean(SLTPropertyName.FacilityNaicsEnabled, slt);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve SLT SCC edit enabled property
     * @return
     */
    @GetMapping(value = "/editScc/{slt}/enabled")
    @ResponseBody
    @Operation(summary = "Get property edit scc enabled",
        description = "Get property edit scc enabled",
        tags = "Property")
    public ResponseEntity<Boolean> retrieveEditSccEnabled(@NotNull @PathVariable String slt) {

        Boolean result = sltPropertyProvider.getBoolean(SLTPropertyName.SLTFeatureSLTEditSccEnabled, slt);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve announcement enabled property
     * @return
     */
    @GetMapping(value = "/announcement/{slt}/enabled")
    @ResponseBody
    @Operation(summary = "Get property SLT announcement enabled",
        description = "Get property SLT announcement enabled",
        tags = "Property")
    public ResponseEntity<Boolean> retrieveSltAnnouncementEnabled(@NotNull @PathVariable String slt) {
        Boolean result = sltPropertyProvider.getBoolean(SLTPropertyName.SLTFeatureAnnouncementEnabled, slt);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve announcement text property
     * @return
     */
    @GetMapping(value = "/announcement/{slt}/text")
    @ResponseBody
    @Operation(summary = "Get property SLT announcement text",
        description = "Get property SLT announcement text",
        tags = "Property")
    public ResponseEntity<PropertyDto> retrieveSltAnnouncementText(@NotNull @PathVariable String slt) {
        String result = sltPropertyProvider.getString(SLTPropertyName.SLTFeatureAnnouncementText, slt);
        return new ResponseEntity<>(new PropertyDto().withValue(result), HttpStatus.OK);
    }

    /**
     * Retrieve report upload properties
     * @return
     */
    @GetMapping(value = "/reportUploadTypes/{slt}")
    @ResponseBody
    @Operation(summary = "Get property all report upload properties for program system",
        description = "Get property all report upload properties for program system",
        tags = "Property")
    public ResponseEntity<PropertyDto> retrieveAllReportUploadPropertiesForProgramSystem(@NotNull @PathVariable String slt) {
        String result = sltPropertyProvider.retrieveAllReportUploadPropertiesForProgramSystem(slt);
        return new ResponseEntity<>(new PropertyDto().withValue(result), HttpStatus.OK);
    }

    /**
    * Retrieve report certify and submit button enabled property
    * @return
    */
   @GetMapping(value = "/reportCertification/enabled")
   @ResponseBody
   @Operation(summary = "Get property report certification enabled",
       description = "Get property report certification enabled",
       tags = "Property")
   public ResponseEntity<Boolean> retrieveReportCertificationEnabled() {
       Boolean result = propertyProvider.getBoolean(AppPropertyName.ReportCertificationEnabled);
       return new ResponseEntity<>(result, HttpStatus.OK);
   }

   /**
    * Retrieve slt compendium emission factors enabled property
    * @return
    */
   @GetMapping(value = "/emissionFactorCompendium/{slt}/enabled")
   @ResponseBody
   @Operation(summary = "Get property SLT feature emission factor compendium enabled",
       description = "Get property SLT feature emission factor compendium enabled",
       tags = "Property")
   public ResponseEntity<Boolean> retrieveSltFeatureEmissionFactorCompendiumEnabled(@NotNull @PathVariable String slt) {
       Boolean result = sltPropertyProvider.getBoolean(SLTPropertyName.SLTFeatureEmissionFactorCompendiumEnabled, slt);
       return new ResponseEntity<>(result, HttpStatus.OK);
   }

   /**
    * Retrieve new report creation enabled property
    * @return
    */
   @GetMapping(value = "/newReportCreation/enabled")
   @ResponseBody
   @Operation(summary = "Get property new report creation enabled",
       description = "Get property mew report creation enabled",
       tags = "Property")
   public ResponseEntity<Boolean> retrieveNewReportCreationEnabled() {
       Boolean result = propertyProvider.getBoolean(AppPropertyName.NewReportCreationEnabled);
       return new ResponseEntity<>(result, HttpStatus.OK);
   }

   /**
    * Retrieve slt new report creation enabled property
    * @return
    */
   @GetMapping(value = "/newReportCreation/{slt}/enabled")
   @ResponseBody
   @Operation(summary = "Get property SLT new report creation enabled",
       description = "Get property SLT new report creation enabled",
       tags = "Property")
   public ResponseEntity<Boolean> retrieveSLTNewReportCreationEnabled(@NotNull @PathVariable String slt) {
       Boolean result = sltPropertyProvider.getBoolean(SLTPropertyName.NewReportCreationEnabled, slt);
       return new ResponseEntity<>(result, HttpStatus.OK);
   }

    /**
     * Retrieve slt billing exempt enabled property
     * @return
     */
    @GetMapping(value = "/sltBillingExempt/{slt}/enabled")
    @ResponseBody
    @Operation(summary = "Get property SLT billing exempt enabled",
        description = "Get property SLT billing exempt enabled",
        tags = "Property")
    public ResponseEntity<Boolean> retrieveSltBillingExemptEnabled(@NotNull @PathVariable String slt) {
        Boolean result = sltPropertyProvider.getBoolean(SLTPropertyName.SLTFeatureBillingExemptEnabled, slt);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
