package ConfHandler.repositories;

import ConfHandler.model.entity.Event;
import ConfHandler.model.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    @Query("select e from Event e where cast(e.timeStart as localdate) = :date and e.session is null")
    List<Event> getEventsByDateWithoutSession(LocalDate date);

    @Query(value = """
            SELECT 
                e.id,
                e.name,
                e.time_start,
                e.time_end,
                e.description,
                s.id as session_id,
                l.topic,
                l.abstract,
                (SELECT string_agg(p.name || ' ' || p.surname, ', ')
                 FROM lecturer lec
                 JOIN participant p ON lec.participant_fk = p.id
                 WHERE lec.lecture_fk = l.id) as lecturers,
                (SELECT string_agg(p.name || ' ' || p.surname, ', ')
                 FROM chairman c
                 JOIN participant p ON c.participant_fk = p.id
                 WHERE c.lecture_fk = l.id) as chairmen
            FROM event e
            LEFT JOIN session s ON e.session_fk = s.id
            LEFT JOIN lecture l ON l.event_fk = e.id
            ORDER BY e.time_start
            """, nativeQuery = true)
    List<Object[]> findAllEventsWithDetails();
}
