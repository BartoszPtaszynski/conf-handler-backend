package ConfHandler.sevice;

import ConfHandler.model.dto.*;
import ConfHandler.model.entity.*;
import ConfHandler.repositories.*;
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
import java.util.UUID;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Service
@Slf4j
public class DisplayConferenceService {

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private AttendeeRepository attendeeRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ConferenceRepository conferenceRepository;
    public List<?> getDayOfConference(LocalDate date,UUID id) {

        List<Object> listOfAllEvents=new ArrayList<>();
        listOfAllEvents.addAll(sessionRepository.getSessionsByTimeStart(date).stream()
                .sorted(Comparator.comparing(Session::getTimeStart))
                .map(s -> SessionDto.builder()
                        .id(s.getId())
                        .name(s.getName())
                        .duration(s.getDuration())
                        .city(s.getCity())
                        .street(s.getStreet())
                        .building(s.getBuilding())
                        .eventList(getAllEventList(s,id))
//                        .chairman(s.getChairman()==null?null:
//                                s.getChairman().getTitleManual()==null?
//                                        String.format("%s %s",s.getChairman().getName(),s.getChairman().getSurname())
//                                        :
//                                        String.format("%s %s %s",s.getChairman().getTitleManual(),s.getChairman().getName(),s.getChairman().getSurname()))
                        .build()
                )
                .filter(sessionDto -> !sessionDto.getEventList().isEmpty())
                .toList());




        listOfAllEvents.addAll(
                id==null
                ?
                eventRepository.getEventsByDateWithoutSession(date)  .stream()
                        .map(event -> new EventDto(event.getId(),event.getName(),event.getDuration())).toList()
                :

                        attendeeRepository.getEventsByDateWithoutSessionOfUSer(date,id)
                .stream()
                .map(event -> new EventDto(event.getId(),event.getName(),event.getDuration())).toList());
        log.warn(attendeeRepository.getEventsByDateWithoutSessionOfUSer(date,id).toString());
        
        listOfAllEvents.sort(Comparator.comparing(o -> {
            if (o instanceof SessionDto) {
                return ((SessionDto) o).getDuration();
            } else {
                return ((EventDto) o).getDuration();
            }
        }));
        return listOfAllEvents;
    }


    private List<EventDto> getAllEventList(Session session,UUID id ) {
        return session.getEventList().stream()
                .filter(event -> id == null || attendeeRepository.getIdsOfUserEvents(id).contains(event.getId()))
                .sorted(Comparator.comparing(Event::getTimeStart))
                .map(event -> {
                    Lecture lecture = lectureRepository.getByEvent_Id(event.getId());
                    return lecture == null ?
                            new EventDto(event.getId(),event.getName(),event.getDuration()) :
                            new LectureDto(event.getId(),event.getName(),event.getDuration(),lecture.get_abstract(),lecture.getLecturersString(),lecture.getTopic());
                })
                .toList();
    }

    public ConferenceInfoDto getConferenceInfo() {
        return conferenceRepository.getConferenceInfo().orElseThrow(()->new NullPointerException("conference not found"));
    }

    public MetadataDto getMetadata() {
        Conference conference = conferenceRepository.getConference().orElseThrow(()->new NullPointerException("conference not found"));
        return   MetadataDto.builder()
                .contactEmail(conference.getContactEmail())
                .contactCellNumber(conference.getContactCellNumber())
                .contactWebsite(conference.getContactWebsite())
                .contactLandlineNumber(conference.getContactLandlineNumber()).build()
                ;

    }
}
