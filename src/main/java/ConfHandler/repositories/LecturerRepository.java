package ConfHandler.repositories;


import ConfHandler.model.entity.Lecture;
import ConfHandler.model.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LecturerRepository extends JpaRepository<Lecturer, UUID> {

    @Query("select lecturer.lecture from Lecturer  lecturer " +
            "where lecturer.participant.id = :id")
    List<Lecture> getLectureOfUser(@Param("id") UUID id);

}
