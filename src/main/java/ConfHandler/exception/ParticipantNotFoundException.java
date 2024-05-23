package ConfHandler.exception;

import java.util.UUID;

public class ParticipantNotFoundException extends Exception{
    public ParticipantNotFoundException(UUID message) {
        super("Participant with id="+message+" not found");
    }
}
