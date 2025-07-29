/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package gov.epa.cef.web.service.impl;

import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.exception.ApplicationException;
import gov.epa.cef.web.exception.NotExistException;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.service.CersXmlService;
import gov.epa.cef.web.service.UserService;
import gov.epa.cef.web.service.dto.EisSubmissionStatus;
import gov.epa.cef.web.service.mapper.cers._2._0.CersV2DataTypeMapper;
import gov.epa.cef.web.util.ConstantUtils;
import net.exchangenetwork.schema.cer._2._0.CERSDataType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.math.RoundingMode;


@Service
public class CersXmlServiceImpl implements CersXmlService {

    private static final Logger logger = LoggerFactory.getLogger(CersXmlServiceImpl.class);

    private final EmissionsReportRepository reportRepo;

    private final UserService userService;

    private final CersV2DataTypeMapper cersV2Mapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    CersXmlServiceImpl(UserService userService,
                       EmissionsReportRepository reportRepo,
                       CersV2DataTypeMapper cersV2Mapper) {

        this.userService = userService;
        this.reportRepo = reportRepo;
        this.cersV2Mapper = cersV2Mapper;
    }


    public CERSDataType generateCersV2Data(Long reportId, EisSubmissionStatus submissionStatus) {
        EmissionsReport source = reportRepo.findById(reportId)
            .orElseThrow(() -> new NotExistException("Emissions Report", reportId));

        try {
            if (submissionStatus != null) {
                source.getFacilitySites().forEach(fs -> {
                    if (ConstantUtils.EIS_TRANSMISSION_POINT_EMISSIONS.contentEquals(submissionStatus.dataCategory())) {
                        generatePointEmissionSubmission(fs);
                    } else if (ConstantUtils.EIS_TRANSMISSION_FACILITY_INVENTORY.equals(submissionStatus.dataCategory())) {
                        generateFacilityInventorySubmission(fs);
                    }
                });
            }
        } catch (Exception ex) {
            logger.info("generateCersV2Data exception: ", ex);
            throw ex;
        }

        CERSDataType cers = cersV2Mapper.fromEmissionsReport(source);
        cers.setUserIdentifier(userService.getCurrentUser().getEmail());

        if (this.entityManager != null) {
            // detach entity from session so that the dirty entity won't get picked up by other database calls
            entityManager.detach(source);
        }

        return cers;
    }


    @Override
    public void writeCersV2XmlTo(long reportId, OutputStream outputStream, EisSubmissionStatus submissionStatus) {

        CERSDataType cers = generateCersV2Data(reportId, submissionStatus);

        try {
            net.exchangenetwork.schema.cer._2._0.ObjectFactory objectFactory = new net.exchangenetwork.schema.cer._2._0.ObjectFactory();
            JAXBContext jaxbContext = JAXBContext.newInstance(CERSDataType.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            jaxbMarshaller.marshal(objectFactory.createCERS(cers), outputStream);

        } catch (JAXBException e) {

            logger.error("error while marshalling", e);
            throw ApplicationException.asApplicationException(e);
        }
    }


    private void generatePointEmissionSubmission(FacilitySite fs) {

        // remove extra data
        fs.getReleasePoints().clear();
        fs.getControlPaths().clear();
        fs.getControls().clear();

        // remove non-operating units and units without processes
        fs.setEmissionsUnits(fs.getEmissionsUnits().stream()
            .peek(eu -> {
                eu.setEmissionsProcesses(filterProcesses(eu));
                eu.setComments(truncateComments(eu.getComments()));
            })
            .filter(eu -> ConstantUtils.STATUS_OPERATING.equals(eu.getOperatingStatusCode().getCode()) && !eu.getEmissionsProcesses().isEmpty())
            .collect(Collectors.toList()));
    }


    private void generateFacilityInventorySubmission(FacilitySite fs) {

        if (!(fs.getFacilitySourceTypeCode() != null
            && ConstantUtils.FACILITY_SOURCE_LANDFILL_CODE.contentEquals(fs.getFacilitySourceTypeCode().getCode()))
            && !ConstantUtils.STATUS_OPERATING.equals(fs.getOperatingStatusCode().getCode())) {

            fs.getReleasePoints().clear();
            fs.getControlPaths().clear();
            fs.getControls().clear();
            fs.getEmissionsUnits().clear();
        } else {
            fs.setEmissionsUnits(fs.getEmissionsUnits().stream()
                .map(eu -> {
                    eu.setComments(truncateComments(eu.getComments()));
                    // remove extra information from units which are not operational
                    if (!ConstantUtils.STATUS_OPERATING.equals(eu.getOperatingStatusCode().getCode())) {
                        EmissionsUnit result = this.cersV2Mapper.emissionsUnitToNonOperatingEmissionsUnit(eu);
                        return result;
                    } else {

                        //first set the Processes for the emissions unit
                        eu.setEmissionsProcesses(eu.getEmissionsProcesses().stream()
                            .filter(ep -> SccCategory.Point == ep.getSccCategory()) // remove all processes with non-point SCCs
                            .map(ep -> {

                                //remove all reporting periods, operating details, and emissions from the emission process
                                //for a FacilityInventory submission
                                ep.getReportingPeriods().clear();

                                // remove extra information from processes which are not operational
                                if (!ConstantUtils.STATUS_OPERATING.equals(ep.getOperatingStatusCode().getCode())) {
                                    EmissionsProcess result = this.cersV2Mapper.processToNonOperatingEmissionsProcess(ep);
                                    return result;
                                }
                                return ep;
                            }).collect(Collectors.toList()));

                        return eu;
                    }

                }).collect(Collectors.toList()));

            fs.setReleasePoints(fs.getReleasePoints().stream()
                .map(rp -> {

                    // remove extra information from release points which are not operational
                    if (!ConstantUtils.STATUS_OPERATING.equals(rp.getOperatingStatusCode().getCode())) {
                        ReleasePoint result = this.cersV2Mapper.releasePointToNonOperatingReleasePoint(rp);
                        return result;
                    }
                    return rp;
                }).collect(Collectors.toList()));
        }
    }


    /***
     * Remove non-operating processes and processes without emissions
     * @param eu
     * @return
     */
    private List<EmissionsProcess> filterProcesses(EmissionsUnit eu) {

        return eu.getEmissionsProcesses().stream()
            .peek(ep -> {

                // remove extra data and remove reporting periods without emissions
                ep.getReleasePointAppts().clear();
                ep.setReportingPeriods(ep.getReportingPeriods().stream()
                    .peek(rp -> {
                        rp.getOperatingDetails().forEach(operatingDetail -> {
                            operatingDetail.setAvgDaysPerWeek(operatingDetail.getAvgDaysPerWeek().setScale(3, RoundingMode.DOWN));
                            operatingDetail.setAvgHoursPerDay(operatingDetail.getAvgHoursPerDay().setScale(3, RoundingMode.DOWN));
                            operatingDetail.setAvgWeeksPerPeriod(operatingDetail.getAvgWeeksPerPeriod().setScale(0, RoundingMode.DOWN));
                            operatingDetail.setActualHoursPerPeriod(operatingDetail.getActualHoursPerPeriod().setScale(0, RoundingMode.DOWN));
                        });
                        List<Emission> filteredEmissions = rp.getEmissions().stream()
                            .peek(e -> e.setComments(truncateComments(e.getComments())))
                            .filter(e -> shouldIncludeEmission(e, rp))
                            .collect(Collectors.toList());
                        rp.setEmissions(filteredEmissions);
                    })
                    .filter(rp -> !rp.getEmissions().isEmpty())
                    .collect(Collectors.toList()));

            }).filter(ep -> SccCategory.Point == ep.getSccCategory()) // remove all processes with non-point SCCs
            .filter(ep -> ConstantUtils.STATUS_OPERATING.equals(ep.getOperatingStatusCode().getCode()) && !ep.getReportingPeriods().isEmpty())
            .collect(Collectors.toList());
    }


    /***
     * Set up filter to remove emission records based on pollutant group rules
     * @param e
     * @param rp
     * @return
     */
    private boolean shouldIncludeEmission(Emission e, ReportingPeriod rp) {

        // Skip emissions that are marked as not reporting
        if (e.getNotReporting()) {
            return false;
        }

        Set<PollutantGroup> pollutantGroups = e.getPollutant().getPollutantGroups();

        if (pollutantGroups == null || pollutantGroups.isEmpty()) {
            return true;
        }

        // this emission is in one or more groups, so we need to decide whether to keep it
        for (PollutantGroup pg : pollutantGroups) {

            if (e.getPollutant().getPollutantCode().equals(pg.getPollutantCode())) {
                // this emission is at the group level, so we need to see if any other pollutants in the reportingPeriod are in the same group
                for (Pollutant p : pg.getPollutants()) {
                    if (!p.getPollutantCode().equals(pg.getPollutantCode())) {
                        // the pollutant is not the same as the group level pollutant
                        if (rp.getEmissions().stream().anyMatch(e1 -> e1.getPollutant().getPollutantCode().equals(p.getPollutantCode()))) {
                            // remove emission e from reportingPeriod rp because there is a member of pollutantGroup that is reported
                            return false;
                        }
                    }
                }
            }

        }


        return true;
    }

    /***
     * Truncates comments to 400
     * @param e
     * @param rp
     * @return
    */
    private String truncateComments(String comments) {
        if (comments != null && comments.length() > 400) {
                return comments.substring(0, 400);
        }

        return comments;
    }
}
