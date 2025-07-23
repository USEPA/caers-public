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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.epa.cef.web.config.AppPropertyName;
import gov.epa.cef.web.config.CefConfig;
import gov.epa.cef.web.config.SLTBaseConfig;
import gov.epa.cef.web.domain.EmissionsReport;
import gov.epa.cef.web.domain.FacilitySourceTypeCode;
import gov.epa.cef.web.domain.MonthlyFuelReporting;
import gov.epa.cef.web.domain.ReportStatus;
import gov.epa.cef.web.domain.ReportingPeriod;
import gov.epa.cef.web.domain.UserFacilityAssociation;
import gov.epa.cef.web.provider.system.AdminPropertyProvider;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.MonthlyFuelReportingRepository;
import gov.epa.cef.web.repository.PointSourceSccCodeRepository;
import gov.epa.cef.web.repository.ReportingPeriodRepository;
import gov.epa.cef.web.repository.UserFacilityAssociationRepository;
import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.MonthlyFuelReportingService;
import gov.epa.cef.web.service.NotificationService;
import gov.epa.cef.web.service.ReportingPeriodService;
import gov.epa.cef.web.service.dto.MonthlyFuelReportingDownloadDto;
import gov.epa.cef.web.service.dto.MonthlyFuelReportingDto;
import gov.epa.cef.web.service.dto.UserFacilityAssociationDto;
import gov.epa.cef.web.service.mapper.MonthlyFuelReportingDownloadMapper;
import gov.epa.cef.web.service.mapper.MonthlyFuelReportingMapper;
import gov.epa.cef.web.util.ConstantUtils;
import gov.epa.cef.web.util.SLTConfigHelper;

@Service
public class MonthlyFuelReportingServiceImpl implements MonthlyFuelReportingService {

	Logger logger = LoggerFactory.getLogger(MonthlyFuelReportingServiceImpl.class);

	@Autowired
    private MonthlyFuelReportingRepository monthlyRptRepo;

	@Autowired
    private ReportingPeriodRepository rptPeriodRepo;

	@Autowired
	private PointSourceSccCodeRepository sccRepo;

	@Autowired
    private EmissionsReportRepository erRepo;

	@Autowired
	private UserFacilityAssociationRepository userFacilityRepo;

	@Autowired
    private MonthlyFuelReportingMapper monthlyRptMapper;

	@Autowired
	private MonthlyFuelReportingDownloadMapper monthlyRptDownloadMapper;

	@Autowired
	private ReportingPeriodService rptPeriodSvc;

	@Autowired
    private EmissionsReportStatusServiceImpl reportStatusSvc;

	@Autowired
    private SLTConfigHelper sltConfigHelper;

	@Autowired
    private AdminPropertyProvider propertyProvider;

	@Autowired
    private CefConfig cefConfig;

	@Autowired
    private SecurityService securityService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
	private UserFacilityAssociationServiceImpl userFacilityAssocService;

	/**
     * Retrieve a list of monthly fuel reports for the given period (month/semi-annual/annual) and facility id
     * @param facilitySiteId
     * @param month
     * @return
     */
	public List<MonthlyFuelReportingDto> retrieveForMonthData(Long facilitySiteId, String period) {

		// retrieves all monthly reporting data for selected month
		List<MonthlyFuelReporting> monthlyFuelData = monthlyRptRepo.findByFacilitySiteId(facilitySiteId);
		List<MonthlyFuelReportingDto> monthlyFuelDataDto = new ArrayList<MonthlyFuelReportingDto>();

		for(MonthlyFuelReporting mfr: monthlyFuelData) {
			MonthlyFuelReportingDto dto = monthlyRptMapper.toDto(mfr);

			switch (period) {
				case ConstantUtils.JANUARY:
					dto.setFuelUseValue(mfr.january_fuelUseValue);
					dto.setTotalOperatingDays(mfr.january_totalOperatingDays);
					break;
				case ConstantUtils.FEBRUARY:
					dto.setFuelUseValue(mfr.february_fuelUseValue);
					dto.setTotalOperatingDays(mfr.february_totalOperatingDays);
					break;
				case ConstantUtils.MARCH:
					dto.setFuelUseValue(mfr.march_fuelUseValue);
					dto.setTotalOperatingDays(mfr.march_totalOperatingDays);
					break;
				case ConstantUtils.APRIL:
					dto.setFuelUseValue(mfr.april_fuelUseValue);
					dto.setTotalOperatingDays(mfr.april_totalOperatingDays);
					break;
				case ConstantUtils.MAY:
					dto.setFuelUseValue(mfr.may_fuelUseValue);
					dto.setTotalOperatingDays(mfr.may_totalOperatingDays);
					break;
				case ConstantUtils.JUNE:
					dto.setFuelUseValue(mfr.june_fuelUseValue);
					dto.setTotalOperatingDays(mfr.june_totalOperatingDays);
					break;
				case ConstantUtils.JULY:
					dto.setFuelUseValue(mfr.july_fuelUseValue);
					dto.setTotalOperatingDays(mfr.july_totalOperatingDays);
					break;
				case ConstantUtils.AUGUST:
					dto.setFuelUseValue(mfr.august_fuelUseValue);
					dto.setTotalOperatingDays(mfr.august_totalOperatingDays);
					break;
				case ConstantUtils.SEPTEMBER:
					dto.setFuelUseValue(mfr.september_fuelUseValue);
					dto.setTotalOperatingDays(mfr.september_totalOperatingDays);
					break;
				case ConstantUtils.OCTOBER:
					dto.setFuelUseValue(mfr.october_fuelUseValue);
					dto.setTotalOperatingDays(mfr.october_totalOperatingDays);
					break;
				case ConstantUtils.NOVEMBER:
					dto.setFuelUseValue(mfr.november_fuelUseValue);
					dto.setTotalOperatingDays(mfr.november_totalOperatingDays);
					break;
				case ConstantUtils.DECEMBER:
					dto.setFuelUseValue(mfr.december_fuelUseValue);
					dto.setTotalOperatingDays(mfr.december_totalOperatingDays);
					break;
				case ConstantUtils.SEMIANNUAL:
					dto.setFuelUseValue(mfr.semiAnnual_fuelUseValue);
					dto.setTotalOperatingDays(mfr.semiAnnual_totalOperatingDays);
					break;
				case ConstantUtils.ANNUAL:
					dto.setFuelUseValue(mfr.annual_fuelUseValue);
					dto.setTotalOperatingDays(mfr.annual_totalOperatingDays);
					break;
				default:
			}
			monthlyFuelDataDto.add(dto);
		}

		// compare data from all reporting periods with scc that require fuel use to data for selected month
		List<MonthlyFuelReportingDto> reportingPeriods = retrieveMonthlyReportingReportingPeriodsForFacilitySite(facilitySiteId, monthlyFuelDataDto);

		return reportingPeriods.stream().sorted((i1, i2) -> i1.getUnitIdentifier().compareToIgnoreCase(i2.getUnitIdentifier()))
                .collect(Collectors.toList());
	}

	/**
     * Retrieve a all reporting periods for monthly reporting that has an SCC that requires fuel use or is asphalt
     * for the given period and facility id
     * @param facilitySiteId
     * @param monthlyFuelData
     * @return
     */
	public List<MonthlyFuelReportingDto> retrieveMonthlyReportingReportingPeriodsForFacilitySite(Long facilitySiteId, List<MonthlyFuelReportingDto> monthlyFuelData) {

		List<String> sccReqFuelAndAsphalt = sccRepo.findByMonthlyReporting(true).stream().map(scc -> scc.getCode()).distinct()
				.collect(Collectors.toList());

		// retrieves all reporting periods for a facility that has scc in sccReqFuelAndAsphalt list
		List<ReportingPeriod> entities = rptPeriodRepo.findByFacilitySiteId(facilitySiteId).stream()
				.filter(rp -> !ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.equals(rp.getEmissionsProcess().getOperatingStatusCode().getCode()))
                .filter(rp -> {
                    FacilitySourceTypeCode typeCode = rp.getEmissionsProcess().getEmissionsUnit().getFacilitySite().getFacilitySourceTypeCode();
                    return !ConstantUtils.STATUS_PERMANENTLY_SHUTDOWN.equals(rp.getEmissionsProcess().getEmissionsUnit().getOperatingStatusCode().getCode())
                            || (typeCode != null && ConstantUtils.FACILITY_SOURCE_LANDFILL_CODE.contentEquals(typeCode.getCode()));
                    })
				.filter(rp -> rp.getEmissionsProcess().getSccCode() != null
						&& sccReqFuelAndAsphalt.contains(rp.getEmissionsProcess().getSccCode()))
				.collect(Collectors.toList());

		List<MonthlyFuelReportingDto> result = monthlyRptMapper.rptPeriodToDtoList(entities);

		List<Long> reportingPeriodIds = monthlyFuelData.stream()
				.map(rp -> rp.getReportingPeriodId()).distinct()
				.collect(Collectors.toList());

		// add reporting period data if reporting data is not in monthly reporting table
		if (!monthlyFuelData.isEmpty()) {
			result.forEach(rp -> {
				if (!reportingPeriodIds.contains(rp.getReportingPeriodId())) {
					monthlyFuelData.add(rp);
				}
			});
			return monthlyFuelData;
		}
		return result;
	}

    /**
     * Groups fuel material data for all months for review period (semi/annual) and
     * sums fuel value and operating days
     * @param monthlyReports
     * @return
     */
    public List<MonthlyFuelReporting> calculateTotals(List<MonthlyFuelReporting> monthlyReports) {
    	for (MonthlyFuelReporting mr: monthlyReports) {

    		List<BigDecimal> semiAnnualFuelValueList = Arrays.asList(mr.getJanuary_fuelUseValue(), mr.getFebruary_fuelUseValue(), mr.getMarch_fuelUseValue(), mr.getApril_fuelUseValue(), mr.getMay_fuelUseValue(), mr.getJune_fuelUseValue())
    														.stream().filter(val -> val != null).collect(Collectors.toList());
    		List<BigDecimal> semiAnnualOpDaysList = Arrays.asList(mr.getJanuary_totalOperatingDays(), mr.getFebruary_totalOperatingDays(), mr.getMarch_totalOperatingDays(), mr.getApril_totalOperatingDays(), mr.getMay_totalOperatingDays(), mr.getJune_totalOperatingDays())
    														.stream().filter(val -> val != null).collect(Collectors.toList());
    		BigDecimal semiAnnualFuelValue = null;
    		BigDecimal semiAnnualOpDays = null;

    		if (!semiAnnualFuelValueList.isEmpty()) {
    			semiAnnualFuelValue = semiAnnualFuelValueList.stream()
        				.reduce(BigDecimal.ZERO, BigDecimal::add);
    		}

    		if (!semiAnnualOpDaysList.isEmpty()) {
    			semiAnnualOpDays = semiAnnualOpDaysList.stream()
					.reduce(BigDecimal.ZERO, BigDecimal::add);
    		}

	    	List<BigDecimal> annualFuelValueList = Arrays.asList(semiAnnualFuelValue, mr.getJuly_fuelUseValue(), mr.getAugust_fuelUseValue(), mr.getSeptember_fuelUseValue(), mr.getOctober_fuelUseValue(), mr.getNovember_fuelUseValue(), mr.getDecember_fuelUseValue())
	    												.stream().filter(val -> val != null).collect(Collectors.toList());
			List<BigDecimal> annualOpDaysList = Arrays.asList(semiAnnualOpDays, mr.getJuly_totalOperatingDays(), mr.getAugust_totalOperatingDays(), mr.getSeptember_totalOperatingDays(), mr.getOctober_totalOperatingDays(), mr.getNovember_totalOperatingDays(), mr.getDecember_totalOperatingDays())
														.stream().filter(val -> val != null).collect(Collectors.toList());
			BigDecimal annualFuelValue = null;
			BigDecimal annualOpDays = null;

			if (!annualFuelValueList.isEmpty()) {
				annualFuelValue = annualFuelValueList.stream()
						.reduce(BigDecimal.ZERO, BigDecimal::add);
			}

			if (!annualOpDaysList.isEmpty()) {
				annualOpDays = annualOpDaysList.stream()
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			}

	    	mr.setSemiAnnual_fuelUseValue(semiAnnualFuelValue);
	    	mr.setSemiAnnual_totalOperatingDays(semiAnnualOpDays);
	    	mr.setAnnual_fuelUseValue(annualFuelValue);
	    	mr.setAnnual_totalOperatingDays(annualOpDays);

    	}

    	return monthlyReports;
    }

    /**
     * Saves all monthly fuel data and updates associated fuel value in process reporting period
     * @param facilitySiteId
     * @param dtos
     * @return
     */
    public List<MonthlyFuelReportingDto> saveMonthlyReporting(Long facilitySiteId, List<MonthlyFuelReportingDto> dtos) {
    	List<MonthlyFuelReporting> periods = new ArrayList<MonthlyFuelReporting>();

    	Map<Object, List<MonthlyFuelReportingDto>> groupedReportingPeriods = dtos.stream()
        		.collect(Collectors.groupingBy(mr -> mr.getReportingPeriodId()));

        for(List<MonthlyFuelReportingDto> dtoList :groupedReportingPeriods.values()) {
        	MonthlyFuelReporting mfr = monthlyRptRepo.findByReportingPeriodId(dtoList.get(0).getReportingPeriodId());

        	if (mfr == null) {
        		monthlyRptRepo.save(monthlyRptMapper.fromDto(dtoList.get(0)));
        		mfr = monthlyRptRepo.findByReportingPeriodId(dtoList.get(0).getReportingPeriodId());
        	}

			periods.add(update(mfr, dtoList));
        }
        periods = calculateTotals(periods);

        rptPeriodSvc.monthlyReportingUpdateReportingPeriod(periods);

        ReportStatus midYearStatus = periods.get(0).getReportingPeriod().getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport().getMidYearSubmissionStatus();
        if (midYearStatus == null) {
        	//reportStatusSvc.setSemiAnnualReports(periods.get(0).getReportingPeriod().getEmissionsProcess().getEmissionsUnit().getFacilitySite().getEmissionsReport().getId(), ReportStatus.IN_PROGRESS);
        }
    	return monthlyRptMapper.toDtoList((List<MonthlyFuelReporting>) monthlyRptRepo.saveAll(periods));
    }

    /**
     * delete monthly report by process id
     * @param id
     * @return
     */
    public void deleteByProcess(Long id) {
    	monthlyRptRepo.deleteByProcess(id);
    }

    /**
     * delete monthly report by unit id
     * @param id
     * @return
     */
    public void deleteByUnit(Long id) {
    	monthlyRptRepo.deleteByUnit(id);
    }

    /**
     * Updates all monthly fuel values and total op days for each process reporting period in monthly reporting
     * @param mfr
     * @param dtos
     * @return
     */
    public MonthlyFuelReporting update(MonthlyFuelReporting mfr, List<MonthlyFuelReportingDto> dtos) {

    	dtos.forEach(dto -> {
	    	switch (dto.getPeriod()) {
		    	case ConstantUtils.JANUARY:
					mfr.setJanuary_fuelUseValue(dto.getFuelUseValue());
					break;
				case ConstantUtils.FEBRUARY:
					mfr.setFebruary_fuelUseValue(dto.getFuelUseValue());
					break;
				case ConstantUtils.MARCH:
					mfr.setMarch_fuelUseValue(dto.getFuelUseValue());
					break;
				case ConstantUtils.APRIL:
					mfr.setApril_fuelUseValue(dto.getFuelUseValue());
					break;
				case ConstantUtils.MAY:
					mfr.setMay_fuelUseValue(dto.getFuelUseValue());
					break;
				case ConstantUtils.JUNE:
					mfr.setJune_fuelUseValue(dto.getFuelUseValue());
					break;
				case ConstantUtils.JULY:
					mfr.setJuly_fuelUseValue(dto.getFuelUseValue());
					break;
				case ConstantUtils.AUGUST:
					mfr.setAugust_fuelUseValue(dto.getFuelUseValue());
					break;
				case ConstantUtils.SEPTEMBER:
					mfr.setSeptember_fuelUseValue(dto.getFuelUseValue());
					break;
				case ConstantUtils.OCTOBER:
					mfr.setOctober_fuelUseValue(dto.getFuelUseValue());
					break;
				case ConstantUtils.NOVEMBER:
					mfr.setNovember_fuelUseValue(dto.getFuelUseValue());
					break;
				case ConstantUtils.DECEMBER:
					mfr.setDecember_fuelUseValue(dto.getFuelUseValue());
					break;
				default:
	    	};

	    	switch (dto.getPeriod()) {
				case ConstantUtils.JANUARY:
					mfr.setJanuary_totalOperatingDays(dto.getTotalOperatingDays());
					break;
				case ConstantUtils.FEBRUARY:
					mfr.setFebruary_totalOperatingDays(dto.getTotalOperatingDays());
					break;
				case ConstantUtils.MARCH:
					mfr.setMarch_totalOperatingDays(dto.getTotalOperatingDays());
					break;
				case ConstantUtils.APRIL:
					mfr.setApril_totalOperatingDays(dto.getTotalOperatingDays());
					break;
				case ConstantUtils.MAY:
					mfr.setMay_totalOperatingDays(dto.getTotalOperatingDays());
					break;
				case ConstantUtils.JUNE:
					mfr.setJune_totalOperatingDays(dto.getTotalOperatingDays());
					break;
				case ConstantUtils.JULY:
					mfr.setJuly_totalOperatingDays(dto.getTotalOperatingDays());
					break;
				case ConstantUtils.AUGUST:
					mfr.setAugust_totalOperatingDays(dto.getTotalOperatingDays());
					break;
				case ConstantUtils.SEPTEMBER:
					mfr.setSeptember_totalOperatingDays(dto.getTotalOperatingDays());
					break;
				case ConstantUtils.OCTOBER:
					mfr.setOctober_totalOperatingDays(dto.getTotalOperatingDays());
					break;
				case ConstantUtils.NOVEMBER:
					mfr.setNovember_totalOperatingDays(dto.getTotalOperatingDays());
					break;
				case ConstantUtils.DECEMBER:
					mfr.setDecember_totalOperatingDays(dto.getTotalOperatingDays());
					break;
				default:
			};
    	});
    	return mfr;
    }

    public List<MonthlyFuelReportingDownloadDto> retrieveDownloadDtoByFacilitySiteId(Long facilitySiteId) {

    	List<MonthlyFuelReporting> mfrs = monthlyRptRepo.findByFacilitySiteId(facilitySiteId);
    	return monthlyRptDownloadMapper.toDtoList(mfrs);
    }

}
