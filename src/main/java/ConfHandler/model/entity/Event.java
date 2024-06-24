package ConfHandler.model.entity;

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
@Getter
@Setter
public class Event {


    @Id
    @GeneratedValue
    private UUID id;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm")
    private LocalDateTime timeStart;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm")
    private LocalDateTime timeEnd;
    private String name;
    private int amountOfUsers;

    @ManyToOne()
    @JoinColumn(name = "session_fk")
    private Session session;
    private String description;

    @ManyToOne()
    @JoinColumn(name = "participant_fk")
    private Participant participant;

    @OneToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;




    public Event(UUID id, LocalDateTime time_start, LocalDateTime time_end, String name ){
        this.id = id;
        this.timeStart = time_start;
        this.timeEnd = time_end;
        this.name = name;
        this.amountOfUsers = 0;
    }

    public Event(LocalDateTime time_start, LocalDateTime time_end, String name,Session session ,String description){

        this.timeStart = time_start;
        this.timeEnd = time_end;
        this.name = name;
        this.amountOfUsers = 0;
        this.session = session;
        this.description = description;

    }
    public Event( LocalDateTime time_start, LocalDateTime time_end, String name,String description ){
        this.timeStart = time_start;
        this.description = description.isEmpty()?null:description;
        this.timeEnd = time_end;
        this.name = name;

    }

    public String getDuration()
    {
        return timeStart.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) +
                " - " +
                timeEnd.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

}
