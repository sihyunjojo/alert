package com.joyride.alert.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;


/**
 * 이 클래스는 Kafka 소비자(Consumer)의 설정을 담당합니다. 여기서는 Kafka 컨슈머 팩토리, 리스너 컨테이너 팩토리 등을 설정하고 있습니다.
 * 특히, setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE)를 통해 메시지 처리를 수동으로 확인(acknowledge)하는 방식으로 설정하였습니다.
 * 이는 메시지를 처리한 후 명시적으로 acknowledge()를 호출해야 하므로, 메시지 소비가 정확하게 이루어졌음을 보장할 수 있습니다.
 * Kafka 리스너가 동작할 때 필요한 설정들을 관리합니다.
 */
@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private String autoCommit;
    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String earliest;
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;
    @Value("${spring.kafka.consumer.key-deserializer}")
    private String keyDeserializer;
    @Value("${spring.kafka.consumer.value-deserializer}")
    private String valueDeserializer;

    private final TaskExecutorConfig taskExecutorConfig;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // 그룹 생성
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);

        //오프셋 수동 관리
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, this.autoCommit);
        // 이 설정은 컨슈머가 어느 시점에서부터 메시지를 읽기 시작할지를 결정합니다.
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, this.earliest);
        // poll 요청을 보내고, 다음 poll 요청을 보내는데 까지의 최대 시간 설정
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 5000);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    // ConsumerFactory<String, String> consumerFactory: 이 팩토리는 Kafka 메시지를 소비할 때 필요한 Consumer 인스턴스를 생성하는 역할을 합니다.
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> factory(ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        // ConsumerFactory는 Kafka 메시지를 소비하는 Consumer 객체를 생성합니다.
        // consumerFactory는 메서드 파라미터로 전달된 인스턴스입니다.
        factory.setConsumerFactory(consumerFactory);

        // 추가 설정
        // 리스너 팩토리의 스레드 동시 실행 개수를 3개로 설정합니다.
        factory.setConcurrency(3); // 하나의 리스너에 대해 3개의 스레드가 병렬로 메시지를 처리합니다.

        // 컨테이너의 다양한 속성을 설정할 수 있는 ContainerProperties 객체를 반환합니다.
        // AckMode를 MANUAL_IMMEDIATE로 설정하여 메시지를 수동으로 즉시 확인(acknowledge)하도록 설정합니다.
        // 리스너 메서드가 메시지를 처리한 후 명시적으로 acknowledge() 메서드를 호출해야 합니다.
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        // 리스너가 작업을 실행할 TaskExecutor를 설정합니다.
        // 비동기 작업 처리를 위한 실행기를 지정하여 리스너가 비동기적으로 실행됩니다.
        factory.getContainerProperties().setListenerTaskExecutor(taskExecutorConfig.executor());

        return factory;
    }

    // 기본 설정
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }
}

