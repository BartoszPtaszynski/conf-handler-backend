package ConfHandler.repositories;

import ConfHandler.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TypeRepository extends JpaRepository<Type, UUID> {

}
