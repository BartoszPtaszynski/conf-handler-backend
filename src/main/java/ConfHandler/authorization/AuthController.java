package ConfHandler.authorization;

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
            return new ResponseEntity<>(participantInfo, HttpStatus.OK);
        }catch (RuntimeException | ParticipantNotFoundException e) {
            return new ResponseEntity<>("invalid email or password",HttpStatus.UNAUTHORIZED);
        }
    }
}
