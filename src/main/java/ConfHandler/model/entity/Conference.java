package ConfHandler.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Conference {
    @Id
    @GeneratedValue
    UUID id;
    String name;
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDate dateStart;
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDate dateEnd;
}
