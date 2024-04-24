package ConfHandler.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Lecture{

    @Id
    @GeneratedValue
    private UUID id;
    private String topic;
    @Column(name="abstract")
    private String Abstract;

    @OneToOne
    @JoinColumn(name = "event_fk", referencedColumnName = "id")
    private Event event;



    public Lecture(String topic, String Abstract,Event event) {
        this.topic = topic;
        this.Abstract = Abstract;
        this.event = event;
    }

}
