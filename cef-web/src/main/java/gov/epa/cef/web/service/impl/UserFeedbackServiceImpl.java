/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
