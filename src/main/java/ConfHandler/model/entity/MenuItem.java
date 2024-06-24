package ConfHandler.model.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "menu_item")
@Getter
@Setter
public class MenuItem {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
}
