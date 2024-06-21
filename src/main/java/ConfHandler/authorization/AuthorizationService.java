package ConfHandler.authorization;

import ConfHandler.model.dto.ShortInvolvedTypeDto;
import ConfHandler.model.entity.Participant;
import ConfHandler.repositories.AttendeeRepository;
import ConfHandler.repositories.ChairmanRepository;
import ConfHandler.repositories.LecturerRepository;
import ConfHandler.repositories.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service

public class AuthorizationService {
    @Autowired
    ParticipantRepository participantRepository;
    @Autowired
    AttendeeRepository attendeeRepository;
    @Autowired
    LecturerRepository lecturerRepository;
    @Autowired
    ChairmanRepository chairmanRepository;
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
        List<ShortInvolvedTypeDto> involvedEvents=new LinkedList<>();

        involvedEvents.addAll( lecturerRepository.getLectureOfUser(participant.getId()).stream()
                .map(lecture -> ShortInvolvedTypeDto.builder()
                        .date(lecture.getEvent().getTimeStart().toLocalDate())
                        .duration(lecture.getEvent().getDuration())
                        .name((lecture.getEvent().getSession()==null?"":lecture.getEvent().getSession().getName()+" - ")+lecture.getTopic())
                        .function("lecturer").build()
                ).toList());
        involvedEvents.addAll( chairmanRepository.getChairmanOfLecture(participant.getId()).stream()
                .map(lecture -> ShortInvolvedTypeDto.builder()
                        .date(lecture.getEvent().getTimeStart().toLocalDate())
                        .duration(lecture.getEvent().getDuration())
                        .name((lecture.getEvent().getSession()==null?"":lecture.getEvent().getSession().getName()+" - ")+lecture.getTopic())
                        .function("chairman of lecture").build()
                ).toList());
        involvedEvents.addAll(chairmanRepository.getChairmanOfSession(participant.getId()).stream()
                .map(session -> ShortInvolvedTypeDto.builder()
                        .date(session.getTimeStart().toLocalDate())
                        .duration(session.getDuration())
                        .name(session.getName())
                        .function("chairman of session")
                        .build()
                ).toList());

        involvedEvents.sort(Comparator.comparing(event->event.getDuration()));
        return ParticipantInfo.builder()
                .id(participant.getId())
                .name(participant.getName())
                .surname(participant.getSurname())
                .email(participant.getEmail())
                .affiliation(participant.getAffiliation())
                .bookmarkedEvents(eventsIds)
                .title(participant.getTitleManual())
                .involvedInEvents(involvedEvents)
                .build();
    }
}
