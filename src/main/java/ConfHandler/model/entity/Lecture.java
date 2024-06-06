package ConfHandler.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private String _abstract;

    @OneToOne
    @JoinColumn(name = "event_fk", referencedColumnName = "id")
    private Event event;

    @OneToMany(mappedBy = "lecture")
    private Set<Lecturer> lecturers;


    public Lecture(String topic, String Abstract,Event event) {
        this.topic = topic;
        this._abstract = Abstract;
        this.event = event;
    }

    public String getLecturers() {
        return lecturers.stream().map(
                lecturer -> (lecturer.getParticipant().getTitleManual()==null?"":lecturer.getParticipant().getTitleManual()+" " )+lecturer.getParticipant().getName()+" "+lecturer.getParticipant().getSurname()
        ).collect(Collectors.joining(", "));
    }
}
