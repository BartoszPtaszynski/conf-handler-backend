package ConfHandler.authorization;

public class ParticipantNotFoundException extends Exception{
    public ParticipantNotFoundException(String email) {
        super(email + " not found");
    }
}
