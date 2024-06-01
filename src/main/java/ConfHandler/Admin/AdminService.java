package ConfHandler.Admin;

import ConfHandler.exception.SessionNotFoundException;
import ConfHandler.model.entity.Event;
import ConfHandler.model.entity.Lecture;
import ConfHandler.model.entity.Participant;
import ConfHandler.model.entity.Session;
import ConfHandler.repositories.EventRepository;
import ConfHandler.repositories.LectureRepository;
import ConfHandler.repositories.ParticipantRepository;
import ConfHandler.repositories.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class AdminService {
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void addParticipants(List<ParticipantCommand> participantCommand) {
        List<Participant> participants = participantCommand.stream()
                .map(participant ->
                        new Participant(participant.getName(),participant.getSurname(),participant.getEmail(),participant.getAffiliation(),generatePassword()))
                .toList();

        participantRepository.saveAll(participants);
    }
    private String generatePassword() {
        Random random = new Random();
        String randomDigits = IntStream.range(0, 8)
                .mapToObj(i -> random.nextInt(10))
                .map(String::valueOf)
                .collect(Collectors.joining());
        return passwordEncoder.encode(randomDigits);
    }

    @Transactional
    public void addSession(List<SessionCommand> sessionCommand) {
        List<Session> sessions = sessionCommand.stream()
                .map(session ->
                        new Session(
                                session.getName(),
                                LocalDateTime.of(session.getSessionDate(),session.getTimeStart()),
                                LocalDateTime.of(session.getSessionDate(),session.getTimeEnd()),
                                session.getCity(),
                                session.getStreet(),
                                session.getBuilding(),
                                session.getRoomNumber()))
                .toList();

        log.info(sessions.toString());
        sessionRepository.saveAll(sessions);
    }

    @Transactional
    public void addEventOrLecture(List<EventLectureCommand> command) {
        List<Event> events= new ArrayList<>();
        List<Lecture> lectures = new ArrayList<>();
        command.stream()
                .forEach(
                        c->{
                            Event event;
                            if(c.getSessionId() == null) {
                                 event = new Event(LocalDateTime.of(c.getEventDate(),c.getTimeStart()),LocalDateTime.of(c.getEventDate(),c.getTimeEnd()),c.getName(),c.getDescription());

                            }
                            else {
                                Session session = sessionRepository.findById(c.getSessionId()).orElseThrow(()->new SessionNotFoundException(c.getSessionId()));
                                event = new Event(LocalDateTime.of(c.getEventDate(),c.getTimeStart()),LocalDateTime.of(c.getEventDate(),c.getTimeEnd()),c.getName(),session,c.getDescription());
                            }

                            events.add(event);
                            if(!c.getTopic().isEmpty()) {
                                lectures.add(new Lecture(c.getTopic(),c.get_abstract(),event));
                            }
                        }
                        );
        eventRepository.saveAll(events);
        lectureRepository.saveAll(lectures);



    }
    public List<ParticipantInfo> getParticipants() {
        return participantRepository.findAll().stream()
                .map(participant -> ParticipantInfo.builder()
                        .id(participant.getId().toString())
                        .name(participant.getName())
                        .surname(participant.getSurname())
                        .email(participant.getEmail())
                        .affiliation(participant.getAffiliation()== null? "":participant.getAffiliation())
                        .title("title").build())
                .toList();
    }

    public List<SessionInfo> getSessions() {
        return sessionRepository.findAll().stream()
                .map(session -> SessionInfo.builder()
                        .id(session.getId().toString())
                        .name(session.getName())
                        .sessionDate(session.getTimeStart().toLocalDate())
                        .timeStart(session.getTimeStart().toLocalTime())
                        .timeEnd(session.getTimeEnd().toLocalTime())
                        .city(session.getCity())
                        .street(session.getStreet())
                        .building(session.getBuilding())
                        .roomNumber(session.getRoom_number()).build())
                .toList();
    }

    public List<EventLectureInfo> getEventsLectures() {
        return eventRepository.findAll().stream()
                .map(event ->
                {
                    Lecture lecture = lectureRepository.getByEvent_Id(event.getId());

                    return EventLectureInfo.builder()
                            .id(event.getId().toString())
                            .name(event.getName()==null?"":event.getName())
                            .eventDate(event.getTimeStart().toLocalDate())
                            .timeStart(event.getTimeStart().toLocalTime())
                            .timeEnd(event.getTimeEnd().toLocalTime())

                            ._abstract(lecture == null ? "" : lecture.get_abstract())
                            .topic(lecture == null ? "" : lecture.getTopic())
                            .description(event.getDescription()==null?"":event.getDescription())
                            .sessionId(event.getSession()==null?"":event.getSession().getId().toString())
                            .build()
                            ;
                }).toList();
    }
}
