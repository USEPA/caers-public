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
package gov.epa.cef.web.service.validation.validator.federal;

import com.baidu.unbiz.fluentvalidator.ValidatorContext;

import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.EmissionsProcessRepository;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.ReleasePointRepository;
import gov.epa.cef.web.service.dto.EntityType;
import gov.epa.cef.web.service.dto.ValidationDetailDto;
import gov.epa.cef.web.service.validation.CefValidatorContext;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.service.validation.validator.BaseValidator;
import gov.epa.cef.web.util.ConstantUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnnualReleasePointValidator extends BaseValidator<ReleasePoint> {

    @Autowired
    private EmissionsReportRepository reportRepo;

    @Autowired
    private EmissionsProcessRepository emissionsProcessRepo;

    @Autowired
    private ReleasePointRepository rpRepo;

    private static final BigDecimal DEFAULT_TOLERANCE = BigDecimal.valueOf(0.003).setScale(6, RoundingMode.DOWN);
    private static final String VELOCITY_UOM_FPS = "FPS";
    private static final String VELOCITY_UOM_FPM = "FPM";
    private static final String FLOW_RATE_UOM_ACFS = "ACFS";
    private static final String FLOW_RATE_UOM_ACFM = "ACFM";
    private static final String UOM_FT = "FT";
    private static final String DIAMETER_FORMULA = "(Pi * (Stack Diameter /2) ^ 2) for circular stacks";
    private static final String LENGTH_WIDTH_FORMULA = "(Stack Length * Stack Width) for rectangular stacks";
    private static final String STACK_DIAMETER = "Stack Diameter is";
    private static final String STACK_LENGTH_WIDTH = "Stack Length/Stack Width are";
    private static final String LONGITUDE_2_2D = "Mid Point 2 Longitude";
    private static final String LATITUDE_2_2D = "Mid Point 2 Latitude";

    private boolean isEmissionProcessNonOperating(EmissionsProcess ep){
        return ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN.contentEquals(ep.getOperatingStatusCode().getCode())
            || ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(ep.getOperatingStatusCode().getCode());
    }

    private boolean checkIfReleasePointOnlyContainsNonOperatingEmissionsProcesses(ReleasePoint rp, CefValidatorContext context, boolean result){
        List<EmissionsProcess> emissionsProcesses = emissionsProcessRepo.findByReleasePointApptsReleasePointIdOrderByEmissionsProcessIdentifier(rp.getId());

        boolean onlyContainsNonOperating = true;
        for(EmissionsProcess ep: emissionsProcesses){
            if(!isEmissionProcessNonOperating(ep)){
                onlyContainsNonOperating = false;
                break;
            }
        }

        if(onlyContainsNonOperating && !emissionsProcesses.isEmpty()){
            result = false;
            context.addFederalError(ValidationField.RP_PROCESS_STATUS_CODE.value(),
                "releasePoint.statusTypeCode.NonOperatingStatus",
                createValidationDetails(rp));
        }
        return result;
    }

    @Override
    public boolean validate(ValidatorContext validatorContext, ReleasePoint releasePoint) {

        boolean result = true;

        CefValidatorContext context = getCefValidatorContext(validatorContext);

        BigDecimal facilitySiteLat = releasePoint.getFacilitySite().getLatitude();
        BigDecimal facilitySiteLong = releasePoint.getFacilitySite().getLongitude();

        String latLabel;
        String longLabel;

        switch (releasePoint.getTypeCode().getCode()) {
            case ConstantUtils.FUGITIVE_RELEASE_PT_AREA_TYPE:
                latLabel = "SW Corner Latitude";
                longLabel = "SW Corner Longitude";
                break;
            case ConstantUtils.FUGITIVE_RELEASE_PT_2D_TYPE:
                latLabel = "Mid Point 1 Longitude";
                longLabel = "Mid Point 1 Longitude";
                break;
            case ConstantUtils.FUGITIVE_RELEASE_PT_3D_TYPE:
                latLabel = "Center Latitude";
                longLabel = "Center Longitude";
                break;
            default:
                latLabel = "Latitude";
                longLabel = "Longitude";
        }

        // Disable most validations for non-operating release points
        //Only run the following checks is the Release Point status is operating. Otherwise, these checks are moot b/c
        //the data will not be sent to EIS and the user shouldn't have to go back and update them. Only id, status,
        //and status year, and RP type are sent to EIS for units that are not operating.
        if (ConstantUtils.STATUS_OPERATING.contentEquals(releasePoint.getOperatingStatusCode().getCode())) {

            // STACK RELEASE POINT CHECKS
            if (!ConstantUtils.FUGITIVE_RELEASE_POINT_CATEGORY.equals(releasePoint.getTypeCode().getCategory())) {

                if (releasePoint.getExitGasTemperature() == null) {

                    // Exit Gas Temperature is required.
                    result = false;
                    context.addFederalError(
                        ValidationField.RP_GAS_TEMP.value(),
                        "releasePoint.exitGasTemperature.required",
                        createValidationDetails(releasePoint));

                } else if (releasePoint.getExitGasTemperature() < -30 || releasePoint.getExitGasTemperature() > 4000) {

                    // Exit Gas Temperature must be between -30 and 4000 degrees Fahrenheit.
                    result = false;
                    context.addFederalError(
                        ValidationField.RP_GAS_TEMP.value(),
                        "releasePoint.exitGasTemperature.range",
                        createValidationDetails(releasePoint));
                }


                // Exit Gas Flow Rate or Exit Gas Velocity is required.
                if (releasePoint.getExitGasFlowRate() == null && releasePoint.getExitGasVelocity() == null) {

                    result = false;
                    context.addFederalError(
                        ValidationField.RP_GAS_RELEASE.value(),
                        "releasePoint.release.required",
                        createValidationDetails(releasePoint));
                }

                // Stack Height is required
                if (releasePoint.getStackHeight() == null) {

                    result = false;
                    context.addFederalError(
                        ValidationField.RP_STACK.value(),
                        "releasePoint.stack.required",
                        createValidationDetails(releasePoint),
                        "Stack Height");

                    // Stack Height range
                } else if (releasePoint.getStackHeight().compareTo(BigDecimal.ONE) == -1 || releasePoint.getStackHeight().compareTo(BigDecimal.valueOf(1300)) == 1) {

                    result = false;
                    context.addFederalError(
                        ValidationField.RP_STACK.value(),
                        "releasePoint.stack.heightRange",
                        createValidationDetails(releasePoint));
                }

                // Stack Diameter or stack length/width is required
                if (releasePoint.getStackDiameter() == null && (releasePoint.getStackWidth() == null || releasePoint.getStackLength() == null)) {

                    result = false;
                    context.addFederalError(
                        ValidationField.RP_STACK.value(),
                        "releasePoint.stack.diameterOrLengthWidth",
                        createValidationDetails(releasePoint));

                    // Only stack diameter or width/length allowed
                } else if (releasePoint.getStackDiameter() != null && (releasePoint.getStackLength() != null || releasePoint.getStackWidth() != null)) {
                    result = false;
                    context.addFederalError(
                        ValidationField.RP_STACK.value(),
                        "releasePoint.stack.noDiameterAndLengthWidth",
                        createValidationDetails(releasePoint)
                    );

                    // Check value ranges when a stack diameter OR width/length is reported.
                } else {
                    // Check Stack Diameter range
                    if (releasePoint.getStackDiameter() != null && (releasePoint.getStackDiameter().compareTo(BigDecimal.valueOf(0.001)) == -1 || releasePoint.getStackDiameter().compareTo(BigDecimal.valueOf(300)) == 1)) {

                        result = false;
                        context.addFederalError(
                            ValidationField.RP_STACK.value(),
                            "releasePoint.stack.diameterRange",
                            createValidationDetails(releasePoint));
                    }

                    // Calculation check, Stack Diameter must be less than Stack Height
                    if (releasePoint.getStackDiameter() != null
                        && releasePoint.getStackHeight() != null
                        && (releasePoint.getStackDiameter().compareTo(releasePoint.getStackHeight()) >= 0 )) {

                        result = false;
                        context.addFederalWarning(
                            ValidationField.RP_STACK_WARNING.value(),
                            "releasePoint.stackWarning.diameterCheck.height",
                            createValidationDetails(releasePoint));
                    }

                    if ((releasePoint.getStackDiameter() != null && releasePoint.getStackDiameter().compareTo(BigDecimal.ZERO) == 1)
                        || (releasePoint.getStackWidth() != null && releasePoint.getStackWidth().compareTo(BigDecimal.ZERO) == 1
                        && releasePoint.getStackLength() != null && releasePoint.getStackLength().compareTo(BigDecimal.ZERO) == 1)) {

                        // Determine stack area based on stack dimensions
                        BigDecimal inputDiameter = null;
                        boolean isDiameter = releasePoint.getStackDiameter() != null && releasePoint.getStackDiameter().compareTo(BigDecimal.ZERO) == 1;
                        String formula = isDiameter ? DIAMETER_FORMULA : LENGTH_WIDTH_FORMULA;
                        String dimension = isDiameter ? STACK_DIAMETER : STACK_LENGTH_WIDTH;

                        if (isDiameter) {
                            inputDiameter = releasePoint.getStackDiameter();
                        }
                        BigDecimal calcArea = isDiameter ? (BigDecimal.valueOf(Math.PI)).multiply((((inputDiameter.divide(BigDecimal.valueOf(2))).pow(2)))) : (releasePoint.getStackWidth().multiply(releasePoint.getStackLength()));

                        // Check exit gas velocity if exit gas flow rate and stack diameter are submitted.
                        if (releasePoint.getExitGasFlowRate() != null && releasePoint.getExitGasFlowRate().compareTo(BigDecimal.ZERO) == 1) {

                            BigDecimal minVelocity = BigDecimal.valueOf(0.001);
                            BigDecimal maxVelocity = BigDecimal.valueOf(1500.0);
                            BigDecimal calcVelocity = BigDecimal.ZERO;
                            BigDecimal inputFlowRate = releasePoint.getExitGasFlowRate();

                            String uom = VELOCITY_UOM_FPS;

                            if (calcArea.compareTo(BigDecimal.ZERO) == 1 && inputFlowRate.compareTo(BigDecimal.ZERO) == 1) {
                                calcVelocity = (inputFlowRate.divide(calcArea,3,BigDecimal.ROUND_HALF_UP));
                            }

                            if (releasePoint.getExitGasFlowUomCode() != null && !FLOW_RATE_UOM_ACFS.contentEquals(releasePoint.getExitGasFlowUomCode().getCode())) {
                                minVelocity = BigDecimal.valueOf(0.060);
                                maxVelocity = BigDecimal.valueOf(90000);
                                uom = VELOCITY_UOM_FPM;
                            }

                            if (calcVelocity.compareTo(maxVelocity.setScale(3, RoundingMode.HALF_UP)) == 1 || calcVelocity.compareTo(minVelocity.setScale(3, RoundingMode.HALF_UP)) == -1) {

                                result = false;
                                context.addFederalError(
                                    ValidationField.RP_GAS_VELOCITY.value(),
                                    "releasePoint.exitGasVelocity.range",
                                    createValidationDetails(releasePoint),
                                    calcVelocity.toString(),
                                    uom,
                                    dimension,
                                    minVelocity,
                                    maxVelocity,
                                    uom,
                                    formula);
                            }
                        }

                        // Check exit gas flow rate if exit gas flow rate, exit gas velocity, and stack diameter are submitted.
                        if ((releasePoint.getExitGasVelocity() != null && releasePoint.getExitGasVelocity().compareTo(BigDecimal.ZERO) == 1)
                            && (releasePoint.getExitGasFlowRate() != null && releasePoint.getExitGasFlowRate().compareTo(BigDecimal.ZERO) == 1)) {

                            BigDecimal inputFlowRate = releasePoint.getExitGasFlowRate();
                            BigDecimal inputVelocity = releasePoint.getExitGasVelocity();

                            BigDecimal calcFlowRate = inputVelocity.multiply(calcArea);
                            BigDecimal lowerLimitFlowRate = BigDecimal.ZERO;
                            BigDecimal upperLimitFlowRate = BigDecimal.ZERO;
                            String uom = FLOW_RATE_UOM_ACFS;

                            if (releasePoint.getExitGasVelocityUomCode() != null && !VELOCITY_UOM_FPS.contentEquals(releasePoint.getExitGasVelocityUomCode().getCode())) {
                                uom = FLOW_RATE_UOM_ACFM;
                            }

                            // set actual flow rate UoM to compare to computed flow rate
                            if (releasePoint.getExitGasFlowUomCode() != null) {
                                if (!FLOW_RATE_UOM_ACFS.contentEquals(releasePoint.getExitGasFlowUomCode().getCode()) && FLOW_RATE_UOM_ACFS.contentEquals(uom)) {
                                    inputFlowRate = releasePoint.getExitGasFlowRate().divide(BigDecimal.valueOf(60), 8, RoundingMode.HALF_UP);
                                } else if (!FLOW_RATE_UOM_ACFM.contentEquals(releasePoint.getExitGasFlowUomCode().getCode()) && FLOW_RATE_UOM_ACFM.contentEquals(uom)) {
                                    inputFlowRate = releasePoint.getExitGasFlowRate().multiply(BigDecimal.valueOf(60));
                                }
                            }

                            lowerLimitFlowRate = BigDecimal.valueOf(0.95).multiply(calcFlowRate).setScale(8, RoundingMode.HALF_UP);
                            upperLimitFlowRate = BigDecimal.valueOf(1.05).multiply(calcFlowRate).setScale(8, RoundingMode.HALF_UP);

                            if (!((inputFlowRate.setScale(8, RoundingMode.HALF_UP)).equals(BigDecimal.valueOf(0.00000001).setScale(8, RoundingMode.HALF_UP)))
                                && (((inputFlowRate.setScale(8, RoundingMode.HALF_UP)).compareTo(upperLimitFlowRate) == 1)
                                || ((inputFlowRate.setScale(8, RoundingMode.HALF_UP)).compareTo(lowerLimitFlowRate) == -1))) {

                                result = false;
                                context.addFederalError(
                                    ValidationField.RP_GAS_FLOW.value(),
                                    "releasePoint.exitGasFlowRate.range",
                                    createValidationDetails(releasePoint),
                                    calcFlowRate.setScale(8, RoundingMode.HALF_UP).toString(),
                                    uom,
                                    dimension,
                                    formula);
                            }
                        }

                    }
                }

                // Stack dimensions must be in FT
                if (!validateUomFT(validatorContext, releasePoint, releasePoint.getStackHeight(), releasePoint.getStackHeightUomCode(), "Stack Height")) {
                    result = false;
                }

                if (!validateUomFT(validatorContext, releasePoint, releasePoint.getStackLength(), releasePoint.getStackLengthUomCode(), "Stack Length")) {
                    result = false;
                }

                if (!validateUomFT(validatorContext, releasePoint, releasePoint.getStackWidth(), releasePoint.getStackWidthUomCode(), "Stack Width")) {
                    result = false;
                }

                if (!validateUomFT(validatorContext, releasePoint, releasePoint.getStackDiameter(), releasePoint.getStackDiameterUomCode(), "Stack Diameter")) {
                    result = false;
                }
            }

            // FUGITIVE RELEASE POINT CHECKS
            if (ConstantUtils.FUGITIVE_RELEASE_POINT_CATEGORY.equals(releasePoint.getTypeCode().getCategory())) {

                // Fugitive Height Range
                if (releasePoint.getFugitiveHeight() != null
                    && (releasePoint.getFugitiveHeight() < 1 || releasePoint.getFugitiveHeight() > 500)) {

                    result = false;
                    context.addFederalError(
                        ValidationField.RP_FUGITIVE.value(),
                        "releasePoint.fugitive.heightRange",
                        createValidationDetails(releasePoint));
                }

                if ((ConstantUtils.FUGITIVE_RELEASE_PT_2D_TYPE.equals(releasePoint.getTypeCode().getCode())
                    || ConstantUtils.FUGITIVE_RELEASE_PT_3D_TYPE.equals(releasePoint.getTypeCode().getCode()))) {

                    if (releasePoint.getFugitiveHeight() == null) {
                        result = false;
                        context.addFederalError(
                            ValidationField.RP_FUGITIVE.value(),
                            "releasePoint.fugitive.height.required",
                            createValidationDetails(releasePoint),
                            releasePoint.getTypeCode().getDescription());
                    }

                    if (releasePoint.getFugitiveWidth() == null) {
                        result = false;
                        context.addFederalError(
                            ValidationField.RP_FUGITIVE.value(),
                            "releasePoint.fugitive.width.required",
                            createValidationDetails(releasePoint),
                            releasePoint.getTypeCode().getDescription());
                    }

                    if (releasePoint.getLatitude() == null ) {
                        result = false;
                        context.addFederalError(
                            ValidationField.RP_FUGITIVE.value(),
                            "releasePoint.fugitive.latitude.required",
                            createValidationDetails(releasePoint),
                            latLabel,
                            releasePoint.getTypeCode().getDescription());
                    }

                    if (releasePoint.getLongitude() == null ) {
                        result = false;
                        context.addFederalError(
                            ValidationField.RP_FUGITIVE.value(),
                            "releasePoint.fugitive.longitude.required",
                            createValidationDetails(releasePoint),
                            longLabel,
                            releasePoint.getTypeCode().getDescription());
                    }
                }

                if (ConstantUtils.FUGITIVE_RELEASE_PT_2D_TYPE.equals(releasePoint.getTypeCode().getCode())) {

                    if (releasePoint.getFugitiveMidPt2Latitude() == null ) {
                        result = false;
                        context.addFederalError(
                            ValidationField.RP_FUGITIVE.value(),
                            "releasePoint.fugitive.latitude.required",
                            createValidationDetails(releasePoint),
                            LATITUDE_2_2D,
                            releasePoint.getTypeCode().getDescription());
                    }

                    if (releasePoint.getFugitiveMidPt2Longitude() == null ) {
                        result = false;
                        context.addFederalError(
                            ValidationField.RP_FUGITIVE.value(),
                            "releasePoint.fugitive.longitude.required",
                            createValidationDetails(releasePoint),
                            LONGITUDE_2_2D,
                            releasePoint.getTypeCode().getDescription());
                    }
                }

                // Fugitive Length Range
                if (releasePoint.getFugitiveLength() != null
                    && (releasePoint.getFugitiveLength().compareTo(BigDecimal.ONE) == -1 || releasePoint.getFugitiveLength().compareTo(BigDecimal.valueOf(10000)) == 1)) {

                    result = false;
                    context.addFederalError(
                        ValidationField.RP_FUGITIVE.value(),
                        "releasePoint.fugitive.lengthRange",
                        createValidationDetails(releasePoint));
                }

                // Fugitive Width Range
                if (releasePoint.getFugitiveWidth() != null
                    && (releasePoint.getFugitiveWidth().compareTo(BigDecimal.ONE) == -1 || releasePoint.getFugitiveWidth().compareTo(BigDecimal.valueOf(10000)) == 1)) {

                    result = false;
                    context.addFederalError(
                        ValidationField.RP_FUGITIVE.value(),
                        "releasePoint.fugitive.widthRange",
                        createValidationDetails(releasePoint));
                }

                // Fugitive Angle Range
                if (releasePoint.getFugitiveAngle() != null
                    && (releasePoint.getFugitiveAngle() < 0 || releasePoint.getFugitiveAngle() > 89)) {

                    result = false;
                    context.addFederalError(
                        ValidationField.RP_FUGITIVE.value(),
                        "releasePoint.fugitive.angleRange",
                        createValidationDetails(releasePoint));
                }

                // Fugitive Dimensions must be in FT
                if (!validateUomFT(validatorContext, releasePoint, releasePoint.getFugitiveLength(), releasePoint.getFugitiveLengthUomCode(), "Fugitive Length")) {
                    result = false;
                }

                if (!validateUomFT(validatorContext, releasePoint, releasePoint.getFugitiveWidth(), releasePoint.getFugitiveWidthUomCode(), "Fugitive Width")) {
                    result = false;
                }

                if (!validateUomFT_long(validatorContext, releasePoint, releasePoint.getFugitiveHeight(), releasePoint.getFugitiveHeightUomCode(), "Fugitive Height")) {
                    result = false;
                }
            }

            if ((releasePoint.getExitGasFlowRate() != null && releasePoint.getExitGasFlowUomCode() == null) ||
                (releasePoint.getExitGasFlowRate() == null && releasePoint.getExitGasFlowUomCode() != null)) {

                // Exit Gas Flow Rate and Exit Gas Flow Rate UoM must be reported together.
                result = false;
                context.addFederalError(
                    ValidationField.RP_GAS_FLOW.value(),
                    "releasePoint.exitGasFlowRate.uom.required",
                    createValidationDetails(releasePoint));

            } else if (releasePoint.getExitGasFlowUomCode() != null && (
                !FLOW_RATE_UOM_ACFS.contentEquals(releasePoint.getExitGasFlowUomCode().getCode()) &&
                    !FLOW_RATE_UOM_ACFM.contentEquals(releasePoint.getExitGasFlowUomCode().getCode()))) {

                // Exit Gas Flow Rate UoM must be in ACFS or ACFM.
                result = false;
                context.addFederalError(
                    ValidationField.RP_GAS_FLOW.value(),
                    "releasePoint.exitGasFlowRate.requiredUom",
                    createValidationDetails(releasePoint));
            }

            // Exit Gas Flow Rate Ranges
            if (releasePoint.getExitGasFlowUomCode() != null && releasePoint.getExitGasFlowRate() != null) {
                if (ConstantUtils.FUGITIVE_RELEASE_POINT_CATEGORY.equals(releasePoint.getTypeCode().getCategory())) {
                    if (FLOW_RATE_UOM_ACFS.contentEquals(releasePoint.getExitGasFlowUomCode().getCode()) &&
                        (releasePoint.getExitGasFlowRate().compareTo(BigDecimal.ZERO) == -1 || releasePoint.getExitGasFlowRate().compareTo(BigDecimal.valueOf(200000)) == 1)) {

                        result = false;
                        context.addFederalError(
                            ValidationField.RP_GAS_FLOW.value(),
                            "releasePoint.exitGasFlowRate.fugitiveACFS.range",
                            createValidationDetails(releasePoint));

                    } else if (!FLOW_RATE_UOM_ACFS.contentEquals(releasePoint.getExitGasFlowUomCode().getCode()) &&
                        (releasePoint.getExitGasFlowRate().compareTo(BigDecimal.ZERO) == -1 || releasePoint.getExitGasFlowRate().compareTo(BigDecimal.valueOf(12000000)) == 1)) {
                        result = false;
                        context.addFederalError(
                            ValidationField.RP_GAS_FLOW.value(),
                            "releasePoint.exitGasFlowRate.fugitiveACFM.range",
                            createValidationDetails(releasePoint));
                    }
                } else {
                    if (FLOW_RATE_UOM_ACFS.contentEquals(releasePoint.getExitGasFlowUomCode().getCode()) &&
                        (releasePoint.getExitGasFlowRate().compareTo(BigDecimal.valueOf(0.00000001)) == -1  || releasePoint.getExitGasFlowRate().compareTo(BigDecimal.valueOf(200000)) == 1)) {

                        result = false;
                        context.addFederalError(
                            ValidationField.RP_GAS_FLOW.value(),
                            "releasePoint.exitGasFlowRate.stackACFS.range",
                            createValidationDetails(releasePoint));

                    } else if (!FLOW_RATE_UOM_ACFS.contentEquals(releasePoint.getExitGasFlowUomCode().getCode()) &&
                        (releasePoint.getExitGasFlowRate().compareTo(BigDecimal.valueOf(0.00000001)) == -1 || releasePoint.getExitGasFlowRate().compareTo(BigDecimal.valueOf(12000000)) == 1)) {

                        result = false;
                        context.addFederalError(
                            ValidationField.RP_GAS_FLOW.value(),
                            "releasePoint.exitGasFlowRate.stackACFM.range",
                            createValidationDetails(releasePoint));
                    }
                }
            }

            // Exit Gas Velocity Ranges
            if (releasePoint.getExitGasVelocityUomCode() != null && releasePoint.getExitGasVelocity() != null) {
                if (ConstantUtils.FUGITIVE_RELEASE_POINT_CATEGORY.equals(releasePoint.getTypeCode().getCategory())) {
                    if (VELOCITY_UOM_FPS.contentEquals(releasePoint.getExitGasVelocityUomCode().getCode()) &&
                        (releasePoint.getExitGasVelocity().compareTo(BigDecimal.ZERO) == -1 || releasePoint.getExitGasVelocity().compareTo(BigDecimal.valueOf(400)) == 1)) {

                        result = false;
                        context.addFederalError(
                            ValidationField.RP_GAS_VELOCITY.value(),
                            "releasePoint.exitGasVelocity.fugitiveFPS.range",
                            createValidationDetails(releasePoint));

                    } else if (!VELOCITY_UOM_FPS.contentEquals(releasePoint.getExitGasVelocityUomCode().getCode()) &&
                        (releasePoint.getExitGasVelocity().compareTo(BigDecimal.ZERO) == -1 || releasePoint.getExitGasVelocity().compareTo(BigDecimal.valueOf(24000)) == 1)) {

                        result = false;
                        context.addFederalError(
                            ValidationField.RP_GAS_VELOCITY.value(),
                            "releasePoint.exitGasVelocity.fugitiveFPM.range",
                            createValidationDetails(releasePoint));
                    }
                } else {
                    if (VELOCITY_UOM_FPS.contentEquals(releasePoint.getExitGasVelocityUomCode().getCode()) &&
                        (releasePoint.getExitGasVelocity().compareTo(BigDecimal.valueOf(0.001)) == -1 || releasePoint.getExitGasVelocity().compareTo(BigDecimal.valueOf(1500)) == 1)) {

                        result = false;
                        context.addFederalError(
                            ValidationField.RP_GAS_VELOCITY.value(),
                            "releasePoint.exitGasVelocity.stackFPS.range",
                            createValidationDetails(releasePoint));

                    } else if (!VELOCITY_UOM_FPS.contentEquals(releasePoint.getExitGasVelocityUomCode().getCode()) &&
                        (releasePoint.getExitGasVelocity().compareTo(BigDecimal.valueOf(0.060)) == -1 || releasePoint.getExitGasVelocity().compareTo(BigDecimal.valueOf(90000)) == 1)) {

                        result = false;
                        context.addFederalError(
                            ValidationField.RP_GAS_VELOCITY.value(),
                            "releasePoint.exitGasVelocity.stackFPM.range",
                            createValidationDetails(releasePoint));
                    }
                }
            }

            if ((releasePoint.getExitGasVelocity() != null && releasePoint.getExitGasVelocityUomCode() == null) ||
                (releasePoint.getExitGasVelocity() == null && releasePoint.getExitGasVelocityUomCode() != null)) {

                // Exit Gas Velocity and Exit Gas Velocity UoM must be reported together.
                result = false;
                context.addFederalError(
                    ValidationField.RP_GAS_VELOCITY.value(),
                    "releasePoint.exitGasVelocity.uom.required",
                    createValidationDetails(releasePoint));

            } else if (releasePoint.getExitGasVelocityUomCode() != null && (
                !VELOCITY_UOM_FPS.contentEquals(releasePoint.getExitGasVelocityUomCode().getCode()) &&
                    !VELOCITY_UOM_FPM.contentEquals(releasePoint.getExitGasVelocityUomCode().getCode()))) {

                // Exit Gas Velocity UoM must be in FPM or FPS.
                result = false;
                context.addFederalError(
                    ValidationField.RP_GAS_VELOCITY.value(),
                    "releasePoint.exitGasVelocity.requiredUom",
                    createValidationDetails(releasePoint));
            }

            // Fence Line Distance Range
            if (releasePoint.getFenceLineDistance() != null
                && (releasePoint.getFenceLineDistance() < 0 || releasePoint.getFenceLineDistance() > 99999)) {

                result = false;
                context.addFederalError(
                    ValidationField.RP_FENCELINE.value(),
                    "releasePoint.fenceLine.range",
                    createValidationDetails(releasePoint));
            }

            // Fence Line Distance must be in FT
            if (!validateUomFT_long(validatorContext, releasePoint, releasePoint.getFenceLineDistance(), releasePoint.getFenceLineUomCode(), "Fence Line Distance")) {
                result = false;
            }

            // Latitude/Longitude warning, must have both or value will not be submitted
            if ((releasePoint.getLatitude() == null && releasePoint.getLongitude() != null)
                || (releasePoint.getLatitude() != null && releasePoint.getLongitude() == null)) {

                result = false;
                context.addFederalWarning(
                    ValidationField.RP_COORDINATE.value(),
                    "releasePoint.coordinate.warning",
                    createValidationDetails(releasePoint),
                    latLabel,
                    longLabel);
            }

            if ((releasePoint.getFugitiveMidPt2Latitude() != null && releasePoint.getFugitiveMidPt2Longitude() == null)
                || (releasePoint.getFugitiveMidPt2Latitude() == null && releasePoint.getFugitiveMidPt2Longitude() != null)) {

                result = false;
                context.addFederalWarning(
                    ValidationField.RP_COORDINATE.value(),
                    "releasePoint.coordinate.warning",
                    createValidationDetails(releasePoint),
                    LATITUDE_2_2D,
                    LONGITUDE_2_2D);
            }

            // Latitude/Longitude Tolerance Check
            if (releasePoint.getLatitude() != null && releasePoint.getLongitude() != null) {
                if (!validateCoordinateTolerance(validatorContext, releasePoint, releasePoint.getLatitude(), facilitySiteLat, latLabel, "latitude")) {
                    result = false;
                }

                if (!validateCoordinateTolerance(validatorContext, releasePoint, releasePoint.getLongitude(), facilitySiteLong, longLabel, "longitude")) {
                    result = false;
                }
            }

            if (releasePoint.getFugitiveMidPt2Latitude() != null && releasePoint.getFugitiveMidPt2Longitude() != null) {
                if (!validateCoordinateTolerance(validatorContext, releasePoint, releasePoint.getFugitiveMidPt2Latitude(), facilitySiteLat, LATITUDE_2_2D, "latitude")) {
                    result = false;
                }

                if (!validateCoordinateTolerance(validatorContext, releasePoint, releasePoint.getFugitiveMidPt2Longitude(), facilitySiteLong, LONGITUDE_2_2D, "longitude")) {
                    result = false;
                }
            }

            if (releasePoint.getTypeCode() != null && releasePoint.getTypeCode().getLastInventoryYear() != null
                && releasePoint.getTypeCode().getLastInventoryYear() < releasePoint.getFacilitySite().getEmissionsReport().getYear()) {

                result = false;
                context.addFederalError(
                    ValidationField.RP_TYPE_CODE.value(),
                    "releasePoint.releasePointTypeCode.legacy",
                    createValidationDetails(releasePoint),
                    releasePoint.getTypeCode().getDescription());

            }

            if (releasePoint.getReleasePointAppts() == null || releasePoint.getReleasePointAppts().size() < 1) {
                result = false;
                context.addFederalError(
                    ValidationField.RP.value(),
                    "releasePoint.unassociated",
                    createValidationDetails(releasePoint));
            }

            result = checkIfReleasePointOnlyContainsNonOperatingEmissionsProcesses(releasePoint, context, result);
        }

        // CHECKS FOR ALL RELEASE POINT TYPES

        // If release point operation status is not operating, status year is required
        if (!ConstantUtils.STATUS_OPERATING.contentEquals(releasePoint.getOperatingStatusCode().getCode()) && releasePoint.getStatusYear() == null) {

            result = false;
            context.addFederalError(
                ValidationField.RP_STATUS_CODE.value(), "releasePoint.statusTypeCode.required",
                createValidationDetails(releasePoint));
        }

        // Warning if release point operation status is permanently shutdown release point will not be copied forward
        if (ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(releasePoint.getOperatingStatusCode().getCode())) {

            result = false;
            context.addFederalWarning(
                ValidationField.RP_STATUS_CODE.value(), "releasePoint.statusTypeCode.psNotCopied",
                createValidationDetails(releasePoint));
        }

        // Status year must be between 1900 and the report year
        if (releasePoint.getStatusYear() != null && (releasePoint.getStatusYear() < 1900 || releasePoint.getStatusYear() > releasePoint.getFacilitySite().getEmissionsReport().getYear())) {

            result = false;
            context.addFederalError(
                ValidationField.RP_STATUS_YEAR.value(), "releasePoint.statusYear.range",
                createValidationDetails(releasePoint),
                releasePoint.getFacilitySite().getEmissionsReport().getYear().toString());
        }

        if (releasePoint != null && releasePoint.getFacilitySite() != null && releasePoint.getFacilitySite().getReleasePoints() != null) {
            // Release Point Identifier must be unique within the facility.
            Map<Object, List<ReleasePoint>> rpMap = releasePoint.getFacilitySite().getReleasePoints().stream()
                .filter(rp -> (rp.getReleasePointIdentifier() != null))
                .collect(Collectors.groupingBy(frp -> frp.getReleasePointIdentifier().trim().toLowerCase()));

            for (List<ReleasePoint> rpList : rpMap.values()) {
                if (rpList.size() > 1) {
                    String id = rpList.get(0).getReleasePointIdentifier().trim().toLowerCase();
                    String rPId = releasePoint.getReleasePointIdentifier().trim().toLowerCase();

                    if (id.contentEquals(rPId)) {
                        result = false;
                        context.addFederalError(
                            ValidationField.RP_IDENTIFIER.value(),
                            "releasePoint.releasePointIdentifier.duplicate",
                            createValidationDetails(releasePoint));

                    }
                }
            }
        }

        // check if previous report exists then check if this rp exists in that report
        EmissionsReport currentReport = releasePoint.getFacilitySite().getEmissionsReport();

        List<EmissionsReport> erList = reportRepo.findByMasterFacilityRecordId(currentReport.getMasterFacilityRecord().getId()).stream()
            .filter(var -> (var.getYear() != null && var.getYear() < currentReport.getYear()))
            .sorted(Comparator.comparing(EmissionsReport::getYear))
            .collect(Collectors.toList());

        boolean pyRpExists = false;

        if (!erList.isEmpty()) {
            Short previousReportYr = erList.get(erList.size()-1).getYear();

            List<ReleasePoint> previousRps = rpRepo.retrieveByIdentifierFacilityYear(
                releasePoint.getReleasePointIdentifier(),
                currentReport.getMasterFacilityRecord().getId(),
                previousReportYr);

            if (!previousRps.isEmpty()) {
                pyRpExists = true;
            }
        }

        if (!pyRpExists) {

            if (!ConstantUtils.STATUS_OPERATING.contentEquals(releasePoint.getOperatingStatusCode().getCode())) {
                // release point is new, but is PS/TS
                result = false;
                context.addFederalError(
                    ValidationField.RP_STATUS_CODE.value(),
                    "releasePoint.statusTypeCode.newShutdown",
                    createValidationDetails(releasePoint));
            }
            // only throw inventory change if reported in prior report
            else if (!erList.isEmpty()){
                result = false;
                context.addFacilityInventoryChange(
                    ValidationField.RP.value(),
                    "releasePoint.new",
                    createValidationDetails(releasePoint));
            }
        }

        // if release point was PS in previous year report and is not PS in this report
        if (releasePoint.getPreviousYearOperatingStatusCode() != null) {
            if (ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(releasePoint.getPreviousYearOperatingStatusCode().getCode()) &&
                !ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.contentEquals(releasePoint.getOperatingStatusCode().getCode())) {

                result = false;
                context.addFederalError(
                    ValidationField.RP_STATUS_CODE.value(),
                    "releasePoint.statusTypeCode.psPreviousYear",
                    createValidationDetails(releasePoint));
            }
        }

        return result;
    }


    public boolean validateUomFT(ValidatorContext validatorContext, ReleasePoint releasePoint, BigDecimal measure, UnitMeasureCode uom, String uomField) {

        CefValidatorContext context = getCefValidatorContext(validatorContext);
        boolean result = true;

        if ((uom != null && !UOM_FT.contentEquals(uom.getCode())) || (uom == null && measure != null)) {

            result = false;
            context.addFederalError(
                ValidationField.RP_UOM_FT.value(),
                "releasePoint.uom.ft",
                createValidationDetails(releasePoint),
                uomField);
        }

        return result;
    }

    public boolean validateUomFT_long(ValidatorContext validatorContext, ReleasePoint releasePoint, Long measure, UnitMeasureCode uom, String uomField) {
        if (measure != null) {
            return validateUomFT(validatorContext, releasePoint, BigDecimal.valueOf(measure), uom, uomField);
        } else {
            return validateUomFT(validatorContext, releasePoint, null, uom, uomField);
        }
    }

    public boolean validateCoordinateTolerance(ValidatorContext validatorContext, ReleasePoint releasePoint, BigDecimal rpCoordinate, BigDecimal facilityCoordinate, String rpLatLongField, String facilityLatLongField) {

        CefValidatorContext context = getCefValidatorContext(validatorContext);
        boolean result = true;
        BigDecimal maxRange;
        BigDecimal minRange;
        BigDecimal releasePointCoordinate = null;
        BigDecimal facilityTolerance = DEFAULT_TOLERANCE;

        if (!rpCoordinate.equals(null)) {
            releasePointCoordinate = rpCoordinate.setScale(6, RoundingMode.HALF_UP);
        }

        facilityTolerance = releasePoint.getFacilitySite().getEmissionsReport().getMasterFacilityRecord().getCoordinateTolerance().setScale(6, RoundingMode.HALF_UP);


        maxRange = facilityCoordinate.add(facilityTolerance).setScale(6, RoundingMode.HALF_UP);
        minRange = facilityCoordinate.subtract(facilityTolerance).setScale(6, RoundingMode.HALF_UP);

        if (releasePointCoordinate == null || (releasePointCoordinate.compareTo(maxRange) == 1) || (releasePointCoordinate.compareTo(minRange) == -1)) {

            result = false;
            context.addFederalError(
                ValidationField.RP_COORDINATE.value(),
                "releasePoint.coordinate.tolerance.facilityRange",
                createValidationDetails(releasePoint),
                rpLatLongField,
                Double.valueOf(facilityTolerance.toString()).toString(),
                facilityLatLongField,
                facilityCoordinate.setScale(6, RoundingMode.HALF_UP).toString());
        }

        return result;
    }

    private ValidationDetailDto createValidationDetails(ReleasePoint source) {

        String description = MessageFormat.format("Release Point: {0}", source.getReleasePointIdentifier());

        ValidationDetailDto dto = new ValidationDetailDto(source.getId(), source.getReleasePointIdentifier(), EntityType.RELEASE_POINT, description);
        return dto;
    }
}
