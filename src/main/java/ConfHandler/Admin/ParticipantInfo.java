package ConfHandler.Admin;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
public class ParticipantInfo {
    private String id;
    private String name;
    private String surname;
    private String email;
    private String affiliation;
    private String title;
}
