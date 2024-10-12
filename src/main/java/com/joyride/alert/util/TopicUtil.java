package com.joyride.alert.util;

import com.joyride.alert.Member;
import com.joyride.alert.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;
import shaded_package.com.sun.istack.NotNull;

@Component
@RequiredArgsConstructor
public class TopicUtil {

    public Long extractMemberIdFromConsumerRecord(@NotNull ConsumerRecord<String, String> consumerRecord) {
        String memberIdStr = extractValueByKeyFromConsumerRecord(consumerRecord, "memberId");
        return Long.parseLong(memberIdStr);
    }

    public String extractValueByKeyFromConsumerRecord(@NotNull ConsumerRecord<String, String> consumerRecord, String key) {
        String[] pairs = consumerRecord.value().split(",");  // 쉼표로 key-value 쌍 분리
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");  // 콜론으로 key와 value를 분리
            if (keyValue.length == 2 && keyValue[0].trim().equals(key)) {
                return keyValue[1].trim();  // 키가 일치하면 값을 반환
            }
        }
        return "unknown";
    }

}
