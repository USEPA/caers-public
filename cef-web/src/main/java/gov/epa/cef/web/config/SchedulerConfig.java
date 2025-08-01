/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
