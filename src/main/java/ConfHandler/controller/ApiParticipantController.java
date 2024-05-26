package ConfHandler.controller;

import ConfHandler.SuccessJsonResponse;
import ConfHandler.exception.AttendeeAlreadyExistsException;
import ConfHandler.exception.AttendeeNotFoundException;
import ConfHandler.exception.EventNotFoundException;
import ConfHandler.exception.ParticipantNotFoundException;
import ConfHandler.repositories.ParticipantRepository;
import ConfHandler.sevice.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController()
@RequestMapping("/participantToEvent")
public class ApiParticipantController {
    @Autowired
    private ParticipantService participantService;

    @PostMapping()
    public ResponseEntity<?> signParticipantToEvent(@RequestParam UUID idEvent,UUID idParticipant) {
        try {
            participantService.saveParticipantToEvent(idParticipant,idEvent);
            return new SuccessJsonResponse("successfully added" );
        } catch (EventNotFoundException | ParticipantNotFoundException| AttendeeAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
    @DeleteMapping()
    public ResponseEntity<?> removeParticipantFromEvent(@RequestParam UUID idEvent,UUID idParticipant) {
        try {
            participantService.deleteParticipantFromEvent(idParticipant,idEvent);
            return new SuccessJsonResponse("successfully removed" );
        } catch (EventNotFoundException | ParticipantNotFoundException | AttendeeNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }


}
