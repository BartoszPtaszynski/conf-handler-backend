package ConfHandler.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;
@AllArgsConstructor
@Getter
@Setter
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventDto{
    private UUID id;
    private String name;
    private String duration;
    private String description;
    private MenuDto menu;

}
