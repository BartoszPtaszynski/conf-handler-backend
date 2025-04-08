package ConfHandler.repositories;

import ConfHandler.model.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MenuRepository extends JpaRepository<Menu, UUID> {
}
