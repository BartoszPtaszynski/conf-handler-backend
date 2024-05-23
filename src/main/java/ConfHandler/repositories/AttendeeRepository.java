package ConfHandler.repositories;


import ConfHandler.model.entity.Attendee;
import ConfHandler.model.entity.Event;
import ConfHandler.model.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AttendeeRepository extends JpaRepository<Attendee, UUID> {

    @Query("select attendee from Attendee  attendee " +
            "where attendee.event = :event " +
            "and " +
            "attendee.participant = :participant")
    public Optional<Attendee> findByEventAndParticipant(@Param("event") Event event, @Param("participant") Participant participant);
}
