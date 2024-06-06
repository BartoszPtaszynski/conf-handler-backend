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
import java.util.UUID;

@RestController
public class ApiConferenceController {

    @Autowired
    private DisplayConferenceService displayConferenceService;
    @GetMapping("/getTimeLineByDate")
    public ResponseEntity<?> getTimeLineByDay(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return new SuccessJsonResponse(displayConferenceService.getDayOfConference(date,null));
    }
    @GetMapping("/getBookmarkedEvents")
    public ResponseEntity<?> getTimeLineByDayOfParticipant(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, UUID id) {
        return new SuccessJsonResponse(displayConferenceService.getDayOfConference(date,id));
    }

    @GetMapping("/conferenceDetails")
    public ResponseEntity<?> getConferenceInfo() {
        try {
            return new SuccessJsonResponse(displayConferenceService.getConferenceInfo());
        }catch (NullPointerException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }
    @GetMapping("/conferenceMetadata")
    public ResponseEntity<? > getMetadata()  {

        try {
            return new SuccessJsonResponse(displayConferenceService.getMetadata());
        }catch (NullPointerException e) {

            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }


}
