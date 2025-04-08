package ConfHandler.model.converter;

import ConfHandler.model.dto.EventDto;
import ConfHandler.model.dto.LectureDto;
import ConfHandler.model.dto.MenuDto;
import ConfHandler.model.entity.EventView;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class EventViewToEventDtoConverter {

    public List<EventDto> convert(List<EventView> eventViews, Map<UUID, MenuDto> menuDtoMap) {
        return eventViews.stream()
                .map(eventView -> createEventDto(eventView, menuDtoMap))
                .toList();
    }

    private EventDto createEventDto(EventView eventView, Map<UUID, MenuDto> menuDtoMap) {
        if (eventView.getLectureId() == null) {
            return new EventDto(eventView.getEventId(), eventView.getEventName(), eventView.getEventDuration(),
                    eventView.getEventDescription(), menuDtoMap.get(eventView.getEventMenuId()));
        } else {
            return new LectureDto(eventView.getEventId(), eventView.getEventName(), eventView.getEventDuration(),
                    eventView.getEventDescription(), eventView.getLectureAbstract(), eventView.getLectureLecturers(),
                    eventView.getLectureTopic(), eventView.getLectureChairmen());
        }
    }

}
