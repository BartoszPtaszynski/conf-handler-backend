package ConfHandler.sevice;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Slf4j
public class ParticipantService {
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private AttendeeRepository attendeeRepository;
    public void saveParticipantToEvent(UUID idParticipant, UUID idEvent) throws AttendeeAlreadyExistsException, EventNotFoundException, ParticipantNotFoundException {
        Participant participant = participantRepository.findById(idParticipant).orElseThrow(() -> new ParticipantNotFoundException(idParticipant));
        Event event = eventRepository.findById(idEvent).orElseThrow(() -> new EventNotFoundException(idEvent));

        attendeeRepository.findByEventAndParticipant(event, participant).ifPresent((attendee) -> {
            throw new AttendeeAlreadyExistsException(idEvent, idParticipant);
        });

        attendeeRepository.save(new Attendee(event, participant));
        log.info(participant.getName() + " " + participant.getSurname() + " signed up to event with id " + idEvent);
    }
    public void deleteParticipantFromEvent(UUID idParticipant, UUID idEvent ) throws ParticipantNotFoundException, EventNotFoundException, AttendeeNotFoundException {
        Participant participant = participantRepository.findById(idParticipant).orElseThrow(()->new ParticipantNotFoundException(idParticipant));
        Event event = eventRepository.findById(idEvent).orElseThrow(()->new EventNotFoundException(idEvent));
        Attendee attendee= attendeeRepository.findByEventAndParticipant(event,participant).orElseThrow(()->new AttendeeNotFoundException(idEvent,idParticipant));
        attendeeRepository.delete(attendee);
        log.info(participant.getName()+" "+participant.getSurname() +"removed from event with id "+idEvent);
    }
}
