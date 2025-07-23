package gov.epa.cef.web.service.impl;

import gov.epa.cef.web.config.SLTBaseConfig;
import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.repository.ReportSummaryRepository;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.EmissionService;
import gov.epa.cef.web.service.ReportingPeriodService;
import gov.epa.cef.web.service.dto.MonthlyReportingEmissionDto;
import gov.epa.cef.web.service.dto.MonthlyReportingEmissionHolderDto;
import gov.epa.cef.web.service.dto.MonthlyReportingPeriodHolderDto;
import gov.epa.cef.web.util.ConstantUtils;
import gov.epa.cef.web.util.SLTConfigHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CorServiceImpl {

    @Autowired
    private EmissionsReportRepository erRepo;

    @Autowired
    private ReportSummaryRepository reportSummaryRepo;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private EmissionService emissionService;

    @Autowired
    private ReportingPeriodService rpService;

    @Autowired
    private SLTConfigHelper sltConfigHelper;

    @Autowired
    private TemplateEngine templateEngine;

    public static final String RADIONUCLIDES = "605";

    public List<MonthlyReportingPeriodHolderDto> getMonthlyReportingOperatingDetailsForFacility(Long facilitySiteId, boolean semiannual) {
        List<MonthlyReportingPeriodHolderDto> list;

        if (semiannual) {
            list = rpService.retrieveAllSemiAnnualMonthlyReportingPeriodsForFacilitySite(facilitySiteId);
        }
        else {
            list = rpService.retrieveAllAnnualMonthlyReportingPeriodsForFacilitySite(facilitySiteId);
        }
        if (list != null && list.size() > 1) {
            list = list.stream().sorted(Comparator.comparing(MonthlyReportingPeriodHolderDto::getUnitIdentifier))
                    .collect(Collectors.toList());
        }

        return list;
    }

    public List<MonthlyReportingEmissionHolderDto> getMonthlyReportingEmissionsForFacility(Long facilitySiteId, boolean semiannual) {

        List<MonthlyReportingEmissionHolderDto> list;
        if (semiannual) {
            list = emissionService.retrieveAllSemiAnnualMonthlyEmissionsForFacilitySite(facilitySiteId);
        }
        else {
            list = emissionService.retrieveAllAnnualMonthlyEmissionsForFacilitySite(facilitySiteId);
        }

        if (list != null && list.size() > 1) {
            list = list.stream().sorted(Comparator.comparing(MonthlyReportingEmissionHolderDto::getUnitIdentifier))
                    .collect(Collectors.toList());

            for (MonthlyReportingEmissionHolderDto holder : list) {
                holder.setEmissions(sortMonthlyEmissionsByPollutantName(holder.getEmissions()));
            }
        }

        return list;
    }

    private List<MonthlyReportingEmissionDto> sortMonthlyEmissionsByPollutantName(List<MonthlyReportingEmissionDto> emissions) {

        if (emissions != null && emissions.size() > 1) {
            emissions = emissions.stream().sorted(Comparator.comparing(i -> i.getPollutant().getPollutantName()))
                    .collect(Collectors.toList());
        }

        return emissions;
    }

    private List<BigDecimal> calculateEmissionsTotals(List<ReportSummary> reportSummary) {
        List<BigDecimal> totals = new ArrayList<BigDecimal>();

        BigDecimal currentYearTotal = new BigDecimal(0);
        BigDecimal previousYearTotal = new BigDecimal(0);

        for (ReportSummary summary : reportSummary) {
            currentYearTotal = currentYearTotal.add(summary.getEmissionsTonsTotal());
            if (summary.getPreviousYearTonsTotal() != null) {
                previousYearTotal = previousYearTotal.add(summary.getPreviousYearTonsTotal());
            }
        }

        totals.add(currentYearTotal);
        totals.add(previousYearTotal);

        return totals;
    }

    public Map<String, Object> generateCopyOfRecordMap(Long reportId, boolean isSemiannual, boolean signed) {

        if (isSemiannual) {
            return generateSemiannualCopyOfRecordMap(reportId, signed);
        } else {
            return generateCopyOfRecordMap(reportId, signed);
        }
    }

    private Map<String, Object> generateCopyOfRecordMap(Long reportId, boolean generate) {

        Map<String, Object> model = new HashMap<>();

        EmissionsReport er = erRepo.findById(reportId).orElse(null);
        List<ReportSummary> reportSummary = reportSummaryRepo.findByReportId(reportId);
        List<ReportSummary> reportSummaryTon = reportSummary.stream().filter(e -> !RADIONUCLIDES.equals(e.getPollutantCode())).collect(Collectors.toList());
        List<ReportSummary> reportSummaryCuries = reportSummary.stream().filter(e -> RADIONUCLIDES.equals(e.getPollutantCode())).collect(Collectors.toList());

        List<BigDecimal> emissionsTotalsTons = calculateEmissionsTotals(reportSummaryTon);

        model.put("report", er);
        model.put("fs", er.getFacilitySites().get(0));
        model.put("reportSummary", reportSummary);
        model.put("reportSummaryTon", reportSummaryTon);
        model.put("reportSummaryCuries", reportSummaryCuries);
        model.put("user", securityService.getCurrentApplicationUser().getName());
        model.put("currentYearTotalEmissions", emissionsTotalsTons.get(0));
        model.put("previousYearTotalEmissions", emissionsTotalsTons.get(1));

        SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(er.getProgramSystemCode().getCode());

        List<String> annualPeriods = null;
        Short monthlyInitialYear = sltConfig.getSltMonthlyFuelReportingInitialYear();
        if (Boolean.TRUE.equals(sltConfig.getSltMonthlyFuelReportingEnabled())
                && (monthlyInitialYear == null || er.getYear() >= monthlyInitialYear)) {

            model.put("opDetails", getMonthlyReportingOperatingDetailsForFacility(er.getFacilitySites().get(0).getId(), false));
            model.put("emissionHolders", getMonthlyReportingEmissionsForFacility(er.getFacilitySites().get(0).getId(), false));

            annualPeriods = new ArrayList<>(ConstantUtils.ALL_MONTHS);
            annualPeriods.add(ConstantUtils.ANNUAL);
        }
        model.put("monthlyPeriods", annualPeriods);

        if (Boolean.TRUE.equals(generate)) {
            model.put("generate", true);
        } else {
            model.put("generate", false);
        }

        return model;
    }

    private Map<String, Object> generateSemiannualCopyOfRecordMap(Long reportId, Boolean generate) {

        Map<String, Object> model = new HashMap<>();

        EmissionsReport er = erRepo.findById(reportId).orElse(null);

        model.put("report", er);
        model.put("fs", er.getFacilitySites().get(0));
        model.put("user", securityService.getCurrentApplicationUser().getName());
        model.put("opDetails", getMonthlyReportingOperatingDetailsForFacility(er.getFacilitySites().get(0).getId(), true));
        model.put("emissionHolders", getMonthlyReportingEmissionsForFacility(er.getFacilitySites().get(0).getId(), true));

        List<String> semiannualPeriods = new ArrayList<>(ConstantUtils.SEMI_ANNUAL_MONTHS);
        semiannualPeriods.add(ConstantUtils.SEMIANNUAL);
        model.put("monthlyPeriods", semiannualPeriods);

        if (Boolean.TRUE.equals(generate)) {
            model.put("generate", true);
        } else {
            model.put("generate", false);
        }

        return model;
    }

    public String generateHTMLCopyOfRecord(Long reportId, boolean isSemiannual, boolean signed) {

        Context context = new Context();

        if (isSemiannual) {
            context.setVariables(generateSemiannualCopyOfRecordMap(reportId, signed));
            return templateEngine.process("cor/semiannualCopyOfRecord", context);
        } else {
            context.setVariables(generateCopyOfRecordMap(reportId, signed));
            return templateEngine.process("cor/copyOfRecord", context);
        }

    }
}
