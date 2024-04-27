package ConfHandler.repositories;

import ConfHandler.model.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ParticipantRepository extends JpaRepository<Participant, UUID> {


    Optional<Participant> findByEmail(String email);
}
