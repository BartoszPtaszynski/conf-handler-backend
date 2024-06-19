package ConfHandler.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class SessionShortDto {
    private String name;
    private LocalDate date;
    private String duration;
}
