package ConfHandler.authorization;

import ConfHandler.model.dto.InvolvedInEvents;
import ConfHandler.model.entity.Participant;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Getter
@Setter
@Builder
public class ParticipantInfo {
    private UUID id;
    private String name;
    private String surname;
    private String email;
    private String affiliation;
    private List<UUID> bookmarkedEvents;
    private String title;
    private InvolvedInEvents involvedInEvents;
}
