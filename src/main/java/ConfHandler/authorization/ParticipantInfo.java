package ConfHandler.authorization;

import ConfHandler.model.Participant;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class ParticipantInfo {
    private UUID id;
    private String name;
    private String surname;
    private String email;
    private String affiliation;

    public ParticipantInfo(UUID id, String name, String surname, String email, String affiliation) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.affiliation = affiliation;
    }

    public static ParticipantInfo getFromParticipant(Participant participant) {
        return new ParticipantInfo(
                participant.getId(),
                participant.getName(),
                participant.getSurname(),
                participant.getEmail(),
                participant.getAffiliation()
        );
    }
}
