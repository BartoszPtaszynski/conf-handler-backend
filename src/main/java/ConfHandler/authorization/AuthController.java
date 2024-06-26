package ConfHandler.authorization;

import ConfHandler.ErrorJsonResponse;
import ConfHandler.SuccessJsonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authenticate")
public class AuthController {
    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginCommand loginCommand) {
        try {
            ParticipantInfo participantInfo = authorizationService.login(loginCommand);
            return new SuccessJsonResponse(participantInfo);
        }catch (RuntimeException | ParticipantNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNAUTHORIZED);
        }
    }
}
