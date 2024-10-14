package com.joyride.alert.util;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TopicUtil {

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 파서

    public Long extractMemberIdFromConsumerRecord(ConsumerRecord<String, String> consumerRecord) {
        try {
            String value = consumerRecord.value();
            // JSON 객체인 경우 파싱 시도
            if (value.startsWith("{") && value.endsWith("}")) {
                JsonNode jsonNode = objectMapper.readTree(value);
                if (jsonNode.has("memberId")) {
                    return jsonNode.get("memberId").asLong();
                }
            }
            // 숫자로 변환 가능한 경우 Long으로 반환
            return Long.parseLong(value);
        } catch (Exception e) {
            throw new RuntimeException("memberId 추출 실패", e);
        }
    }

    // 이 메소드를 사용하면 Kafka 메시지에서 특정 키의 값을 쉽게 추출할 수 있으며, JSON 형식과 비 JSON 형식의 메시지를 모두 처리할 수 있습니다.
    public String extractValueByKeyFromConsumerRecord(ConsumerRecord<String, String> consumerRecord, String key) {
        try {
            String value = consumerRecord.value();
            // JSON 객체인 경우 파싱 시도
            if (value.startsWith("{") && value.endsWith("}")) {
                JsonNode jsonNode = objectMapper.readTree(value);
                return jsonNode.has(key) ? jsonNode.get(key).asText() : value;
            }
            // JSON이 아닌 경우 value 그대로 반환
            return value;
        } catch (Exception e) {
            throw new RuntimeException("값 추출 실패: " + key, e);
        }
    }
}
