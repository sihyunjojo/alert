package com.joyride.alert.util;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TopicUtil {

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 파서

    // JSON 메시지에서 memberId를 추출
    public Long extractMemberIdFromConsumerRecord(ConsumerRecord<String, String> consumerRecord) {
        try {
            JsonNode jsonNode = objectMapper.readTree(consumerRecord.value());
            return jsonNode.get("memberId").asLong(); // memberId를 Long으로 추출
        } catch (Exception e) {
            throw new RuntimeException("memberId 추출 실패", e);
        }
    }

    // JSON 메시지에서 특정 키의 값을 추출
    public String extractValueByKeyFromConsumerRecord(ConsumerRecord<String, String> consumerRecord, String key) {
        try {
            JsonNode jsonNode = objectMapper.readTree(consumerRecord.value());
            return jsonNode.has(key) ? jsonNode.get(key).asText() : "unknown"; // key에 해당하는 값을 String으로 추출
        } catch (Exception e) {
            throw new RuntimeException("값 추출 실패: " + key, e);
        }
    }
}
