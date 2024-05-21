package ConfHandler.Admin;

import ConfHandler.model.entity.Participant;
import ConfHandler.repositories.ParticipantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class AdminService {
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void addParticipants(List<ParticipantCommand> participantCommand) {
        List<Participant> participants = participantCommand.stream()
                .map(participant ->
                        new Participant(participant.getName(),participant.getSurname(),participant.getEmail(),participant.getAffiliation(),generatePassword()))
                .toList();

        participantRepository.saveAll(participants);
    }
    private String generatePassword() {
        Random random = new Random();
        String randomDigits = IntStream.range(0, 8)
                .mapToObj(i -> random.nextInt(10))
                .map(String::valueOf)
                .collect(Collectors.joining());
        log.info(randomDigits);
        return passwordEncoder.encode(randomDigits);
    }

    public List<ParticipantInfo> getParticipants() {
        return participantRepository.findAll().stream()
                .map(participant -> ParticipantInfo.builder()
                        .id(participant.getId().toString())
                        .name(participant.getName())
                        .surname(participant.getSurname())
                        .email(participant.getEmail())
                        .affiliation(participant.getAffiliation()== null? "":participant.getAffiliation())
                        .title("title").build())
                .toList();
    }
}
