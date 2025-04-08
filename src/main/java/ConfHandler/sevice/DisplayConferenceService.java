package ConfHandler.sevice;

import ConfHandler.model.converter.EventViewToEventDtoConverter;
import ConfHandler.model.converter.SessionViewToSessionDtoConverter;
import ConfHandler.model.dto.ConferenceInfoDto;
import ConfHandler.model.dto.MenuDto;
import ConfHandler.model.dto.MetadataDto;
import ConfHandler.model.dto.TimeItem;
import ConfHandler.model.entity.Conference;
import ConfHandler.repositories.ConferenceRepository;
import ConfHandler.repositories.EventViewRepository;
import ConfHandler.repositories.SessionViewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class DisplayConferenceService {

    @Autowired
    private SessionViewRepository sessionViewRepository;
    @Autowired
    private EventViewRepository eventViewRepository;
    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private MenuService menuService;

    @Autowired
    private SessionViewToSessionDtoConverter sessionViewToSessionDtoConverter;
    @Autowired
    private EventViewToEventDtoConverter eventViewToEventDtoConverter;

    @Cacheable(value = "dayOfConferenceCache", key = "#date")
    public List<?> getDayOfConference(LocalDate date) {
        log.info("getDayOfConference({})", date);
        List<Object> listOfAllEvents = new ArrayList<>();

        Map<UUID, MenuDto> menuDtoMap = menuService.retrieveMenuMap();
        
        listOfAllEvents.addAll(sessionViewToSessionDtoConverter.convert(sessionViewRepository.getSessionsByTimeStart(date), menuDtoMap));
        listOfAllEvents.addAll(eventViewToEventDtoConverter.convert(eventViewRepository.getEventsByDateWithoutSession(date), menuDtoMap));
        
        listOfAllEvents.sort(Comparator.comparing(o -> ((TimeItem) o).getDuration()));

        return listOfAllEvents;
    }

    public ConferenceInfoDto getConferenceInfo() {
        return conferenceRepository.getConferenceInfo().orElseThrow(() -> new NullPointerException("conference not found"));
    }

    public MetadataDto getMetadata() {
        Conference conference = conferenceRepository.getConference().orElseThrow(() -> new NullPointerException("conference not found"));
        return MetadataDto.builder()
                .contactEmail(conference.getContactEmail())
                .contactCellNumber(conference.getContactCellNumber())
                .contactWebsite(conference.getContactWebsite())
                .contactLandlineNumber(conference.getContactLandlineNumber()).build()
                ;

    }
}
