package ConfHandler.model.entity;

import ConfHandler.Admin.SessionInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;


@Entity
@NoArgsConstructor
@Setter
@Getter
public class Session {
    @Id
    @GeneratedValue()
    private UUID id;
    private String name;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime timeStart;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime timeEnd;
    private String city;
    private String street;
    private String building;
    private String room_number;

    @OneToMany(mappedBy = "session")
    private List<Event> eventList;

    @ManyToOne
    @JoinColumn(name = "chairman")
    private Participant chairman;

    public Session(String name, LocalDateTime time_start, LocalDateTime time_end, String city, String street, String building, String room_number,Participant chairman) {
        this.name = name;
        this.timeStart = time_start;
        this.timeEnd = time_end;
        this.city = city;
        this.street = street;
        this.building = building;
        this.room_number = room_number;
        this.chairman = chairman;
    }

    public String getDuration()
    {
        return timeStart.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) +
                " - " +
                timeEnd.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

}
