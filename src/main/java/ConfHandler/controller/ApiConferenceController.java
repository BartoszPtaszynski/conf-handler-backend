package ConfHandler.controller;

import ConfHandler.SuccessJsonResponse;
import ConfHandler.sevice.DisplayConferenceService;
import org.hibernate.mapping.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
public class ApiConferenceController {

    @Autowired
    private DisplayConferenceService displayConferenceService;
    @GetMapping("/getTimeLineByDate")
    public ResponseEntity<?> getTimeLineByDay(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return new SuccessJsonResponse(displayConferenceService.getDayOfConference(date));
    }
}
