package com.joyride.alert.config.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 비동기 작업을 처리하는 스레드 풀 설정을 위한 구성 클래스입니다.
 * Kafka 리스너에서 비동기 작업을 수행할 수 있도록 TaskExecutor를 설정합니다.
 */
@Configuration
public class TaskExecutorConfig {

    @Bean
    public AsyncTaskExecutor executor() {
        // 비동기 작업을 처리하기 위한 ThreadPoolTaskExecutor 인스턴스를 생성합니다.
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 코어 스레드 풀의 크기를 설정합니다.
        // 최소 3개의 스레드가 항상 실행될 수 있도록 설정합니다.
//        executor.setCorePoolSize(3);
        executor.setCorePoolSize(20);  // 기본 스레드 풀 크기를 더 증가
        // 최대 스레드 풀의 크기를 설정합니다.
        // 동시 실행되는 최대 스레드 수를 10개로 제한합니다.
//        executor.setMaxPoolSize(10);
        executor.setMaxPoolSize(50);   // 최대 스레드 풀 크기를 더 증가
        // 작업 대기열의 크기를 설정합니다.
        // 대기할 수 있는 최대 작업 수는 25개로 설정합니다.
//        executor.setQueueCapacity(25);
        executor.setQueueCapacity(1000); // 큐 용량 증가




        // 스레드의 이름에 붙을 접두사를 설정합니다.
        // 생성된 스레드 이름이 "KafkaListener-1", "KafkaListener-2" 등으로 표시됩니다.
        executor.setThreadNamePrefix("KafkaListener-");

        // 설정한 값들을 바탕으로 실행기를 초기화합니다.
        executor.initialize();

        return executor;
    }
}
