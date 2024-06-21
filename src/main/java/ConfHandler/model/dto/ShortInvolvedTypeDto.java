package ConfHandler.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class ShortInvolvedTypeDto {
    private String name;
    private String duration;
    private LocalDate date;
    private String function;
}
