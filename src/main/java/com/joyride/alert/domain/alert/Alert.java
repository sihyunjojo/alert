package com.joyride.alert.domain.alert;

import com.joyride.alert.domain.member.Member;
import com.joyride.alert.domain.alert.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String body;

    @Setter
    @Enumerated(EnumType.STRING)
    private Status status;


    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Alert(String title, String body, Status status, Member member) {
        this.title = title;
        this.body = body;
        this.status = status;
        this.member = member;
    }


    public static Alert createPendingAlert(String title, String body, Member member){
        return new Alert(title, body, Status.PENDING, member);
    }

}
