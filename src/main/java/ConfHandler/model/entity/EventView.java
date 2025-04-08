package ConfHandler.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class EventView {

    @Id
    private UUID eventId;
    private LocalDateTime eventTimeStart;
    private LocalDateTime eventTimeEnd;
    private String eventName;
    private String eventDuration;
    private String eventDescription;
    private UUID eventMenuId;
    private UUID session;

    private UUID lectureId;
    private String lectureAbstract;
    private String lectureLecturers;
    private String lectureTopic;
    private String lectureChairmen;
}

