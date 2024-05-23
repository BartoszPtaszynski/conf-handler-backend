package ConfHandler.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;


@Getter
@Setter

public class LectureDto extends EventDto{

    @JsonProperty("abstract")
    private String _abstract;
    private String lecturer;
    private String topic;
    private UUID idEvent;

    public LectureDto(UUID id, String name, String duration, String _abstract, String lecturer, String topic,UUID idEvent) {
        super(id, name, duration);
        this._abstract = _abstract;
        this.lecturer = lecturer;
        this.topic = topic;
        this.idEvent = idEvent;
    }
}
