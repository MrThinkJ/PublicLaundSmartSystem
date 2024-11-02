package com.c1se22.publiclaundsmartsystem.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
public class SchedulingConfig {
    @Value("${spring.task.scheduling.pool.size}")
    private int poolSize;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(poolSize);
        scheduler.setThreadNamePrefix("laundry-task-");
        scheduler.setErrorHandler(throwable ->
                logger.error("Scheduled task error", throwable));
        return scheduler;
    }
}
