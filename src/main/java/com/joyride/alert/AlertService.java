package com.joyride.alert;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.joyride.alert.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.joyride.alert.Alert.createPendingAlert;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    public void getPendingNotificationsForUser(Long memberId) throws FirebaseMessagingException {
        List<Alert> pendingNotifications = alertRepository.findPendingByMemberId(memberId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 멤버가 존재하지 않습니다."));

        for (Alert alert : pendingNotifications) {
            notificationService.sendFcmNotification(member.getFcmToken(), alert.getTitle(), alert.getBody());
            alert.setStatus(Status.SENT);
        }
        alertRepository.saveAll(pendingNotifications);
    }

    public void sendHotAreaAlert(Long memberId, String areaName) throws FirebaseMessagingException {
        String title = "오늘 " + areaName + " 상권이 핫해요!";
        String body =  "궁금하지 않으세요?";
        sendAlert(memberId, title, body);
    }

    public void sendHotPostAlert(Long memberId, String postName, String boardName) throws FirebaseMessagingException{
        String title = "오늘 " + boardName + " 게시판의 " + postName + " 글이 핫해요!";
        String body =  "궁금하지 않으세요?";
        sendAlert(memberId, title, body);
    }

    public void sendUpdateAlert(Long memberId, String type) throws FirebaseMessagingException {
        String title = "새로운 " + type + " 정보가 업로드되었어요!";
        String body = "새로운 " + type + " 정보가 업로드되었습니다. 확인해보세요!";
        sendAlert(memberId, title, body);
    }


    public void sendAlert(Long memberId, String title, String body) throws FirebaseMessagingException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 멤버가 존재하지 않습니다."));

        if (member.isOnline()) {
            String token = member.getFcmToken();
            notificationService.sendFcmNotification(token, title, body);
        } else {
            saveOfflineAlert(member, title, body);
        }
    }

    private void saveOfflineAlert(Member member, String title, String body) {
        Alert pendingAlert = createPendingAlert(title, body, member);
        alertRepository.save(pendingAlert);
    }

}
