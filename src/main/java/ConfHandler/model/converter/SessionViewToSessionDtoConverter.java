package ConfHandler.model.converter;

import ConfHandler.model.dto.*;
import ConfHandler.model.entity.SessionView;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SessionViewToSessionDtoConverter {

    public List<SessionDto> convert(List<SessionView> sessionViews, Map<UUID, MenuDto> menuDtoMap) {
        List<SessionDto> result = new ArrayList<>();
        SessionDto currentSessionDto = null;
        for (SessionView sessionView : sessionViews) {
            if (currentSessionDto == null || !currentSessionDto.getId().equals(sessionView.getId())) {
                currentSessionDto = SessionDto.builder()
                        .id(sessionView.getId())
                        .name(sessionView.getName())
                        .city(sessionView.getCity())
                        .street(sessionView.getStreet())
                        .building(sessionView.getBuilding())
                        .roomNumber(sessionView.getRoomNumber())
                        .duration(sessionView.getDuration())
                        .chairman(sessionView.getSessionChairmen())
                        .build();

                result.add(currentSessionDto);
            }
            currentSessionDto.getEventList().add(createEventDto(sessionView, menuDtoMap));
        }
        result.forEach(sessionDto -> sessionDto.getEventList().sort(Comparator.comparing(EventDto::getDuration)));
        return result;
    }

    private EventDto createEventDto(SessionView sessionView, Map<UUID, MenuDto> menuDtoMap) {
        if (sessionView.getLectureId() != null) {
            return new LectureDto(sessionView.getEventId(), sessionView.getEventName(), sessionView.getEventDuration(), sessionView.getEventDescription(), sessionView.getLectureAbstract(
            ), sessionView.getLectureLecturers(), sessionView.getLectureTopic(), sessionView.getLectureChairmen());
        } else {
            return new EventDto(sessionView.getEventId(), sessionView.getEventName(), sessionView.getEventDuration(), sessionView.getEventDescription(), menuDtoMap.get(sessionView.getEventMenuId()));
        }
    }

}
