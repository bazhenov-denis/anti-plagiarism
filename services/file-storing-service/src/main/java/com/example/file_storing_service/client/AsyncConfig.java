package com.example.file_storing_service.client;


import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

  @Bean("analysisExecutor")
  public Executor analysisExecutor() {
    ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
    exec.setCorePoolSize(4);
    exec.setMaxPoolSize(8);
    exec.setQueueCapacity(100);
    exec.setThreadNamePrefix("analysis-");
    exec.initialize();
    return exec;
  }
}