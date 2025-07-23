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
package gov.epa.cef.web.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import gov.epa.cef.web.service.task.DeleteReportsTask;
import gov.epa.cef.web.service.task.RemoveNullCdxUsersTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import gov.epa.cef.web.service.task.SccUpdateTask;

@Configuration
@EnableScheduling
public class SchedulerConfig implements SchedulingConfigurer {

    @Autowired
    private CefConfig cefConfig;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler());
        taskRegistrar.addCronTask(sccUpdateTask(), this.cefConfig.getSccUpdateTaskCron());
        taskRegistrar.addCronTask(removeNullCdxUsersTask(), this.cefConfig.getRemoveNullUsersTaskCron());
        taskRegistrar.addCronTask(deleteReportsTask(), this.cefConfig.getDeleteReportsTaskCron());
    }

    @Bean(destroyMethod="shutdown")
    public Executor taskScheduler() {
        return Executors.newScheduledThreadPool(10);
    }

    @Bean
    public SccUpdateTask sccUpdateTask() {
        return new SccUpdateTask();
    }

    @Bean
    public RemoveNullCdxUsersTask removeNullCdxUsersTask() { return new RemoveNullCdxUsersTask(); }

    @Bean
    public DeleteReportsTask deleteReportsTask() {
        return new DeleteReportsTask();
    }
}
