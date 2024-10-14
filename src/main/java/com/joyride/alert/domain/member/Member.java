package com.joyride.alert.domain.member;

import com.joyride.alert.domain.alert.Alert;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "members")
@NoArgsConstructor
public class Member{

    @Id
    private Long id;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String fcmToken;

    private boolean isOnline;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alert> alert;
}

