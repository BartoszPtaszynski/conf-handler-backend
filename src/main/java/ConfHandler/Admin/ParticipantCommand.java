package ConfHandler.Admin;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ParticipantCommand {
    private String name;
    private String surname;
    private String email;
    private String affiliation;
    private String title;
}
