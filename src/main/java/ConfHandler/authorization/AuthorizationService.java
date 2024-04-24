package ConfHandler.authorization;

import ConfHandler.model.Participant;
import ConfHandler.repositories.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    @Autowired
    ParticipantRepository participantRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ParticipantInfo login(LoginCommand loginCommand) throws ParticipantNotFoundException {

        Participant participant = participantRepository
                .findByEmail(loginCommand.getEmail())
                .orElseThrow(()->new ParticipantNotFoundException(loginCommand.getEmail()) );

        if(! passwordEncoder.matches(loginCommand.getPassword(),participant.getPassword())) {
            throw new RuntimeException();
        }
        return ParticipantInfo.getFromParticipant(participant);
    }
}
