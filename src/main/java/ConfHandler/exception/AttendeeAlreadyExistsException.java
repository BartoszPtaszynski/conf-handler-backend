package ConfHandler.exception;

import java.util.UUID;

public class AttendeeAlreadyExistsException extends RuntimeException{
    public AttendeeAlreadyExistsException(UUID idEvent, UUID idParticipant) {
        super("Attendee with id of event: "+idEvent+" and with it of participant: "+idParticipant+" already exists");
    }
}
