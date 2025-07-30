/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.epa.cef.web.config.SLTBaseConfig;
import gov.epa.cef.web.config.SLTPropertyName;
import gov.epa.cef.web.domain.ReportStatus;
import gov.epa.cef.web.domain.SubmissionsReviewDashboardView;
import gov.epa.cef.web.domain.SubmissionsReviewNotStartedDashboardView;
import gov.epa.cef.web.provider.system.SLTPropertyProvider;
import gov.epa.cef.web.repository.SubmissionsReviewDashboardRepository;
import gov.epa.cef.web.service.SubmissionsReviewDasboardService;
import gov.epa.cef.web.service.UserService;
import gov.epa.cef.web.service.dto.SubmissionsReviewDashboardDto;
import gov.epa.cef.web.service.dto.UserDto;
import gov.epa.cef.web.service.mapper.SubmissionsReviewDashboardMapper;
import gov.epa.cef.web.util.DateUtils;
import gov.epa.cef.web.util.SLTConfigHelper;

/**
 * @author ahmahfou
 *
 */
@Service
public class SubmissionsReviewDasboardServiceImpl implements SubmissionsReviewDasboardService{

    @Autowired
    private SubmissionsReviewDashboardRepository repo;

    @Autowired
    private SubmissionsReviewDashboardMapper mapper;

    @Autowired
    private UserService userService;

    @Autowired
    private SLTConfigHelper sltConfigHelper;

    @Autowired
    private SLTPropertyProvider sltPropertyProvider;

    @PersistenceContext
    public EntityManager em;

    private final String OPT_OUT_SUBMITTED = "OPT_OUT_SUBMITTED";
    private final String OPT_OUT_APPROVED = "OPT_OUT_APPROVED";

    public List<SubmissionsReviewDashboardDto> retrieveReviewerFacilityReports(Short reportYear, String reportStatus) {
        UserDto currentUser=userService.getCurrentUser();
        return retrieveFacilityReports(reportYear, reportStatus, currentUser.getProgramSystemCode());
    }

    public List<SubmissionsReviewDashboardDto> retrieveFacilityReports(Short reportYear, String reportStatus, String programSystemCode) {

    	 Boolean optOutEnabled = sltPropertyProvider.getBoolean(SLTPropertyName.SLTFeatureThresholdScreeningGADNREnabled, programSystemCode);

    	ReportStatus status = null;
    	if (reportStatus != null) {
    		if (EnumUtils.isValidEnum(ReportStatus.class, reportStatus)) {
    			status = ReportStatus.valueOf(reportStatus);
    		} else {
    			status = reportStatus.equals(OPT_OUT_APPROVED) ? ReportStatus.APPROVED: ReportStatus.SUBMITTED;
    		}
    	}
    	List<SubmissionsReviewDashboardView> reportsList = null;

		// retrieve reports when specific report year and specific report status is selected
        if (reportYear != null && status != null) {

        	if (reportStatus.equals(OPT_OUT_APPROVED) || reportStatus.equals(OPT_OUT_SUBMITTED)) {
        		// Retrieve all reports for selected status and year that have OPERATING_BELOW_THRESHOLD threshold status.
        		reportsList = repo.findByYearAndReportStatusAndProgramSystemCodeAndIsOptOut(reportYear, status, programSystemCode);
        	} else if (optOutEnabled && (ReportStatus.SUBMITTED.equals(status) || ReportStatus.APPROVED.equals(status))) {
        		// Retrieve all reports for selected status and year that does not have OPERATING_BELOW_THRESHOLD threshold status. If SLT threshold screen is disabled threshold status is null.
        		reportsList = repo.findByYearAndReportStatusAndProgramSystemCodeAndNotOptOut(reportYear, status, programSystemCode);
        	} else {
        		// Retrieve all reports for selected status regardless of threshold status
        		reportsList = repo.findByYearAndReportStatusAndProgramSystemCode(reportYear, status, programSystemCode);
        	}

        // retrieve all reports for selected year
        } else if (reportYear != null) {
            short currentReportingYear =  DateUtils.getCurrentReportingYear().shortValue();

        	if (reportYear == currentReportingYear || reportYear == currentReportingYear + 1) {
        		return retrieveFacilityAllReportsWithNotStarted(reportYear, programSystemCode);
        	} else {
        		reportsList = repo.findByYearAndProgramSystemCode(reportYear, programSystemCode);
        	}

        // retrieve reports when specific report status is and all years is selected
        } else if (status != null) {

        	if (reportStatus.equals(OPT_OUT_APPROVED) || reportStatus.equals(OPT_OUT_SUBMITTED)) {
        		// Retrieve all reports for selected status that have OPERATING_BELOW_THRESHOLD threshold status.
        		reportsList = repo.findByReportStatusAndProgramSystemCodeAndIsOptOut(status, programSystemCode);
        	} else if (optOutEnabled && (ReportStatus.SUBMITTED.equals(status) || ReportStatus.APPROVED.equals(status))) {
        		// Retrieve all reports for selected status that does not have OPERATING_BELOW_THRESHOLD threshold status. If SLT threshold screen is disabled threshold status is null.
        		reportsList = repo.findByReportStatusAndProgramSystemCodeAndNotOptOut(status, programSystemCode);
        	} else {
        		// Retrieve all reports for selected status regardless of threshold status
        		reportsList = repo.findByReportStatusAndProgramSystemCode(status, programSystemCode);
        	}

        } else {
            reportsList = repo.findByProgramSystemCode(programSystemCode);
        }

        return mapper.toDtoList(reportsList);
    }

    public List<SubmissionsReviewDashboardDto> retrieveReviewerFacilityNotStartedReports(Short reportYear) {
        UserDto currentUser=userService.getCurrentUser();
        return retrieveFacilityNotStartedReports(reportYear, currentUser.getProgramSystemCode());
    }

    public List<SubmissionsReviewDashboardDto> retrieveFacilityNotStartedReports(Short reportYear, String programSystemCode) {
		@SuppressWarnings("unchecked")
		List<SubmissionsReviewNotStartedDashboardView> reportsList = em.createNamedQuery("notStartedReportsResults")
        		.setParameter("year", reportYear)
        		.setParameter("programSystemCode", programSystemCode)
        		.getResultList();
        return mapper.notStartedToDtoList(reportsList);
    }

    // Retrieve all reports and not started facilities
    public List<SubmissionsReviewDashboardDto> retrieveFacilityAllReportsWithNotStarted(Short reportYear, String programSystemCode) {
    	@SuppressWarnings("unchecked")
		List<SubmissionsReviewNotStartedDashboardView> reportsList = em.createNamedQuery("allReportsWithNotStartedResults")
        		.setParameter("year", reportYear)
        		.setParameter("programSystemCode", programSystemCode)
        		.getResultList();
        return mapper.notStartedToDtoList(reportsList);
    }

    public List<SubmissionsReviewDashboardDto> retrieveReviewerSemiAnnualFacilityReports(Short reportYear, ReportStatus midYearSubmissionStatus) {
        UserDto currentUser = userService.getCurrentUser();
        List<SubmissionsReviewDashboardDto> result= new ArrayList<SubmissionsReviewDashboardDto>();
        SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(currentUser.getProgramSystemCode());
        Short monthlyReportingInitialYear = sltConfig.getSltMonthlyFuelReportingInitialYear();

        if (Boolean.TRUE.equals(sltConfig.getSltMonthlyFuelReportingEnabled())
            && (monthlyReportingInitialYear == null || reportYear >= monthlyReportingInitialYear)) {
        	result = retrieveSemiAnnualFacilityReports(reportYear, midYearSubmissionStatus, currentUser.getProgramSystemCode());
        }
        return result;
    }

    public List<SubmissionsReviewDashboardDto> retrieveSemiAnnualFacilityReports(Short reportYear, ReportStatus midYearSubmissionStatus, String programSystemCode) {

        List<SubmissionsReviewDashboardView> reportsList;

        if (reportYear != null && midYearSubmissionStatus != null) {
            reportsList = repo.findByYearAndMidYearSubmissionStatusAndProgramSystemCode(reportYear, midYearSubmissionStatus, programSystemCode);
        } else if (reportYear != null) {
            reportsList = repo.findByYearAndProgramSystemCode(reportYear, programSystemCode);
        } else if (midYearSubmissionStatus != null) {
            reportsList = repo.findByMidYearSubmissionStatusAndProgramSystemCode(midYearSubmissionStatus, programSystemCode);
        } else {
            reportsList = repo.findByProgramSystemCode(programSystemCode);
        }

        return mapper.toDtoList(reportsList);
    }

    public List<SubmissionsReviewNotStartedDashboardView> retrieveFacilityReportingStatusReport(Short reportYear, String programSystemCode) {
        @SuppressWarnings("unchecked")
        List<SubmissionsReviewNotStartedDashboardView> reportsList = em.createNamedQuery("allReportsWithNotStartedResults")
            .setParameter("year", reportYear)
            .setParameter("programSystemCode", programSystemCode)
            .getResultList();
        return reportsList;
    }
}
