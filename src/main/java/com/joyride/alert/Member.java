package com.joyride.alert;

import jakarta.persistence.*;
import lombok.*;


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
    private String FcmToken;

    private boolean isOnline;

    @OneToOne
    private Alert alert;
}

