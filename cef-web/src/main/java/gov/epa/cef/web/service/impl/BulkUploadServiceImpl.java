/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.service.*;
import gov.epa.cef.web.service.dto.PointSourceSccCodeDto;
import gov.epa.cef.web.service.dto.json.*;
import gov.epa.cef.web.service.dto.json.shared.FacilityNAICSJsonDto;
import gov.epa.cef.web.service.mapper.json.JsonUploadMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import gov.epa.cef.web.client.api.ExcelParserClient;
import gov.epa.cef.web.client.api.ExcelParserResponse;
import gov.epa.cef.web.config.SLTBaseConfig;
import gov.epa.cef.web.exception.BulkReportValidationException;
import gov.epa.cef.web.exception.CalculationException;
import gov.epa.cef.web.exception.NotExistException;
import gov.epa.cef.web.repository.AircraftEngineTypeCodeRepository;
import gov.epa.cef.web.repository.CalculationMaterialCodeRepository;
import gov.epa.cef.web.repository.CalculationMethodCodeRepository;
import gov.epa.cef.web.repository.CalculationParameterTypeCodeRepository;
import gov.epa.cef.web.repository.ContactTypeCodeRepository;
import gov.epa.cef.web.repository.ControlMeasureCodeRepository;
import gov.epa.cef.web.repository.ControlRepository;
import gov.epa.cef.web.repository.EmissionFormulaVariableCodeRepository;
import gov.epa.cef.web.repository.EmissionsOperatingTypeCodeRepository;
import gov.epa.cef.web.repository.EmissionsProcessRepository;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.EmissionsUnitRepository;
import gov.epa.cef.web.repository.FacilityCategoryCodeRepository;
import gov.epa.cef.web.repository.FacilitySiteRepository;
import gov.epa.cef.web.repository.FacilitySourceTypeCodeRepository;
import gov.epa.cef.web.repository.FipsCountyRepository;
import gov.epa.cef.web.repository.FipsStateCodeRepository;
import gov.epa.cef.web.repository.MasterFacilityRecordRepository;
import gov.epa.cef.web.repository.NaicsCodeRepository;
import gov.epa.cef.web.repository.OperatingStatusCodeRepository;
import gov.epa.cef.web.repository.PointSourceSccCodeRepository;
import gov.epa.cef.web.repository.PollutantRepository;
import gov.epa.cef.web.repository.ProgramSystemCodeRepository;
import gov.epa.cef.web.repository.ReleasePointRepository;
import gov.epa.cef.web.repository.ReleasePointTypeCodeRepository;
import gov.epa.cef.web.repository.ReportChangeRepository;
import gov.epa.cef.web.repository.ReportingPeriodCodeRepository;
import gov.epa.cef.web.repository.TribalCodeRepository;
import gov.epa.cef.web.repository.UnitMeasureCodeRepository;
import gov.epa.cef.web.repository.UnitTypeCodeRepository;
import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.service.dto.EisSubmissionStatus;
import gov.epa.cef.web.service.dto.EmissionsReportDto;
import gov.epa.cef.web.service.dto.EmissionsReportStarterDto;
import gov.epa.cef.web.service.dto.bulkUpload.BlankToNullModule;
import gov.epa.cef.web.service.dto.bulkUpload.ControlAssignmentBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ControlBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ControlPathBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ControlPathPollutantBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ControlPollutantBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionFormulaVariableBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionsProcessBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionsReportBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.EmissionsUnitBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.FacilityNAICSBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.FacilitySiteBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.FacilitySiteContactBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.OperatingDetailBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ReleasePointApptBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ReleasePointBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.ReportingPeriodBulkUploadDto;
import gov.epa.cef.web.service.dto.bulkUpload.WorksheetError;
import gov.epa.cef.web.service.mapper.BulkUploadMapper;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.util.CalculationUtils;
import gov.epa.cef.web.util.ConstantUtils;
import gov.epa.cef.web.util.MassUomConversion;
import gov.epa.cef.web.util.ReportCreationContext;
import gov.epa.cef.web.util.SLTConfigHelper;
import gov.epa.cef.web.util.TempFile;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class BulkUploadServiceImpl implements BulkUploadService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AircraftEngineTypeCodeRepository aircraftEngineRepo;

    @Autowired
    private CalculationMaterialCodeRepository calcMaterialCodeRepo;

    @Autowired
    private CalculationMethodCodeRepository calcMethodCodeRepo;

    @Autowired
    private CalculationParameterTypeCodeRepository calcParamTypeCodeRepo;

    @Autowired
    private ContactTypeCodeRepository contactTypeRepo;

    @Autowired
    private ControlMeasureCodeRepository controlMeasureCodeRepo;

    @Autowired
    private FipsCountyRepository countyRepo;

    @Autowired
    private EmissionFormulaVariableCodeRepository emissionFormulaVariableCodeRepo;

    @Autowired
    private EmissionsOperatingTypeCodeRepository emissionsOperatingTypeCodeRepo;

    @Autowired
    private EmissionsReportService emissionsReportService;

    @Autowired
    private EmissionService emissionService;

    @Autowired
    private ReportingPeriodService rpService;

    @Autowired
    private ExcelParserClient excelParserClient;

    @Autowired
    private FacilityCategoryCodeRepository facilityCategoryRepo;

    @Autowired
    private FacilitySourceTypeCodeRepository facilitySourceTypeRepo;

    @Autowired
    private MasterFacilityRecordRepository mfrRepo;

    @Autowired
    private NaicsCodeRepository naicsCodeRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OperatingStatusCodeRepository operatingStatusRepo;

    @Autowired
    private PollutantRepository pollutantRepo;

    @Autowired
    private ProgramSystemCodeRepository programSystemCodeRepo;

    @Autowired
    private ReleasePointTypeCodeRepository releasePointTypeRepo;

    @Autowired
    private ReportingPeriodCodeRepository reportingPeriodCodeRepo;

    @Autowired
    private FipsStateCodeRepository stateCodeRepo;

    @Autowired
    private TribalCodeRepository tribalCodeRepo;

    @Autowired
    private UnitMeasureCodeRepository unitMeasureCodeRepo;

    @Autowired
    private UnitTypeCodeRepository unitTypeRepo;

    @Autowired
    private BulkUploadMapper uploadMapper;

    @Autowired
    private JsonUploadMapper caersJsonMapper;

    @Autowired
    private BulkReportValidator validator;

    @Autowired
    private FacilitySiteRepository facilitySiteRepo;

    @Autowired
    private SLTConfigHelper sltConfigHelper;

    @Autowired
    private EmissionsReportRepository emissionsReportRepo;

    @Autowired
    private ReleasePointRepository releasePointRepo;

    @Autowired
    private EmissionsUnitRepository emissionsUnitRepo;

    @Autowired
    private ControlRepository controlRepo;

    @Autowired
    private EmissionsProcessRepository emissionsProcessRepo;

    @Autowired
    private ReportChangeRepository reportChangeRepo;

    @Autowired
	private PointSourceSccCodeRepository sccRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private LookupService lookupService;

    @Override
    public Function<JsonNode, EmissionsReportBulkUploadDto> parseJsonNode(boolean failUnknownProperties) {

        return new JsonNodeToBulkUploadDto(this.objectMapper, failUnknownProperties);
    }

    public Function<JsonNode, EmissionsReportJsonDto> parseCaersJsonNode(boolean failUnknownProperties) {

        return new JsonNodeToCaersJsonDto(this.objectMapper, failUnknownProperties);
    }

    /**
     * Save the emissions report to the database.
     *
     * @param bulkEmissionsReport
     * @return
     */
    @Override
    public EmissionsReportDto saveBulkEmissionsReport(EmissionsReportBulkUploadDto bulkEmissionsReport) {

        ReportCreationContext context = new ReportCreationContext(ConstantUtils.REPORT_CREATION_CHANGE_BUNDLE);

        EmissionsReport emissionsReport = toEmissionsReport().apply(bulkEmissionsReport, context);

        // if a previous report already exists, then update that existing report with the data from this uploaded file
        // and reset the validation, report, and CROMERR status for the report
        Optional<EmissionsReport> previousReport = this.emissionsReportService.retrieveByMasterFacilityRecordIdAndYear(bulkEmissionsReport.getMasterFacilityRecordId(),
                                                                                                                       bulkEmissionsReport.getYear());
        SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(bulkEmissionsReport.getProgramSystemCode());
        Boolean monthlyEnabled = sltConfig.getSltMonthlyFuelReportingEnabled();
        Short monthlyInitialYear = sltConfig.getSltMonthlyFuelReportingInitialYear();

        if (previousReport.isPresent()) {

        	//remove any previous facilitySites from the report (the main report data)
        	//should be only one facility site, but putting in a loop for security
        	List<FacilitySite> oldSites = previousReport.get().getFacilitySites();
        	for (FacilitySite oldSite : oldSites) {
        		facilitySiteRepo.deleteById(oldSite.getId());
        	}

        	reportChangeRepo.deleteAll(previousReport.get().getReportCreationChanges());

        	//add in the new facility site from the uploaded report to the old report
        	EmissionsReport reportToUpdate = previousReport.get();
        	reportToUpdate.getFacilitySites().clear();
        	reportToUpdate.getReportCreationChanges().clear();
        	List<FacilitySite> newFacilitySites = emissionsReport.getFacilitySites();
        	for (FacilitySite newSite : newFacilitySites) {
        		newSite.setEmissionsReport(reportToUpdate);
        		reportToUpdate.getFacilitySites().add(newSite);
        	}

        	//update the report metadata to make sure it's reset since the report has been recreated
        	if (ReportStatus.RETURNED == reportToUpdate.getStatus()) {
        	    reportToUpdate.setStatus(ReportStatus.RETURNED);
        	    reportToUpdate.setReturnedReport(true);
        	} else {
        	    reportToUpdate.setStatus(ReportStatus.IN_PROGRESS);
        	    reportToUpdate.setReturnedReport(false);
        	}
            reportToUpdate.setValidationStatus(ValidationStatus.UNVALIDATED);
            reportToUpdate.setEisLastSubmissionStatus(EisSubmissionStatus.NotStarted);
        	reportToUpdate.setCromerrActivityId(null);
        	reportToUpdate.setCromerrDocumentId(null);

        	if (Boolean.TRUE.equals(monthlyEnabled)) {
        		if (ReportStatus.RETURNED == reportToUpdate.getMidYearSubmissionStatus()
                        && (monthlyInitialYear == null || reportToUpdate.getYear() >= monthlyInitialYear)) {
        			reportToUpdate.setMidYearSubmissionStatus(ReportStatus.RETURNED);
        		} else {
        			reportToUpdate.setMidYearSubmissionStatus(ReportStatus.IN_PROGRESS);
        		}
        	}

        	return this.emissionsReportService.saveAndAuditEmissionsReport(reportToUpdate, ReportAction.UPLOADED, context);
        }
        //otherwise, just add the entire report from the excel upload into the system
        else {
        	if (Boolean.TRUE.equals(monthlyEnabled)
                && (monthlyInitialYear == null || emissionsReport.getYear() >= monthlyInitialYear)) {
        		emissionsReport.setMidYearSubmissionStatus(ReportStatus.IN_PROGRESS);
        	}
        	emissionsReport.setReturnedReport(false);
        	return this.emissionsReportService.saveAndAuditEmissionsReport(emissionsReport, ReportAction.UPLOADED, context);
        }
    }

    public EmissionsReportDto saveBulkWorkbook(EmissionsReportStarterDto metadata, TempFile workbook) {

        EmissionsReportDto result = null;

        ExcelParserResponse response = this.excelParserClient.parseWorkbook(workbook);

        if (response.getResponseCode() == HttpURLConnection.HTTP_OK) {

            logger.trace("ExcelJsonParser Result {}", response.getJson());

            EmissionsReportBulkUploadDto bulkEmissionsReport = parseWorkbookJson(response, metadata);

            this.validator.validate(bulkEmissionsReport);

            try {

                result = saveBulkEmissionsReport(bulkEmissionsReport);

            } catch (Exception e) {

                String msg = e.getMessage()
                    .replaceAll(EmissionsReportBulkUploadDto.class.getPackage().getName().concat("."), "")
                    .replaceAll(EmissionsReport.class.getPackage().getName().concat("."), "");

                WorksheetError violation = WorksheetError.createSystemError(msg);

                throw new BulkReportValidationException(Collections.singletonList(violation));
            }

        } else {

            List<WorksheetError> errors = new ArrayList<>();
            errors.add(WorksheetError.createSystemError("Unable to read workbook."));
            if (response.getJson() != null && response.getJson().hasNonNull("message")) {

                errors.add(WorksheetError.createSystemError(response.getJson().path("message").asText()));
            }

            throw new BulkReportValidationException(errors);
        }

        return result;
    }

    public EmissionsReportDto saveCaersJson(EmissionsReportStarterDto metadata, JsonNode jsonNode) {

        EmissionsReportDto result = null;

        EmissionsReportJsonDto jsonReport = parseCaersJsonNode(false).apply(jsonNode);

        EmissionsReportBulkUploadDto bulkEmissionsReport = this.caersJsonMapper.emissionsReportToDto(jsonReport);

        Long idIncrementor = 0l;

        for (FacilitySiteJsonDto fs: jsonReport.getFacilitySite()) {
            FacilitySiteBulkUploadDto fsDto = this.caersJsonMapper.facilitySiteToDto(fs);
            fsDto.setId(idIncrementor++);

            bulkEmissionsReport.getFacilitySites().add(fsDto);

            for (EmissionsUnitJsonDto eu: fs.getEmissionsUnits()) {
                EmissionsUnitBulkUploadDto euDto = this.caersJsonMapper.emissionsUnitToDto(eu);
                euDto.setId(idIncrementor++);
                euDto.setFacilitySiteId(fsDto.getId());

                bulkEmissionsReport.getEmissionsUnits().add(euDto);

                for (EmissionsProcessJsonDto ep: eu.getEmissionsProcesses()) {
                    EmissionsProcessBulkUploadDto epDto = this.caersJsonMapper.emissionsProcessToDto(ep);
                    epDto.setId(idIncrementor++);
                    epDto.setEmissionsUnitId(euDto.getId());

                    bulkEmissionsReport.getEmissionsProcesses().add(epDto);

                    for (ReportingPeriodJsonDto rp: ep.getReportingPeriods()) {
                        ReportingPeriodBulkUploadDto rpDto = this.caersJsonMapper.reportingPeriodToDto(rp);
                        rpDto.setId(idIncrementor++);
                        rpDto.setEmissionsProcessId(epDto.getId());

                        bulkEmissionsReport.getReportingPeriods().add(rpDto);

                        for (OperatingDetailJsonDto od: rp.getOperatingDetails()) {
                            OperatingDetailBulkUploadDto odDto = this.caersJsonMapper.operatingDetailToDto(od);
                            odDto.setId(idIncrementor++);
                            odDto.setReportingPeriodId(rpDto.getId());

                            bulkEmissionsReport.getOperatingDetails().add(odDto);
                        }

                        for (EmissionJsonDto e: rp.getEmissions()) {
                            EmissionBulkUploadDto eDto = this.caersJsonMapper.emissionToDto(e);
                            eDto.setId(idIncrementor++);
                            eDto.setReportingPeriodId(rpDto.getId());

                            bulkEmissionsReport.getEmissions().add(eDto);
                        }

                    }

                    for (ReleasePointApptJsonDto rpa: ep.getReleasePointApportionment()) {
                        ReleasePointApptBulkUploadDto rpaDto = this.caersJsonMapper.releasePointApptToDto(rpa);
                        rpaDto.setId(idIncrementor++);
                        rpaDto.setEmissionProcessId(epDto.getId());

                        bulkEmissionsReport.getReleasePointAppts().add(rpaDto);
                    }
                }

            }

            for (ReleasePointJsonDto rp: fs.getReleasePoints()) {
                ReleasePointBulkUploadDto rpDto = this.caersJsonMapper.releasePointToDto(rp);
                rpDto.setId(idIncrementor++);
                rpDto.setFacilitySiteId(fsDto.getId());

                bulkEmissionsReport.getReleasePoints().add(rpDto);
            }

            for (ControlPathJsonDto cp: fs.getControlPaths()) {
                ControlPathBulkUploadDto cpDto = this.caersJsonMapper.controlPathToDto(cp);
                cpDto.setId(idIncrementor++);
                cpDto.setFacilitySiteId(fsDto.getId());

                bulkEmissionsReport.getControlPaths().add(cpDto);

                for (ControlPollutantJsonDto cpp: cp.getControlPathPollutants()) {
                    ControlPathPollutantBulkUploadDto cppDto = this.caersJsonMapper.controlPathPollutantToDto(cpp);
                    cppDto.setId(idIncrementor++);
                    cppDto.setControlPathId(cpDto.getId());

                    bulkEmissionsReport.getControlPathPollutants().add(cppDto);
                }

                for (ControlAssignmentJsonDto cpa: cp.getControlPathDefinition()) {
                    ControlAssignmentBulkUploadDto cpaDto = this.caersJsonMapper.controlAssignmentToDto(cpa);
                    cpaDto.setId(idIncrementor++);
                    cpaDto.setControlPathId(cpDto.getId());

                    bulkEmissionsReport.getControlAssignments().add(cpaDto);
                }
            }

            for (ControlJsonDto c: fs.getControls()) {
                ControlBulkUploadDto cDto = this.caersJsonMapper.controlToDto(c);
                cDto.setId(idIncrementor++);
                cDto.setFacilitySiteId(fsDto.getId());

                bulkEmissionsReport.getControls().add(cDto);

                for (ControlPollutantJsonDto cp: c.getControlPollutants()) {
                    ControlPollutantBulkUploadDto cpDto = this.caersJsonMapper.controlPollutantToDto(cp);
                    cpDto.setId(idIncrementor++);
                    cpDto.setControlId(cDto.getId());

                    bulkEmissionsReport.getControlPollutants().add(cpDto);
                }
            }


            bulkEmissionsReport.getReleasePointAppts().stream().forEach(rpa -> {
                if (Strings.emptyToNull(rpa.getReleasePointName()) != null) {
                    Optional<ReleasePointBulkUploadDto> rp = bulkEmissionsReport.getReleasePoints().stream()
                        .filter(item -> rpa.getReleasePointName().equals(item.getReleasePointIdentifier())).findFirst();

                    if (rp.isPresent()) {
                        rpa.setReleasePointId(rp.get().getId());
                    }
                }

            });

            bulkEmissionsReport.getControlAssignments().stream().forEach(cpa -> {

                if (Strings.emptyToNull(cpa.getControlName()) != null) {
                    Optional<ControlBulkUploadDto> c = bulkEmissionsReport.getControls().stream()
                        .filter(item -> cpa.getControlName().equals(item.getIdentifier())).findFirst();

                    if (c.isPresent()) {
                        cpa.setControlId(c.get().getId());
                    }
                }

                if (Strings.emptyToNull(cpa.getChildPathName()) != null) {
                    Optional<ControlPathBulkUploadDto> cp = bulkEmissionsReport.getControlPaths().stream()
                        .filter(item -> cpa.getChildPathName().equals(item.getPathId())).findFirst();

                    if (cp.isPresent()) {
                        cpa.setControlPathChildId(cp.get().getId());
                    }
                }

            });

            for (FacilityNAICSJsonDto naics: fs.getFacilityNAICS()) {
                FacilityNAICSBulkUploadDto naicsDto = this.caersJsonMapper.faciliytNAICSToDto(naics);
                naicsDto.setId(idIncrementor++);
                naicsDto.setFacilitySiteId(fsDto.getId());

                bulkEmissionsReport.getFacilityNAICS().add(naicsDto);
            }

        }

        bulkEmissionsReport.setProgramSystemCode(metadata.getProgramSystemCode());
        bulkEmissionsReport.setMasterFacilityRecordId(metadata.getMasterFacilityRecordId());
        bulkEmissionsReport.setEisProgramId(metadata.getEisProgramId());
        bulkEmissionsReport.setAgencyFacilityIdentifier(metadata.getAgencyFacilityIdentifier());
        bulkEmissionsReport.setYear(metadata.getYear());
        bulkEmissionsReport.setStatus(ReportStatus.IN_PROGRESS.name());
        bulkEmissionsReport.setValidationStatus(ValidationStatus.UNVALIDATED.name());
        bulkEmissionsReport.setEisLastSubmissionStatus(EisSubmissionStatus.NotStarted.name());



        this.validator.validate(bulkEmissionsReport, false);

        try {

            result = saveBulkEmissionsReport(bulkEmissionsReport);

        } catch (Exception e) {

            String msg = e.getMessage()
                .replaceAll(EmissionsReportBulkUploadDto.class.getPackage().getName().concat("."), "")
                .replaceAll(EmissionsReport.class.getPackage().getName().concat("."), "");

            WorksheetError violation = WorksheetError.createSystemError(msg);

            throw new BulkReportValidationException(Collections.singletonList(violation));
        }

        return result;
    }

    public EmissionsReportDto saveBulkJson(EmissionsReportStarterDto metadata, JsonNode jsonNode) {

        EmissionsReportDto result = null;

            EmissionsReportBulkUploadDto bulkEmissionsReport = parseJsonNode(false).andThen(r -> {

                r.setProgramSystemCode(metadata.getProgramSystemCode());
                r.setMasterFacilityRecordId(metadata.getMasterFacilityRecordId());
                r.setEisProgramId(metadata.getEisProgramId());
                r.setAgencyFacilityIdentifier(metadata.getAgencyFacilityIdentifier());
                r.setYear(metadata.getYear());
                r.setStatus(ReportStatus.IN_PROGRESS.name());
                r.setValidationStatus(ValidationStatus.UNVALIDATED.name());
                r.setEisLastSubmissionStatus(EisSubmissionStatus.NotStarted.name());

                return r;

            }).apply(jsonNode);

            this.validator.validate(bulkEmissionsReport, false);

            try {

                result = saveBulkEmissionsReport(bulkEmissionsReport);

            } catch (Exception e) {

                String msg = e.getMessage()
                    .replaceAll(EmissionsReportBulkUploadDto.class.getPackage().getName().concat("."), "")
                    .replaceAll(EmissionsReport.class.getPackage().getName().concat("."), "");

                WorksheetError violation = WorksheetError.createSystemError(msg);

                throw new BulkReportValidationException(Collections.singletonList(violation));
            }

        return result;
    }

    protected BiFunction<EmissionsReportBulkUploadDto, ReportCreationContext, EmissionsReport> toEmissionsReport() {

    	Boolean isAdminUpload = userService.getCurrentUser().getRole().equalsIgnoreCase(AppRole.RoleType.CAERS_ADMIN.roleName());
    	return (bulkEmissionsReport, context) -> {

            Collection<String> warnings = new ArrayList<>();

            EmissionsReport emissionsReport = mapEmissionsReport(bulkEmissionsReport);

            SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(bulkEmissionsReport.getProgramSystemCode());

            for (FacilitySiteBulkUploadDto bulkFacility : bulkEmissionsReport.getFacilitySites()) {
                FacilitySite facility = mapFacility(bulkFacility);

                // compare uploaded facility info to MFR
                if (context != null && !(Objects.equals(facility.getName(), emissionsReport.getMasterFacilityRecord().getName())
                        && Objects.equals(facility.getFacilitySourceTypeCode(), emissionsReport.getMasterFacilityRecord().getFacilitySourceTypeCode())
                        && Objects.equals(facility.getLongitude().stripTrailingZeros(), emissionsReport.getMasterFacilityRecord().getLongitude().stripTrailingZeros())
                        && Objects.equals(facility.getLatitude().stripTrailingZeros(), emissionsReport.getMasterFacilityRecord().getLatitude().stripTrailingZeros())
                        && Objects.equals(facility.getStreetAddress(), emissionsReport.getMasterFacilityRecord().getStreetAddress())
                        && Objects.equals(facility.getCity(), emissionsReport.getMasterFacilityRecord().getCity())
                        && Objects.equals(facility.getStateCode(), emissionsReport.getMasterFacilityRecord().getStateCode())
                        && Objects.equals(facility.getPostalCode(), emissionsReport.getMasterFacilityRecord().getPostalCode())
                        && Objects.equals(facility.getCountyCode(), emissionsReport.getMasterFacilityRecord().getCountyCode()))) {
                    context.addCreationChange(ValidationField.FACILITY_ADDRESS, "facilitysite.address.updated", facility, ReportChangeType.UPDATE);
                }

                facility.setName(emissionsReport.getMasterFacilityRecord().getName());
                facility.setFacilitySourceTypeCode(emissionsReport.getMasterFacilityRecord().getFacilitySourceTypeCode());
                facility.setLongitude(emissionsReport.getMasterFacilityRecord().getLongitude());
                facility.setLatitude(emissionsReport.getMasterFacilityRecord().getLatitude());
                facility.setStreetAddress(emissionsReport.getMasterFacilityRecord().getStreetAddress());
                facility.setCity(emissionsReport.getMasterFacilityRecord().getCity());
                facility.setStateCode(emissionsReport.getMasterFacilityRecord().getStateCode());
                facility.setPostalCode(emissionsReport.getMasterFacilityRecord().getPostalCode());
                facility.setCountyCode(emissionsReport.getMasterFacilityRecord().getCountyCode());

                Preconditions.checkArgument(bulkFacility.getId() != null,
                    "FacilitySite ID can not be null.");

                // Maps for storing the entity referenced by a certain id in the JSON
                Map<Long, ReleasePoint> releasePointMap = new HashMap<>();
                Map<Long, EmissionsProcess> processMap = new HashMap<>();
                Map<Long, ControlPath> controlPathMap = new HashMap<>();
                Map<Long, Control> controlMap = new HashMap<>();

                // Map Facility Contacts
                for (FacilitySiteContactBulkUploadDto bulkFacilityContact : bulkEmissionsReport.getFacilityContacts()) {
                    FacilitySiteContact facilityContact = mapFacilityContact(bulkFacilityContact);

                    if (bulkFacility.getId().equals(bulkFacilityContact.getFacilitySiteId())) {
                        facilityContact.setFacilitySite(facility);
                        facility.getContacts().add(facilityContact);
                    }
                }

                // Map Facility NAICS
                if (Boolean.FALSE.equals(sltConfig.getFacilityNaicsEnabled()) || isAdminUpload) {
	                for (FacilityNAICSBulkUploadDto bulkFacilityNAICS : bulkEmissionsReport.getFacilityNAICS()) {
	                	FacilityNAICSXref facilityNAICS;
	                    facilityNAICS = mapFacilityNAICS(bulkFacilityNAICS);
	                    if (bulkFacility.getId().equals(bulkFacilityNAICS.getFacilitySiteId())) {
	                        facilityNAICS.setFacilitySite(facility);
	                        facility.getFacilityNAICS().add(facilityNAICS);
	                    }
	                }
                } else {

                    if (context != null) {
                        List<FacilityNAICSXref> fsNaics = bulkEmissionsReport.getFacilityNAICS().stream().map(bulk -> mapFacilityNAICS(bulk)).collect(Collectors.toList());
                        List<MasterFacilityNAICSXref> mfrNaics = emissionsReport.getMasterFacilityRecord().getMasterFacilityNAICS();

                        // find NAICS from the facility that aren't on the MFR (will be removed)
                        fsNaics.stream()
                                .filter(fn -> mfrNaics.stream().noneMatch(mn -> mn.getNaicsCode().equals(fn.getNaicsCode())))
                                .forEach(fn -> {
                                    context.addCreationChange(ValidationField.FACILITY_NAICS, "facilitysite.naics.deleted", facility,
                                            ReportChangeType.DELETE, fn.getNaicsCode().getCode().toString());
                                });

                        // find NAICS that are present in both, but as different types
                        mfrNaics.stream()
                                .filter(mn -> fsNaics.stream().anyMatch(fn -> fn.getNaicsCode().equals(mn.getNaicsCode()) && !fn.getNaicsCodeType().equals(mn.getNaicsCodeType())))
                                .forEach(mn -> {
                                    context.addCreationChange(ValidationField.FACILITY_NAICS, "facilitysite.naics.updated", facility, ReportChangeType.UPDATE,
                                            mn.getNaicsCode().getCode().toString(), mn.getNaicsCodeType());
                                });

                        // find NAICS from the MFR that aren't on the facility (will be added)
                        mfrNaics.stream()
                                .filter(mn -> fsNaics.stream().noneMatch(fn -> fn.getNaicsCode().equals(mn.getNaicsCode())))
                                .forEach(mn -> {
                                    context.addCreationChange(ValidationField.FACILITY_NAICS, "facilitysite.naics.added", facility,
                                            ReportChangeType.CREATE, mn.getNaicsCode().getCode().toString());
                                });
                    }

                	for (MasterFacilityNAICSXref masterFacilityNAICS : emissionsReport.getMasterFacilityRecord().getMasterFacilityNAICS()) {
                		FacilityNAICSXref facilityNAICS;
                		facilityNAICS = mapMasterFacilityNAICS(masterFacilityNAICS);
                        facilityNAICS.setFacilitySite(facility);
                        facility.getFacilityNAICS().add(facilityNAICS);
                    }
                }

                // get the previous year report and sub-components for comparing statuses
                List<EmissionsReport> erList = emissionsReportRepo.findByMasterFacilityRecordId(emissionsReport.getMasterFacilityRecord().getId()).stream()
                        .filter(var -> (var.getYear() != null && var.getYear() < emissionsReport.getYear()))
                        .sorted(Comparator.comparing(EmissionsReport::getYear))
                        .collect(Collectors.toList());

                EmissionsReport previousReport = (!erList.isEmpty()) ? erList.get(erList.size()-1) : null;
                Short previousReportYr = (!erList.isEmpty()) ? erList.get(erList.size()-1).getYear() : -1;

                List<ReleasePoint> previousRps = new ArrayList<>();
                if (!isAdminUpload) {
                	previousRps = releasePointRepo.retrieveByFacilityYear(
                        emissionsReport.getMasterFacilityRecord().getId(),
                        previousReportYr);
                }

                // Map Release Points
                for (ReleasePointBulkUploadDto bulkRp : bulkEmissionsReport.getReleasePoints()) {
                    ReleasePoint releasePoint = mapReleasePoint(bulkRp);

                    if (!isAdminUpload) {
	                    for (ReleasePoint previousRp : previousRps) {
	                    	if (releasePoint.getReleasePointIdentifier().equals(previousRp.getReleasePointIdentifier())) {

	                			releasePoint.setPreviousYearOperatingStatusCode(previousRp.getOperatingStatusCode());
	                    		previousRps.remove(previousRp);
	                    		break;
	                    	}
	                    }

                        if (releasePoint.getFenceLineDistance() == null && releasePoint.getFenceLineUomCode() != null) {
                            releasePoint.setFenceLineUomCode(null);
                        }
                        if (releasePoint.getFugitiveHeight() == null && releasePoint.getFugitiveHeightUomCode() != null) {
                            releasePoint.setFugitiveHeightUomCode(null);
                        }
                        if (releasePoint.getFugitiveLength() == null && releasePoint.getFugitiveLengthUomCode() != null) {
                            releasePoint.setFugitiveLengthUomCode(null);
                        }
                        if (releasePoint.getFugitiveWidth() == null && releasePoint.getFugitiveWidthUomCode() != null) {
                            releasePoint.setFugitiveWidthUomCode(null);
                        }
                        if (releasePoint.getStackDiameter() == null && releasePoint.getStackDiameterUomCode() != null) {
                            releasePoint.setStackDiameterUomCode(null);
                        }
                        if(releasePoint.getStackHeight() == null && releasePoint.getStackHeightUomCode() != null) {
                            releasePoint.setStackHeightUomCode(null);
                        }
                        if(releasePoint.getStackLength() == null && releasePoint.getStackLengthUomCode() != null) {
                            releasePoint.setStackLengthUomCode(null);
                        }
                        if(releasePoint.getStackWidth() == null && releasePoint.getStackWidthUomCode() != null) {
                            releasePoint.setStackWidthUomCode(null);
                        }
                    }

                    if (bulkFacility.getId().equals(bulkRp.getFacilitySiteId())) {
                        releasePoint.setFacilitySite(facility);
                        facility.getReleasePoints().add(releasePoint);
                        releasePointMap.put(bulkRp.getId(), releasePoint);
                    }
                }

                if (!isAdminUpload) {
	                for (ReleasePoint previousRp : previousRps) {
	                	if (!previousRp.getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN)) {
		            		ReleasePoint rpToAdd = new ReleasePoint(facility, previousRp);
		            		// id is nulled to stop hibernate from getting mad
		            		rpToAdd.clearId();
		            		// previous year status will be the same since we're pulling from the previous year report
		            		rpToAdd.setPreviousYearOperatingStatusCode(rpToAdd.getOperatingStatusCode());
		                    facility.getReleasePoints().add(rpToAdd);

		                    context.addCreationChange(ValidationField.RP, "component.added", rpToAdd, ReportChangeType.CREATE, "Release Point");
	                	}
	                }
                }

                List<EmissionsUnit> previousEus = new ArrayList<EmissionsUnit>();

                if (!isAdminUpload) {
	                previousEus = emissionsUnitRepo.retrieveByFacilityYear(
	                        emissionsReport.getMasterFacilityRecord().getId(),
	                        previousReportYr);
                }

                List<String> sccReqFuelAndAsphalt = sccRepo.findByMonthlyReporting(true).stream().map(scc -> scc.getCode()).distinct()
        				.collect(Collectors.toList());

                // Map Emissions Units
                for (EmissionsUnitBulkUploadDto bulkEmissionsUnit : bulkEmissionsReport.getEmissionsUnits()) {

                    if (bulkFacility.getId().equals(bulkEmissionsUnit.getFacilitySiteId())) {

                        EmissionsUnit emissionsUnit = mapEmissionsUnit(bulkEmissionsUnit);
                        emissionsUnit.setFacilitySite(facility);

                        if (!isAdminUpload) {
	                        for (EmissionsUnit previousEu : previousEus) {
	                        	if (emissionsUnit.getUnitIdentifier().equals(previousEu.getUnitIdentifier())) {

	                        		emissionsUnit.setPreviousYearOperatingStatusCode(previousEu.getOperatingStatusCode());
                                    emissionsUnit.setInitialMonthlyReportingPeriod(ConstantUtils.JANUARY);
	                        		previousEus.remove(previousEu);
	                        		break;
	                        	}
	                        }
                        }

                        // Map Emissions Processes
                        List<EmissionsProcess> previousProcs = emissionsProcessRepo.retrieveByParentFacilityYear(
                        		emissionsUnit.getUnitIdentifier(),
                                emissionsReport.getMasterFacilityRecord().getId(),
                                previousReportYr);

                        List<EmissionsProcess> processes = bulkEmissionsReport.getEmissionsProcesses().stream()
                            .filter(p -> bulkEmissionsUnit.getId().equals(p.getEmissionsUnitId()))
                            .map(bulkProcess -> {
                                EmissionsProcess process = mapEmissionsProcess(bulkProcess);

                                if (!isAdminUpload) {
	                                for (EmissionsProcess previousProc : previousProcs) {

	                                	if (process.getEmissionsProcessIdentifier().equals(previousProc.getEmissionsProcessIdentifier())) {

	                                		process.setPreviousYearOperatingStatusCode(previousProc.getOperatingStatusCode());
                                            process.setInitialMonthlyReportingPeriod(ConstantUtils.JANUARY);

                                            PointSourceSccCode newCode = sccRepo.findByCode(process.getSccCode());
                                            PointSourceSccCode oldCode = sccRepo.findByCode(previousProc.getSccCode());

                                            // if only SLT can edit SCC, prevent changes and add a message saying it was reverted
                                            if (Boolean.TRUE.equals(sltConfig.getEditSccEnabled()) && !newCode.getCode().equals(oldCode.getCode())) {

                                                process.setSccCode(oldCode.getCode());
                                                process.setSccCategory(oldCode.getCategory());

                                                context.addCreationChange(ValidationField.PROCESS, "process.scc.reverted.slt", process, ReportChangeType.UPDATE, "Emissions Process");
                                            }

                                            if ((previousReport != null && ((previousReport.getHasSubmitted() != null && previousReport.getHasSubmitted()) || previousReport.getEisLastTransactionId() != null))
                                                || (newCode.getLastInventoryYear() != null && newCode.getLastInventoryYear() < emissionsReport.getYear())
                                                || !oldCode.getFuelUseRequired().equals(newCode.getFuelUseRequired())
                                                || !oldCode.getMonthlyReporting().equals(newCode.getMonthlyReporting())) {

                                                process.setSccCode(oldCode.getCode());
                                                process.setSccCategory(oldCode.getCategory());

                                                context.addCreationChange(ValidationField.PROCESS, "process.scc.reverted", process, ReportChangeType.UPDATE, "Emissions Process");
                                            }
	                                		previousProcs.remove(previousProc);
	                                		break;
	                                	}
	                                }
                                }

                                // Map Reporting Periods
                                List<ReportingPeriod> periods = bulkEmissionsReport.getReportingPeriods().stream()
                                    .filter(rp -> bulkProcess.getId().equals(rp.getEmissionsProcessId()))
                                    .map(bulkPeriod -> {

                                        if (Boolean.TRUE.equals(sltConfig.getSltMonthlyFuelReportingEnabled())
                            					&& sccReqFuelAndAsphalt.contains(bulkProcess.getSccCode())) {
                                        	bulkPeriod.setFuelUseValue(null);
                            			}
                                        ReportingPeriod period = mapReportingPeriod(bulkPeriod);

                                        // Map Operating Details, should only be 1
                                        List<OperatingDetail> details = bulkEmissionsReport.getOperatingDetails().stream()
                                            .filter(od -> bulkPeriod.getId().equals(od.getReportingPeriodId()))
                                            .map(bulkDetail -> {
                                                OperatingDetail detail = mapOperatingDetail(bulkDetail);
                                                detail.setReportingPeriod(period);

                                                if (!isAdminUpload) {

                                                    if (detail.getAvgHoursPerDay() != null && detail.getAvgDaysPerWeek() != null && detail.getAvgWeeksPerPeriod() != null) {

                                                        BigDecimal calculatedTotalHours = detail.getAvgHoursPerDay().multiply(detail.getAvgDaysPerWeek())
                                                            .multiply(new BigDecimal(detail.getAvgWeeksPerPeriod().toString()));
                                                        short shortTotalHours = calculatedTotalHours.round(new MathContext(0, RoundingMode.HALF_UP)).shortValue();

                                                        // if operating 24/7/52, make sure correct value shows
                                                        if (shortTotalHours == 8736){
                                                            boolean leapYear = emissionsReport.getYear() % 4 == 0;

                                                            if (leapYear) {
                                                                shortTotalHours = 8784;
                                                            }
                                                            else {
                                                                shortTotalHours = 8760;
                                                            }
                                                        }

                                                        if (calculatedTotalHours != detail.getActualHoursPerPeriod()) {

                                                            detail.setActualHoursPerPeriod(calculatedTotalHours);

                                                            context.addCreationChange(ValidationField.DETAIL_ACT_HR_PER_PERIOD,
                                                                "operatingDetail.actualHoursPerPeriod.recalculated", detail, ReportChangeType.UPDATE);
                                                        }
                                                    }

                                                }

                                                return detail;
                                            }).collect(Collectors.toList());

                                        // Map Emissions
                                        List<Emission> emissions = bulkEmissionsReport.getEmissions().stream()
                                            .filter(e -> bulkPeriod.getId().equals(e.getReportingPeriodId()))
                                            .map(bulkEmission -> {
                                                Emission emission = mapEmission(bulkEmission);

                                                List<EmissionFormulaVariable> variables = bulkEmissionsReport.getEmissionFormulaVariables().stream()
                                                    .filter(efv -> bulkEmission.getId().equals(efv.getEmissionId()))
                                                    .map(bulkVariable -> {
                                                        EmissionFormulaVariable variable = mapEmissionFormulaVariable(bulkVariable);
                                                        variable.setEmission(emission);

                                                        return variable;
                                                    }).collect(Collectors.toList());

                                                emission.setReportingPeriod(period);
                                                emission.setVariables(variables);

                                                if (Boolean.TRUE.equals(emission.getFormulaIndicator()) && !emission.getVariables().isEmpty()) {
                                                    try {
                                                        emission.setEmissionsFactor(CalculationUtils.calculateEmissionFormula(emission.getEmissionsFactorFormula(), emission.getVariables()));
                                                    } catch (CalculationException e) {
                                                        // TODO: handle exception
                                                    }
                                                }

                                                if (!isAdminUpload) {
	                                                // looks wonky because the lambda function above expects emissions to be effectively final
                                                	if (emission.getEmissionsCalcMethodCode().getEpaEmissionFactor()) { // Redundant conditional prevents removal of text
		                                                emission.setEmissionsFactorText(
		                                                        emissionService.updateEmissionFactorDetails(emission, process, emissionsReport.getYear(), context)
		                                                        .getEmissionsFactorText());
                                                	} else if (Boolean.FALSE.equals(emission.getEmissionsCalcMethodCode().getTotalDirectEntry()) && Boolean.FALSE.equals(emission.getTotalManualEntry())) {
                                                		emissionService.recalculateTotalEmissions(emission, emissionsReport.getYear(), context);
                                                	}
                                                }

                                                return emission;
                                            }).collect(Collectors.toList());

                                        period.setEmissionsProcess(process);
                                        period.setEmissions(emissions);
                                        period.setOperatingDetails(details);

                                        return period;
                                    }).collect(Collectors.toList());

                                process.setEmissionsUnit(emissionsUnit);

                                // add a reporting period for each month if monthly reporting is enabled
                                // and process scc code is a monthly reporting scc
                                Short monthlyInitialYear = sltConfig.getSltMonthlyFuelReportingInitialYear();
                                if (Boolean.TRUE.equals(sltConfig.getSltMonthlyFuelReportingEnabled())
                                    && sccReqFuelAndAsphalt.contains(process.getSccCode())
                                    && (monthlyInitialYear == null || emissionsReport.getYear() >= monthlyInitialYear)) {
                                    List<ReportingPeriodCode> rpcs = reportingPeriodCodeRepo.findAllMonthsAndSemiAnnual();

                                    if (!periods.isEmpty()) {
                                        ReportingPeriod rp = periods.get(0);
                                        for (ReportingPeriodCode rpc : rpcs) {
                                            ReportingPeriod newRp = new ReportingPeriod(process, rp);

                                            // set the annual ids for reporting period and emissions
                                            newRp.setAnnualReportingPeriod(rp);
                                            for (Emission e : newRp.getEmissions()) {
                                                for (Emission em : rp.getEmissions()) {
                                                    if (e.getPollutant().getPollutantCode().equals(em.getPollutant().getPollutantCode())) {
                                                        e.setAnnualEmission(em);
                                                        break;
                                                    }
                                                }
                                            }

                                            newRp.clearId();
                                            newRp.setReportingPeriodTypeCode(rpc);

                                            rpService.nullMonthlyReportingFields(newRp);

                                            periods.add(newRp);
                                        }
                                    }
                                }

                                process.setReportingPeriods(periods);

                                processMap.put(bulkProcess.getId(), process);

                                return process;
                            }).collect(Collectors.toList());

                        if (!isAdminUpload) {
	                        for (EmissionsProcess previousProc : previousProcs) {
	                        	if (!previousProc.getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN)) {
		                        	EmissionsProcess procToAdd = mapToNewProcess(previousProc);
		                        	// update to the current emissions unit
		                        	procToAdd.setEmissionsUnit(emissionsUnit);
		                        	procToAdd.clearId();
		                        	procToAdd.setReleasePointAppts(null);

		                            processes.add(procToAdd);

		                            if (context != null) {
		                                context.addCreationChange(ValidationField.PROCESS, "component.added", procToAdd, ReportChangeType.CREATE, "Process");
		                            }
	                        	}
	                        }
                        }

                        emissionsUnit.setEmissionsProcesses(processes);

                        facility.getEmissionsUnits().add(emissionsUnit);

                    }

                }

                if (!isAdminUpload) {
	                for (EmissionsUnit previousEu : previousEus) {
	                	if (!previousEu.getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN)) {
		                	EmissionsUnit euToAdd = new EmissionsUnit(facility, previousEu);
		                    // id is nulled to stop hibernate from getting mad
		                	euToAdd.clearId();
		                    // previous year status will be the same since we're pulling from the previous year report
		                	euToAdd.setPreviousYearOperatingStatusCode(euToAdd.getOperatingStatusCode());
		                	// also need to set previous op code and current op year for processes
		                	for (EmissionsProcess proc : euToAdd.getEmissionsProcesses()) {
		                		proc.setPreviousYearOperatingStatusCode(proc.getOperatingStatusCode());
		                		proc.setStatusYear(euToAdd.getStatusYear());
		                		proc.setReleasePointAppts(null);
		                	}
		                    facility.getEmissionsUnits().add(euToAdd);

		                    if (context != null) {
		                        context.addCreationChange(ValidationField.EMISSIONS_UNIT, "component.added", euToAdd, ReportChangeType.CREATE, "Unit");
		                    }
	                	}
	                }
                }

                // Map Control Paths
                List<ControlPath> controlPaths = bulkEmissionsReport.getControlPaths().stream()
                    .filter(c -> bulkFacility.getId().equals(c.getFacilitySiteId()))
                    .map(bulkControlPath -> {
                        ControlPath path = mapControlPath(bulkControlPath);
                        path.setFacilitySite(facility);

                        // Map Control Path Pollutants
                        List<ControlPathPollutant> controlPathPollutants = bulkEmissionsReport.getControlPathPollutants().stream()
                            .filter(cp -> bulkControlPath.getId().equals(cp.getControlPathId()))
                            .map(bulkControlPathPollutant -> {

                                ControlPathPollutant controlPathPollutant = mapControlPathPollutant(bulkControlPathPollutant);
                                controlPathPollutant.setControlPath(path);

                                return controlPathPollutant;
                            }).collect(Collectors.toList());

                        path.setPollutants(controlPathPollutants);

                        controlPathMap.put(bulkControlPath.getId(), path);

                        return path;
                    }).collect(Collectors.toList());

                facility.setControlPaths(controlPaths);

                // Map Controls
                List<Control> previousControls = controlRepo.retrieveByFacilityYear(
                        emissionsReport.getMasterFacilityRecord().getId(),
                        previousReportYr);

                List<Control> controls = bulkEmissionsReport.getControls().stream()
                    .filter(c -> bulkFacility.getId().equals(c.getFacilitySiteId()))
                    .map(bulkControl -> {
                        Control control = mapControl(bulkControl);
                        control.setFacilitySite(facility);

                        if (!isAdminUpload) {
	                        for (Control previousControl : previousControls) {
	                            if (control.getIdentifier().equals(previousControl.getIdentifier())) {

	                            	control.setPreviousYearOperatingStatusCode(previousControl.getOperatingStatusCode());
	                            	previousControls.remove(previousControl);
	                                break;
	                            }
	                        }
                        }

                        // Map Control Pollutants
                        List<ControlPollutant> controlPollutants = bulkEmissionsReport.getControlPollutants().stream()
                            .filter(c -> bulkControl.getId().equals(c.getControlId()))
                            .map(bulkControlPollutant -> {
                                ControlPollutant controlPollutant = mapControlPollutant(bulkControlPollutant);
                                controlPollutant.setControl(control);

                                return controlPollutant;
                            }).collect(Collectors.toList());

                        control.setPollutants(controlPollutants);

                        controlMap.put(bulkControl.getId(), control);

                        return control;
                    }).collect(Collectors.toList());

                if (!isAdminUpload) {
	                for (Control previousControl : previousControls) {
	                	if (!previousControl.getOperatingStatusCode().getCode().equals(ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN)) {

		                	Control controlToAdd = new Control(facility, previousControl);
		                	// id is nulled to stop hibernate from getting mad
		                	controlToAdd.clearId();
		                	// previous year status will be the same since we're pulling from the previous year report
		                	controlToAdd.setPreviousYearOperatingStatusCode(controlToAdd.getOperatingStatusCode());

		                	controls.add(controlToAdd);

		                	context.addCreationChange(ValidationField.CONTROL, "component.added", controlToAdd, ReportChangeType.CREATE, "Control Device");
	                	}
	                }
                }

                facility.setControls(controls);

                // Map Control Assignments into controls and control paths
                bulkEmissionsReport.getControlAssignments().forEach(bulkControlAssignment -> {
                    ControlAssignment controlAssignment = mapControlAssignment(bulkControlAssignment);

                    ControlPath controlPath = controlPathMap.get(bulkControlAssignment.getControlPathId());
                    if (controlPath != null) {
                        controlAssignment.setControlPath(controlPath);
                        controlPath.getAssignments().add(controlAssignment);
                    } else {

                        warnings.add(String.format("ControlPath %s referenced by assigned %s does not exist.",
                            bulkControlAssignment.getControlPathId(), bulkControlAssignment.getId()));
                    }

                    ControlPath controlPathChild = controlPathMap.get(bulkControlAssignment.getControlPathChildId());
                    if (controlPathChild != null) {
                        controlAssignment.setControlPathChild(controlPathChild);
                        controlPathChild.getChildAssignments().add(controlAssignment);
                    } else {

                        warnings.add(String.format("ControlPath %s referenced by assigned %s does not exist.",
                            bulkControlAssignment.getControlPathChildId(), bulkControlAssignment.getId()));
                    }

                    Control control = controlMap.get(bulkControlAssignment.getControlId());
                    if (control != null) {
                        controlAssignment.setControl(control);
                        control.getAssignments().add(controlAssignment);
                    } else {

                        warnings.add(String.format("Control %s referenced by assigned %s does not exist.",
                            bulkControlAssignment.getControlId(), bulkControlAssignment.getId()));
                    }

                });

                // Map Release Point Apportionments into release points and emissions processes, along with control paths
                bulkEmissionsReport.getReleasePointAppts().forEach(bulkRpAppt -> {
                    ReleasePoint rp = releasePointMap.get(bulkRpAppt.getReleasePointId());
                    EmissionsProcess ep = processMap.get(bulkRpAppt.getEmissionProcessId());
                    if (rp != null && ep != null) {
                        ReleasePointAppt rpAppt = mapReleasePointAppt(bulkRpAppt);
                        rpAppt.setReleasePoint(rp);
                        rpAppt.setEmissionsProcess(ep);

                        if (bulkRpAppt.getControlPathId() != null) {
                            ControlPath controlPath = controlPathMap.get(bulkRpAppt.getControlPathId());

                            if (controlPath != null) {
                                rpAppt.setControlPath(controlPath);
                                controlPath.getReleasePointAppts().add(rpAppt);
                            }
                        }

                        rp.getReleasePointAppts().add(rpAppt);
                        ep.getReleasePointAppts().add(rpAppt);
                    }
                });

                facility.setEmissionsReport(emissionsReport);
                emissionsReport.getFacilitySites().add(facility);
            }

            logger.debug("Warnings {}", warnings);

            emissionsReport.setDeleted(false);
            return emissionsReport;
        };
    }

    /**
     * Map an ControlBulkUploadDto to an Control domain model
     */
    private Control mapControl(ControlBulkUploadDto dto) {

        Control result = uploadMapper.controlFromDto(dto);

        if (dto.getOperatingStatusCode() != null) {
            result.setOperatingStatusCode(operatingStatusRepo.findById(dto.getOperatingStatusCode()).orElse(null));
        }
        if (dto.getStatusYear() != null) {
        	result.setStatusYear(toShort(dto.getStatusYear()));
        }
        if (dto.getControlMeasureCode() != null) {
            result.setControlMeasureCode(controlMeasureCodeRepo.findById(dto.getControlMeasureCode()).orElse(null));
        }
        // Percent capture has been removed for control device per CEF-984.
        if (dto.getPercentCapture() != null) {
        	result.setPercentCapture(null);
        }

        return result;
    }

    /**
     * Map an ControlAssignmentBulkUploadDto to an ControlAssignment domain model
     */
    private ControlAssignment mapControlAssignment(ControlAssignmentBulkUploadDto dto) {

        ControlAssignment result = uploadMapper.controlAssignmentFromDto(dto);

        return result;
    }

    /**
     * Map an ControlPathBulkUploadDto to an ControlPath domain model
     */
    private ControlPath mapControlPath(ControlPathBulkUploadDto dto) {

        ControlPath result = uploadMapper.controlPathFromDto(dto);

        return result;
    }

    /**
     * Map an ControlPollutantBulkUploadDto to an ControlPollutant domain model
     */
    private ControlPollutant mapControlPollutant(ControlPollutantBulkUploadDto dto) {

        ControlPollutant result = uploadMapper.controlPollutantFromDto(dto);

        if (dto.getPollutantCode() != null) {
            result.setPollutant(pollutantRepo.findById(dto.getPollutantCode()).orElse(null));
        }

        return result;
    }

    /**
     * Map an ControlPathPollutantBulkUploadDto to an ControlPathPollutant domain model
     */
    private ControlPathPollutant mapControlPathPollutant(ControlPathPollutantBulkUploadDto dto) {

    	ControlPathPollutant result = uploadMapper.controlPathPollutantFromDto(dto);

        if (dto.getPollutantCode() != null) {
            result.setPollutant(pollutantRepo.findById(dto.getPollutantCode()).orElse(null));
        }

        return result;
    }

    /**
     * Map an OperatingDetailBulkUploadDto to an OperatingDetail domain model
     */
    private Emission mapEmission(EmissionBulkUploadDto dto) {

        Emission result = uploadMapper.emissionsFromDto(dto);

        result.setFormulaIndicator(Strings.emptyToNull(dto.getEmissionsFactorFormula()) != null);

        if (dto.getEmissionsCalcMethodCode() != null) {
            result.setEmissionsCalcMethodCode(calcMethodCodeRepo.findById(dto.getEmissionsCalcMethodCode()).orElse(null));
        }
        if (dto.getEmissionsUomCode() != null) {
            result.setEmissionsUomCode(unitMeasureCodeRepo.findById(dto.getEmissionsUomCode()).orElse(null));
        }
        if (dto.getEmissionsNumeratorUom() != null) {
            result.setEmissionsNumeratorUom(unitMeasureCodeRepo.findById(dto.getEmissionsNumeratorUom()).orElse(null));
        }
        if (dto.getEmissionsDenominatorUom() != null) {
            result.setEmissionsDenominatorUom(unitMeasureCodeRepo.findById(dto.getEmissionsDenominatorUom()).orElse(null));
        }
        if (dto.getPollutantCode() != null) {
            result.setPollutant(pollutantRepo.findById(dto.getPollutantCode()).orElse(null));
        }

        if (result.getEmissionsUomCode() != null && result.getTotalEmissions() != null) {
            result.setCalculatedEmissionsTons(emissionService.calculateEmissionTons(result));
        }

        result.setTotalEmissions(CalculationUtils.setSignificantFigures(result.getTotalEmissions(), CalculationUtils.EMISSIONS_PRECISION));

        return result;
    }

    private EmissionFormulaVariable mapEmissionFormulaVariable(EmissionFormulaVariableBulkUploadDto dto) {

        EmissionFormulaVariable result = new EmissionFormulaVariable();
        result.setValue(toBigDecimal(dto.getValue()));

        if (dto.getEmissionFormulaVariableCode() != null) {
            result.setVariableCode(emissionFormulaVariableCodeRepo.findById(dto.getEmissionFormulaVariableCode()).orElse(null));
        }

        return result;
    }

    /**
     * Map an EmissionsProcessBulkUploadDto to an EmissionsProcess domain model
     */
    private EmissionsProcess mapEmissionsProcess(EmissionsProcessBulkUploadDto dto) {

        EmissionsProcess result = uploadMapper.emissionsProcessFromDto(dto);

        if (dto.getAircraftEngineTypeCode() != null) {
            result.setAircraftEngineTypeCode(aircraftEngineRepo.findById(dto.getAircraftEngineTypeCode()).orElse(null));
        }

        if (dto.getOperatingStatusCode() != null) {
            result.setOperatingStatusCode(operatingStatusRepo.findById(dto.getOperatingStatusCode()).orElse(null));
        }

        setSccCategoryFindBySccCodeOrDefault(result, dto.getSccCode());

        return result;
    }

    private void setSccCategoryFindBySccCodeOrDefault(EmissionsProcess ep, String sourceSccCode) {
        ep.setSccCategory(ConstantUtils.DEFAULT_SCC_CATEGORY);
        if (sourceSccCode != null) {
            PointSourceSccCode scc = sccRepo.findByCode(sourceSccCode);
            if (scc != null) {
                ep.setSccCategory(scc.getCategory());
            }
        }
    }

    /**
     * Map an EmissionsReportBulkUploadDto to an EmissionsReport domain model
     */
    private EmissionsReport mapEmissionsReport(EmissionsReportBulkUploadDto bulkEmissionsReport) {

        EmissionsReport emissionsReport = new EmissionsReport();
        MasterFacilityRecord mfr = findMasterFacilityRecord(bulkEmissionsReport);

        emissionsReport.setEisProgramId(mfr.getEisProgramId());
        emissionsReport.setYear(bulkEmissionsReport.getYear());
        emissionsReport.setStatus(ReportStatus.valueOf(bulkEmissionsReport.getStatus()));
        emissionsReport.setEisLastSubmissionStatus(EisSubmissionStatus.NotStarted);
        emissionsReport.setProgramSystemCode(mfr.getProgramSystemCode());

        if (bulkEmissionsReport.getValidationStatus() != null) {
            emissionsReport.setValidationStatus(ValidationStatus.valueOf(bulkEmissionsReport.getValidationStatus()));
        }

        emissionsReport.setMasterFacilityRecord(mfr);


        return emissionsReport;
    }


    /**
     * Find the master facility record associated with the bulk upload emissions report
     */
    private MasterFacilityRecord findMasterFacilityRecord(EmissionsReportBulkUploadDto bulkEmissionsReport) {
        Optional<MasterFacilityRecord> mfr = null;

        //check based on the master facility record id
        if (bulkEmissionsReport.getMasterFacilityRecordId() != null) {
            mfr = mfrRepo.findById(bulkEmissionsReport.getMasterFacilityRecordId());
        }

        //if the master facility record isn't found yet, search based on EIS ID
        if ((mfr == null || !mfr.isPresent()) && bulkEmissionsReport.getEisProgramId() != null && !(bulkEmissionsReport.getEisProgramId().isEmpty())) {
            mfr = mfrRepo.findByEisProgramId(bulkEmissionsReport.getEisProgramId());
        }

        //if the master facility record isn't found yet, search based on agency facility ID and PSC combo
        if (mfr == null || !mfr.isPresent()) {
            String agencyFacilityIdentifier = findAgencyFacilityIdentifier(bulkEmissionsReport);
            List<MasterFacilityRecord> mfrList = mfrRepo.findByProgramSystemCodeCodeAndAgencyFacilityIdentifierIgnoreCase(bulkEmissionsReport.getProgramSystemCode(), agencyFacilityIdentifier);
            if (mfrList.size() > 0) {
                mfr = Optional.of(mfrList.get(0));
            }

        }

        return mfr.orElseThrow(() -> new NotExistException("Master Facility Record", bulkEmissionsReport.getMasterFacilityRecordId().toString()));
    }


    /**
     * Find the agency facility ID within the facility site of the bulk emissions report
     */
    private String findAgencyFacilityIdentifier(EmissionsReportBulkUploadDto bulkEmissionsReport) {

        String agencyFacilityIdentifier = null;

        if (bulkEmissionsReport.getFacilitySites().size() > 0) {
            agencyFacilityIdentifier = bulkEmissionsReport.getFacilitySites().get(0).getAgencyFacilityIdentifier();
        }

        return agencyFacilityIdentifier;
    }


    /**
     * Map an EmissionsUnitBulkUploadDto to an EmissionsUnit domain model
     */
    private EmissionsUnit mapEmissionsUnit(EmissionsUnitBulkUploadDto bulkEmissionsUnit) {

        EmissionsUnit emissionsUnit = new EmissionsUnit();

        emissionsUnit.setUnitIdentifier(bulkEmissionsUnit.getUnitIdentifier());
        emissionsUnit.setDescription(bulkEmissionsUnit.getDescription());
        emissionsUnit.setStatusYear(toShort(bulkEmissionsUnit.getStatusYear()));
        emissionsUnit.setDesignCapacity(toBigDecimal(bulkEmissionsUnit.getDesignCapacity()));
        emissionsUnit.setComments(bulkEmissionsUnit.getComments());

        if (bulkEmissionsUnit.getTypeCode() != null) {
            emissionsUnit.setUnitTypeCode(unitTypeRepo.findById(bulkEmissionsUnit.getTypeCode()).orElse(null));
        }
        if (bulkEmissionsUnit.getOperatingStatusCodeDescription() != null) {
            emissionsUnit.setOperatingStatusCode(operatingStatusRepo.findById(bulkEmissionsUnit.getOperatingStatusCodeDescription()).orElse(null));
        }
        if (bulkEmissionsUnit.getUnitOfMeasureCode() != null) {
            emissionsUnit.setUnitOfMeasureCode(unitMeasureCodeRepo.findById(bulkEmissionsUnit.getUnitOfMeasureCode()).orElse(null));
        }

        return emissionsUnit;
    }

    /**
     * Map a FacilitySiteBulkUploadDto to a FacilitySite domain model
     */
    private FacilitySite mapFacility(FacilitySiteBulkUploadDto bulkFacility) {

        FacilitySite facility = new FacilitySite();

        facility.setAgencyFacilityIdentifier(bulkFacility.getAgencyFacilityIdentifier());
        facility.setName(bulkFacility.getName());
        facility.setDescription(bulkFacility.getDescription());
        facility.setStatusYear(toShort(bulkFacility.getStatusYear()));
        facility.setStreetAddress(bulkFacility.getStreetAddress());
        facility.setCity(bulkFacility.getCity());
        facility.setCountryCode(bulkFacility.getCountryCode());
        facility.setPostalCode(bulkFacility.getPostalCode());
        facility.setLatitude(toBigDecimal(bulkFacility.getLatitude()));
        facility.setLongitude(toBigDecimal(bulkFacility.getLongitude()));
        facility.setMailingStreetAddress(bulkFacility.getMailingStreetAddress());
        facility.setMailingCity(bulkFacility.getMailingCity());
        facility.setMailingPostalCode(bulkFacility.getMailingPostalCode());
        facility.setComments(bulkFacility.getComments());

        if (bulkFacility.getFacilityCategoryCode() != null) {
            facility.setFacilityCategoryCode(facilityCategoryRepo.findById(bulkFacility.getFacilityCategoryCode()).orElse(null));
        }
        if (bulkFacility.getFacilitySourceTypeCode() != null) {
            facility.setFacilitySourceTypeCode(facilitySourceTypeRepo.findById(bulkFacility.getFacilitySourceTypeCode()).orElse(null));
        }
        if (bulkFacility.getOperatingStatusCode() != null) {
            facility.setOperatingStatusCode(operatingStatusRepo.findById(bulkFacility.getOperatingStatusCode()).orElse(null));
        }
        if (bulkFacility.getProgramSystemCode() != null) {
            facility.setProgramSystemCode(programSystemCodeRepo.findById(bulkFacility.getProgramSystemCode()).orElse(null));
        }
        if (bulkFacility.getTribalCode() != null) {
            facility.setTribalCode(tribalCodeRepo.findById(bulkFacility.getTribalCode()).orElse(null));
        }

        if (bulkFacility.getStateCode() != null) {
            facility.setStateCode((stateCodeRepo.findByUspsCode(bulkFacility.getStateCode().toUpperCase())).orElse(null));
            if (facility.getStateCode() != null && Strings.emptyToNull(bulkFacility.getCountyCode()) != null) {
                facility.setCountyCode(countyRepo.findByFipsStateCodeCodeAndCountyCode(facility.getStateCode().getCode(), bulkFacility.getCountyCode()).orElse(null));
            }
        }
        if (bulkFacility.getMailingStateCode() != null) {
            facility.setMailingStateCode((stateCodeRepo.findByUspsCode(bulkFacility.getMailingStateCode().toUpperCase())).orElse(null));
        }

        return facility;
    }

    /**
     * Map an FacilitySiteContactBulkUploadDto to an FacilitySiteContact domain model
     */
    private FacilitySiteContact mapFacilityContact(FacilitySiteContactBulkUploadDto bulkFacilityContact) {

        FacilitySiteContact facilityContact = new FacilitySiteContact();

        facilityContact.setPrefix(bulkFacilityContact.getPrefix());
        facilityContact.setFirstName(bulkFacilityContact.getFirstName());
        facilityContact.setLastName(bulkFacilityContact.getLastName());
        facilityContact.setEmail(bulkFacilityContact.getEmail());
        facilityContact.setPhone(bulkFacilityContact.getPhone());
        facilityContact.setPhoneExt(bulkFacilityContact.getPhoneExt());
        facilityContact.setStreetAddress(bulkFacilityContact.getStreetAddress());
        facilityContact.setCity(bulkFacilityContact.getCity());
        facilityContact.setCountryCode(bulkFacilityContact.getCountryCode());
        facilityContact.setPostalCode(bulkFacilityContact.getPostalCode());
        facilityContact.setMailingStreetAddress(bulkFacilityContact.getMailingStreetAddress());
        facilityContact.setMailingCity(bulkFacilityContact.getMailingCity());
        facilityContact.setMailingPostalCode(bulkFacilityContact.getMailingPostalCode());
        facilityContact.setMailingCountryCode(bulkFacilityContact.getMailingCountryCode());

        if (bulkFacilityContact.getType() != null) {
            facilityContact.setType((contactTypeRepo.findById(bulkFacilityContact.getType())).orElse(null));
        }
        if (bulkFacilityContact.getStateCode() != null) {
            facilityContact.setStateCode((stateCodeRepo.findByUspsCode(bulkFacilityContact.getStateCode().toUpperCase())).orElse(null));
            if (facilityContact.getStateCode() != null && Strings.emptyToNull(bulkFacilityContact.getCountyCode()) != null) {
                facilityContact.setCountyCode(countyRepo.findByFipsStateCodeCodeAndCountyCode(facilityContact.getStateCode().getCode(), bulkFacilityContact.getCountyCode()).orElse(null));
            }
        }
        if (bulkFacilityContact.getMailingStateCode() != null) {
            facilityContact.setMailingStateCode((stateCodeRepo.findByUspsCode(bulkFacilityContact.getMailingStateCode().toUpperCase())).orElse(null));
        }

        return facilityContact;
    }

    /**
     * Map an FacilityNAICSBulkUploadDto to an FacilityNAICS domain model
     */
    private FacilityNAICSXref mapFacilityNAICS(FacilityNAICSBulkUploadDto bulkFacilityNAICS) {

        FacilityNAICSXref facilityNAICS = new FacilityNAICSXref();

        if (Boolean.TRUE.equals(bulkFacilityNAICS.isPrimaryFlag())) {
        	facilityNAICS.setNaicsCodeType(NaicsCodeType.PRIMARY);
        }
        else if (Boolean.FALSE.equals(bulkFacilityNAICS.isPrimaryFlag())) {
        	facilityNAICS.setNaicsCodeType(NaicsCodeType.SECONDARY);
        }

        if (Strings.emptyToNull(bulkFacilityNAICS.getNaicsCodeType()) != null) {
        	facilityNAICS.setNaicsCodeType(NaicsCodeType.valueOf(bulkFacilityNAICS.getNaicsCodeType()));
        }

        Integer naics = toInt(bulkFacilityNAICS.getCode());
        if (naics != null) {
            facilityNAICS.setNaicsCode((naicsCodeRepo.findById(naics)).orElse(null));
        }

        return facilityNAICS;
    }

    /**
     * Map a MasterFacilityNAICSXref to an FacilityNAICS domain model
     */
    private FacilityNAICSXref mapMasterFacilityNAICS(MasterFacilityNAICSXref masterFacilityNAICS) {

        FacilityNAICSXref facilityNAICS = new FacilityNAICSXref();

        facilityNAICS.setNaicsCodeType(masterFacilityNAICS.getNaicsCodeType());

        facilityNAICS.setNaicsCode(masterFacilityNAICS.getNaicsCode());

        return facilityNAICS;
    }

    /**
     * Map an OperatingDetailBulkUploadDto to an OperatingDetail domain model
     */
    private OperatingDetail mapOperatingDetail(OperatingDetailBulkUploadDto dto) {

        OperatingDetail result = uploadMapper.operatingDetailFromDto(dto);

        return result;
    }

    /**
     * Map a ReleasePointBulkUploadDto to a ReleasePoint domain model
     */
    private ReleasePoint mapReleasePoint(ReleasePointBulkUploadDto bulkReleasePoint) {

        ReleasePoint releasePoint = new ReleasePoint();

        releasePoint.setReleasePointIdentifier(bulkReleasePoint.getReleasePointIdentifier());
        releasePoint.setDescription(bulkReleasePoint.getDescription());
        releasePoint.setStackHeight(toBigDecimal(bulkReleasePoint.getStackHeight()));
        releasePoint.setStackDiameter(toBigDecimal(bulkReleasePoint.getStackDiameter()));
        releasePoint.setStackWidth(toBigDecimal(bulkReleasePoint.getStackWidth()));
        releasePoint.setStackLength(toBigDecimal(bulkReleasePoint.getStackLength()));
        releasePoint.setExitGasVelocity(toBigDecimal(bulkReleasePoint.getExitGasVelocity()));
        releasePoint.setExitGasTemperature(toShort(bulkReleasePoint.getExitGasTemperature()));
        releasePoint.setExitGasFlowRate(toBigDecimal(bulkReleasePoint.getExitGasFlowRate()));
        releasePoint.setStatusYear(toShort(bulkReleasePoint.getStatusYear()));
        releasePoint.setLatitude(toBigDecimal(bulkReleasePoint.getLatitude()));
        releasePoint.setLongitude(toBigDecimal(bulkReleasePoint.getLongitude()));
        releasePoint.setFugitiveMidPt2Latitude(toBigDecimal(bulkReleasePoint.getFugitiveLine2Latitude()));
        releasePoint.setFugitiveMidPt2Longitude(toBigDecimal(bulkReleasePoint.getFugitiveLine2Longitude()));
        releasePoint.setComments(bulkReleasePoint.getComments());
        releasePoint.setFugitiveHeight(toLong(bulkReleasePoint.getFugitiveHeight()));
        releasePoint.setFugitiveWidth(toBigDecimal(bulkReleasePoint.getFugitiveWidth()));
        releasePoint.setFugitiveLength(toBigDecimal(bulkReleasePoint.getFugitiveLength()));
        releasePoint.setFugitiveAngle(toLong(bulkReleasePoint.getFugitiveAngle()));
        releasePoint.setFenceLineDistance(toLong(bulkReleasePoint.getFenceLineDistance()));

        if (bulkReleasePoint.getOperatingStatusCode() != null) {
            releasePoint.setOperatingStatusCode(operatingStatusRepo.findById(bulkReleasePoint.getOperatingStatusCode()).orElse(null));
        }
        if (bulkReleasePoint.getTypeCode() != null) {
            releasePoint.setTypeCode(releasePointTypeRepo.findById(bulkReleasePoint.getTypeCode()).orElse(null));
        }
        if (bulkReleasePoint.getStackHeightUomCode() != null) {
            releasePoint.setStackHeightUomCode(unitMeasureCodeRepo.findById(bulkReleasePoint.getStackHeightUomCode()).orElse(null));
        }
        if (bulkReleasePoint.getStackDiameterUomCode() != null) {
            releasePoint.setStackDiameterUomCode(unitMeasureCodeRepo.findById(bulkReleasePoint.getStackDiameterUomCode()).orElse(null));
        }
        if (bulkReleasePoint.getStackWidthUomCode() != null) {
            releasePoint.setStackWidthUomCode(unitMeasureCodeRepo.findById(bulkReleasePoint.getStackWidthUomCode()).orElse(null));
        }
        if (bulkReleasePoint.getStackLengthUomCode() != null) {
            releasePoint.setStackLengthUomCode(unitMeasureCodeRepo.findById(bulkReleasePoint.getStackLengthUomCode()).orElse(null));
        }
        if (bulkReleasePoint.getExitGasVelocityUomCode() != null) {
            releasePoint.setExitGasVelocityUomCode(unitMeasureCodeRepo.findById(bulkReleasePoint.getExitGasVelocityUomCode()).orElse(null));
        }
        if (bulkReleasePoint.getExitGasFlowUomCode() != null) {
            releasePoint.setExitGasFlowUomCode(unitMeasureCodeRepo.findById(bulkReleasePoint.getExitGasFlowUomCode()).orElse(null));
        }
        if (bulkReleasePoint.getFenceLineUomCode() != null) {
            releasePoint.setFenceLineUomCode(unitMeasureCodeRepo.findById(bulkReleasePoint.getFenceLineUomCode()).orElse(null));
        }
        if (bulkReleasePoint.getFugitiveHeightUomCode() != null) {
            releasePoint.setFugitiveHeightUomCode(unitMeasureCodeRepo.findById(bulkReleasePoint.getFugitiveHeightUomCode()).orElse(null));
        }
        if (bulkReleasePoint.getFugitiveWidthUomCode() != null) {
            releasePoint.setFugitiveWidthUomCode(unitMeasureCodeRepo.findById(bulkReleasePoint.getFugitiveWidthUomCode()).orElse(null));
        }
        if (bulkReleasePoint.getFugitiveLengthUomCode() != null) {
            releasePoint.setFugitiveLengthUomCode(unitMeasureCodeRepo.findById(bulkReleasePoint.getFugitiveLengthUomCode()).orElse(null));
        }

        return releasePoint;
    }

    /**
     * Map an ReleasePointApptBulkUploadDto to an OperatingDetail domain model
     */
    private ReleasePointAppt mapReleasePointAppt(ReleasePointApptBulkUploadDto dto) {

        ReleasePointAppt result = uploadMapper.releasePointApptFromDto(dto);

        return result;
    }

    /**
     * Map an ReportingPeriodBulkUploadDto to an ReportingPeriod domain model
     */
    private ReportingPeriod mapReportingPeriod(ReportingPeriodBulkUploadDto dto) {

        ReportingPeriod result = uploadMapper.reportingPeriodFromDto(dto);

        if (dto.getCalculationMaterialCode() != null) {
            result.setCalculationMaterialCode(calcMaterialCodeRepo.findById(dto.getCalculationMaterialCode()).orElse(null));
        }
        if (dto.getCalculationParameterTypeCode() != null) {
            result.setCalculationParameterTypeCode(calcParamTypeCodeRepo.findById(dto.getCalculationParameterTypeCode()).orElse(null));
        }
        if (dto.getCalculationParameterUom() != null) {
            result.setCalculationParameterUom(unitMeasureCodeRepo.findById(dto.getCalculationParameterUom()).orElse(null));
        }
        if (dto.getEmissionsOperatingTypeCode() != null) {
            result.setEmissionsOperatingTypeCode(emissionsOperatingTypeCodeRepo.findById(dto.getEmissionsOperatingTypeCode()).orElse(null));
        }
        if (dto.getReportingPeriodTypeCode() != null) {
            result.setReportingPeriodTypeCode(reportingPeriodCodeRepo.findById(dto.getReportingPeriodTypeCode()).orElse(null));
        }
        if (dto.getFuelUseMaterialCode() != null) {
            result.setFuelUseMaterialCode(calcMaterialCodeRepo.findById(dto.getFuelUseMaterialCode()).orElse(null));
        }
        if (dto.getFuelUseUom() != null) {
            result.setFuelUseUom(unitMeasureCodeRepo.findById(dto.getFuelUseUom()).orElse(null));
        }
        if (dto.getHeatContentUom() != null) {
            result.setHeatContentUom(unitMeasureCodeRepo.findById(dto.getHeatContentUom()).orElse(null));
        }
        if (dto.getCalculationParameterValue() != null) {
        	result.setCalculationParameterValue(new BigDecimal(dto.getCalculationParameterValue()).setScale(6, RoundingMode.DOWN).stripTrailingZeros());
        }
        if (dto.getHeatContentValue() != null) {
        	result.setHeatContentValue(new BigDecimal(dto.getHeatContentValue()).setScale(5, RoundingMode.DOWN).stripTrailingZeros());
        }
        if (dto.getFuelUseValue() != null) {
        	result.setFuelUseValue(new BigDecimal(dto.getFuelUseValue()).setScale(6, RoundingMode.DOWN).stripTrailingZeros());
        }
        result.setStandardizedNonPointFuelUse(rpService.calculateFuelUseNonPointStandardized(result));

        return result;
    }

    private EmissionsReportBulkUploadDto parseWorkbookJson(ExcelParserResponse response,
                                                           EmissionsReportStarterDto metadata) {

       return parseJsonNode(true).andThen(result -> {

           result.setProgramSystemCode(metadata.getProgramSystemCode());
           result.setMasterFacilityRecordId(metadata.getMasterFacilityRecordId());
           result.setEisProgramId(metadata.getEisProgramId());
           result.setAgencyFacilityIdentifier(metadata.getAgencyFacilityIdentifier());
           result.setYear(metadata.getYear());
           result.setStatus(ReportStatus.IN_PROGRESS.name());
           result.setValidationStatus(ValidationStatus.UNVALIDATED.name());
           result.setEisLastSubmissionStatus(EisSubmissionStatus.NotStarted.name());

           return result;

       }).apply(response.getJson());
    }

    private BigDecimal toBigDecimal(String strval) {

        return Strings.isNullOrEmpty(strval) ? null : new BigDecimal(strval);
    }

    private Double toDouble(String strval) {

        return Strings.isNullOrEmpty(strval) ? null : Double.parseDouble(strval);
    }

    private Integer toInt(String strval) {

        return Strings.isNullOrEmpty(strval) ? null : Integer.parseInt(strval);
    }

    private Long toLong(String strval) {

        return Strings.isNullOrEmpty(strval) ? null : Long.parseLong(strval);
    }

    private Short toShort(String strval) {

        return Strings.isNullOrEmpty(strval) ? null : Short.parseShort(strval);
    }

    public static class JsonNodeToBulkUploadDto implements Function<JsonNode, EmissionsReportBulkUploadDto> {

        private final ObjectMapper objectMapper;

        public JsonNodeToBulkUploadDto(ObjectMapper objectMapper, boolean failUnknownProperties) {

            this.objectMapper = objectMapper.copy()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failUnknownProperties)
                .registerModule(new BlankToNullModule());
        }

        @Override
        public EmissionsReportBulkUploadDto apply(JsonNode jsonNode) {

            EmissionsReportBulkUploadDto result;

            try {
                result = this.objectMapper.treeToValue(jsonNode, EmissionsReportBulkUploadDto.class);

            } catch (JsonProcessingException e) {

                String msg = e.getMessage().replaceAll(
                    EmissionsReportBulkUploadDto.class.getPackage().getName().concat("."), "");

                WorksheetError violation = WorksheetError.createSystemError(msg);

                throw new BulkReportValidationException(Collections.singletonList(violation));
            }

            return result;
        }
    }

    public static class JsonNodeToCaersJsonDto implements Function<JsonNode, EmissionsReportJsonDto> {

        private final ObjectMapper objectMapper;

        public JsonNodeToCaersJsonDto(ObjectMapper objectMapper, boolean failUnknownProperties) {

            this.objectMapper = objectMapper.copy()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failUnknownProperties)
                .registerModule(new BlankToNullModule());
        }

        @Override
        public EmissionsReportJsonDto apply(JsonNode jsonNode) {

            EmissionsReportJsonDto result;

            try {
                result = this.objectMapper.treeToValue(jsonNode, EmissionsReportJsonDto.class);

            } catch (JsonProcessingException e) {

                String msg = e.getMessage().replaceAll(
                    EmissionsReportJsonDto.class.getPackage().getName().concat("."), "");

                WorksheetError violation = WorksheetError.createSystemError(msg);

                throw new BulkReportValidationException(Collections.singletonList(violation));
            }

            return result;
        }
    }

    public EmissionsProcess mapToNewProcess(EmissionsProcess originalProcess) {
    	EmissionsProcess newProcess = new EmissionsProcess();

    	newProcess.setId(null);
    	newProcess.setEmissionsUnit(originalProcess.getEmissionsUnit());
    	newProcess.setAircraftEngineTypeCode(originalProcess.getAircraftEngineTypeCode());
    	newProcess.setOperatingStatusCode(originalProcess.getOperatingStatusCode());
    	newProcess.setPreviousYearOperatingStatusCode(originalProcess.getOperatingStatusCode());
    	newProcess.setEmissionsProcessIdentifier(originalProcess.getEmissionsProcessIdentifier());
    	newProcess.setStatusYear(originalProcess.getStatusYear());
    	newProcess.setSccCode(originalProcess.getSccCode());

        setSccCategoryFindBySccCodeOrDefault(newProcess, originalProcess.getSccCode());

    	newProcess.setSccDescription(originalProcess.getSccDescription());
    	newProcess.setSccShortName(originalProcess.getSccShortName());
    	newProcess.setDescription(originalProcess.getDescription());
    	newProcess.setComments(originalProcess.getComments());

        for (ReportingPeriod reportingPeriod : originalProcess.getReportingPeriods()) {
        	ReportingPeriod rpToAdd = new ReportingPeriod(newProcess, reportingPeriod);

        	rpToAdd.setId(null);

        	for (OperatingDetail opDetail : rpToAdd.getOperatingDetails()) {
        		opDetail.setId(null);
        	}
        	for (Emission e : rpToAdd.getEmissions()) {
        		e.setId(null);
        	}

        	newProcess.getReportingPeriods().add(rpToAdd);

        }

        return newProcess;
    }
}
