package ConfHandler.authorization;

import ConfHandler.model.entity.Event;
import ConfHandler.model.entity.Lecture;
import ConfHandler.model.entity.Participant;
import ConfHandler.repositories.AttendeeRepository;
import ConfHandler.repositories.ChairmanRepository;
import ConfHandler.repositories.LecturerRepository;
import ConfHandler.repositories.ParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorizationServiceTests {
    @InjectMocks
    private AuthorizationService authorizationService;
    @Mock
    private ParticipantRepository participantRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AttendeeRepository attendeeRepository;
    @Mock
    private LecturerRepository lecturerRepository;
    @Mock
    private ChairmanRepository chairmanRepository;

    private LoginCommand loginCommand;
    private Participant mockParticipant;

    @BeforeEach
    public void setUp(){
        loginCommand = new LoginCommand("mail@mail.com", "password");

        mockParticipant = new Participant();
        mockParticipant.setId(UUID.randomUUID());
        mockParticipant.setEmail("mail@mail.com");
        mockParticipant.setPassword("password");
        mockParticipant.setName("John");
        mockParticipant.setSurname("Doe");
        mockParticipant.setAffiliation("University");
        mockParticipant.setTitleManual("Dr.");
    }

    @Test
    public void testLoginParticipantNotFound(){
        when(participantRepository.findByEmail(loginCommand.getEmail())).thenReturn(Optional.empty());

        assertThrows(ParticipantNotFoundException.class, () -> authorizationService.login(loginCommand));
    }

    @Test
    public void testLoginPasswordDoesntMatch(){
        when(participantRepository.findByEmail(loginCommand.getEmail())).thenReturn(Optional.of(mockParticipant));
        when(passwordEncoder.matches(loginCommand.getPassword(), mockParticipant.getPassword())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authorizationService.login(loginCommand));
    }

    @Test
    public void testLogin() throws ParticipantNotFoundException {
        when(participantRepository.findByEmail(loginCommand.getEmail())).thenReturn(Optional.of(mockParticipant));
        when(passwordEncoder.matches(loginCommand.getPassword(), mockParticipant.getPassword())).thenReturn(true);
        when(attendeeRepository.getIdsOfUserEvents(mockParticipant.getId())).thenReturn(List.of(UUID.randomUUID()));

        Lecture lecture = new Lecture();
        lecture.setTopic("Topic");
        Event event = new Event();
        event.setTimeStart(LocalDateTime.now());
        event.setTimeEnd(LocalDateTime.now().plusHours(1));
        lecture.setEvent(event);

        when(lecturerRepository.getLectureOfUser(mockParticipant.getId())).thenReturn(List.of(lecture));
        when(chairmanRepository.getChairmanOfLecture(mockParticipant.getId())).thenReturn(List.of());
        when(chairmanRepository.getChairmanOfSession(mockParticipant.getId())).thenReturn(List.of());

        ParticipantInfo participantInfo = authorizationService.login(loginCommand);

        assertThat(participantInfo).isNotNull();
        assertThat(participantInfo.getEmail()).isEqualTo("mail@mail.com");
        assertThat(participantInfo.getName()).isEqualTo("John");
        assertThat(participantInfo.getInvolvedInEvents()).hasSize(1);
    }
}
