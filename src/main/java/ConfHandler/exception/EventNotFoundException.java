package ConfHandler.exception;

import java.util.UUID;

public class EventNotFoundException extends Exception{
    public EventNotFoundException(UUID message) {
        super("Event with id="+message+" not found");
    }
}
