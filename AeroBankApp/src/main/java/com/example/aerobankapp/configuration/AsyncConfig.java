package com.example.aerobankapp.configuration;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer
{
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Set the core number of threads
        executor.setMaxPoolSize(20); // Set the maximum number of threads
        executor.setQueueCapacity(500); // Set the capacity of the queue
        executor.setThreadNamePrefix("Async-Executor-");
        executor.initialize(); // Initialize the executor
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        // Handle exceptions thrown by @Async methods
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
