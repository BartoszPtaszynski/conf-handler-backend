package ConfHandler.authorization;

import ConfHandler.model.dto.ShortInvolvedTypeDto;
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
    private List<ShortInvolvedTypeDto> involvedInEvents;
}
