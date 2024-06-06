package ConfHandler.Admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class SessionCommand {
    private String name;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate sessionDate;

    private LocalTime timeStart;
    private LocalTime timeEnd;
    private String city;
    private String street;
    private String building;
    private String roomNumber;
    private UUID chairman;
}
