package ConfHandler.sevice;

import ConfHandler.model.dto.EventDto;
import ConfHandler.model.dto.SessionDto;
import ConfHandler.repositories.AttendeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class DisplayConferenceUserService {

    @Autowired
    private AttendeeRepository attendeeRepository;

    @Autowired
    private DisplayConferenceService displayConferenceService;

    public List<?> getDayOfConferenceForUser(LocalDate date, UUID id) {
        log.info("getDayOfConferenceForUser({}, {})", date, id);
        final Set<UUID> eventIdsOfAttendee = new HashSet<>(attendeeRepository.getIdsOfUserEventsByDate(date, id));

        List<Object> listOfAllEvents = new ArrayList<>();
        displayConferenceService.getDayOfConference(date).forEach(o -> {
            if (o instanceof SessionDto sessionDto) {
                List<EventDto> userEvents = sessionDto.getEventList().stream()
                        .filter(e -> eventIdsOfAttendee.contains(e.getId()))
                        .toList();
                if (!userEvents.isEmpty()) {
                    listOfAllEvents.add(sessionDto.copy(userEvents));
                }
            } else if (eventIdsOfAttendee.contains(((EventDto) o).getId())) {
                listOfAllEvents.add(o);
            }
        });

        return listOfAllEvents;
    }

}
