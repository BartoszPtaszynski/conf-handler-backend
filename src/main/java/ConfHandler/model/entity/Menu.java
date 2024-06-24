package ConfHandler.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "menu")
@Getter
@Setter
public class Menu {
    @Id
    @GeneratedValue
    private UUID id;
    private String header;
    @ManyToMany
    @JoinTable(
            name = "menu__menu_item",
            joinColumns = @JoinColumn(name = "id_menu"),
            inverseJoinColumns = @JoinColumn(name = "id_menu_item"))
    private List<MenuItem> menuItems;

    public List<String> getItems() {

        return  menuItems.stream().map(MenuItem::getName).toList();

    }

}
