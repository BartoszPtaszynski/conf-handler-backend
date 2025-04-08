package ConfHandler.repositories;

import ConfHandler.model.entity.EventView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface EventViewRepository extends JpaRepository<EventView, UUID> {

    @Query("select e from EventView e where e.session is null and CAST(e.eventTimeStart as localdate )= :date order by e.eventTimeStart, e.eventName")
    List<EventView> getEventsByDateWithoutSession(LocalDate date);

}
