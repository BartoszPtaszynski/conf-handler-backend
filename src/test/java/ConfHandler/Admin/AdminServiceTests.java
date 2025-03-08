package ConfHandler.Admin;

import ConfHandler.exception.SessionNotFoundException;
import ConfHandler.model.entity.*;
import ConfHandler.repositories.*;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTests {
    @InjectMocks
    private AdminService adminService;
    @Mock
    private ParticipantRepository participantRepository;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private LectureRepository lectureRepository;
    @Mock
    private LecturerRepository lecturerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private SimpMessagingTemplate messagingTemplate;
    @Mock
    private EmailService emailService;
    @Mock
    private ChairmanRepository chairmanRepository;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testAddParticipant() throws MessagingException {
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

        ParticipantCommand mockParticipantCommand = new ParticipantCommand();
        mockParticipantCommand.setName("John");
        mockParticipantCommand.setSurname("Smith");
        mockParticipantCommand.setEmail("john@mail.com");
        mockParticipantCommand.setAffiliation("Affiliation");
        mockParticipantCommand.setTitle("Title");

        adminService.addParticipants(List.of(mockParticipantCommand));

//        ArgumentCaptor<Map<String, Object>> templateModelCaptor = ArgumentCaptor.forClass(Map.class);
//
//        verify(emailService, times(1)).sendEmailWithTemplate(
//                eq(mockParticipantCommand.getEmail()),
//                eq("Welcome to the dsa conference!"),
//                templateModelCaptor.capture()
//        );
//
//        Map<String, Object> capturedTemplate = templateModelCaptor.getValue();
//
//        assertEquals("John Smith", capturedTemplate.get("name"));
//        assertEquals("john@mail.com", capturedTemplate.get("email"));
//        assertNotNull(capturedTemplate.get("password"));

        verify(participantRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void testAddSession() throws MessagingException {
        SessionCommand mockSessionCommand;
        mockSessionCommand = new SessionCommand();
        mockSessionCommand.setName("Session");
        mockSessionCommand.setTimeStart(LocalTime.now());
        mockSessionCommand.setTimeEnd(LocalTime.now().plusHours(1));
        mockSessionCommand.setCity("City");
        mockSessionCommand.setBuilding("Building");
        mockSessionCommand.setChairman(UUID.randomUUID().toString());
        mockSessionCommand.setSessionDate(LocalDate.now());
        Participant mockChairman = new Participant();

        when(participantRepository.findById(UUID.fromString(mockSessionCommand.getChairman())))
                .thenReturn(Optional.of(mockChairman));

        adminService.addSession(List.of(mockSessionCommand));

        verify(sessionRepository, times(1)).save(any());
        verify(chairmanRepository, times(1)).saveAll(any());
    }

    @Test
    public void testAddEventOrLectureWithoutTopic(){
        EventLectureCommand mockEvent = new EventLectureCommand();
        mockEvent.setTimeStart(LocalTime.now());
        mockEvent.setTimeEnd(LocalTime.now().plusHours(1));
        mockEvent.setEventDate(LocalDate.now());
        mockEvent.setDescription("Description");
        mockEvent.setTopic("");

        adminService.addEventOrLecture(List.of(mockEvent));

        verify(eventRepository, times(1)).save(any());
        verify(lectureRepository, times(0)).save(any());
        verify(chairmanRepository, times(0)).saveAll(any());
    }

    @Test
    public void testAddEventOrLectureSessionNotFound(){
        EventLectureCommand mockEvent = new EventLectureCommand();
        mockEvent.setTimeStart(LocalTime.now());
        mockEvent.setTimeEnd(LocalTime.now().plusHours(1));
        mockEvent.setEventDate(LocalDate.now());
        mockEvent.setDescription("Description");
        mockEvent.setTopic("");
        mockEvent.setSessionId(UUID.randomUUID());

        when(sessionRepository.findById(mockEvent.getSessionId())).thenReturn(Optional.empty());

        assertThrows(SessionNotFoundException.class, () -> adminService.addEventOrLecture(List.of(mockEvent)));
    }

    @Test
    public void testAddEventOrLectureWithTopic(){
        EventLectureCommand mockEvent = new EventLectureCommand();
        mockEvent.setTimeStart(LocalTime.now());
        mockEvent.setTimeEnd(LocalTime.now().plusHours(1));
        mockEvent.setEventDate(LocalDate.now());
        mockEvent.setDescription("Description");
        mockEvent.setTopic("Topic");
        mockEvent.setLecturers(UUID.randomUUID().toString());
        mockEvent.setSessionId(UUID.randomUUID());
        mockEvent.setChairman(mockEvent.getLecturers());
        Participant mockChairman = new Participant();

        when(participantRepository.findById(UUID.fromString(mockEvent.getChairman())))
                .thenReturn(Optional.of(mockChairman));

        when(sessionRepository.findById(mockEvent.getSessionId())).thenReturn(Optional.of(new Session()));

        adminService.addEventOrLecture(List.of(mockEvent));

        verify(eventRepository, times(1)).save(any());
        verify(lectureRepository, times(1)).save(any());
        verify(lecturerRepository, times(1)).save(any());
        verify(chairmanRepository, times(1)).saveAll(any());
    }

    @Test
    public void testGetParticipants(){
        Participant mockParticipant1, mockParticipant2;
        mockParticipant1 = new Participant();
        mockParticipant2 = new Participant();
        mockParticipant1.setId(UUID.randomUUID());
        mockParticipant2.setId(UUID.randomUUID());
        mockParticipant1.setName("Participant 1");
        mockParticipant2.setName("Participant 2");
        when(participantRepository.findAll()).thenReturn(List.of(mockParticipant1, mockParticipant2));

        List<ParticipantInfo> participants = adminService.getParticipants();

        assertEquals(participants.size(), 2);
        assertEquals(participants.get(0).getName(), mockParticipant1.getName());
        assertEquals(participants.get(1).getName(), mockParticipant2.getName());
    }

    @Test
    public void testGetSessions(){
        Session mockSession1, mockSession2;
        mockSession1 = new Session();
        mockSession2 = new Session();
        mockSession1.setId(UUID.randomUUID());
        mockSession2.setId(UUID.randomUUID());
        mockSession1.setTimeStart(LocalDateTime.now());
        mockSession1.setTimeEnd(LocalDateTime.now().plusHours(1));
        mockSession2.setTimeStart(LocalDateTime.now().plusHours(1));
        mockSession2.setTimeEnd(LocalDateTime.now().plusHours(2));
        mockSession1.setChairmanList(List.of());
        mockSession2.setChairmanList(List.of());
        mockSession1.setName("Session 1");
        mockSession2.setName("Session 2");
        when(sessionRepository.findAll()).thenReturn(List.of(mockSession1, mockSession2));

        List<SessionInfo> sessions = adminService.getSessions();

        assertEquals(sessions.size(), 2);
        assertEquals(sessions.get(0).getId(), mockSession1.getId());
        assertEquals(sessions.get(1).getId(), mockSession2.getId());
    }

    @Test
    public void testGetEventsLecture(){
        Event mockEvent1, mockEvent2;
        Lecture mockLecture;
        mockEvent1 = new Event();
        mockEvent2 = new Event();
        mockLecture = new Lecture();
        mockEvent1.setId(UUID.randomUUID());
        mockEvent2.setId(UUID.randomUUID());
        mockEvent1.setTimeStart(LocalDateTime.now());
        mockEvent1.setTimeEnd(LocalDateTime.now().plusHours(1));
        mockEvent2.setTimeStart(LocalDateTime.now().plusHours(1));
        mockEvent2.setTimeEnd(LocalDateTime.now().plusHours(2));
        mockEvent1.setName("Event 1");
        mockEvent2.setName("Event 2");
        mockLecture.setId(UUID.randomUUID());
        mockLecture.setTopic("Topic");
        mockLecture.setEvent(mockEvent1);
        mockLecture.setLecturers(List.of());
        mockLecture.setChairmanList(List.of());

        when(eventRepository.findAll()).thenReturn(List.of(mockEvent1, mockEvent2));
        when(lectureRepository.getByEvent_Id(mockEvent1.getId())).thenReturn(mockLecture);

        List<EventLectureInfo> events = adminService.getEventsLectures();

        assertEquals(events.size(), 2);
        assertEquals(UUID.fromString(events.get(0).getId()), mockEvent1.getId());
        assertEquals(UUID.fromString(events.get(1).getId()), mockEvent2.getId());
        assertEquals(events.get(0).getTopic(), mockLecture.getTopic());
    }

    @Test
    public void testUpdateEventLecture(){
        EventLectureInfo mockEventInfo = new EventLectureInfo(UUID.randomUUID().toString(), "EventInfo",
                LocalDate.now(), LocalTime.now(), LocalTime.now().plusHours(1), UUID.randomUUID().toString(),
                "Topic", "Abstract", "Description", UUID.randomUUID().toString(),
                UUID.randomUUID().toString());
        Event mockEvent = new Event();
        mockEvent.setId(UUID.randomUUID());

        Lecture mockLecture = new Lecture();
        mockLecture.setId(UUID.randomUUID());
        mockLecture.setLecturers(new ArrayList<>());

        Participant mockParticipant = new Participant();
        mockParticipant.setId(UUID.randomUUID());

        when(eventRepository.findById(UUID.fromString(mockEventInfo.getId()))).thenReturn(Optional.of(mockEvent));
        when(lectureRepository.getByEvent_Id(mockEvent.getId())).thenReturn(mockLecture);
        when(sessionRepository.findById(UUID.fromString(mockEventInfo.getId()))).thenReturn(Optional.of(new Session()));
        when(participantRepository.findById(UUID.fromString(mockEventInfo.getLecturers()))).thenReturn(Optional.of(mockParticipant));


        adminService.updateEventLecture(List.of(mockEventInfo));

        verify(eventRepository, times(1)).save(any(Event.class));
        verify(lectureRepository, times(1)).save(any(Lecture.class));
        verify(lecturerRepository, times(1)).deleteAll(anyList());
        verify(lecturerRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void testUpdateSession(){
        SessionInfo mockSessionInfo = new SessionInfo(UUID.randomUUID(), "SessionInfo", LocalDate.now(),
                LocalTime.now(), LocalTime.now(), "City", "Street", "Building",
                "Room", UUID.randomUUID().toString());

        Session mockSession = new Session();
        mockSession.setId(UUID.randomUUID());

        when(sessionRepository.findById(mockSessionInfo.getId())).thenReturn(Optional.of(mockSession));

        adminService.updateSession(List.of(mockSessionInfo));

        verify(sessionRepository, times(1)).save(any(Session.class));
    }
}
