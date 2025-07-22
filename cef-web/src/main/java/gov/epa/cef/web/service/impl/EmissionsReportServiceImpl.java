/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.impl;

import gov.epa.cef.web.api.rest.EmissionsReportApi.ReviewDTO;
import gov.epa.cef.web.client.soap.DocumentDataSource;
import gov.epa.cef.web.client.soap.InboxClient;
import gov.epa.cef.web.client.soap.RegisterPdfClient;
import gov.epa.cef.web.client.soap.SignatureServiceClient;
import gov.epa.cef.web.config.AppPropertyName;
import gov.epa.cef.web.config.CefConfig;
import gov.epa.cef.web.config.SLTBaseConfig;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.exception.ApplicationException;
import gov.epa.cef.web.exception.NotExistException;
import gov.epa.cef.web.provider.system.AdminPropertyProvider;
import gov.epa.cef.web.repository.*;
import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.service.*;
import gov.epa.cef.web.service.dto.*;
import gov.epa.cef.web.service.mapper.*;
import gov.epa.cef.web.service.validation.ValidationField;
import gov.epa.cef.web.util.ConstantUtils;
import gov.epa.cef.web.util.ReportCreationContext;
import gov.epa.cef.web.util.SLTConfigHelper;
import gov.epa.cef.web.util.StringUtils;
import net.exchangenetwork.wsdl.register.pdf._1.PdfDocumentType;
import net.exchangenetwork.wsdl.register.sign._1.SignatureDocumentFormatType;
import net.exchangenetwork.wsdl.register.sign._1.SignatureDocumentType;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.io.ByteStreams;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.activation.DataHandler;
import javax.validation.constraints.NotBlank;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class EmissionsReportServiceImpl implements EmissionsReportService {

    private static final Logger logger = LoggerFactory.getLogger(EmissionsReportServiceImpl.class);

    @Autowired
    private AttachmentRepository attachmentsRepo;

    @Autowired
    private EmissionsReportRepository erRepo;

    @Autowired
    private FacilitySiteRepository fsRepo;

    @Autowired
    private MasterFacilityRecordRepository mfrRepo;

    @Autowired
    private AttachmentRepository reportAttachmentsRepo;

    @Autowired
    private ReportChangeRepository reportChangeRepo;

    @Autowired
    private ReportReviewRepository rrRepo;

    @Autowired
    private MasterFacilityNAICSXrefRepository mfNaicsXrefRepo;

    @Autowired
	private PointSourceSccCodeRepository sccRepo;

    @Autowired
	private UserFacilityAssociationRepository userFacilityRepo;

    @Autowired
    private EmissionsReportMapper emissionsReportMapper;

    @Autowired
    private MasterFacilityRecordMapper mfrMapper;

    @Autowired
    private MasterFacilityNAICSMapper mfNaicsMapper;

    @Autowired
    private FacilityNAICSMapper facilityNaicsMapper;

    @Autowired
    private ReportChangeMapper reportChangeMapper;

    @Autowired
    private LookupEntityMapper lookupMapper;

    @Autowired
    private CefConfig cefConfig;

    @Autowired
    private SLTConfigHelper sltConfigHelper;

    @Autowired
    private SignatureServiceClient signatureServiceClient;

    @Autowired
    private InboxClient inboxClient;

    @Autowired
    private RegisterPdfClient pdfClient;

    @Autowired
    private CersXmlService cersXmlService;

    @Autowired
    private MasterFacilityRecordServiceImpl mfrService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportingPeriodService rpService;

    @Autowired
    private OperatingDetailService opDetailService;

    @Autowired
    private EmissionsReportStatusService statusService;

    @Autowired
    private UserFeedbackService userFeedbackService;

    @Autowired
    private AdminPropertyProvider propertyProvider;

    @Autowired
    private EmissionService emissionService;

    @Autowired
	private UserFacilityAssociationServiceImpl userFacilityAssocService;

    @Autowired
	private UserService userService;

    @Autowired
    private LookupService lookupService;

    @Autowired
    private CorServiceImpl corService;

    private static final String CERS_V1_XMLNS = "http://www.exchangenetwork.net/schema/cer/1";
    private static final String CERS_V2_XMLNS = "http://www.exchangenetwork.net/schema/cer/2";
    private static final String CERS_V1_XSLT = "/schema/CER_v1_StyleSheet.xslt";
    private static final String CERS_V2_XSLT = "/schema/CER_v2_StyleSheet.xslt";

    /* (non-Javadoc)
     * @see gov.epa.cef.web.service.impl.ReportService#findByFacilityId(java.lang.String)
     */
    @Override
    public List<EmissionsReportDto> findByMasterFacilityRecordId(Long masterFacilityRecordId) {
        return findByMasterFacilityRecordId(masterFacilityRecordId, false, false);
    }


    /* (non-Javadoc)
     * @see gov.epa.cef.web.service.impl.ReportService#findByFacilityId(java.lang.String)
     */
    @Override
    public List<EmissionsReportDto> findByMasterFacilityRecordId(Long masterFacilityRecordId, boolean addReportForCurrentReportYear, boolean addReportForCurrentCalendarYear) {
        List<EmissionsReport> emissionReports= erRepo.findByMasterFacilityRecordId(masterFacilityRecordId);

        if (addReportForCurrentReportYear) {
        	Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());

            //note: current reporting year is always the previous calendar year - like taxes.
            //e.g. in 2020, facilities will be creating a 2019 report.
            calendar.add(Calendar.YEAR, -1);
            short currentReportingYear = (short) calendar.get(Calendar.YEAR);

            addReportForYear(emissionReports, masterFacilityRecordId, currentReportingYear);
        }

        if (addReportForCurrentCalendarYear) {
        	Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());

            short currentYear = (short) calendar.get(Calendar.YEAR);

            addReportForYear(emissionReports, masterFacilityRecordId, currentYear);
        }

        List<EmissionsReportDto> dtoList = emissionsReportMapper.toDtoList(emissionReports);
        return dtoList;
    }


	/* (non-Javadoc)
     * @see gov.epa.cef.web.service.impl.ReportService#findById(java.lang.Long)
     */
    @Override
    public EmissionsReportDto findById(Long id) {

        return erRepo.findById(id)
            .map(report -> emissionsReportMapper.toDto(report))
            .orElse(null);
    }

    @Override
    public Optional<EmissionsReport> retrieve(long id) {

        return erRepo.findById(id);
    }

    @Override
    public Optional<EmissionsReport> retrieveByMasterFacilityRecordIdAndYear(@NotBlank Long masterFacilityRecordId, int year) {

        return erRepo.findByMasterFacilityRecordIdAndYear(masterFacilityRecordId, Integer.valueOf(year).shortValue());
    }

    public EmissionsReportDto retrieveReportByMasterFacilityRecordIdAndYear(Long masterFacilityRecordId, int year) {

        return retrieveByMasterFacilityRecordIdAndYear(masterFacilityRecordId, year)
            .map(emissionsReport -> emissionsReportMapper.toDto(emissionsReport))
            .orElse(null);
    }

    /* (non-Javadoc)
     * @see gov.epa.cef.web.service.impl.ReportService#findMostRecentByFacility(java.lang.String)
     */
    @Override
    public EmissionsReportDto findMostRecentByMasterFacilityRecordId(Long masterFacilityRecordId) {

        return findMostRecentEmissionsReport(masterFacilityRecordId)
            .map(emissionsReport -> emissionsReportMapper.toDto(emissionsReport))
            .orElse(null);
    }

    /**
     * Find all agencies with reports and which years they have reports for
     */
    public List<EmissionsReportAgencyDataDto> findAgencyReportedYears() {

        List<EmissionsReportAgencyDataDto> result = erRepo.findDistinctProgramSystems()
                .stream()
                .map(agency -> {
                    return new EmissionsReportAgencyDataDto()
                            .withProgramSystemCode(this.lookupMapper.toDto(agency))
                            .withYears(erRepo.findDistinctReportingYears(agency.getCode()));
                }).collect(Collectors.toList());
        return result;
    }

    /**
     * Find all agencies with monthly reporting enabled with reports and which years they have reports for
     */
    public List<EmissionsReportAgencyDataDto> findAgencyMonthlyReportedYears() {
    	List<EmissionsReportAgencyDataDto> result = erRepo.findDistinctProgramSystems()
                .stream()
                .filter(agency -> sltConfigHelper.getAllSltByMonthlyEnabled().contains(agency.getCode()))
                .map(agency -> {
                    return new EmissionsReportAgencyDataDto()
                            .withProgramSystemCode(this.lookupMapper.toDto(agency))
                            .withYears(erRepo.findDistinctReportingYearsWithMonthlyReportingEnabled(agency.getCode()));
                }).collect(Collectors.toList());
        return result;
    }

    public Boolean readyToCertifyNotification(Long emissionsReportId, boolean isSemiannual) {
        EmissionsReport emissionsReport = erRepo.findById(emissionsReportId).orElse(null);
        MasterFacilityRecord mfr = emissionsReport.getMasterFacilityRecord();
        SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(emissionsReport.getProgramSystemCode().getCode());
        UserDto cdxUser = userService.getCurrentUser();

        reportService.createReportHistory(emissionsReportId, ReportAction.READY_TO_CERTIFY);

        List<String> emailList = new ArrayList<>();
        List<UserFacilityAssociation> userList = this.userFacilityRepo.findByMasterFacilityRecordIdAndActive(mfr.getId(), true);
        List<UserFacilityAssociationDto> filteredUserList = userFacilityAssocService.mapAssociations(userList).stream()
            .filter(role -> role.getRoleDescription().equals(AppRole.RoleType.NEI_CERTIFIER.roleName())).collect(Collectors.toList());

        HashMap<String, Object> varMap = new HashMap<String, Object>();
        varMap.put("reportingYear", emissionsReport.getYear().toString());
        varMap.put("facilityName", emissionsReport.getFacilitySites().get(0).getName());

        String copiedEmailsField = cdxUser.getEmail();
        if (Boolean.TRUE.equals(sltConfig.getSLTEmailReadyToCertifyEnabled())) {
            copiedEmailsField += ", " + sltConfig.getSltEmail();
        }

        if (isSemiannual) {
            for (UserFacilityAssociationDto user : filteredUserList) {
                emailList.add(user.getEmail());

                // Send email to all NEI Certifier CDX Inboxes for Facility
                notificationService.sendCdxInboxMessage(CdxInboxMessageType.SEMIANNUAL_REPORT_READY_FOR_CERT_SUBJECT, user.getCdxUserId(), cefConfig.getDefaultEmailAddress(), varMap);
            }
            // Send email to CDX Inbox of Preparer who is sending notification
            notificationService.sendCdxInboxMessage(CdxInboxMessageType.SEMIANNUAL_REPORT_READY_FOR_CERT_SUBJECT, cdxUser.getCdxUserId(), cefConfig.getDefaultEmailAddress(), varMap);

            // Send an email notification to the certifiers associated with master facility report is ready to be certified,
            // CC email preparer who is sending notification and the SLT if the SLT Property is enabled.
            return notificationService.sendMidYearReportReadyForCertificationNotification(
                emailList.toString().substring(1, emailList.toString().length() - 1),
                copiedEmailsField,
                cefConfig.getDefaultEmailAddress(),
                emissionsReport.getFacilitySites().get(0).getName(),
                emissionsReport.getYear().toString());
        }
        else {
            for (UserFacilityAssociationDto user : filteredUserList) {
                emailList.add(user.getEmail());

                // Send email to all NEI Certifier CDX Inboxes for Facility
                notificationService.sendCdxInboxMessage(CdxInboxMessageType.REPORT_READY_FOR_CERT_SUBJECT, user.getCdxUserId(), cefConfig.getDefaultEmailAddress(), varMap);
            }
            // Send email to CDX Inbox of Preparer who is sending notification
            notificationService.sendCdxInboxMessage(CdxInboxMessageType.REPORT_READY_FOR_CERT_SUBJECT, cdxUser.getCdxUserId(), cefConfig.getDefaultEmailAddress(), varMap);

            // Send an email notification TO the certifiers associated with master facility report is ready to be certified,
            // CC email preparer who is sending notification and the SLT if the SLT Property is enabled.
            return notificationService.sendReportReadyForCertificationNotification(
                emailList.toString().substring(1, emailList.toString().length() - 1),
                copiedEmailsField,
                cefConfig.getDefaultEmailAddress(),
                emissionsReport.getFacilitySites().get(0).getName(),
                emissionsReport.getYear().toString());
        }
    }

    @Override
    public String submitToCromerr(Long emissionsReportId, String activityId, boolean isSemiannual) throws ApplicationException {
        String cromerrDocumentId=null;
        File tmp = null;
        try {
            Optional<EmissionsReport> emissionsReportOptional=erRepo.findById(emissionsReportId);
            if(emissionsReportOptional.isPresent()) {

            	DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            	String datestring = df.format(Calendar.getInstance().getTime());
                String fileName = StringUtils.createValidLongFileName(emissionsReportOptional.get().getMasterFacilityRecord().getAgencyFacilityIdentifier(),
                        emissionsReportOptional.get().getMasterFacilityRecord().getName(),
                        emissionsReportOptional.get().getYear(),
                        datestring);

                URL signatureServiceUrl = new URL(cefConfig.getCdxConfig().getRegisterSignServiceEndpoint());
                String signatureToken = signatureServiceClient.authenticate(signatureServiceUrl, cefConfig.getCdxConfig().getNaasUser(), cefConfig.getCdxConfig().getNaasPassword());
                SignatureDocumentType sigDoc = new SignatureDocumentType();
                sigDoc.setName(String.format("%s.zip", fileName));
                sigDoc.setFormat(SignatureDocumentFormatType.BIN);

                tmp = File.createTempFile("Submission", ".zip");

                try (FileOutputStream fos = new FileOutputStream(tmp);
                     ZipOutputStream zos = new ZipOutputStream(fos)) {

                    ZipEntry zipEntry = new ZipEntry(String.format("%s.html", fileName));
                    zos.putNextEntry(zipEntry);

                    String corHTML = generateHTMLCopyOfRecord(emissionsReportId, isSemiannual, true);
                    zos.write(corHTML.getBytes(StandardCharsets.UTF_8));

                    emissionsReportOptional.get().getReportAttachments().forEach(item -> {
                        try (InputStream inputStream = item.getAttachment().getBinaryStream()) {

                            ZipEntry attachmentEntry = new ZipEntry(item.getFileName());
                            zos.putNextEntry(attachmentEntry);

                            ByteStreams.copy(inputStream, zos);

                        } catch (SQLException | IOException e) {
                            throw new IllegalStateException(e);
                        }
                    });

                    zos.closeEntry();

                    zos.finish();

                } catch (FileNotFoundException e) {

                    throw new IllegalStateException(e);
                } catch (IOException e) {

                    throw new IllegalStateException(e);
                }

                sigDoc.setContent(new DataHandler(new DocumentDataSource(tmp, "application/octet-stream")));
                cromerrDocumentId =
                    signatureServiceClient.sign(signatureServiceUrl, signatureToken, activityId, sigDoc);
                // get fresh copy of emissionsReport to make sure any modifications from creating the XML are cleared
                EmissionsReport emissionsReport = erRepo.findById(emissionsReportId).get();
                if (isSemiannual) {
                    emissionsReport.setMidYearSubmissionStatus(ReportStatus.SUBMITTED);
                }
                else {
                    emissionsReport.setStatus(ReportStatus.SUBMITTED);
                }
                emissionsReport.setCromerrActivityId(activityId);
                emissionsReport.setCromerrDocumentId(cromerrDocumentId);

                // calculate standardized non-point fuel use
                for (EmissionsUnit eu : emissionsReport.getFacilitySites().get(0).getEmissionsUnits()) {
                	for (EmissionsProcess ep : eu.getEmissionsProcesses()) {
                		for (ReportingPeriod rp : ep.getReportingPeriods()) {
                			rp.setStandardizedNonPointFuelUse(rpService.calculateFuelUseNonPointStandardized(rp));
                		}
                	}
                }

                erRepo.save(emissionsReport);

                // update MFR record with site changes
                MasterFacilityRecord mfr = emissionsReport.getMasterFacilityRecord();
                FacilitySite fs = emissionsReport.getFacilitySites().get(0);

                SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(emissionsReport.getProgramSystemCode().getCode());
                UserDto cdxUser = userService.getCurrentUser();
                // update Master Facility NAICS with Facility Site NAICS if edit NAICs is enabled for certifiers and preparers
                if (Boolean.FALSE.equals(sltConfig.getFacilityNaicsEnabled())) {
	                mfNaicsXrefRepo.deleteByMasterFacilityId(mfr.getId());

	                for (FacilityNAICSXref fsNaics: fs.getFacilityNAICS()) {
	                	MasterFacilityNAICSXref mfNaics;
		                mfNaics = mfNaicsMapper.toMasterFacilityNaicsXref(fsNaics);
		                mfNaics.setMasterFacilityRecord(mfr);
		                mfr.getMasterFacilityNAICS().add(mfNaics);
	                }
                }

                mfrService.updateMasterFacilityRecord(mfr, fs);

                if (this.propertyProvider.getBoolean(AppPropertyName.FeatureFacilityAutomatedEmailEnabled)) {
	                  String cdxSubmissionUrl = cefConfig.getCdxConfig().getSubmissionHistoryUrl() + activityId;

                    //add vars to map
                    HashMap<String, Object> varMap = new HashMap<String, Object>();
                    varMap.put("reportingYear", emissionsReport.getYear().toString());
                    varMap.put("facilityName", emissionsReport.getFacilitySites().get(0).getName());
                    varMap.put("agencyFacilityId", emissionsReport.getFacilitySites().get(0).getAgencyFacilityIdentifier());
                    varMap.put("sltEmail", sltConfig.getSltEmail());
                    varMap.put("slt", sltConfig.getSltEisProgramCode());
                    varMap.put("cdxSubmissionUrl", cdxSubmissionUrl);

	                  // Send an email notification To the certifier, CC email list contains all emails of active certifiers and preparers associated with master facility,
	                  // and SLT's predefined address that a report has been submitted. Does not include the certifier submitting.
                    if (isSemiannual) {
                        notificationService.sendMidYearReportSubmittedNotification(
                            cdxUser.getEmail(),
                            getFacilityPreparerAndCertifierEmails(mfr.getId(), cdxUser.getCdxUserId(), sltConfig.getSltEmail()),
                            cefConfig.getDefaultEmailAddress(),
                            emissionsReport.getFacilitySites().get(0).getName(),
                            emissionsReport.getFacilitySites().get(0).getAgencyFacilityIdentifier(),
                            emissionsReport.getYear().toString(),
                            sltConfig.getSltEisProgramCode(),
                            sltConfig.getSltEmail(),
                            cdxSubmissionUrl);

                        notificationService.sendCdxInboxMessage(CdxInboxMessageType.SEMIANNUAL_REPORT_SUBMITTED, cdxUser.getCdxUserId(),
                            cefConfig.getDefaultEmailAddress(), varMap);
                    }
                    else {
                        notificationService.sendReportSubmittedNotification(
                        cdxUser.getEmail(),
                        getFacilityPreparerAndCertifierEmails(mfr.getId(), cdxUser.getCdxUserId(), sltConfig.getSltEmail()),
                        cefConfig.getDefaultEmailAddress(),
                        emissionsReport.getFacilitySites().get(0).getName(),
                        emissionsReport.getFacilitySites().get(0).getAgencyFacilityIdentifier(),
                        emissionsReport.getYear().toString(),
                        sltConfig.getSltEisProgramCode(),
                        sltConfig.getSltEmail(),
                        cdxSubmissionUrl);

                        notificationService.sendCdxInboxMessage(CdxInboxMessageType.REPORT_SUBMITTED, cdxUser.getCdxUserId(),
                            cefConfig.getDefaultEmailAddress(), varMap);
                    }
                }
            }
            return cromerrDocumentId;
        } catch(IOException e) {
            logger.error("submitToCromerr - {}", e.getMessage());
            throw ApplicationException.asApplicationException(e);
        } finally {
            if(tmp!=null) {
                FileUtils.deleteQuietly(tmp);
            }
        }
    }

    /**
     * Create a copy of the emissions report for the current year based on the specified facility and year.  The copy of the report is NOT saved to the database.
     * @param facilityEisProgramId
     * @param reportYear The year of the report that is being created
     * @return
     */
    @Override
    @Transactional
    public EmissionsReportDto createEmissionReportCopy(EmissionsReportStarterDto reportDto) {

        return findMostRecentEmissionsReportWithEmissions(reportDto.getMasterFacilityRecordId(), reportDto.getYear())
            .map(mostRecentReport -> {

                Optional<EmissionsReport> currentYearReport = this.retrieveByMasterFacilityRecordIdAndYear(reportDto.getMasterFacilityRecordId(),
                    reportDto.getYear());

                ReportCreationContext context = new ReportCreationContext(ConstantUtils.REPORT_CREATION_CHANGE_BUNDLE);
                EmissionsReport cloneReport = new EmissionsReport(mostRecentReport, context);

                cloneReport.setYear(reportDto.getYear());
                cloneReport.setStatus(ReportStatus.IN_PROGRESS);
                cloneReport.setValidationStatus(ValidationStatus.UNVALIDATED);
                cloneReport.setHasSubmitted(false);
                cloneReport.setReturnedReport(false);
                cloneReport.setEisLastSubmissionStatus(EisSubmissionStatus.NotStarted);

                SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(cloneReport.getMasterFacilityRecord().getProgramSystemCode().getCode());
                boolean monthlyFuelReportingEnabled = monthlyReportingEnabledAndYearCheck(sltConfig, cloneReport);

                if (Boolean.TRUE.equals(monthlyFuelReportingEnabled)) {
                    cloneReport.setMidYearSubmissionStatus(ReportStatus.IN_PROGRESS);
                }

                cloneReport.getFacilitySites().forEach(fs -> {
                    FacilitySite mfrFs = this.mfrMapper.toFacilitySite(cloneReport.getMasterFacilityRecord());

                    if (context != null && !(fs.isSameFacilityInfo(mfrFs) && fs.isSameMailingInfo(mfrFs))) {
                        context.addCreationChange(ValidationField.FACILITY_ADDRESS, "facilitysite.address.updated", fs, ReportChangeType.UPDATE);
                    }

                    this.mfrMapper.updateFacilitySite(cloneReport.getMasterFacilityRecord(), fs);

                    List<String> sccReqFuelAndAsphalt = sccRepo.findByMonthlyReporting(true).stream().map(scc -> scc.getCode()).distinct()
                        .collect(Collectors.toList());

                    if (context != null) {
                        List<MasterFacilityNAICSXref> mfrNaics = cloneReport.getMasterFacilityRecord().getMasterFacilityNAICS();

                        // find NAICS from the facility that aren't on the MFR (will be removed)
                        fs.getFacilityNAICS().stream()
                            .filter(fn -> mfrNaics.stream().noneMatch(mn -> mn.getNaicsCode().equals(fn.getNaicsCode())))
                            .forEach(fn -> {
                                context.addCreationChange(ValidationField.FACILITY_NAICS, "facilitysite.naics.deleted", fs,
                                    ReportChangeType.DELETE, fn.getNaicsCode().getCode().toString());
                            });

                        // find NAICS that are present in both, but as different types
                        mfrNaics.stream()
                            .filter(mn -> fs.getFacilityNAICS().stream().anyMatch(fn -> fn.getNaicsCode().equals(mn.getNaicsCode()) && !fn.getNaicsCodeType().equals(mn.getNaicsCodeType())))
                            .forEach(mn -> {
                                context.addCreationChange(ValidationField.FACILITY_NAICS, "facilitysite.naics.updated", fs, ReportChangeType.UPDATE,
                                    mn.getNaicsCode().getCode().toString(), mn.getNaicsCodeType());
                            });

                        // find NAICS from the MFR that aren't on the facility (will be added)
                        mfrNaics.stream()
                            .filter(mn -> fs.getFacilityNAICS().stream().noneMatch(fn -> fn.getNaicsCode().equals(mn.getNaicsCode())))
                            .forEach(mn -> {
                                context.addCreationChange(ValidationField.FACILITY_NAICS, "facilitysite.naics.added", fs,
                                    ReportChangeType.CREATE, mn.getNaicsCode().getCode().toString());
                            });
                    }

                    fs.getFacilityNAICS().clear();
                    for (MasterFacilityNAICSXref masterFacilityNAICS : cloneReport.getMasterFacilityRecord().getMasterFacilityNAICS()) {
                        FacilityNAICSXref facilityNAICS;
                        facilityNAICS = facilityNaicsMapper.toFacilityNaicsXref(masterFacilityNAICS);
                        facilityNAICS.setFacilitySite(fs);
                        fs.getFacilityNAICS().add(facilityNAICS);
                    }

                    // list of reporting period months for use later on
                    List<ReportingPeriodCode> rpcs = lookupService.findReportingPeriodMonthAndSemiAnnualCodes();
                    Short reportYear = fs.getEmissionsReport().getYear();

                    for (EmissionsUnit eu : fs.getEmissionsUnits()) {
                        for (EmissionsProcess ep : eu.getEmissionsProcesses()) {

                            boolean isFuelScc = sccReqFuelAndAsphalt.contains(ep.getSccCode());

                            for (ReportingPeriod rp : ep.getReportingPeriods()) {

                                if (monthlyFuelReportingEnabled && isFuelScc) {
                                    rp.setFuelUseValue(null);
                                }

                                for (OperatingDetail detail : rp.getOperatingDetails()) {

                                    if (detail.getAvgHoursPerDay() != null && detail.getAvgDaysPerWeek() != null && detail.getAvgWeeksPerPeriod() != null) {

                                        BigDecimal calculatedTotalHours = detail.getAvgHoursPerDay().multiply(detail.getAvgDaysPerWeek())
                                            .multiply(new BigDecimal(detail.getAvgWeeksPerPeriod().toString()));

                                        // actualHoursPerPeriod null-check fixes null-pointer
                                        if (detail.getActualHoursPerPeriod() == null || calculatedTotalHours != detail.getActualHoursPerPeriod()) {

                                            detail.setActualHoursPerPeriod(calculatedTotalHours);

                                            if (context != null) {
                                                context.addCreationChange(ValidationField.DETAIL_ACT_HR_PER_PERIOD,
                                                    "operatingDetail.actualHoursPerPeriod.recalculated", detail, ReportChangeType.UPDATE);
                                            }
                                        }
                                    }
                                }

                                if (ThresholdStatus.OPERATING_BELOW_THRESHOLD.equals(reportDto.getThresholdStatus())) {
                                    rp.getEmissions().clear();
                                } else {
                                    for (Emission e : rp.getEmissions()) {
                                        e = emissionService.updateEmissionFactorDetails(e, ep, reportYear, context);
                                    }
                                }
                            }
                            // if monthly reporting is enabled, and process scc code is a monthly reporting scc
                            // add a reporting period for each month to each process
                            if (monthlyFuelReportingEnabled && isFuelScc) {

                                // only create monthly reporting periods when annual exists
                                List<ReportingPeriod> currentRps = ep.getReportingPeriods();
                                if (currentRps != null && !currentRps.isEmpty()) {
                                    ReportingPeriod rp = currentRps.get(0);
                                    List<ReportingPeriod> monthlyRpsToAdd = new ArrayList<>();

                                    for (ReportingPeriodCode rpc : rpcs) {
                                        ReportingPeriod newRp = new ReportingPeriod(ep, rp);

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

                                        monthlyRpsToAdd.add(newRp);
                                    }
                                    ep.getReportingPeriods().addAll(monthlyRpsToAdd);
                                }
                            }
                        }
                    }
                });

                if (reportDto.getThresholdStatus() != null) {
                    if (ThresholdStatus.PERM_SHUTDOWN.equals(reportDto.getThresholdStatus())) {
                        cloneReport.setThresholdStatus(ThresholdStatus.PERM_SHUTDOWN);
                        updateFacilityStatus(cloneReport.getFacilitySites().get(0), new OperatingStatusCode().withCode("PS"), cloneReport.getYear(), mostRecentReport.getFacilitySites().get(0).getOperatingStatusCode());
                        cloneReport.setValidationStatus(ValidationStatus.PASSED);
                    } else if (ThresholdStatus.TEMP_SHUTDOWN.equals(reportDto.getThresholdStatus())) {
                        cloneReport.setThresholdStatus(ThresholdStatus.TEMP_SHUTDOWN);
                        updateFacilityStatus(cloneReport.getFacilitySites().get(0), new OperatingStatusCode().withCode("TS"), cloneReport.getYear(), mostRecentReport.getFacilitySites().get(0).getOperatingStatusCode());
                        cloneReport.setValidationStatus(ValidationStatus.PASSED);
                    } else if (ThresholdStatus.OPERATING_BELOW_THRESHOLD.equals(reportDto.getThresholdStatus())) {
                        cloneReport.setThresholdStatus(ThresholdStatus.OPERATING_BELOW_THRESHOLD);
                        updateFacilityStatus(cloneReport.getFacilitySites().get(0), new OperatingStatusCode().withCode("OP"), cloneReport.getYear(), mostRecentReport.getFacilitySites().get(0).getOperatingStatusCode());
                        cloneReport.setValidationStatus(ValidationStatus.PASSED);
                    } else if (ThresholdStatus.OPERATING_ABOVE_THRESHOLD.equals(reportDto.getThresholdStatus())) {
                        cloneReport.setThresholdStatus(ThresholdStatus.OPERATING_ABOVE_THRESHOLD);
                        updateFacilityStatus(cloneReport.getFacilitySites().get(0), new OperatingStatusCode().withCode("OP"), cloneReport.getYear(), mostRecentReport.getFacilitySites().get(0).getOperatingStatusCode());
                    }
                }

                cloneReport.clearId();

                // if there is already a report for the current year and data for the previous, we are deleting and resetting the report to the previous report data
                if (currentYearReport.isPresent() && currentYearReport.get().getYear().equals(reportDto.getYear())) {
                    for (ReportHistory rh : currentYearReport.get().getReportHistory()) {
                        ReportHistory newReportHistory = new ReportHistory(cloneReport, rh);
                        newReportHistory.clearId();
                        // break the link for any prior attachments before resetting the report
                        if (newReportHistory.getReportAction().equals(ReportAction.ATTACHMENT)) {
                            newReportHistory.setFileDeleted(true);
                        }
                        cloneReport.getReportHistory().add(newReportHistory);
                    }
                    this.erRepo.save(cloneReport);
                    this.reportService.createReportHistory(cloneReport.getId(), ReportAction.DELETED);
                    markReportForDeletion(currentYearReport.get().getId());
                }

                if (cloneReport.getThresholdStatus() != null && cloneReport.getThresholdStatus().equals(ThresholdStatus.OPERATING_BELOW_THRESHOLD)) {
                	this.reportService.createReportHistory(this.emissionsReportMapper.toDto(this.erRepo.save(cloneReport)).getId(), ReportAction.OPTED_OUT);
                }
                else {
                	this.reportService.createReportHistory(this.emissionsReportMapper.toDto(this.erRepo.save(cloneReport)).getId(), ReportAction.COPIED_FWD);
                }

            	if (context != null) {
            	    this.reportChangeRepo.saveAll(context.buildReportChanges(cloneReport));
            	}

                return this.emissionsReportMapper.toDto(this.erRepo.save(cloneReport));
            })
            .orElse(null);

    }

    private void updateFacilityStatus(FacilitySite facilitySite, OperatingStatusCode code, Short tempStatusYear, OperatingStatusCode mostRecentReport) {
        if(!code.getCode().contentEquals(ConstantUtils.STATUS_OPERATING)
                || (code.getCode().contentEquals(ConstantUtils.STATUS_OPERATING) && !mostRecentReport.equals(ConstantUtils.STATUS_OPERATING))) {

            if(code.getCode().contentEquals(ConstantUtils.STATUS_OPERATING)
                    || code.getCode().contentEquals(ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN)
                    || code.getCode().contentEquals(ConstantUtils.STATUS_TEMPORARILY_SHUTDOWN)) {

                facilitySite.getEmissionsUnits().forEach(unit -> {
                    if(!unit.getOperatingStatusCode().getCode().contentEquals("PS")){
                        unit.setOperatingStatusCode(code);
                        unit.setStatusYear(tempStatusYear);
                        unit.getEmissionsProcesses().forEach(process -> {
                            if(!process.getOperatingStatusCode().getCode().contentEquals("PS")){
                                process.setOperatingStatusCode(code);
                                process.setStatusYear(tempStatusYear);
                            }
                        });
                    }
                });

                facilitySite.getControls().forEach(control -> {
                    if(!control.getOperatingStatusCode().getCode().contentEquals("PS")){
                        control.setOperatingStatusCode(code);
                        control.setStatusYear(tempStatusYear);
                    }
                });

                facilitySite.getReleasePoints().forEach(releasePoint -> {
                    if(!releasePoint.getOperatingStatusCode().getCode().contentEquals("PS")){
                        releasePoint.setOperatingStatusCode(code);
                        releasePoint.setStatusYear(tempStatusYear);
                    }
                });
            }
        }

        facilitySite.setOperatingStatusCode(code);
        facilitySite.setStatusYear(tempStatusYear);
    }

    public EmissionsReportDto createEmissionReport(EmissionsReportStarterDto reportDto) {

        Optional<EmissionsReport> currentYearReport = this.retrieveByMasterFacilityRecordIdAndYear(reportDto.getMasterFacilityRecordId(),
            reportDto.getYear());

    	MasterFacilityRecord mfr = this.mfrRepo.findById(reportDto.getMasterFacilityRecordId())
           .orElseThrow(() -> new NotExistException("Master Facility Record", reportDto.getMasterFacilityRecordId()));

    	SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(mfr.getProgramSystemCode().getCode());

        EmissionsReport newReport = new EmissionsReport();
        newReport.setEisProgramId(mfr.getEisProgramId());
        newReport.setMasterFacilityRecord(mfr);
        newReport.setYear(reportDto.getYear());
        newReport.setStatus(ReportStatus.IN_PROGRESS);
        newReport.setValidationStatus(ValidationStatus.UNVALIDATED);
        newReport.setEisLastSubmissionStatus(EisSubmissionStatus.NotStarted);
        newReport.setHasSubmitted(false);
        newReport.setReturnedReport(false);
        newReport.setThresholdStatus(reportDto.getThresholdStatus());
        newReport.setDeleted(false);

        if (monthlyReportingEnabledAndYearCheck(sltConfig, newReport)) {
        	newReport.setMidYearSubmissionStatus(ReportStatus.IN_PROGRESS);
        }

        FacilitySite facilitySite = this.mfrMapper.toFacilitySite(mfr);
        facilitySite.setEmissionsReport(newReport);

        newReport.setProgramSystemCode(facilitySite.getProgramSystemCode());

        newReport.getFacilitySites().add(facilitySite);

        EmissionsReport report = this.erRepo.save(newReport);

        if (Boolean.TRUE.equals(sltConfig.getFacilityNaicsEnabled())) {
	        for (FacilitySite fs : report.getFacilitySites()) {
		        for (MasterFacilityNAICSXref masterFacilityNAICS : mfr.getMasterFacilityNAICS()) {
		        	FacilityNAICSXref facilityNAICS;
		        	facilityNAICS = facilityNaicsMapper.toFacilityNaicsXref(masterFacilityNAICS);
		            facilityNAICS.setFacilitySite(fs);

		            fs.getFacilityNAICS().add(facilityNAICS);
		        }
	        }
        }

        // if there is already a report for the current year and no previous report data, copy over the report history and
        // delete the existing current year report so it can be overwritten without duplication
        if (currentYearReport.isPresent() && currentYearReport.get().getYear().equals(reportDto.getYear())) {
            for (ReportHistory rh : currentYearReport.get().getReportHistory()) {
                ReportHistory newReportHistory = new ReportHistory(newReport, rh);
                newReportHistory.clearId();
                // break the link for any prior attachments before resetting the report
                if (newReportHistory.getReportAction().equals(ReportAction.ATTACHMENT)) {
                    newReportHistory.setFileDeleted(true);
                }

                newReport.getReportHistory().add(newReportHistory);
            }
            this.erRepo.save(newReport);
            this.reportService.createReportHistory(newReport.getId(), ReportAction.DELETED);
            markReportForDeletion(currentYearReport.get().getId());
        }

        if (mfr.getOperatingStatusCode().getCode().equals("ONRE")) {
          return saveAndAuditEmissionsReport(report, ReportAction.MARKED_ONRE);
        }
        else {
          return saveAndAuditEmissionsReport(report, ReportAction.CREATED);
        }
    }

    @Override
    public EmissionsReportDto saveAndAuditEmissionsReport(EmissionsReport emissionsReport, ReportAction reportAction) {

    	EmissionsReport result = this.erRepo.save(emissionsReport);

      logger.debug("Report {} {}.", result.getId(), reportAction.label());

      this.reportService.createReportHistory(result.getId(), reportAction);

      return this.emissionsReportMapper.toDto(result);
    }

    @Override
    public EmissionsReportDto saveAndAuditEmissionsReport(EmissionsReport emissionsReport, ReportAction reportAction, ReportCreationContext context) {

        // if updating an emissions report, save the new facility site first so that Hibernate doesn't lose object refs
        if (emissionsReport.getId() != null && !emissionsReport.getFacilitySites().isEmpty()) {
            this.fsRepo.save(emissionsReport.getFacilitySites().get(0));
        }
        EmissionsReport result = this.erRepo.save(emissionsReport);

        logger.debug("Report {} {}.", result.getId(), reportAction.label());

        this.reportService.createReportHistory(result.getId(), reportAction);

        this.reportChangeRepo.saveAll(context.buildReportChanges(result));

        return this.emissionsReportMapper.toDto(result);
    }

    /**
     * Delete an emissons report for a given id
     * @param id
     */
    @Transactional
    public void delete(Long id) {

        EmissionsReport er = erRepo.findById(id).orElse(null);

        if (er != null) {

            userFeedbackService.removeReportFromUserFeedback(id);

            for (FacilitySite fs : er.getFacilitySites()) {
                // force bulk delete for emissions, op details, reporting periods
                // as hibernate will do it row by row rather than in bulk
                Long fsId = fs.getId();
                emissionService.deleteByFacilitySite(fsId);
                opDetailService.deleteByFacilitySite(fsId);
                rpService.deleteByFacilitySite(fsId);
            }

            erRepo.deleteById(id);
        }
    }

    public void markReportForDeletion(Long id) {
        EmissionsReport er = erRepo.findById(id).orElse(null);

        if (er != null) {
            er.setDeleted(true);

            erRepo.save(er);
        }
    }

    public List<EmissionsReport> getReportsToDelete() {
        return erRepo.findDeletedReports();
    }

    /**
     * Find the most recent emissions report model object for the given facility
     * @param facilityEisProgramId
     * @return The EmissionsReport model object
     */
    private Optional<EmissionsReport> findMostRecentEmissionsReport(Long masterFacilityRecordId) {

        return erRepo.findByMasterFacilityRecordId(masterFacilityRecordId, Sort.by(Sort.Direction.DESC, "year"))
            .stream().findFirst();
    }

    /**
     * Find the most recent emissions report model object for the given facility that has emissions
     * @param facilityEisProgramId
     * @return The EmissionsReport model object
     */
    private Optional<EmissionsReport> findMostRecentEmissionsReportWithEmissions(Long masterFacilityRecordId, Short reportYear) {

    	return erRepo.findByMasterFacilityRecordId(masterFacilityRecordId, Sort.by(Sort.Direction.DESC, "year"))
    		.stream().filter(report -> report.getThresholdStatus() != ThresholdStatus.OPERATING_BELOW_THRESHOLD)
            .filter(report -> report.getYear() < reportYear).findFirst();
    }

    public List<ReportChangeDto> retrieveChangesForReport(Long reportId) {

        return reportChangeMapper.toDtoList(reportChangeRepo.findByEmissionsReportId(reportId));
    }

	  /**
	  *  Begin Advanced QA for the specified reports, move from Submitted to Advanced QA
	  * @param reportIds
	  * @return
	  */
	 @Override
	 public List<EmissionsReportDto> beginAdvancedQAEmissionsReports(List<Long> reportIds){
	     List<EmissionsReportDto> updatedReports = statusService.advancedQAEmissionsReports(reportIds);
	     reportService.createReportHistory(reportIds, ReportAction.ADVANCED_QA);

	     if (this.propertyProvider.getBoolean(AppPropertyName.FeatureFacilityAutomatedEmailEnabled)) {
	    	 StreamSupport.stream(this.erRepo.findAllById(reportIds).spliterator(), false)
		       .forEach(report -> {

		           SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(report.getProgramSystemCode().getCode());

		           //there should always be exactly one facility site for a CEF emissions report for now. This may change at
		           //some point in the future if different report types are included in the system
		           FacilitySite reportFacilitySite = report.getFacilitySites().get(0);

	               // send an email notification to all certifiers and prepares associated with the master facility
		           // and cc SLT's predefined address that advanced QA has begun for their report
                   notificationService.sendReportAdvancedQANotification(
                		   getFacilityPreparerAndCertifierEmails(report.getMasterFacilityRecord().getId()),
                		   sltConfig.getSltEmail(),
                           cefConfig.getDefaultEmailAddress(),
                           reportFacilitySite.getName(),
                           report.getYear().toString(),
                           sltConfig.getSltEisProgramCode(),
                           sltConfig.getSltEmail());

                   //add vars to map
 	               HashMap<String, Object> varMap = new HashMap<String, Object>();
 	               varMap.put("reportingYear", report.getYear().toString());
 	               varMap.put("facilityName", reportFacilitySite.getName());
 	               varMap.put("sltEmail", sltConfig.getSltEmail());
 	               varMap.put("slt", sltConfig.getSltEisProgramCode());

 	               List<UserFacilityAssociation> userList = this.userFacilityRepo.findByMasterFacilityRecordIdAndActive(report.getMasterFacilityRecord().getId(), true);

 	               for (UserFacilityAssociation user : userList) {
 	            	   notificationService.sendCdxInboxMessage(CdxInboxMessageType.REPORT_ADVANCED_QA, user.getCdxUserId(), cefConfig.getDefaultEmailAddress(), varMap);
 	               }
		       });
	     }
	     return updatedReports;
	 }

    /**
     * Approve the specified reports and move to approved
     * @param reportIds
     * @param comments
     * @return
     */
    @Override
    public List<EmissionsReportDto>acceptEmissionsReports(List<Long> reportIds, String comments) {
    	List<EmissionsReportDto> updatedReports = statusService.acceptEmissionsReports(reportIds);
        reportService.createReportHistory(reportIds, ReportAction.ACCEPTED, comments);

        if (this.propertyProvider.getBoolean(AppPropertyName.FeatureFacilityAutomatedEmailEnabled)) {
        	StreamSupport.stream(this.erRepo.findAllById(reportIds).spliterator(), false)
		      .forEach(report -> {

		    	  SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(report.getProgramSystemCode().getCode());

		    	  //there should always be exactly one facility site for a CEF emissions report for now. This may change at
		    	  //some point in the future if different report types are included in the system
		    	  FacilitySite reportFacilitySite = report.getFacilitySites().get(0);

		    	  Boolean isOptOut = report.getThresholdStatus() != null ? ThresholdStatus.OPERATING_BELOW_THRESHOLD.equals(report.getThresholdStatus()) : false;

		    	  // send an email notification to the certifier and preparers associated with master facility,
		    	  // and cc SLT's predefined address that a report has been accepted
		          notificationService.sendReportAcceptedNotification(
		        		  getFacilityPreparerAndCertifierEmails(report.getMasterFacilityRecord().getId()),
		        		  sltConfig.getSltEmail(),
		        		  cefConfig.getDefaultEmailAddress(),
		        		  reportFacilitySite.getName(),
		        		  report.getYear().toString(),
		        		  comments,
		        		  sltConfig.getSltEisProgramCode(),
		        		  sltConfig.getSltEmail(),
		        		  isOptOut);

		          //add vars to map
	              HashMap<String, Object> varMap = new HashMap<String, Object>();
	              varMap.put("reportingYear", report.getYear().toString());
	              varMap.put("facilityName", reportFacilitySite.getName());
	              varMap.put("comments", comments);
	              varMap.put("sltEmail", sltConfig.getSltEmail());
	              varMap.put("slt", sltConfig.getSltEisProgramCode());
	              varMap.put("isOptOut", isOptOut);

	              List<UserFacilityAssociation> userList = this.userFacilityRepo.findByMasterFacilityRecordIdAndActive(report.getMasterFacilityRecord().getId(), true);

	              for (UserFacilityAssociation user : userList) {
	            	  notificationService.sendCdxInboxMessage(CdxInboxMessageType.REPORT_ACCEPTED, user.getCdxUserId(), cefConfig.getDefaultEmailAddress(), varMap);
	              }
		      });
        }
    	return updatedReports;
    }

    /**
     * Reject the specified reports and move to Rejected
     * @param reportIds
     * @param comments
     * @return
     */
    @Override
    public List<EmissionsReportDto>rejectEmissionsReports(ReviewDTO reviewDTO) {
    	List<EmissionsReportDto> updatedReports = statusService.rejectEmissionsReports(reviewDTO.getReportIds());

        createRejectionReportHistory(reviewDTO, false);

        Iterable<EmissionsReport> reports = this.erRepo.findAllById(reviewDTO.getReportIds());

        reports.forEach(report -> {
            rrRepo.findFirstByEmissionsReportIdAndStatusOrderByVersionDesc(report.getId(), ReportReviewStatus.ACTIVE)
                .ifPresent(review -> {
                    review.setStatus(ReportReviewStatus.HISTORY);
                    rrRepo.save(review);
                });

            rrRepo.findFirstByEmissionsReportIdAndStatusOrderByVersionDesc(report.getId(), ReportReviewStatus.DRAFT)
                .ifPresent(review -> {
                    review.setStatus(ReportReviewStatus.ACTIVE);
                    rrRepo.save(review);
                });
        });

    	if (this.propertyProvider.getBoolean(AppPropertyName.FeatureFacilityAutomatedEmailEnabled)) {
    		StreamSupport.stream(this.erRepo.findAllById(reviewDTO.getReportIds()).spliterator(), false)
		      .forEach(report -> {

		    	  //there should always be exactly one facility site for a CEF emissions report for now. This may change at
		    	  //some point in the future if different report types are included in the system
		    	  FacilitySite reportFacilitySite = report.getFacilitySites().get(0);

		    	  SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(report.getProgramSystemCode().getCode());

		    	  // send an email notification to the certifier and preparers associated with master facility,
		    	  // and cc SLT's predefined address that a report has been rejected
		          notificationService.sendReportRejectedNotification(
		        		  getFacilityPreparerAndCertifierEmails(report.getMasterFacilityRecord().getId()),
		        		  sltConfig.getSltEmail(),
		        		  cefConfig.getDefaultEmailAddress(),
		        		  reportFacilitySite.getName(),
		        		  report.getYear().toString(),
		        		  reviewDTO.getComments(), reviewDTO.getAttachmentId(),
		        		  sltConfig.getSltEisProgramCode(),
		        		  sltConfig.getSltEmail());

		          //add vars to map
                  HashMap<String, Object> varMap = getRejectionVariables(report, sltConfig, reviewDTO);

	              List<UserFacilityAssociation> userList = this.userFacilityRepo.findByMasterFacilityRecordIdAndActive(report.getMasterFacilityRecord().getId(), true);

	              for (UserFacilityAssociation user : userList) {
	            	  notificationService.sendCdxInboxMessage(CdxInboxMessageType.REPORT_REJECTED, user.getCdxUserId(), cefConfig.getDefaultEmailAddress(), varMap);
	              }
		      });
    	}
    	return updatedReports;
    }

    /**
     * Add an emissions report to the list if one does not exist for the current year
     * @param emissionReports
     */
	private void addReportForYear(List<EmissionsReport> emissionReports, Long masterFacilityRecordId, short year) {

        if (!reportYearExists(year, emissionReports)) {
	        EmissionsReport newReport = new EmissionsReport();
	        newReport.setStatus(ReportStatus.NEW);
	        newReport.setValidationStatus(ValidationStatus.UNVALIDATED);
	        newReport.setYear(year);

	        MasterFacilityRecord mfr = new MasterFacilityRecord();
	        mfr.setId(masterFacilityRecordId);
	        newReport.setMasterFacilityRecord(mfr);

	        emissionReports.add(newReport);
        }
	}

    /**
     * Determine whether an emissions report exists for the given year
     * @param year
     * @param emissionReports
     * @return
     */
    private boolean reportYearExists(short year, List<EmissionsReport> emissionReports) {
        for (EmissionsReport rpt : emissionReports) {
	    	if (rpt.getYear() != null && rpt.getYear() ==  year) {
	    		return true;
	    	}
        }
        return false;
    }

    @Override
    public EmissionsReportDto updateSubmitted(long reportId, boolean submitted){

    	EmissionsReport emissionsReport = this.erRepo.findById(reportId)
            .orElseThrow(() -> new NotExistException("Emissions Report", reportId));

    	emissionsReport.setHasSubmitted(submitted);

    	return this.emissionsReportMapper.toDto(this.erRepo.save(emissionsReport));
    }

    public EmissionsReportDto updateMaxNumberOfQAs(long reportId, int numErrors, int numWarnings){

        EmissionsReport emissionsReport = this.erRepo.findById(reportId)
            .orElseThrow(() -> new NotExistException("Emissions Report", reportId));

        if (emissionsReport.getMaxQaErrors() == null || numErrors > emissionsReport.getMaxQaErrors()) {
            emissionsReport.setMaxQaErrors(numErrors);
        }
        if (emissionsReport.getMaxQaWarnings() == null || numWarnings > emissionsReport.getMaxQaWarnings()) {
            emissionsReport.setMaxQaWarnings(numWarnings);
        }

        return this.emissionsReportMapper.toDto(this.erRepo.save(emissionsReport));
    }

    public List<EmissionsReportDto> acceptSemiAnnualReports(ReviewDTO reviewDTO) {
    	List<EmissionsReportDto> updatedReports = statusService.acceptSemiAnnualReports(reviewDTO.getReportIds());

    	if (this.propertyProvider.getBoolean(AppPropertyName.FeatureFacilityAutomatedEmailEnabled)) {
        	StreamSupport.stream(this.erRepo.findAllById(reviewDTO.getReportIds()).spliterator(), false)
		      .forEach(report -> {

		    	  SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(report.getProgramSystemCode().getCode());

		    	  //there should always be exactly one facility site for a CEF emissions report for now. This may change at
		    	  //some point in the future if different report types are included in the system
		    	  FacilitySite reportFacilitySite = report.getFacilitySites().get(0);

		    	  // send an email notification to the certifier and preparers associated with master facility,
		    	  // and cc SLT's predefined address that a semi-annual report has been accepted
		          notificationService.sendMidYearReportAcceptedNotification(
		        		  getFacilityPreparerAndCertifierEmails(report.getMasterFacilityRecord().getId()),
		        		  sltConfig.getSltEmail(),
		        		  cefConfig.getDefaultEmailAddress(),
		        		  reportFacilitySite.getName(),
		        		  report.getYear().toString(),
		        		  reviewDTO.getComments(),
		        		  sltConfig.getSltEisProgramCode(),
		        		  sltConfig.getSltEmail());

                  //add vars to map
                  HashMap<String, Object> varMap = new HashMap<String, Object>();
                  varMap.put("reportingYear", report.getYear().toString());
                  varMap.put("facilityName", report.getFacilitySites().get(0).getName());
                  varMap.put("comments", reviewDTO.getComments());
                  varMap.put("sltEmail", sltConfig.getSltEmail());
                  varMap.put("slt", sltConfig.getSltEisProgramCode());

                  List<UserFacilityAssociation> userList = this.userFacilityRepo.findByMasterFacilityRecordIdAndActive(report.getMasterFacilityRecord().getId(), true);

                  for (UserFacilityAssociation user : userList) {
                      notificationService.sendCdxInboxMessage(CdxInboxMessageType.SEMIANNUAL_REPORT_ACCEPTED, user.getCdxUserId(), cefConfig.getDefaultEmailAddress(), varMap);
                  }
		      });
        }
    	return updatedReports;
    }

    public List<EmissionsReportDto> rejectSemiAnnualReports(ReviewDTO reviewDTO) {
    	List<EmissionsReportDto> updatedReports = statusService.rejectSemiAnnualReports(reviewDTO.getReportIds());

        createRejectionReportHistory(reviewDTO, true);

    	if (this.propertyProvider.getBoolean(AppPropertyName.FeatureFacilityAutomatedEmailEnabled)) {
    		StreamSupport.stream(this.erRepo.findAllById(reviewDTO.getReportIds()).spliterator(), false)
		      .forEach(report -> {

		    	  //there should always be exactly one facility site for a CEF emissions report for now. This may change at
		    	  //some point in the future if different report types are included in the system
		    	  FacilitySite reportFacilitySite = report.getFacilitySites().get(0);

		    	  SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(report.getProgramSystemCode().getCode());

		    	  // send an email notification to the certifier and preparers associated with master facility,
		    	  // and cc SLT's predefined address that a semi-annual report has been rejected
		          notificationService.sendMidYearReportRejectedNotification(
		        		  getFacilityPreparerAndCertifierEmails(report.getMasterFacilityRecord().getId()),
		        		  sltConfig.getSltEmail(),
		        		  cefConfig.getDefaultEmailAddress(),
		        		  reportFacilitySite.getName(),
		        		  report.getYear().toString(),
		        		  reviewDTO.getComments(),
		        		  sltConfig.getSltEisProgramCode(),
		        		  sltConfig.getSltEmail());

                  //add vars to map
                  HashMap<String, Object> varMap = getRejectionVariables(report, sltConfig, reviewDTO);

                  List<UserFacilityAssociation> userList = this.userFacilityRepo.findByMasterFacilityRecordIdAndActive(report.getMasterFacilityRecord().getId(), true);

                  for (UserFacilityAssociation user : userList) {
                      notificationService.sendCdxInboxMessage(CdxInboxMessageType.SEMIANNUAL_REPORT_REJECTED, user.getCdxUserId(), cefConfig.getDefaultEmailAddress(), varMap);
                  }
		      });
    	}
    	return updatedReports;
    }

    public SignatureDocumentType getCopyOfRecord(String activityId, String documentId) {
    	try {
	    	URL signatureServiceUrl = new URL(cefConfig.getCdxConfig().getRegisterSignServiceEndpoint());
	    	String token = signatureServiceClient.authenticate(signatureServiceUrl, cefConfig.getCdxConfig().getNaasUser(), cefConfig.getCdxConfig().getNaasPassword());
	    	SignatureDocumentType document = signatureServiceClient.downloadByDocumentId(signatureServiceUrl, token, activityId, documentId);

	    	return document;

    	} catch(Exception e) {
            logger.error("getCopyOfRecord - {}", e.getMessage());
            throw ApplicationException.asApplicationException(e);
        }
    }

    public PdfDocumentType generateCopyOfRecord(Long reportId, boolean isSemiannual, boolean signed) {
        try {
            String url;

            if (isSemiannual) {
                url = String.format("%s/semiannual/cor/%d?generate=%b", cefConfig.getCdxConfig().getAppBaseUrl(), reportId, signed);
            }
            else {
                url = String.format("%s/cor/%d?generate=%b", cefConfig.getCdxConfig().getAppBaseUrl(), reportId, signed);
            }

            return pdfClient.renderUrlAsPdf(url);

        } catch(Exception e) {
            logger.error("generateCopyOfRecord - {}", e.getMessage());
            throw ApplicationException.asApplicationException(e);
        }
    }

    private String generateHTMLCopyOfRecord(Long reportId, boolean isSemiannual, boolean signed) {

        return corService.generateHTMLCopyOfRecord(reportId, isSemiannual, signed);
    }

    public void writeFileTo(SignatureDocumentType document, OutputStream outputStream) {
		if (document != null) {
			try {
				document.getContent().writeTo(outputStream);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}

    public void transformXmlCor(SignatureDocumentType document, OutputStream outputStream) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(document.getContent().getInputStream());

            if (CERS_V2_XMLNS.equalsIgnoreCase(doc.getDocumentElement().getNamespaceURI())) {
                transform(doc, outputStream, CERS_V2_XSLT);
            } else {
                transform(doc, outputStream, CERS_V1_XSLT);
            }

        } catch (IOException | ParserConfigurationException e) {
            throw new IllegalStateException(e);
        } catch (SAXException | TransformerException e) {
            logger.error("transformXmlCor - {}", e.getMessage());
            throw ApplicationException.asApplicationException(e);
        }
    }

    private void transform(Document doc, OutputStream output, String stylesheet)
        throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        // add XSLT in Transformer
        Transformer transformer = transformerFactory.newTransformer(
            new StreamSource(this.getClass().getResourceAsStream(stylesheet)));

        transformer.transform(new DOMSource(doc), new StreamResult(output));

    }

    private String getFacilityPreparerAndCertifierEmails(Long mfrId) {
    	return getFacilityPreparerAndCertifierEmails(mfrId, null, null);
    }

    private String getFacilityPreparerAndCertifierEmails(Long mfrId, String cdxUserId, String sltEmail) {

    	List<String> emailList = new ArrayList<>();
		List<UserFacilityAssociation> userList = this.userFacilityRepo.findByMasterFacilityRecordIdAndActive(mfrId, true);
		Map<Object,List<UserFacilityAssociationDto>> filteredUserList;

		// For Annual Report Submissions CC email list contains all emails of active users associated with facility and SLT email
		// Does not include the certifier submitting. Submission email notifications addressed TO certifier.
		if (org.apache.commons.lang.StringUtils.isNotBlank(cdxUserId)) {
			emailList.add(sltEmail);
			filteredUserList = userFacilityAssocService.mapAssociations(userList).stream()
					.filter(role -> !role.getCdxUserId().equalsIgnoreCase(cdxUserId))
					.collect(Collectors.groupingBy(user -> user.getCdxUserId()));
		} else {
			// For all other email notifications TO email list contains all emails of active users associated with facility
			filteredUserList = userFacilityAssocService.mapAssociations(userList).stream()
					.collect(Collectors.groupingBy(user -> user.getCdxUserId()));
		}

		for (List<UserFacilityAssociationDto> ufa: filteredUserList.values()) {
			emailList.add(ufa.get(0).getEmail());
		}

    	return emailList.toString().substring(1, emailList.toString().length() - 1);
    }

    private void createRejectionReportHistory(ReviewDTO reviewDTO, boolean semiAnnual) {
        Iterable<EmissionsReport> reports = this.erRepo.findAllById(reviewDTO.getReportIds());

        if(reviewDTO.getAttachmentId() != null) {
            Attachment attachment = this.reportAttachmentsRepo.findById(reviewDTO.getAttachmentId())
                .orElseThrow(() -> new NotExistException("Report Attachment", reviewDTO.getAttachmentId()));

            reports.forEach(report -> {

                if (attachment.getEmissionsReport().getId() == report.getId()) {
                    if (semiAnnual) {
                        reportService.createReportHistory(report.getId(), ReportAction.SEMIANNUAL_REJECTED, reviewDTO.getComments(), attachment);
                    } else {
                        reportService.createReportHistory(report.getId(), ReportAction.REJECTED, reviewDTO.getComments(), attachment);
                    }

                } else {
                    Attachment copyAttachment = new Attachment(attachment);
                    copyAttachment.clearId();
                    copyAttachment.setEmissionsReport(report);

                    Attachment result = reportAttachmentsRepo.save(copyAttachment);
                    if (semiAnnual) {
                        reportService.createReportHistory(report.getId(), ReportAction.SEMIANNUAL_REJECTED, reviewDTO.getComments(), attachment);
                    } else {
                        reportService.createReportHistory(report.getId(), ReportAction.REJECTED, reviewDTO.getComments(), attachment);
                    }
                }
            });

        } else {
            if (semiAnnual) {
                reportService.createReportHistory(reviewDTO.getReportIds(), ReportAction.SEMIANNUAL_REJECTED, reviewDTO.getComments());
            } else {
                reportService.createReportHistory(reviewDTO.getReportIds(), ReportAction.REJECTED, reviewDTO.getComments());
            }
        }
    }

    private boolean monthlyReportingEnabledAndYearCheck(SLTBaseConfig sltConfig, EmissionsReport er) {
        Short monthlyInitialYear = sltConfig.getSltMonthlyFuelReportingInitialYear();
        return Boolean.TRUE.equals(sltConfig.getSltMonthlyFuelReportingEnabled())
            && (monthlyInitialYear == null || er.getYear() >= monthlyInitialYear);
    }

    // rejection emails for semiannual and annual reports share all fields
    private HashMap<String, Object> getRejectionVariables(EmissionsReport report, SLTBaseConfig sltConfig, ReviewDTO reviewDTO) {
        HashMap<String, Object> varMap = new HashMap<String, Object>();
        varMap.put("reportingYear", report.getYear().toString());
        varMap.put("facilityName", report.getFacilitySites().get(0).getName());
        varMap.put("sltEmail", sltConfig.getSltEmail());
        varMap.put("slt", sltConfig.getSltEisProgramCode());
        varMap.put("comments", reviewDTO.getComments());

        if (reviewDTO.getAttachmentId() != null) {
            Attachment attachment = attachmentsRepo.findById(reviewDTO.getAttachmentId())
                .orElseThrow(() -> new NotExistException("Report Attachment", reviewDTO.getAttachmentId()));

            varMap.put("attachment", attachment.getFileName());
        }

        return varMap;
    }

}
