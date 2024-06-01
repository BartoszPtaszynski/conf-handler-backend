package ConfHandler.Admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class EventLectureCommand {
    private String name;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private  LocalDate eventDate;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private UUID sessionId;
    private String topic;
    @JsonProperty("abstract")
    private String _abstract;
    private String description;
}
