package ConfHandler.exception;

import java.util.UUID;

public class AttendeeNotFoundException extends Exception{
    public AttendeeNotFoundException(UUID idEvent,UUID idParticipant) {
        super("Attendee with id of event: "+idEvent+" and with it of participant: "+idParticipant+" not found");
    }
}
