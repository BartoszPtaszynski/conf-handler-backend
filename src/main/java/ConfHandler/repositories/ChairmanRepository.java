package ConfHandler.repositories;


import ConfHandler.model.entity.Chairman;
import ConfHandler.model.entity.Lecture;
import ConfHandler.model.entity.Lecturer;
import ConfHandler.model.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ChairmanRepository extends JpaRepository<Chairman, UUID> {


    @Query("select chairman.lecture from Chairman chairman where chairman.participant.id=:id and chairman.session is null" )
    List<Lecture> getChairmanOfLecture(@Param("id") UUID id);
    @Query("select chairman.session from Chairman chairman where chairman.participant.id=:id and chairman.lecture is null")
    List<Session> getChairmanOfSession(@Param("id") UUID id);

}
