package ConfHandler.repositories;


import ConfHandler.model.entity.Attendee;
import ConfHandler.model.entity.Event;
import ConfHandler.model.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendeeRepository extends JpaRepository<Attendee, UUID> {

    @Query("select attendee from Attendee  attendee " +
            "where attendee.event = :event " +
            "and " +
            "attendee.participant = :participant")
    public Optional<Attendee> findByEventAndParticipant(@Param("event") Event event, @Param("participant") Participant participant);

    @Query("SELECT attendee.event.id " +
            "from Attendee attendee " +
            "where attendee.participant.id = :id ")
    List<UUID> getIdsOfUserEvents(@Param("id") UUID id);


    @Query("select e.event from Attendee e where cast(e.event.timeStart as localdate) = :date and e.participant.id = :id and e.event.session is null ")
    List<Event> getEventsByDateWithoutSessionOfUSer(LocalDate date, UUID id);
}
