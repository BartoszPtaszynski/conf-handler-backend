package ConfHandler.service;

import ConfHandler.model.dto.ConferenceInfoDto;
import ConfHandler.model.dto.EventDto;
import ConfHandler.model.dto.MetadataDto;
import ConfHandler.model.dto.SessionDto;
import ConfHandler.model.entity.*;
import ConfHandler.repositories.*;
import ConfHandler.sevice.DisplayConferenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DisplayConferenceServiceTests {
    @InjectMocks
    private DisplayConferenceService displayConferenceService;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private LectureRepository lectureRepository;
    @Mock
    private AttendeeRepository attendeeRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private ConferenceRepository conferenceRepository;

    private Conference mockConference;
    private ConferenceInfoDto mockConferenceInfoDto;
    private Session mockSession;
    private Event mockEvent;
    private UUID userId;
    private LocalDate date;

    @BeforeEach
    public void setUp(){
        mockConference = new Conference();
        mockConference.setContactEmail("conference@mail.com");

        mockConferenceInfoDto = new ConferenceInfoDto("conference", OffsetDateTime.now(), OffsetDateTime.now());

        date = LocalDate.now();
    }

    private void prepareOtherData(){
        Chairman chairman = new Chairman();
        chairman.setParticipant(new Participant());

        userId = UUID.randomUUID();
        mockEvent = new Event();
        mockEvent.setId(UUID.randomUUID());
        mockEvent.setName("Event");
        mockEvent.setTimeEnd(LocalDateTime.now().plusMinutes(30));
        mockEvent.setTimeStart(LocalDateTime.now());

        mockSession = new Session();
        mockSession.setId(UUID.randomUUID());
        mockSession.setName("Session");
        mockSession.setTimeStart(LocalDateTime.now());
        mockSession.setTimeEnd(LocalDateTime.now().plusHours(1));
        mockSession.setChairmanList(List.of(chairman));
        mockSession.setEventList(List.of(mockEvent));

    }

    @Test
    public void testGetConferenceInfo(){
        when(conferenceRepository.getConferenceInfo()).thenReturn(Optional.of(mockConferenceInfoDto));

        ConferenceInfoDto confInfo = displayConferenceService.getConferenceInfo();

        assertNotNull(confInfo);
        assertEquals(confInfo.getName(), "conference");
        verify(conferenceRepository).getConferenceInfo();
    }

    @Test
    public void testGetConferenceInfoNotFound(){
        when(conferenceRepository.getConferenceInfo()).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> displayConferenceService.getConferenceInfo());
    }

    @Test
    public void testGetMedadata(){
        when(conferenceRepository.getConference()).thenReturn(Optional.of(mockConference));

        MetadataDto metadata = displayConferenceService.getMetadata();

        assertNotNull(metadata);
        assertEquals(metadata.getContactEmail(), "conference@mail.com");
        verify(conferenceRepository).getConference();
    }

    @Test
    public void testGetMedadataNotFound(){
        when(conferenceRepository.getConference()).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> displayConferenceService.getMetadata());
    }

    @Test
    public void testGetDayOfConferenceEmpty(){
        when(sessionRepository.getSessionsByTimeStart(date)).thenReturn(Collections.emptyList());

        List<?> result = displayConferenceService.getDayOfConference(date, null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetDayOfConference() {
        prepareOtherData();
        when(sessionRepository.getSessionsByTimeStart(date)).thenReturn(List.of(mockSession));
        when(eventRepository.getEventsByDateWithoutSession(date)).thenReturn(List.of(mockEvent));

        List<?> result = displayConferenceService.getDayOfConference(date, null);
        assertNotNull(result);
        assertEquals(2, result.size());

        for (Object obj : result) {
            if (obj instanceof SessionDto sessionDto) {
                assertEquals("Session", sessionDto.getName());
            } else if (obj instanceof EventDto eventDto) {
                assertEquals("Event", eventDto.getName());
            } else {
                fail("Unexpected object type: " + obj.getClass().getSimpleName());
            }
        }
    }
}
