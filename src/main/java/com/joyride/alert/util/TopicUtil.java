package com.joyride.alert.util;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Component
public class TopicUtil {
    public String extractTokenFromConsumerRecord(ConsumerRecord<String, String> consumerRecord) {
        String[] parts = consumerRecord.value().split(":");
        return parts.length > 1 ? parts[1] : "unknown";
    }

    public String extractTypeFromConsumerRecord(ConsumerRecord<String, String> consumerRecord) {
        String[] parts = consumerRecord.value().split(":");
        return parts.length > 0 ? parts[0] : "unknown";
    }
}
