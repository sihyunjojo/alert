package com.joyride.alert;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final FirebaseMessaging firebaseMessaging;

    public void sendNotification(String token, String title, String body) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .setToken(token)
                .build();

        firebaseMessaging.send(message);
    }

    public void sendUpdateNotification(String token, String type) throws FirebaseMessagingException {
        String title = "새로운 " + type + " 정보가 업로드되었어요!";
        String body = "새로운 " + type + " 정보가 업로드되었습니다. 확인해보세요!";
        sendNotification(token, title, body);
    }

    public void sendLikeNotification(String token, String type, int count) throws FirebaseMessagingException {
        String title = count + "개의 " + type + "이 핫해요!";
        String body = "오늘 하루 " + count + "개의 " + type + "에 대한 조회가 많습니다! 관심을 가져보세요!";
        sendNotification(token, title, body);
    }
}
