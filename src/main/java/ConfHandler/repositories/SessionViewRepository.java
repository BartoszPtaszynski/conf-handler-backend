package ConfHandler.repositories;

import ConfHandler.model.entity.SessionView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SessionViewRepository extends JpaRepository<SessionView, UUID> {

    @Query("select s from SessionView s where CAST(s.timeStart as localdate )= :date order by s.timeStart, s.name")
    List<SessionView> getSessionsByTimeStart(LocalDate date);

}
