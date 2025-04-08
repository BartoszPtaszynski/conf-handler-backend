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
public class SessionView {
    private UUID id;
    private String name;
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
    private String city;
    private String street;
    private String building;
    private String roomNumber;
    private String duration;
    private String sessionChairmen;

    @Id
    private UUID eventId;
    private String eventName;
    private String eventDuration;
    private String eventDescription;
    private UUID eventMenuId;

    private UUID lectureId;
    private String lectureAbstract;
    private String lectureLecturers;
    private String lectureTopic;
    private String lectureChairmen;
}

