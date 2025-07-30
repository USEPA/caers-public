/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.api.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.epa.cef.web.domain.AdminProperty;
import gov.epa.cef.web.provider.system.AdminPropertyProvider;
import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.service.EmissionService;
import gov.epa.cef.web.service.NotificationService;
import gov.epa.cef.web.service.PropertyService;
import gov.epa.cef.web.service.ReportingPeriodService;
import gov.epa.cef.web.service.dto.EmissionDto;
import gov.epa.cef.web.service.dto.PropertyDto;
import gov.epa.cef.web.service.dto.ReportingPeriodDto;
import gov.epa.cef.web.service.mapper.AppPropertyMapper;

import gov.epa.cef.web.config.AppPropertyName;

@RestController
@RequestMapping("/api/admin/property")
@RolesAllowed(value = {AppRole.ROLE_CAERS_ADMIN})
public class AdminPropertyApi {

    @Autowired
    private AdminPropertyProvider propertyProvider;

    @Autowired
    private EmissionService emissionService;

    @Autowired
    private ReportingPeriodService reportingPeriodService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PropertyService propService;

    @Autowired
    private AppPropertyMapper mapper;

    /**
     * Retrieve a properties
     * @return
     */
    @GetMapping(value = "/{name}")
    @Operation(summary = "Retrieve property",
        description = "Get admin property information that match the provided criteria.",
        tags = "Admin Property")
    public ResponseEntity<PropertyDto> retrieveProperty(@NotNull @PathVariable String name) {
        PropertyDto result = mapper.toDto(propertyProvider.retrieve(new PropertyDto().withName(name)));

        if (result.getName() != null && result.getName().equals(AppPropertyName.FeatureAnnouncementText)) {
			propService.sanitizeHtml(result);
		}

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieve all properties
     * @return
     */
    @GetMapping
    @Operation(summary = "Retrieve all properties",
        description = "Get all admin property information that match the provided criteria.",
        tags = "Admin Property")
    public ResponseEntity<List<PropertyDto>> retrieveAllProperties() {
        List<PropertyDto> result = mapper.toDtoList(propertyProvider.retrieveAll());

        for (PropertyDto prop : result) {
    		if (prop.getName() != null && prop.getName().equals(AppPropertyName.FeatureAnnouncementText)) {
    			propService.sanitizeHtml(prop);
    		}
    	}

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Update a property value
     * @return
     */
    @PutMapping(value = "/{propName}")
    @Operation(summary = "Update property",
        description = "Update admin property information that match the provided criteria.",
        tags = "Admin Property")
    public ResponseEntity<PropertyDto> updateProperty(@NotNull @PathVariable String propName,
            @NotNull @RequestBody PropertyDto dto) {

    	if (dto.getName() != null && dto.getName().equals(AppPropertyName.FeatureAnnouncementText)) {
			propService.sanitizeHtml(dto);
		}

        AdminProperty result = this.propertyProvider.update(dto.withName(propName), dto.getValue());
        return new ResponseEntity<>(mapper.toDto(result), HttpStatus.OK);
    }

    /**
     * Update multiple properties
     * @return
     */
    @PostMapping
    @Operation(summary = "Update properties",
        description = "Update admin property information that match the provided criteria.",
        tags = "Admin Property")
    public ResponseEntity<List<PropertyDto>> updateProperties(@NotNull @RequestBody List<PropertyDto> dtos) {

    	for (PropertyDto prop : dtos) {
    		if (prop.getName() != null && prop.getName().equals(AppPropertyName.FeatureAnnouncementText)) {
    			propService.sanitizeHtml(prop);
    		}
    	}

        List<PropertyDto> result = dtos.stream().map(dto -> {
            AdminProperty prop = this.propertyProvider.update(dto, dto.getValue());
            return mapper.toDto(prop);
        }).collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Send a test email to the Admin email list
     */
    @PostMapping(value = "/sendTestEmail")
    @Operation(summary = "Send admin email",
        description = "Send admin email.",
        tags = "Admin Property")
    public void sendTestAdminEmail() {

        this.notificationService.sendAdminNotification(NotificationService.AdminEmailType.AdminTest, null);
    }

    @PostMapping(value = "/emission/recalculate/{reportId}")
    @Operation(summary = "Recalculate Emission Total by Tons",
        description = "Recalculate emission total by tons.",
        tags = "Admin Property")
    public ResponseEntity<List<EmissionDto>> recalculateEmissionTotalTons(@NotNull @PathVariable Long reportId) {

        List<EmissionDto> result = emissionService.recalculateEmissionTons(reportId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/calculateNonPointStandardFuelUse")
    @Operation(summary = "Calculate Non Point Standard Fuel Use",
        description = "Calculate non point standard fuel use.",
        tags = "Admin Property")
    public ResponseEntity<List<ReportingPeriodDto>> calculateNonPointStandardizedFuelUse() {

        List<ReportingPeriodDto> result = reportingPeriodService.calculateNonPointStandardizedFuelUse();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
