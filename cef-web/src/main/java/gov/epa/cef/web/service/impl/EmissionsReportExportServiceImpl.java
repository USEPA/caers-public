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
package gov.epa.cef.web.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import gov.epa.cef.web.config.SLTBaseConfig;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.*;
import gov.epa.cef.web.repository.spec.ReportExportSpecs;
import gov.epa.cef.web.service.dto.CodeLookupDto;
import gov.epa.cef.web.service.dto.EmissionFactorDto;
import gov.epa.cef.web.service.dto.PollutantDto;
import gov.epa.cef.web.service.dto.PointSourceSccCodeDto;
import gov.epa.cef.web.service.dto.json.*;
import gov.epa.cef.web.service.mapper.EmissionFactorMapper;
import gov.epa.cef.web.service.mapper.LookupEntityMapper;
import gov.epa.cef.web.service.mapper.json.JsonReportMapper;
import gov.epa.cef.web.util.SLTConfigHelper;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.base.Functions;
import com.google.common.base.Strings;

import gov.epa.cef.web.exception.NotExistException;
import gov.epa.cef.web.service.EmissionsReportExportService;
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
import gov.epa.cef.web.service.dto.bulkUpload.WorksheetName;
import gov.epa.cef.web.service.mapper.BulkUploadMapper;
import gov.epa.cef.web.util.TempFile;

@Service
public class EmissionsReportExportServiceImpl implements EmissionsReportExportService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String EXCEL_FILE_PATH = "/excel/CEF_BulkUpload_Template.xlsx";
    private static final String EXCEL_GENERIC_LOOKUP_TEXT = "INDEX(%s!$A$2:$A$%d,MATCH(\"%s\",%s!$B$2:$B$%d,0))";
    private static final String EXCEL_GENERIC_LOOKUP_NUMBER = "INDEX(%s!$A$2:$A$%d,MATCH(%s,%s!$B$2:$B$%d,0))";
    private static final int EXCEL_MAPPING_HEADER_ROWS = 23;
    private static final int EXCEL_LOOKUP_MAPPING_HEADER_ROWS = 1;
    private static final float ROW_HEIGHT = 14.4f;

    private static final int LOOKUP_ROW_START = EXCEL_LOOKUP_MAPPING_HEADER_ROWS + 1;

    @Autowired
    private EmissionsReportRepository emissionsReportRepo;

    @Autowired
    private CalculationMaterialCodeRepository calcMaterialCodeRepo;

    @Autowired
    private CalculationMethodCodeRepository calcMethodCodeRepo;

    @Autowired
    private CalculationParameterTypeCodeRepository calcParamTypeCodeRepo;

    @Autowired
    private ControlMeasureCodeRepository controlMeasureCodeRepo;

    @Autowired
    private EmissionsOperatingTypeCodeRepository emissionsOperatingTypeCodeRepo;

    @Autowired
    private GHGEmissionFactorRepository ghgEfRepo;

    @Autowired
    private WebfireEmissionFactorRepository webfireEfRepo;

    @Autowired
    private OperatingStatusCodeRepository operatingStatusRepo;

    @Autowired
    private PollutantRepository pollutantRepo;

    @Autowired
    private ProgramSystemCodeRepository programSystemCodeRepo;

    @Autowired
    private PointSourceSccCodeRepository sccCodeRepo;

    @Autowired
    private ReportingPeriodCodeRepository reportingPeriodCodeRepo;

    @Autowired
    private UnitTypeCodeRepository unitTypeRepo;

    @Autowired
    private EmissionFactorMapper emissionFactorMapper;

    @Autowired
    private BulkUploadMapper uploadMapper;

    @Autowired
    private JsonReportMapper jsonMapper;

    @Autowired
    private LookupEntityMapper lookupMapper;

    @Autowired
    private LookupServiceImpl lookupService;

    @Autowired
    private SLTConfigHelper sltConfigHelper;

    /**
     * Testing method for generating upload JSON for a report
     *
     * @param reportId
     * @return
     */
    @Override
    public EmissionsReportBulkUploadDto generateBulkUploadDto(Long reportId) {
        EmissionsReport report = this.emissionsReportRepo.findById(reportId)
            .orElseThrow(() -> new NotExistException("Emissions Report", reportId));

        return generateBulkUploadDto(report);
    }

    public EmissionsReportBulkUploadDto generateBulkUploadDto(EmissionsReport report) {
        List<String> SHUTDOWN_CODES = new ArrayList<>(Arrays.asList("PS","TS"));

        List<FacilitySite> facilitySites = report.getFacilitySites();
        List<EmissionsUnit> units = facilitySites.stream()
            .flatMap(f -> f.getEmissionsUnits().stream())
            .sorted((i1, i2) -> i1.getUnitIdentifier().compareToIgnoreCase(i2.getUnitIdentifier()))
            .collect(Collectors.toList());
        List<EmissionsProcess> processes = units.stream()
            .flatMap(u -> u.getEmissionsProcesses().stream())
            .collect(Collectors.toList());
        List<ReportingPeriod> periods = processes.stream()
            .filter(p -> !SHUTDOWN_CODES.contains(p.getOperatingStatusCode().getCode())) // do not include PS TS's reporting period
            .flatMap(p -> p.getReportingPeriods().stream())
            .filter(rp -> "A".equals(rp.getReportingPeriodTypeCode().getCode()))
            .collect(Collectors.toList());
        List<OperatingDetail> operatingDetails = periods.stream()
            .flatMap(p -> p.getOperatingDetails().stream())
            .sorted((i1, i2) -> {
                String display1 = String.format("%s-%s-%s",
                        i1.getReportingPeriod().getEmissionsProcess().getEmissionsUnit().getUnitIdentifier(),
                        i1.getReportingPeriod().getEmissionsProcess().getEmissionsProcessIdentifier(),
                        i1.getReportingPeriod().getReportingPeriodTypeCode().getShortName());
                String display2 = String.format("%s-%s-%s",
                        i2.getReportingPeriod().getEmissionsProcess().getEmissionsUnit().getUnitIdentifier(),
                        i2.getReportingPeriod().getEmissionsProcess().getEmissionsProcessIdentifier(),
                        i2.getReportingPeriod().getReportingPeriodTypeCode().getShortName());
                return display1.compareToIgnoreCase(display2);
            })
            .collect(Collectors.toList());
        List<Emission> emissions = periods.stream()
            .flatMap(p -> p.getEmissions().stream())
            .collect(Collectors.toList());
        List<EmissionFormulaVariable> variables = emissions.stream()
            .flatMap(e -> e.getVariables().stream())
            .collect(Collectors.toList());
        List<ReleasePoint> releasePoints = facilitySites.stream()
            .flatMap(f -> f.getReleasePoints().stream())
            .sorted((i1, i2) -> i1.getReleasePointIdentifier().compareToIgnoreCase(i2.getReleasePointIdentifier()))
            .collect(Collectors.toList());
        List<ReleasePointAppt> releasePointAppts = releasePoints.stream()
            .flatMap(r -> r.getReleasePointAppts().stream())
            .collect(Collectors.toList());
        List<ControlPath> controlPaths = facilitySites.stream()
            .flatMap(f -> f.getControlPaths().stream())
            .collect(Collectors.toList());
        List<Control> controls = facilitySites.stream()
            .flatMap(c -> c.getControls().stream())
            .sorted((i1, i2) -> i1.getIdentifier().compareToIgnoreCase(i2.getIdentifier()))
            .collect(Collectors.toList());
        // control_path_id in the DB is non-null so this should get every assignment exactly once
        List<ControlAssignment> controlAssignments = controlPaths.stream()
            .flatMap(c -> c.getAssignments().stream())
            .collect(Collectors.toList());
        List<ControlPollutant> controlPollutants = controls.stream()
            .flatMap(c -> c.getPollutants().stream())
            .collect(Collectors.toList());
        List<ControlPathPollutant> controlPathPollutants = controlPaths.stream()
                .flatMap(c -> c.getPollutants().stream())
                .collect(Collectors.toList());
        List<FacilityNAICSXref> facilityNaics = facilitySites.stream()
            .flatMap(fn -> fn.getFacilityNAICS().stream())
            .collect(Collectors.toList());
        List<FacilitySiteContact> facilityContacts = facilitySites.stream()
            .flatMap(fc -> fc.getContacts().stream())
            .collect(Collectors.toList());

        EmissionsReportBulkUploadDto reportDto = uploadMapper.emissionsReportToDto(report);
        reportDto.setFacilitySites(uploadMapper.facilitySiteToDtoList(facilitySites));
        reportDto.setEmissionsUnits(uploadMapper.emissionsUnitToDtoList(units));

        reportDto.setEmissionsProcesses(processes.stream().map(i -> {
                EmissionsProcessBulkUploadDto result = uploadMapper.emissionsProcessToDto(i);
                result.setDisplayName(String.format("%s-%s",
                        i.getEmissionsUnit().getUnitIdentifier(),
                        i.getEmissionsProcessIdentifier()));
                return result;
            }).sorted((i1, i2) -> i1.getDisplayName().compareToIgnoreCase(i2.getDisplayName()))
            .collect(Collectors.toList()));

        reportDto.setReportingPeriods(periods.stream().map(i -> {
                ReportingPeriodBulkUploadDto result = uploadMapper.reportingPeriodToDto(i);
                result.setDisplayName(String.format("%s-%s-%s",
                        i.getEmissionsProcess().getEmissionsUnit().getUnitIdentifier(),
                        i.getEmissionsProcess().getEmissionsProcessIdentifier(),
                        i.getReportingPeriodTypeCode().getShortName()));
                return result;
            }).sorted((i1, i2) -> i1.getDisplayName().compareToIgnoreCase(i2.getDisplayName()))
            .collect(Collectors.toList()));

        reportDto.setOperatingDetails(uploadMapper.operatingDetailToDtoList(operatingDetails));

        reportDto.setEmissions(emissions.stream().map(i -> {
            EmissionBulkUploadDto result = uploadMapper.emissionToDto(i);
            result.setDisplayName(String.format("%s-%s-%s(%s)",
                    i.getReportingPeriod().getEmissionsProcess().getEmissionsUnit().getUnitIdentifier(),
                    i.getReportingPeriod().getEmissionsProcess().getEmissionsProcessIdentifier(),
                    i.getReportingPeriod().getReportingPeriodTypeCode().getShortName(),
                    i.getPollutant().getPollutantName()));
            return result;
        }).sorted((i1, i2) -> i1.getDisplayName().compareToIgnoreCase(i2.getDisplayName()))
        .collect(Collectors.toList()));

        // Update UoMs in all release points
        for (ReleasePoint rp : releasePoints) {
        	rp = sanitizeReleasePointUoMs(rp);
        }

        reportDto.setEmissionFormulaVariables(uploadMapper.emissionFormulaVariableToDtoList(variables));
        reportDto.setReleasePoints(uploadMapper.releasePointToDtoList(releasePoints));
        reportDto.setReleasePointAppts(uploadMapper.releasePointApptToDtoList(releasePointAppts));
        reportDto.setControlPaths(uploadMapper.controlPathToDtoList(controlPaths));
        reportDto.setControls(uploadMapper.controlToDtoList(controls));
        reportDto.setControlAssignments(uploadMapper.controlAssignmentToDtoList(controlAssignments));
        reportDto.setControlPollutants(uploadMapper.controlPollutantToDtoList(controlPollutants));
        reportDto.setControlPathPollutants(uploadMapper.controlPathPollutantToDtoList(controlPathPollutants));
        reportDto.setFacilityNAICS(uploadMapper.faciliytNAICSToDtoList(facilityNaics));
        reportDto.setFacilityContacts(uploadMapper.facilitySiteContactToDtoList(facilityContacts));

        for (FacilityNAICSBulkUploadDto nc : reportDto.getFacilityNAICS()) {
            String description = lookupService.findNaicsCodeDescriptionByYear(
                Integer.valueOf(nc.getCode()), reportDto.getYear());
            if (description != null) { // Alternate description found for year
                nc.setDescription(description);
            }
        }

        return reportDto;
    }

    public EmissionsReportJsonDto generateJsonDto(Long reportId) {
        EmissionsReport report = this.emissionsReportRepo.findById(reportId)
            .orElseThrow(() -> new NotExistException("Emissions Report", reportId));

        return generateJsonDto(report);
    }

    public EmissionsReportJsonDto generateJsonDto(EmissionsReport report) {

        for (FacilitySite fsite: report.getFacilitySites()) {
            // Update UoMs in all release points
            for (ReleasePoint rp : fsite.getReleasePoints()) {
                sanitizeReleasePointUoMs(rp);
            }
        }

        EmissionsReportJsonDto reportJsonDto = jsonMapper.fromEmissionsReport(report);

        checkUpdateBillingExempt(reportJsonDto, report);

        return reportJsonDto;
    }

    private Specification<EmissionsReport> generateJsonExportQuery(ReportExportQueryDto criteria) {

        Specification<EmissionsReport> spec = ReportExportSpecs.forSlt(criteria.getProgramSystemCode());

        if (criteria.getReportYear() != null) {
            spec = spec.and(ReportExportSpecs.forReportYear(criteria.getReportYear()));
        }

        if (criteria.getAgencyFacilityId() != null && !criteria.getAgencyFacilityId().isEmpty()) {
            spec = spec.and(ReportExportSpecs.forAgencyFacilityId(criteria.getAgencyFacilityId()));
        }

        if (criteria.getReportId() != null && !criteria.getReportId().isEmpty()) {
            spec = spec.and(ReportExportSpecs.forReportId(criteria.getReportId()));
        }

        if (criteria.getReportStatus() != null && !criteria.getReportStatus().isEmpty()) {
            spec = spec.and(ReportExportSpecs.forStatuses(criteria.getReportStatus()));
        }

        if (criteria.getAgencyFacilityId() != null && !criteria.getAgencyFacilityId().isEmpty()) {
            spec = spec.and(ReportExportSpecs.forAgencyFacilityId(criteria.getAgencyFacilityId()));
        }

        if (criteria.getModifiedStartDate() != null) {
            spec = spec.and(ReportExportSpecs.afterModifiedDate(criteria.getModifiedStartDate()));
        }

        if (criteria.getModifiedEndDate() != null) {
            spec = spec.and(ReportExportSpecs.beforeModifiedDate(criteria.getModifiedEndDate()));
        }

        if (criteria.getCertifiedStartDate() != null) {
            spec = spec.and(ReportExportSpecs.afterCertifiedDate(criteria.getCertifiedStartDate()));
        }

        if (criteria.getCertifiedEndDate() != null) {
            spec = spec.and(ReportExportSpecs.beforeCertifiedDate(criteria.getCertifiedEndDate()));
        }

        return spec;
    }

    public List<EmissionsReportJsonDto> generateJsonExportDto(ReportExportQueryDto criteria) {

        Specification<EmissionsReport> spec = generateJsonExportQuery(criteria);

        Page<EmissionsReport> reports = this.emissionsReportRepo.findAll(spec, Pageable.ofSize(criteria.getPageSize()).withPage(criteria.getPage()));
        for (EmissionsReport r : reports.getContent()) {
            r.getFacilitySites().forEach(fs -> {
                for (EmissionsUnit eu : fs.getEmissionsUnits()) {
                    for (EmissionsProcess ep : eu.getEmissionsProcesses()) {
                        for (ReportingPeriod rp : ep.getReportingPeriods()) {
                            for (Emission e : rp.getEmissions()) {
                                List<EmissionFactorDto> efList = emissionFactorMapper.toWebfireEfDtoList(
                                webfireEfRepo.findBySccCodePollutantControlIndicator(ep.getSccCode(),
                                    e.getPollutant().getPollutantCode(),
                                    e.getEmissionsCalcMethodCode().getControlIndicator())
                                .stream().filter(ef -> !ef.getRevoked()).collect(Collectors.toList()));
                                if (efList.size() > 0) {
                                    EmissionFactorDto ef = efList.get(0);
                                    e.setEmissionsFactorDescription(ef.getDescription());
                                }
                            }
                        }
                    }
                }
            });

        }
        return jsonMapper.fromEmissionsReportList(reports.getContent());
    }

    public List<ReportInfoJsonDto> generateJsonExportSummaryDto(ReportExportQueryDto criteria) {

        Specification<EmissionsReport> spec = generateJsonExportQuery(criteria);

        List<EmissionsReport> reports = this.emissionsReportRepo.findAll(spec);

        return jsonMapper.infoFromEmissionsReportList(reports);

    }

    /**
     * Generate an Excel spreadsheet export for a report
     *
     * Maps a report into our Excel template for uploading. This function creates a temporary copy of the Excel
     * template and then uses Apache POI to populate that copy with the existing data. First, this dynamically
     * populates lookup tables that inform the dropdowns where applicable. Then, we modify existing rows like a
     * user would so that validation and formulas remain intact and populate dropdowns by looking up the value
     * in the spreadsheet for the code we have.
     *
     * Currently has commented out debugging code while more sections are added
     * @param reportId
     * @param outputStream
     */
    @Override
    public synchronized void generateExcel(Long reportId, OutputStream outputStream) {

        logger.info("Begin generating Excel export");

        EmissionsReportBulkUploadDto uploadDto = this.generateBulkUploadDto(reportId);

        SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(uploadDto.getFacilitySites().get(0).getProgramSystemCode());
        Boolean billingExemptEnabled = sltConfig.getSltFeatureBillingExemptEnabled();

        logger.info("Begin file manipulation");

        try (InputStream is = this.getClass().getResourceAsStream(EXCEL_FILE_PATH);
             TempFile tempFile = TempFile.from(is, UUID.randomUUID().toString());
             OPCPackage pkg = OPCPackage.open(tempFile.getFile());
             XSSFWorkbook wb = XSSFWorkbookFactory.createWorkbook(pkg)) {

            // locked cells will return null and cause null pointer exceptions without this
            wb.setMissingCellPolicy(MissingCellPolicy.CREATE_NULL_AS_BLANK);

//            facilitySheet.disableLocking();

            XSSFFormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();

            Map<Long, ReleasePointBulkUploadDto> rpMap = uploadDto.getReleasePoints()
                    .stream().collect(Collectors.toMap(ReleasePointBulkUploadDto::getId, Functions.identity()));
            Map<Long, EmissionsUnitBulkUploadDto> euMap = uploadDto.getEmissionsUnits()
                    .stream().collect(Collectors.toMap(EmissionsUnitBulkUploadDto::getId, Functions.identity()));
            Map<Long, EmissionsProcessBulkUploadDto> epMap = uploadDto.getEmissionsProcesses()
                    .stream().collect(Collectors.toMap(EmissionsProcessBulkUploadDto::getId, Functions.identity()));
            Map<Long, ControlBulkUploadDto> controlMap = uploadDto.getControls()
                    .stream().collect(Collectors.toMap(ControlBulkUploadDto::getId, Functions.identity()));
            Map<Long, ControlPathBulkUploadDto> pathMap = uploadDto.getControlPaths()
                    .stream().collect(Collectors.toMap(ControlPathBulkUploadDto::getId, Functions.identity()));
            Map<Long, ReportingPeriodBulkUploadDto> periodMap = uploadDto.getReportingPeriods()
                    .stream().collect(Collectors.toMap(ReportingPeriodBulkUploadDto::getId, Functions.identity()));
            Map<Long, EmissionBulkUploadDto> emissionMap = uploadDto.getEmissions()
                    .stream().collect(Collectors.toMap(EmissionBulkUploadDto::getId, Functions.identity()));

            generateFacilityExcelSheet(wb, formulaEvaluator, wb.getSheet(WorksheetName.FacilitySite.sheetName()), uploadDto.getFacilitySites());
            generateFacilityContactExcelSheet(wb, formulaEvaluator, wb.getSheet(WorksheetName.FacilitySiteContact.sheetName()), uploadDto.getFacilityContacts());
            generateNAICSExcelSheet(wb, wb.getSheet(WorksheetName.FacilityNaics.sheetName()), uploadDto.getFacilityNAICS());
            generateReleasePointsExcelSheet(wb, formulaEvaluator, wb.getSheet(WorksheetName.ReleasePoint.sheetName()), uploadDto.getReleasePoints());
            generateEmissionUnitExcelSheet(wb, formulaEvaluator, wb.getSheet(WorksheetName.EmissionsUnit.sheetName()), uploadDto.getEmissionsUnits());
            generateProcessesExcelSheet(wb, formulaEvaluator, wb.getSheet(WorksheetName.EmissionsProcess.sheetName()), uploadDto.getEmissionsProcesses(), euMap, billingExemptEnabled);
            generateControlsExcelSheet(wb, formulaEvaluator, wb.getSheet(WorksheetName.Control.sheetName()), uploadDto.getControls());
            generateControlPathsExcelSheet(wb, formulaEvaluator, wb.getSheet(WorksheetName.ControlPath.sheetName()), uploadDto.getControlPaths());
            generateControlAssignmentsExcelSheet(wb, formulaEvaluator, wb.getSheet(WorksheetName.ControlAssignment.sheetName()), uploadDto.getControlAssignments(), controlMap, pathMap);
            generateControlPollutantExcelSheet(wb, formulaEvaluator, wb.getSheet(WorksheetName.ControlPollutant.sheetName()), uploadDto.getControlPollutants(), controlMap);
            generateControlPathPollutantExcelSheet(wb, formulaEvaluator, wb.getSheet(WorksheetName.ControlPathPollutant.sheetName()), uploadDto.getControlPathPollutants(), pathMap);
            generateApportionmentExcelSheet(wb, formulaEvaluator, wb.getSheet(WorksheetName.ReleasePointAppt.sheetName()), uploadDto.getReleasePointAppts(), rpMap, epMap, pathMap);
            generateReportingPeriodExcelSheet(wb, formulaEvaluator, wb.getSheet(WorksheetName.ReportingPeriod.sheetName()), uploadDto.getReportingPeriods(), epMap);
            generateOperatingDetailExcelSheet(wb, formulaEvaluator, wb.getSheet(WorksheetName.OperatingDetail.sheetName()), uploadDto.getOperatingDetails(), periodMap);
            generateEmissionExcelSheet(wb, formulaEvaluator, wb.getSheet(WorksheetName.Emission.sheetName()), uploadDto.getEmissions(), periodMap);
            generateEmissionFormulaVariableExcelSheet(wb, formulaEvaluator, wb.getSheet(WorksheetName.EmissionFormulaVariable.sheetName()),
                    uploadDto.getEmissionFormulaVariables(), emissionMap);

            try (SXSSFWorkbook swb = new SXSSFWorkbook(wb)) { // SXSSFWorkbook improves performance; write-only

                generateLookupNAICSExcelSheetAndNamedRanges(swb, swb.getSheet(WorksheetName.LookupNaics.sheetName()), uploadDto.getYear());
                generateLookupEmissionFactorExcelSheetAndNamedRanges(swb, swb.getSheet(WorksheetName.LookupEmissionFactors.sheetName()));
                generateLookupSCCExcelSheetAndNamedRanges(swb, swb.getSheet(WorksheetName.LookupSCCs.sheetName()), sltConfig.getNonPointSccEnabled());
                generateLookupPollutantExcelSheetAndNamedRanges(swb, swb.getSheet(WorksheetName.LookupPollutant.sheetName()), uploadDto.getYear(), uploadDto.getProgramSystemCode());
                swb.setForceFormulaRecalculation(true);
                swb.write(outputStream);
                swb.close();
                wb.close();
                // Flushes temp backup files for workbook from memory
                swb.dispose();

                logger.info("Finished generating Excel export");

            } catch (IOException | EncryptedDocumentException ex) {

                logger.error("Unable to generate dynamic Excel lookups", ex);
                throw new IllegalStateException(ex);
            }
        } catch (IOException | EncryptedDocumentException | InvalidFormatException ex) {

            logger.error("Unable to generate Excel export ", ex);
            throw new IllegalStateException(ex);
        }
    }

    /**
     * add WebFIRE & GHG EmissionFactors lookup values, generate unique EF formula list, and update named ranges
     * @param wb
     * @param sheet
     */
    private void generateLookupEmissionFactorExcelSheetAndNamedRanges(Workbook wb, Sheet sheet) {

        int currentRow = EXCEL_LOOKUP_MAPPING_HEADER_ROWS;

        List<EmissionFactorDto> efList = emissionFactorMapper.toWebfireEfDtoList(
            webfireEfRepo.findAll(Sort.by(Sort.Direction.ASC, "sccCode")
                    .and(Sort.by(Sort.Direction.ASC, "id")))
                .stream().filter(ef -> !ef.getRevoked()).collect(Collectors.toList()));
        efList.addAll(emissionFactorMapper.toGHGEfDtoList(
            ghgEfRepo.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream().filter(ef -> !ef.getRevoked()).collect(Collectors.toList())));

        List<String> uniqueFormulasList = new ArrayList<>(webfireEfRepo
            .findDistinctWebfireEFFormulasNotRevoked(Sort.by(Sort.Direction.ASC, "emissionFactorFormula")));
        uniqueFormulasList.addAll(ghgEfRepo
            .findDistinctGHGEFFormulasNotRevoked(Sort.by(Sort.Direction.ASC, "emissionFactorFormula"))
                .stream().filter(efFormula -> !uniqueFormulasList.contains(efFormula)).collect(Collectors.toList()));
        final String stringNull = "NULL";

        CellStyle textStyle = wb.createCellStyle();
        textStyle.cloneStyleFrom(sheet.getColumnStyle(0));

        for (EmissionFactorDto ef : efList) {
            Row row;
            if (sheet.getRow(currentRow) == null) {
                row = sheet.createRow(currentRow);
                for (int i = 0; i < 19; i++) {
                    row.createCell(i);
                }
            } else {
                row = sheet.getRow(currentRow);
            }
            // Emission Factors
            row.getCell(0).setCellValue(ef.getSccCode() != null ? ef.getSccCode() : stringNull);
            row.getCell(1).setCellValue(ef.getPollutantCode());
            row.getCell(2).setCellValue(ef.getDescription() != null ? ef.getDescription() : stringNull);
            row.getCell(3).setCellValue(ef.getNote() != null ? ef.getNote() : stringNull);
            row.getCell(4).setCellValue(ef.getCalculationMaterialCode().getCode());
            row.getCell(5).setCellValue(ef.getCalculationMaterialCode().getDescription());
            if (ef.getEmissionFactor() != null) {
                setCellNumberValue(row.getCell(6), ef.getEmissionFactor().toString());
            } else {
                row.getCell(6).setCellValue(stringNull);
            }
            row.getCell(7).setCellValue(ef.getEmissionFactorFormula() != null ? ef.getEmissionFactorFormula() : stringNull);
            row.getCell(8).setCellValue(ef.getMinValue() != null ? ef.getMinValue().toString() : stringNull);
            row.getCell(9).setCellValue(ef.getMaxValue() != null ? ef.getMaxValue().toString() : stringNull);
            row.getCell(10).setCellValue(ef.getEmissionsNumeratorUom().getCode());
            row.getCell(11).setCellValue(ef.getEmissionsDenominatorUom().getCode());
            row.getCell(12).setCellValue(ef.getQuality());
            row.getCell(13).setCellValue(ef.getApplicability() != null ? ef.getApplicability() : stringNull);
            row.getCell(14).setCellValue(ef.getDerivation() != null ? ef.getDerivation() : stringNull);
            row.getCell(15).setCellValue(ef.getCondition() != null ? ef.getCondition() : stringNull);

            // Emission Factor Formulas
            if (currentRow <= uniqueFormulasList.size()) {
                row.getCell(18).setCellValue(uniqueFormulasList.get(currentRow - 1));
            }

            row.setZeroHeight(false);
            row.setHeightInPoints(ROW_HEIGHT);

            row.getCell(0).setCellStyle(textStyle);
            row.getCell(1).setCellStyle(textStyle);
            row.getCell(4).setCellStyle(textStyle);

            currentRow++;
        }

        final int efLookupRowEnd = efList.size() + 1;
        final int formulaLookupRowEnd = uniqueFormulasList.size() + 1;

        // Emission Factors
        updateNamedRange(WorksheetName.LookupEmissionFactors.sheetName(), wb.getName("emission_factor."),
            "A", "P", LOOKUP_ROW_START, efLookupRowEnd);
        // EF Formulas, with header
        updateNamedRange(WorksheetName.LookupEmissionFactors.sheetName(), wb.getName("emission_factor._1"),
            "S", "S", EXCEL_LOOKUP_MAPPING_HEADER_ROWS, formulaLookupRowEnd);
        // EF Formulas, without header
        updateNamedRange(WorksheetName.LookupEmissionFactors.sheetName(), wb.getName("emission_factor_formulas"),
            "S", "S", LOOKUP_ROW_START, formulaLookupRowEnd);
    }

    /**
     * add NAICS codes lookup values and descriptions per report year and update named ranges
     * @param wb
     * @param sheet
     * @param year
     */
    private void generateLookupNAICSExcelSheetAndNamedRanges(Workbook wb, Sheet sheet, Short year) {

        int currentRow = EXCEL_LOOKUP_MAPPING_HEADER_ROWS;

        List<CodeLookupDto> naicsCodesByYear =
            lookupService.retrieveNaicsCodesByYear(year, false, "description");

        for (CodeLookupDto naicsCode : naicsCodesByYear) {
            Row row;
            if (sheet.getRow(currentRow) == null) {
                row = sheet.createRow(currentRow);
                row.createCell(0);
                row.createCell(1);
            } else {
                row = sheet.getRow(currentRow);
            }
            row.getCell(0).setCellValue(formatNaicsDropdown(naicsCode.getCode(), naicsCode.getDescription()));
            row.getCell(1).setCellValue(naicsCode.getCode());
            row.setZeroHeight(false);
            row.setHeightInPoints(ROW_HEIGHT);

            currentRow++;
        }

        final int lookupRowEnd = naicsCodesByYear.size() + 1;

        // Just descriptions
        updateNamedRange(WorksheetName.LookupNaics.sheetName(), wb.getName("naics_code_descriptions"),
            "A", "A", LOOKUP_ROW_START, lookupRowEnd);
        // Codes and descriptions
        updateNamedRange(WorksheetName.LookupNaics.sheetName(), wb.getName("naics_codes"),
            "A", "B", LOOKUP_ROW_START, lookupRowEnd);
    }

    private void generateLookupPollutantExcelSheetAndNamedRanges(Workbook wb, Sheet sheet, Short year, String programSystemCode) {
        int currentRow = EXCEL_LOOKUP_MAPPING_HEADER_ROWS;
        int currentYear = year;
        List<PollutantDto> pollutantsByYear =
            lookupService.retrieveCurrentPollutants(currentYear);
        for (PollutantDto pollutant: pollutantsByYear) {
            Row row;

            if (pollutant.getProgramSystemCodes().isEmpty() || pollutant.getProgramSystemCodes().stream().anyMatch(psc -> psc.getCode().equals(programSystemCode))) {
                if (sheet.getRow(currentRow) == null) {
                    row = sheet.createRow(currentRow);
                    row.createCell(0);
                    row.createCell(1);
                } else {
                    row = sheet.getRow(currentRow);
                }
                row.getCell(0).setCellValue(pollutant.getPollutantName());
                row.getCell(1).setCellValue(pollutant.getPollutantCode());
                row.setZeroHeight(false);
                row.setHeightInPoints(ROW_HEIGHT);

                currentRow++;
            }
        }

        final int lookupRowEnd = pollutantsByYear.size() + 1;

        // Codes and descriptions
        updateNamedRange(WorksheetName.LookupPollutant.sheetName(), wb.getName("pollutant_codes"),
            "A", "B", LOOKUP_ROW_START, lookupRowEnd);
        // Just descriptions
        updateNamedRange(WorksheetName.LookupPollutant.sheetName(), wb.getName("pollutants"),
            "A", "A", LOOKUP_ROW_START, lookupRowEnd);

    }

    /**
     * add Source Classification Code lookup values and update named ranges
     * @param wb
     * @param sheet
     */
    private void generateLookupSCCExcelSheetAndNamedRanges(Workbook wb, Sheet sheet, Boolean isNonPointSCCEnabled) {

        int currentRow = EXCEL_LOOKUP_MAPPING_HEADER_ROWS;

        List<PointSourceSccCodeDto> sccCodesNotRetired = new ArrayList<>();

        if (isNonPointSCCEnabled) {
            sccCodesNotRetired.addAll(lookupMapper.pointSourceSccCodeToDtoList(
                this.sccCodeRepo.findAllNotRetired(Sort.by(Sort.Direction.ASC, "code"))));
        } else {
            sccCodesNotRetired.addAll(lookupMapper.pointSourceSccCodeToDtoList(
                this.sccCodeRepo.findAllPointNotRetired(Sort.by(Sort.Direction.ASC, "code"))));
        }

        for (PointSourceSccCodeDto sccCodeDto : sccCodesNotRetired) {
            Row row;
            if (sheet.getRow(currentRow) == null) {
                row = sheet.createRow(currentRow);
                row.createCell(0);
                row.createCell(1);
            } else {
                row = sheet.getRow(currentRow);
            }
            setCellNumberValue(row.getCell(0), sccCodeDto.getCode());
            row.getCell(1).setCellValue(sccCodeDto.getDescription());
            row.setZeroHeight(false);
            row.setHeightInPoints(ROW_HEIGHT);

            currentRow++;
        }

        final int lookupRowEnd = sccCodesNotRetired.size() + 1;

        // Just codes
        updateNamedRange(WorksheetName.LookupSCCs.sheetName(), wb.getName("Source_Classification_Codes"),
            "A", "A", LOOKUP_ROW_START, lookupRowEnd);
        // Codes and descriptions
        updateNamedRange(WorksheetName.LookupSCCs.sheetName(), wb.getName("Source_Classification_Code_Rows"),
            "A", "B", LOOKUP_ROW_START, lookupRowEnd);

    }

    /**
     * Map facility site into the facility site excel sheet
     * @param wb
     * @param formulaEvaluator
     * @param sheet
     * @param dtos
     */
    private void generateFacilityExcelSheet(Workbook wb, FormulaEvaluator formulaEvaluator, Sheet sheet, List<FacilitySiteBulkUploadDto> dtos) {

        int currentRow = EXCEL_MAPPING_HEADER_ROWS;

        for (FacilitySiteBulkUploadDto dto : dtos) {
            Row row = sheet.getRow(currentRow);

            row.getCell(2).setCellValue(dto.getAgencyFacilityIdentifier());
            row.getCell(3).setCellValue(dto.getFacilityCategoryCode());
//                row.getCell(4).setCellValue(dto.getFacilitySourceTypeCode());
            if (dto.getFacilitySourceTypeCode() != null) {
                // find the display name using the code in an excel lookup similar to how the code is found for the dropdown
                // generates a lookup formula then evaluates it to get the correct value using evaluateInCell so that the formula is removed afterwards
                row.getCell(5).setCellFormula(generateLookupFormula(wb, "FacilitySourceTypeCode", dto.getFacilitySourceTypeCode(), false));
                formulaEvaluator.evaluateInCell(row.getCell(5));
            }
            row.getCell(6).setCellValue(dto.getName());
            row.getCell(7).setCellValue(dto.getDescription());
//                row.getCell(8).setCellValue(dto.getOperatingStatusCode());
            if (dto.getOperatingStatusCode() != null) {
                this.operatingStatusRepo.findById(dto.getOperatingStatusCode()).ifPresent(item -> row.getCell(9).setCellValue(item.getDescription()));
            }
            setCellNumberValue(row.getCell(10), dto.getStatusYear());
//                row.getCell(11).setCellValue(dto.getProgramSystemCode());
            if (dto.getProgramSystemCode() != null) {
                this.programSystemCodeRepo.findById(dto.getProgramSystemCode()).ifPresent(item -> row.getCell(12).setCellValue(item.getDescription()));
            }
            row.getCell(13).setCellValue(dto.getStreetAddress());
            row.getCell(14).setCellValue(dto.getCity());
            row.getCell(15).setCellValue(dto.getStateFipsCode());
            row.getCell(16).setCellValue(dto.getStateCode());
            row.getCell(17).setCellValue(dto.getCountyCode());
            row.getCell(18).setCellValue(String.format("%s (%s)", dto.getCounty(), dto.getStateCode()));
//                row.getCell(19).setCellValue(dto.getCountryCode());
            row.getCell(20).setCellValue(dto.getPostalCode());
            setCellNumberValue(row.getCell(21), dto.getLatitude());
            setCellNumberValue(row.getCell(22), dto.getLongitude());
            row.getCell(23).setCellValue(dto.getMailingStreetAddress());
            row.getCell(24).setCellValue(dto.getMailingCity());
            row.getCell(25).setCellValue(dto.getMailingStateCode());
            row.getCell(26).setCellValue(dto.getMailingPostalCode());
//                row.getCell(27).setCellValue(dto.getMailingCountryCode());
            row.getCell(28).setCellValue(dto.getEisProgramId());
//                row.getCell(29).setCellValue(dto.getTribalCode());
            if (dto.getTribalCode() != null) {
                row.getCell(30).setCellFormula(generateLookupFormula(wb, "TribalCode", dto.getTribalCode(), false));
                formulaEvaluator.evaluateInCell(row.getCell(30));
            }

            currentRow++;

        }
    }

    /**
     * Map facility contacts into the facility contacts excel sheet
     * @param wb
     * @param formulaEvaluator
     * @param sheet
     * @param dtos
     */
    private void generateFacilityContactExcelSheet(Workbook wb, FormulaEvaluator formulaEvaluator, Sheet sheet, List<FacilitySiteContactBulkUploadDto> dtos) {

        int currentRow = EXCEL_MAPPING_HEADER_ROWS;

        for(FacilitySiteContactBulkUploadDto dto : dtos) {
            Row row = sheet.getRow(currentRow);

            if (dto.getType() != null) {
                row.getCell(4).setCellFormula(generateLookupFormula(wb, "ContactTypeCode", dto.getType(), true));
                formulaEvaluator.evaluateInCell(row.getCell(4));
            }
            row.getCell(5).setCellValue(dto.getPrefix());
            row.getCell(6).setCellValue(dto.getFirstName());
            row.getCell(7).setCellValue(dto.getLastName());
            row.getCell(8).setCellValue(dto.getEmail());
            setCellNumberValue(row.getCell(9), dto.getPhone());
            row.getCell(10).setCellValue(dto.getPhoneExt());
            row.getCell(11).setCellValue(dto.getStreetAddress());
            row.getCell(12).setCellValue(dto.getCity());
//            row.getCell(13).setCellValue(dto.getStateFipsCode());
            row.getCell(14).setCellValue(dto.getStateCode());
//            row.getCell(15).setCellValue(dto.getCountyCode());
            row.getCell(16).setCellValue(String.format("%s (%s)", dto.getCounty(), dto.getStateCode()));
//            row.getCell(17).setCellValue(dto.getCountryCode());
            row.getCell(18).setCellValue(dto.getPostalCode());
            row.getCell(19).setCellValue(dto.getMailingStreetAddress());
            row.getCell(20).setCellValue(dto.getMailingCity());
            row.getCell(21).setCellValue(dto.getMailingStateCode());
//            row.getCell(22).setCellValue(dto.getMailingCountryCode());
            row.getCell(23).setCellValue(dto.getMailingPostalCode());
//            row.getCell().setCellValue(dto.);

            currentRow++;

        }
    }

    /**
     * Map NAICS into the NAICS excel sheet and dynamically insert formulas
     * @param wb
     * @param sheet
     * @param dtos
     */
    private void generateNAICSExcelSheet(Workbook wb, Sheet sheet, List<FacilityNAICSBulkUploadDto> dtos) {

        int currentRow = EXCEL_MAPPING_HEADER_ROWS;

        // The last 2 columns for NAICS codes were not leading the correct style and were defaulting
        // to general data type and locked. This will get the overall column style for the columns
        // and use them instead which have the correct data types and are unlocked

        // general data type and unlocked
        CellStyle unlockedStyle = wb.createCellStyle();
        unlockedStyle.cloneStyleFrom(sheet.getColumnStyle(4));

        // text datatype and unlocked
        CellStyle tfStyle = wb.createCellStyle();
        tfStyle.cloneStyleFrom(sheet.getColumnStyle(5));

        for (FacilityNAICSBulkUploadDto dto : dtos) {
            Row row = sheet.getRow(currentRow);

            if (dto.getCode() != null) {
                row.getCell(3).setCellValue(dto.getCode());

                row.getCell(4).setCellStyle(unlockedStyle);
                row.getCell(4).setCellValue(formatNaicsDropdown(dto.getCode(), dto.getDescription()));

            }
            row.getCell(5).setCellStyle(tfStyle);
            row.getCell(5).setCellValue("" + dto.getNaicsCodeType());
            currentRow++;

        }
    }

    /**
     * Map release points into the release points excel sheet
     * @param wb
     * @param formulaEvaluator
     * @param sheet
     * @param dtos
     */
    private void generateReleasePointsExcelSheet(Workbook wb, FormulaEvaluator formulaEvaluator, Sheet sheet, List<ReleasePointBulkUploadDto> dtos) {

        int currentRow = EXCEL_MAPPING_HEADER_ROWS;

        for (ReleasePointBulkUploadDto dto : dtos) {
            Row row = sheet.getRow(currentRow);

            row.getCell(2).setCellValue(dto.getReleasePointIdentifier());
            if (dto.getTypeCode() != null) {
                row.getCell(4).setCellValue(dto.getTypeCode());
                row.getCell(5).setCellFormula(generateLookupFormula(wb, "ReleasePointTypeCode", dto.getTypeCode(), false));
                formulaEvaluator.evaluateInCell(row.getCell(5));
            }
            row.getCell(6).setCellValue(dto.getDescription());
            if (dto.getOperatingStatusCode() != null) {
                row.getCell(7).setCellValue(dto.getOperatingStatusCode());
                this.operatingStatusRepo.findById(dto.getOperatingStatusCode()).ifPresent(item -> row.getCell(8).setCellValue(item.getDescription()));
            }
            setCellNumberValue(row.getCell(9), dto.getStatusYear());
            setCellNumberValue(row.getCell(10), dto.getLatitude());
            setCellNumberValue(row.getCell(11), dto.getLongitude());
            setCellNumberValue(row.getCell(14), dto.getFugitiveLine2Latitude());
            setCellNumberValue(row.getCell(15), dto.getFugitiveLine2Longitude());
            setCellNumberValue(row.getCell(16), dto.getStackHeight());
            row.getCell(17).setCellValue(dto.getStackHeightUomCode());
            setCellNumberValue(row.getCell(18), dto.getStackDiameter());
            row.getCell(19).setCellValue(dto.getStackDiameterUomCode());
            setCellNumberValue(row.getCell(20), dto.getStackWidth());
            row.getCell(21).setCellValue(dto.getStackWidthUomCode());
            setCellNumberValue(row.getCell(22), dto.getStackLength());
            row.getCell(23).setCellValue(dto.getStackLengthUomCode());
            setCellNumberValue(row.getCell(24), dto.getExitGasVelocity());
            row.getCell(25).setCellValue(dto.getExitGasVelocityUomCode());
            setCellNumberValue(row.getCell(26), dto.getExitGasTemperature());
            setCellNumberValue(row.getCell(27), dto.getExitGasFlowRate());
            row.getCell(28).setCellValue(dto.getExitGasFlowUomCode());
            setCellNumberValue(row.getCell(29), dto.getFenceLineDistance());
            row.getCell(30).setCellValue(dto.getFenceLineUomCode());
            setCellNumberValue(row.getCell(31), dto.getFugitiveHeight());
            row.getCell(32).setCellValue(dto.getFugitiveHeightUomCode());
            setCellNumberValue(row.getCell(33), dto.getFugitiveWidth());
            row.getCell(34).setCellValue(dto.getFugitiveWidthUomCode());
            setCellNumberValue(row.getCell(35), dto.getFugitiveLength());
            row.getCell(36).setCellValue(dto.getFugitiveLengthUomCode());
            setCellNumberValue(row.getCell(37), dto.getFugitiveAngle());
            row.getCell(38).setCellValue(dto.getComments());

            currentRow++;

            dto.setRow(currentRow);

        }

    }

    /**
     * Map emissions units into the emissions units excel sheet
     * @param wb
     * @param formulaEvaluator
     * @param sheet
     * @param dtos
     */
    private void generateEmissionUnitExcelSheet(Workbook wb, FormulaEvaluator formulaEvaluator, Sheet sheet, List<EmissionsUnitBulkUploadDto> dtos) {

        int currentRow = EXCEL_MAPPING_HEADER_ROWS;

        for (EmissionsUnitBulkUploadDto dto : dtos) {
            Row row = sheet.getRow(currentRow);

            row.getCell(2).setCellValue(dto.getUnitIdentifier());
            row.getCell(4).setCellValue(dto.getDescription());
            if (dto.getTypeCode() != null) {
                this.unitTypeRepo.findById(dto.getTypeCode()).ifPresent(item -> row.getCell(6).setCellValue(item.getDescription()));
            }
            if (dto.getOperatingStatusCodeDescription() != null) {
                this.operatingStatusRepo.findById(dto.getOperatingStatusCodeDescription()).ifPresent(item -> row.getCell(8).setCellValue(item.getDescription()));
            }
            setCellNumberValue(row.getCell(9), dto.getStatusYear());
            setCellNumberValue(row.getCell(10), dto.getDesignCapacity());
            row.getCell(11).setCellValue(dto.getUnitOfMeasureCode());
            row.getCell(12).setCellValue(dto.getComments());

            currentRow++;

            dto.setRow(currentRow);

        }

    }

    /**
     * Map emissions processes into the emissions processes excel sheet
     * @param wb
     * @param formulaEvaluator
     * @param sheet
     * @param dtos
     */
    private void generateProcessesExcelSheet(Workbook wb, FormulaEvaluator formulaEvaluator, Sheet sheet,
            List<EmissionsProcessBulkUploadDto> dtos, Map<Long, EmissionsUnitBulkUploadDto> euMap, Boolean billingExemptEnabled) {

        int currentRow = EXCEL_MAPPING_HEADER_ROWS;

        for (EmissionsProcessBulkUploadDto dto : dtos) {
            Row row = sheet.getRow(currentRow);

            row.getCell(2).setCellValue(euMap.get(dto.getEmissionsUnitId()).getUnitIdentifier());
            row.getCell(3).setCellValue(dto.getEmissionsProcessIdentifier());
            row.getCell(6).setCellValue(dto.getDescription());
            if (dto.getOperatingStatusCode() != null) {
                row.getCell(7).setCellValue(dto.getOperatingStatusCode());
                this.operatingStatusRepo.findById(dto.getOperatingStatusCode()).ifPresent(item -> row.getCell(8).setCellValue(item.getDescription()));
            }
            setCellNumberValue(row.getCell(9), dto.getStatusYear());
            // using the double version of setCellValue since the spreadsheet expects this value to display as a number
            setCellNumberValue(row.getCell(11), dto.getSccCode());
            if (dto.getAircraftEngineTypeCode() != null) {
                row.getCell(13).setCellFormula(generateLookupFormula(wb, "AircraftEngineTypeCode", dto.getAircraftEngineTypeCode(), false));
                formulaEvaluator.evaluateInCell(row.getCell(13));
            }
            row.getCell(14).setCellValue(dto.getComments());


            if (billingExemptEnabled && dto.getSltBillingExempt() != null) {
                row.getCell(15).setCellValue("" + dto.getSltBillingExempt());
                row.getCell(15).getCellStyle().setLocked(false);
            }

            currentRow++;

            // store values to be used in later sheets; after row increments to deal with difference between 0-based and 1-based
            dto.setRow(currentRow);
            dto.setDisplayName(euMap.get(dto.getEmissionsUnitId()).getUnitIdentifier() + "-" + dto.getEmissionsProcessIdentifier());

        }

    }

    /**
     * Map controls into the controls excel sheet
     * @param wb
     * @param formulaEvaluator
     * @param sheet
     * @param dtos
     */
    private void generateControlsExcelSheet(Workbook wb, FormulaEvaluator formulaEvaluator, Sheet sheet, List<ControlBulkUploadDto> dtos) {

        int currentRow = EXCEL_MAPPING_HEADER_ROWS;

        for (ControlBulkUploadDto dto : dtos) {
            Row row = sheet.getRow(currentRow);

            row.getCell(2).setCellValue(dto.getIdentifier());
            row.getCell(4).setCellValue(dto.getDescription());
            setCellNumberValue(row.getCell(5), dto.getPercentControl());
            if (dto.getOperatingStatusCode() != null) {
                row.getCell(6).setCellValue(dto.getOperatingStatusCode());
                this.operatingStatusRepo.findById(dto.getOperatingStatusCode()).ifPresent(item -> row.getCell(7).setCellValue(item.getDescription()));
            }
            setCellNumberValue(row.getCell(8), dto.getStatusYear());
            if (dto.getControlMeasureCode() != null) {
                row.getCell(9).setCellValue(dto.getControlMeasureCode());
                this.controlMeasureCodeRepo.findById(dto.getControlMeasureCode()).ifPresent(item -> row.getCell(10).setCellValue(item.getDescription()));
            }
            setCellNumberValue(row.getCell(11), dto.getNumberOperatingMonths());
            row.getCell(12).setCellValue(dto.getStartDate());
            row.getCell(13).setCellValue(dto.getUpgradeDate());
            row.getCell(14).setCellValue(dto.getEndDate());
            row.getCell(15).setCellValue(dto.getUpgradeDescription());
            row.getCell(16).setCellValue(dto.getComments());

            currentRow++;

            dto.setRow(currentRow);

        }

    }

    /**
     * Map control paths into the control path excel sheet
     * @param wb
     * @param formulaEvaluator
     * @param sheet
     * @param dtos
     */
    private void generateControlPathsExcelSheet(Workbook wb, FormulaEvaluator formulaEvaluator, Sheet sheet, List<ControlPathBulkUploadDto> dtos) {

        int currentRow = EXCEL_MAPPING_HEADER_ROWS;

        for (ControlPathBulkUploadDto dto : dtos) {
            Row row = sheet.getRow(currentRow);

            row.getCell(2).setCellValue(dto.getPathId());
            row.getCell(3).setCellValue(dto.getDescription());
            setCellNumberValue(row.getCell(5), dto.getPercentControl());

            currentRow++;

            dto.setRow(currentRow);

        }

    }

    /**
     * Map control path pollutants into the control path pollutant excel sheet
     * @param wb
     * @param formulaEvaluator
     * @param sheet
     * @param dtos
     */
    private void generateControlPathPollutantExcelSheet(Workbook wb, FormulaEvaluator formulaEvaluator, Sheet sheet,
            List<ControlPathPollutantBulkUploadDto> dtos, Map<Long, ControlPathBulkUploadDto> controlMap) {

        int currentRow = EXCEL_MAPPING_HEADER_ROWS;

        for (ControlPathPollutantBulkUploadDto dto : dtos) {
            Row row = sheet.getRow(currentRow);

            if (dto.getControlPathId() != null) {
                row.getCell(2).setCellValue(controlMap.get(dto.getControlPathId()).getRow());
                row.getCell(3).setCellValue(controlMap.get(dto.getControlPathId()).getPathId());
            }
            if (dto.getPollutantCode() != null) {
                row.getCell(4).setCellValue(dto.getPollutantCode());
                // check if the code is a number or not when looking it up
                this.pollutantRepo.findById(dto.getPollutantCode()).ifPresent(item -> row.getCell(5).setCellValue(item.getPollutantName()));
            }
            setCellNumberValue(row.getCell(6), dto.getPercentReduction());

            currentRow++;

        }

    }

    /**
     * Map control assignments into the control assignments excel sheet
     * @param wb
     * @param formulaEvaluator
     * @param sheet
     * @param dtos
     */
    private void generateControlAssignmentsExcelSheet(Workbook wb, FormulaEvaluator formulaEvaluator, Sheet sheet,
            List<ControlAssignmentBulkUploadDto> dtos, Map<Long, ControlBulkUploadDto> controlMap,
            Map<Long, ControlPathBulkUploadDto> pathMap) {

        int currentRow = EXCEL_MAPPING_HEADER_ROWS;

        for (ControlAssignmentBulkUploadDto dto : dtos) {
            Row row = sheet.getRow(currentRow);

            if (dto.getControlPathId() != null) {
                row.getCell(2).setCellValue(pathMap.get(dto.getControlPathId()).getRow());
                row.getCell(3).setCellValue(pathMap.get(dto.getControlPathId()).getPathId());
            }
            if (dto.getControlId() != null) {
                row.getCell(4).setCellValue(controlMap.get(dto.getControlId()).getRow());
                row.getCell(5).setCellValue(controlMap.get(dto.getControlId()).getIdentifier());
            }
            if (dto.getControlPathChildId() != null) {
                row.getCell(6).setCellValue(pathMap.get(dto.getControlPathChildId()).getRow());
                row.getCell(7).setCellValue(pathMap.get(dto.getControlPathChildId()).getPathId());
            }
            setCellNumberValue(row.getCell(8), dto.getSequenceNumber());
            setCellNumberValue(row.getCell(9), dto.getPercentApportionment());

            currentRow++;

        }

    }

    /**
     * Map control pollutants into the control pollutant excel sheet
     * @param wb
     * @param formulaEvaluator
     * @param sheet
     * @param dtos
     */
    private void generateControlPollutantExcelSheet(Workbook wb, FormulaEvaluator formulaEvaluator, Sheet sheet,
            List<ControlPollutantBulkUploadDto> dtos, Map<Long, ControlBulkUploadDto> controlMap) {

        int currentRow = EXCEL_MAPPING_HEADER_ROWS;

        for (ControlPollutantBulkUploadDto dto : dtos) {
            Row row = sheet.getRow(currentRow);

            if (dto.getControlId() != null) {
                row.getCell(2).setCellValue(controlMap.get(dto.getControlId()).getRow());
                row.getCell(3).setCellValue(controlMap.get(dto.getControlId()).getIdentifier());
            }
            if (dto.getPollutantCode() != null) {
                row.getCell(4).setCellValue(dto.getPollutantCode());
                // check if the code is a number or not when looking it up
                this.pollutantRepo.findById(dto.getPollutantCode()).ifPresent(item -> row.getCell(5).setCellValue(item.getPollutantName()));
            }
            setCellNumberValue(row.getCell(6), dto.getPercentReduction());

            currentRow++;

        }

    }

    /**
     * Map apportionments into the apportionment excel sheet
     * @param wb
     * @param formulaEvaluator
     * @param sheet
     * @param dtos
     */
    private void generateApportionmentExcelSheet(Workbook wb, FormulaEvaluator formulaEvaluator, Sheet sheet,
            List<ReleasePointApptBulkUploadDto> dtos, Map<Long, ReleasePointBulkUploadDto> rpMap,
            Map<Long, EmissionsProcessBulkUploadDto> epMap, Map<Long, ControlPathBulkUploadDto> pathMap) {

        int currentRow = EXCEL_MAPPING_HEADER_ROWS;

        for (ReleasePointApptBulkUploadDto dto : dtos) {
            Row row = sheet.getRow(currentRow);

            if (dto.getReleasePointId() != null) {
                row.getCell(2).setCellValue(rpMap.get(dto.getReleasePointId()).getRow());
                row.getCell(3).setCellValue(rpMap.get(dto.getReleasePointId()).getReleasePointIdentifier());
            }
            if (dto.getEmissionProcessId() != null) {
                row.getCell(4).setCellValue(epMap.get(dto.getEmissionProcessId()).getRow());
                row.getCell(5).setCellValue(epMap.get(dto.getEmissionProcessId()).getDisplayName());
            }
            if (dto.getControlPathId() != null) {
                row.getCell(6).setCellValue(pathMap.get(dto.getControlPathId()).getRow());
                row.getCell(7).setCellValue(pathMap.get(dto.getControlPathId()).getPathId());
            }
            setCellNumberValue(row.getCell(8), dto.getPercent());

            currentRow++;

        }

    }

    /**
     * Map reporting periods into the reporting period excel sheet
     * @param wb
     * @param formulaEvaluator
     * @param sheet
     * @param dtos
     */
    private void generateReportingPeriodExcelSheet(Workbook wb, FormulaEvaluator formulaEvaluator, Sheet sheet,
            List<ReportingPeriodBulkUploadDto> dtos, Map<Long, EmissionsProcessBulkUploadDto> epMap) {

        int currentRow = EXCEL_MAPPING_HEADER_ROWS;

        for (ReportingPeriodBulkUploadDto dto : dtos) {
            Row row = sheet.getRow(currentRow);

            if (dto.getEmissionsProcessId() != null) {
                row.getCell(1).setCellValue(epMap.get(dto.getEmissionsProcessId()).getRow());
                row.getCell(2).setCellValue(epMap.get(dto.getEmissionsProcessId()).getDisplayName());
            }
            if (dto.getReportingPeriodTypeCode() != null) {
                row.getCell(5).setCellValue(dto.getReportingPeriodTypeCode());
                this.reportingPeriodCodeRepo.findById(dto.getReportingPeriodTypeCode()).ifPresent(item -> row.getCell(6).setCellValue(item.getShortName()));
            }
            if (dto.getEmissionsOperatingTypeCode() != null) {
                row.getCell(7).setCellValue(dto.getEmissionsOperatingTypeCode());
                this.emissionsOperatingTypeCodeRepo.findById(dto.getEmissionsOperatingTypeCode()).ifPresent(item -> row.getCell(8).setCellValue(item.getShortName()));
            }
            if (dto.getCalculationParameterTypeCode() != null) {
                row.getCell(9).setCellValue(dto.getCalculationParameterTypeCode());
                this.calcParamTypeCodeRepo.findById(dto.getCalculationParameterTypeCode()).ifPresent(item -> row.getCell(10).setCellValue(item.getDescription()));
            }
            row.getCell(11).setCellValue(dto.getCalculationParameterValue());
            row.getCell(12).setCellValue(dto.getCalculationParameterUom());
            if (dto.getCalculationMaterialCode() != null) {
                row.getCell(13).setCellValue(dto.getCalculationMaterialCode());
                this.calcMaterialCodeRepo.findById(dto.getCalculationMaterialCode()).ifPresent(item -> row.getCell(14).setCellValue(item.getDescription()));
            }
            row.getCell(15).setCellValue(dto.getFuelUseValue());
            row.getCell(16).setCellValue(dto.getFuelUseUom());
            if (dto.getFuelUseMaterialCode() != null) {
                row.getCell(17).setCellValue(dto.getFuelUseMaterialCode());
                this.calcMaterialCodeRepo.findById(dto.getFuelUseMaterialCode()).ifPresent(item -> row.getCell(18).setCellValue(item.getDescription()));
            }
            row.getCell(19).setCellValue(dto.getHeatContentValue());
            row.getCell(20).setCellValue(dto.getHeatContentUom());
            row.getCell(21).setCellValue(dto.getComments());

            currentRow++;

            dto.setRow(currentRow);
            // have to pull value from cell since we don't have this value anywhere else
            dto.setDisplayName(epMap.get(dto.getEmissionsProcessId()).getDisplayName() + "-" + row.getCell(6).getStringCellValue());

        }

    }

    /**
     * Map operating details into the operating details excel sheet
     * @param wb
     * @param formulaEvaluator
     * @param sheet
     * @param dtos
     */
    private void generateOperatingDetailExcelSheet(Workbook wb, FormulaEvaluator formulaEvaluator, Sheet sheet,
            List<OperatingDetailBulkUploadDto> dtos, Map<Long, ReportingPeriodBulkUploadDto> periodMap) {

        int currentRow = EXCEL_MAPPING_HEADER_ROWS;

        for (OperatingDetailBulkUploadDto dto : dtos) {
            Row row = sheet.getRow(currentRow);

            if (dto.getReportingPeriodId() != null) {
                row.getCell(2).setCellValue(periodMap.get(dto.getReportingPeriodId()).getRow());
                row.getCell(3).setCellValue(periodMap.get(dto.getReportingPeriodId()).getDisplayName());
            }
            setCellNumberValue(row.getCell(4), dto.getActualHoursPerPeriod());
            setCellNumberValue(row.getCell(5), dto.getAverageHoursPerDay());
            setCellNumberValue(row.getCell(6), dto.getAverageDaysPerWeek());
            setCellNumberValue(row.getCell(7), dto.getAverageWeeksPerPeriod());
            setCellNumberValue(row.getCell(8), dto.getPercentWinter());
            setCellNumberValue(row.getCell(9), dto.getPercentSpring());
            setCellNumberValue(row.getCell(10), dto.getPercentSummer());
            setCellNumberValue(row.getCell(11), dto.getPercentFall());

            currentRow++;

        }

    }

    /**
     * Map emissions into the emission excel sheet
     * @param wb
     * @param formulaEvaluator
     * @param sheet
     * @param dtos
     */
    private void generateEmissionExcelSheet(Workbook wb, FormulaEvaluator formulaEvaluator, Sheet sheet,
            List<EmissionBulkUploadDto> dtos, Map<Long, ReportingPeriodBulkUploadDto> periodMap) {

        int currentRow = EXCEL_MAPPING_HEADER_ROWS;

        for (EmissionBulkUploadDto dto : dtos) {
            Row row = sheet.getRow(currentRow);

            if (dto.getReportingPeriodId() != null) {
                row.getCell(1).setCellValue(periodMap.get(dto.getReportingPeriodId()).getRow());
                row.getCell(2).setCellValue(periodMap.get(dto.getReportingPeriodId()).getDisplayName());
            }
            if (dto.getPollutantCode() != null) {
                row.getCell(3).setCellValue(dto.getPollutantCode());
                // check if the code is a number or not when looking it up
                this.pollutantRepo.findById(dto.getPollutantCode()).ifPresent(item -> row.getCell(4).setCellValue(item.getPollutantName()));
            }
            row.getCell(5).setCellValue("" + dto.isTotalManualEntry());
            row.getCell(6).setCellValue("" + dto.getNotReporting());
            setCellNumberValue(row.getCell(7), dto.getTotalEmissions());
            row.getCell(8).setCellValue(dto.getEmissionsUomCode());
            setCellNumberValue(row.getCell(9), dto.getOverallControlPercent());
            // don't include EF for formula emissions
            if (Strings.emptyToNull(dto.getEmissionsFactorFormula()) == null) {
                setCellNumberValue(row.getCell(10), dto.getEmissionsFactor());
            }
            row.getCell(11).setCellValue(dto.getEmissionsFactorText());

            row.getCell(14).setCellValue(dto.getEmissionsFactorFormula());
            if (dto.getEmissionsCalcMethodCode() != null) {
                row.getCell(15).setCellValue(dto.getEmissionsCalcMethodCode());
                this.calcMethodCodeRepo.findById(dto.getEmissionsCalcMethodCode()).ifPresent(item -> row.getCell(16).setCellValue(item.getDescription()));
            }
            row.getCell(17).setCellValue(dto.getEmissionsNumeratorUom());
            row.getCell(18).setCellValue(dto.getEmissionsDenominatorUom());
            row.getCell(19).setCellValue(dto.getCalculationComment());
            row.getCell(20).setCellValue(dto.getComments());

            currentRow++;

            dto.setRow(currentRow);
            // have to pull value from cell since we don't have this value anywhere else
            dto.setDisplayName(String.format("%s (%s)", periodMap.get(dto.getReportingPeriodId()).getDisplayName(), row.getCell(4).getStringCellValue()));

        }

    }

    /**
     * Map emission formula variables into the emission formula variable excel sheet
     * @param wb
     * @param formulaEvaluator
     * @param sheet
     * @param dtos
     */
    private void generateEmissionFormulaVariableExcelSheet(Workbook wb, FormulaEvaluator formulaEvaluator, Sheet sheet,
            List<EmissionFormulaVariableBulkUploadDto> dtos, Map<Long, EmissionBulkUploadDto> emissionMap) {

        int currentRow = EXCEL_MAPPING_HEADER_ROWS;

        for (EmissionFormulaVariableBulkUploadDto dto : dtos) {
            Row row = sheet.getRow(currentRow);

            if (dto.getEmissionId() != null) {
                row.getCell(2).setCellValue(emissionMap.get(dto.getEmissionId()).getRow());
                row.getCell(4).setCellValue(emissionMap.get(dto.getEmissionId()).getDisplayName());
            }
            if (dto.getEmissionFormulaVariableCode() != null) {
                row.getCell(5).setCellValue(dto.getEmissionFormulaVariableCode());
                row.getCell(6).setCellFormula(generateLookupFormula(wb, "EmissionFormulaVariable", dto.getEmissionFormulaVariableCode(), true));
                formulaEvaluator.evaluateInCell(row.getCell(6));
            }
            setCellNumberValue(row.getCell(7), dto.getValue());

            currentRow++;

        }

    }

    /**
     * Set the value of a cell as a number for formatting
     * @param cell
     * @param value
     */
    private void setCellNumberValue(Cell cell, String value) {
        Double numVal = toDouble(value);
        if (numVal != null) {
            cell.setCellValue(numVal);
        }
    }

    /**
     * Generate a basic reverse lookup formula for creating excel exports.
     * The formula will find the dropdown value for a code in a basic lookup sheet in excel
     * @param workbook
     * @param sheetName
     * @param value the code to lookup
     * @param text if the code is text or number in excel
     * @return
     */
    private String generateLookupFormula(Workbook workbook, String sheetName, String value, boolean text) {

        int rowCount = workbook.getSheet(sheetName).getLastRowNum() + 1;
        String result;
        // if the code is a number in excel we need to make sure it's a number here too so it will match
        if (text) {
            String query = String.format(EXCEL_GENERIC_LOOKUP_TEXT, sheetName, rowCount, value, sheetName, rowCount);
            // leave field blank if invalid value
            result = String.format("IF(ISNA(%s), \"\", %s)", query, query);
        } else {
            String query = String.format(EXCEL_GENERIC_LOOKUP_NUMBER, sheetName, rowCount, value, sheetName, rowCount);
            // leave field blank if invalid value
            result = String.format("IF(ISNA(%s), \"\", %s)", query, query);
        }
//        logger.info(result);
        return result;
    }

    private void updateNamedRange(String sheetName, Name namedRange, String startColumn,
                                  String endColumn, int startRow, int endRow) {
        String namedRangeFormula = String.format("%s!$%s$%d:$%s$%d",
            sheetName, startColumn.toUpperCase(), startRow, endColumn.toUpperCase(), endRow);
        namedRange.setRefersToFormula(namedRangeFormula);
    }

    private Double toDouble(String strval) {

        return Strings.isNullOrEmpty(strval) ? null : Double.parseDouble(strval);
    }

    /***
	 * Sanitize UoMs for RPs based on type and if data is present
	 * @param releasePoint
	 * @return
	 */
    private ReleasePoint sanitizeReleasePointUoMs(ReleasePoint releasePoint) {
    	if (releasePoint.getExitGasFlowRate() == null) {
    		releasePoint.setExitGasFlowUomCode(null);
    	}
    	if (releasePoint.getExitGasVelocity() == null) {
    		releasePoint.setExitGasVelocityUomCode(null);
    	}
    	if (releasePoint.getFenceLineDistance() == null) {
    		releasePoint.setFenceLineUomCode(null);
    	}

    	if (releasePoint.getTypeCode().getCategory().equals("Fugitive")) {
    		releasePoint.setStackDiameter(null);
    		releasePoint.setStackDiameterUomCode(null);
    		releasePoint.setStackHeight(null);
    		releasePoint.setStackHeightUomCode(null);
    		releasePoint.setStackLength(null);
    		releasePoint.setStackLengthUomCode(null);
    		releasePoint.setStackWidth(null);
    		releasePoint.setStackWidthUomCode(null);

    		if (releasePoint.getFugitiveHeight() == null) {
    			releasePoint.setFugitiveHeightUomCode(null);
    		}
    		if (releasePoint.getFugitiveLength() == null) {
    			releasePoint.setFugitiveLengthUomCode(null);
    		}
    		if (releasePoint.getFugitiveWidth() == null) {
    			releasePoint.setFugitiveWidthUomCode(null);
    		}

    	} else {
    		releasePoint.setFugitiveHeight(null);
			releasePoint.setFugitiveHeightUomCode(null);
    		releasePoint.setFugitiveLength(null);
			releasePoint.setFugitiveLengthUomCode(null);
    		releasePoint.setFugitiveWidth(null);
			releasePoint.setFugitiveWidthUomCode(null);
			releasePoint.setFugitiveAngle(null);
			releasePoint.setFugitiveMidPt2Latitude(null);
			releasePoint.setFugitiveMidPt2Longitude(null);

			if (releasePoint.getStackDiameter() == null) {
    			releasePoint.setStackDiameterUomCode(null);
    		}
			if (releasePoint.getStackHeight() == null) {
    			releasePoint.setStackHeightUomCode(null);
    		}
			if (releasePoint.getStackLength() == null) {
    			releasePoint.setStackLengthUomCode(null);
    		}
			if (releasePoint.getStackWidth() == null) {
    			releasePoint.setStackWidthUomCode(null);
    		}
    	}

    	return releasePoint;
    }

    private String formatNaicsDropdown(String code, String description) {
        return String.format("%s (%s)", description, code);
    }

    private void checkUpdateBillingExempt(EmissionsReportJsonDto reportJsonDto, EmissionsReport report) {
        SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(report.getFacilitySites().get(0).getProgramSystemCode().getCode());

        // reset all billable values to null for slts that do not have the feature enabled
        // to prevent possibly getting weird/unexpected data
        if (!sltConfig.getSltFeatureBillingExemptEnabled()) {
            for (FacilitySiteJsonDto facSite : reportJsonDto.getFacilitySite()) {
                for (EmissionsUnitJsonDto unit : facSite.getEmissionsUnits()) {
                    for (EmissionsProcessJsonDto proc : unit.getEmissionsProcesses()) {
                        proc.setIsBillable(null);
                    }
                }
            }
        }
    }
}
