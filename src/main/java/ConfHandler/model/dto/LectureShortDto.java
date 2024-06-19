package ConfHandler.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class LectureShortDto {
    private LocalDate date;
    private String duration;
    private String sessionName;
    private String topic;
}
