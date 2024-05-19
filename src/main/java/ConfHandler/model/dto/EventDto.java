package ConfHandler.model.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;
@AllArgsConstructor
@Getter
@Setter
@Data

public class EventDto{
    private UUID id;
    private String name;
    private String duration;


}
