package ConfHandler.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ConferenceInfoDto {
    private String name;
    private OffsetDateTime startDate;
    private  OffsetDateTime endDate;
}
