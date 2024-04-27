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
import java.time.format.DateTimeFormatter;
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
    public ConferenceByDayDto getDayOfConference(LocalDate date) {

        List<SessionDto> sessions = sessionRepository.getSessionsByTimeStart(date).stream()
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
                .toList();


                List<EventDto> eventsNoInSession = eventRepository.getEventsByDateWithoutSession(date)
                        .stream().map(event -> new EventDto(event.getId(),event.getName(),event.getDuration())).toList();


        return ConferenceByDayDto.builder().sessions(sessions).events(eventsNoInSession).build();
    }

    private List<EventDto> getEventList(Session session) {
        return session.getEventList().stream()
                .map(event -> {
                    Lecture lecture = lectureRepository.getByEvent_Id(event.getId());
                    return lecture == null ?
                            new EventDto(event.getId(),event.getName(),event.getDuration()) :
                            new LectureDto(lecture.getId(),event.getName(),event.getDuration(),lecture.get_abstract(),lecture.getLecturers(),lecture.getTopic());
                })
                .toList();
    }
}
