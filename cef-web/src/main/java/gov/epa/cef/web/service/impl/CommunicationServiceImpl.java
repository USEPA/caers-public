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
import java.util.Map;
import java.util.stream.Collectors;

import gov.epa.cef.web.security.AppRole;
import gov.epa.cef.web.service.SubmissionsReviewDasboardService;
import gov.epa.cef.web.service.dto.SubmissionsReviewDashboardDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import gov.epa.cef.web.config.CefConfig;
import gov.epa.cef.web.config.SLTBaseConfig;
import gov.epa.cef.web.domain.Communication;
import gov.epa.cef.web.domain.UserFacilityAssociation;
import gov.epa.cef.web.repository.AttachmentRepository;
import gov.epa.cef.web.repository.CommunicationRepository;
import gov.epa.cef.web.repository.ProgramSystemCodeRepository;
import gov.epa.cef.web.repository.UserFacilityAssociationRepository;
import gov.epa.cef.web.service.CommunicationService;
import gov.epa.cef.web.service.NotificationService;
import gov.epa.cef.web.service.UserService;
import gov.epa.cef.web.service.dto.CommunicationDto;
import gov.epa.cef.web.service.dto.UserDto;
import gov.epa.cef.web.service.dto.UserFacilityAssociationDto;
import gov.epa.cef.web.service.mapper.CommunicationMapper;
import gov.epa.cef.web.util.SLTConfigHelper;

@Service
public class CommunicationServiceImpl implements CommunicationService {

	private static final Logger logger = LoggerFactory.getLogger(CommunicationServiceImpl.class);

	@Autowired
	private UserService userService;

	@Autowired
	private CommunicationMapper commMapper;

	@Autowired
	private CommunicationRepository commRepo;

	@Autowired
	private UserFacilityAssociationRepository userFacilityRepo;

	@Autowired
	private ProgramSystemCodeRepository programSystemCodeRepo;

	@Autowired
    private AttachmentRepository attachmentsRepo;

	@Autowired
	private UserFacilityAssociationServiceImpl userFacilityAssocService;

	@Autowired
    private NotificationService notificationService;

    @Autowired
    private SubmissionsReviewDasboardService submissionsReviewDasboardService;

	@Autowired
    private SLTConfigHelper sltConfigHelper;

	@Autowired
    private CefConfig cefConfig;

	public static final String SENT = "Sent";

	public Communication save(CommunicationDto dto, String reportStatus, String userRole, String industrySector, Integer reportYear) {
		Communication comm = commMapper.fromDto(dto);
		UserDto appUser = this.userService.getCurrentUser();
    	String fullName = String.format("%s %s", appUser.getFirstName(), appUser.getLastName());
    	comm.setSenderName(fullName);
    	comm.setProgramSystemCode(programSystemCodeRepo.findById(appUser.getProgramSystemCode()).orElse(null));

        List<UserFacilityAssociation> userList = new ArrayList<>();

        // just get all associations for the slt when report status and sector are not specified
        if (reportStatus == null && industrySector == null) {
            userList = this.userFacilityRepo.findByProgramSystemCodeAndActive(comm.getProgramSystemCode().getCode(), true);
        }
        else {
            // get list of reports that match the selected report status and industry sector
            // year will never be null as "all years" is not an option
            List<SubmissionsReviewDashboardDto> reportList = submissionsReviewDasboardService.retrieveFacilityAllReportsWithNotStarted(reportYear.shortValue(), comm.getProgramSystemCode().getCode());
            if (reportStatus != null && industrySector != null) {
                reportList = reportList.stream()
                    .filter(report -> report.getReportStatus().equals(reportStatus))
                    .filter(report -> report.getIndustry() != null && report.getIndustry().equals(industrySector))
                    .collect(Collectors.toList());
            }
            else if (reportStatus != null) {
                reportList = reportList.stream()
                    .filter(report -> report.getReportStatus().equals(reportStatus))
                    .collect(Collectors.toList());
            }
            else {
                reportList = reportList.stream()
                    .filter(report -> report.getIndustry() != null && report.getIndustry().equals(industrySector))
                    .collect(Collectors.toList());
            }

            // get all users associated to the filtered reports
            for (SubmissionsReviewDashboardDto report : reportList) {
                userList.addAll(this.userFacilityRepo.findByMasterFacilityRecordIdAndActive(report.getMasterFacilityId(), true));
            }
        }

		Map<Object,List<UserFacilityAssociationDto>> filteredUserList;
        if (userRole != null) {
            filteredUserList = userFacilityAssocService.mapAssociations(userList).stream()
                .filter(user -> user.getCdxUserId() != null)
                .filter(user -> (AppRole.NEI_CERTIFIER.equals(userRole) && AppRole.RoleType.NEI_CERTIFIER.isSameRoleName(user.getRoleDescription()))
                                || (AppRole.PREPARER.equals(userRole) && AppRole.RoleType.PREPARER.isSameRoleName(user.getRoleDescription())))
                .collect(Collectors.groupingBy(user -> user.getCdxUserId()));
        }
        else {
            filteredUserList = userFacilityAssocService.mapAssociations(userList).stream()
                .filter(user -> user.getCdxUserId() != null)
                .collect(Collectors.groupingBy(user -> user.getCdxUserId()));
        }

        List<String> emailList = new ArrayList<>();
		for (List<UserFacilityAssociationDto> ufa: filteredUserList.values()) {
			emailList.add(ufa.get(0).getEmail());
		}

		comm.setRecipientEmail(emailList.toString().substring(1, emailList.toString().length() - 1));

		return commRepo.save(comm);
	}

	/**
     * Send SLT Email Notification
     */
	public Communication sendNotification(Communication comm, MultipartFile file) {
		SLTBaseConfig sltConfig = sltConfigHelper.getCurrentSLTConfig(comm.getProgramSystemCode().getCode());

		notificationService.sendSLTNotification(sltConfig.getSltEmail(), cefConfig.getDefaultEmailAddress(), sltConfig.getSltEmail(), comm, file);

		comm.setEmailStatus(SENT);
		commRepo.save(comm);
    	return comm;
	}

	public Communication sendNotification(Communication comm) {
		return sendNotification(comm, null);
	}

	public void deleteCommunication(Communication comm) {
		commRepo.delete(comm);
	}

	public void deleteAllEmailStatusNotSent () {
		attachmentsRepo.deleteAllCommunicationAttachmentsEmailStatusNotSent();
		commRepo.deleteAllEmailStatusNotSent();
	}

}
