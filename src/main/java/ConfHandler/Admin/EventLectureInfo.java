package ConfHandler.Admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
public class EventLectureInfo {
    private String id;
    private String name;
    private LocalDate eventDate;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private String sessionId;
    private String topic;
    @JsonProperty("abstract")
    private String _abstract;
    private String description;
    private  String lecturers;
}
