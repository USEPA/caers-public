package gov.epa.cef.web.controller;

import gov.epa.cef.web.domain.*;
import gov.epa.cef.web.repository.EmissionsReportRepository;
import gov.epa.cef.web.security.SecurityService;
import gov.epa.cef.web.service.impl.CorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

@Controller
public class CorController  {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private CorServiceImpl corService;

    @GetMapping("/cor/{reportId}")
    public String cor(Model model, @NotNull @PathVariable Long reportId, @RequestParam(required = false) Boolean generate) {

        this.securityService.facilityEnforcer().enforceEntity(reportId, EmissionsReportRepository.class);

        Map<String, Object> map = this.corService.generateCopyOfRecordMap(reportId, false, generate);

        model.addAllAttributes(map);

        return "cor/copyOfRecord";
    }

    @GetMapping("/semiannual/cor/{reportId}")
    public String semiannualCor(Model model, @NotNull @PathVariable Long reportId, @RequestParam(required = false) Boolean generate) {

        this.securityService.facilityEnforcer().enforceEntity(reportId, EmissionsReportRepository.class);

        Map<String, Object> map = this.corService.generateCopyOfRecordMap(reportId, true, generate);

        model.addAllAttributes(map);

        return "cor/semiannualCopyOfRecord";
    }

    public static List<FacilityNAICSXref> getSortedNaicsList(List<FacilityNAICSXref> list) {
        List<FacilityNAICSXref> result = list;
        if (list != null && list.size() > 1) {
            result = list.stream().sorted((i1, i2) -> i1.getNaicsCodeType().code().compareTo(i2.getNaicsCodeType().code()))
                .collect(Collectors.toList());
        }
        return result;
    }

    public static List<EmissionsUnit> getSortedUnitList(List<EmissionsUnit> list) {
        List<EmissionsUnit> result = list;
        if (list != null && list.size() > 1) {
            result = list.stream().sorted((i1, i2) -> i1.getUnitIdentifier().compareTo(i2.getUnitIdentifier()))
                .collect(Collectors.toList());
        }
        return result;
    }

    public static List<EmissionsProcess> getSortedProcessList(List<EmissionsProcess> list) {
        List<EmissionsProcess> result = list;
        if (list != null && list.size() > 1) {
            result = list.stream().sorted((i1, i2) -> i1.getEmissionsProcessIdentifier().compareTo(i2.getEmissionsProcessIdentifier()))
                .collect(Collectors.toList());
        }
        return result;
    }

    public static List<Emission> getSortedEmissionsList(List<Emission> list) {
        List<Emission> result = list;
        if (list != null && list.size() > 1) {
            result = list.stream().sorted((i1, i2) -> i1.getPollutant().getPollutantName().compareTo(i2.getPollutant().getPollutantName()))
                .collect(Collectors.toList());
        }
        return result;
    }

    public static List<ReleasePoint> getSortedReleasePointList(List<ReleasePoint> list) {
        List<ReleasePoint> result = list;
        if (list != null && list.size() > 1) {
            result = list.stream().sorted((i1, i2) -> i1.getReleasePointIdentifier().compareTo(i2.getReleasePointIdentifier()))
                .collect(Collectors.toList());
        }
        return result;
    }

    public static List<ControlPath> getSortedControlPathList(List<ControlPath> list) {
        List<ControlPath> result = list;
        if (list != null && list.size() > 1) {
            result = list.stream().sorted((i1, i2) -> i1.getPathId().compareTo(i2.getPathId()))
                .collect(Collectors.toList());
        }
        return result;
    }

    public static List<Control> getSortedControlList(List<Control> list) {
        List<Control> result = list;
        if (list != null && list.size() > 1) {
            result = list.stream().sorted((i1, i2) -> i1.getIdentifier().compareTo(i2.getIdentifier()))
                .collect(Collectors.toList());
        }
        return result;
    }

    public static List<ControlPathPollutant> getSortedPathPollutantList(List<ControlPathPollutant> list) {
        List<ControlPathPollutant> result = list;
        if (list != null && list.size() > 1) {
            result = list.stream().sorted((i1, i2) -> i1.getPollutant().getPollutantName().compareTo(i2.getPollutant().getPollutantName()))
                .collect(Collectors.toList());
        }
        return result;
    }

    public static List<ControlPollutant> getSortedControlPollutantList(List<ControlPollutant> list) {
        List<ControlPollutant> result = list;
        if (list != null && list.size() > 1) {
            result = list.stream().sorted((i1, i2) -> i1.getPollutant().getPollutantName().compareTo(i2.getPollutant().getPollutantName()))
                .collect(Collectors.toList());
        }
        return result;
    }

    public static List<ReleasePointAppt> getSortedReleasePointApptList(List<ReleasePointAppt> list) {
        List<ReleasePointAppt> result = list;
        if (list != null && list.size() > 1) {
            result = list.stream().sorted((i1, i2) -> i1.getReleasePoint().getReleasePointIdentifier().compareTo(i2.getReleasePoint().getReleasePointIdentifier()))
                .collect(Collectors.toList());
        }
        return result;
    }

    public static List<ControlAssignment> getSortedControlAssignmentList(List<ControlAssignment> list) {
        List<ControlAssignment> result = list;
        if (list != null && list.size() > 1) {
            result = list.stream().sorted((i1, i2) -> i1.getSequenceNumber().compareTo(i2.getSequenceNumber()))
                .collect(Collectors.toList());
        }
        return result;
    }

    public static BigDecimal stripTrailingZeros(BigDecimal number) {
        return number.stripTrailingZeros();
    }

}
