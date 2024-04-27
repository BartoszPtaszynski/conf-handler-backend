package ConfHandler.repositories;

import ConfHandler.model.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session,UUID> {

    @Query("select s from Session s where CAST(s.timeStart as localdate )= :date")
    List<Session> getSessionsByTimeStart(LocalDate date);
}
