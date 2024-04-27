package ConfHandler.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity(name="admin")
@NoArgsConstructor
@Getter
@Setter
public class Admin {
    @Id
    @GeneratedValue
    private UUID id;
    private String login;
    private String pass;

    public Admin(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    public void setPass(String pass){
            this.pass = pass;
    }
}
