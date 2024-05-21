package ConfHandler.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity(name = "participant")
@NoArgsConstructor
@AllArgsConstructor

@Getter
@Setter
public class Participant {


    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String surname;
    private String email;
    private String affiliation;
    private String password;



    /*    public Set<Attendance_Event> getAttendence_event() {
        return attendence_event;
    }

    @OneToMany(mappedBy = "participant")
    private Set<Attendance_Event> attendence_event;*/

    public Participant( String name, String surname, String email, String affiliation, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.affiliation = affiliation;
        this.password = password;
    }
    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail_login() {
        return email;
    }

    public void setEmail_login(String email_login) {
        this.email = email_login;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
