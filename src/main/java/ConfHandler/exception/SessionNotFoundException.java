package ConfHandler.exception;

import java.util.UUID;

public class SessionNotFoundException extends RuntimeException{
    public SessionNotFoundException(UUID id) {
        super("session with id="+id+" not found");
    }
}
