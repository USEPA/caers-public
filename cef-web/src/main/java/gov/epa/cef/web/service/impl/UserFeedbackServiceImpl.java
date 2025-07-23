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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import gov.epa.cef.web.domain.UserFeedback;
import gov.epa.cef.web.repository.UserFeedbackRepository;
import gov.epa.cef.web.service.NotificationService;
import gov.epa.cef.web.service.UserFeedbackService;
import gov.epa.cef.web.service.dto.UserFeedbackDto;
import gov.epa.cef.web.service.dto.IUserFeedbackStatsDto;
import gov.epa.cef.web.service.mapper.UserFeedbackMapper;

import java.util.List;

@Service
public class UserFeedbackServiceImpl implements UserFeedbackService {
	
    @Autowired
    private UserFeedbackMapper userFeedbackMapper;
    
    @Autowired
    private UserFeedbackRepository userFeedbackRepo;
    
    @Autowired
    private NotificationService notificationService;
    
    public UserFeedbackDto create(UserFeedbackDto dto) {
		
    	UserFeedback userFeedback = userFeedbackMapper.fromDto(dto);
    	
    	UserFeedbackDto result = userFeedbackMapper.toDto(userFeedbackRepo.save(userFeedback));
    	
        notificationService.sendUserFeedbackNotification(dto);
	  
    	return result;
	}
		
	public void removeReportFromUserFeedback(Long reportId) {
		List<UserFeedback> userFeedback = userFeedbackRepo.findAllByEmissionsReportId(reportId);
		if(userFeedback != null) {
			userFeedback.forEach(submission -> {
				submission.setEmissionsReport(null);
				userFeedbackRepo.save(submission);
			});
		}
	}
	
	public List<UserFeedbackDto> retrieveAllByYearAndProgramSystem(Short year, String programSystem) {
		
		List<UserFeedback> userFeedback = programSystem.contentEquals("ALL_AGENCIES")
	            ? this.userFeedbackRepo.findAllByYear(year, Sort.by(Sort.DEFAULT_DIRECTION.DESC, "createdDate"))
	            : this.userFeedbackRepo.findByYearAndProgramSystemCodeCode(year, programSystem, Sort.by(Sort.DEFAULT_DIRECTION.DESC, "createdDate"));
		
		return userFeedbackMapper.toDtoList(userFeedback);
	}
	
	public IUserFeedbackStatsDto retrieveStatsByYearAndProgramSystem(Short year, String programSystem) {
		
		IUserFeedbackStatsDto userFeedback = programSystem.contentEquals("ALL_AGENCIES")
	            ? this.userFeedbackRepo.findAvgByYear(year) 
	            : this.userFeedbackRepo.findAvgByYearAndProgramSystem(year, programSystem);
	            
		return userFeedback;
	}
	
	public List<String> retrieveAvailableProgramSystems() {
		
		return this.userFeedbackRepo.findDistinctProgramSystems(Sort.by(Sort.Direction.DESC, "programSystemCode.code"));
	}
	
	public List<Short> retrieveAvailableYears() {
		
		return this.userFeedbackRepo.findDistinctYears(Sort.by(Sort.Direction.DESC, "year"));
	}
	
	
}
