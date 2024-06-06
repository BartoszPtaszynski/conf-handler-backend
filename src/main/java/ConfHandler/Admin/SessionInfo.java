package ConfHandler.Admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
public class SessionInfo {
    private UUID id;
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate sessionDate;

    private LocalTime timeStart;
    private LocalTime timeEnd;
    private String city;
    private String street;
    private String building;
    private String roomNumber;
    private UUID chairman;
}
