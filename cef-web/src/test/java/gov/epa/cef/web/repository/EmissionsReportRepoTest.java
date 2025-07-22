/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.repository;

import gov.epa.cef.web.config.CommonInitializers;
import gov.epa.cef.web.domain.CalculationMaterialCode;
import gov.epa.cef.web.domain.CalculationMethodCode;
import gov.epa.cef.web.domain.CalculationParameterTypeCode;
import gov.epa.cef.web.domain.ContactTypeCode;
import gov.epa.cef.web.domain.Control;
import gov.epa.cef.web.domain.ControlAssignment;
import gov.epa.cef.web.domain.ControlMeasureCode;
import gov.epa.cef.web.domain.ControlPath;
import gov.epa.cef.web.domain.ControlPollutant;
import gov.epa.cef.web.domain.Emission;
import gov.epa.cef.web.domain.EmissionsOperatingTypeCode;
import gov.epa.cef.web.domain.EmissionsProcess;
import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.domain.EmissionsUnit;
import gov.epa.cef.web.domain.FacilityNAICSXref;
import gov.epa.cef.web.domain.FacilitySite;
import gov.epa.cef.web.domain.FacilitySiteContact;
import gov.epa.cef.web.domain.FacilitySourceTypeCode;
import gov.epa.cef.web.domain.FipsStateCode;
import gov.epa.cef.web.domain.MasterFacilityRecord;
import gov.epa.cef.web.domain.NaicsCode;
import gov.epa.cef.web.domain.NaicsCodeType;
import gov.epa.cef.web.domain.OperatingDetail;
import gov.epa.cef.web.domain.OperatingStatusCode;
import gov.epa.cef.web.domain.Pollutant;
import gov.epa.cef.web.domain.ProgramSystemCode;
import gov.epa.cef.web.domain.ReleasePoint;
import gov.epa.cef.web.domain.ReleasePointAppt;
import gov.epa.cef.web.domain.ReleasePointTypeCode;
import gov.epa.cef.web.domain.ReportStatus;
import gov.epa.cef.web.domain.ReportingPeriod;
import gov.epa.cef.web.domain.ReportingPeriodCode;
import gov.epa.cef.web.domain.SccCategory;
import gov.epa.cef.web.domain.UnitMeasureCode;
import gov.epa.cef.web.domain.UnitTypeCode;
import gov.epa.cef.web.domain.ValidationStatus;
import gov.epa.cef.web.service.dto.EmissionsReportDto;
import gov.epa.cef.web.service.dto.EmissionsReportStarterDto;
import gov.epa.cef.web.service.impl.EmissionsReportServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@SqlGroup(value = { @Sql("classpath:db/test/baseTestData.sql") })
@ContextConfiguration(initializers = {
		CommonInitializers.NoCacheInitializer.class
})
public class EmissionsReportRepoTest extends BaseRepositoryTest {

	@Autowired
	EmissionsReportRepository reportRepo;

	@Autowired
	FacilitySiteRepository facilitySiteRepo;

	@Autowired
	ReleasePointRepository releasePointRepo;

	@Autowired
	EmissionsUnitRepository unitRepo;

	@Autowired
	private EmissionsReportServiceImpl emissionsReportServiceImpl;

	/**
	 * Verify that deleting an emissions report works and that any children are
	 * also deleted
	 *
	 * @throws Exception
	 */
	@Test
	public void deletingReport_should_DeleteChild() throws Exception {

		// verify the emissionsReport, facilitySite, releasePoint, and emissionsUnit
		// exist
		Optional<EmissionsReport> report = reportRepo.findById(9999997L);
		assertEquals(true, report.isPresent());

		Optional<FacilitySite> facilitySite = facilitySiteRepo.findById(9999991L);
		assertEquals(true, facilitySite.isPresent());

		Optional<ReleasePoint> releasePoint = releasePointRepo.findById(9999991L);
		assertEquals(true, releasePoint.isPresent());

		Optional<EmissionsUnit> unit = unitRepo.findById(9999991L);
		assertEquals(true, unit.isPresent());

		// delete the emissions report and verify that the children are gone as well
		reportRepo.deleteById(9999997L);

		// verify the emissionsReport, facilitySite, releasePoint, and emissionsUnit
		// no longer exist
		report = reportRepo.findById(9999997L);
		assertEquals(false, report.isPresent());

		facilitySite = facilitySiteRepo.findById(9999991L);
		assertEquals(false, facilitySite.isPresent());

		releasePoint = releasePointRepo.findById(9999991L);
		assertEquals(false, releasePoint.isPresent());

		unit = unitRepo.findById(9999991L);
		assertEquals(false, unit.isPresent());
	}

	// Test to confirm copy emissions report functionality works and does not result in an error due to
	// missing required fields. Purpose of test is not confirm copy of every field.
	//
	// If new required fields are created, the fields should be added to createHydratedEmissionsReport.
	@Test
	public void createEmissionReportCopy_Should_ReturnValidDeepCopy_WhenValidFacilityAndYearPassed() throws Exception {

		// existing report
		EmissionsReport originalEmissionsReport = createHydratedEmissionsReport();
		originalEmissionsReport = reportRepo.save(originalEmissionsReport);

		// copy report
		EmissionsReportStarterDto starter = new EmissionsReportStarterDto();
        starter.setMasterFacilityRecordId(9999991L);
        starter.setYear((short) 2020);
		EmissionsReportDto emissionsReportCopy = emissionsReportServiceImpl.createEmissionReportCopy(starter);

		assertEquals(ReportStatus.IN_PROGRESS.toString(), emissionsReportCopy.getStatus());
		assertEquals(ValidationStatus.UNVALIDATED.toString(), emissionsReportCopy.getValidationStatus());
		assertEquals("2020", emissionsReportCopy.getYear().toString());
		assertNotEquals(emissionsReportCopy.getId(), originalEmissionsReport.getId());
	}

	private EmissionsReport createHydratedEmissionsReport() {
	    MasterFacilityRecord mfr = new MasterFacilityRecord();
	    mfr.setId(9999991L);

	    ProgramSystemCode psc = new ProgramSystemCode();
        psc.setCode("GADNR");

		EmissionsReport er = new EmissionsReport();
		er.setMasterFacilityRecord(mfr);
		er.setProgramSystemCode(psc);
		er.setEisProgramId("ABC");
		er.setId(1L);
		er.setStatus(ReportStatus.APPROVED);
		er.setValidationStatus(ValidationStatus.PASSED);
		er.setYear((short) 2018);
		er.setReturnedReport(false);

		List<FacilitySite> facilitySites = new ArrayList<>();
		FacilitySite fs = new FacilitySite();
		fs.setName("A Facility");
		fs.setAgencyFacilityIdentifier("ALTID");
		fs.setCity("Raleigh");
		fs.setDescription("Facility Description");
		fs.setEmissionsReport(er);
		fs.setId(1L);
		fs.setLatitude(BigDecimal.valueOf(2.5d));
		fs.setLongitude(BigDecimal.valueOf(2.5d));
		fs.setStreetAddress("123 Test St");
		fs.setCity("Fitzgerald");
		fs.setStateCode(new FipsStateCode());
		fs.getStateCode().setCode("13");
		fs.getStateCode().setUspsCode("GA");

		OperatingStatusCode status = new OperatingStatusCode();
		status.setCode("OP");
		status.setDescription("Operating");
		fs.setOperatingStatusCode(status);

		FacilitySourceTypeCode fstc = new FacilitySourceTypeCode();
		fstc.setCode("999");
		fstc.setDescription("Ship Yards");
		fs.setFacilitySourceTypeCode(fstc);

		List<FacilitySiteContact> contacts = new ArrayList<>();
		FacilitySiteContact fsc = new FacilitySiteContact();
		fsc.setStreetAddress("123 Test St");
		fsc.setCity("Raleigh");
		fsc.setId(1L);
		fsc.setFacilitySite(fs);
		fsc.setFirstName("John");
		fsc.setLastName("Doe");
		fsc.setEmail("");

		ContactTypeCode contactTypeCode = new ContactTypeCode();
		contactTypeCode.setCode("FAC");
		fsc.setType(contactTypeCode);

		FipsStateCode stateCode = new FipsStateCode();
		stateCode.setCode("37");
		stateCode.setUspsCode("NC");
		fsc.setStateCode(stateCode);

		contacts.add(fsc);
		fs.setContacts(contacts);

		List<FacilityNAICSXref> facilityNAICS = new ArrayList<>();
		FacilityNAICSXref xref = new FacilityNAICSXref();
		xref.setFacilitySite(fs);
		xref.setId(1L);
		xref.setNaicsCodeType(NaicsCodeType.PRIMARY);
		NaicsCode naics = new NaicsCode();
		naics.setCode(123);
		naics.setDescription("ABCDE");
		xref.setNaicsCode(naics);

		fs.setFacilityNAICS(facilityNAICS);

		List<ReleasePoint> releasePoints = new ArrayList<>();
		ReleasePoint rp = new ReleasePoint();
		rp.setId(1L);
		rp.setFacilitySite(fs);
		rp.setReleasePointIdentifier("TestRP");
		rp.setComments("Comments");
		rp.setDescription("Test Description");
		rp.setLatitude(BigDecimal.valueOf(1111.000000));
		rp.setLongitude(BigDecimal.valueOf(1111.000000));

		status = new OperatingStatusCode();
		status.setCode("OP");
		status.setDescription("Operating");
		rp.setOperatingStatusCode(status);

		ReleasePointTypeCode releasePointTypeCode = new ReleasePointTypeCode();
		releasePointTypeCode.setCode("1");
		rp.setTypeCode(releasePointTypeCode);

		releasePoints.add(rp);
		fs.setReleasePoints(releasePoints);

		List<Control> controls = new ArrayList<>();
		Control control = new Control();
		control.setId(1L);
		control.setFacilitySite(fs);
		control.setIdentifier("Control 001");

		ControlMeasureCode controlMeasureCode = new ControlMeasureCode();
		controlMeasureCode.setCode("1");
		controlMeasureCode.setDescription("Control measure code description");
		control.setControlMeasureCode(controlMeasureCode);

		List<ControlPollutant> controlPollutant = new ArrayList<>();
		ControlPollutant cpollutant = new ControlPollutant();
		cpollutant.setControl(control);
		controlPollutant.add(cpollutant);

		List<ControlAssignment> assignments = new ArrayList<>();
		ControlAssignment ca = new ControlAssignment();
		ca.setControl(control);
		ca.setId(1L);
		ca.setSequenceNumber(1);
		ca.setPercentApportionment(BigDecimal.valueOf(50.0));

		ControlPath cp = new ControlPath();
		List<ControlAssignment> caSet = new ArrayList<>();
		caSet.add(ca);
		cp.setAssignments(caSet);
		cp.setId(9999991L);
		cp.setFacilitySite(fs);
		cp.setDescription("Test ControlPath");
		cp.setPathId("Test ControlPath");
		ca.setControlPath(cp);
		ca.setControlPathChild(cp);
		assignments.add(ca);

		control.setAssignments(assignments);
		controls.add(control);
		fs.setControls(controls);

		List<EmissionsUnit> units = new ArrayList<>();
		EmissionsUnit eu = new EmissionsUnit();
		eu.setId(1L);
		eu.setComments("Test Unit");
		eu.setFacilitySite(fs);
		eu.setUnitIdentifier("Unit 001");

		status = new OperatingStatusCode();
		status.setCode("OP");
		status.setDescription("Operating");
		eu.setOperatingStatusCode(status);

		UnitTypeCode unitTypeCode = new UnitTypeCode();
		unitTypeCode.setCode("100");
		unitTypeCode.setDescription("Boiler");
		eu.setUnitTypeCode(unitTypeCode);

		List<EmissionsProcess> processes = new ArrayList<>();
		EmissionsProcess ep = new EmissionsProcess();
		ep.setId(1L);
		ep.setEmissionsUnit(eu);
		ep.setEmissionsProcessIdentifier("Process 001");
		ep.setComments("Test Process Comments");
		ep.setSccCode("39999999");
		ep.setSccCategory(SccCategory.Point);

		status = new OperatingStatusCode();
		status.setCode("OP");
		status.setDescription("Operating");
		ep.setOperatingStatusCode(status);

		List<ReleasePointAppt> releasePointAppt = new ArrayList<>();
		ReleasePointAppt rpAppt = new ReleasePointAppt();
		rpAppt.setEmissionsProcess(ep);
		rpAppt.setReleasePoint(rp);
		rpAppt.setControlPath(cp);
		releasePointAppt.add(rpAppt);

		List<ReportingPeriod> reportingPeriods = new ArrayList<>();
		ReportingPeriod repPer = new ReportingPeriod();
		repPer.setId(1L);
		repPer.setComments("Reporting Period Comments");
		repPer.setEmissionsProcess(ep);
		repPer.setCalculationParameterValue(new BigDecimal(1000.0));

		ReportingPeriodCode rptc = new ReportingPeriodCode();
		rptc.setCode("A");
		repPer.setReportingPeriodTypeCode(rptc);

		EmissionsOperatingTypeCode eotc = new EmissionsOperatingTypeCode();
		eotc.setCode("R");
		repPer.setEmissionsOperatingTypeCode(eotc);

		CalculationParameterTypeCode cptc = new CalculationParameterTypeCode();
		cptc.setCode("E");
		repPer.setCalculationParameterTypeCode(cptc);

		UnitMeasureCode uom = new UnitMeasureCode();
		uom.setCode("FT");
		repPer.setCalculationParameterUom(uom);

		CalculationMaterialCode cMaterialc = new CalculationMaterialCode();
		cMaterialc.setCode("70");
		repPer.setCalculationMaterialCode(cMaterialc);

		List<Emission> emissions = new ArrayList<>();
		Emission e = new Emission();
		e.setId(1L);
		e.setReportingPeriod(repPer);
		e.setTotalEmissions(new BigDecimal(1000.0));
		e.setFormulaIndicator(false);
		e.setComments("Test Emission Comments");
		e.setTotalManualEntry(true);
        e.setNotReporting(false);

		uom = new UnitMeasureCode();
		uom.setCode("LB");
		e.setEmissionsUomCode(uom);

		Pollutant p = new Pollutant();
		p.setPollutantCode("199");
		e.setPollutant(p);

		CalculationMethodCode cMethodc = new CalculationMethodCode();
		cMethodc.setCode("10");
		e.setEmissionsCalcMethodCode(cMethodc);

		emissions.add(e);

		List<OperatingDetail> operatingDetails = new ArrayList<>();
		OperatingDetail opDetail = new OperatingDetail();
		opDetail.setId(1L);
		opDetail.setReportingPeriod(repPer);
		operatingDetails.add(opDetail);

		repPer.setEmissions(emissions);
		repPer.setOperatingDetails(operatingDetails);
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
