package ConfHandler.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Chairman {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne()
    @JoinColumn(name="participant_fk")
    private Participant participant;


    @ManyToOne()
    @JoinColumn(name="session_fk")
    private Session session;
    @ManyToOne()
    @JoinColumn(name="lecture_fk")
    private Lecture lecture;


    public Chairman( Participant participant, Lecture lecture, Session session) {
        this.participant = participant;
        this.lecture = lecture;
        this.session = session;
    }


}
