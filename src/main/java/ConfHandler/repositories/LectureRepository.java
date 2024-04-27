package ConfHandler.repositories;

import ConfHandler.model.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LectureRepository extends JpaRepository<Lecture, UUID> {

    Lecture getByEvent_Id(UUID id);


}
