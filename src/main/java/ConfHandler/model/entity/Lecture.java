package ConfHandler.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;
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
    private List<Lecturer> lecturers;

    @OneToMany(mappedBy = "lecture")
    private List<Chairman> chairmanList;


    public Lecture(String topic, String Abstract, Event event, List<Lecturer> lecturers) {
        this.topic = topic;
        this._abstract = Abstract;
        this.event = event;
        this.lecturers = lecturers;
    }
    public Lecture(String topic, String Abstract, Event event) {
        this.topic = topic;
        this._abstract = Abstract;
        this.event = event;
    }


    public String getLecturersString() {
        return lecturers.stream().map(
                lecturer -> (lecturer.getParticipant().getTitleManual()==null?"":lecturer.getParticipant().getTitleManual()+" " )+lecturer.getParticipant().getName()+" "+lecturer.getParticipant().getSurname()
        ).collect(Collectors.joining(", "));
    }
    public String getLecturersIds() {
        return lecturers.stream().map(
                lecturer -> (lecturer.getParticipant().getId().toString())
        ).collect(Collectors.joining(","));
    }
     public boolean equalsLecturers(List<Lecturer> lecturers) {
        this.lecturers.sort(Comparator.comparing(c->c.getParticipant().getId()));
        lecturers.sort(Comparator.comparing(c->c.getParticipant().getId()));
        if(this.lecturers.size()!=lecturers.size()) return false;
        for(int i=0;i<lecturers.size();i++) {
            if(lecturers.get(i).equals(this.lecturers.get(i))) {
                return false;
            }
        }
        return true;
     }
}
