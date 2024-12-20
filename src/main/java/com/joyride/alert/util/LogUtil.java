package com.joyride.alert.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogUtil {

    public static void printLog(ConsumerRecord<String, String> consumerRecord
    ) {
        log.info("Consumer Data = {},Header OffSet = {}, Partition = {}"
                , consumerRecord.value(), consumerRecord.offset(), consumerRecord.partition());
    }
}
