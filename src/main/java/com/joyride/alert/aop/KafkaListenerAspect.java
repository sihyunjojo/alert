package com.joyride.alert.aop;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.joyride.alert.util.TopicUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaListenerAspect {

    private final TopicUtil topicUtil;

    @SuppressWarnings("unchecked")
    @Around("@annotation(org.springframework.kafka.annotation.KafkaListener)")
    public Object handleKafkaExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs(); // 메서드의 인자를 가져옴

        // KafkaListener 메서드의 인자 순서에 맞게 token과 type을 추출
        if (args[0] instanceof ConsumerRecord) {
            ConsumerRecord<String, String> consumerRecord = (ConsumerRecord<String, String>) args[0];
            String token = topicUtil.extractTokenFromConsumerRecord(consumerRecord);
            String type = topicUtil.extractTypeFromConsumerRecord(consumerRecord);

            try {
                // 원래 Kafka 리스너 메서드를 실행 (notificationService.sendUpdateNotification 포함)
                return joinPoint.proceed();
            } catch (FirebaseMessagingException e) {
                // FCM 메시지 전송 중 예외 처리
                log.error("FCM 메시지 전송에 실패했습니다. 토큰: {}, 유형: {}, 오류: {}", token, type, e.getMessage());
                // 예외 처리: 재시도, 토큰 무효화 등의 로직 추가 가능
                handleFirebaseMessagingException(joinPoint, token, e);
                return null; // 실패 시 null 반환 또는 다른 처리 가능
            }
            catch (DeserializationException e) {
                handleDeserializationException(joinPoint, e);
                return null;
            } catch (CommitFailedException e) {
                handleCommitFailedException(joinPoint, e);
                return null;
            } catch (KafkaException e) {
                handleKafkaException(joinPoint, e);
                return null;
            } catch (Exception e) {
                handleGenericException(joinPoint, e);
                return null;
            }
        } else {
            log.error("메서드 인자가 ConsumerRecord가 아닙니다: {}", args[0]);
            return null;
        }
    }


    private void handleFirebaseMessagingException(ProceedingJoinPoint joinPoint, String token, FirebaseMessagingException e) {
        String methodName = joinPoint.getSignature().getName();

        // 예외에 따라 분기 처리할 수 있음
        if (e.getMessage().contains("InvalidToken")) {
            log.warn("{} 메서드에서 잘못된 토큰으로 인해 메시지 전송 실패: {}", methodName, token);
            // 이 경우 토큰 무효화 처리 로직을 추가하거나 알림을 건너뛸 수 있음
        } else {
            log.error("{} 메서드에서 Firebase 메시지 전송 오류: {}", methodName, e.getMessage());
            // 네트워크 오류 등의 경우 재시도 로직을 추가할 수 있음
        }
    }

    private void handleDeserializationException(ProceedingJoinPoint joinPoint, DeserializationException e) {
        String methodName = joinPoint.getSignature().getName();
        log.error("{} 메서드에서 메시지 역직렬화에 실패했습니다. 오류: {}", methodName, e.getMessage());
    }

    private void handleCommitFailedException(ProceedingJoinPoint joinPoint, CommitFailedException e) {
        String methodName = joinPoint.getSignature().getName();
        log.error("{} 메서드에서 커밋에 실패했습니다. 오류: {}", methodName, e.getMessage());
    }

    private void handleKafkaException(ProceedingJoinPoint joinPoint, KafkaException e) {
        String methodName = joinPoint.getSignature().getName();
        log.error("{} 메서드에서 Kafka 관련 오류가 발생했습니다. 오류: {}", methodName, e.getMessage());
    }

    private void handleGenericException(ProceedingJoinPoint joinPoint, Exception e) {
        String methodName = joinPoint.getSignature().getName();
        log.error("{} 메서드에서 메시지 처리 중 오류가 발생했습니다. 오류: {}", methodName, e.getMessage());
    }

}