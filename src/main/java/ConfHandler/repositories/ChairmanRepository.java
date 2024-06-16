package ConfHandler.repositories;


import ConfHandler.model.entity.Chairman;
import ConfHandler.model.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChairmanRepository extends JpaRepository<Chairman, UUID> {

}
