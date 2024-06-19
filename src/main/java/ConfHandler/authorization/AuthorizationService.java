package ConfHandler.authorization;

import ConfHandler.model.dto.InvolvedInEvents;
import ConfHandler.model.dto.LectureShortDto;
import ConfHandler.model.dto.SessionShortDto;
import ConfHandler.model.entity.Lecture;
import ConfHandler.model.entity.Participant;
import ConfHandler.repositories.AttendeeRepository;
import ConfHandler.repositories.ChairmanRepository;
import ConfHandler.repositories.LecturerRepository;
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

        List<LectureShortDto> lecturerOfLectures = lecturerRepository.getLectureOfUser(participant.getId()).stream()
                .map(lecture -> LectureShortDto.builder()
                        .date(lecture.getEvent().getTimeStart().toLocalDate())
                        .duration(lecture.getEvent().getDuration())
                        .sessionName(lecture.getEvent().getSession()==null?null:lecture.getEvent().getSession().getName())
                        .topic(lecture.getTopic()).build()
                ).toList();
        List<LectureShortDto> lecturesChairman = chairmanRepository.getChairmanOfLecture(participant.getId()).stream()
                .map(lecture -> LectureShortDto.builder()
                        .date(lecture.getEvent().getTimeStart().toLocalDate())
                        .duration(lecture.getEvent().getDuration())
                        .sessionName(lecture.getEvent().getSession()==null?null:lecture.getEvent().getSession().getName())
                        .topic(lecture.getTopic()).build()
                ).toList();
        List<SessionShortDto> sessionChairman =  chairmanRepository.getChairmanOfSession(participant.getId()).stream()
                .map(session -> SessionShortDto.builder()
                        .date(session.getTimeStart().toLocalDate())
                        .duration(session.getDuration())
                        .name(session.getName())
                        .build()
                ).toList();


        InvolvedInEvents involvedInEvents = InvolvedInEvents.builder()
                .chairmanOfLectures(lecturesChairman)
                .lecturerOfLectures(lecturerOfLectures)
                .chairmanOfSessions(sessionChairman)
                .build();

        return ParticipantInfo.builder()
                .id(participant.getId())
                .name(participant.getName())
                .surname(participant.getSurname())
                .email(participant.getEmail())
                .affiliation(participant.getAffiliation())
                .bookmarkedEvents(eventsIds)
                .title(participant.getTitleManual())
                .involvedInEvents(involvedInEvents)
                .build();
    }
}
