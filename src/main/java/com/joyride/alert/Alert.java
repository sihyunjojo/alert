package com.joyride.alert;

import com.joyride.alert.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Alert {

    @Id
    private Long id;

    private String title;
    private String body;

    @Setter
    private Status status;

    @OneToOne
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
