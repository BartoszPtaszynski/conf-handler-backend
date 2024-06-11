package ConfHandler.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Lecturer {
    @Id
    @GeneratedValue
    private UUID id;

  @ManyToOne
    @JoinColumn(name="lecture_fk")
    private Lecture lecture;


    @ManyToOne
    @JoinColumn(name="participant_fk")
    private Participant participant;

    public Lecturer(Lecture lecture, Participant participant) {
        this.lecture = lecture;
        this.participant = participant;
    }

    @Override
    public boolean equals(Object object) {
        Lecturer lecturer = (Lecturer) object;
        return Objects.equals(lecture.getId(), lecturer.lecture.getId()) && Objects.equals(participant.getId(), lecturer.participant.getId());
    }

}
