package ConfHandler.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Attendee {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne()
    @JoinColumn(name="event_fk")
    private Event event;


    @ManyToOne()
    @JoinColumn(name="participant_fk")
    private Participant participant;

    public Attendee(Event event, Participant participant) {
        this.event = event;
        this.participant = participant;
    }
}
