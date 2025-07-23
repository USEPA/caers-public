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

import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.domain.ProgramSystemCode;
import gov.epa.cef.web.domain.MasterFacilityRecord;
import gov.epa.cef.web.domain.MasterFacilityNAICSXref;
import gov.epa.cef.web.domain.NaicsCode;
import gov.epa.cef.web.domain.ReportStatus;
import gov.epa.cef.web.domain.ValidationStatus;
import gov.epa.cef.web.domain.OperatingStatusCode;
import gov.epa.cef.web.domain.FacilitySite;
import gov.epa.cef.web.domain.FacilityNAICSXref;
import gov.epa.cef.web.domain.FacilitySiteContact;
import gov.epa.cef.web.domain.ReleasePoint;
import gov.epa.cef.web.domain.Control;
import gov.epa.cef.web.domain.ControlAssignment;
import gov.epa.cef.web.domain.ControlPath;
import gov.epa.cef.web.domain.EmissionsUnit;
import gov.epa.cef.web.domain.EmissionsProcess;
import gov.epa.cef.web.domain.ReportingPeriod;
import gov.epa.cef.web.domain.ReportingPeriodCode;
import gov.epa.cef.web.domain.Emission;
import gov.epa.cef.web.domain.ReportHistory;
import gov.epa.cef.web.domain.ReportAction;
import gov.epa.cef.web.domain.FacilitySourceTypeCode;
import gov.epa.cef.web.service.mapper.json.JsonReleasePointMapper;
import gov.epa.cef.web.service.mapper.json.JsonEmissionsUnitMapper;
import gov.epa.cef.web.service.mapper.json.JsonControlMapper;
import gov.epa.cef.web.service.mapper.json.JsonFacilitySiteMapper;
import gov.epa.cef.web.service.mapper.json.JsonReportMapper;
import gov.epa.cef.web.service.mapper.json.JsonReleasePointMapperImpl;
import gov.epa.cef.web.service.mapper.json.JsonEmissionsUnitMapperImpl;
import gov.epa.cef.web.service.mapper.json.JsonControlMapperImpl;
import gov.epa.cef.web.service.mapper.json.JsonFacilitySiteMapperImpl;
import gov.epa.cef.web.service.mapper.json.JsonReportMapperImpl;
import gov.epa.cef.web.service.dto.json.ReportInfoJsonDto;
import gov.epa.cef.web.service.dto.json.EmissionsReportJsonDto;
import gov.epa.cef.web.service.dto.json.FacilitySiteContactJsonDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

import org.springframework.data.jpa.domain.Specification;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;


@RunWith(MockitoJUnitRunner.Silent.class)
public class EmissionsReportExportServiceImplTest extends BaseServiceTest {

    @Test
    public void validateMapping() {
        EmissionsReport er = createHydratedEmissionsReport();

        JsonReleasePointMapper rpMapper = new JsonReleasePointMapperImpl();
        JsonEmissionsUnitMapper unitMapper = new JsonEmissionsUnitMapperImpl(rpMapper);
        JsonControlMapper controlMapper = new JsonControlMapperImpl();
        JsonFacilitySiteMapper facSiteMapper = new JsonFacilitySiteMapperImpl(controlMapper, unitMapper, rpMapper);
        JsonReportMapper jsonReportMapper = new JsonReportMapperImpl(facSiteMapper);

        ReportInfoJsonDto dto = jsonReportMapper.infoFromEmissionsReport(er);

        Calendar cal = new GregorianCalendar(2010, Calendar.JUNE, 17);
        assert(Objects.equals(dto.getCertifiedDate(), cal.getTime()));
    }

    @Test
    public void validateFacilityContactMapping() {
        EmissionsReport er = createHydratedEmissionsReport();

        JsonReleasePointMapper rpMapper = new JsonReleasePointMapperImpl();
        JsonEmissionsUnitMapper unitMapper = new JsonEmissionsUnitMapperImpl(rpMapper);
        JsonControlMapper controlMapper = new JsonControlMapperImpl();
        JsonFacilitySiteMapper facSiteMapper = new JsonFacilitySiteMapperImpl(controlMapper, unitMapper, rpMapper);
        JsonReportMapper jsonReportMapper = new JsonReportMapperImpl(facSiteMapper);

        EmissionsReportJsonDto dto = jsonReportMapper.fromEmissionsReport(er);

        List<FacilitySiteContactJsonDto> contactsDtoList = dto.getFacilitySite().get(0).getFacilityContacts();
        List<FacilitySiteContact> contactsList = er.getFacilitySites().get(0).getContacts();

        assert(dto.getFacilitySite().size() == 1);
        assert(contactsDtoList.size() == contactsList.size());
        assert(contactsDtoList.get(0).getFirstName().equals(contactsList.get(0).getFirstName()));
        assert(contactsDtoList.get(0).getLastName().equals(contactsList.get(0).getLastName()));
        assert(contactsDtoList.get(0).getEmail().equals(contactsList.get(0).getEmail()));
        assert(contactsDtoList.get(0).getStreetAddress().getAddressText().equals(contactsList.get(0).getStreetAddress()));
        assert(contactsDtoList.get(0).getStreetAddress().getLocalityName().equals(contactsList.get(0).getCity()));
        assert(contactsDtoList.get(0).getMailingStreetAddress().getAddressText().equals(contactsList.get(0).getMailingStreetAddress()));
    }


    private EmissionsReport createHydratedEmissionsReport() {
        ProgramSystemCode  psc = new ProgramSystemCode();
        psc.setCode("GADNR");

        MasterFacilityRecord mfr = new MasterFacilityRecord();
        mfr.setAgencyFacilityIdentifier("ALTID");
        mfr.setCity("Raleigh");
        mfr.setDescription("Facility Description");
        mfr.setEisProgramId("EISID");
        mfr.setId(1L);
        mfr.setLatitude(BigDecimal.valueOf(2.5d));
        mfr.setLongitude(BigDecimal.valueOf(2.5d));
        mfr.setProgramSystemCode(psc);

        List<MasterFacilityNAICSXref> masterFacilityNAICS = new ArrayList<>();
        MasterFacilityNAICSXref mfXref = new MasterFacilityNAICSXref();
        mfXref.setMasterFacilityRecord(mfr);
        mfXref.setId(1L);
        NaicsCode naics = new NaicsCode();
        naics.setCode(123);
        naics.setDescription("ABCDE");
        mfXref.setNaicsCode(naics);
        masterFacilityNAICS.add(mfXref);
        mfr.setMasterFacilityNAICS(masterFacilityNAICS);

        EmissionsReport er = new EmissionsReport();
        er.setEisProgramId("");
        er.setId(1L);
        er.setStatus(ReportStatus.APPROVED);
        er.setValidationStatus(ValidationStatus.PASSED);
        er.setYear((short) 2018);

        er.setProgramSystemCode(psc);
        er.setMasterFacilityRecord(mfr);

        Calendar cal = new GregorianCalendar(2010, Calendar.JUNE, 17);
        List<ReportHistory> reportHistoryList = new ArrayList<ReportHistory>();
        ReportHistory rh = new ReportHistory();
        rh.setEmissionsReport(er);
        rh.setReportAction(ReportAction.SUBMITTED);
        rh.setActionDate(cal.getTime());
        rh.setUserId("john.doe");
        rh.setUserFullName("John Doe");
        rh.setUserRole("NEI Certifier");
        rh.setFileDeleted(false);
        reportHistoryList.add(rh);
        er.setReportHistory(reportHistoryList);

        OperatingStatusCode opStatus = new OperatingStatusCode();
        opStatus.setCode("OP");

        List<FacilitySite> facilitySites = new ArrayList<>();
        FacilitySite fs = new FacilitySite();
        fs.setAgencyFacilityIdentifier("ALTID");
        fs.setCity("Raleigh");
        fs.setDescription("Facility Description");
        fs.setEmissionsReport(er);
        fs.setId(1L);
        fs.setLatitude(BigDecimal.valueOf(2.5d));
        fs.setLongitude(BigDecimal.valueOf(2.5d));

        FacilitySourceTypeCode fstc = new FacilitySourceTypeCode();
        fstc.setCode("Source Type Code");
        fstc.setDescription("Source Type Desc");
        fs.setFacilitySourceTypeCode(fstc);

        List<FacilitySiteContact> contacts = new ArrayList<>();
        FacilitySiteContact fsc = new FacilitySiteContact();
        fsc.setCity("Raleigh");
        fsc.setId(1L);
        fsc.setFacilitySite(fs);
        fsc.setFirstName("John");
        fsc.setLastName("Doe");
        fsc.setEmail("email@test.com");
        fsc.setStreetAddress("123 Some Street");
        fsc.setCity("Acity");
        fsc.setMailingStreetAddress("123 Another Street");
        contacts.add(fsc);
        fs.setContacts(contacts);

        List<FacilityNAICSXref> facilityNAICS = new ArrayList<>();
        FacilityNAICSXref xref = new FacilityNAICSXref();
        xref.setFacilitySite(fs);
        xref.setId(1L);
        xref.setNaicsCode(naics);
        fs.setFacilityNAICS(facilityNAICS);

        List<ReleasePoint> releasePoints = new ArrayList<>();
        ReleasePoint rp = new ReleasePoint();
        rp.setOperatingStatusCode(opStatus);
        rp.setId(1L);
        rp.setComments("Comments");
        releasePoints.add(rp);
        fs.setReleasePoints(releasePoints);

        List<Control> controls = new ArrayList<>();
        Control control = new Control();
        control.setOperatingStatusCode(opStatus);
        control.setId(1L);
        control.setFacilitySite(fs);

        List<ControlAssignment> assignments = new ArrayList<>();
        ControlAssignment ca = new ControlAssignment();
        ca.setControl(control);
        ca.setId(1L);
        ControlPath cp = new ControlPath();
        List<ControlAssignment> caSet = new ArrayList<>();
        caSet.add(ca);
        cp.setAssignments(caSet);
        cp.setId(1L);
        ca.setControlPath(cp);
        assignments.add(ca);

        control.setAssignments(assignments);
        controls.add(control);
        fs.setControls(controls);


        List<EmissionsUnit> units = new ArrayList<>();
        EmissionsUnit eu = new EmissionsUnit();
        eu.setOperatingStatusCode(opStatus);
        eu.setId(1L);
        eu.setComments("Test Unit");
        eu.setFacilitySite(fs);

        List<EmissionsProcess> processes = new ArrayList<>();
        EmissionsProcess ep = new EmissionsProcess();
        ep.setOperatingStatusCode(opStatus);
        ep.setId(1L);
        ep.setEmissionsUnit(eu);
        ep.setComments("Test Process Comments");

        List<ReportingPeriod> reportingPeriods = new ArrayList<>();
        ReportingPeriod repPer = new ReportingPeriod();
        repPer.setId(1L);
        repPer.setComments("Reporting Period Comments");
        repPer.setEmissionsProcess(ep);

        ReportingPeriodCode rptc = new ReportingPeriodCode();
        rptc.setCode("A");
        rptc.setShortName("Annual");
        repPer.setReportingPeriodTypeCode(rptc);

        List<Emission> emissions = new ArrayList<>();
        Emission e = new Emission();
        e.setId(1L);
        e.setComments("Test Emission Comments");
        emissions.add(e);
        repPer.setEmissions(emissions);

        reportingPeriods.add(repPer);
        ep.setReportingPeriods(reportingPeriods);
        processes.add(ep);
        eu.setEmissionsProcesses(processes);
        units.add(eu);
        fs.setEmissionsUnits(units);

        facilitySites.add(fs);
        er.setFacilitySites(facilitySites);

        return er;
    }
}
