package ConfHandler.service;

import ConfHandler.exception.AttendeeAlreadyExistsException;
import ConfHandler.exception.AttendeeNotFoundException;
import ConfHandler.exception.EventNotFoundException;
import ConfHandler.exception.ParticipantNotFoundException;
import ConfHandler.model.entity.Attendee;
import ConfHandler.model.entity.Event;
import ConfHandler.model.entity.Participant;
import ConfHandler.repositories.AttendeeRepository;
import ConfHandler.repositories.EventRepository;
import ConfHandler.repositories.ParticipantRepository;
import ConfHandler.sevice.ParticipantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParticipantServiceTests {

    @InjectMocks
    private ParticipantService participantService;
    @Mock
    private ParticipantRepository participantRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private AttendeeRepository attendeeRepository;

    private Participant mockParticipant;
    private Event mockEvent;
    private Attendee mockAttendee;
    private UUID participantId;
    private UUID eventId;

    @BeforeEach
    public void setUp(){
        eventId = UUID.randomUUID();
        participantId = UUID.randomUUID();

        mockParticipant = new Participant();
        mockParticipant.setId(participantId);

        mockEvent = new Event();
        mockEvent.setId(eventId);

        mockAttendee = new Attendee(mockEvent, mockParticipant);
    }

    @Test
    public void testSaveParticipantToEvent() throws EventNotFoundException, ParticipantNotFoundException {
        when(participantRepository.findById(participantId)).thenReturn(Optional.of(mockParticipant));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(mockEvent));
        when(attendeeRepository.findByEventAndParticipant(mockEvent, mockParticipant)).thenReturn(Optional.empty());

        participantService.saveParticipantToEvent(participantId, eventId);

        verify(attendeeRepository, times(1)).save(any(Attendee.class));
    }

    @Test
    public void testSaveParticipantToEventParticipantNotFound()
            throws EventNotFoundException, ParticipantNotFoundException {
        when(participantRepository.findById(participantId)).thenReturn(Optional.empty());

        assertThrows(ParticipantNotFoundException.class,
                () -> participantService.saveParticipantToEvent(participantId, eventId));
    }

    @Test
    public void testSaveParticipantToEventEventNotFound() throws EventNotFoundException, ParticipantNotFoundException {
        when(participantRepository.findById(participantId)).thenReturn(Optional.of(mockParticipant));
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class,
                () -> participantService.saveParticipantToEvent(participantId, eventId));
    }

    @Test
    public void testSaveParticipantToEventAttendeeExists() throws EventNotFoundException, ParticipantNotFoundException {
        when(participantRepository.findById(participantId)).thenReturn(Optional.of(mockParticipant));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(mockEvent));
        when(attendeeRepository.findByEventAndParticipant(mockEvent, mockParticipant)).thenReturn(Optional.of(mockAttendee));

        assertThrows(AttendeeAlreadyExistsException.class,
                () -> participantService.saveParticipantToEvent(participantId, eventId));
    }

    @Test
    public void testDeleteParticipantFromEvent()
            throws AttendeeNotFoundException, EventNotFoundException, ParticipantNotFoundException {
        when(participantRepository.findById(participantId)).thenReturn(Optional.of(mockParticipant));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(mockEvent));
        when(attendeeRepository.findByEventAndParticipant(mockEvent, mockParticipant)).thenReturn(Optional.of(mockAttendee));

        participantService.deleteParticipantFromEvent(participantId, eventId);
        verify(attendeeRepository).delete(mockAttendee);
    }

    @Test
    public void testDeleteParticipantFromEventParticipantNotFound()
            throws AttendeeNotFoundException, EventNotFoundException, ParticipantNotFoundException {
        when(participantRepository.findById(participantId)).thenReturn(Optional.empty());

        assertThrows(ParticipantNotFoundException.class,
                () -> participantService.deleteParticipantFromEvent(participantId, eventId));
    }

    @Test
    public void testDeleteParticipantFromEventEventNotFound()
            throws AttendeeNotFoundException, EventNotFoundException, ParticipantNotFoundException {
        when(participantRepository.findById(participantId)).thenReturn(Optional.of(mockParticipant));
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class,
                () -> participantService.deleteParticipantFromEvent(participantId, eventId));
    }

    @Test
    public void testDeleteParticipantFromEventAttendeeNotFound()
            throws AttendeeNotFoundException, EventNotFoundException, ParticipantNotFoundException {
        when(participantRepository.findById(participantId)).thenReturn(Optional.of(mockParticipant));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(mockEvent));
        when(attendeeRepository.findByEventAndParticipant(mockEvent, mockParticipant)).thenReturn(Optional.empty());

        assertThrows(AttendeeNotFoundException.class,
                () -> participantService.deleteParticipantFromEvent(participantId, eventId));
    }
}
