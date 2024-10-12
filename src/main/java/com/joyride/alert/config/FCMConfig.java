package com.joyride.alert.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FCMConfig {

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {

        // Firebase의 서비스 계정 키 파일을 읽어 GoogleCredentials 객체로 만듭니다.
        // ClassPathResource는 클래스패스에 있는 리소스 파일을 읽어오는 역할을 합니다.
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource("firebase-service-account.json").getInputStream());

        // FirebaseOptions 객체를 빌드합니다. 이 객체는 FirebaseApp을 초기화하는 데 필요한 설정을 담고 있습니다.
        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                .setCredentials(googleCredentials)  // GoogleCredentials 객체를 통해 인증 정보 설정
                .build();

        // FirebaseApp 객체를 생성하고 초기화합니다. Firebase 서비스에 접근하는 데 필요한 앱 객체입니다.
        FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions);

        // FirebaseApp에서 FirebaseMessaging 인스턴스를 가져와서 반환합니다.
        return FirebaseMessaging.getInstance(app);
    }
}
