package ConfHandler.sevice;

import ConfHandler.model.dto.ConferenceByDayDto;
import ConfHandler.model.dto.EventDto;
import ConfHandler.model.dto.LectureDto;
import ConfHandler.model.dto.SessionDto;
import ConfHandler.model.entity.Conference;
import ConfHandler.model.entity.Event;
import ConfHandler.model.entity.Lecture;
import ConfHandler.model.entity.Session;
import ConfHandler.repositories.EventRepository;
import ConfHandler.repositories.LectureRepository;
import ConfHandler.repositories.SessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Service
@Slf4j
public class DisplayConferenceService {

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private EventRepository eventRepository;
    public List<?> getDayOfConference(LocalDate date) {

        List<Object> listOfAllEvents=new ArrayList<>();
        listOfAllEvents.addAll(
          sessionRepository.getSessionsByTimeStart(date).stream()
                .sorted(Comparator.comparing(Session::getTimeStart))
                .map(s -> SessionDto.builder()
                        .id(s.getId())
                        .name(s.getName())
                        .duration(s.getDuration())
                        .city(s.getCity())
                        .street(s.getStreet())
                        .building(s.getBuilding())
                        .eventList(getEventList(s))
                        .build()
                )
                .toList());

        listOfAllEvents.addAll(eventRepository.getEventsByDateWithoutSession(date)
                .stream().sorted(Comparator.comparing(Event::getTimeStart)).map(event -> new EventDto(event.getId(),event.getName(),event.getDuration())).toList());

        
        listOfAllEvents.sort(Comparator.comparing(o -> {
            if (o instanceof SessionDto) {
                return ((SessionDto) o).getDuration();
            } else {
                return ((EventDto) o).getDuration();
            }
        }));
        return listOfAllEvents;
    }

    private List<EventDto> getEventList(Session session) {
        return session.getEventList().stream()
                .sorted(Comparator.comparing(Event::getTimeStart))
                .map(event -> {
                    Lecture lecture = lectureRepository.getByEvent_Id(event.getId());
                    return lecture == null ?
                            new EventDto(event.getId(),event.getName(),event.getDuration()) :
                            new LectureDto(lecture.getId(),event.getName(),event.getDuration(),lecture.get_abstract(),lecture.getLecturers(),lecture.getTopic(),event.getId());
                })
                .toList();
    }
}
