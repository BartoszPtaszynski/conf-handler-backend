package ConfHandler.model.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class SessionDto implements TimeItem {
    private UUID id;
    private String name;
    private String duration;
    private String city;
    private String street;
    private String building;
    private String roomNumber;
    private List<EventDto> eventList;
    private String chairman;

    public SessionDto copy(List<EventDto> eventListCopy) {
        return new SessionDto(this.id, this.name, this.duration, this.city, this.street, this.building, this.roomNumber, eventListCopy, this.chairman);
    }

    public List<EventDto> getEventList() {
        return Optional.ofNullable(eventList).orElseGet(() -> eventList = new ArrayList<>());
    }
}

