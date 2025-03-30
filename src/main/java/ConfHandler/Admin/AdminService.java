package ConfHandler.Admin;

import ConfHandler.exception.ParticipantNotFoundException;
import ConfHandler.exception.SessionNotFoundException;
import ConfHandler.model.dto.MetadataDto;
import ConfHandler.model.entity.*;
import ConfHandler.repositories.*;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
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
    private LecturerRepository lecturerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ChairmanRepository chairmanRepository;


    public void addParticipants(List<ParticipantCommand> participantCommand) {
        List<Participant> participants = participantCommand.stream()
                .map(participant ->{
                    String password = generatePassword();
                    Participant p = new Participant(participant.getName(),participant.getSurname(),participant.getEmail(),participant.getAffiliation(),passwordEncoder.encode(password),participant.getTitle());
                    Map<String, Object> templateModel = new HashMap<>();
                    templateModel.put("name", participant.getName()+" "+participant.getSurname());
                    templateModel.put("password", password);
                    templateModel.put("email", participant.getEmail());
//                    try {
//                        emailService.sendEmailWithTemplate(p.getEmail(), "Welcome to the dsa conference!", templateModel);
//                        log.info("email to "+p.getName()+" "+p.getSurname()+" sent");
//                    } catch (MessagingException e) {
//                        log.warn("email not sent");
//                    }
                    return  p;
                })
                .toList();

        participantRepository.saveAll(participants);
    }
    private String generatePassword() {
        Random random = new Random();
        String randomDigits = IntStream.range(0, 4)
                .mapToObj(i -> random.nextInt(10))
                .map(String::valueOf)
                .collect(Collectors.joining());
        return randomDigits;
    }

    @Transactional
    public void addSession(List<SessionCommand> sessionCommand)  {
        List<Session> sessions = sessionCommand.stream()
                .map(session ->{
                        Session newSession =   new Session(
                                session.getName(),
                                LocalDateTime.of(session.getSessionDate(),session.getTimeStart()),
                                LocalDateTime.of(session.getSessionDate(),session.getTimeEnd()),
                                session.getCity(),
                                session.getStreet(),
                                session.getBuilding(),
                                session.getRoomNumber()
                        );
                    List<String> chairmanIds = Arrays.stream(session.getChairman().split(",")).toList();
                    List<Chairman> chairmanList = chairmanIds.stream()
                            .map(chairmanId->{
                                Chairman chairman = new Chairman(participantRepository.findById(UUID.fromString(chairmanId)).get(),null,newSession);
                                return chairman;
                            }).toList();
                    newSession.setChairmanList(chairmanList);
                    sessionRepository.save(newSession);
                    chairmanRepository.saveAll(chairmanList);
                    return newSession;
                })
                .toList();




        log.info(sessions.toString());

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
                            eventRepository.save(event);

                            events.add(event);
                            if(!c.getTopic().isEmpty()) {

                                Lecture lecture = new Lecture(c.getTopic(),c.get_abstract(),event);
                                lectureRepository.save(lecture);
                                List<String> lecturersIds =Arrays.stream(c.getLecturers().split(",")).toList();
                                List<Lecturer> lecturers = lecturersIds.stream()
                                                         .map(lecturerId->{
                                                             Lecturer lecturer =new Lecturer(lecture,participantRepository.findById(UUID.fromString(lecturerId)).get());
                                                             lecturerRepository.save(lecturer);
                                                             return lecturer;
                                                         })
                                                        .toList();
                                lecture.setLecturers(lecturers);
                                lectures.add(lecture);

                                if(c.getChairman().length()>1) {
                                    List<String> chairmanIds = Arrays.stream(c.getChairman().split(",")).toList();
                                    log.info(String.valueOf(chairmanIds.isEmpty()));
                                    if (chairmanIds.size() > 0) {
                                        List<Chairman> chairmanList = chairmanIds.stream()
                                                .map(chairmanId -> {
                                                    Chairman chairman = new Chairman(participantRepository.findById(UUID.fromString(chairmanId)).get(), lecture, null);
                                                    return chairman;
                                                }).toList();
                                        lecture.setChairmanList(chairmanList);
                                        chairmanRepository.saveAll(chairmanList);
                                    }
                                }

                            }
                        }
                        );




    }
    public List<ParticipantInfo> getParticipants() {
        return participantRepository.findAll().stream()
                .map(participant -> ParticipantInfo.builder()
                        .id(participant.getId().toString())
                        .name(participant.getName())
                        .surname(participant.getSurname())
                        .email(participant.getEmail())
                        .affiliation(participant.getAffiliation()== null? "":participant.getAffiliation())
                        .title(participant.getTitleManual()).build())
                .toList();
    }

    public List<SessionInfo> getSessions() {
        messagingTemplate.convertAndSend("/conference", "XDD");
        return sessionRepository.findAll().stream()
                .map(session -> SessionInfo.builder()
                        .id(session.getId())
                        .name(session.getName())
                        .sessionDate(session.getTimeStart().toLocalDate())
                        .timeStart(session.getTimeStart().toLocalTime())
                        .timeEnd(session.getTimeEnd().toLocalTime())
                        .city(session.getCity())
                        .street(session.getStreet())
                        .building(session.getBuilding())
                        .roomNumber(session.getRoom_number())
                        .chairman(session.getChairmanList().stream().map(chairman -> chairman.getParticipant().getId().toString()).collect(Collectors.joining(","))).build()
                )
                .toList();
    }

    public List<EventLectureInfo> getEventsLectures() {
        return eventRepository.findAllEventsWithDetails().stream()
                .map(row -> EventLectureInfo.builder()
                        .id(row[0].toString())
                        .name(row[1] == null ? "" : row[1].toString())
                        .eventDate(((java.sql.Timestamp) row[2]).toLocalDateTime().toLocalDate())
                        .timeStart(((java.sql.Timestamp) row[2]).toLocalDateTime().toLocalTime())
                        .timeEnd(((java.sql.Timestamp) row[3]).toLocalDateTime().toLocalTime())
                        .description(row[4] == null ? "" : row[4].toString())
                        .sessionId(row[5] == null ? "" : row[5].toString())
                        .topic(row[6] == null ? "" : row[6].toString())
                        ._abstract(row[7] == null ? "" : row[7].toString())
                        .lecturers(row[8] == null ? "" : row[8].toString())
                        .chairman(row[9] == null ? "" : row[9].toString())
                        .build())
                .toList();
    }

    public void updateEventLecture(List<EventLectureInfo> command) {
        command.stream().forEach(
                eventInfo -> {

                    Optional<Event> eventOptional = eventRepository.findById(UUID.fromString(eventInfo.getId()));
                    if (eventOptional.isPresent()) {
                        Event event = eventOptional.get();
                        event.setName(eventInfo.getName());
                        event.setTimeStart(LocalDateTime.of(eventInfo.getEventDate(),eventInfo.getTimeStart()));
                        event.setTimeEnd(LocalDateTime.of(eventInfo.getEventDate(),eventInfo.getTimeEnd()));
                        event.setDescription(eventInfo.getDescription().isEmpty()?null:eventInfo.getDescription());
                        if( eventInfo.getSessionId()!=null)  {
                            sessionRepository.findById(UUID.fromString(eventInfo.getId())).ifPresent(event::setSession);
                        }
                        if(eventInfo.getTopic().length()>0) {
                            Lecture lecture = lectureRepository.getByEvent_Id(event.getId());
                            if (lecture != null) {
                                lecture.setTopic(eventInfo.getTopic());
                                lecture.set_abstract(eventInfo.get_abstract());

                                ArrayList<Lecturer> lecturers = Arrays.stream(eventInfo.getLecturers().split(",")).map(
                                      lecturerId->
                                                new Lecturer(lecture,participantRepository.findById(UUID.fromString(lecturerId)).get())

                                ).collect(Collectors.toCollection(ArrayList::new));

                                if(!lecture.equalsLecturers(lecturers)) {
                                    lecturerRepository.deleteAll(lecture.getLecturers());

                                    lecturerRepository.saveAll(lecturers);
                                    lecture.setLecturers(lecturers);
                                }
                                lectureRepository.save(lecture);
                            }
                        }
                        eventRepository.save(event);
                    }
                });

    }

    public void updateSession(List<SessionInfo> command) {
        command.stream().forEach(
                sessionInfo -> {

                    Optional<Session> sessionOptional = sessionRepository.findById((sessionInfo.getId()));
                    if (sessionOptional.isPresent()) {
                        Session session = sessionOptional.get();
                        session.setName(sessionInfo.getName());
                        session.setTimeStart(LocalDateTime.of(sessionInfo.getSessionDate(), sessionInfo.getTimeStart()));
                        session.setTimeEnd(LocalDateTime.of(sessionInfo.getSessionDate(), sessionInfo.getTimeEnd()));
                        session.setCity(sessionInfo.getCity());
                        session.setStreet(sessionInfo.getStreet());
                        session.setBuilding(sessionInfo.getBuilding());
                        session.setRoom_number(sessionInfo.getRoomNumber());

                        sessionRepository.save(session);
                    }
                });

    }

    public void getMetadata(MetadataDto command) {
    }

    public void sendEmails() {
        List<Participant> participants = participantRepository.findAll();

//        for (Participant participant : participants) {
        Participant participant = participantRepository.findById(UUID.fromString("a1455c36-d7d2-4dcb-abcf-b0916ca3117b")).get();
        for(int i=0;i<122;i++) {

            String password = generatePassword();
            participant.setPassword(passwordEncoder.encode(password));
            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("name", participant.getName()+" "+participant.getSurname());
            templateModel.put("password", password);
            templateModel.put("email", participant.getEmail());

            try {
                emailService.sendEmailWithTemplate(participant.getEmail(), "Welcome to the dsa conference!", templateModel);
                log.info("email to "+participant.getName()+" "+participant.getSurname()+" sent");
            } catch (MessagingException e) {
                log.warn("email not sent");
            }
        }

    }
}
