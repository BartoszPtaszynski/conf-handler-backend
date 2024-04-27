package ConfHandler.repositories;

import ConfHandler.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    @Query("select e from Event e where cast(e.timeStart as localdate) = :date and e.session is null")
    List<Event> getEventsByDateWithoutSession(LocalDate date);
}
