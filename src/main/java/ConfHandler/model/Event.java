package ConfHandler.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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

    @ManyToOne()
    @JoinColumn(name = "participant_fk")
    private Participant participant;




    public Event(UUID id, LocalDateTime time_start, LocalDateTime time_end, String name ){
        this.id = id;
        this.timeStart = time_start;
        this.timeEnd = time_end;
        this.name = name;
        this.amountOfUsers = 0;
    }
    public Event( LocalDateTime time_start, LocalDateTime time_end, String name ){
        this.timeStart = time_start;
        this.timeEnd = time_end;
        this.name = name;

    }

}
