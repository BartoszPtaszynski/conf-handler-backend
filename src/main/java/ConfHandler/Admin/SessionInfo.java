package ConfHandler.Admin;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class SessionInfo {
    private String id;
    private String name;
    private LocalDate sessionDate;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private String city;
    private String street;
    private String building;
    private String roomNumber;
}
