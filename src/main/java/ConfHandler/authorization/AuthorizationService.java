package ConfHandler.authorization;

import ConfHandler.model.entity.Participant;
import ConfHandler.repositories.AttendeeRepository;
import ConfHandler.repositories.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AuthorizationService {
    @Autowired
    ParticipantRepository participantRepository;
    @Autowired
    AttendeeRepository attendeeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ParticipantInfo login(LoginCommand loginCommand) throws ParticipantNotFoundException {

        Participant participant = participantRepository
                .findByEmail(loginCommand.getEmail())
                .orElseThrow(()->new ParticipantNotFoundException(loginCommand.getEmail()) );

        if(! passwordEncoder.matches(loginCommand.getPassword(),participant.getPassword())) {
            throw new RuntimeException();
        }
        List<UUID> eventsIds = attendeeRepository.getIdsOfUserEvents(participant.getId());
        return ParticipantInfo.builder()
                .id(participant.getId())
                .name(participant.getName())
                .surname(participant.getSurname())
                .email(participant.getEmail())
                .affiliation(participant.getAffiliation())
                .bookmarkedEvents(eventsIds)
                .build();
    }
}
