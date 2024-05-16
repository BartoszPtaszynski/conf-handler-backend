package ConfHandler.Admin;

import ConfHandler.SuccessJsonResponse;
import ConfHandler.model.entity.Participant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("admin")
@Slf4j
public class AdminController {

    @PostMapping("/addParticipants")
    public SuccessJsonResponse addParticipants(@RequestBody List<ParticipantCommand> command) {
        command.forEach(
                s->log.info(String.valueOf(s)));
        return new SuccessJsonResponse("Added "+command.size());
    }
}
