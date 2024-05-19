package ConfHandler.model.dto;

import ConfHandler.model.entity.Event;
import ConfHandler.model.entity.Session;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
public class SessionDto {
    private UUID id;
    private String name;
    private String duration;
    private String city;
    private String street;
    private String building;
    private String roomNumber;
    private List<EventDto> eventList;

}

